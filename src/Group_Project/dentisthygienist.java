package Group_Project;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

@SuppressWarnings("serial")
public class dentisthygienist extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
					dentisthygienist frame = new dentisthygienist("Dentist");
					frame.setVisible(true);
				
			}
		});
		
	}
	/**
	 * Create the frame.
	 */
	public dentisthygienist(String user){
		setResizable(false);
		setTitle("My Appointments");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1197, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTodaysAppointments = new JLabel("Todays Appointments:");
		lblTodaysAppointments.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTodaysAppointments.setBounds(280, 22, 200, 40);
		contentPane.add(lblTodaysAppointments);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 75, 700, 300);
		contentPane.add(scrollPane);

		JButton btnRecordVisit = new JButton("Record Visit");
		btnRecordVisit.setEnabled(false);
		btnRecordVisit.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		btnRecordVisit.setBounds(775, 236, 400, 139);
		contentPane.add(btnRecordVisit);
		
		// It creates and displays the table
		JTable todayApptTable;
		try {
			todayApptTable = new JTable(buildTodayApptTable(user)){

				private static final long serialVersionUID = 1L;
	    
				public boolean isCellEditable(int row, int column) {
			        return false;
			    }
			};;
			todayApptTable.setFont(new Font("Tahoma", Font.PLAIN, 20));
			TableColumnModel tcm = todayApptTable.getColumnModel();
			tcm.removeColumn(tcm.getColumn(0));
			scrollPane.setViewportView(todayApptTable);
			setLocationRelativeTo(null);
			todayApptTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
				 public void valueChanged(ListSelectionEvent event) {
						btnRecordVisit.setEnabled(true);
				 }
				});
			todayApptTable.setRowHeight(40);
			btnRecordVisit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int selected = todayApptTable.getSelectedRow();
					int appointmentID =  Integer.parseInt(todayApptTable.getModel().getValueAt(selected, 0).toString());
					String title = (String) todayApptTable.getValueAt(selected, 0);
					String forename = (String) todayApptTable.getValueAt(selected, 1);
					String surname = (String) todayApptTable.getValueAt(selected, 2);
					String startTime = (String) todayApptTable.getValueAt(selected,3);
					try {
						if (!checkExist(appointmentID)) {
							Recordvisit recordVisit = new Recordvisit(appointmentID, title, forename, surname, startTime);
							recordVisit.setVisible(true);
						}
						else {
							final JDialog dialog = new JDialog();
							dialog.setAlwaysOnTop(true);  
							JOptionPane.showMessageDialog(dialog,
				    		    "This visit has already been recorded",
				    		    "Error",
				    		    JOptionPane.INFORMATION_MESSAGE);
						}
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});


		} catch (SQLException e) {
			btnRecordVisit.setEnabled(true);
			e.printStackTrace();
		}
		
		
	    		
		
		JLabel lblNextAppointment = new JLabel("Next Appointment:");
		lblNextAppointment.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNextAppointment.setBounds(887, 22, 175, 40);
		contentPane.add(lblNextAppointment);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 20));
		try {
			textArea.setText(findNextAppt(user));
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textArea.setBounds(775, 75, 400, 114);
		contentPane.add(textArea);
		
		JButton home = new JButton("Home");
		home.setFont(new Font("Tahoma", Font.PLAIN, 20));
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Secretaryhome Secretaryhome = new Secretaryhome();   
		        setVisible(false); // Hide current frame
		        Secretaryhome.setVisible(true);
			}
		});
		home.setBounds(33, 22, 127, 40);
		contentPane.add(home);
					
					
	}
	public static Vector<Vector<Object>> getTodaysAppts(String user) throws SQLException{
		PreparedStatement pstmt = null;
		Vector<Vector<Object>> data = null;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String today = dateFormat.format(date);
			
			pstmt = con.prepareStatement("SELECT * FROM Appointment NATURAL JOIN Patient WHERE partner=? AND appointmentDate=? ORDER BY startTime ASC");
			pstmt.setObject(1,user);
			pstmt.setObject(2,today);
			ResultSet rs = pstmt.executeQuery();
			
			// data of the table
			data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				vector.add(rs.getInt("appointmentId"));
				vector.add(rs.getString("title"));
				vector.add(rs.getString("forename"));
				vector.add(rs.getString("surname"));
				vector.add(rs.getString("startTime"));
				vector.add(rs.getString("endTime"));
				vector.add(rs.getString("type"));
				data.add(vector);
			}
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
				if (pstmt != null)
					pstmt.close();
		
		}
		return data;
	}
	
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
			e.printStackTrace();
		}
		
		finally {
				if (pstmt != null)
					pstmt.close();
		
		}
		return exist;
	}
	
	public static DefaultTableModel buildTodayApptTable(String user)
	        throws SQLException {

			Vector<String> columnNames = new Vector<String>(); 
			columnNames.addElement("appointmentID");
			columnNames.add("Title");
			columnNames.add("Patient Forename");
			columnNames.add("Patient Surname");
			columnNames.add("Start Time");
			columnNames.add("End Time");
			columnNames.add("Appointment Type");
			
			Vector<Vector<Object>> data = getTodaysAppts(user);
			
			
			return new DefaultTableModel(data, columnNames);
	}

	public static String findNextAppt(String user) throws SQLException{
		Vector<Vector<Object>> data = getTodaysAppts(user);
		
		DateFormat dateFormat = new SimpleDateFormat("HHmm");
		Date current = new Date();
		int currentTime = Integer.parseInt(dateFormat.format(current));
		int[] time = new int[data.size()];
		for (int i = 0; i<data.size(); i++) {
			time[i] = Integer.parseInt(data.get(i).get(4).toString().substring(0,2) + data.get(i).get(4).toString().substring(3));
			
		}
		int nextIndex = 100;
		Boolean stop = false;
		for (int i=0; i<data.size(); i++) {
			if (!stop) {
				if (time[i] > currentTime) {
					nextIndex = i;
					stop=true;
				}
			
				else {
					nextIndex=100;
				}
			}
			
		}
		String next;
		if (nextIndex==100){
			next = "You have no more appointments today";
		}
		else {
			String title = data.get(nextIndex).get(1).toString();
			String forename = data.get(nextIndex).get(2).toString();
			String surname = data.get(nextIndex).get(3).toString();
			String startTime = data.get(nextIndex).get(4).toString();
			String endTime = data.get(nextIndex).get(5).toString();
			next = "Your next appoinment is with " + title + " " + forename + " " + surname + " at " + startTime + " until " + endTime + ".";
		}
		return next;
	}
}
