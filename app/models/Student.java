package models;

import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.*;

import javax.persistence.*;

import java.util.*;

/**
 * Student entity managed by Ebean.
 */
@Entity
@Table(name="students")
public class Student extends Model {

    private static final String ID = "id";
    private static final String USERNAME = "username";

    @Id
    @Constraints.Required
    @Formats.NonEmpty
    public String id;

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String fullname;

    @Constraints.Required
    @ManyToMany
    public List<Course> coursesPreferred = new ArrayList<Course>();

    @Constraints.Required
    public int numCoursesPreferred;

    @Constraints.Required
    @OneToOne
    public Transcript transcript;

    // -- Queries

    private static final Model.Finder<String, Student> FIND =
            new Model.Finder<>(String.class, Student.class);

    /**
     * Returns all registered students.
     *
     * @return all students
     */
    public static List<Student> findAll() {
        return FIND.all();
    }

    /**
     * Returns the student with the given ID.
     *
     * @param id student ID
     * @return corresponding student
     */
    public static Student findById(String id) {
        return FIND.where().eq(ID, id).findUnique();
    }

    /**
     * Returns the student with the given username.
     *
     * @param user student username
     * @return corresponding student
     */
    public static Student findByUserName(String user) {
        return FIND.where().eq(USERNAME, user).findUnique();
    }


    // --

    @Override
    public String toString() {
        return "Student{" + id + ":" + username + "}";
    }

}