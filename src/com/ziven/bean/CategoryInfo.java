package com.ziven.bean;

public class CategoryInfo {
	private boolean isTitle;
	private String title;

	private String imageUrl1;
	private String imageUrl2;
	private String imageUrl3;

	private String name1;
	private String name2;
	private String name3;
	public synchronized boolean isTitle() {
		return isTitle;
	}
	public synchronized void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getImageUrl1() {
		return imageUrl1;
	}
	public synchronized void setImageUrl1(String imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}
	public synchronized String getImageUrl2() {
		return imageUrl2;
	}
	public synchronized void setImageUrl2(String imageUrl2) {
		this.imageUrl2 = imageUrl2;
	}
	public synchronized String getImageUrl3() {
		return imageUrl3;
	}
	public synchronized void setImageUrl3(String imageUrl3) {
		this.imageUrl3 = imageUrl3;
	}
	public synchronized String getName1() {
		return name1;
	}
	public synchronized void setName1(String name1) {
		this.name1 = name1;
	}
	public synchronized String getName2() {
		return name2;
	}
	public synchronized void setName2(String name2) {
		this.name2 = name2;
	}
	public synchronized String getName3() {
		return name3;
	}
	public synchronized void setName3(String name3) {
		this.name3 = name3;
	}
	
}
