package pl.rabbitmq.notificationlistener.notification.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rabbitmq.notificationlistener.notification.Notification;

@Component
@Slf4j
public class SmsSender implements Sender {

    @Override
    public void send(Notification notification) {
        log.info("Sent sms notification: " + notification.toString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
