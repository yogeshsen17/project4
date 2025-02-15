package com.rays.pro4.Bean;

import java.util.Date;

public class PrescriptionBean extends BaseBean {

	public String Name;
	public String Decase;
	public Date Date;
	public int Capacity;
	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDecase() {
		return Decase;
	}
	public void setDecase(String decase) {
		Decase = decase;
	}
	public Date getDate() {
		return Date;
	}
	public void setDate(Date date) {
		Date = date;
	}
	public int getCapacity() {
		return Capacity;
	}
	public void setCapacity(int capacity) {
		Capacity = capacity;
	}
}