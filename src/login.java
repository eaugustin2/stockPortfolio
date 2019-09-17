

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
		
		String nextPage = "/portfolio.jsp"; //should next page be output from server?
		String notUser = "/index.jsp";
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		String email = request.getParameter("loginEmail");
		String password = request.getParameter("loginPassword");
		
		if(isUser(email,password)) {
			
			//alot more has to be done before page is redirected
			/*
			 * Make java bean for stock Table
			 * Have to set up session for portfolio.jsp
			 * Have to make stock Table
			 * have to make connection to API
			 */
			
			getServletContext()
			.getRequestDispatcher(nextPage)
			.forward(request,response);
		}
		
		else {
			out.println("Email or password is incorrect");
			getServletContext()
			.getRequestDispatcher(notUser)
			.include(request, response);
		}
		
		
		
	}
	
	protected boolean isUser(String email, String password) {
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
				
				return true;
				
			}
			
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return false;
	}

}
