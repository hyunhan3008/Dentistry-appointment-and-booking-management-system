package Group_Project;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


import com.toedter.calendar.JDateChooser;

public class BookAppointment extends JFrame {
	private JPanel contentPane;
	private String startTime;
	private String endTime;
	private String[] time = new String[2];
	private String partner;
	private int startTimeHours;
	private int startTimeMins;
	private static int patientID;
	private String type;
	private String appointmentDate;
	private String appointment;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookAppointment frame = new BookAppointment(patientID);
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
	public BookAppointment(int selectedPatientID) {
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Book Appointment");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 353, 238);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JPanel bookPanel = new JPanel();
		bookPanel.setLayout(null);
		bookPanel.setBorder(new TitledBorder(null, "Book a New Appointment:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		bookPanel.setBounds(10, 11, 327, 187);
		contentPane.add(bookPanel);
		
		JLabel lblDate = new JLabel("Choose a date:");
		lblDate.setBounds(21, 33, 89, 14);
		bookPanel.add(lblDate);
		
		JDateChooser dateField = new JDateChooser();
		dateField.getCalendarButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		dateField.setDateFormatString("yyyy-MM-dd");
		dateField.setBounds(184, 31, 120, 20);
		bookPanel.add(dateField);
		
		JLabel lblTime = new JLabel("Choose a start time:");
		lblTime.setBounds(21, 64, 134, 17);
		bookPanel.add(lblTime);
		
		JComboBox<String> timeField = new JComboBox<String>();
		timeField.setModel(new DefaultComboBoxModel<String>(new String[] {
				"", "09:00", "09:20", "09:40", "10:00", "10:20", "10:40", "11:00", "11:20", "11:40", "12:00", "12:20", "12:40", 
				"13:00", "13:20", "13:40", "14:00", "14:20", "14:40", "15:00", "15:20", "15:40", "16:00", "16:20", "16:40"}));
		timeField.setBounds(184, 62, 120, 20);
		bookPanel.add(timeField);	
		
		JLabel lblType = new JLabel("Appointment type:");
		lblType.setBounds(21, 94, 134, 17);
		bookPanel.add(lblType);
		
		JComboBox<String> typeField = new JComboBox<String>();
		typeField.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Clean", "Treatment", "Check-up"}));
		typeField.setBounds(184, 92, 120, 20);
		bookPanel.add(typeField);	
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dateField.setCalendar(null);
				timeField.setSelectedIndex(0);
				typeField.setSelectedIndex(0);;
			}
		});
		btnClear.setBounds(21, 153, 89, 23);
		bookPanel.add(btnClear);
		setLocationRelativeTo(null);
		
		JButton addAppBtn = new JButton("Book");
		addAppBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				//get values from fields
				SimpleDateFormat Date_Format = new SimpleDateFormat("yyyy-MM-dd");
				appointmentDate = "";
				//formats the date of birth 
			    if (dateField.getDate() != null) {
				appointmentDate = Date_Format.format(dateField.getDate());
				}	
			    
