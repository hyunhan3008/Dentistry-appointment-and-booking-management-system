package Group_Project;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class testing_sql {
	public static void main(String[] args) throws Exception {
		Statement stmt = null;
		
		try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team009", "team009", "9e81b723")){
			stmt = con.createStatement();
			
			/*
			String updateTableSQL = "UPDATE Appointment SET startTime = ?  WHERE startTime = ?";
			PreparedStatement pstmt = con.prepareStatement(updateTableSQL);
			pstmt.setString(1, "09:00");
			pstmt.setString(2, "9:00");
			pstmt.executeUpdate();
			*/
			
			ResultSet res = stmt.executeQuery("SELECT * FROM Appointment");
			while (res.next()) {
				String planName = res.getString("appointmentId");
				String title = res.getString("patientID");
				String forename = res.getString("appointmentDate");
				String surname = res.getString("startTime");
				String id = res.getString("endTime");
				String partner = res.getString("partner");
				System.out.println(" " + planName + " " + title + " " + forename + " " + surname + " " + id + " " + partner + " ");
				//System.out.println(id);
			}
					
			//RETRIEVES COLUMN NAMES FOR A TABLE
			DatabaseMetaData meta = con.getMetaData();
		     ResultSet resultSet = meta.getColumns("team009", null, "Appointment", "%");
		     while (resultSet.next()) {
		       System.out.println(resultSet.getString(4));
			}
		
			
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
