package com.onesa.messageBroker.domin.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onesa.messageBroker.domin.student.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    
    Student findStudentById(long id);
}
