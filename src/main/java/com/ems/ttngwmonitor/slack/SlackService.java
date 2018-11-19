package com.ems.ttngwmonitor.slack;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


	public void informChannelForGateway( String gatewayid, Date date )
	{
		try
		{
			if(map.containsKey( gatewayid ))
			{
				Date lastMessageDate = map.get( gatewayid );
				Calendar now = Calendar.getInstance();
				now.add( Calendar.DATE, -1 );
				if(lastMessageDate.after( now.getTime() ))
					return;
			}
			else
				map.put( gatewayid, new Date() );
			if( !session.isConnected() )
				session.connect();
			SlackChannel channel = session.findChannelByName( "gateways" ); //make sure bot is a member of the channel.
			session.sendMessage( channel, "Gateway: " + gatewayid + " ist zu lange offline, letzte Meldung: " + date );
		}
		catch( IOException e )
		{
			logger.error( "error while connecting to slack" );
		}

	}
}
