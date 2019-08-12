package Group_Project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import com.toedter.calendar.JDayChooser;
import com.toedter.components.JLocaleChooser;
import com.toedter.components.JSpinField;
import java.awt.Choice;
import com.toedter.calendar.JDateChooser;

public class bookholiday extends JFrame {

	private JPanel contentPane;
    private String startTime;
    private String endTime;
    private String partner;
    private String appointmentDate;
    private String datefield;
    private String datefield2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					bookholiday frame = new bookholiday();
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
	public bookholiday() {
		setResizable(false);
		setTitle("Book a Holiday");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 335, 236);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton home = new JButton("Home");
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Secretaryhome Secretaryhome = new Secretaryhome();   
		        setVisible(false); // Hide current frame
		        Secretaryhome.setVisible(true);
			}
		});
		home.setBounds(10, 11, 79, 23);
		contentPane.add(home);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Select a plan:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 45, 308, 149);
		contentPane.add(panel);
		
		JComboBox employeefield = new JComboBox();
		employeefield.setModel(new DefaultComboBoxModel(new String[] {"", "Dentist", "Hygienist"}));
		employeefield.setBounds(164, 27, 129, 20);
		panel.add(employeefield);
		
		
		
		JLabel lblSelectEmployee = new JLabel("Select Employee:");
		lblSelectEmployee.setBounds(10, 27, 136, 14);
		panel.add(lblSelectEmployee);
		
		JLabel lblStartDate = new JLabel("Select Date:");
		lblStartDate.setBounds(10, 58, 136, 14);
		panel.add(lblStartDate);
		
		JDateChooser startdatefield = new JDateChooser();
		startdatefield.setDateFormatString("yyyy-MM-dd");
		startdatefield.setBounds(164, 55, 129, 20);
		panel.add(startdatefield);
		
		JButton btnBookHoldiay = new JButton("Book Holdiay");
		btnBookHoldiay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				partner = (String) employeefield.getSelectedItem();
				
				SimpleDateFormat Date_Format = new SimpleDateFormat("yyyy-MM-dd");
				datefield = "";
				 if (startdatefield.getDate() != null) {
					 datefield = Date_Format.format(startdatefield.getDate());
				 }
				 
				 try {
					if ( bookingPossible() == true) {
							try {
								doinsert();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							final JDialog dialog = new JDialog();
					    	dialog.setAlwaysOnTop(true);    
							JOptionPane.showMessageDialog(dialog,
					    		    "Booking Successful!",
					    		    "Booking Confirmation",
					    		    JOptionPane.INFORMATION_MESSAGE);
							
							
					}
					else {
						final JDialog dialog = new JDialog();
				    	dialog.setAlwaysOnTop(true);    
						JOptionPane.showMessageDialog(dialog,
				    		    "This date already contains appointments",
				    		    "Booking Error",
				    		    JOptionPane.ERROR_MESSAGE);
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 
				
				
			}
		});
		
		btnBookHoldiay.setBounds(84, 114, 141, 23);
		panel.add(btnBookHoldiay);

	}
	
	public void doinsert() throws Exception {
		Statement stmt = null;
		//System.out.println(house);
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			//get next id for table
			int newid = 0;
				ResultSet res = stmt.executeQuery("SELECT MAX(appointmentID) FROM Appointment");
				while (res.next()) {
					newid = res.getInt(1) +1;
				}
		
			String SQL = "INSERT INTO Appointment VALUES (?, ?, ?, ?, ?, ?,?)";
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, newid);
			pstmt.setString(2, "6");
			pstmt.setString(3, datefield);
			pstmt.setString(4, "09:00");
			pstmt.setString(5, "18:00");
			pstmt.setString(6, "Holiday");
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
		}
	}
	
	public boolean bookingPossible() throws Exception{
		Statement stmt = null;
		String existingPartner = "";
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			 
			 String SQL = "SELECT appointmentId FROM Appointment  WHERE appointmentDate = ? AND partner =?";;
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, datefield);
				pstmt.setString(2, partner);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					existingPartner = rs.getString("appointmentId");
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
		
		if (existingPartner != "") {
			return false;
		}
		
		return true;
	}
	
}
