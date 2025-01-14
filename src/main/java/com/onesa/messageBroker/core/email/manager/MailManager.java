package com.onesa.messageBroker.core.email.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.onesa.messageBroker.core.email.manager.utils.MailMessage;
import com.onesa.messageBroker.core.email.service.MailService;
import com.onesa.messageBroker.core.rabbitMQ.MQConfig;
import com.onesa.messageBroker.domin.student.model.Student;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailManager implements  MailService{

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.name}")
    private String from;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${app.url}") 
    private String appUrl;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    @Async
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username, from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);

            
        } catch (MessagingException e) {
            e.printStackTrace(); 
            throw new RuntimeException("Failed to send plain email", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported encoding for email", e);
        }
        CompletableFuture.completedFuture(null);
    }

    @Override
    public void updateStudentOnSuccessfulCreation(Student student) {
        try {
             String emailContent = generateEmailContent(student, "Welcome to RabbitMQ Messaging");
             MailMessage emailMessage = new MailMessage(
                student.getEmail(),
                "Account Created Successfully",
                emailContent
        );


            
             amqpTemplate.convertAndSend(MQConfig.EMAIL_EXCHANGE, MQConfig.EMAIL_ROUTING_KEY, emailMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send account creation and activation email", e);
        }

        
    }

     private String generateEmailContent(Student student, String message) throws IOException {

        ClassPathResource resource = new ClassPathResource("email_template.html");
        String content = new String(Files.readAllBytes(resource.getFile().toPath()));
        
        content = content.replace("{{firstName}}", student.getFirstName());
        content = content.replace("{{lastName}}", student.getLastName());
        content = content.replace("{{message}}", message);
    
        content = content.replace("{{message}}", message);

        return content;
    }


    
}
