package com.shopperStore.view;

import com.shopperStore.controller.UserController;
import com.shopperStore.model.user.Customer;
import com.shopperStore.model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupPage extends JFrame {

	private JPanel contentPane;
	private JTextField textEmail;
	private JLabel lblFullName;
	private JTextField textName;
	private JLabel lblAddress;
	private JTextField textAddress;
	private JLabel lbWelcome;
	private JButton btnLogin;
	private JLabel lblNewLabel_2;
	private JPasswordField textPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignupPage frame = new SignupPage();
					frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("View error: " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame
	 */
	public SignupPage() {
		setBackground(Color.LIGHT_GRAY);
		setTitle("SIGN UP PAGE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 611);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("SMART SHOPPERS STORE");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBounds(242, 20, 352, 28);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("Email Address");
		lblNewLabel.setBounds(260, 63, 226, 28);
		contentPane.add(lblNewLabel);
		
		textEmail = new JTextField();
		textEmail.setBounds(250, 85, 359, 38);
		contentPane.add(textEmail);
		textEmail.setColumns(10);
		
		lblFullName = new JLabel("Name");
		lblFullName.setBounds(260, 128, 226, 28);
		contentPane.add(lblFullName);
		
		textName = new JTextField();
		textName.setColumns(10);
		textName.setBounds(250, 148, 359, 38);
		contentPane.add(textName);
		
		lblAddress = new JLabel("Address");
		lblAddress.setBounds(260, 220, 226, 28);
		contentPane.add(lblAddress);
		
		textAddress = new JTextField();
		textAddress.setColumns(10);
		textAddress.setBounds(250, 243, 359, 38);
		contentPane.add(textAddress);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(260, 321, 226, 28);
		contentPane.add(lblPassword);
		
		lbWelcome = new JLabel("");
		lbWelcome.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lbWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lbWelcome.setForeground(Color.RED);
		lbWelcome.setBounds(190, 509, 474, 51);
		contentPane.add(lbWelcome);
		
		JButton btnSignUp = new JButton("SIGN UP");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email, name, password, address;

				// base case
			
				

				email = textEmail.getText();
				name = textName.getText();
				address = textAddress.getText();
				password = textPassword.getText();


				UserController controller = new UserController();
				Customer newCustomer = new Customer(email, name, password, address);
				
				if(email.length() < 1 || password.length() < 1) {
					lbWelcome.setText("Fill in required fields!!!");
				}
				else if(controller == null)
					System.out.println("couldn't establish connection to controller");
				else if(controller.signup(newCustomer)) {// pass it to controller to do some input checks for security reasons before passing it to DB
					lbWelcome.setText("Welcome New Customer!!!");
					homeButton(e, newCustomer); // call home page
				}
				else if(controller.getErrMessage().toLowerCase().contains("duplicate")) {
					lbWelcome.setText("User already exists!");
					System.out.println("reachable");
				}
				else if(controller.getErrMessage() != null)
					lbWelcome.setText(controller.getErrMessage());
				else
					lbWelcome.setText("Something went wrong");
				
			}
		});
		btnSignUp.setBounds(312, 394, 216, 38);
		contentPane.add(btnSignUp);
		
		btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage frame2 = new LoginPage();
				frame2.show();
				dispose(); // close the signup page on the opening of the login page
			}
		});
		btnLogin.setBounds(312, 471, 216, 38);
		contentPane.add(btnLogin);
		
		lblNewLabel_2 = new JLabel("_____  or _____");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(310, 443, 218, 16);
		contentPane.add(lblNewLabel_2);
		
		textPassword = new JPasswordField();
		textPassword.setBounds(250, 344, 359, 38);
		contentPane.add(textPassword);
	}
	
	private void homeButton(java.awt.event.ActionEvent event, User usr) {
		// open welcome page -> frame on sign up
		HomePage frame2 = new HomePage(usr);
		frame2.setBounds(100, 100, 796, 611);
		frame2.show();
		dispose(); // close the signup page on the opening of the login page
	}
}
