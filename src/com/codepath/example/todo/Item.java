package com.codepath.example.todo;

public class Item {
	private long id;
	private String item;
	private long completion_date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public long getCompletionDate() {
		return completion_date;
	}

	public void setCompletionDate(long completion_date) {
		this.completion_date = completion_date;
	}
}
