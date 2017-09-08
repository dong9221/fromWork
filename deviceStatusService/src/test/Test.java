package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	public static void main(String[] args) {
		testOJDBC();
	}

	private static void testOJDBC() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			FileOutputStream outputStream = new FileOutputStream(new File("d:\tt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.80:1521:gakk","its_rz","its_rz");
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from d_bjlx_t");
			while(resultSet.next()){
				System.out.println(resultSet.getString("BJLX")+":"+resultSet.getString("BJLXMC"));
			}
			"".contains("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(connection!=null)
				connection.close();
				if(statement!=null)
				statement.close();
				if(resultSet!=null)
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
}
