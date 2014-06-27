package com.example.budgetnotebook;

public class Goal {

	private int id;
	private int a_id;
	private String name;
	private String description;
	private String type;
	private String start_amount;
	private String delta_amount;
	private String end_date;
	
	public Goal(){}
	
	public Goal(String name, String description, String type, String start_amount, String delta_amount, String end_date) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.start_amount = start_amount;
		this.delta_amount = delta_amount;
		this.end_date = end_date;
	}
	
	@Override
	public String toString() {
		return "Goal [id=" + id + ", a_id=" + a_id +", name=" + name + ", description=" + description + ", type=" + type + ", start_amount=" + start_amount + ", delta_amount=" + delta_amount + ", end_date=" + end_date +"]";
	}
	
	//Getters --------------------------------------------------------------------
	public int getId(){
		return id;
	}
	
	public int getAId(){
		return a_id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getType(){
		return type;
	}
	
	public String getStartAmount(){
		return start_amount;
	}
	
	public String getDeltaAmount(){
		return delta_amount;
	}
	
	public String getEndDate(){
		return end_date;
	}
	
	//Setters --------------------------------------------------------------------
	public void setId(int id){
		this.id = id;
	}
	
	public void setAId(int a_id){
		this.a_id = a_id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setStartAmount(String start_amount){
		this.start_amount = start_amount;
	}
	
	public void setDeltaAmount(String delta_amount){
		this.delta_amount = delta_amount;
	}
	
	public void setEndDate(String end_date){
		this.end_date = end_date;
	}
}
