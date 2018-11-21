package com.ems.ttngwmonitor.presentation;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.brickred.socialauth.cdi.SocialAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Named
@SessionScoped
public class SocialLoginBean implements Serializable
{

	private static final Logger logger = LoggerFactory.getLogger( SocialLoginBean.class );

	@Inject
	private SocialAuth socialauth;


	@PostConstruct
	public void postConstruct()
	{
		socialauth.setViewUrl( "/register.xhtml" );
	}


	public void updateId( ActionEvent actionEvent )
	{
		String id = actionEvent.getComponent().getId();
		if( id.contains( "facebook" ) )
		{
			socialauth.setId( "facebook" );
		}
		if( id.contains( "github" ) )
			socialauth.setId( "github" );
		logger.info( "social login provider is choosen as:" + socialauth.getId() );
	}
}
