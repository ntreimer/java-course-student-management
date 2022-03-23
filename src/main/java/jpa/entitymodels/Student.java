package jpa.entitymodels;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "student")
public class Student implements Serializable {

    @Column(name = "email", length = 50, nullable = false)
    @Id
    private String sEmail;

    @Column(name = "name", length = 50, nullable = false)
    private String sName;

    @Column(name = "password", length = 50, nullable = false)
    private String sPass;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "student_course",
            joinColumns = {@JoinColumn(name = "student_email", referencedColumnName = "email")},
            inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")}
    )
    private Set<Course> sCourses;

    public Student() {
        this.sEmail = "";
        this.sName = "";
        this.sPass = "";
        this.sCourses = new HashSet<>();
    }

    public Student(String sEmail, String sName, String sPass, Set<Course> sCourses) {
        this.sEmail = sEmail;
        this.sName = sName;
        this.sPass = sPass;
        this.sCourses = sCourses;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsPass() {
        return sPass;
    }

    public void setsPass(String sPass) {
        this.sPass = sPass;
    }

    public Set<Course> getsCourses() {
        return sCourses;
    }

    public void setsCourses(Set<Course> sCourses) {
        this.sCourses = sCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(sEmail, student.sEmail) && Objects.equals(sName, student.sName) && Objects.equals(sPass, student.sPass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sEmail, sName, sPass);
    }
}
