package pl.rabbitmq.notificationlistener.notification.sender;

import pl.rabbitmq.notificationlistener.notification.Notification;

public interface Sender {

    void send(Notification notification);
}
