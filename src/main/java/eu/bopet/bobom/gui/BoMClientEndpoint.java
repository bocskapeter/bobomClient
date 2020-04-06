package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMMessage;
import javafx.application.Platform;

import javax.websocket.*;
import java.io.IOException;
import java.util.logging.Logger;

@ClientEndpoint(
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class)
public class BoMClientEndpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private GUIContext context;
    private Session session;

    public BoMClientEndpoint(GUIContext context) {
        this.context = context;
    }


    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        logger.warning(session.toString());
        this.session = session;
    }

    @OnMessage
    public void onMessage(BoMMessage message) {
        Platform.runLater(() -> processMessage(message));
    }

    public void sendMessage(BoMMessage message) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            logger.warning(message.toString());
            logger.warning(e.getLocalizedMessage());
        }
    }

    private void processMessage(BoMMessage message) {
        switch (message.getActivity()) {
            case LOGIN: {
                if (message.getUser() != null) {
                    context.userProperty().setValue(message.getUser());
                    break;
                }
            }
            case READ_ALL: {
                context.putIntoEntityList(message.getTheClass(), message.getList());
                break;
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.warning(String.format("Session %s error: %s", session.getId(), error.getMessage()));
    }
}