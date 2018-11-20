package com.ems.ttngwmonitor.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		}
	}
}
