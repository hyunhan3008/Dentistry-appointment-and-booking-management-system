package Group_Project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Secretaryhome extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Secretaryhome frame = new Secretaryhome();
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
	public Secretaryhome() {
		setResizable(false);
		setTitle("Sheffield Dental Care");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton viewcalendar = new JButton("View Calendar");
		viewcalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CalenderView CalenderView = new CalenderView();   
		        setVisible(false); // Hide current frame
		        CalenderView.setVisible(true);
			}
		});
		viewcalendar.setBounds(10, 150, 160, 50);
		contentPane.add(viewcalendar);
		
		JButton manageappointments = new JButton("Manage Appointments");
		manageappointments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Appointmentmanage Appointmentmanage = new Appointmentmanage();   
		        setVisible(false); // Hide current frame
		        Appointmentmanage.setVisible(true);
				
			}
		});
		manageappointments.setBounds(180, 150, 160, 50);
		contentPane.add(manageappointments);
		
		JButton managepatient = new JButton("Manage Patient");
		managepatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Patientmanage Patientmanage = new Patientmanage();   
		        setVisible(false); // Hide current frame
		        Patientmanage.setVisible(true);
			}
		});
		managepatient.setBounds(350, 150, 160, 50);
		contentPane.add(managepatient);
		
		JButton bookholiday = new JButton("Book Holiday");
		bookholiday.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookholiday bookholiday = new bookholiday();   
		        setVisible(false); // Hide current frame
		        bookholiday.setVisible(true);
			}
		});
		bookholiday.setBounds(520, 150, 160, 50);
		contentPane.add(bookholiday);
		
		JButton billing = new JButton("Billing");
		billing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Billing Billing = new Billing();   
		        setVisible(false); // Hide current frame
		        Billing.setVisible(true);
			}
		});
		billing.setBounds(690, 150, 160, 50);
		contentPane.add(billing);
		
		JLabel label = new JLabel("Welcome");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 24));
		label.setBounds(355, 50, 150, 50);
		contentPane.add(label);
		
		JButton dentist = new JButton("Dentist");
		dentist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dentisthygienist dentist = new dentisthygienist("Dentist");   
		        setVisible(false); // Hide current frame
		        dentist.setVisible(true);
			}
		});
		dentist.setBounds(200, 250, 160, 50);
		contentPane.add(dentist);
		
		JButton hygienist = new JButton("Hygienist");
		hygienist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dentisthygienist hygienist = new dentisthygienist("Hygienist");   
		        setVisible(false); // Hide current frame
		        hygienist.setVisible(true);
			}
		});
		hygienist.setBounds(460, 250, 160, 50);
		contentPane.add(hygienist);
	}
}
