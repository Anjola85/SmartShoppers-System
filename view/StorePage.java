package com.shopperStore.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.shopperStore.model.humanResource.Store;

import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JScrollBar;

public class StorePage extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearchItemBy;
	private JTextField txtItemNameTo;
	public Store currentStore;
	
	
	public StorePage(Store s) {
		currentStore = s;
		main(null);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StorePage frame = new StorePage();
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
	public StorePage() {
		setTitle("STORE PAGE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 611);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtSearchItemBy = new JTextField();
		txtSearchItemBy.setText("     Search for Item");
		txtSearchItemBy.setHorizontalAlignment(SwingConstants.LEFT);
		txtSearchItemBy.setColumns(10);
		txtSearchItemBy.setBackground(Color.LIGHT_GRAY);
		txtSearchItemBy.setBounds(36, 74, 297, 40);
		contentPane.add(txtSearchItemBy);
		
		JButton searchBtn_1 = new JButton("SEARCH");
		searchBtn_1.setBounds(36, 139, 124, 33);
		contentPane.add(searchBtn_1);
		
		JLabel output = new JLabel("");
		output.setBackground(Color.BLUE);
		output.setHorizontalAlignment(SwingConstants.CENTER);
		output.setBounds(36, 173, 297, 392);
		contentPane.add(output);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("category");
		chckbxNewCheckBox.setBounds(36, 39, 128, 23);
		contentPane.add(chckbxNewCheckBox);
		
		JCheckBox chckbxItemName = new JCheckBox("item name");
		chckbxItemName.setBounds(205, 39, 128, 23);
		contentPane.add(chckbxItemName);
		
		JLabel lblNewLabel = new JLabel("Select the appropriate checkbox and search");
		lblNewLabel.setBounds(46, 11, 317, 16);
		contentPane.add(lblNewLabel);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(317, 173, 15, 96);
		contentPane.add(scrollBar);
		
		txtItemNameTo = new JTextField();
		txtItemNameTo.setText("Item name to add/remove to shopping list");
		txtItemNameTo.setHorizontalAlignment(SwingConstants.LEFT);
		txtItemNameTo.setColumns(10);
		txtItemNameTo.setBackground(Color.LIGHT_GRAY);
		txtItemNameTo.setBounds(477, 74, 297, 40);
		contentPane.add(txtItemNameTo);
		
		JButton removeBtn = new JButton("REMOVE");
		removeBtn.setBounds(650, 126, 124, 33);
		contentPane.add(removeBtn);
		
		JButton addBtn = new JButton("ADD");
		addBtn.setBounds(477, 126, 124, 33);
		contentPane.add(addBtn);
		
		JLabel shoppingListOutput = new JLabel("");
		shoppingListOutput.setHorizontalAlignment(SwingConstants.CENTER);
		shoppingListOutput.setBounds(477, 173, 297, 392);
		contentPane.add(shoppingListOutput);
		
		JLabel lblShoppingList = new JLabel("SHOPPING LIST");
		lblShoppingList.setHorizontalAlignment(SwingConstants.CENTER);
		lblShoppingList.setBounds(536, 193, 196, 16);
		contentPane.add(lblShoppingList);
		
		JScrollBar scrollBar_1 = new JScrollBar();
		scrollBar_1.setBounds(717, 197, 15, 96);
		contentPane.add(scrollBar_1);
		
		JLabel lblStoreItems = new JLabel("STORE ITEMS");
		lblStoreItems.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoreItems.setBounds(78, 184, 196, 16);
		contentPane.add(lblStoreItems);
	}
}
