package com.example.budgetnotebook;

public class Transaction {
	private int _id;
	private int t_a_id;
	private String transaction_name;
	private String transaction_date;
	private String transaction_amount;
	private String transaction_category;
	private String transaction_type;
	private String transaction_interval;
	private String transaction_description;
	private boolean transaction_accounted;
	private String transaction_change;
	private String t_change_color;
	
	public Transaction(){}
	
	public Transaction(int t_a_id, String transaction_name, String transaction_date, String transaction_amount, String transaction_category, String transaction_type, String transaction_interval, String transaction_description, boolean transaction_accounted, String transaction_change, String t_change_color) {
		super();
		this.t_a_id = t_a_id;
		this.transaction_name = transaction_name;
		this.transaction_date = transaction_date;
		this.transaction_amount = transaction_amount;
		this.transaction_category = transaction_category;
		this.transaction_type = transaction_type;
		this.transaction_interval = transaction_interval;
		this.transaction_description = transaction_description;
		this.transaction_accounted = transaction_accounted;
		this.transaction_change = transaction_change;
		this.t_change_color = t_change_color;
	}
	
	@Override
	public String toString() {
		return "Transaction [id=" + _id + ", t_a_id=" + t_a_id + ", transaction_name=" + transaction_name + ", transaction_date=" + transaction_date + ", transaction_amount=" + transaction_amount + ", transaction_category=" + transaction_category + ", transaction_type=" + transaction_type + ", transaction_interval=" + transaction_interval + ", transaction_description=" + transaction_description +", transaction_accounted=" + transaction_accounted + "transaction_change=" + transaction_change + "t_change_color=" + t_change_color + "]";
	}
	
	//Getters --------------------------------------------------------------------
	public int getId(){
		return _id;
	}
	
	public int getAID() {
		return t_a_id;
	}
	
	public String getName() {
		return transaction_name;
	}
	
	public String getDate() {
		return transaction_date;
	}
	
	public String getAmount() {
		return transaction_amount;
	}
	
	public String getCategory() {
		return transaction_category;
	}
	
	public String getType() {
		return transaction_type;
	}
	
	public String getInterval() {
		return transaction_interval;
	}
	
	public String getDescription() {
		return transaction_description;
	}
	
	public boolean getAccounted() {
		return transaction_accounted;
	}
	
	public String getChange() {
		return transaction_change;
	}
	
	public String getCColor() {
		return t_change_color;
	}
	
	//Setters --------------------------------------------------------------------	
	public void setId(int id){
		this._id = id;
	}
	
	public void setAId(int a_id){
		this.t_a_id = a_id;
	}
	
	public void setName(String name){
		this.transaction_name = name;
	}
	
	public void setDate(String transaction_date) {
		this.transaction_date = transaction_date;
	}
	
	public void setAmount(String transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	
	public void setCategory(String transaction_category) {
		this.transaction_category = transaction_category;
	}
	
	public void setType(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	public void setInterval(String transaction_interval) {
		this.transaction_interval = transaction_interval;
	}
	
	public void setDescription(String transaction_description) {
		this.transaction_description = transaction_description;
	}
	
	public void setAccounted(boolean transaction_accounted) {
		this.transaction_accounted = transaction_accounted;
	}
	
	public void setChange(String transaction_change) {
		this.transaction_change = transaction_change;
	}

	public void setCColor(String t_change_color) {
		this.t_change_color = t_change_color;
	}
	
}
