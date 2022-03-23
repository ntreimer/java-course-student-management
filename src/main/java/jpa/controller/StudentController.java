package jpa.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

public class StudentController {

    protected void populateStudentTable() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        Transaction t = session.beginTransaction();

        // delete existing student data
        session.createSQLQuery("drop table student_course;").executeUpdate();
        session.createSQLQuery("truncate table student;").executeUpdate();

        String nativeSQL =
            "insert into Student (email, name, password) values " +
            "('hluckham0@google.ru', 'Hazel Luckham', 'X1uZcoIh0dj'),\n" +
            "('sbowden1@yellowbook.com', 'Sonnnie Bowden', 'SJc4aWSU'),\n" +
            "('qllorens2@howstuffworks.com', 'Quillan Llorens', 'W6rJuxd'),\n" +
            "('cstartin3@flickr.com', 'Clem Startin', 'XYHzJ1S'),\n" +
            "('tattwool4@biglobe.ne.jp', 'Thornie Attwool', 'Hjt0SoVmuBz'),\n" +
            "('hguerre5@deviantart.com', 'Harcourt Guerre', 'OzcxzD1PGs'),\n" +
            "('htaffley6@columbia.edu', 'Holmes Taffley', 'xowtOQ'),\n" +
            "('aiannitti7@is.gd', 'Alexandra Iannitti', 'TWP4hf5j'),\n" +
            "('ljiroudek8@sitemeter.com', 'Laryssa Jiroudek', 'bXRoLUP'),\n" +
            "('cjaulme9@bing.com', 'Cahra Jaulme', 'FnVklVgC6r6');";
        NativeQuery nativeQuery = session.createSQLQuery(nativeSQL);
        nativeQuery.executeUpdate();

        t.commit();
        System.out.println("Added students.");
        factory.close();
        session.close();
    }
}

class StudentApp {
    public static void main(String[] args) {
        StudentController s = new StudentController();
        s.populateStudentTable();
    }
}