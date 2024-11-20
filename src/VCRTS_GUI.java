//package vcrts_gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VCRTS_GUI extends JFrame {

	private JRadioButton clientButton;
	private JRadioButton ownerButton;
	private JPanel clientPanel;
	private JPanel ownerPanel;
	private JPanel mainPanel; // Panel to hold both client and owner panels
	private CardLayout cardLayout; // Layout manager to switch between panels
	private JTextField clientIdField;
	private JTextField jobDurationField;
	private JTextField jobDeadlineField;
	private JTextField ownerIdField;
	private JTextField vehicleModelField;
	private JTextField vehicleBrandField;
	private JTextField plateNumberField;
	private JTextField serialNumberField;
	private JTextField vinNumberField;
	private JTextField residencyTimeField;
	private JButton submitButton;
	private ButtonGroup roleGroup;

	public VCRTS_GUI() {
		setTitle("Vehicular Cloud Real Time System (VCRTS)");
		setSize(1000, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		ImageIcon logo = new ImageIcon("src/VCRTS_logo.png");
		setIconImage(logo.getImage());

		// Panel for choosing role (Client or Owner)
		JPanel rolePanel = new JPanel();
		rolePanel.setLayout(new FlowLayout());
		clientButton = new JRadioButton("Client");
		ownerButton = new JRadioButton("Owner");
		roleGroup = new ButtonGroup();
		roleGroup.add(clientButton);
		roleGroup.add(ownerButton);
		rolePanel.add(clientButton);
		rolePanel.add(ownerButton);
		add(rolePanel, BorderLayout.NORTH);

		 // CardLayout for switching between Client and Owner panels
		 cardLayout = new CardLayout();
		 mainPanel = new JPanel(cardLayout);

		// Client Panel Setup
		clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout());

		// Client Input Panel
		JPanel clientDetailsPanel = new JPanel();
        clientDetailsPanel.setLayout(new BoxLayout(clientDetailsPanel, BoxLayout.Y_AXIS));

        clientDetailsPanel.add(Box.createVerticalStrut(20));
        clientDetailsPanel.add(createLabelFieldPanel("Client ID:"));
        clientDetailsPanel.add(Box.createVerticalStrut(10));
        clientDetailsPanel.add(createLabelFieldPanel("Job Duration (hrs):"));
        clientDetailsPanel.add(Box.createVerticalStrut(10));
        clientDetailsPanel.add(createLabelFieldPanel("Job Deadline:"));
        clientDetailsPanel.add(Box.createVerticalStrut(20));

        // Get references to the text fields
        clientIdField = (JTextField) ((JPanel)clientDetailsPanel.getComponent(1)).getComponent(1);
        jobDurationField = (JTextField) ((JPanel)clientDetailsPanel.getComponent(3)).getComponent(1);
        jobDeadlineField = (JTextField) ((JPanel)clientDetailsPanel.getComponent(5)).getComponent(1);

        clientPanel.add(clientDetailsPanel, BorderLayout.CENTER);
		// Owner Input Panel
		ownerPanel = new JPanel();
		ownerPanel.setLayout(new BorderLayout()); 

		// Create left panel for owner details
		JPanel ownerDetailsPanel = new JPanel();
		ownerDetailsPanel.setLayout(new BoxLayout(ownerDetailsPanel, BoxLayout.Y_AXIS));

		ownerDetailsPanel.add(Box.createVerticalStrut(20));
		ownerDetailsPanel.add(createLabelFieldPanel("Owner ID:"));
		ownerDetailsPanel.add(Box.createVerticalStrut(10));
		ownerDetailsPanel.add(createLabelFieldPanel("Vehicle Model:"));
		ownerDetailsPanel.add(Box.createVerticalStrut(10));
		ownerDetailsPanel.add(createLabelFieldPanel("Vehicle Brand:"));
		ownerDetailsPanel.add(Box.createVerticalStrut(10));
		ownerDetailsPanel.add(createLabelFieldPanel("Plate Number:"));
		ownerDetailsPanel.add(Box.createVerticalStrut(10));
		ownerDetailsPanel.add(createLabelFieldPanel("Serial Number:"));
		ownerDetailsPanel.add(Box.createVerticalStrut(10));
		ownerDetailsPanel.add(createLabelFieldPanel("VIN Number:"));
		ownerDetailsPanel.add(Box.createVerticalStrut(10));
		ownerDetailsPanel.add(createLabelFieldPanel("Residency Time:"));
		ownerDetailsPanel.add(Box.createVerticalStrut(20));

		// Get references to all the text fields
		ownerIdField = (JTextField) ((JPanel) ownerDetailsPanel.getComponent(1)).getComponent(1);
		vehicleModelField = (JTextField) ((JPanel) ownerDetailsPanel.getComponent(3)).getComponent(1);
		vehicleBrandField = (JTextField) ((JPanel) ownerDetailsPanel.getComponent(5)).getComponent(1);
		plateNumberField = (JTextField) ((JPanel) ownerDetailsPanel.getComponent(7)).getComponent(1);
		serialNumberField = (JTextField) ((JPanel) ownerDetailsPanel.getComponent(9)).getComponent(1);
		vinNumberField = (JTextField) ((JPanel) ownerDetailsPanel.getComponent(11)).getComponent(1);
		residencyTimeField = (JTextField) ((JPanel) ownerDetailsPanel.getComponent(13)).getComponent(1);

		// Create right panel for registered vehicles
		RegisteredVehiclesPanel vehiclesPanel = new RegisteredVehiclesPanel();

		// Add both panels to ownerPanel
		ownerPanel.add(ownerDetailsPanel, BorderLayout.WEST);
		ownerPanel.add(vehiclesPanel, BorderLayout.EAST);

		// Add both panels to the CardLayout mainPanel
		mainPanel.add(clientPanel, "Client");
		mainPanel.add(ownerPanel, "Owner");
		add(mainPanel, BorderLayout.CENTER);

		submitButton = new JButton("Submit");
		add(submitButton, BorderLayout.SOUTH);

		// Action listeners for role selection
		clientButton.addActionListener(e -> cardLayout.show(mainPanel, "Client"));
		ownerButton.addActionListener(e -> cardLayout.show(mainPanel, "Owner"));

		// Action listener for submit button
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (clientButton.isSelected()) {
					handleClientSubmission();
				} else if (ownerButton.isSelected()) {
					handleOwnerSubmission();
				} else {
					JOptionPane.showMessageDialog(null, "Please select a role: Client or Owner.");
				}
			}
		});

		// Set default selection to Owner so it's visible initially or can be set to
		// blank later
		ownerButton.setSelected(true);
		cardLayout.show(mainPanel, "Owner");
	}

	private JPanel createLabelFieldPanel(String labelText) {
		JPanel panel = new JPanel(new BorderLayout(10, 0));
		JLabel label = new JLabel(labelText);
		label.setPreferredSize(new Dimension(150, 25));
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label, BorderLayout.WEST);

		JTextField field = new JTextField(20);
		panel.add(field, BorderLayout.CENTER);

		panel.setMaximumSize(new Dimension(400, 30));
		return panel;
	}

	private void handleClientSubmission() {
		String clientId = clientIdField.getText();
		String jobDuration = jobDurationField.getText();
		String jobDeadline = jobDeadlineField.getText();
		LocalDateTime timestamp = LocalDateTime.now();
		String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		if (!clientId.isEmpty() && !jobDuration.isEmpty() && !jobDeadline.isEmpty()) {
			String data = clientId + "," + jobDuration + "," + jobDeadline + "," + formattedTimestamp;
			saveToFile(data);
			JOptionPane.showMessageDialog(null, "Client data submitted!");
			clearFields();
		} else {
			JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
		}
	}

	private void handleOwnerSubmission() {
		String ownerId = ownerIdField.getText();
		String vehicleModel = vehicleModelField.getText();
		String vehicleBrand = vehicleBrandField.getText();
		String plateNumber = plateNumberField.getText();
		String serialNumber = serialNumberField.getText();
		String vinNumber = vinNumberField.getText();
		String residencyTime = residencyTimeField.getText();
		LocalDateTime timestamp = LocalDateTime.now();
		String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		if (!ownerId.isEmpty() && !vehicleModel.isEmpty() && !vehicleBrand.isEmpty() && !plateNumber.isEmpty()
				&& !serialNumber.isEmpty() && !vinNumber.isEmpty() && !residencyTime.isEmpty()) {
			String data = ownerId + "," + vehicleModel + "," + vehicleBrand + "," + plateNumber + "," + serialNumber
					+ "," + vinNumber + "," + residencyTime + "," + formattedTimestamp;
			saveToFile(data);
			JOptionPane.showMessageDialog(null, "Owner data submitted!");
			clearFields();
		} else {
			JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
		}
	}

	private void saveToFile(String data) {
		String fileName;
		String header;
	
		// Determine which file to write based on the selected panel
		if (clientButton.isSelected()) {
			fileName = "vcrts_data_client.csv";
			header = "Client ID,Job Duration,Job Deadline,Timestamp";
		} else if (ownerButton.isSelected()) {
			fileName = "vcrts_data_owner.csv";
			header = "Owner ID,Vehicle Model,Vehicle Brand,Plate Number,Serial Number,VIN Number,Residency Time,Timestamp";
		} else {
			JOptionPane.showMessageDialog(null, "Please select a role: Client or Owner.");
			return;
		}
	
		// Check if the file already exists
		boolean fileExists = new java.io.File(fileName).exists();
	
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			// If the file does not exist, write the headers
			if (!fileExists) {
				writer.write(header);
				writer.newLine();
			}
			
			// Write the data
			writer.write(data);
			writer.newLine();
	
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error saving data to CSV file.");
		}
	} 

	private void clearFields() {
		clientIdField.setText("");
		jobDurationField.setText("");
		jobDeadlineField.setText("");
		ownerIdField.setText("");
		vehicleModelField.setText("");
		vehicleBrandField.setText("");
		plateNumberField.setText("");
		serialNumberField.setText("");
		vinNumberField.setText("");
		residencyTimeField.setText("");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			VCRTS_GUI gui = new VCRTS_GUI();
			gui.setVisible(true);
		});
	}
}
