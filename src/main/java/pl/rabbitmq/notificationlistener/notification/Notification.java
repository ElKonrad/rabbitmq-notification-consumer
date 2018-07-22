package pl.rabbitmq.notificationlistener.notification;

import lombok.Data;

import java.util.Date;

@Data
public class Notification {

    private String text;
    private String to;
    private String level;
    private Date createdDate = new Date();
}
