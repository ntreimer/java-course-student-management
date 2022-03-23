package jpa.mainrunner;

import jpa.controller.StudentController;
import jpa.entitymodels.Course;
import jpa.entitymodels.Student;
import jpa.service.CourseService;
import jpa.service.StudentService;

import java.util.List;
import java.util.Scanner;


public class SMSRunner {

    private final Scanner inputScanner;
    private final StringBuilder sb;
    private final CourseService courseService;
    private final StudentService studentService;
    private StudentController studentController;
    private Student currentStudent;

    public SMSRunner() {
        inputScanner = new Scanner(System.in);
        sb = new StringBuilder();
        courseService = new CourseService();
        studentService = new StudentService();
        studentController = new StudentController();
    }

    public static void main(String[] args) {

        SMSRunner sms = new SMSRunner();
        sms.run();

    }

    private void run() {
        // Logic for login/quit menu
        switch (menu1()) {
            case 1:
                if (studentLogin()) {
                    registerMenu();
                }
                break;
            case 2:
                System.out.println("Goodbye!");
                break;

            default:

        }
    }

    private int menu1() {
        // Display menu and handle input
        sb.append("\n1. Student Login\n2. Quit Application\nPlease Enter Selection: ");
        System.out.print(sb);
        sb.delete(0, sb.length());

        return inputScanner.nextInt();
    }

    private boolean studentLogin() {
        // Receive input for login
        boolean retValue = false;
        System.out.print("Enter your email address: ");
        String email = inputScanner.next();
        System.out.print("Enter your password: ");
        String password = inputScanner.next();

        // Get Student object by email
        Student student = studentService.getStudentByEmail(email);
        if (student != null) {
            currentStudent = student;
        }

        // Check if user is valid student
        if (studentService.validateStudent(email, password)) {
            List<Course> courses = studentService.getStudentCourses(email);
            System.out.println("MyClasses");
            if (courses != null) {
                for (Course course : courses) {
                    System.out.println(course.getcName());
                }
            }
            retValue = true;
        } else {
            System.out.println("User Validation failed. GoodBye!");
        }
        return retValue;
    }

    private void registerMenu() {
        // Show registration/quit menu for students
        sb.append("\n1. Register a class\n2. Logout\nPlease Enter Selection: ");
        System.out.print(sb);
        sb.delete(0, sb.length());

        switch (inputScanner.nextInt()) {
            case 1:
                // Course registration
                List<Course> allCourses = courseService.getAllCourses();
                List<Course> studentCourses = studentService.getStudentCourses(currentStudent.getsEmail());
                allCourses.removeAll(studentCourses);
                // Display this student's courses
                System.out.printf("%-5s %-30s %-25s\n", "ID", "Course", "Instructor");
                for (Course course : allCourses) {
                    System.out.printf("%-5s %-30S %-25s\n",
                            course.getcId(), course.getcName(), course.getcInstructorName());
                }
                System.out.println();
                // Request course number for registration
                System.out.print("Enter Course Number: ");
                int number = inputScanner.nextInt();
                // check if input is within bounds
                Course newCourse = courseService.getAllCourses().get(number - 1);

                if (newCourse != null) {
                    // if the course number is valid, register student for course
                    studentService.registerStudentToCourse(currentStudent.getsEmail(), number);
                    Student temp = studentService.getStudentByEmail(currentStudent.getsEmail());

                    StudentService scService = new StudentService();
                    List<Course> sCourses = scService.getStudentCourses(temp.getsEmail());

                    // Show updated list of classes
                    System.out.println("MyClasses");
                    for (Course course : sCourses) {
                        System.out.println(course.getcName());
                    }
                }
                System.out.println("Thanks for registering! See you in class!");
                break;
            case 2:
            default:
                System.out.println("Goodbye!");
        }
    }
}
