package Group_Project;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Recordvisit extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private Boolean alreadyRecorded;

	
	public static Boolean checkExist(int appointmentID) throws SQLException{
		PreparedStatement pstmt = null;
		Boolean exist=false;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			
			pstmt = con.prepareStatement("SELECT * FROM Visit WHERE appointmentId=?");
			pstmt.setObject(1,appointmentID);
			ResultSet res = pstmt.executeQuery();
			exist=res.next();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
				if (pstmt != null)
					pstmt.close();
		
		}
		return exist;
	}
	public static Vector<Vector<Object>> getTreatments() throws SQLException{
		Statement stmt = null;
		Vector<Vector<Object>> treatments = null;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			
			stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT treatmentName FROM Treatment");
			treatments = new Vector<Vector<Object>>();
			while (res.next()) {
				Vector<Object> vector = new Vector<Object>();
				vector.add(res.getString(1));	
				vector.add(false);
				treatments.add(vector);
			};
		
			
		}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally {
					if (stmt != null)
						stmt.close();
			
			}
		return treatments;
		}
	
	public static void recordTreatment(String treatmentName, int appointmentID) throws SQLException{
		PreparedStatement pstmt = null;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			
			pstmt = con.prepareStatement("INSERT INTO Visit(appointmentId,treatmentName) VALUES(?,?)");
			pstmt.setObject(1,appointmentID);
			pstmt.setObject(2,treatmentName);
			pstmt.executeUpdate();
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
				if (pstmt != null)
					pstmt.close();
		
		}
	}
	
	
		/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
			}
		});
	}

	public static DefaultTableModel buildRecordVisitTable()
	        throws SQLException {

			Vector<String> columnNames = new Vector<String>(); 
			columnNames.add("Treatment");
			columnNames.add("Received?");
			
			Vector<Vector<Object>> data = getTreatments(); 
			
			return new DefaultTableModel(data, columnNames);
	}
	/**
	 * Create the frame.
	 */
	public Recordvisit(int appointmentID, String title, String forename, String surname,String  startTime) {
		setTitle("Record a visit");
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(25, 15, 450, 35);
		contentPane.add(textArea);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 60, 450, 300);
		contentPane.add(scrollPane);
		
		JButton btnRecordVisit = new JButton("Record Visit");

		JTable recordTreatments;
		try {
			textArea.setText("Record visit for " + title + " "  + forename + " " + surname + " at " + startTime +".");
			
			if (!checkExist(appointmentID)) {
				recordTreatments = new JTable(buildRecordVisitTable()){

					private static final long serialVersionUID = 1L;
            
					public Class getColumnClass(int column) {
						switch (column) {
							case 0:
								return String.class;
							default:
								return Boolean.class;
						}
					}
				};
				recordTreatments.setRowHeight(40);
				scrollPane.setViewportView(recordTreatments);
				setLocationRelativeTo(null);
				btnRecordVisit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int treatmentCount = recordTreatments.getRowCount();
						for (int i=0;i<treatmentCount;i++) {
							if ((Boolean) recordTreatments.getValueAt(i, 1)) {
								String treatment = (String) recordTreatments.getValueAt(i, 0);
									try {
										recordTreatment(treatment,appointmentID);
									} catch (SQLException e1) {
										e1.printStackTrace();
									}
							}
						}
						setVisible(false);
						final JDialog dialog = new JDialog();
						dialog.setAlwaysOnTop(true);    
						JOptionPane.showMessageDialog(dialog,
			    		    "Visit Recorded Successfully!",
			    		    "Confirmation",
			    		    JOptionPane.INFORMATION_MESSAGE);
					}
			
				});
			}
			else {
				btnRecordVisit.setEnabled(false);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		btnRecordVisit.setBounds(100, 390, 300, 50);
		contentPane.add(btnRecordVisit);
		
		
		
		
		
		
	}
}
