# university management system (thema 8)

projekt 1: basic structure of a spring boot application, in-memory (no database).
team of two: each of us owns half of the entities.

## run

```bash
cd demo
./mvnw spring-boot:run
```

then open `http://localhost:8080/hello`. it answers `Die Anwendung funktioniert!`.

## structure

```
demo/src/main/java/com/example/UniversityMan/
  model/        classes from the uml diagram
  repository/   in-memory storage
  service/      business logic and validation
  controller/   one rest controller per entity + HelloController
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

## added properties (task 5)

| class | new property |
|---|---|
| `Course` | `semester` |
| `Student` | `email`, `birthDate` |
| `Teacher` | `email`, `hireDate` |

## known limits

- data is lost when the application stops (in-memory by design).
