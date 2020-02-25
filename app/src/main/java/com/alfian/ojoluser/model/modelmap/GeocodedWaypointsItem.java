package com.alfian.ojoluser.model.modelmap;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GeocodedWaypointsItem{

	@SerializedName("types")
	private List<String> types;

	@SerializedName("geocoder_status")
	private String geocoderStatus;

	@SerializedName("place_id")
	private String placeId;

	@SerializedName("partial_match")
	private boolean partialMatch;

	public void setTypes(List<String> types){
		this.types = types;
	}

	public List<String> getTypes(){
		return types;
	}

	public void setGeocoderStatus(String geocoderStatus){
		this.geocoderStatus = geocoderStatus;
	}

	public String getGeocoderStatus(){
		return geocoderStatus;
	}

	public void setPlaceId(String placeId){
		this.placeId = placeId;
	}

	public String getPlaceId(){
		return placeId;
	}

	public void setPartialMatch(boolean partialMatch){
		this.partialMatch = partialMatch;
	}

	public boolean isPartialMatch(){
		return partialMatch;
	}
}