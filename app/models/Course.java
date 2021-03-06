package models;

import models.names.Columns;
import models.names.Fields;
import models.names.Tables;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Course entity managed by Ebean.
 */
@Entity
@Table(name = Tables.COURSES)
public class Course extends Model {

    /**
     * Number of credits each course is worth.
     */
    public static final Integer CREDIT_VALUE = 3;

    /**
     * Internal ID.
     */
    @Id
    @Constraints.Required
    @Formats.NonEmpty
    public String id;

    /**
     * Course tag (for example, "CS 6310").
     */
    @Constraints.Required
    public String tag;

    /**
     * Course name (for example, "Software Architecture and Design").
     */
    @Constraints.Required
    public String name;

    /**
     * Course abbreviation (for example, "SAD").
     */
    @Constraints.Required
    public String abbrev;

    /**
     * Whether the course is a foundational (core) course.
     */
    @Constraints.Required
    public boolean core;

    /**
     * Maximum class size for this course.
     */
    @Constraints.Required
    public Integer maxClassSize;

    /**
     * Student transcripts that include this course.
     */
    @ManyToMany
    public List<Transcript> transcriptsIncludingCourse = new ArrayList<>();

    /**
     * The courses for which this course is a prerequisite.
     */
    @ManyToMany
    @JoinTable(name = Tables.COURSES_PREREQUISITES,
            joinColumns = @JoinColumn(name = Columns.COURSE_ID),
            inverseJoinColumns = @JoinColumn(name = Columns.PREREQUISITE_ID))
    public List<Course> prerequisiteFor = new ArrayList<>();

    /**
     * The courses that are prerequisites for this course. That is, you have
     * to have already taken the courses listed here, before being allowed
     * to take this course.
     */
    @ManyToMany(mappedBy = Fields.PREREQUISITE_FOR)
    public List<Course> prerequisites = new ArrayList<>();

    /**
     * The students interested in taking this course.
     */
    @Constraints.Required
    @ManyToMany(mappedBy = Fields.COURSES_PREFERRED)
    public List<Student> studentsInterested = new ArrayList<>();

    /**
     * The student requests that this course is referenced from.
     */
    @ManyToMany(mappedBy = Fields.COURSES_PREFERRED)
    public List<StudentRequest> studentRequest = new ArrayList<>();

    // -- Queries

    private static final Model.Finder<String, Course> FIND =
            new Model.Finder<>(String.class, Course.class);

    /**
     * Returns all available courses.
     */
    public static List<Course> findAll() {
        return FIND.all();
    }

    /**
     * Returns the course with the given ID.
     *
     * @param id course ID
     */
    public static Course findById(String id) {
        return FIND.fetch(Fields.STUDENTS_INTERESTED)
                .where().eq(Fields.ID, id).findUnique();
    }

    // --

    /**
     * Returns the course title, a concatenation of the course tag and the
     * course name.
     *
     * @return course title
     */
    public String title() {
        return tag + " " + name;
    }

    @Override
    public String toString() {
        return "Course{" + tag + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Course course = (Course) o;
        return id.equals(course.id);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}