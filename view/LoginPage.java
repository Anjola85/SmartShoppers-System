package com.shopperStore.view;

import com.shopperStore.controller.UserController;
import com.shopperStore.model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

	private JPanel contentPane;
	private JTextField txtEmail;
	private JLabel lbWelcome;
	private JPasswordField txtPassword;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage frame = new LoginPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginPage() {
		setForeground(Color.WHITE);
		setTitle("LOGIN PAGE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 611);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("SMART SHOPPERS STORE");
		lblNewLabel_1.setBounds(-22, 24, 786, 25);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		contentPane.add(lblNewLabel_1);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(193, 143, 359, 38);
		contentPane.add(txtEmail);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(193, 215, 226, 28);
		contentPane.add(lblPassword);
		
		JLabel lblNewLabel = new JLabel("Email Address");
		lblNewLabel.setBounds(193, 107, 226, 28);
		contentPane.add(lblNewLabel);
		
		JButton btnSignup = new JButton("SIGNUP");
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignupPage page = new SignupPage();
			 	page.show(); // open sign up page
				dispose();
			}
		});
		btnSignup.setBackground(Color.MAGENTA);
		btnSignup.setBounds(407, 340, 216, 38);
		contentPane.add(btnSignup);
		ch
		
		lbWelcome = new JLabel("");
		lbWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lbWelcome.setForeground(Color.RED);
		lbWelcome.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lbWelcome.setBounds(149, 433, 474, 51);
		contentPane.add(lbWelcome);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserController controller = new UserController();
				String email, password;
				email = txtEmail.getText();
				password = txtPassword.getText();
				
				System.out.println("debugging " + email.length());
				// get user info and pass it to an object
				User user = controller.login(email, password);

				if(email.equals("") || password.equals(""))
						lbWelcome.setText("Fill in required fields!");
				else if(user != null) {
//					System.out.println();
					lbWelcome.setText(controller.getSuccessMessage()); // display welcome message
					homeButton(e, user); // navigate to home page
				}
				else
					lbWelcome.setText(controller.getErrMessage());

			}
		});
		btnLogin.setBackground(Color.MAGENTA);
		btnLogin.setBounds(104, 340, 216, 38);
		contentPane.add(btnLogin);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(193, 243, 359, 38);
		contentPane.add(txtPassword);
		 
		
		

	}
	
	
	@SuppressWarnings("deprecation")
	private void homeButton(java.awt.event.ActionEvent event, User user) {
		// open welcome page -> frame on sign up
		HomePage frame2 = new HomePage(user);
		frame2.setBounds(100, 100, 796, 611);
		frame2.show();
		dispose(); // close the signup page on the opening of the login page
	}
}

