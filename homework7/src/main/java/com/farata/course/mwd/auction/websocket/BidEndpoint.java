package com.farata.course.mwd.auction.websocket;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.entity.BidConfirmation;
import com.farata.course.mwd.auction.entity.User;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.Json;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/bid")
@MessageDriven(name = "BidconfirmationsQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/bidconfirmations"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class BidEndpoint implements MessageListener {

    private static final Logger LOG = Logger.getLogger(BidEndpoint.class.getName());

    private final transient Map<User, Session> userToSessionMap = new HashMap<>();
    private final transient Map<Session, User> sessionToUserMap = new HashMap<>();

    @Inject
    private DataEngine dataEngine;

    @OnMessage
    public void userSendsMessageToServer(String textMessage, Session mySession) throws IOException {
        if (sessionToUserMap.containsKey(mySession)) {
            LOG.info("Session already registered to push notifications");
            return;
        }

        User user = dataEngine.createUser();
        userToSessionMap.put(user, mySession);
        sessionToUserMap.put(mySession, user);
        LOG.info("New session registered for push notifications: " + user.getId() + ", " + user.getName() + ", " + textMessage);
        mySession.getBasicRemote().sendText(Json.createObjectBuilder().add("userId", user.getId()).build().toString());
    }

    // server sends message to user
    @Override
    public void onMessage(Message message) {
        try {
            BidConfirmation bidConfirmation = message.getBody(BidConfirmation.class);
            boolean sendConfirmationToEveryone = bidConfirmation.user == null;
            if (sendConfirmationToEveryone) {
                for (Session session : sessionToUserMap.keySet()) {
                    LOG.info("Pushing bidConfirmation to: " + sessionToUserMap.get(session).getName());
                    session.getBasicRemote().sendText(bidConfirmation.content);
                }
            } else {
                Session session = userToSessionMap.get(bidConfirmation.user);
                session.getBasicRemote().sendText(bidConfirmation.content);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to push bidConfirmation", e);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        User removedUser = sessionToUserMap.remove(session);
        userToSessionMap.remove(removedUser);
        LOG.info("Session closed: " + (removedUser != null ? removedUser.getName() : "NULL") + ", " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        LOG.log(Level.SEVERE, "Error", t);
    }
}
