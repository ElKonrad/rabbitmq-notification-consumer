package pl.rabbitmq.notificationlistener.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.rabbitmq.notificationlistener.notification.Notification;
import pl.rabbitmq.notificationlistener.notification.sender.SmsSender;

import java.io.IOException;

@Transactional
@Slf4j
public class SmsListener implements MessageListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SmsSender smsSender;

    @Override
    public void onMessage(Message message) {
        try {
            Notification notification = objectMapper.readValue(message.getBody(), Notification.class);
            smsSender.send(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
