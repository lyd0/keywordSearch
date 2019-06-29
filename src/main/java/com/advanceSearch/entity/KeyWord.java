package com.advanceSearch.entity;


public class KeyWord {
	private String keyWord;

	public KeyWord(){

	}

	public KeyWord(String key){
		this.keyWord = key;
	}

	public String getKey() {
		return keyWord;
	}

	public void setKey(String key) {
		this.keyWord = key;
	}

	@Override
	public String toString() {
		return "KeyWord [key=" + keyWord + "]";
	}

}
