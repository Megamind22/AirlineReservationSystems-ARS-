package javaapplication3;


import java.util.concurrent.ExecutorService;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.*;


public class Client extends JFrame implements Runnable
{	
        private JButton reserveBtn,avaliableBtn;
        private JLabel reserveLabel;
        private JTextField resrveInput;
	private  JTextArea log;       

	private  SeatPlanPanel seatPlan;
        
	private static FlightBuffer server;
        	
	
	public Client(FlightBuffer flight)
	{		

            log = new JTextArea();

            server = flight;
            server.ClientsLog(log);

            JButtonInit();
            JTextAreaInit();
            seatPlanInit();
            JFrameInit();
                
	}
	
	private void JButtonInit()
	{

		/************** BtnRegisterSet **************/
                reserveLabel=new JLabel("Enter Seat Number");
                resrveInput=new JTextField();
                reserveBtn=new JButton("Reserve");
                avaliableBtn=new JButton("Avaliable Seats");
                
		reserveLabel.setBounds(30, 220, 120, 100);		
                resrveInput.setBounds(150, 255, 130, 30);
                reserveBtn.setBounds(290, 255, 100, 30);
                avaliableBtn.setBounds(400, 255, 150, 30);
                
		add(reserveLabel);
                add(resrveInput);
                add(reserveBtn);
                add(avaliableBtn);
            


	}

	private void seatPlanInit() {
                /* SeatPlan Form */    
		seatPlan = new SeatPlanPanel(server.seatCount());
		seatPlan.setBounds(10, 10, 530, 200);
		add(seatPlan);
	}
	
	private void JTextAreaInit()
	{
		/************ log ************/
		log.setBounds(10, 340, 540, 160);
		log.setFont(new Font("Consolas", Font.PLAIN, 14));
		log.setBackground(Color.white);
		log.setForeground(Color.black);
		log.setEditable(false);
		add(log);
	}
	
	private void JFrameInit()
	{
		setTitle("Airlane");
		setSize(580, 580);
		setResizable(false);
		setLayout(null);
		setVisible(true);
	}
	
	/*** Events ***/
	
	
        


    public int getSetNumber() {
        String SetNumber = resrveInput.getText();
        boolean check = false;
        if (SetNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Seat Number", "Message", JOptionPane.ERROR_MESSAGE);
        } else {
            for (int i = 1; i < 25; i++) {
                if (Integer.parseInt(SetNumber) == i) {
                    check = true;
                    return Integer.parseInt(SetNumber);
                }
            }

        }
        if (check == false && !(SetNumber.equals(""))) {
            JOptionPane.showMessageDialog(null, "Invalid Seat Number", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;

    }
   

    public void updateSeatPlan(int seatNo) {

        seatPlan.bookSeat(seatNo);

    }

    public void updateAvaliableSeats(HashMap<Integer, Long> seats) {

        seatPlan.AvaliableSeats(seats);

    }

    @Override
    public void run() {
        ReverseAction();
        AvaliableSeatsAction();
    }
    private void ReverseAction() {
        String name = "client: " + Thread.currentThread().getName();

        reserveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (e.getSource() == reserveBtn) {

                        if (getSetNumber() != 0) {

                            try {
                                Object[] hisOwnGui = server.makeReservation(name, getSetNumber(), Thread.currentThread().getId());
                                log.setText((String) hisOwnGui[1]);
                                if (hisOwnGui[0].equals("true")) {
                                    updateSeatPlan(getSetNumber());
                                }

                            } catch (InterruptedException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }

                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Seat Number", "Error", JOptionPane.ERROR_MESSAGE);
                }
                resrveInput.setText("");
            }
        });
    }

    private void AvaliableSeatsAction() {
        String name = "client: " + Thread.currentThread().getName();

        avaliableBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (e.getSource() == avaliableBtn) {

                        try {
                            List<Object> sa = server.seeAvailableSeats(name);
                            updateAvaliableSeats((HashMap<Integer, Long>) sa.get(1));
                            log.setText((String) sa.get(0));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid Seat Number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}