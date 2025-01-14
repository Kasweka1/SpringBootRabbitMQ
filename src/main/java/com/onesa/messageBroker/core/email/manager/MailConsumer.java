package com.onesa.messageBroker.core.email.manager;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.onesa.messageBroker.core.email.manager.utils.MailMessage;
import com.onesa.messageBroker.core.email.service.MailService;
import com.onesa.messageBroker.core.rabbitMQ.MQConfig;

@Component
public class MailConsumer {
    @Autowired
    private MailService mailService;

    @RabbitListener(queues = MQConfig.EMAIL_QUEUE)
    public void consumeEmail(MailMessage emailMessage) {
        try {

            mailService.sendMail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getContent());
        } catch (Exception e) {
            throw new RuntimeException("Failed to process email queue message", e);
        }
    }
}
