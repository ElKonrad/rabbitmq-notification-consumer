package pl.rabbitmq.notificationlistener.notification.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rabbitmq.notificationlistener.notification.Notification;

@Component
@Slf4j
public class EmailSender implements Sender {

    @Override
    public void send(Notification notification) {
        log.info("Sent email notification: " + notification.toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
