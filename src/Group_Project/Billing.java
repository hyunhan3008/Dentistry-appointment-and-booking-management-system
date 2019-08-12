package Group_Project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;

public class Billing extends JFrame {

	private JPanel contentPane;
	private JTextField forenamefield;
	private JTextField surnamefield;
	private JTable patientTable;
	private JTable treatmentsTable;
	public static int patientrowSelectedID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Billing frame = new Billing();
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
	public Billing() {
		setTitle("Billing");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 686, 484);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton home = new JButton("Home");
		home.setFont(new Font("Tahoma", Font.PLAIN, 15));
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Secretaryhome Secretaryhome = new Secretaryhome();   
		        setVisible(false); // Hide current frame
		        Secretaryhome.setVisible(true);
			}
		});
		home.setBounds(10, 14, 79, 44);
		contentPane.add(home);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(null);
		searchPanel.setBorder(new TitledBorder(null, "Search For a Patient:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchPanel.setBounds(101, 14, 567, 44);
		contentPane.add(searchPanel);
		
		forenamefield = new JTextField();
		forenamefield.setColumns(10);
		forenamefield.setBounds(125, 17, 86, 20);
		searchPanel.add(forenamefield);
		
		surnamefield = new JTextField();
		surnamefield.setColumns(10);
		surnamefield.setBounds(320, 17, 86, 20);
		searchPanel.add(surnamefield);
		
		JLabel label = new JLabel("Forename:");
		label.setBounds(57, 20, 72, 14);
		searchPanel.add(label);
		
		JLabel label_1 = new JLabel("Surname:");
		label_1.setBounds(249, 20, 59, 14);
		searchPanel.add(label_1);
		
		JButton search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//try search
				try {
					searchforpatient();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		search.setBounds(454, 16, 89, 23);
		searchPanel.add(search);
		
		
		
		
		JScrollPane patientPane = new JScrollPane();
		patientPane.setBounds(33, 71, 635, 77);
		contentPane.add(patientPane);
		
		JPanel costPanel = new JPanel();
		costPanel.setBounds(33, 313, 276, 123);
		costPanel.setBorder(new TitledBorder(null, "Costs:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(costPanel);
		costPanel.setLayout(null);
		
		JLabel lblAbsolouteTotal = new JLabel("Absoloute Total:");
		lblAbsolouteTotal.setBounds(10, 30, 112, 14);
		costPanel.add(lblAbsolouteTotal);
		
		JLabel lblDeductions = new JLabel("Plan Deductions:");
		lblDeductions.setBounds(10, 60, 157, 14);
		costPanel.add(lblDeductions);
		
		JLabel lblNewTotal = new JLabel("Total After Deductions:");
		lblNewTotal.setBounds(10, 93, 157, 14);
		costPanel.add(lblNewTotal);
		
		JTextArea txtAbsolute = new JTextArea();
		txtAbsolute.setText("");
		txtAbsolute.setBounds(152, 26, 112, 22);
		costPanel.add(txtAbsolute);
		
		JTextArea txtDeductions = new JTextArea();
		txtDeductions.setText("");
		txtDeductions.setBounds(152, 56, 112, 22);
		costPanel.add(txtDeductions);
		
		JTextArea txtFinalTotal = new JTextArea();
		txtFinalTotal.setText("");
		txtFinalTotal.setBounds(152, 89, 112, 22);
		costPanel.add(txtFinalTotal);
		
		JTextArea txtInfo = new JTextArea();
		txtInfo.setFont(new Font("Monospaced", Font.BOLD, 18));
		txtInfo.setBounds(33, 155, 635, 25);
		contentPane.add(txtInfo);
		txtInfo.setVisible(false);
		
		patientTable = new JTable();
		patientTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				//gets values from the selected row
				int i = patientTable.getSelectedRow();

		        TableModel model = patientTable.getModel();
		        
		        String Fname = model.getValueAt(i,1).toString();
		        String Sname = model.getValueAt(i,2).toString();
		        String Postcode = model.getValueAt(i,6).toString();
		        String Housenumber = model.getValueAt(i,5).toString();
		        
		        try{
		        	patientrowSelectedID = getIdfromselectedrow(Fname, Sname, Postcode, Housenumber);
		        	txtInfo.setText("Unpaid treatments for "+Fname+" "+Sname);
		        	txtInfo.setVisible(true);
		        }
		        catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	        
		        try{
		        	int[] costs = fillCostTable();
		        	int absoluteCost = costs[0];
		        	int deductions = costs[1];
		        	int finalCost = costs[2];
		        	txtAbsolute.setText("£"+absoluteCost);
		        	txtDeductions.setText("£"+deductions);
		        	txtFinalTotal.setText("£"+finalCost);
		        }
		        catch (SQLException e) {
		        	e.printStackTrace();
		        }
			}
		});

		patientPane.setViewportView(patientTable);
		
		
		
		JScrollPane treatmentsPane = new JScrollPane();
		treatmentsPane.setBounds(33, 188, 635, 112);
		contentPane.add(treatmentsPane);
		
		treatmentsTable = new JTable(){

			private static final long serialVersionUID = 1L;
    
			public Class getColumnClass(int column) {
				switch (column) {
					case 0:
						return String.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return int.class;
					default:
						return Boolean.class;
				}
			}
			public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		treatmentsPane.setViewportView(treatmentsTable);
		setLocationRelativeTo(null);
		JButton paybill = new JButton("Pay Bill");
		paybill.setFont(new Font("Tahoma", Font.PLAIN, 25));
		paybill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					payBill();
					DefaultTableModel model = (DefaultTableModel) treatmentsTable.getModel();
					model.setRowCount(0);
					fillCostTable();
					txtAbsolute.setText("£"+0);
			    	txtDeductions.setText("£"+0);
			    	txtFinalTotal.setText("£"+0);
					final JDialog dialog = new JDialog();
					dialog.setAlwaysOnTop(true);    
					JOptionPane.showMessageDialog(dialog,
		    		    "Bill Paid Successfully!",
		    		    "Confirmation",
		    		    JOptionPane.INFORMATION_MESSAGE);
				}
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		paybill.setBounds(377, 313, 291, 112);
		contentPane.add(paybill);
		
		
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
			 patientTable.setModel(model);
			
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		
		
	} 
	
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
		catch (Exception ex) {
				ex.printStackTrace();
		}
		finally {
				if (stmt != null)
					stmt.close();
		}
		return ID;
	}
	
	public int[] getTreatmentsLeft() throws SQLException{
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		String planName=null;
		int checkUpsUsed = 0;
		int hygieneUsed = 0;
		int repairsUsed = 0;
		int checkUpsAllowed = 0;
		int hygieneAllowed = 0;
		int repairsAllowed = 0;
		int[] treatmentLeft = new int[6];
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			
			pstmt = con.prepareStatement("SELECT * FROM TreatmentsUsed WHERE patientID = ?");
			pstmt.setInt(1, patientrowSelectedID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
					planName = rs.getString("planName");
					checkUpsUsed = rs.getInt("checkUpsUsed");
					hygieneUsed = rs.getInt("hygieneVisitsUsed");
					repairsUsed = rs.getInt("repairsUsed");
			}
			rs.close();
			pstmt2 = con.prepareStatement("SELECT * FROM HealthcarePlan where planName=?");
			pstmt2.setString(1, planName);
			ResultSet planDetails = pstmt2.executeQuery();
			if (planName != null) {
				while (planDetails.next()){
					checkUpsAllowed = planDetails.getInt("checkUpsCovered");
					hygieneAllowed = planDetails.getInt("hygieneVisitsCovered");
					repairsAllowed = planDetails.getInt("repairWorkCovered");				}
			}
			treatmentLeft[0] = checkUpsAllowed - checkUpsUsed;
			treatmentLeft[1] = hygieneAllowed - hygieneUsed;
			treatmentLeft[2] = repairsAllowed - repairsUsed;
			treatmentLeft[3] = checkUpsUsed;
			treatmentLeft[4] = hygieneUsed;
			treatmentLeft[5] = repairsUsed;
			
		}
		catch (SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (pstmt != null)
					pstmt.close();
				if (pstmt2 != null)
					pstmt2.close();
		}
		return treatmentLeft;
	}
	
	public int[] fillCostTable() throws SQLException{
		PreparedStatement pstmt = null;
		DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Time", "Treatment","Cost", "Covered in Plan?"}, 0);
		String date = null;
		String startTime = null;
		String treatment = null;
		int cost = 0;
		int absoluteCost = 0;
		int finalCost = 0;
		int deductions = 0;
		int checkUpsUsed=0;
		int hygieneUsed=0;
		int repairUsed=0;
		int[]costs = new int[3];
		Boolean prePaid = false;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			pstmt = con.prepareStatement("SELECT * FROM Appointment NATURAL JOIN Visit WHERE patientID = ? AND paidFor=false ORDER BY appointmentDate, startTime");
			pstmt.setInt(1, patientrowSelectedID);
			ResultSet rs = pstmt.executeQuery();
			int[] treatmentsLeft = getTreatmentsLeft();
			checkUpsUsed = treatmentsLeft[3];
			hygieneUsed = treatmentsLeft[4];
			repairUsed = treatmentsLeft[5];
			 while(rs.next())
			 {
			     date = rs.getString("appointmentDate");
			     startTime = rs.getString("startTime");
			     treatment = rs.getString("treatmentName");
			     cost = getCost(treatment);
			     absoluteCost+=cost;
			     finalCost+=cost;
			     if (treatment.equals("Check-up")) {
			    	 if (treatmentsLeft[0]>0) {
			    		 prePaid = true;
			    		 treatmentsLeft[0]--;
			    		 finalCost-=cost;
			    		 deductions+=cost;
			    		 checkUpsUsed++;
			    	 }
			     }
			     else if (treatment.equals("Hygiene Visit")) {
			    	 if (treatmentsLeft[1]>0) {
			    		 prePaid=true;
			    		 treatmentsLeft[1]--;
			    		 finalCost-=cost;
			    		 deductions+=cost;
			    		 hygieneUsed++;
			    	 }
			     }
			     else {
			    	 if(treatmentsLeft[2]>0) {
			    		 prePaid=true;
			    		 treatmentsLeft[2]--;
			    		 finalCost-=cost;
			    		 deductions+=cost;
			    		 repairUsed++;
			    	 }
			     }
			     model.addRow(new Object[]{date, startTime, treatment ,cost,prePaid});
			     prePaid=false;
			     updateTreatments(checkUpsUsed,hygieneUsed,repairUsed);
			 }
			 costs[0] = absoluteCost;
			 costs[1] = deductions;
			 costs[2] = finalCost;
			 treatmentsTable.setModel(model);
		}
		catch(SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (pstmt != null)
					pstmt.close();
		}
		return costs;
		 
	}
	
	public int getCost(String name) throws SQLException{
		PreparedStatement pstmt = null;
		int cost = 0;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			pstmt = con.prepareStatement("SELECT * FROM Treatment WHERE treatmentName=?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cost = rs.getInt("cost");
			}
		}
		catch(SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (pstmt != null)
					pstmt.close();
		}
		return cost;
	}
	
	public void payBill() throws SQLException{
		PreparedStatement pstmt = null;
		try(Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			pstmt = con.prepareStatement("SELECT appointmentId,treatmentName FROM Appointment NATURAL JOIN Visit WHERE patientID = ? AND paidFor=false ORDER BY appointmentDate, startTime");
			pstmt.setInt(1, patientrowSelectedID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int appointmentId = rs.getInt("appointmentId");
				String treatment = rs.getString("treatmentName");
				payTreatment(appointmentId, treatment);
			}
		
		}
		catch(SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (pstmt != null)
					pstmt.close();
		}
	}
	
	public void payTreatment(int appointmentId, String treatment) throws SQLException {
		PreparedStatement pstmt = null;
		try(Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			pstmt = con.prepareStatement("UPDATE Visit SET paidFor = true where appointmentId = ? and treatmentName=?");
			pstmt.setInt(1, appointmentId);
			pstmt.setString(2, treatment);
			pstmt.executeUpdate();
		}
		catch(SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (pstmt != null)
					pstmt.close();
		}
	}
	
	public void updateTreatments(int checkUpsUsed, int hygieneUsed, int repairUsed) throws SQLException {
		PreparedStatement pstmt = null;
		try(Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			pstmt = con.prepareStatement("UPDATE TreatmentsUsed SET checkUpsUsed=?, hygieneVisitsUsed=?, repairsUsed=? where patientID=?");
			pstmt.setInt(1, checkUpsUsed);
			pstmt.setInt(2, hygieneUsed);
			pstmt.setInt(3, repairUsed);
			pstmt.setInt(4, patientrowSelectedID);
			pstmt.executeUpdate();
		}
		catch(SQLException ex) {
				ex.printStackTrace();
		}
		finally {
				if (pstmt != null)
					pstmt.close();
		}
	}
	
}

