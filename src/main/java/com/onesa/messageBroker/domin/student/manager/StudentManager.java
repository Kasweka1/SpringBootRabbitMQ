package com.onesa.messageBroker.domin.student.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onesa.messageBroker.core.email.service.MailService;
import com.onesa.messageBroker.domin.student.model.Student;
import com.onesa.messageBroker.domin.student.repository.StudentRepository;
import com.onesa.messageBroker.domin.student.service.StudentService;

@Service
public class StudentManager implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MailService mailService;

    @Override
    public Student addStudent(Student student) {
        Student createdStudent = studentRepository.save(student);
        mailService.updateStudentOnSuccessfulCreation(createdStudent);
        return createdStudent;
    }
    
}
