package com.ems.ttngwmonitor.ttn.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "id", "description", "owner", "owners", "location", "country_code", "attributes", "last_seen" } )
@JsonIgnoreProperties( ignoreUnknown = true )
public class Gateway
{

	@JsonProperty( "id" )
	private String id;

	@JsonProperty( "description" )
	private String description;

	@JsonProperty( "owner" )
	private String owner;

	@JsonProperty( "owners" )
	private List<String> owners = null;

	@JsonProperty( "location" )
	private Location location;

	@JsonProperty( "country_code" )
	private String countryCode;

	@JsonProperty( "attributes" )
	private Attributes attributes;

	@JsonProperty( "last_seen" )
	private String lastSeen;


	@JsonProperty( "id" )
	public String getId()
	{
		return id;
	}


	@JsonProperty( "id" )
	public void setId( String id )
	{
		this.id = id;
	}


	@JsonProperty( "description" )
	public String getDescription()
	{
		return description;
	}


	@JsonProperty( "description" )
	public void setDescription( String description )
	{
		this.description = description;
	}


	@JsonProperty( "owner" )
	public String getOwner()
	{
		return owner;
	}


	@JsonProperty( "owner" )
	public void setOwner( String owner )
	{
		this.owner = owner;
	}


	@JsonProperty( "owners" )
	public List<String> getOwners()
	{
		return owners;
	}


	@JsonProperty( "owners" )
	public void setOwners( List<String> owners )
	{
		this.owners = owners;
	}


	@JsonProperty( "location" )
	public Location getLocation()
	{
		return location;
	}


	@JsonProperty( "location" )
	public void setLocation( Location location )
	{
		this.location = location;
	}


	@JsonProperty( "country_code" )
	public String getCountryCode()
	{
		return countryCode;
	}


	@JsonProperty( "country_code" )
	public void setCountryCode( String countryCode )
	{
		this.countryCode = countryCode;
	}


	@JsonProperty( "attributes" )
	public Attributes getAttributes()
	{
		return attributes;
	}


	@JsonProperty( "attributes" )
	public void setAttributes( Attributes attributes )
	{
		this.attributes = attributes;
	}


	@JsonProperty( "last_seen" )
	public String getLastSeen()
	{
		return lastSeen;
	}


	@JsonProperty( "last_seen" )
	public void setLastSeen( String lastSeen )
	{
		this.lastSeen = lastSeen;
	}

}