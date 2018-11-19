package com.ems.ttngwmonitor.service;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
@TransactionAttribute( TransactionAttributeType.NOT_SUPPORTED )
@Lock( LockType.READ )
public class TimerServiceBean
{
	private final static Logger logger = LoggerFactory.getLogger( TimerServiceBean.class );

	@Inject
	private MonitorService monitorService;


	@Schedule( hour = "*", minute = "*/5", persistent = false )
	public void checkOnlineGateways()
	{
		logger.info( "checking online gateways" );
		monitorService.checkForOfflineGateways();

	}

}
