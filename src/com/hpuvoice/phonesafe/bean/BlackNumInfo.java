package com.hpuvoice.phonesafe.bean;


public class BlackNumInfo {

	private String name;
	private String number;
	private int title_id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getTitle_id() {
		return title_id;
	}
	public void setTitle_id(int title_id) {
		if(title_id > 0&& title_id <9){
			this.title_id = title_id;
		}else{
			this.title_id = 2;
		}
	}
	public BlackNumInfo(String name, String number, int title_id) {
		super();
		this.name = name;
		this.number = number;
		this.title_id = title_id;
	}
	public BlackNumInfo() {
	}
	
	@Override
	public String toString() {
		return "BlackNumInfo [name=" + name + ", number=" + number
				+ ", title_id=" + title_id +"]";
	}
	
	
	
}
