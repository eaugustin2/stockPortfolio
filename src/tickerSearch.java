

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;

import kong.unirest.Unirest;


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
		
		//This is api call url using ticker gotten from user and api token key
		//https://cloud.iexapis.com/stable/stock/aapl/quote?token=pk_a4d2e1125be54222ac14a504ed3831ef
		HttpResponse<String> apiResponse = Unirest.get("https://cloud.iexapis.com/stable/stock/aapl/quote?token=pk_a4d2e1125be54222ac14a504ed3831ef");
		.asString();
		response.sendRedirect("portfolio.jsp");
		
		/*
		 * making api calls from tickerSearch
		 */
	}

}
