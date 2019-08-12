package Group_Project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;

public class Healthcaresubscribe extends JFrame {

	private JPanel contentPane;
	private JTextField treatmentsfield;
	private JTextField checkupsfield;
	private JTextField cleansfield;
	private JComboBox planfield = new JComboBox();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Healthcaresubscribe frame = new Healthcaresubscribe();
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
	public Healthcaresubscribe() {
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Subscribe to a Healthcare Plan");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 372, 247);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Select a plan:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(26, 11, 320, 192);
		contentPane.add(panel);
		
		treatmentsfield = new JTextField();
		treatmentsfield.setEditable(false);
		treatmentsfield.setColumns(10);
		treatmentsfield.setBounds(159, 52, 126, 20);
		panel.add(treatmentsfield);
		
		JLabel lblPlanType = new JLabel("Plan type:");
		lblPlanType.setBounds(10, 27, 59, 14);
		panel.add(lblPlanType);
		
		JLabel lblTreatmentsCovered = new JLabel("Treatments Covered:");
		lblTreatmentsCovered.setBounds(10, 58, 136, 14);
		panel.add(lblTreatmentsCovered);
		
		//button action used to commit new subscirbe record
		JButton btnSubscribe = new JButton("Subscribe");
		btnSubscribe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//checks user has selected a plan and if so does insert else shows error
				if ((String) planfield.getSelectedItem()!=""){
				try {
					addpatienttoplan();
					final JDialog dialog = new JDialog();
			    	dialog.setAlwaysOnTop(true);    
					JOptionPane.showMessageDialog(dialog,
			    		    "Subscription Successful!",
			    		    "Confirmation",
			    		    JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
				}
				setVisible(false);
				Patientmanage.addplanpanel.setVisible(false);
				Patientmanage.currentplanpanel.setVisible(true);
				//updates the currentplan to show newly added plan
				try {
					Patientmanage.fillcurrentplantable();
				} catch (Exception e1) {
				}
				}
				else {
					final JDialog dialog = new JDialog();
			    	dialog.setAlwaysOnTop(true);    
					JOptionPane.showMessageDialog(dialog,
			    		    "Must Select a Healthcare plan.",
			    		    "Input error",
			    		    JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		btnSubscribe.setBounds(92, 158, 102, 23);
		panel.add(btnSubscribe);
		
		JLabel lblCheckupsCovered = new JLabel("Check-ups Covered:");
		lblCheckupsCovered.setBounds(10, 89, 116, 14);
		panel.add(lblCheckupsCovered);
		
		checkupsfield = new JTextField();
		checkupsfield.setEditable(false);
		checkupsfield.setColumns(10);
		checkupsfield.setBounds(159, 83, 126, 20);
		panel.add(checkupsfield);
		
		JLabel lblCleansCovered = new JLabel("Hygiene Visits Covered:");
		lblCleansCovered.setBounds(10, 119, 151, 14);
		panel.add(lblCleansCovered);
		
		cleansfield = new JTextField();
		cleansfield.setEditable(false);
		cleansfield.setColumns(10);
		cleansfield.setBounds(159, 113, 126, 20);
		panel.add(cleansfield);
		
		
		planfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			//fills the fields of checkups covered etc based on plan selected
				
			String selected = (String) planfield.getSelectedItem();
				if (selected==("NHS free plan")) {
					checkupsfield.setText("2");
					cleansfield.setText("2");
					treatmentsfield.setText("6");
				}
				else if (selected==("Maintenance plan")) {
					checkupsfield.setText("2");
					cleansfield.setText("2");
					treatmentsfield.setText("0");
				}
				else if (selected==("Oral Health plan")) {
					checkupsfield.setText("2");
					cleansfield.setText("4");
					treatmentsfield.setText("0");
				}
				else if (selected==("Dental Repair plan")) {
					checkupsfield.setText("2");
					cleansfield.setText("2");
					treatmentsfield.setText("2");
				}


			}
		});
		planfield.setModel(new DefaultComboBoxModel(new String[] {"", "NHS free plan", "Maintenance plan", "Oral Health plan", "Dental Repair plan"}));
		planfield.setBounds(159, 24, 126, 20);
		panel.add(planfield);
		setLocationRelativeTo(null);
	}
	
	//assigns the patient to the selected plan
	public void addpatienttoplan() throws Exception{
		Statement stmt = null;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			//gets the curent date
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			
			//insert new record to database
			String SQL = "INSERT INTO TreatmentsUsed VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, Patientmanage.patientrowSelectedID);
			pstmt.setString(2, (String) planfield.getSelectedItem());
			pstmt.setInt(3, 0);
			pstmt.setInt(4, 0);
			pstmt.setInt(5, 0);
			//formats the current date to a string
			pstmt.setString(6,dtf.format(localDate));
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
}
