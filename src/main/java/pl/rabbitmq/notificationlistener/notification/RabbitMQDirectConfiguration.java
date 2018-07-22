package pl.rabbitmq.notificationlistener.notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.rabbitmq.notificationlistener.notification.listener.EmailListener;
import pl.rabbitmq.notificationlistener.notification.listener.SmsListener;

@Configuration
public class RabbitMQDirectConfiguration {

    private static final String HOSTNAME = "localhost";
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";

    private static final String NOTIFICATION_DIRECT_EXCHANGE = "NotificationDirectExchange";
    private static final String NOTIFICATION_TOPIC_EXCHANGE = "NotificationTopicExchange";
    private static final String NOTIFICATION_FANOUT_EXCHANGE = "NotificationFanoutExchange";

    private static final String NOTIFICATION_EMAIL_QUEUE_NAME = "NotificationEmailQueue";
    private static final String NOTIFICATION_SMS_QUEUE_NAME = "NotificationSmsQueue";

    private static final String ROUTING_KEY_NOTIFICATION_MESSAGE_EMAIL = "notification.message.email";
    private static final String ROUTING_KEY_NOTIFICATION_MESSAGE_SMS = "notification.message.sms";
    private static final String ROUTING_KEY_NOTIFICATION_LOG_ALL = "notification.log.*";
    private static final String ROUTING_KEY_NOTIFICATION_LOG_ERROR = "notification.log.error";
    private static final String ROUTING_KEY_NOTIFICATION_LOG_WARNING = "notification.log.warning";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(HOSTNAME);
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        return connectionFactory;
    }

    @Bean
    public Exchange notificationDirectExchange() {
        return ExchangeBuilder.directExchange(NOTIFICATION_DIRECT_EXCHANGE)
                              .build();
    }

    @Bean
    public Exchange notificationTopicExchange() {
        return ExchangeBuilder.topicExchange(NOTIFICATION_TOPIC_EXCHANGE)
                              .build();
    }

    @Bean
    public Exchange notificationFanoutExchange() {
        return ExchangeBuilder.fanoutExchange(NOTIFICATION_FANOUT_EXCHANGE)
                              .build();
    }

    /*
     *** DIRECT - using Email and Sms Queues ***
     */

    @Bean
    public Binding directMessageEmailBinding() {
        return BindingBuilder.bind(notificationEmailQueue())
                             .to(notificationDirectExchange())
                             .with(ROUTING_KEY_NOTIFICATION_MESSAGE_EMAIL)
                             .noargs();
    }

    @Bean
    public Binding directMessageSmsBinding() {
        return BindingBuilder.bind(notificationSmsQueue())
                             .to(notificationDirectExchange())
                             .with(ROUTING_KEY_NOTIFICATION_MESSAGE_SMS)
                             .noargs();
    }

    /*
     *** FANOUT - using Email and Sms Queues ***
     */

    @Bean
    public Binding fanoutEmailQueueBinding() {
        return BindingBuilder.bind(notificationEmailQueue())
                             .to(notificationFanoutExchange())
                             .with(ROUTING_KEY_NOTIFICATION_LOG_WARNING)
                             .noargs();
    }

    @Bean
    public Binding fanoutSmsQueueBinding() {
        return BindingBuilder.bind(notificationSmsQueue())
                             .to(notificationFanoutExchange())
                             .with(ROUTING_KEY_NOTIFICATION_LOG_WARNING)
                             .noargs();
    }

    /*
     *** TOPIC ***
     */

    // EMAIL: Queue, Listener, Binding
    @Bean
    public Queue notificationEmailQueue() {
        return new Queue(NOTIFICATION_EMAIL_QUEUE_NAME);
    }

    @Bean
    public EmailListener notificationEmailListener() {
        return new EmailListener();
    }

    @Bean
    public Binding topicLogEmailBinding() {
        return BindingBuilder.bind(notificationEmailQueue())
                             .to(notificationTopicExchange())
                             .with(ROUTING_KEY_NOTIFICATION_LOG_ALL)
                             .noargs();
    }

    // SMS: Queue, Listener, Binding

    @Bean
    public Queue notificationSmsQueue() {
        return new Queue(NOTIFICATION_SMS_QUEUE_NAME);
    }

    @Bean
    public SmsListener notificationSmsListener() {
        return new SmsListener();
    }

    @Bean
    public Binding topicLogSmsBinding() {
        return BindingBuilder.bind(notificationSmsQueue())
                             .to(notificationTopicExchange())
                             .with(ROUTING_KEY_NOTIFICATION_LOG_ERROR)
                             .noargs();
    }

    // Email and Sms containers

    @Bean
    public MessageListenerContainer notificationEmailListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(notificationEmailQueue());
        simpleMessageListenerContainer.setMessageListener(notificationEmailListener());
        return simpleMessageListenerContainer;
    }

    @Bean
    public MessageListenerContainer notificationSmsListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(notificationSmsQueue());
        simpleMessageListenerContainer.setMessageListener(notificationSmsListener());
        return simpleMessageListenerContainer;
    }

}
