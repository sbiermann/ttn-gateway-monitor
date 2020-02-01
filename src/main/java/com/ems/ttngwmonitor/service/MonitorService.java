package com.ems.ttngwmonitor.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ems.ttngwmonitor.mail.MailService;
import com.ems.ttngwmonitor.slack.SlackService;
import com.ems.ttngwmonitor.ttn.TTNAdapter;
import com.ems.ttngwmonitor.ttn.res.Gateway;


@ApplicationScoped
public class MonitorService
{
	private final static Logger logger = LoggerFactory.getLogger( MonitorService.class );

	@Inject
	private TTNAdapter ttnAdapter;

	@Inject
	private SlackService slackService;

	@EJB
	private MailService mailService;

	@EJB
	private PersistenceService persistenceService;

	private List<String> reportedAsNotSeen = new ArrayList();

	public void checkForOfflineGateways()
	{
		List<Gateway> idList = ttnAdapter.listOfGateways();
		Map<Gateway, Optional<Date>> lastSeenMap = idList.stream()
				.collect( Collectors.toMap( Function.identity(), gateway -> ttnAdapter.lastSeenOfGateway( gateway.getId() ) ) );
		lastSeenMap.keySet().stream().forEach( id -> lastSeenMap.get( id ).ifPresent( date -> checkDate(date, id) ) );
	}


	private void checkDate( Date date, Gateway gateway )
	{
		Calendar now = Calendar.getInstance();
		now.add( Calendar.MINUTE, -30 );
		Date shortTime = now.getTime();
		now.add( Calendar.DATE, -7 );
		Date longTime = now.getTime();
		if(date.before( shortTime ) && date.after( longTime ))
		{
			 logger.info("last seen of "+gateway.getId()+" is to old "+date);
			 slackService.informChannelForGateway(gateway, date);
			 reportedAsNotSeen.add(gateway.getId());
		}
		else
		{
			if(reportedAsNotSeen.contains( gateway.getId() ) && date.after( shortTime ))
			{
				logger.info("it seems that "+gateway.getId()+" is back on online "+date);
				reportedAsNotSeen.remove( gateway.getId() );
				slackService.informChannelForReturningGateway( gateway, date );
			}
		}
	}


	public void checkForEachDBGateways()
	{
		List<com.ems.ttngwmonitor.entity.Gateway> gatewayList = persistenceService.readAllGateways();
		Map<com.ems.ttngwmonitor.entity.Gateway, Optional<Date>> lastSeenMap = gatewayList.stream()
				.collect( Collectors.toMap( Function.identity(), gateway -> ttnAdapter.lastSeenOfGateway( gateway.getTtngatewayid() ) ) );
		lastSeenMap.keySet().stream().forEach( id -> lastSeenMap.get( id ).ifPresent( date -> checkDateAndEMail(date, id) ) );
	}


	private void checkDateAndEMail( Date lastSeen, com.ems.ttngwmonitor.entity.Gateway gateway )
	{
		Calendar now = Calendar.getInstance();
		now.add( Calendar.MINUTE, -30 );
		Date shortTime = now.getTime();
		now.add( Calendar.DATE, -7 );
		Date longTime = now.getTime();
		gateway.setLastseen( lastSeen );
		persistenceService.mergeGateway(gateway);
		if(lastSeen.before( shortTime ) && lastSeen.after( longTime ))
		{
			logger.info("last seen of "+gateway.getTtngatewayid()+" is to old "+lastSeen);
			mailService.informUserForGateway(gateway);
		}
	}
}
