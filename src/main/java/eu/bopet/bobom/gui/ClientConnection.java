package eu.bopet.bobom.gui;


import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientConnection {
    private static CountDownLatch messageLatch;
    public static void main(String[] args) {
        try {
            messageLatch = new CountDownLatch(1);
            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
            ClientManager client = ClientManager.createClient();
            URI localhost = new URI("ws://localhost:8080/bobomServer-1.0-SNAPSHOT/engineering/admin");
            GetUserClientEndpoint clientEndpoint = new GetUserClientEndpoint();
            client.asyncConnectToServer(clientEndpoint,cec,localhost);
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (URISyntaxException | DeploymentException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
