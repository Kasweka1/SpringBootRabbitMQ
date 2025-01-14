package com.onesa.messageBroker.core.email.service;

import com.onesa.messageBroker.domin.student.model.Student;

public interface MailService {
     // Plain mail sending method
     void sendMail(String to, String subject, String content);

     void updateStudentOnSuccessfulCreation(Student student);
}
