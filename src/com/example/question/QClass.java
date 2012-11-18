package com.example.question;

public class QClass {

	String number = null;
	String text = null;
	String answer1 = null;
	String answer2 = null;
	String answer3 = null;
	String answer4 = null;
	String right = null;
	String audience = null;
	String phone = null;
	String fifty1 = null;
	String fifty2= null;

	public QClass() {
	}
	
	public QClass(String number, String text, String answer1, String answer2, String answer3, String answer4, String right, String audience, String phone, String fifty1, String fifty2) {
		this.number = number;
		this.text = text;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.right = right;
		this.audience = audience;
		this.phone = phone;
		this.fifty1 = fifty1;
		this.fifty2= fifty2;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public int getRight() {
		return Integer.parseInt(right);
	}

	public void setRight(String right) {
		this.right = right;
	}

	public int getAudience() {
		return Integer.parseInt(audience);
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public int getPhone() {
		return Integer.parseInt(phone);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getFifty1() {
		return Integer.parseInt(fifty1);
	}

	public void setFifty1(String fifty1) {
		this.fifty1 = fifty1;
	}

	public int getFifty2() {
		return Integer.parseInt(fifty2);
	}

	public void setFifty2(String fifty2) {
		this.fifty2 = fifty2;
	}

	public int getNumber() {
		return Integer.parseInt(number);
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