				startTime = (String) timeField.getSelectedItem();
				type = (String) typeField.getSelectedItem();
				
				
				if (type.equals("Clean")){
					partner = "Hygenist";
				} else {
					partner = "Dentist";
				}
				
				 
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				Date d;
				try {
					d = df.parse(startTime);
					Calendar cal = Calendar.getInstance();
					 cal.setTime(d);
					 if (type.equals("Treatment")) {
						 cal.add(Calendar.MINUTE, 60);
					 }
					 else {
						 cal.add(Calendar.MINUTE, 20);
					 }
					endTime = df.format(cal.getTime());
					if (checkPatientClash(selectedPatientID)) {
						final JDialog dialog = new JDialog();
						dialog.setAlwaysOnTop(true);    
						JOptionPane.showMessageDialog(dialog,
			    		    "The patient already has an appointment for this time, please consult the calendar",
			    		    "Clash",
			    		    JOptionPane.INFORMATION_MESSAGE);
						}
					if (checkPartnerClash()) {
						final JDialog dialog = new JDialog();
						dialog.setAlwaysOnTop(true);    
						JOptionPane.showMessageDialog(dialog,
			    		    "The " + partner + " already has an appointment for this time, please consult the calendar",
			    		    "Clash",
			    		    JOptionPane.INFORMATION_MESSAGE);
						}
					
					else {
						doInsert(selectedPatientID);
						setVisible(false);
						final JDialog dialog = new JDialog();
						dialog.setAlwaysOnTop(true);    
						JOptionPane.showMessageDialog(dialog,
			    		    "Appointment Created Successfully!",
			    		    "Confirmation",
			    		    JOptionPane.INFORMATION_MESSAGE);
					}
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				} 
				
				}
			});
		addAppBtn.setBounds(215, 153, 89, 23);
		bookPanel.add(addAppBtn);
		}
				
	
	//method to insert the new data to appointment table
	public void doInsert(int selectedPatientID) throws SQLException {
		Statement stmt = null;
		PreparedStatement pstmt=null;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			//get next id for table
			int newid = 0;
				ResultSet res = stmt.executeQuery("SELECT MAX(appointmentId) FROM Appointment");
				while (res.next()) {
					newid = res.getInt(1) +1;
				}
		
			String SQL = "INSERT INTO Appointment VALUES (?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, newid);
			pstmt.setInt(2, selectedPatientID);
			pstmt.setString(3, appointmentDate);
			pstmt.setString(4, startTime);
			pstmt.setString(5, endTime);
			pstmt.setString(6, type);
			pstmt.setString(7, partner);
			pstmt.executeUpdate();
			pstmt.close();

		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
		}
	}
	
	public boolean checkPatientClash(int selectedPatientID) throws Exception{
		Statement stmt = null;
		PreparedStatement pstmt=null;
		ResultSet res = null;
		boolean clashes = false;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			pstmt = con.prepareStatement("SELECT * FROM Appointment WHERE patientID=? AND appointmentDate=?");
			pstmt.setObject(1,selectedPatientID);
			pstmt.setObject(2,appointmentDate);
			res = pstmt.executeQuery();
			clashes = checkExist(res);
				}
		catch (Exception ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
		}
		return clashes;
	}
	
	public boolean checkPartnerClash() throws Exception{
		PreparedStatement pstmt=null;
		ResultSet res = null;
		boolean clashes = false;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			pstmt = con.prepareStatement("SELECT * FROM Appointment WHERE partner=? AND appointmentDate=?");
			pstmt.setObject(1,partner);
			pstmt.setObject(2,appointmentDate);
			res = pstmt.executeQuery();
			clashes = checkExist(res);
			
				}
		catch (Exception ex) {
				ex.printStackTrace();
		}
		
		return clashes;
	}
	
	public boolean checkExist(ResultSet res) throws Exception {
		boolean clashes = false;
		if (type.equals("Treatment")) {
			while (res.next()) {
				String existstartTime = res.getString("startTime");
				String existtype = res.getString("type");
				if ((existstartTime.equals(startTime) || existstartTime.equals(changeBy(startTime,20)) || existstartTime.equals(changeBy(startTime,40)))) {
					clashes=true;
				}
				else if (existtype.equals("Treatment")) {
						if ((existstartTime.equals(changeBy(startTime,-20)) || existstartTime.equals(changeBy(startTime,-40)))) {
							clashes=true;
						}
				}	
				
			}
		}
		else {
			while (res.next()) {
				String existstartTime = res.getString("startTime");
				String existtype = res.getString("type");
				if (existstartTime.equals(startTime)){
					clashes=true;
				}
				else if (existtype.equals("Treatment")) {
						if ((existstartTime.equals(changeBy(startTime,-20)) || existstartTime.equals(changeBy(startTime,-40)))) {
							clashes=true;
						}
				}	
				
			}
		}
	
	return clashes;
	}
	
	public String changeBy(String time,int change) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date d = df.parse(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, change);
		String newTime = df.format(cal.getTime());
		return newTime;
	}
	
}
