package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMActivity;
import eu.bopet.bobom.core.BoMMessage;
import eu.bopet.bobom.core.entities.Users;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientEndpointTest {
    @Test
    public void Test() throws InterruptedException {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle labels = ResourceBundle.getBundle("text/LabelsBundle", currentLocale, new UTF8Control());
        GUIContext context = new GUIContext(labels);
        String eMail = "admin";
        BoMMessage message = new BoMMessage(BoMActivity.LOGIN, Users.class,null, Arrays.asList(eMail));
        Thread.sleep(2000);
        context.sendMessage(message);
        Thread.sleep(2000);
        assert context.userProperty().get().getEMail().equals(eMail);
    }
}
