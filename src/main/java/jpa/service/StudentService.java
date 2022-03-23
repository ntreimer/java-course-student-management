package jpa.service;

import jpa.dao.StudentDAO;
import jpa.entitymodels.Student;
import jpa.entitymodels.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class StudentService implements StudentDAO {


    @Override
    public List<Student> getAllStudents() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        Query query = session.createQuery("from Student");
        List<Student> students = query.getResultList();

        session.close();
        factory.close();
        return students;
    }

    @Override
    public Student getStudentByEmail(String sEmail) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        Student student = session.get(Student.class, sEmail);
        session.close();
        factory.close();

        return student;
    }

    @Override
    public boolean validateStudent(String sEmail, String sPassword) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        Student student = session.get(Student.class, sEmail);

        session.close();
        factory.close();

        if (student != null) {
            return student.getsPass().equals(sPassword);
        } else {
            return false;
        }
    }


    @Override
    @Transactional
    public void registerStudentToCourse(String sEmail, int cId) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        Transaction t = session.beginTransaction();

        List<Course> courses = getStudentCourses(sEmail);
        for (Course course : courses) {
            if (course.getcId() == cId) {
                return;
            }
        }
        String hql = "INSERT INTO student_course (student_email, course_id) " +
                "VALUES ( :emailParam, :courseParam)";

        Query query = session.createSQLQuery(hql);
        query.setParameter("emailParam", sEmail);
        query.setParameter("courseParam", cId);
        query.executeUpdate();

        t.commit();
        session.close();
        factory.close();
    }


    @Override
    public List<Course> getStudentCourses(String email) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        Query query = session.createQuery(
                "SELECT c FROM Course c " +
                        "JOIN c.students " +
                        "where email= :emailParam"

        );
        query.setParameter("emailParam", email);
        List<Course> courses = query.getResultList();
        return courses;
    }
}
