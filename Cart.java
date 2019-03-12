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
public class Cart extends HttpServlet {
	public static String show_cart = "";
	public database db = new database();

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		show_cart = display_cart(request,out);
			
		int total = -1;
		try { total = getTotalAmount(request); } catch(Exception ex){}
		
		out.println(db.get_html(out,"header.html"));
		out.println("<form method=GET action=\"Pay\" class=\"checkout\">");
		if (total > 0) {
			out.println("<span class=\"total\">Total:</span> <span class=\"total_price\">"+database.currency+""+total+"</span><br>");
			out.println("<input type=hidden value="+total+" name=total><input type=submit value=checkout>");
		} else {
			out.println("<div class=\"payable_div\"><span class=\"payable_amt\">No Items in your Cart</span></div>");
		}
		out.println("</form>");
		out.println(show_cart);
		
		out.println(db.get_html(out,"footer.html"));
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try {
			String item_id = request.getParameter("item_id");
			delete_from_cart(response, request, item_id);
		} catch(Exception ex){}
		show_cart = display_cart(request,out);
		out.println("<script>window.location = \"http://localhost:9090//Cart\";</script>");
	}
	
	//--------------- Shop Related -------------------
	// Delete id from cookie (From cart actually)
	public void delete_from_cart(HttpServletResponse response,HttpServletRequest request, String id) {
		Cookie cookies[] = request.getCookies();
		Cookie cookie = null;
		for (Cookie c : cookies) {
			String name = c.getName();
			if (id.contains(name)) {
				cookie = c;
			}
		}
		String temp[] = (cookie.getValue()).split(",");
		cookie.setValue(temp[0]+",0");
		response.addCookie(cookie);
	}
	
	// Displays the cart items
	public String display_cart(HttpServletRequest request, PrintWriter out) {
		String s = "";
		
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				String name = c.getName();
				String value = c.getValue();
				
				String[] values = value.split(",");
				int v = Integer.parseInt(values[1]);
				// Product count should be more than 1
				if (v > 0) {
					String p_name = db.getData("p_name", name, out);
					String p_price = db.getData("p_price", name, out);
					
					String cart_list = "<div class=\"product-box\"><ul><img alt=\"\" src=\"images/"+name+".png\"></ul>"+
					"<ul class=\"product-details\">"+
					"<li><span class=\"product-name\">"+p_name+"</span></li>"+
					"<li class=\"product-list-price\">"+
					"<input type=\"text\" name=\"item_amount\" value=\""+values[1]+"\" disabled>"+
					"<span class=\"product-price\">"+database.currency+""+p_price+"</span></li>"+
					"</ul><div class=\"product-fields\"><form method=POST>"+
					"<input type=\"hidden\" name=\"delete\" value=\"delete\"/>"+
					"<input type=\"hidden\" name=\"item_id\" value=\""+name+"\">"+
					"<input class=\"close-btn\" type=\"submit\" value=\"x\"></form></div></div>";
					s+=cart_list;
				}
			}
		}
		return s;
	}
	
	// Returns total price to be paid for cart items
	public int getTotalAmount(HttpServletRequest request) {
		int total = 0;
		Cookie c[] = request.getCookies();
		for (int i=0; i<c.length; i++) {
			String name = c[i].getName();
			String value = c[i].getValue();
			String[] values = value.split(",");
			
			int price = Integer.parseInt(values[0]);
			int count = Integer.parseInt(values[1]);
			
			int product_total = (price*count);
			total += product_total;
		}
		return total;
	}
}