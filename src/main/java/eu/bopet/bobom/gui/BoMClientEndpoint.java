package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMMessage;
import eu.bopet.bobom.core.entities.Users;
import javafx.application.Platform;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.logging.Logger;

@ClientEndpoint
public class BoMClientEndpoint extends Endpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private GUIContext context;
    private Session session;

    public BoMClientEndpoint(GUIContext context) {
        this.context = context;
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        logger.warning(session.toString());
        session.addMessageHandler((MessageHandler.Whole<String>) s -> {decodeMessage(s);});
        this.session = session;
    }

    private void decodeMessage(String s) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] data = decoder.decode(s);
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            BoMMessage message = (BoMMessage) is.readObject();
            processMessage(message);
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("Incoming String: " + s);
            logger.warning(e.getLocalizedMessage());
        }
    }

    public void sendMessage(BoMMessage message){
        encodeMessage(message);
    }

    private void encodeMessage(BoMMessage message) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(message);
            Base64.Encoder encoder = Base64.getEncoder();
            String result = encoder.encodeToString(byteArrayOutputStream.toByteArray());
            System.out.println(session);
            session.getBasicRemote().sendText(result);
        } catch (IOException e) {
            logger.warning(message.toString());
            logger.warning(e.getLocalizedMessage());
        }
    }

    private void processMessage(BoMMessage message) {
        switch (message.getActivity()) {
            case LOGIN: {
                if (context.getUser() == null) {
                    context.userProperty().setValue(message.getUser());
                }
            }
            case READ: {

            }
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {
        super.onError(session, thr);
    }
}