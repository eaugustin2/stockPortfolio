

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.PrintWriter;


@WebServlet("/register")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public register() {
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
		
		String finished = "/index.jsp";
		String notFinished = "/register.jsp";
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String firstName = request.getParameter("fName");
		String lastName = request.getParameter("lName");
		String email = request.getParameter("email");
		String password = request.getParameter("regPassword");
		
		//have to check DB first to make sure email is not already registered
		if(!isNewUser(firstName,lastName,email,password)) {
			out.println("An account with this email already exists");
			getServletContext()
				.getRequestDispatcher(notFinished)
				.include(request,response);
		}
		
		else {
			out.println("Account created successfully");
			getServletContext()
				.getRequestDispatcher(finished)
				.include(request,response);
		}
		
		
	}
	
	protected boolean isNewUser(String fname, String lname, String email, String password) {
		 double balance = 5000.00;
		 boolean newUser = true;
		 
		Connection con;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String dbUser = "stockUser";
			String dbPassword = "stock123";
			con = DriverManager.getConnection(mysqlConnection,dbUser,dbPassword);
			Statement s = con.createStatement();
			
			ResultSet myResult = s.executeQuery("select * from userTable where email='"+email+"'");
			
			if(myResult.next()) {
				
				newUser = false;
				
			}
			
			else {
				String query = "insert into userTable " + "(first_name,last_name,email,password,balance)" + "values ('" + fname + "','" + lname + "','" + email + "','" + password + "','" + balance +"')";
				
				s.executeUpdate(query);
			}
			
			
		}
		
		catch (ClassNotFoundException e){
			e.printStackTrace();
			
		}
		
		catch (SQLException e){
			e.printStackTrace();
		}
		
		System.out.println("user successfully added");
		
		return newUser;
		
	}

}
