package pl.rabbitmq.notificationlistener.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import pl.rabbitmq.notificationlistener.notification.Notification;
import pl.rabbitmq.notificationlistener.notification.sender.EmailSender;

import java.io.IOException;

public class EmailListener implements MessageListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void onMessage(Message message) {
        try {
            Notification notification = objectMapper.readValue(message.getBody(), Notification.class);
            emailSender.send(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
