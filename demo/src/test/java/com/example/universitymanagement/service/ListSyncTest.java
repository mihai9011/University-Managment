package com.example.universitymanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.universitymanagement.model.Assistant;
import com.example.universitymanagement.model.AssistantRole;
import com.example.universitymanagement.model.Course;
import com.example.universitymanagement.model.Department;
import com.example.universitymanagement.model.Enrollment;
import com.example.universitymanagement.model.ManagingType;
import com.example.universitymanagement.model.Room;
import com.example.universitymanagement.model.Student;
import com.example.universitymanagement.model.Teacher;
import com.example.universitymanagement.model.TeachingAssignment;

// the spring context (and so the repositories) is shared between tests, every test uses its own new objects
@SpringBootTest
class ListSyncTest {

    @Autowired private DepartmentService departments;
    @Autowired private RoomService rooms;
    @Autowired private CourseService courses;
    @Autowired private StudentService students;
    @Autowired private StaffService staff;
    @Autowired private EnrollmentService enrollments;
    @Autowired private TeachingAssignmentService assignments;

    private Department department;
    private Room room;
    private Course course;
    private Student student;
    private Teacher teacher;

    @BeforeEach
    void createData() {
        department = newDepartment();
        room = newRoom();
        course = newCourse(department, room);
        student = newStudent();
        teacher = newTeacher(department);
    }

    @Test
    void enrollmentIsLinkedOnSaveAndUnlinkedOnDelete() {
        Enrollment enrollment = newEnrollment(student, course);

        assertTrue(student.getEnrollments().contains(enrollment));
        assertTrue(course.getEnrollments().contains(enrollment));

        enrollments.delete(enrollment.getId());

        assertTrue(student.getEnrollments().isEmpty());
        assertTrue(course.getEnrollments().isEmpty());
    }

    @Test
    void sameStudentAndCourseCannotBeEnrolledTwice() {
        newEnrollment(student, course);
        int total = enrollments.getAll().size();

        assertThrows(IllegalArgumentException.class, () -> newEnrollment(student, course));

        assertEquals(1, student.getEnrollments().size());
        assertEquals(1, course.getEnrollments().size());
        assertEquals(total, enrollments.getAll().size());
    }

    @Test
    void savingAnEnrollmentAgainDoesNotDuplicateIt() {
        Enrollment enrollment = newEnrollment(student, course);

        enrollments.save(enrollment);

        assertEquals(1, student.getEnrollments().size());
        assertEquals(1, course.getEnrollments().size());
    }

    @Test
    void movingAnEnrollmentUpdatesOldAndNewStudent() {
        Enrollment enrollment = newEnrollment(student, course);
        Student other = newStudent();

        Enrollment moved = new Enrollment(other.getId(), course.getId());
        moved.setId(enrollment.getId());
        enrollments.save(moved);

        assertTrue(student.getEnrollments().isEmpty());
        assertTrue(other.getEnrollments().contains(moved));
        assertEquals(1, course.getEnrollments().size());
        assertSame(moved, course.getEnrollments().get(0));
    }

    @Test
    void assignmentIsLinkedOnSaveAndUnlinkedOnDelete() {
        TeachingAssignment assignment = newAssignment(course, ManagingType.LAB);

        assertTrue(course.getAssignments().contains(assignment));
        assertTrue(teacher.getAssignments().contains(assignment));

        assignments.delete(assignment.getId());

        assertTrue(course.getAssignments().isEmpty());
        assertTrue(teacher.getAssignments().isEmpty());
    }

    @Test
    void duplicateAssignmentIsRejectedButOtherManagingTypeIsAllowed() {
        newAssignment(course, ManagingType.LAB);

        assertThrows(IllegalArgumentException.class, () -> newAssignment(course, ManagingType.LAB));
        newAssignment(course, ManagingType.SEMINAR);

        assertEquals(2, course.getAssignments().size());
        assertEquals(2, teacher.getAssignments().size());
    }

    @Test
    void courseIsLinkedToDepartmentAndRoomAndCanMove() {
        assertTrue(department.getCourses().contains(course));
        assertTrue(room.getCourses().contains(course));

        Department otherDepartment = newDepartment();
        Room otherRoom = newRoom();
        course.setDepartmentId(otherDepartment.getId());
        course.setRoomId(otherRoom.getId());
        courses.save(course);

        assertTrue(department.getCourses().isEmpty());
        assertTrue(room.getCourses().isEmpty());
        assertEquals(1, otherDepartment.getCourses().size());
        assertEquals(1, otherRoom.getCourses().size());
    }

