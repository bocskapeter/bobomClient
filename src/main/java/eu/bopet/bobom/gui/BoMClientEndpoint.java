package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMMessage;
import eu.bopet.bobom.core.entities.Users;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.logging.Logger;

@ClientEndpoint
public class BoMClientEndpoint extends Endpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Session session;
    private GUIContext context;

    public BoMClientEndpoint(GUIContext context) {
        this.context = context;
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        session.addMessageHandler((MessageHandler.Whole<String>) s -> {
            try {
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] data = decoder.decode(s);
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                BoMMessage message = (BoMMessage) is.readObject();
                processMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    private void processMessage(BoMMessage message) {
        switch (message.getActivity()) {
            case LOGIN: {
                if (context.getUser() == null) {
                    System.out.println("user login");
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