package com.ems.ttngwmonitor.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;


@Entity
@NamedQueries({
		@NamedQuery(name = "gateway.findById", query = "SELECT c FROM Gateway c WHERE c.gwid = :id"),
		@NamedQuery(name = "gateway.findAll", query = "SELECT c FROM Gateway c"),
		@NamedQuery(name = "gateway.findByTTNId", query = "SELECT c FROM Gateway c WHERE c.ttngatewayid = :id"),
		@NamedQuery(name = "gateway.findAllByUserId", query = "SELECT c FROM Gateway c WHERE c.userid = :userid order by c.gwid ASC")
})
public class Gateway
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gwid;

	private String ttngatewayid;

	private String userid;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastseen;


	public String getTtngatewayid()
	{
		return ttngatewayid;
	}


	public void setTtngatewayid( String ttngatewayid )
	{
		this.ttngatewayid = ttngatewayid;
	}


	public String getUserid()
	{
		return userid;
	}


	public void setUserid( String userid )
	{
		this.userid = userid;
	}


	public Date getLastseen()
	{
		return lastseen;
	}


	public void setLastseen( Date lastseen )
	{
		this.lastseen = lastseen;
	}


	public Long getGwid()
	{
		return gwid;
	}
}
