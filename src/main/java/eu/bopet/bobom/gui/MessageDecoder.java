package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMMessage;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.logging.Logger;

public class MessageDecoder implements Decoder.Text<BoMMessage> {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public BoMMessage decode(String s) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] data = decoder.decode(s);
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            BoMMessage result = (BoMMessage) is.readObject();
            return result;
        } catch (IOException | ClassNotFoundException e) {
            logger.warning(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
