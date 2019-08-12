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
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;

public class Patientmanage extends JFrame {

	private JPanel contentPane;
	private JTextField forenamefield;
	private JTextField surnamefield;
	private JTable searchtable;
	private static JTable plantable;
	private JScrollPane searchscrollPane = new JScrollPane();
	private JPanel tableselectpanel = new JPanel();
	static JPanel currentplanpanel = new JPanel();
	public static JPanel addplanpanel = new JPanel();
	public static int patientrowSelectedID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Patientmanage frame = new Patientmanage();
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
	public Patientmanage() {
		setTitle("Patient Management");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1146, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton homebtn = new JButton("Home");
		homebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Secretaryhome Secretaryhome = new Secretaryhome();   
		        setVisible(false); // Hide current frame
		        Secretaryhome.setVisible(true);
			}
		});
		homebtn.setBounds(10, 11, 79, 23);
		contentPane.add(homebtn);
		
		JPanel searchpanel = new JPanel();
		searchpanel.setLayout(null);
		searchpanel.setBorder(new TitledBorder(null, "Search For a Patient:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchpanel.setBounds(10, 45, 167, 151);
		contentPane.add(searchpanel);
		
		forenamefield = new JTextField();
		forenamefield.setColumns(10);
		forenamefield.setBounds(75, 47, 86, 20);
		searchpanel.add(forenamefield);
		
		surnamefield = new JTextField();
		surnamefield.setColumns(10);
		surnamefield.setBounds(75, 78, 86, 20);
		searchpanel.add(surnamefield);
		
		JLabel label = new JLabel("Forename:");
		label.setBounds(6, 50, 78, 14);
		searchpanel.add(label);
		
		JLabel label_1 = new JLabel("Surname:");
		label_1.setBounds(6, 81, 59, 14);
		searchpanel.add(label_1);
		
		JButton searchbtn = new JButton("Search");
		searchbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//try search
				try {
					searchforpatient();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		searchbtn.setBounds(37, 117, 89, 23);
		searchpanel.add(searchbtn);
		
		JButton registerbtn = new JButton("Register New Patient");
		registerbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				RegisterPatient RegisterPatient = new RegisterPatient();  
		        RegisterPatient.setVisible(true);
			}
		});
		registerbtn.setBounds(10, 208, 167, 23);
		contentPane.add(registerbtn);
		
		
		tableselectpanel.setBounds(187, 23, 943, 393);
		contentPane.add(tableselectpanel);
		tableselectpanel.setLayout(null);
		tableselectpanel.setVisible(false);
		
		JLabel lblCurrentInformation = new JLabel("Select a row to view the patients health plan or to add a new one:");
		lblCurrentInformation.setBounds(10, 11, 463, 23);
		tableselectpanel.add(lblCurrentInformation);
		searchscrollPane.setBounds(10, 45, 923, 334);
		tableselectpanel.add(searchscrollPane);
		
		searchtable = new JTable();
		searchtable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				//gets values from the selected row
				int i = searchtable.getSelectedRow();

		        TableModel model = searchtable.getModel();
		        
		        String Fname = model.getValueAt(i,1).toString();
		        String Sname = model.getValueAt(i,2).toString();
		        String Postcode = model.getValueAt(i,6).toString();
		        String Housenumber = model.getValueAt(i,5).toString();
		        
		        try {
		        	patientrowSelectedID = getIdfromselectedrow(Fname, Sname, Postcode, Housenumber);
				} catch (Exception e) {
				}
		        
		        try {
					if (planExists()==true) {
						currentplanpanel.setVisible(true);
						addplanpanel.setVisible(false);
						fillcurrentplantable();
					}
					else {
						addplanpanel.setVisible(true);
						currentplanpanel.setVisible(false);
					}
				} catch (Exception e) {
				}
		        
			}
		});
		searchscrollPane.setViewportView(searchtable);
		
		currentplanpanel.setBounds(187, 422, 820, 88);
		contentPane.add(currentplanpanel);
		currentplanpanel.setLayout(null);
		currentplanpanel.setVisible(false);
		
		JLabel lblCurrentPlanInformation = new JLabel("Current Plan Information:");
		lblCurrentPlanInformation.setBounds(10, 11, 153, 14);
		currentplanpanel.add(lblCurrentPlanInformation);
		
		JScrollPane planscrollPane = new JScrollPane();
		planscrollPane.setBounds(10, 36, 604, 36);
		currentplanpanel.add(planscrollPane);
		
		plantable = new JTable();
		planscrollPane.setViewportView(plantable);
		
		JButton cancelplanbtn = new JButton("Cancel Plan");
		cancelplanbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					cancelSelectedPlan();
					addplanpanel.setVisible(true);
					currentplanpanel.setVisible(false);
				} catch (Exception e) {
				}
			}
		});
		cancelplanbtn.setBounds(624, 49, 153, 23);
		currentplanpanel.add(cancelplanbtn);
		
		
		addplanpanel.setBounds(10, 260, 177, 43);
		contentPane.add(addplanpanel);
		addplanpanel.setLayout(null);
		
		JButton addplanbtn = new JButton("Add Healthcare Plan");
		addplanbtn.setBounds(0, 11, 170, 23);
		addplanpanel.add(addplanbtn);
		addplanbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Healthcaresubscribe Healthcaresubscribe = new Healthcaresubscribe();  
				Healthcaresubscribe.setVisible(true);
			}
		});
		addplanpanel.setVisible(false);
	}

	public boolean planExists() throws Exception{
		Statement stmt = null;
		int ID = 0;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			 
			 String SQL = "SELECT patientID FROM TreatmentsUsed  WHERE patientID = ? ";;
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setInt(1, patientrowSelectedID);
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
	
	//using the values passsed in founds the unique patient id relating to these values
	public int getIdfromselectedrow(String Fname, String Sname, String Postcode, String Housenumber) throws Exception{
		Statement stmt = null;
		int ID = 0;
		
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			 
			 String SQL = "SELECT patientID FROM Patient NATURAL JOIN Address WHERE forename = ? AND surname = ? AND postcode = ? AND houseNumber = ?";;
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, Fname);
				pstmt.setString(2, Sname);
				pstmt.setString(3, Postcode);
				pstmt.setString(4, Housenumber);
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
			String query = "SELECT title, forename, surname, dateOfBirth, phonenumber, houseNumber, postcode, streetName, districtName, cityName  FROM Patient NATURAL JOIN Address WHERE forename LIKE '%" + forenamefield.getText() + "%' AND surname LIKE '%" + surnamefield.getText() + "%' ";
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
			 searchtable.setModel(model);
			
			 tableselectpanel.setVisible(true);
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		
	} 
	
	public static void fillcurrentplantable() throws Exception{
		Statement stmt = null;
		DefaultTableModel model = new DefaultTableModel(new String[]{"Plan:", "Checkups Used", "Cleans used", "Repairs used", "Start Date"}, 0);
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			String SQL = "SELECT planName, checkUpsUsed, hygieneVisitsUsed, repairsUsed, startDate FROM TreatmentsUsed WHERE patientID = ?";
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, patientrowSelectedID);
			ResultSet rs = pstmt.executeQuery();
			
			 while(rs.next())
			 {
			     String a = rs.getString("planName");
			     String b = rs.getString("checkUpsUsed");
			     String c = rs.getString("hygieneVisitsUsed");
			     String d = rs.getString("repairsUsed");
			     String e = rs.getString("startDate");

			     model.addRow(new Object[]{a, b, c, d, e,});
			 }
			 
			 //searchscrollPane.add(new JScrollPane(table));
			 plantable.setModel(model);
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		
	}                    
	
	public void cancelSelectedPlan() throws Exception{
		Statement stmt = null;
		
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			String SQL = "DELETE FROM TreatmentsUsed WHERE patientID = ?";
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, patientrowSelectedID);
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
