package com.example.budgetnotebook;

public class Recommendation {
	
	private int _id;
	private String c1;
	private String c2;
	private String c3;
	private String c4;
	private String c5;
	private boolean iv;
	
	public Recommendation() {}
	
	public Recommendation (String c1, String c2, String c3, String c4, String c5, boolean iv) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.c5 = c5;
		this.iv = iv;
	}
	//Getters --------------------------------------------------------------------
		public int getId(){
			return _id;
		}
		
		public String getC1(){
			return c1;
		}
		
		public String getC2(){
			return c2;
		}
		
		public String getC3(){
			return c3;
		}
		
		public String getC4(){
			return c4;
		}
		
		public String getC5(){
			return c5;
		}
		
		public boolean getIV(){
			return iv;
		}
		
		//Setters --------------------------------------------------------------------
		public void setId(int id){
			this._id = id;
		}
		
		public void setC1(String c1) {
			this.c1 = c1;
		}

		public void setC2(String c2) {
			this.c2 = c2;
		}

		public void setC3(String c3) {
			this.c3 = c3;
		}

		public void setC4(String c4) {
			this.c4 = c4;
		}

		public void setC5(String c5) {
			this.c5 = c5;
		}

		public void setIV(boolean iv) {
			this.iv = iv;
		}
}
