package com.ems.ttngwmonitor.slack;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.glassfish.grizzly.http.server.util.SimpleDateFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ems.ttngwmonitor.ttn.res.Gateway;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;


@ApplicationScoped
public class SlackService
{
	private final static Logger logger = LoggerFactory.getLogger( SlackService.class );

	private SlackSession session;

	private HashMap<String, Date> map = new HashMap();

	@Inject
	@ConfigProperty(name = "slack.token")
	private String slackToken;

	@PostConstruct
	public void postConstruct()
	{
		session = SlackSessionFactory.createWebSocketSlackSession( slackToken );
		try
		{
			session.connect();
		}
		catch( IOException e )
		{
			logger.error( "error while connecting to slack" );
		}
	}


	public void informChannelForGateway( Gateway gateway, Date date )
	{
		try
		{
			if(map.containsKey( gateway.getId() ))
			{
				Date lastMessageDate = map.get( gateway.getId() );
				Calendar now = Calendar.getInstance();
				now.add( Calendar.DATE, -1 );
				if(lastMessageDate.after( now.getTime() ))
					return;
			}
			map.put( gateway.getId(), new Date() );
			if( !session.isConnected() )
				session.connect();
			SlackChannel channel = session.findChannelByName( "gatewaymonitoring" ); //make sure bot is a member of the channel.
			String message = String.format( "Gateway: %1$s (%2$s) ist zu lange offline, letzte online Meldung: %3$td.%3$tm.%3$ty %3$tT", gateway.getId(), gateway.getOwner(), date );
			session.sendMessage( channel, message);
		}
		catch( IOException e )
		{
			logger.error( "error while connecting to slack" );
		}

	}

	public void informChannelForReturningGateway(Gateway gateway, Date date) {
		try
		{
			if( !session.isConnected() )
				session.connect();
			SlackChannel channel = session.findChannelByName( "gatewaymonitoring" ); //make sure bot is a member of the channel.
			String message = String.format( "Gateway: %1$s (%2$s) ist wieder seid %3$td.%3$tm.%3$ty %3$tT online", gateway.getId(), gateway.getOwner(), date );
			session.sendMessage( channel, message);
		}
		catch( IOException e )
		{
			logger.error( "error while connecting to slack" );
		}
	}
}
