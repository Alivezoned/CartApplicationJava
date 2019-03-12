import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.lang.*;
/*
Assignment 12 - Question 1
Shopping Cart (Using Cooking and things)
*/
class database {
		//_____________________________________________
		private String database_username = "SYSTEM2";
		private String database_password = "system";
		public static String currency = "&#8377;";
		// Rupee code for html &#8377;
		//_____________________________________________
	
		public static Connection con = null;
		private Statement stmt = null;
		private ResultSet rs = null;
		
		database() {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", database_username, database_password);
				stmt = con.createStatement();	
				
				executeQuery("drop table Shop");
				executeQuery("create table shop(p_id number(5,0) primary key, p_name varchar2(100), p_price number(10,0), p_img varchar2(100))");
				executeUpdate("insert into shop VALUES(1, 'iPhone XS (256 GB Black)', 114900, '1')");
				executeUpdate("insert into shop VALUES(2, 'Galaxy S9+ (512 GB Black)', 59990, '2')");
				executeUpdate("insert into shop VALUES(3, 'Honor 8 (64 GB White)', 21900, '3')");
				executeUpdate("insert into shop VALUES(4, 'Note 7 Pro (64 GB Black)', 13999, '4')");
				executeUpdate("insert into shop VALUES(5, 'Xperia XZ2 (128 GB Black)', 73500, '5')");
				executeUpdate("insert into shop VALUES(6, 'Vivo V15 Pro (128 GB)', 28990, '6')");
				executeUpdate("insert into shop VALUES(7, 'Oppo K1 (64 GB Astral Blue)', 16990, '7')");
				executeUpdate("insert into shop VALUES(8, 'Galaxy S10 (Prism Black)', 66900, '8')");
			} catch(Exception ex) { System.out.println("Database Class: "+ex.toString()); ex.printStackTrace(); }
		}
	
		public ResultSet executeQuery(String query) throws Exception {	
			if (con != null) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
			}
			return rs;
		}
		public void executeUpdate(String query) {
			try { stmt.executeUpdate(query); } catch(Exception ex){}
		}
		public String getData(String column_name, String id, PrintWriter out) {
			try {
				rs = stmt.executeQuery("select "+column_name+" from shop where p_id='"+id+"'");
				rs.next();
				return rs.getString(1);
			} catch (Exception ex) {}
			return "";
		}
		
		
		//___________________________________
		// -------------- IGNORE THE BELOW --------------
		public String get_html(PrintWriter out, String file_name) {
			String line = "__", finalLine = "";
			try {
				BufferedReader br = new BufferedReader(new FileReader("tomcat\\webapps\\ROOT\\WEB-INF\\assignment_12\\"+file_name));
				while (line != null) { line = br.readLine(); finalLine += "\n"+line; }
			} catch (Exception ex) {out.println("get_html() -- "+ex.toString()); ex.printStackTrace();}
			finalLine = finalLine.replace("null","");
			return finalLine;
		}
		
		public static Cookie getCookie(HttpServletRequest request, String name) {
			Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if (cookie.getName().equals(name)) {
							return cookie;
						}
					}
				}
			return null;
		}
}