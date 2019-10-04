

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockPortfolio.stock;

import java.sql.*;
import java.util.ArrayList;
import java.io.PrintWriter;


@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public login() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		//need to make sure that this servlet can be reached from front end form
		
		String nextPage = "portfolio.jsp"; //should next page be output from server?
		String notUser = "/index.jsp";
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		String email = request.getParameter("loginEmail");
		String password = request.getParameter("loginPassword");
		
		int userBalance = isUser(email,password);
		
		
		
		if(userBalance >= 0) {
			
			//alot more has to be done before page is redirected
			/*
			 * Have to set up session for portfolio.jsp
			 * have to make connection to API, in next servlet, not this one
			 */
			
			//stock stockP = new stock();
			//stockP.setEmail(email); //passing over email retrieved from user to access/use stockTable in DB
			
			ArrayList<String> tickerArr = new ArrayList<String>();
			tickerArr = getTicker(email);
			
			ArrayList<String> sharesArr = new ArrayList<String>();
			 sharesArr = getShares(email);
			 
			 ArrayList<String> valueArr = new ArrayList<String>();
			 valueArr = getValues(email);
			 
			String userBalanceString = String.valueOf(userBalance);
			
			Double sum = 0.0;
			for(int i =0; i<valueArr.size();i++) {
				sum+=Double.parseDouble(valueArr.get(i));
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("email", email);
			session.setAttribute("balance", userBalanceString);
			
			//session.setAttribute("ticker", stockInfo[0]);
			session.setAttribute("ticker", tickerArr);
			session.setAttribute("shares", sharesArr);
			session.setAttribute("value", valueArr);
			session.setAttribute("portfolioValue", sum);
			
			session.setMaxInactiveInterval(10*60);
			
			
			
			/*
			getServletContext()
			.getRequestDispatcher(nextPage)
			.forward(request,response);
			*/
			
			response.sendRedirect(nextPage);
		}
		
		else {
			out.println("Email or password is incorrect");
			getServletContext()
			.getRequestDispatcher(notUser)
			.include(request, response);
		}
		
		
		
	}
	
	protected int isUser(String email, String password) {
		Connection con;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String user = "stockUser";
			String pwd = "stock123";
			
			con = DriverManager.getConnection(mysqlConnection,user,pwd); 
			
			Statement s = con.createStatement();
			
			ResultSet myResult = s.executeQuery("select * from userTable where email='"+email+"'");
			
			if(myResult.next()) {
				
				return myResult.getInt(5);
				
			}
			
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return -1;
	}
	
	protected ArrayList<String> getShares(String email) {
		Connection con;
		
		ArrayList<String> sharesArr = new ArrayList<String>();
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String user = "stockInfo";
			String pwd = "stock123";
			
			con = DriverManager.getConnection(mysqlConnection,user,pwd); 
			
			Statement s = con.createStatement();
			
			ResultSet myResult = s.executeQuery("select * from stockTable where email='"+email+"'");
			
			while(myResult.next()) {
				sharesArr.add(myResult.getString(3));
			}
			
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return sharesArr;
	}
	
	protected ArrayList<String> getTicker(String email){
		Connection con;
		
		ArrayList<String> tickerArr = new ArrayList<String>();
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String user = "stockInfo";
			String pwd = "stock123";
			
			con = DriverManager.getConnection(mysqlConnection,user,pwd); 
			
			Statement s = con.createStatement();
			
			ResultSet myResult = s.executeQuery("select * from stockTable where email='"+email+"'");
			
			
			
			while(myResult.next()) {
				tickerArr.add(myResult.getString(2));
				
				//update info based on how many shares they have 
				
				
			}
			
			
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return tickerArr;
	}
	
	protected ArrayList<String> getValues(String email){
		Connection con;
		
		ArrayList<String> valueArr = new ArrayList<String>();
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String user = "stockInfo";
			String pwd = "stock123";
			
			con = DriverManager.getConnection(mysqlConnection,user,pwd); 
			
			Statement s = con.createStatement();
			
			ResultSet myResult = s.executeQuery("select * from stockTable where email='"+email+"'");
			
			
			
			while(myResult.next()) {
				//valueArr.add(myResult.getString(4));
				
				int tickerQuantity = Integer.parseInt(myResult.getString(3));
				Double value = Double.parseDouble(myResult.getString(4));
				
				Double totalVal = tickerQuantity * value;
				String newVal = Double.toString(totalVal);
				valueArr.add(newVal);
			}
			
			
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return valueArr;
	}

}
