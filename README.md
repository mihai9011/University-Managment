# university management system (thema 8)

projekt 1: basic structure of a spring boot application, in-memory (no database).
projekt 2: thymeleaf web interface (list, create, delete) on top of the same entities.
team of two: each of us owns half of the entities.

## run

```bash
cd demo
./mvnw spring-boot:run
```

then open `http://localhost:8080/hello`. it answers `Die Anwendung funktioniert!`.
the web interface starts at e.g. `http://localhost:8080/universities`.

run the tests with `./mvnw test`.

## structure

```
demo/src/main/java/com/example/universitymanagement/
  model/        classes from the uml diagram
  repository/   in-memory storage
  service/      business logic and validation
  controller/   one web controller per entity + HelloController
demo/src/main/resources/templates/
  <entity>/     index.html (table + delete buttons), form.html (create form)
```

layers are kept strictly separate: controller -> service -> repository -> model.

## who did what

| part | entities |
|---|---|
| emi | `University`, `Department`, `Room`, `Course` |
| bogdan | `Student`, `Enrollment`, `Staff` (`Teacher`, `Assistant`), `TeachingAssignment` |

## design decisions

- **one repository per entity, one shared base class.** every repository extends
  `BaseRepo<T extends BaseEntity>` (an in-memory `List` with `save`, `findAll`,
  `findById`, `delete`). this avoids duplicated code (dry). the entity ids are uuid
  strings, generated once in `BaseEntity`.
- **one service and one controller per entity.** each service has a single clear
  responsibility (single responsibility) and works through repositories. the only
  service-to-service calls are the delete cascades described under "keeping the lists
  in sync". controllers are one per entity too, so they can grow independently in
  later phases.
- **validation lives in the services.** services check that referenced ids exist
  (e.g. `CourseService` checks the department and room, `EnrollmentService` checks
  the student and course), reject null/missing required fields, and throw
  `IllegalArgumentException` for not-found cases. student and staff e-mails must be
  unique and the student's birth date must be in the past.
- **inheritance for staff.** `Staff` is abstract, with `Teacher` and `Assistant` as
  subclasses (liskov: both can be used wherever a `Staff` is expected). the web form
  picks the subclass through `StaffDTO`.
- **relations by id.** entities reference each other by id (`departmentId`, `roomId`,
  `studentId`, ...) like in the uml, which keeps the in-memory data simple. the uml's
  lists on the parent classes are filled from these ids (see below).
- **dates** use `LocalDate`, not `String`. enums (`Grade`, `ManagingType`,
  `AssistantRole`) are used instead of free-text status fields.

## keeping the lists in sync

a relation is stored twice: as an id on the child (`course.departmentId`) and as an
object in a list on the parent (`department.courses`). our strategy: **the id is the
source of truth, and the service of the child keeps the parent's lists up to date.**
controllers and repositories know nothing about it.

| saving or deleting a... | updates |
|---|---|
| `Enrollment` | `Student.enrollments`, `Course.enrollments` |
| `TeachingAssignment` | `Staff.assignments`, `Course.assignments` |
| `Course` | `Department.courses`, `Room.courses` |
| `Teacher` | `Department.teachers` |

- **save: unlink, then link.** the entry is first removed by id from every parent, then
  added to the right ones. saving twice can't create a duplicate, and a moved child
  disappears from its old parent.
- **duplicates are rejected.** a student can enroll in a course only once. a staff member
  can have one assignment per course and managing type (lab and seminar are allowed).
- **re-saving a parent keeps its lists**, because a form creates a new, empty object.
- **deleting cascades to link objects only.** deleting a student deletes its enrollments,
  deleting a course deletes its enrollments and assignments, deleting staff deletes its
  assignments. they mean nothing without both sides.
- **deleting a department or room does not delete its courses or teachers.** they are
  real data and one click shouldn't remove them, so they keep the old id.
- **`University.departments` and `University.rooms` are not synced.** the uml gives
  `Department` and `Room` no `universityId`, so nothing says where they belong.

- **why lists and not queries.** we could store only the ids and offer queries like
  `findByStudentId(...)`, which can't go out of sync. we kept the lists because the uml
  specifies them.

`ListSyncTest` covers these rules.

## web interface (projekt 2)

every entity has the same four routes, handled by its own `@Controller`:

| method | route | purpose |
|---|---|---|
| GET | `/<entity>` | list all objects (`index.html`) |
| GET | `/<entity>/new` | show the create form (`form.html`) |
| POST | `/<entity>` | create the object, then redirect to the list |
| POST | `/<entity>/{id}/delete` | delete the object, then redirect to the list |

entities and routes: `/universities`, `/departments`, `/rooms`, `/courses`,
`/students`, `/staff`, `/enrollments`, `/teachingassignments`.

- **delete is a POST**, as the task asks, so a link or a crawler can never delete
  something by accident (a GET on a delete url returns 405).
- **controllers only call services**, never repositories. after a successful post they
  redirect (post/redirect/get), so a page refresh doesn't submit the form twice.
- **staff form uses a `StaffDTO`.** `Staff` is abstract, so a form can't bind to it
  directly. the dto carries the fields of both subtypes plus a `type`, and the
  controller builds a `Teacher` or an `Assistant`. a little javascript shows only the
  fields of the chosen type.
- **foreign ids are typed in by hand** in the forms (e.g. `departmentId` on a course),
  to keep the views simple. the services reject ids that don't exist.
- one template folder per entity, each file with one purpose (list or form).

## added properties (task 5)

| class | new property |
|---|---|
| `Course` | `semester` |
| `Student` | `email`, `birthDate` |
| `Teacher` | `email`, `hireDate` |

## known limits

- data is lost when the application stops (in-memory by design).
- invalid input (unknown id, duplicate or malformed e-mail, ...) is rejected by the
  services with an `IllegalArgumentException`, which shows as a plain 500 error page.
  friendly error messages are not part of projekt 2.
- deleting a department or room leaves its courses and teachers with an id that no
  longer exists.
