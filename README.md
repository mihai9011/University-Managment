# university management system (thema 8)

projekt 1: basic structure of a spring boot application, in-memory (no database).
projekt 2: thymeleaf web interface (list, create, delete) on top of the same entities.
projekt 3: the in-memory storage is replaced by json files (`InFileRepository`), the web interface gets details and edit pages.
team of two: each of us owns half of the entities.

## run

```bash
cd demo
./mvnw spring-boot:run
```

start the app from the `demo` folder, the json files are found through a relative path.
then open `http://localhost:8080/hello`. it answers `Die Anwendung funktioniert!`.
the web interface starts at e.g. `http://localhost:8080/universities`.

run the tests with `./mvnw test`. the tests use the same json files as the app and overwrite
them, so restore them afterwards with `git checkout -- demo/src/main/resources/data`.

## structure

```
demo/src/main/java/com/example/universitymanagement/
  model/        classes from the uml diagram
  repository/   json file storage (`InFileRepository`)
  service/      business logic and validation
  controller/   one web controller per entity + HelloController
demo/src/main/resources/templates/
  <entity>/     index.html (table + details/edit/delete), details.html, form.html (create and edit)
demo/src/main/resources/data/
  <entity>.json one json file per entity, a list of objects
```

layers are kept strictly separate: controller -> service -> repository -> model.

## who did what

| part | entities |
|---|---|
| emi | `University`, `Department`, `Room`, `Course` |
| bogdan | `Student`, `Enrollment`, `Staff` (`Teacher`, `Assistant`), `TeachingAssignment` |

## design decisions

- **one repository per entity, one shared base class.** every repository extends
  `InFileRepository<T extends BaseEntity>`, which implements the `Repo<T>` interface
  (`save`, `findAll`, `findById`, `delete`) on top of a json file. the repository classes
  only pass their file name and entity class. this avoids duplicated code (dry). the
  entity ids are strings, generated as uuids in `BaseEntity`. the services only know
  `Repo<T>`, so the storage can be swapped without touching them.
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
  `studentId`, ...) like in the uml, which keeps the stored data simple. the uml's
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
| `Department` | `University.departments` |
| `Room` | `University.rooms` |
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
- **a parent with real data can't be deleted.** a university with departments or rooms,
  a department with courses or teachers, and a room with courses are rejected. one click
  shouldn't remove real data, and nothing may keep an id that no longer exists. delete
  bottom-up instead (courses and teachers, then departments and rooms, then the university).
- **`Department` and `Room` have a `universityId`.** the uml doesn't have it, but without it
  nothing says where they belong and `University.departments`/`rooms` can't be filled.

- **why lists and not queries.** we could store only the ids and offer queries like
  `findByStudentId(...)`, which can't go out of sync. we kept the lists because the uml
  specifies them.

`ListSyncTest` covers these rules.

## json storage (projekt 3)

each entity has its own file in `demo/src/main/resources/data/` (`university.json`,
`course.json`, ...), a list of json objects. every file starts with at least 10 records.
the ids in the seed files are readable (`course-1`) and every reference points to a record
that exists.

- **`InFileRepository` keeps the list in memory.** it reads the file once when the app
  starts and writes the whole list back after every `save` and `delete`, so every change
  made in the web interface is in the file immediately. `save` replaces the entry with the
  same id, which is also how an edit works. a missing file starts empty, a broken file
  stops the app, so it can never be overwritten by accident.
- **the path is a source path** (`src/main/resources/data`), not the classpath, so the
  files in the repository change. this is why the app has to be started from `demo`.
- **the relation lists are not stored.** the getters of `University.departments`,
  `Department.courses`, ... are `@JsonIgnore`. a department stores only its own fields and
  `universityId`, every course is stored once, in `course.json`. a stored copy would go
  stale, because the services change the lists without saving the parent.
- **the lists are rebuilt on startup.** a `@PostConstruct` method in each service fills the
  lists of the parents from the stored ids. while the app runs, the services keep them
  up to date as described under "keeping the lists in sync". a seed record that points to a
  missing parent stops the app at startup.

## web interface (projekt 2 and 3)

every entity has the same routes, handled by its own `@Controller`:

| method | route | purpose |
|---|---|---|
| GET | `/<entity>` | list all objects (`index.html`) |
| GET | `/<entity>/new` | show the create form (`form.html`) |
| GET | `/<entity>/{id}` | show the details of one object (`details.html`) |
| GET | `/<entity>/{id}/edit` | show the form filled with the object (`form.html`) |
| POST | `/<entity>` | create or update the object, then redirect to the list |
| POST | `/<entity>/{id}/delete` | delete the object, then redirect to the list |

entities and routes: `/universities`, `/departments`, `/rooms`, `/courses`,
`/students`, `/staff`, `/enrollments`, `/teachingassignments`.

- **edit reuses the create form and `save`.** the form has a hidden `id` field. a new object
  carries a fresh id, an edited one carries its existing id, so `save` replaces the entry
  with that id. the id in the url only chooses which object the form shows.
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
- one template folder per entity, each file with one purpose (list, details or form).

## added properties (task 5)

| class | new property |
|---|---|
| `Course` | `semester` |
| `Department`, `Room` | `universityId` (needed for the list sync) |
| `Student` | `email`, `birthDate` |
| `Teacher` | `email`, `hireDate` |

## known limits

- the data is kept in json files, written on every change. there is no locking, the app is
  meant for one user at a time.
- the tests run against the real json files (see "run").
- invalid input (unknown id, duplicate or malformed e-mail, ...) is rejected by the
  services with an `IllegalArgumentException`, which shows as a plain 500 error page.
  friendly error messages are not part of projekt 2.
