

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockPortfolio.stock;

import java.sql.*;
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
			String[] stockInfo = new String[2];
			 stockInfo = userStockInfo(email);
			 
			String userBalanceString = String.valueOf(userBalance);
			
			HttpSession session = request.getSession();
			session.setAttribute("email", email);
			session.setAttribute("balance", userBalanceString);
			
			session.setAttribute("ticker", stockInfo[0]);
			session.setAttribute("shares", stockInfo[1]);
			
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
	
	protected String[] userStockInfo(String email) {
		Connection con;
		
		String[] stockInfo = new String[2];
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String user = "stockInfo";
			String pwd = "stock123";
			
			con = DriverManager.getConnection(mysqlConnection,user,pwd); 
			
			Statement s = con.createStatement();
			
			ResultSet myResult = s.executeQuery("select * from userTable where email='"+email+"'");
			
			if(myResult.next()) {
				
				//return myResult.getInt(5);
				stockInfo[0] = myResult.getString(2); //ticker
				stockInfo[1] = myResult.getString(3); //shares
				
			}
			
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return stockInfo;
	}

}