    @Test
    void teacherIsLinkedToDepartmentAndUnlinkedOnDelete() {
        assertTrue(department.getTeachers().contains(teacher));

        staff.save(teacher);
        assertEquals(1, department.getTeachers().size());

        staff.delete(teacher.getId());
        assertTrue(department.getTeachers().isEmpty());
    }

    @Test
    void assistantIsNotLinkedToADepartment() {
        Assistant assistant = new Assistant();
        assistant.setName("Assistant");
        assistant.setRole(AssistantRole.TA);
        staff.save(assistant);

        assertEquals(1, department.getTeachers().size());
        staff.delete(assistant.getId());
    }

    @Test
    void deletingAStudentDeletesItsEnrollments() {
        Enrollment enrollment = newEnrollment(student, course);

        students.delete(student.getId());

        assertTrue(course.getEnrollments().isEmpty());
        assertFalse(enrollments.getAll().contains(enrollment));
    }

    @Test
    void deletingACourseCleansUpEverythingThatPointsToIt() {
        Enrollment enrollment = newEnrollment(student, course);
        TeachingAssignment assignment = newAssignment(course, ManagingType.COURSE);

        courses.delete(course.getId());

        assertTrue(student.getEnrollments().isEmpty());
        assertTrue(teacher.getAssignments().isEmpty());
        assertTrue(department.getCourses().isEmpty());
        assertTrue(room.getCourses().isEmpty());
        assertFalse(enrollments.getAll().contains(enrollment));
        assertFalse(assignments.getAll().contains(assignment));
    }

    @Test
    void deletingStaffDeletesItsAssignments() {
        TeachingAssignment assignment = newAssignment(course, ManagingType.LAB);

        staff.delete(teacher.getId());

        assertTrue(course.getAssignments().isEmpty());
        assertFalse(assignments.getAll().contains(assignment));
    }

    @Test
    void savingAParentAgainWithANewObjectKeepsItsLists() {
        newEnrollment(student, course);
        newAssignment(course, ManagingType.LAB);

        Student newStudentObject = new Student("Renamed", student.getEmail(), student.getBirthDate());
        newStudentObject.setId(student.getId());
        students.save(newStudentObject);
        assertEquals(1, newStudentObject.getEnrollments().size());

        Course newCourseObject = new Course("Renamed", 5, "1", department.getId(), room.getId());
        newCourseObject.setId(course.getId());
        courses.save(newCourseObject);
        assertEquals(1, newCourseObject.getEnrollments().size());
        assertEquals(1, newCourseObject.getAssignments().size());
        assertSame(newCourseObject, department.getCourses().get(0));

        Department newDepartmentObject = new Department("Renamed");
        newDepartmentObject.setId(department.getId());
        departments.save(newDepartmentObject);
        assertEquals(1, newDepartmentObject.getCourses().size());
        assertEquals(1, newDepartmentObject.getTeachers().size());

        Room newRoomObject = new Room(10, "Renamed");
        newRoomObject.setId(room.getId());
        rooms.save(newRoomObject);
        assertEquals(1, newRoomObject.getCourses().size());

        Teacher newTeacherObject = new Teacher("Renamed", "Prof", department.getId(), teacher.getEmail(), teacher.getHireDate());
        newTeacherObject.setId(teacher.getId());
        staff.save(newTeacherObject);
        assertEquals(1, newTeacherObject.getAssignments().size());
    }

    private Department newDepartment() {
        return departments.save(new Department("Department"));
    }

    private Room newRoom() {
        return rooms.save(new Room(30, "101"));
    }

    private Course newCourse(Department department, Room room) {
        return courses.save(new Course("Course", 5, "1", department.getId(), room.getId()));
    }

    private Student newStudent() {
        Student created = new Student("Student", uniqueEmail(), LocalDate.of(2000, 1, 15));
        students.save(created);
        return created;
    }

    private Teacher newTeacher(Department department) {
        Teacher created = new Teacher("Teacher", "Prof", department.getId(), uniqueEmail(), LocalDate.of(2020, 1, 1));
        staff.save(created);
        return created;
    }

    private Enrollment newEnrollment(Student student, Course course) {
        Enrollment created = new Enrollment(student.getId(), course.getId());
        enrollments.save(created);
        return created;
    }

    private TeachingAssignment newAssignment(Course course, ManagingType managing) {
        TeachingAssignment created = new TeachingAssignment(course.getId(), teacher.getId(), managing);
        assignments.save(created);
        return created;
    }

    private String uniqueEmail() {
        return UUID.randomUUID() + "@test.com";
    }
}
