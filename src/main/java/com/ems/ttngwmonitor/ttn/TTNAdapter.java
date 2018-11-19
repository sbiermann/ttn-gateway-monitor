package com.ems.ttngwmonitor.ttn;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class TTNAdapter
{
	private static final Logger logger = LoggerFactory.getLogger( TTNAdapter.class );

	//https://www.thethingsnetwork.org/gateway-data/location?latitude=47.99606771946258&longitude=7.849594652652738&distance=10000
	private static final String TTN_GW_DATA_URL = "https://www.thethingsnetwork.org/gateway-data";
	//http://noc.thethingsnetwork.org:8085/api/v2/gateways/
	private static final String TTN_GW_URL = "http://noc.thethingsnetwork.org:8085/api/v2/gateways/";

	private ObjectMapper mapper;

	@PostConstruct
	public void postConstruct(){
		mapper = new ObjectMapper();
	}

	public List<String> listOfGateways()
	{
		ResteasyClient client = null;
		Response response = null;
		List<String> gatewayIds = new ArrayList();
		try {
			client = new ResteasyClientBuilder().build();
			WebTarget couponTarget = client.target( TTN_GW_DATA_URL );
			response = couponTarget.path( "location")
					.queryParam( "latitude", "47.99606771946258" )
					.queryParam( "longitude", "7.849594652652738" )
					.queryParam( "distance", "10000" )
					.request()
					.get();
			if( response.getStatusInfo().getStatusCode() == 404 ) {
				return gatewayIds;
			}
			JsonNode rootNode = mapper.readTree( response.readEntity( String.class ) );
			Iterator<JsonNode> iterator = rootNode.iterator();
			while(iterator.hasNext())
			{
				JsonNode jsonNode = iterator.next();
				gatewayIds.add( jsonNode.get( "id" ).asText() );
			}
		}
		catch( Exception e )
		{
			logger.error("error while getting gateways",e);
		}
		finally {
			if( client!=null )
				client.close();
			if( response != null )
				response.close();
		}
		return gatewayIds;
	}


	public Optional<Date> lastSeenOfGateway( String id )
	{
		ResteasyClient client = null;
		Response response = null;
		try {
			client = new ResteasyClientBuilder().build();
			WebTarget couponTarget = client.target( TTN_GW_URL );
			response = couponTarget.path( id )
					.request()
					.get();
			if( response.getStatusInfo().getStatusCode() == 404 ) {
				return Optional.empty();
			}
			JsonNode rootNode = mapper.readTree( response.readEntity( String.class ) );
			Instant timestamp = Instant.parse( rootNode.get( "timestamp" ).asText() );
			return Optional.of( Date.from( timestamp ) );
		}
		catch( Exception e )
		{
			logger.error("error while getting gateway status for "+id,e);
		}
		finally {
			if( client!=null )
				client.close();
			if( response != null )
				response.close();
		}
		return Optional.empty();
	}
}
