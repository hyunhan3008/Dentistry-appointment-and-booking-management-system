package Group_Project;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.Font;

public class Appointmentmanage extends JFrame {

	private JPanel contentPane;
	private JTextField forenameField;
	private JTextField surnameField;
	private JTable searchTable;
	private static JTable appointmentTable;
	private JScrollPane searchScrollPane = new JScrollPane();
	private JPanel tableSelectPanel = new JPanel();
	static JPanel currentAppointsPanel = new JPanel();
	public static int selectedPatientId;
	public static int selectedAppId;
	private JLabel noAppointments;
	private JScrollPane appointmentScrollPane = new JScrollPane();
	private JButton removeAppointmentBtn = new JButton("Remove Selected Appointment");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Appointmentmanage frame = new Appointmentmanage();
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
	public Appointmentmanage() {
		setTitle("Appointment Management");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1146, 617);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton homeBtn = new JButton("Home");
		homeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Secretaryhome Secretaryhome = new Secretaryhome();   
		        setVisible(false); // Hide current frame
		        Secretaryhome.setVisible(true);
			}
		});
		homeBtn.setBounds(10, 23, 79, 23);
		contentPane.add(homeBtn);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(null);
		searchPanel.setBorder(new TitledBorder(null, "Search For a Patient:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchPanel.setBounds(10, 65, 167, 151);
		contentPane.add(searchPanel);
		
		forenameField = new JTextField();
		forenameField.setColumns(10);
		forenameField.setBounds(75, 47, 86, 20);
		searchPanel.add(forenameField);
		
		surnameField = new JTextField();
		surnameField.setColumns(10);
		surnameField.setBounds(75, 78, 86, 20);
		searchPanel.add(surnameField);
		
		JLabel fLabel = new JLabel("Forename:");
		fLabel.setBounds(6, 50, 78, 14);
		searchPanel.add(fLabel);
		
		JLabel sLabel = new JLabel("Surname:");
		sLabel.setBounds(6, 81, 59, 14);
		searchPanel.add(sLabel);
		
		JButton viewcalendar = new JButton("View Calendar");
		viewcalendar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		viewcalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CalenderView CalenderView = new CalenderView();   
		         // Hide current frame
		        CalenderView.setVisible(true);
			}
		});
		viewcalendar.setBounds(10, 245, 167, 115);
		contentPane.add(viewcalendar);
		
		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//try search
				try {
					searchforpatient();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		searchBtn.setBounds(37, 117, 89, 23);
		searchPanel.add(searchBtn);
		
		tableSelectPanel.setBounds(187, 11, 943, 388);
		contentPane.add(tableSelectPanel);
		tableSelectPanel.setLayout(null);
		tableSelectPanel.setVisible(false);
		
		JLabel lblCurrentInformation = new JLabel("Select a row to view a patients' current appointments:");
		lblCurrentInformation.setBounds(10, 0, 461, 27);
		tableSelectPanel.add(lblCurrentInformation);
		searchScrollPane.setBounds(10, 28, 923, 357);
		tableSelectPanel.add(searchScrollPane);
		
		searchTable = new JTable();
		searchTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				//gets values from the selected row
				int i = searchTable.getSelectedRow();

		        TableModel model = searchTable.getModel();
		        
		        String fName = model.getValueAt(i,1).toString();
		        String sName = model.getValueAt(i,2).toString();
		        String postcode = model.getValueAt(i,6).toString();
		        String houseNum = model.getValueAt(i,5).toString();
		        
		        try {
		        	setTitle(fName, sName);
		        	selectedPatientId = getIdFromSelectedRow(fName, sName, postcode, houseNum);
				} catch (Exception e) {
				}
		        
		        try {
		        	currentAppointsPanel.setVisible(true);
		        	removeAppointmentBtn.setVisible(false);
					if (appointmentExists()==true) {
						appointmentScrollPane.setVisible(true);
						noAppointments.setVisible(false);
						fillAppointmentTable();
					}
					else {
						appointmentScrollPane.setVisible(false);
						noAppointments.setVisible(true);
					}
				} catch (Exception e) {
				}
		        
			}
		});
		searchScrollPane.setViewportView(searchTable);
		
		currentAppointsPanel.setBounds(187, 401, 943, 176);
		contentPane.add(currentAppointsPanel);
		currentAppointsPanel.setLayout(null);
		currentAppointsPanel.setVisible(false);
		
		appointmentScrollPane.setBounds(10, 24, 923, 98);
		currentAppointsPanel.add(appointmentScrollPane);
		
		appointmentTable = new JTable();
		appointmentTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				//gets values from the selected row
				int i = appointmentTable.getSelectedRow();

		        TableModel model = appointmentTable.getModel();
		        
		        String appDate = model.getValueAt(i,0).toString();
		        String appStart = model.getValueAt(i,1).toString();
		        String appEnd = model.getValueAt(i,2).toString();
		        
		        try {
		        	selectedAppId = getAppIdFromSelected(appDate, appStart, appEnd);
		        	removeAppointmentBtn.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
		appointmentScrollPane.setViewportView(appointmentTable);
	
		removeAppointmentBtn.setBounds(684, 142, 249, 23);
		currentAppointsPanel.add(removeAppointmentBtn);
		removeAppointmentBtn.setVisible(false);
		
		JButton addAppointmentBtn = new JButton("Book a New Appointment");
		addAppointmentBtn.setBounds(10, 142, 214, 23);
		currentAppointsPanel.add(addAppointmentBtn);
		addAppointmentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BookAppointment BookAppointment = new BookAppointment(selectedPatientId);
				BookAppointment.setVisible(true);
			}
		});
		
		noAppointments = new JLabel("There are no current appointments for this patient");
		noAppointments.setFont(new Font("Tahoma", Font.PLAIN, 18));
		noAppointments.setBounds(10, 43, 705, 36);
		currentAppointsPanel.add(noAppointments);
		noAppointments.setVisible(false);
		
		removeAppointmentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					cancelSelectedAppt();
					DefaultTableModel model = (DefaultTableModel) appointmentTable.getModel();
					model.setRowCount(0);
					fillAppointmentTable();
					final JDialog dialog = new JDialog();
					dialog.setAlwaysOnTop(true);    
					JOptionPane.showMessageDialog(dialog,
		    		    "Appointment Cancelled Successfully!",
		    		    "Confirmation",
		    		    JOptionPane.INFORMATION_MESSAGE);
				
			//		fillcurrentplantable();
				} catch (Exception e) {
				}
			}
		});
	//	removeAppointmentbtn.setVisible(false);
	}

	public boolean appointmentExists() throws Exception{
		Statement stmt = null;
		int ID = 0;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			 
			 String SQL = "SELECT patientID FROM Appointment  WHERE patientID = ? ";;
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setInt(1, selectedPatientId);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					ID = rs.getInt("patientID");
				}
				pstmt.close();
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		
		if (ID != 0) {
			return true;
		}
		
		return false;
	}
	
	public void setTitle(String fName, String sName){
		String name = fName + " " + sName;
		currentAppointsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Future appointments booked for " + name + ":", TitledBorder.CENTER,TitledBorder.TOP));
	}
	
	//using the values passed in founds the unique patient id relating to these values
	public int getIdFromSelectedRow(String fName, String sName, String postcode, String houseNum) throws Exception{
		Statement stmt = null;
		int ID = 0;
		
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			 
			 String SQL = "SELECT patientID FROM Patient NATURAL JOIN Address WHERE forename = ? AND surname = ? AND postcode = ? AND houseNumber = ?";;
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, fName);
				pstmt.setString(2, sName);
				pstmt.setString(3, postcode);
				pstmt.setString(4, houseNum);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					ID = rs.getInt("patientID");
				}
				pstmt.close();
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		return ID;
	}
	
	public void searchforpatient() throws Exception{
		Statement stmt = null;
		DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Forename", "Surname", "DOB", "phone", "House no.", "Postcode", "Street", "District", "City"}, 0);
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			String query = "SELECT title, forename, surname, dateOfBirth, phonenumber, houseNumber, postcode, streetName, districtName, cityName  FROM Patient NATURAL JOIN Address WHERE forename LIKE '%" + forenameField.getText() + "%' AND surname LIKE '%" + surnameField.getText() + "%' ";
			 ResultSet rs = stmt.executeQuery(query);
			 
			 while(rs.next())
			 {
			     String a = rs.getString("title");
			     String b = rs.getString("forename");
			     String c = rs.getString("surname");
			     String d = rs.getString("dateOfBirth");
			     String e = rs.getString("phonenumber");
			     String f = rs.getString("houseNumber");
			     String g = rs.getString("postcode");
			     String h = rs.getString("streetName");
			     String i = rs.getString("districtName");
			     String j = rs.getString("cityName");
			     model.addRow(new Object[]{a, b, c, d, e, f, g, h, i ,j});
			 }
			 
			 //searchscrollPane.add(new JScrollPane(table));
			 searchTable.setModel(model);
			
			 tableSelectPanel.setVisible(true);
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		
	} 
	
	public static void fillAppointmentTable() throws Exception{
		Statement stmt = null;
		DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Start time", "End time", "Type", "Partner"}, 0);
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			String SQL = "SELECT appointmentDate, startTime, endTime, type, partner FROM Appointment WHERE patientID = ? ORDER BY appointmentDate, startTime";
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, selectedPatientId);
			ResultSet rs = pstmt.executeQuery();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String today = dateFormat.format(date);
			
			 while(rs.next())
			 {
			     String a = rs.getString("appointmentDate");
			     String b = rs.getString("startTime");
			     String c = rs.getString("endTime");
			     String d = rs.getString("type");
			     String e = rs.getString("partner");
			     if (a.compareTo(today)>=0) {
			    	 model.addRow(new Object[]{a, b, c, d, e,});
			     }
			    }
			 
			 //searchscrollPane.add(new JScrollPane(table));
			 appointmentTable.setModel(model);
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		
	}                    
		
	public int getAppIdFromSelected(String appDate, String appStart, String appEnd) throws Exception{
		Statement stmt = null;
		int ID = 0;
		
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			 
			 String SQL = "SELECT appointmentId FROM Appointment WHERE appointmentDate = ? AND startTime = ? AND endTime= ?";;
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, appDate);
				pstmt.setString(2, appStart);
				pstmt.setString(3, appEnd);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					ID = rs.getInt("appointmentId");
				}
				pstmt.close();
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		return ID;
	}		 
	
	public void cancelSelectedAppt() throws Exception{
		Statement stmt = null;
		
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			String SQL = "DELETE FROM Appointment WHERE appointmentId = ?";
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, selectedAppId);
			pstmt.executeUpdate();
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
	}
}
