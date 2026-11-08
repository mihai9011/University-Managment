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
  responsibility (single responsibility) and only talks to repositories. controllers
  are one per entity too, so they can grow independently in later phases.
- **validation lives in the services.** services check that referenced ids exist
  (e.g. `CourseService` checks the department and room, `EnrollmentService` checks
  the student and course), reject null/missing required fields, and throw
  `IllegalArgumentException` for not-found cases. student and staff e-mails must be
  unique and the student's birth date must be in the past.
- **inheritance for staff.** `Staff` is abstract, with `Teacher` and `Assistant` as
  subclasses (liskov: both can be used wherever a `Staff` is expected). `@JsonTypeInfo`
  on `Staff` lets json choose the subclass through a `type` field.
- **relations by id.** entities reference each other by id (`departmentId`, `roomId`,
  `studentId`, ...) like in the uml, which keeps the in-memory data simple.
- **dates** use `LocalDate`, not `String`. enums (`Grade`, `ManagingType`,
  `AssistantRole`) are used instead of free-text status fields.

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
- deleting an entity does not check whether others still reference it.
