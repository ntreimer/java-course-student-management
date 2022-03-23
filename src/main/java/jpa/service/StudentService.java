package jpa.service;

import jpa.dao.StudentDAO;
import jpa.entitymodels.Student;
import jpa.entitymodels.Course;
import org.hibernate.HibernateException;
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

        // check if the student's entered password matches the one in the database
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

        Transaction t = null;
        try {
            t = session.beginTransaction();
            List<Course> courses = getStudentCourses(sEmail);

            // check if student is already registered for course
            for (Course course : courses) {
                if (course.getcId() == cId) {
                    return;
                }
            }

            // if student is not registered, add to the student_course table
            String hql = "INSERT INTO student_course (student_email, course_id) " +
                    "VALUES ( :emailParam, :courseParam)";

            Query query = session.createSQLQuery(hql);
            query.setParameter("emailParam", sEmail);
            query.setParameter("courseParam", cId);
            query.executeUpdate();

            t.commit();
        } catch (HibernateException e) {
            if (t != null) {
                t.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }
    }


    @Override
    public List<Course> getStudentCourses(String email) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        // find courses where student email matches
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
