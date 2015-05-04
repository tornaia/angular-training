package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.entity.Bid;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(name = "IncomingbidsQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/incomingbids"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class IncomingbidsAsyncMessageListener implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(IncomingbidsAsyncMessageListener.class.toString());

    @Inject
    private DataEngine dataEngine;

    @Override
    public void onMessage(Message message) {
        try {
            Bid receivedBid = message.getBody(Bid.class);
            dataEngine.placeBid(receivedBid);
        } catch (JMSException e) {
            LOGGER.log(Level.WARNING, "Unknown message", e);
        }
    }
}
