package com.ems.ttngwmonitor.slack;

import com.ems.ttngwmonitor.ttn.res.Gateway;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@Singleton
public class SlackService {
    private final static Logger logger = LoggerFactory.getLogger(SlackService.class);

    private SlackSession session;

    private HashMap<String, Date> map = new HashMap();

    private long lastAccess = 0;

    @Inject
    @ConfigProperty(name = "slack.token")
    private String slackToken;

    @PostConstruct
    public void postConstruct() {
        session = SlackSessionFactory
                .getSlackSessionBuilder(slackToken)
                .build();
    }


    public void informChannelForGateway(Gateway gateway, Date date) {
        try {
            if (map.containsKey(gateway.getId())) {
                Date lastMessageDate = map.get(gateway.getId());
                Calendar now = Calendar.getInstance();
                now.add(Calendar.DATE, -1);
                if (lastMessageDate.after(now.getTime()))
                    return;
            }
            map.put(gateway.getId(), new Date());
            lastAccess = System.currentTimeMillis();
            if (!session.isConnected())
                session.connect();
            SlackChannel channel = session.findChannelByName("gatewaymonitoring"); //make sure bot is a member of the channel.
            String message = String.format("Gateway: %1$s (%2$s) ist zu lange offline, letzte online Meldung: %3$td.%3$tm.%3$ty %3$tT", gateway.getId(), gateway.getOwner(), date);
            session.sendMessage(channel, message);
        } catch (IOException e) {
            logger.error("error while connecting to slack");
        }

    }

    public void informChannelForReturningGateway(Gateway gateway, Date date) {
        lastAccess = System.currentTimeMillis();
        try {
            if (!session.isConnected())
                session.connect();
            SlackChannel channel = session.findChannelByName("gatewaymonitoring"); //make sure bot is a member of the channel.
            String message = String.format("Gateway: %1$s (%2$s) ist wieder seid %3$td.%3$tm.%3$ty %3$tT online", gateway.getId(), gateway.getOwner(), date);
            session.sendMessage(channel, message);
        } catch (IOException e) {
            logger.error("error while connecting to slack");
        }
    }

    @Schedule(hour = "*", minute = "*/6", persistent = false)
    public void checkForSessionOpen() {
        if (lastAccess <= 0)
            return;
        try {
            if (System.currentTimeMillis() - lastAccess > TimeUnit.MINUTES.toMillis(5))
                session.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
