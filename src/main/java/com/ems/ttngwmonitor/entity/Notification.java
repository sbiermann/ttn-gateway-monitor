package com.ems.ttngwmonitor.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Email;


@Entity
@NamedQueries({
		@NamedQuery(name = "notification.findById", query = "SELECT c FROM Notification c WHERE c.notificationid = :id"),
		@NamedQuery(name = "notification.findAllByUserId", query = "SELECT c FROM Notification c WHERE c.userid = :userid order by c.notificationid ASC")
})
public class Notification
{

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long notificationid;

	@Email
	private String email;

	private String userid;

	public String getEmail()
	{
		return email;
	}


	public void setEmail( String email )
	{
		this.email = email;
	}


	public Long getNotificationid()
	{
		return notificationid;
	}


	public String getUserid()
	{
		return userid;
	}


	public void setUserid( String userid )
	{
		this.userid = userid;
	}
}
