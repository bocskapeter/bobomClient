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
public class GetUserClientEndpoint extends Endpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Users user;

    public GetUserClientEndpoint() {
        this.user = null;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String s) {
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] data = decoder.decode(s);
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = null;
                try {
                    is = new ObjectInputStream(in);
                    BoMMessage message = (BoMMessage) is.readObject();
                    System.out.println(message.toString());
                    if (user == null) {
                        user = message.getUser();
                    }
                    System.out.println(user.toString());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

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