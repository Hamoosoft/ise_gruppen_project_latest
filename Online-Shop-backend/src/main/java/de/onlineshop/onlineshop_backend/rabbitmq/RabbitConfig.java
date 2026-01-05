package de.onlineshop.onlineshop_backend.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Queue + DLQ Namen
    public static final String PAYMENT_COMMANDS_QUEUE = "payment.commands";
    public static final String PAYMENT_RESULTS_QUEUE  = "payment.results";

    public static final String PAYMENT_DLX_EXCHANGE = "payment.dlx";
    public static final String PAYMENT_COMMANDS_DLQ = "payment.commands.dlq";
    public static final String PAYMENT_COMMANDS_DLQ_ROUTING_KEY = "payment.commands.dlq";

    // Dead Letter Exchange
    @Bean
    public DirectExchange paymentDlxExchange() {
        return new DirectExchange(PAYMENT_DLX_EXCHANGE);
    }

    // Dead Letter Queue
    @Bean
    public Queue paymentCommandsDlq() {
        return QueueBuilder.durable(PAYMENT_COMMANDS_DLQ).build();
    }

    // Binding DLQ an DLX
    @Bean
    public Binding paymentCommandsDlqBinding(Queue paymentCommandsDlq, DirectExchange paymentDlxExchange) {
        return BindingBuilder.bind(paymentCommandsDlq)
                .to(paymentDlxExchange)
                .with(PAYMENT_COMMANDS_DLQ_ROUTING_KEY);
    }

    // Queue für Payment Commands mit TTL + DLQ
    @Bean
    public Queue paymentCommandsQueue() {
        return QueueBuilder.durable(PAYMENT_COMMANDS_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PAYMENT_COMMANDS_DLQ_ROUTING_KEY)
                .withArgument("x-message-ttl", 60000) 
                .build();
    }

    // Queue für Payment Results (ohne TTL)
    @Bean
    public Queue paymentResultsQueue() {
        return QueueBuilder.durable(PAYMENT_RESULTS_QUEUE).build();
    }

    // JSON Converter
    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate mit JSON Converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }
}