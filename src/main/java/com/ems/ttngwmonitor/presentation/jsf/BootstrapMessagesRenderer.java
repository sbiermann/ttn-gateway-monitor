package com.ems.ttngwmonitor.presentation.jsf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.inject.Inject;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.MessagesRenderer;


/**
 * Component for twitter bootstrap alerts.
 * Overrides default JSF Message renderer with Bootstrap alert design.
 *
 * @author vlcekmi3
 */
@FacesRenderer(componentFamily="javax.faces.Messages", rendererType="javax.faces.Messages")
public class BootstrapMessagesRenderer extends MessagesRenderer {

    private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.MESSAGESMESSAGES);

    @Inject
    private FacesContext ctx;

    @Override
    public void encodeBegin( FacesContext context, UIComponent component) throws IOException
	{
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd( FacesContext context, UIComponent component) throws IOException
	{
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) return;

        boolean mustRender = shouldWriteIdAttribute(component);

        UIMessages messages = (UIMessages) component;
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String clientId = ((UIMessages) component).getFor();
        if (clientId == null)
            if (messages.isGlobalOnly())
                clientId = "";

        Iterator messageIter = getMessageIter(context, clientId, component);

        assert(messageIter != null);

        if (!messageIter.hasNext()) {
            if (mustRender) {
                if ("javax_faces_developmentstage_messages".equals(component.getId())) {
                    return;
                }
                writer.startElement("div", component);
                writeIdAttributeIfNecessary(context, writer, component);
                writer.endElement("div");
            }
            return;
        }

        writeIdAttributeIfNecessary(context, writer, component);

        // style is rendered as a passthru attribute
        RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES);

        Map<Severity, List<FacesMessage>> msgs = new HashMap<Severity, List<FacesMessage>>();
        msgs.put( FacesMessage.SEVERITY_INFO, new ArrayList<FacesMessage>()); // Bootstrap info
        msgs.put( FacesMessage.SEVERITY_WARN, new ArrayList<FacesMessage>()); // Bootstrap warning
        msgs.put( FacesMessage.SEVERITY_ERROR, new ArrayList<FacesMessage>()); // Bootstrap error
        msgs.put( FacesMessage.SEVERITY_FATAL, new ArrayList<FacesMessage>()); // Bootstrap error

        while (messageIter.hasNext()) {
            FacesMessage curMessage = (FacesMessage) messageIter.next();

            if (curMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            msgs.get(curMessage.getSeverity()).add(curMessage);
        }

        List<FacesMessage> severityMessages = msgs.get( FacesMessage.SEVERITY_FATAL);
        if (severityMessages.size() > 0){
            encodeSeverityMessages(context, component, messages, FacesMessage.SEVERITY_FATAL, severityMessages);
        }

        severityMessages = msgs.get( FacesMessage.SEVERITY_ERROR);
        if (severityMessages.size() > 0){
            encodeSeverityMessages(context, component, messages, FacesMessage.SEVERITY_ERROR, severityMessages);
        }

        severityMessages = msgs.get( FacesMessage.SEVERITY_WARN);
        if (severityMessages.size() > 0){
            encodeSeverityMessages(context, component, messages, FacesMessage.SEVERITY_WARN, severityMessages);
        }

        severityMessages = msgs.get( FacesMessage.SEVERITY_INFO);
        if (severityMessages.size() > 0){
            encodeSeverityMessages(context, component, messages, FacesMessage.SEVERITY_INFO, severityMessages);
        }
    }

    private void encodeSeverityMessages( FacesContext facesContext, UIComponent component, UIMessages uiMessages, Severity severity, List<FacesMessage> messages) throws
			IOException
	{
        ResponseWriter writer = facesContext.getResponseWriter();

        String alertSeverityClass = "";

        String alertText = "";

        if( FacesMessage.SEVERITY_INFO.equals(severity)) {
            alertSeverityClass = "alert-info";
            alertText = "Information";
        } else if( FacesMessage.SEVERITY_WARN.equals(severity)) {
            alertSeverityClass = "alert-warning"; // Default alert is a warning
            alertText = "Warnung";
        } else if( FacesMessage.SEVERITY_ERROR.equals(severity)) {
            alertSeverityClass = "alert-danger";
            alertText = "Fehler";
        } else if( FacesMessage.SEVERITY_FATAL.equals(severity)) {
            alertSeverityClass = "alert-danger";
            alertText = "Fatal";
        }

        //<div class="alert alert-warning">
        //<a href="#" class="close" data-dismiss="alert">&times;</a>
        //<strong>Warning!</strong> There was a problem with your network connection.
        //</div>
        writer.startElement("div", null);
        writer.writeAttribute("class", "alert " + alertSeverityClass, "alert " + alertSeverityClass);
        writer.writeText("\n",null);
        writer.startElement("a", component);
        writer.writeAttribute("href", "#", "href");
        writer.writeAttribute("class", "close", "class");
        writer.writeAttribute("data-dismiss", "alert", "data-dismiss");
        writer.write("&times;");
        writer.endElement("a");
        writer.writeText("\n",null);


        //writer.startElement("ul", null);
        //writer.writeAttribute("class","fa-ul","class");

        for ( FacesMessage msg : messages){
            String summary = msg.getSummary() != null ? msg.getSummary() : "";
            String detail = msg.getDetail() != null ? msg.getDetail() : summary;

            // writer.startElement("li", component);
            writer.startElement("strong", null);
            writer.writeText(alertText, null);
            writer.endElement("strong");
            if (uiMessages.isShowSummary()) {
                writer.writeText(" "+summary, component, null);
            }

            if (uiMessages.isShowDetail()) {
                writer.writeText(" " + detail, null);
            }
            writer.startElement("br",null);
            writer.endElement("br");

            //writer.endElement("li");
            msg.rendered();
        }
        // writer.endElement("ul");
        writer.endElement("div");
    }
}