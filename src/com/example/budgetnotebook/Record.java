package com.example.budgetnotebook;

public class Record {
	private String item = "";
	private String amount = "";
	private String type = "";
	private String notes = "";

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return (getItem());
	}

	public String getNotes() {
		return (notes);
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
