package com.ycg.ksh.test;

import java.io.Serializable;

public class TestObject implements Serializable{

	private static final long serialVersionUID = 4717389052800057246L;
	private String name;

	public TestObject(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
