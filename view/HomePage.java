package com.shopperStore.view;

import com.shopperStore.controller.StoreController;
import com.shopperStore.controller.UserController;
import com.shopperStore.model.humanResource.Store;
import com.shopperStore.model.user.Customer;
import com.shopperStore.model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HomePage extends JFrame {

	private JPanel contentPane;
	private JTextField txtLocation;
	Customer customer;
	JLabel output;
	Store store = null;
	
	public HomePage(User usr) {
		this.customer = (Customer) usr;
		main(null);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomePage frame = new HomePage();
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
	public HomePage() {
		setTitle("HOME PAGE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 611);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton logoutBtn = new JButton("LOGOUT");
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		logoutBtn.setForeground(Color.RED);
		logoutBtn.setBackground(UIManager.getColor("Focus.color"));
		logoutBtn.setBounds(69, 32, 117, 29);
		contentPane.add(logoutBtn);
		
		
		txtLocation = new JTextField();
		txtLocation.setText("     Search for store location");
		txtLocation.setHorizontalAlignment(SwingConstants.LEFT);
		txtLocation.setColumns(10);
		txtLocation.setBackground(Color.LIGHT_GRAY);
		txtLocation.setBounds(430, 104, 297, 40);
		contentPane.add(txtLocation);
		
		JButton navigateBtn = new JButton("NAVIGATE TO STORE PAGE");
		navigateBtn.addActionListener(new ActionListener() { // navigate to next page -> store page
			public void actionPerformed(ActionEvent e) {
				navigateToStore(e); // call navigate function
//				if(store != null) 
//					navigateToStore(e); // call navigate function
//				else
//					output.setText("Could not find store");
			}
		});
		navigateBtn.setBounds(430, 201, 215, 40);
		contentPane.add(navigateBtn);
		
		JButton saveLocationBtn = new JButton("SAVE LOCATION");
		saveLocationBtn.addActionListener(new ActionListener() { // save location of store to customer db
			public void actionPerformed(ActionEvent e) {
				UserController controller = new UserController();
				if(store != null) {
					customer.getSavedLocation().add(txtLocation.getText());
					controller.update(customer);
				}
				else
					output.setText("Could not find store");
			}
		});
		saveLocationBtn.setBounds(603, 156, 124, 33);
		contentPane.add(saveLocationBtn);
		
		output = new JLabel("");
		output.setHorizontalAlignment(SwingConstants.CENTER);
		output.setBounds(69, 263, 682, 277);
		contentPane.add(output);
		
		JButton searchBtn = new JButton("SEARCH");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StoreController controller = new StoreController();
				store = controller.getStore(txtLocation.getText());
				if(store != null) {
					// display store information
					output.setText(store.getName() + " " + store.getDescription() + " " + store.getLocation() + " " + 
							store.getOpeningHours() + " " + store.getClosingHours() + "\n");
					
				} else {
					System.out.println("Controller could not retrieve store");
					output.setText("Store does not exist");
				}
			}
		});
		searchBtn.setBounds(430, 156, 124, 33);
		contentPane.add(searchBtn);
		
		JButton nearestStoresBtn = new JButton("FIND NEAREST STORES");
		nearestStoresBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StoreController controller = new StoreController();
				List<Store> storesList = controller.getStoreBasedOnLocation(customer.getAddress());
				
				
				String buffer = "";
				
				for(int i = 0; i < storesList.size(); i++) {
					Store curr = storesList.get(i);
					String info = curr.getName() + " " + curr.getDescription() + " " + curr.getLocation() + " " + curr.getOpeningHours() + " " + curr.getClosingHours() + "\n";
					buffer += info;
				}
				
				System.out.println("REsult of stores:::: \n" + buffer);
				
				// display the store information on view -> info: name, address, closing and opening hours
				output.setText(buffer);
			}
		});
		nearestStoresBtn.setBounds(69, 104, 246, 40);
		contentPane.add(nearestStoresBtn);

	}
	
	private void navigateToStore(java.awt.event.ActionEvent event) {
		// open welcome page -> frame on sign up
		StorePage frame2 = new StorePage(store); // passing the store data
		frame2.setBounds(100, 100, 796, 611);
		frame2.show();
		dispose(); // close the signup page on the opening of the login page
	}
}
