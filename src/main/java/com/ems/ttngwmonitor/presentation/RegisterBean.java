package com.ems.ttngwmonitor.presentation;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.brickred.socialauth.cdi.SocialAuth;

import com.ems.ttngwmonitor.entity.Gateway;
import com.ems.ttngwmonitor.service.PersistenceService;


@Named
@ViewScoped
public class RegisterBean implements Serializable
{

	private List<Gateway> gatewayList;

	private String toDelete;

	private String email;

	private String gateway;

	@Inject
	private FacesContext fc;

	@Inject
	private SocialAuth socialAuth;

	@EJB
	private PersistenceService persistenceService;


	public List<Gateway> getGatewayList()
	{
		if(gatewayList == null)
		{
			gatewayList = persistenceService.readAllGatewaysForUser(socialAuth.getProfile().getValidatedId());
		}
		return gatewayList;
	}


	public void setGatewayList( List<Gateway> gatewayList )
	{
		this.gatewayList = gatewayList;
	}

	public void settingGatewayToDelete(String gateway) {
		toDelete = gateway;
	}


	public String deleteGateway() {
		if (toDelete != null) {
			if (persistenceService.removeGateway(toDelete)) {
				fc.addMessage( null,
						new FacesMessage( FacesMessage.SEVERITY_INFO, "Gateway ID: "+toDelete+" wurde gelöscht!",
								null ) );
				gatewayList = null;
				return "register.xhtml";
			}
		}
		return null;
	}


	public String getEmail()
	{
		if(email == null)
		{
			email = persistenceService.readNotificationEmailForUser(socialAuth.getProfile().getValidatedId());
		}

		return email;
	}


	public void setEmail( String email )
	{
		this.email = email;
	}


	public String addGateway()
	{
		persistenceService.addGatewayForUser(socialAuth.getProfile().getValidatedId(), gateway);
		fc.addMessage( null,
			new FacesMessage( FacesMessage.SEVERITY_INFO, "Gateway ID: "+gateway+" wurde gespeichert!",
					null ) );
		gateway = "";
		return "register.xhtml";
	}


	public String getGateway()
	{
		return gateway;
	}


	public void setGateway( String gateway )
	{
		this.gateway = gateway;
	}


	public String saveEmail()
	{
		persistenceService.saveNotificationEmail(socialAuth.getProfile().getValidatedId(), email);
		fc.addMessage( null,
				new FacesMessage( FacesMessage.SEVERITY_INFO, "Notification E-Mail wurde gespeichert!",
						null ) );
		return "register.xhtml";
	}
}
