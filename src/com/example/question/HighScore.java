package com.example.question;

public class HighScore implements Comparable<HighScore>{

	String name;
	int scoring;
	
	public HighScore() {
	}
	
	public HighScore (String name, int scoring) {
		this.name = name;
		this.scoring = scoring;
	}


	@Override
	public int compareTo(HighScore o) {
		// TODO Auto-generated method stub
		if (this.getScoring() > o.getScoring()) {
			return 1;
		}
		else if (this.getScoring() < o.getScoring()) {
			return -1;
		}
		else {
			return 0;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScoring() {
		return scoring;
	}

	public void setScoring(int scoring) {
		this.scoring = scoring;
	}
}
