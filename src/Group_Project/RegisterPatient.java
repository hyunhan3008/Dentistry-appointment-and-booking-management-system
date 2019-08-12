package Group_Project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import java.util.*;

public class RegisterPatient extends JFrame {

	private JPanel contentPane;
	private JTextField forenamefield;
	private JTextField surnamefield;
	private JTextField postcodefield;
	private JTextField phonenumberfield;
	private JTextField housefield;
	private JTextField districtfield;
	private JTextField streetfield;
	private JTextField cityfield;
	private String forename;
	private String surname;
	private String title;
	private String phonenumber;
	private String dateOfBirth;
	private String house;
	private int houseint;
	private String street;
	private String district;
	private String city;
	private String postcode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterPatient frame = new RegisterPatient();
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
	public RegisterPatient() {
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Register Patient");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 326, 490);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Register a Patient:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 25, 290, 412);
		contentPane.add(panel);
		
		forenamefield = new JTextField();
		forenamefield.setColumns(10);
		forenamefield.setBounds(145, 35, 120, 20);
		panel.add(forenamefield);
		
		surnamefield = new JTextField();
		surnamefield.setColumns(10);
		surnamefield.setBounds(145, 66, 120, 20);
		panel.add(surnamefield);
		
		JLabel label = new JLabel("Forename:");
		label.setBounds(21, 33, 89, 14);
		panel.add(label);
		
		JLabel label_1 = new JLabel("Surname:");
		label_1.setBounds(21, 64, 59, 14);
		panel.add(label_1);
		
		JComboBox titlefield = new JComboBox();
		titlefield.setModel(new DefaultComboBoxModel(new String[] {"", "Mr", "Mr's", "Miss"}));
		titlefield.setBounds(145, 97, 120, 20);
		panel.add(titlefield);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(21, 94, 59, 14);
		panel.add(lblTitle);
		
		JLabel lblDateOfBirth = new JLabel("Postcode:");
		lblDateOfBirth.setBounds(21, 323, 89, 14);
		panel.add(lblDateOfBirth);

		postcodefield = new JTextField();
		postcodefield.setColumns(10);
		postcodefield.setBounds(145, 320, 120, 20);
		panel.add(postcodefield);
		
		JLabel lblPhoneNo = new JLabel("Phone no:");
		lblPhoneNo.setBounds(21, 131, 59, 14);
		panel.add(lblPhoneNo);
		
		phonenumberfield = new JTextField();
		phonenumberfield.setColumns(10);
		phonenumberfield.setBounds(145, 128, 120, 20);
		panel.add(phonenumberfield);
		
		JLabel lblHouseNo = new JLabel("House no:");
		lblHouseNo.setBounds(21, 195, 59, 14);
		panel.add(lblHouseNo);
		
		housefield = new JTextField();
		housefield.setColumns(10);
		housefield.setBounds(145, 192, 120, 20);
		panel.add(housefield);
		
		JLabel label_3 = new JLabel("Date of Birth:");
		label_3.setBounds(21, 159, 89, 14);
		panel.add(label_3);
		
		JLabel lblDistrict = new JLabel("District:");
		lblDistrict.setBounds(21, 261, 59, 14);
		panel.add(lblDistrict);
		
		districtfield = new JTextField();
		districtfield.setColumns(10);
		districtfield.setBounds(145, 258, 120, 20);
		panel.add(districtfield);
		
		streetfield = new JTextField();
		streetfield.setColumns(10);
		streetfield.setBounds(145, 227, 120, 20);
		panel.add(streetfield);
		
		JLabel lblStreetName = new JLabel("Street Name:");
		lblStreetName.setBounds(21, 225, 89, 14);
		panel.add(lblStreetName);
		
		cityfield = new JTextField();
		cityfield.setColumns(10);
		cityfield.setBounds(145, 289, 120, 20);
		panel.add(cityfield);
		
		JLabel lblCity = new JLabel("City:");
		lblCity.setBounds(21, 292, 59, 14);
		panel.add(lblCity);
		
		JDateChooser dateofbirthfield = new JDateChooser();
		dateofbirthfield.getCalendarButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		dateofbirthfield.setDateFormatString("yyyy-MM-dd");
		dateofbirthfield.setBounds(145, 159, 120, 20);
		panel.add(dateofbirthfield);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				forenamefield.setText("");
				surnamefield.setText("");
				titlefield.setSelectedIndex(0);;
				phonenumberfield.setText("");
				dateofbirthfield.setCalendar(null);
				housefield.setText("");
				streetfield.setText("");
				districtfield.setText("");
				cityfield.setText("");
				postcodefield.setText("");
			}
		});
		btnClear.setBounds(21, 366, 89, 23);
		panel.add(btnClear);
		setLocationRelativeTo(null);
		
		JButton registerbtn = new JButton("Register");
		registerbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				//get values from fields
				forename = forenamefield.getText();
				surname = surnamefield.getText();
				title = (String) titlefield.getSelectedItem();
				phonenumber = phonenumberfield.getText();
				SimpleDateFormat Date_Format = new SimpleDateFormat("yyyy-MM-dd");
				dateOfBirth = "";
				//formats the date of birth 
			    if (dateofbirthfield.getDate() != null) {
				dateOfBirth = Date_Format.format(dateofbirthfield.getDate());
				}	
				house = housefield.getText();
				street = streetfield.getText();
				district = districtfield.getText();
				city = cityfield.getText();
				postcode = postcodefield.getText();
				
				//validate non empty fields
				if (postcode != ("") && forename != ("") && surname != ("") && title != ("") && phonenumber != ("")
						&& dateOfBirth != ("") && house != ("") && street != ("") && district != ("") && city != ("") 
						&& postcode != ("")) {
					System.out.println("here");
					
					//validate is a number for house
					try
				    {
				      houseint = Integer.parseInt(house);
				    }
				    catch (NumberFormatException nfe)
				    {
				    	final JDialog dialog = new JDialog();
				    	dialog.setAlwaysOnTop(true);    
						JOptionPane.showMessageDialog(dialog,
				    		    "House number must be a number.",
				    		    "Type error",
				    		    JOptionPane.ERROR_MESSAGE);
				    }
					
					//call insert method
					try {
						doinsert();
						final JDialog dialog = new JDialog();
				    	dialog.setAlwaysOnTop(true);    
						JOptionPane.showMessageDialog(dialog,
				    		    "Registration Successful!",
				    		    "Confirmation",
				    		    JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					setVisible(false);
				}
				//display error if all fields not complete
				else {
					final JDialog dialog = new JDialog();
			    	dialog.setAlwaysOnTop(true);    
					JOptionPane.showMessageDialog(dialog,
			    		    "One or more fields is not completed.",
			    		    "Completion error",
			    		    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		registerbtn.setBounds(176, 366, 89, 23);
		panel.add(registerbtn);
	}
	
	//method to insert the new data to address and patient tables
	public void doinsert() throws Exception {
		Statement stmt = null;
		System.out.println(house);
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			//get next id for table
			int newid = 0;
				ResultSet res = stmt.executeQuery("SELECT MAX(patientID) FROM Patient");
				while (res.next()) {
					newid = res.getInt(1) +1;
				}
		
			String SQL = "INSERT INTO Address VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, houseint);
			pstmt.setString(2, postcode);
			pstmt.setString(3, street);
			pstmt.setString(4, district);
			pstmt.setString(5, city);
			pstmt.executeUpdate();
			pstmt.close();
				
			String SQL2 = "INSERT INTO Patient VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt2 = con.prepareStatement(SQL2);
			pstmt2.setInt(1, newid);
			pstmt2.setInt(2, houseint);
			pstmt2.setString(3, postcode);
			pstmt2.setString(4, title);
			pstmt2.setString(5, forename);
			pstmt2.setString(6, surname);
			pstmt2.setString(7, dateOfBirth);
			pstmt2.setString(8, phonenumber);
			pstmt2.executeUpdate();
			pstmt2.close();

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
