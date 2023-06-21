package javaapplication3;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.*;
import java.util.HashMap;

public class SeatPlanPanel extends JPanel {
    private final int seatCount;
    private final List<JLabel> seatButtons = new ArrayList<JLabel>();
    Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);

    public SeatPlanPanel(int seatCount) {
        this.seatCount = seatCount;

        this.setLayout(new GridLayout(4, 6, 10, 10));
        for (int i = 0; i < this.seatCount; i++) {
            String seatNo = Integer.toString(i + 1);
            JLabel seat = new JLabel(seatNo);
            seat.setHorizontalAlignment(JLabel.CENTER);
            seat.setVerticalAlignment(JLabel.CENTER);
            seat.setFont(new Font("Serif", Font.BOLD, 30));
            seat.setBackground(Color.GREEN);
            seat.setBorder(border);
            seatButtons.add(seat);
            this.add(seat);
        }
    }

    public void bookSeat(int seatNo) {
        seatButtons.get(seatNo - 1).setOpaque(true);
        seatButtons.get(seatNo - 1).setBorder(redBorder);
        seatButtons.get(seatNo - 1).setBackground(Color.RED);
        seatButtons.get(seatNo - 1).setForeground(Color.WHITE);   
    }
    public void AvaliableSeats(HashMap<Integer,Long> seats){

        for (int i = 1; i <= seats.size(); i++) {
            if (seats.get(i) == 0L) {
                seatButtons.get(i - 1).setOpaque(true);
                seatButtons.get(i - 1).setBackground(Color.GREEN);
                seatButtons.get(i - 1).setForeground(Color.WHITE);

            } else {
                bookSeat(i);
            }

        }
       
    }

    public void cancelSeat(int seatNo) {
        seatButtons.get(seatNo - 1).setBorder(border);
    }
}
