package stockPortfolio;

import java.io.Serializable;

public class stock implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String email;
	private String ticker;
	private int shares;
	private int priceBought;
	
	public stock() {
		this.email = "";
		this.ticker = "";
		this.shares = 0;
		this.priceBought = 0;
	}
	
	public stock(String email, String ticker, int shares, int priceBought) {
		this.email = email;
		this.ticker = ticker;
		this.shares = shares;
		this.priceBought = priceBought;
	}
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	
	public int getPriceBought() {
		return priceBought;
	}
	public void setPriceBought(int priceBought) {
		this.priceBought = priceBought;
	}
	
	
}
