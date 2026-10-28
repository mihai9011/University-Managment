# University Management System (Thema 8)

Spring Boot 4 / Java 17, alle Daten In-Memory (keine Datenbank). Start: `./mvnw spring-boot:run`, Test: `http://localhost:8080/hello`.

## Struktur (`com.example.UniversityMan`)

| Paket | Inhalt |
|---|---|
| `model` | UML-Klassen: University, Department, Course, Room, Student, Enrollment, TeachingAssignment, `Staff` (abstrakt) → Teacher, Assistant; Enums Grade, ManagingType, AssistantRole |
| `repository` | `GenericRepository<T>` (Interface: save, findAll, findById, delete), `InMemoryRepository<T>` (Map), je Entitaet ein eigenes Repository |
| `service` | `CrudService<T>` (gemeinsame Logik), je Entitaet ein Service mit eigener Geschaeftslogik |
| `controller` | `HelloController`, `CrudController<T>` (GET/POST/DELETE), je Entitaet ein REST-Controller |

## Entscheidungen und Begruendung

- **Ein Repository und ein Service pro Entitaet** (Single Responsibility). `Teacher` und `Assistant` teilen sich bewusst `StaffRepository`/`StaffService`, weil sie dieselbe Hierarchie (`Staff`) bilden und Lehrzuweisungen beide referenzieren.
- **Dependency Inversion:** Services haengen nur vom Interface `GenericRepository<T>` ab, nicht von der In-Memory-Implementierung. Fuer die spaetere Datenbank-Phase muss nur die Repository-Implementierung ausgetauscht werden (Open/Closed).
- **DRY:** `InMemoryRepository`, `CrudService` und `CrudController` enthalten den gemeinsamen CRUD-Code; die konkreten Klassen sind nur noch duenn bzw. enthalten nur ihre Spezialregeln.
- **Ein Controller pro Entitaet** (statt eines grossen), damit die URLs (`/students`, `/courses`, ...) den Entitaeten entsprechen.
- **Beziehungen wie im UML:** Verweise ueber Ids (`courseId`, `studentId`, ...) plus Listen (`Course.enrollments`, ...). Die Services halten beide Seiten synchron und pruefen, ob referenzierte Eintraege existieren.
- **Geschaeftsregeln:** keine doppelte Einschreibung, Einschreibung nur solange der Raum des Kurses Platz hat, Loeschen verweigert (HTTP 409), solange noch Referenzen bestehen. Fehler: 404 nicht gefunden, 400 ungueltige Eingabe, 409 Regelverstoss.
- **Enums statt Strings** fuer Grade (`A/B/C/D/F/NA`), Managing (`LAB/SEMINAR/COURSE`) und Assistant-Rolle (`LAB/TA/GRADER`); Datumswerte als `LocalDate`.

## Erweiterung der Modelle (Aufgabe 5)

- `Student`: `email`, `birthDate`
- `Teacher`: `email`, `hireDate`
- `Course`: `semester`

## Beispiel-Endpunkte

`GET/POST /universities`, `/departments`, `/courses`, `/rooms`, `/students`, `/staff`, `/enrollments`, `/assignments`; `GET/DELETE /<pfad>/{id}`;
`POST /universities/{id}/departments/{departmentId}`; `PUT /enrollments/{id}/grade?grade=A`.
`POST /staff` braucht `"type": "teacher"` oder `"assistant"` im JSON.
