package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMMessage;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.logging.Logger;

public class MessageEncoder implements Encoder.Text<BoMMessage> {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(BoMMessage message) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(message);
            Base64.Encoder encoder = Base64.getEncoder();
            String result = encoder.encodeToString(byteArrayOutputStream.toByteArray());
            return result;
        } catch (IOException e) {
            logger.warning(e.getLocalizedMessage());
            return "";
        }
    }
}
