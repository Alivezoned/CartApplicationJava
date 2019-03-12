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
public class Shop extends HttpServlet {
	// Display products list. Function print products fills this string.
	public static String products_list = "";
	public database db = new database();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println(db.get_html(out,"header.html"));
		out.println(printProducts());
		out.println(db.get_html(out,"footer.html"));
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println(db.get_html(out,"header.html"));
		out.println(printProducts());
		
		String id = request.getParameter("product_id");
		String price = request.getParameter("product_price");

		add_to_cart(id, price, response, request, out);
		
		out.println(db.get_html(out, "footer.html"));
	}
	
	//--------------- Shop Related -------------------
	// Add product to cart (add to cookie)
	public void add_to_cart(String id, String price, HttpServletResponse response, HttpServletRequest request, PrintWriter out) {			
		Cookie cookie = null; String value = ""; String[] values;
		cookie = database.getCookie(request,id);
		if (cookie != null) {
			value = cookie.getValue();
			values = value.split(",");
			int count = Integer.parseInt(values[1]);
			count = count + 1;
			
			Cookie temp = new Cookie(id, values[0]+","+count);
			temp.setMaxAge(60*60);
			response.addCookie(temp);
		} else {
			Cookie temp = new Cookie(id, price+",1");
			temp.setMaxAge(60*60);
			response.addCookie(temp);
		}
	}
	
	// Print product list
	public String printProducts() {
		String code = "<div class=\"products_list\">";
		try {
			ResultSet rs = db.executeQuery("select * from shop");
			while (rs.next()) {
					code += add_product_html(rs.getString(4),rs.getString(2),rs.getString(3),rs.getString(1));
			}		
		} catch (Exception ex) {}
		code += "</div>";
		return code;
	}
	//_______________________________
	//--------------- HTML related -------------------
	// HTML design for product list (to display the list)
	public String add_product_html(String img, String prod_name, String price, String id) {
		String path = "images/";
	
		String code = "<div class=\"product_bar\">"+
		"<center><div class=\"product_image\">"+ 
		"<img src=\""+path+img+".png\">"+
		"</div><br><div class=\"description\">"+
		"<form method=\"POST\">"+
		"<li class=\"name\">"+prod_name+"</li>"+
		"<li class=\"price\">Price: <span class=\"price_value\">"+database.currency+""+price+"</span></li>"+
		"<input type=\"hidden\" value=\""+id+"\" name=\"product_id\">"+
		"<input type=\"hidden\" value=\""+price+"\" name=\"product_price\">"+
		"<input type=\"submit\" value=\"ADD TO CART\" class=\"add_to_cart\">"+
		"</form></div></center></div>";
		return code;
	}
}