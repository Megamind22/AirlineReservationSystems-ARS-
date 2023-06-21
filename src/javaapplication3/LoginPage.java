package javaapplication3;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LoginPage extends JFrame implements ActionListener
        
{
    
    
    
    private static ExecutorService service;
    private Server oneServer;
    private static int coreCount=Runtime.getRuntime().availableProcessors();
    
    private File usersDB = new java.io.File("D:\\my materials\\LEVEL4\\projects\\parallel\\final\\JavaApplication3\\users.txt");
    private JLabel userNameLabel,passLabel,
            userNameLabelRegister,passLabelRegister;
    private JTextField userNameInput,
                userNameInputRegister;
    private JPasswordField passInput,
                    passInputRegister;
    private JButton loginButton,
             registerButton,registerEnsureButton,exitRegisterBtn;
    private JFrame RegisterFrame=new JFrame();

                    
	public LoginPage()
	{

            oneServer = new Server();
            service = Executors.newFixedThreadPool(coreCount);

            this.setTitle("LOGIN");
            this.setSize(400, 300);
            this.setLocation(600, 200);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setLayout(null);
            
            userNameLabel = new JLabel("UserName");
            passLabel = new JLabel("Password");
            userNameInput = new JTextField();
            passInput = new JPasswordField();
            loginButton = new JButton("Login");
            registerButton = new JButton("Register");
            
            userNameLabel.setBounds(50, 20, 200, 100);
            userNameInput.setBounds(150, 55, 150, 30);
            passLabel.setBounds(50, 90, 200, 100);
            passInput.setBounds(150, 125, 150, 30);
            loginButton.setBounds(90, 200, 100, 30);
            registerButton.setBounds(210, 200, 100, 30);
            
            this.add(userNameLabel);
            this.add(userNameInput);
            this.add(passLabel);
            this.add(passInput);
            this.add(loginButton);
            this.add(registerButton);

            this.setVisible(true);
            
            loginButton.addActionListener(this);
            registerButton.addActionListener(this);

 
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String userName = userNameInput.getText();
            String password = passInput.getText();
            try {
                ArrayList<String> userNameFromFile = GetUsersNamefromFile();
                ArrayList<String> passwordFromFile = GetPasswordfromFile();

                if (userNameFromFile.contains(userName) && passwordFromFile.contains(password)) {
                    service.execute(new Client(oneServer));
                } else {
                    JOptionPane.showMessageDialog(null, "invalid Login", "Message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (FileNotFoundException ex) {
                System.out.println("error FileNotFoundException");
            }
            userNameInput.setText("");
            passInput.setText("");

        }
        if (e.getSource() == registerButton) {

            RegisterForm();

        }
        if (e.getSource() == registerEnsureButton) {
            String userNameRegister = userNameInputRegister.getText();
            String passRegister = passInputRegister.getText();

            if (userNameRegister.equals("") && passRegister.equals("")) {

                JOptionPane.showMessageDialog(null, "invalid register", "Message", JOptionPane.ERROR_MESSAGE);

            } else {
                service.execute(new Client(oneServer));
                RegisterFrame.dispose();
                
                if (!usersDB.exists()) {
                    System.out.println("the file not exit ");
                } else {
                    PrintWriter newUser;
                    try {
                        ArrayList<String> allDataInFile = GetFiledata();
                        newUser = new PrintWriter(usersDB);
                        allDataInFile.add("username: " + userNameRegister + " password: " + passRegister);
                        for (int i = 0; i < allDataInFile.size(); i++) {
                            newUser.println(allDataInFile.get(i));
                        }
                        newUser.close();
                    } catch (FileNotFoundException ex) {
                        System.out.println("error FileNotFoundException");
                    }

                }


         }
             userNameInputRegister.setText("");
             passInputRegister.setText("");

      }
     if(e.getSource()==exitRegisterBtn){
        RegisterFrame.dispose();
    
     }
    }
    protected void RegisterForm(){
        RegisterFrame = new JFrame();
        
        RegisterFrame.setTitle("REGISTER");
        RegisterFrame.setSize(400, 300);
        RegisterFrame.setLocation(600, 200);
        RegisterFrame.setResizable(false);
        RegisterFrame.setLayout(null);
        
        userNameLabelRegister = new JLabel("Enter UserName");
        passLabelRegister = new JLabel("Enter Password");
        userNameInputRegister = new JTextField();
        passInputRegister = new JPasswordField();
        registerEnsureButton = new JButton("Confirm");
        exitRegisterBtn = new JButton("Exit");
        
        userNameLabelRegister.setBounds(50, 20, 200, 100);
        userNameInputRegister.setBounds(190, 55, 150, 30);
        passLabelRegister.setBounds(50, 90, 200, 100);
        passInputRegister.setBounds(190, 125, 150, 30);
        registerEnsureButton.setBounds(90, 200, 100, 30);
        exitRegisterBtn.setBounds(210, 200, 100, 30);
        
        RegisterFrame.add(userNameLabelRegister);
        RegisterFrame.add(userNameInputRegister);
        RegisterFrame.add(passLabelRegister);
        RegisterFrame.add(passInputRegister);
        RegisterFrame.add(registerEnsureButton);
        RegisterFrame.add(exitRegisterBtn);
        
        registerEnsureButton.addActionListener(this);
        exitRegisterBtn.addActionListener(this);
        
        this.addWindowListener(new WindowAdapter() {
                public void WindowClosing(WindowEvent e){
                service.shutdown();
                }
            });
        RegisterFrame.setVisible(true);


}
    
    public ArrayList<String> GetUsersNamefromFile() throws FileNotFoundException {
        ArrayList<String> UsersName = new ArrayList<String>();

        Scanner values = new Scanner(usersDB);
        if (!usersDB.exists()) {
            System.out.println("the file not exit ");
        } else {
            while (values.hasNext()) {
                String dat = values.next();
                if (dat.contains("username")) {
                    String details = values.next();
                    UsersName.add(details);
                }
            }
            values.close();
        }
        return UsersName;
    }

    public ArrayList<String> GetPasswordfromFile() throws FileNotFoundException {

        ArrayList<String> Password = new ArrayList<String>();

        if (!usersDB.exists()) {
            System.out.println("the file not exit ");
        } else {
            Scanner values = new Scanner(usersDB);
            while (values.hasNext()) {
                String dat = values.next();
                if (dat.contains("password")) {
                    String details = values.next();
                    Password.add(details);
                }
            }

            values.close();
        }
        return Password;
    }

    public ArrayList<String> GetFiledata() throws FileNotFoundException {

        ArrayList<String> data = new ArrayList<String>();
        Scanner values = new Scanner(usersDB);
        if (!usersDB.exists()) {
            System.out.println("the file not exit ");
        } else {
            while (values.hasNext()) {
                data.add(values.nextLine());
            }
        }

        values.close();
        return data;
    }

    
}


