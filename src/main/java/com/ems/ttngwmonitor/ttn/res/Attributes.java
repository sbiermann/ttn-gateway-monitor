package com.ems.ttngwmonitor.ttn.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "antenna_model", "brand", "frequency_plan", "model", "placement" } )
public class Attributes
{

	@JsonProperty( "antenna_model" )
	private String antennaModel;

	@JsonProperty( "brand" )
	private String brand;

	@JsonProperty( "frequency_plan" )
	private String frequencyPlan;

	@JsonProperty( "model" )
	private String model;

	@JsonProperty( "placement" )
	private String placement;


	@JsonProperty( "antenna_model" )
	public String getAntennaModel()
	{
		return antennaModel;
	}


	@JsonProperty( "antenna_model" )
	public void setAntennaModel( String antennaModel )
	{
		this.antennaModel = antennaModel;
	}


	@JsonProperty( "brand" )
	public String getBrand()
	{
		return brand;
	}


	@JsonProperty( "brand" )
	public void setBrand( String brand )
	{
		this.brand = brand;
	}


	@JsonProperty( "frequency_plan" )
	public String getFrequencyPlan()
	{
		return frequencyPlan;
	}


	@JsonProperty( "frequency_plan" )
	public void setFrequencyPlan( String frequencyPlan )
	{
		this.frequencyPlan = frequencyPlan;
	}


	@JsonProperty( "model" )
	public String getModel()
	{
		return model;
	}


	@JsonProperty( "model" )
	public void setModel( String model )
	{
		this.model = model;
	}


	@JsonProperty( "placement" )
	public String getPlacement()
	{
		return placement;
	}


	@JsonProperty( "placement" )
	public void setPlacement( String placement )
	{
		this.placement = placement;
	}

}
