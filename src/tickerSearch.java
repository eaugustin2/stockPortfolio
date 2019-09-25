

import java.io.IOException;
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
		
		//have to have a way to redirect if user enters an invalid ticker symbol
		
		//System.out.println(apiResponse.getBody().getObject().ge);
		
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
			
			if(canBuy(email,priceBought)) {
				updateStockTable(email,symbol,tickerQuantity,priceBought); //input into stockTable 
				//update customers balance once they buy a stock
				
				System.out.println("you were able to buy stock");
				response.sendRedirect("portfolio.jsp");
			}
			
			else {
				out.println("Unable to buy, insufficent funds");
				getServletContext()
				.getRequestDispatcher(path)
				.include(request,response);
			}
			
			
			
		}
		
		//System.out.println(email);
		//System.out.println(apiResponse.getBody().getObject());
		
		//response.sendRedirect("portfolio.jsp");
		
		/*
		 * making api calls from tickerSearch
		 */
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
			ResultSet myResult = s.executeQuery("select shares from stockTable where email='"+email+"'AND ticker='"+ticker+"'");
			
			if(myResult.next()) {
				
				//newUser = false;
				
				//add current numOfshares with incoming num of shares
				int currShares = Integer.parseInt(myResult.getString(3));
				int incomingShares = Integer.parseInt(numOfShares);
				int totalShares = currShares+incomingShares;
				numOfShares = Integer.toString(totalShares);
				
				
			}
			
			
				String query = "insert into stockTable " + "(email,ticker,shares,price_bought)" + "values ('" + email + "','" + ticker + "','" + numOfShares + "','" + priceBought+"')";
				
				s.executeUpdate(query);
			
			
			
		}
		
		catch (ClassNotFoundException e){
			e.printStackTrace();
			
		}
		
		catch (SQLException e){
			e.printStackTrace();
		}
		
		System.out.println("stock successfully added");
		
		//return newUser;
		
	}
	
	protected boolean canBuy(String email, String priceBought) {
		Connection con;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String mysqlConnection = "jdbc:mysql://localhost/stockPortfolio?serverTimezone=UTC";
			String dbUser = "stockUser";
			String dbPassword = "stock123";
			con = DriverManager.getConnection(mysqlConnection,dbUser,dbPassword);
			Statement s = con.createStatement();
			
			
			//check for preexisitng ticker
			ResultSet myResult = s.executeQuery("select balance from stockUser where email='"+email+"'");
			
			
				
				//newUser = false;
			
				
				int price = Integer.parseInt(priceBought);
				int balance = myResult.getInt(5);
				
				if(balance - price > 0) {
					//insert into db
					//return true
					String query = "insert into stockUser " + "balance" + "values ('"+ balance+"')";
					
					s.executeUpdate(query);
					return true;

				}
				
				
					//return false;
				
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
	

}
