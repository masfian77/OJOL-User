package com.alfian.ojoluser.model.modelmap;

import com.google.gson.annotations.SerializedName;


public class Polyline{

	@SerializedName("points")
	private String points;

	public void setPoints(String points){
		this.points = points;
	}

	public String getPoints(){
		return points;
	}
}