

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.http.HttpResponse;

import kong.unirest.Unirest;
import kong.unirest.JsonNode;
import kong.unirest.HttpResponse;

import java.io.PrintWriter;

import java.sql.*;


@WebServlet("/tickerSearch")
public class tickerSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public tickerSearch() {
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
		
		String userTicker = request.getParameter("ticker");
		String tickerQuantity = request.getParameter("tickerQuantity");
		String path = "/portfolio.jsp";
		
		HttpSession session = request.getSession(false);
		String email = (String)session.getAttribute("email");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//This is api call url using ticker gotten from user and api token key
		//https://cloud.iexapis.com/stable/stock/aapl/quote?token=pk_a4d2e1125be54222ac14a504ed3831ef
		
		String apiUrl = "https://cloud.iexapis.com/stable/stock/" + userTicker + "/quote?token=pk_a4d2e1125be54222ac14a504ed3831ef";
		
		final HttpResponse<JsonNode> apiResponse = Unirest.get(apiUrl).asJson();
		
		
		
		if(apiResponse.getBody() == null) {
			out.println("<h2>Invalid ticker Symbol</h2>");
			getServletContext()
				.getRequestDispatcher(path)
				.include(request,response);
		}
		
		
		//else upload to database which should hopefuly update on front end
		else {
			
			String symbol = apiResponse.getBody().getObject().getString("symbol");
			String priceBought = Double.toString(apiResponse.getBody().getObject().getDouble("latestPrice"));// equal to latest price
			
			//if user has enough money to buy stock
			if(canBuy(email,priceBought,tickerQuantity)) {
				updateStockTable(email,symbol,tickerQuantity,priceBought); //input into stockTable 
				//update customers balance once they buy a stock
				
				System.out.println("you were able to buy stock");
				
				HttpSession stockSession = request.getSession(false);
				//try setting attribute in session again so that i can be retrieved by jsp file
				ArrayList<String> tickerArr = new ArrayList<String>();
				ArrayList<String> shareArr = new ArrayList<String>();
				ArrayList<String> valuesArr = new ArrayList<String>();
				
				tickerArr = getTicker(email);
				shareArr = getShares(email);
				valuesArr = getValues(email);
				
				System.out.println("size of ticker: " + tickerArr.size());
				
				stockSession.setAttribute("ticker", tickerArr);
				stockSession.setAttribute("shares", shareArr);
				stockSession.setAttribute("value", valuesArr);
				
				System.out.println("data sent to portfolio.jsp");
				
				response.sendRedirect("portfolio.jsp");
			}
			
			else {
				out.println("Unable to buy, insufficent funds");
				getServletContext()
				.getRequestDispatcher(path)
				.include(request,response);
			}
			
			
		}
		
		
	}
	
	//need email, ticker, num of shares, and price bought
	protected void updateStockTable(String email, String ticker, String numOfShares, String priceBought) {
		Connection con;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String dbUser = "stockInfo";
			String dbPassword = "stock123";
			con = DriverManager.getConnection(mysqlConnection,dbUser,dbPassword);
			Statement s = con.createStatement();
			
			
			//check for preexisitng ticker
			ResultSet myResult = s.executeQuery("select * from stockTable where email='"+email+"'AND ticker='"+ticker+"'");
			
			if(myResult.next()) {
				
				
				int currShares = Integer.parseInt(myResult.getString(3));
				int incomingShares = Integer.parseInt(numOfShares);
				int totalShares = currShares+incomingShares;
				numOfShares = Integer.toString(totalShares);
				
				String query = "update stockTable set shares = '"+numOfShares+"' where email='"+email+"'AND ticker ='"+ticker+"'";
				s.executeUpdate(query);
			}
				
			else {
				String query = "insert into stockTable " + "(email,ticker,shares,price_bought)" + "values ('" + email + "','" + ticker + "','" + numOfShares + "','" + priceBought+"')";
				s.executeUpdate(query);
			}
			
			
		}
		
		catch (ClassNotFoundException e){
			e.printStackTrace();
			
		}
		
		catch (SQLException e){
			e.printStackTrace();
		}
		
		System.out.println("stock successfully added");
	
		
	}
	
	protected boolean canBuy(String email, String priceBought, String quantity) {
		Connection con;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String dbUser = "stockUser";
			String dbPassword = "stock123";
			con = DriverManager.getConnection(mysqlConnection,dbUser,dbPassword);
			Statement s = con.createStatement();
			
			
			//check for preexisitng ticker
			ResultSet myResult = s.executeQuery("select * from userTable where email='"+email+"'");
			
				
				//newUser = false;
			
				if(myResult.next()) {
					
					
					Double price = Double.parseDouble(priceBought);
					int tickerQuantity = Integer.parseInt(quantity);
					int stockBalance = myResult.getInt(5);
					Double remainingBalance = stockBalance - (price*tickerQuantity);
					if(remainingBalance > 0) {
						//insert into db
						//return true
						
						//String query = "update userTable " + "(balance)" + "values ('"+remainingBalance+"')";
						String query = "update userTable set balance = '"+remainingBalance+"' where email='"+email+"'";
						
						s.executeUpdate(query);
						return true;

					}
				
			}
						
				
		}
		
		catch (ClassNotFoundException e){
			e.printStackTrace();
			
		}
		
		catch (SQLException e){
			e.printStackTrace();
		}
		
		//System.out.println("stock successfully added");
		return false;
		
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
