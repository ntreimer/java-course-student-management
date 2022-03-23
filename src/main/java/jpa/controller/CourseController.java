package jpa.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

public class CourseController {

    protected void populateCourseTable() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        Transaction t = session.beginTransaction();

        // delete existing course data
        session.createSQLQuery("drop table student_course;").executeUpdate();
        session.createSQLQuery("truncate table course;").executeUpdate();

        String nativeSQL =
        "insert into Course (id, name, instructor) values \n" +
        "(1, 'English', 'Anderea Scamaden'),\n" +
        "(2, 'Mathematics', 'Eustace Niemetz'),\n" +
        "(3, 'Anatomy', 'Reynolds Pastor'),\n" +
        "(4, 'Organic Chemistry', 'Odessa Belcher'),\n" +
        "(5, 'Physics', 'Dani Swallow'),\n" +
        "(6, 'Digital Logic', 'Glenden Reilingen'),\n" +
        "(7, 'Object Oriented Programming', 'Giselle Ardy'),\n" +
        "(8, 'Data Structures', 'Carolan Stoller'),\n" +
        "(9, 'Politics', 'Carmita De Maine'),\n" +
        "(10, 'Art', 'Kingsly Doxsey');";

        NativeQuery nativeQuery = session.createSQLQuery(nativeSQL);
        nativeQuery.executeUpdate();

        t.commit();
        System.out.println("Added courses.");
        factory.close();
        session.close();
    }
}

class CourseApp {
    public static void main(String[] args) {
        CourseController c = new CourseController();
        c.populateCourseTable();
    }
}
