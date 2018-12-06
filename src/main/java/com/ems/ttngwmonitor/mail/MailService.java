package com.ems.ttngwmonitor.mail;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ems.ttngwmonitor.entity.Gateway;
import com.ems.ttngwmonitor.service.PersistenceService;


@Singleton
public class MailService implements Serializable 
{
	@Resource( lookup = "java:jboss/mail/ttngwSMTP" )
	private Session mailSession;

	private HashMap<String, Date> map = new HashMap();


	@EJB
	private PersistenceService persistenceService;


	public void informUserForGateway( Gateway gateway )
	{
		if(map.containsKey( gateway.getTtngatewayid() ))
		{
			Date lastMessageDate = map.get( gateway.getTtngatewayid() );
			Calendar now = Calendar.getInstance();
			now.add( Calendar.DATE, -1 );
			if(lastMessageDate.after( now.getTime() ))
				return;
		}
		map.put( gateway.getTtngatewayid(), new Date() );

		String email = persistenceService.readNotificationEmailForUser( gateway.getUserid() );

		String msgText = String.format(
				"Hallo %1$s,\nGateway: %2$s  ist zu lange offline, letzte online Meldung: %3$td.%3$tm.%3$ty %3$tT\n\nViele Grüße\nIhr GatewayMonitor",
				email, gateway.getTtngatewayid(), gateway.getLastseen() );
		MailMessageBuilder.MailMessage mailMessage = new MailMessageBuilder().from( "ttngateway@ttn-freiburg.de" )
				.addTo( email )
				.subject( "TTN Gateway: " + gateway.getTtngatewayid() + " offline!" )
				.body( msgText )
				.contentType( "plain/text" )
				.build();
		sendMail( mailMessage );
	}


	private void sendMail( final MailMessageBuilder.MailMessage mailMessage )
	{

		try
		{
			// Translate
			final MimeMessage mime = new MimeMessage( mailSession );
			final Address from = new InternetAddress( mailMessage.from );
			final int numToAddresses = mailMessage.to.length;
			final Address[] to = new InternetAddress[numToAddresses];
			for( int i = 0; i < numToAddresses; i++ )
			{
				to[i] = new InternetAddress( mailMessage.to[i] );
			}
			mime.setFrom( from );
			mime.setRecipients( Message.RecipientType.TO, to );
			mime.setSubject( mailMessage.subject );
			mime.setContent( mailMessage.body, mailMessage.contentType );
			Transport.send( mime );
		} // Puke on error
		catch( final javax.mail.MessagingException e )
		{
			throw new RuntimeException( "Error in sending " + mailMessage, e );
		}
	}
}
