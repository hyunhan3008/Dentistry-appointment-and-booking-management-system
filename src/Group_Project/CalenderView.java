package Group_Project;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.JScrollPane;

public class CalenderView extends JFrame {

	private JPanel contentPane;
	private static  JTable tabledentist;
	private static  JTable tablehygienist;
	private JPanel tablepanel = new JPanel();
	private static String datefield;
	private static ArrayList<String> datelist = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalenderView frame = new CalenderView();
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
	public CalenderView() {
		setResizable(false);
		setTitle("View Calendar");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 797, 415);
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
		home.setBounds(10, 11, 74, 23);
		contentPane.add(home);
		
		JLabel label = new JLabel("Select Start Date:");
		label.setBounds(10, 49, 101, 14);
		contentPane.add(label);
		
		JDateChooser startdatefield = new JDateChooser();
		startdatefield.setDateFormatString("yyyy-MM-dd");
		startdatefield.setBounds(153, 49, 109, 20);
		contentPane.add(startdatefield);
		
		tablepanel.setVisible(false);
		tablepanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tablepanel.setBounds(10, 94, 772, 281);
		contentPane.add(tablepanel);
		tablepanel.setLayout(null);
		
		JLabel label_2 = new JLabel("Dentist:");
		label_2.setBounds(10, 16, 46, 14);
		tablepanel.add(label_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 41, 370, 236);
		tablepanel.add(scrollPane);
		
		tabledentist = new JTable();
		scrollPane.setViewportView(tabledentist);
		tabledentist.setEnabled(false);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(392, 41, 370, 236);
		tablepanel.add(scrollPane_1);
		
		tablehygienist = new JTable();
		scrollPane_1.setViewportView(tablehygienist);
		
		JLabel label_3 = new JLabel("Hygienist:");
		label_3.setBounds(399, 16, 60, 14);
		tablepanel.add(label_3);
		
		JButton display = new JButton("Display ");
		display.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//clears contents of arraylist
				datelist.clear();
				
				SimpleDateFormat Date_Format = new SimpleDateFormat("yyyy-MM-dd");
				datefield = "";
				 if (startdatefield.getDate() != null) {
					 //parse the datefield entry into seperate day month, year variables
					 datefield = Date_Format.format(startdatefield.getDate());
					 int day = Integer.parseInt(datefield.substring(8,10));
					 int month = Integer.parseInt(datefield.substring(5,7));
					 int year = Integer.parseInt(datefield.substring(0,4));
					
					 	//use the variables to set the calendar start date, then get the next 6 dates
					 	Calendar start = Calendar.getInstance();
						start.set(year, month-1, day);
						Calendar end = Calendar.getInstance();
						end.setTime(start.getTime());
						end.add(Calendar.DATE, 6);

						Calendar now = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						
						//create a list of the 7 dates which will be used to get appointments for a week
						for (java.util.Date dt = start.getTime(); !start.after(end); start.add(
						                Calendar.DATE, 1), dt = start.getTime()) {
							datelist.add(sdf.format(dt));
						}
						//fill the two tables using the week of dates created
						try {
							fillhygienisttable();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						try {
							filldentisttable();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						tablepanel.setVisible(true);
				}	
				 
				
			}
		});
		display.setBounds(310, 45, 89, 23);
		contentPane.add(display);
		
		JLabel lblNewLabel = new JLabel("(displays a weeks worth of appointments)");
		lblNewLabel.setBounds(10, 74, 252, 14);
		contentPane.add(lblNewLabel);
	}
	
	public static void filldentisttable() throws Exception{
		Statement stmt = null;
		
		
		DefaultTableModel model = new DefaultTableModel(new String[]{"Forename", "Surname", "Date", "Start Time", "End Time"}, 0);
		for (int i = 0; i < datelist.size(); i++) {
			try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			
				stmt = con.createStatement();
				String SQL = "SELECT forename, surname, appointmentDate, startTime, endTime FROM Appointment NATURAL JOIN Patient WHERE appointmentDate = ? AND partner = ? ORDER BY appointmentDate, startTime";
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, datelist.get(i));
				pstmt.setString(2, "Dentist");
				ResultSet rs = pstmt.executeQuery();
			
				while(rs.next())
				{
					String a = rs.getString("forename");
					String b = rs.getString("surname");
					String c = rs.getString("appointmentDate");
					String d = rs.getString("startTime");
					String e = rs.getString("endTime");

					model.addRow(new Object[]{a, b, c, d, e});
				}
			 
				//searchscrollPane.add(new JScrollPane(table));
				
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
			finally {
				if (stmt != null)
					stmt.close();
			}
		}
		tabledentist.setModel(model);
		
	}    
	
	public static void fillhygienisttable() throws Exception{
		Statement stmt = null;
		
		DefaultTableModel model = new DefaultTableModel(new String[]{"Forename", "Surname", "Date", "Start Time", "End Time"}, 0);
		for (int i = 0; i < datelist.size(); i++) {
			
			try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			
				stmt = con.createStatement();
				String SQL = "SELECT forename, surname, appointmentDate, startTime, endTime FROM Appointment NATURAL JOIN Patient WHERE appointmentDate = ? AND partner = ? ORDER BY appointmentDate, startTime";
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, datelist.get(i));
				pstmt.setString(2, "Hygienist");
				ResultSet rs = pstmt.executeQuery();
			
				while(rs.next())
				{
					String a = rs.getString("forename");
					String b = rs.getString("surname");
					String c = rs.getString("appointmentDate");
					String d = rs.getString("startTime");
					String e = rs.getString("endTime");

					model.addRow(new Object[]{a, b, c, d, e});
				}
			 
				//searchscrollPane.add(new JScrollPane(table));
				
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
			finally {
				if (stmt != null)
					stmt.close();
			}
		}
		
		tablehygienist.setModel(model);
	}   
}
