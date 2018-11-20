package com.ems.ttngwmonitor.ttn.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "latitude", "longitude", "altitude" } )
public class Location
{

	@JsonProperty( "latitude" )
	private Double latitude;

	@JsonProperty( "longitude" )
	private Double longitude;

	@JsonProperty( "altitude" )
	private Integer altitude;


	@JsonProperty( "latitude" )
	public Double getLatitude()
	{
		return latitude;
	}


	@JsonProperty( "latitude" )
	public void setLatitude( Double latitude )
	{
		this.latitude = latitude;
	}


	@JsonProperty( "longitude" )
	public Double getLongitude()
	{
		return longitude;
	}


	@JsonProperty( "longitude" )
	public void setLongitude( Double longitude )
	{
		this.longitude = longitude;
	}


	@JsonProperty( "altitude" )
	public Integer getAltitude()
	{
		return altitude;
	}


	@JsonProperty( "altitude" )
	public void setAltitude( Integer altitude )
	{
		this.altitude = altitude;
	}
}