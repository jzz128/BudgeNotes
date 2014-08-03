package com.example.budgetnotebook;

public class Recommendation {
		
	private int _id;
	private String category1;
	private String category2;
	private String category3;
	private String category4;
	private String category5;
	private boolean isValid;
	
	public Recommendation() {}
	
	public Recommendation (String category1, String category2, 
						   String category3, String category4, 
						   String category5, boolean isValid) {
		super();
		this.category1 = category1;
		this.category2 = category2;
		this.category3 = category3;
		this.category4 = category4;
		this.category5 = category5;
		this.isValid = isValid;
	}
	//Getters --------------------------------------------------------------------
		public int getId(){
			return _id;
		}
		
		public String getCategory1(){
			return category1;
		}
		
		public String getCategory2(){
			return category2;
		}
		
		public String getCategory3(){
			return category3;
		}
		
		public String getCategory4(){
			return category4;
		}
		
		public String getCategory5(){
			return category5;
		}
		
		public boolean getIsValid(){
			return isValid;
		}
		
		//Setters --------------------------------------------------------------------
		public void setId(int id){
			this._id = id;
		}
		
		public void setCategory1(String c1) {
			this.category1 = c1;
		}

		public void setCategory2(String c2) {
			this.category2 = c2;
		}

		public void setCategory3(String c3) {
			this.category3 = c3;
		}

		public void setCategory4(String c4) {
			this.category4 = c4;
		}

		public void setCategory5(String c5) {
			this.category5 = c5;
		}

		public void setIsValid(boolean iv) {
			this.isValid = iv;
		}
}
