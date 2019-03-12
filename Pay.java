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
public class Pay extends HttpServlet {
	public static String products_list = "";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String total = request.getParameter("total");
		
		database db = new database();
		out.println(db.get_html(out, "header.html"));
		out.println("<div class=\"payable_div\"><span class=\"payable_amt\">Amount payable: <span>"+database.currency+""+total+"</span></span></div>");
		out.println(db.get_html(out, "pay.html"));
		out.println(db.get_html(out, "footer.html"));
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		database db = new database();
	
		out.println(db.get_html(out, "header.html"));
		out.println("<div class=\"payable_div\"><span class=\"payable_amt\">Payment Successful</span></div>");
		out.println(db.get_html(out, "footer.html"));
		
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			c.setMaxAge(0);
			response.addCookie(c);
		}
	}
}