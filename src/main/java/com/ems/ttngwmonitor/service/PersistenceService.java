package com.ems.ttngwmonitor.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ems.ttngwmonitor.entity.Gateway;
import com.ems.ttngwmonitor.entity.Notification;


@Stateless
public class PersistenceService
{
	private static final Logger logger = LoggerFactory.getLogger( PersistenceService.class );

	@PersistenceContext
	private EntityManager entityManager;


	public List<Gateway> readAllGatewaysForUser( String userid )
	{
		try {
			TypedQuery<Gateway> query = entityManager.createNamedQuery("gateway.findAllByUserId", Gateway.class);
			query.setParameter( "userid", userid );
			return query.getResultList();
		} catch (Exception e) {
			logger.warn("error while retrieving gateways for userid:"+userid, e);
		}
		return new ArrayList<>();
	}


	public void addGatewayForUser( String userid, String gateway )
	{
		Gateway gw = new Gateway();
		gw.setUserid( userid );
		gw.setTtngatewayid( gateway );
		entityManager.persist( gw );
	}


	public void saveNotificationEmail( String userid, String email )
	{
		try {
			TypedQuery<Notification> query = entityManager.createNamedQuery("notification.findAllByUserId", Notification.class);
			query.setParameter( "userid", userid );
			List<Notification> resultList = query.getResultList();
			Notification notification = null;
			if(resultList == null || resultList.isEmpty())
				notification = new Notification();
			else
				notification = resultList.get( 0 );
			notification.setUserid( userid );
			notification.setEmail( email );
			entityManager.merge( notification );
		} catch (Exception e) {
			logger.warn("error while retrieving gateways for userid:"+userid, e);
		}
	}


	public boolean removeGateway( String gwid )
	{
		TypedQuery<Gateway> query = entityManager.createNamedQuery( "gateway.findByTTNId", Gateway.class );
		query.setParameter( "id", gwid );
		Gateway singleResult = query.getSingleResult();
		entityManager.remove( singleResult );
		return true;
	}


	public String readNotificationEmailForUser( String userid )
	{
		try {
			TypedQuery<Notification> query = entityManager.createNamedQuery("notification.findAllByUserId", Notification.class);
			query.setParameter( "userid", userid );
			List<Notification> resultList = query.getResultList();
			if(resultList ==  null || resultList.isEmpty())
				return null;
			else
				return resultList.get( 0 ).getEmail();
		} catch (Exception e) {
			logger.warn("error while retrieving notification for userid:"+userid, e);
		}
		return null;
	}


	public List<Gateway> readAllGateways()
	{
		try {
			TypedQuery<Gateway> query = entityManager.createNamedQuery("gateway.findAll", Gateway.class);
			return query.getResultList();
		} catch (Exception e) {
			logger.warn("error while retrieving gateways ", e);
		}
		return new ArrayList<>();
	}


	public void mergeGateway( Gateway gateway )
	{
		entityManager.merge( gateway );
	}
}
