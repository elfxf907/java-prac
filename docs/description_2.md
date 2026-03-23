Цель этапа: реализовать библиотеку доступа к данным на основе Hibernate для БД учебного центра, включая классы хранимых объектов, DAO-слой с типовыми запросами и модульные тесты (TestNG) для всех DAO-методов с нетривиальной логикой.

Классы хранимых объектов (Entity)
Реализованы классы, соответствующие таблицам БД: Company, Teacher, Student, Course, Lesson, а также классы для таблиц связей CourseTeacher, StudentCourse, LessonStudent и составные ключи CourseTeacherId, StudentCourseId, LessonStudentId. Отображение таблиц на объекты выполнено через JPA-аннотации (@Entity, @Table, @ManyToOne, @OneToMany, @EmbeddedId, @MapsId). Для поля courses.duration_type используется перечисление CourseDurationType.

Конфигурация Hibernate
Используется конфигурация в файле src/main/resources/hibernate.cfg.xml с параметрами подключения к PostgreSQL (host/port/db/user/password) и режимом проверки схемы (hibernate.hbm2ddl.auto=validate). Проверка работоспособности подключения выполнялась через smoke-test (создание SessionFactory и выполнение простого запроса).

DAO-слой (служебные классы) и типовые запросы
Реализованы DAO-классы (интерфейсы и реализации):

CompanyDao / CompanyDaoImpl: CRUD компаний.

StudentDao / StudentDaoImpl: CRUD обучающихся; история обучения (findCoursesByStudentId); запись обучающегося на курс (enrollStudentToCourse); расписание обучающегося на интервал (findScheduleByStudentIdAndPeriod).

TeacherDao / TeacherDaoImpl: CRUD преподавателей; преподаватели по курсу (findByCourseId); расписание преподавателя на интервал (findScheduleByTeacherIdAndPeriod).

CourseDao / CourseDaoImpl: CRUD курсов; список обучающихся курса (findStudentsByCourseId); список преподавателей курса (findTeachersByCourseId); назначение преподавателя на курс (addTeacherToCourse); добавление занятия в расписание курса (addLesson).

LessonDao / LessonDaoImpl: CRUD занятий; занятия по курсу (findByCourseId); занятия преподавателя на интервал (findByTeacherIdAndPeriod); занятия обучающегося на интервал (findByStudentIdAndPeriod).

Unit-тесты (TestNG)
Для каждого DAO реализован отдельный тестовый класс в src/test/java/java_prac/dao: CompanyDaoTest, StudentDaoTest, TeacherDaoTest, CourseDaoTest, LessonDaoTest. Тесты содержат проверки через Assert.assert…() и покрывают различные ветвления поведения методов:

“найдено/не найдено” (findById),

“успех/ошибка/дублирование” (enrollStudentToCourse, addTeacherToCourse),

“пустой/непустой результат” (расписание и выборки по интервалу),

корректность изменений в БД после save/update/delete.

Сборка и запуск тестов (Ant)
Файл build.xml дополнен задачами сборки проекта и выполнения тестов:

ant compile — сборка проекта (Gradle classes)

ant compile-tests — сборка тестов (Gradle testClasses)

ant test — запуск unit-тестов (Gradle test, TestNG)
Также реализованы команды управления БД через Ant: db-create, db-init, db-clean, db-reset, db-show. Комплексная проверка выполняется командой ant ci (db-reset + test).
Структура реализованной части: src/main/java/java_prac/model — классы хранимых объектов; src/main/java/java_prac/dao — интерфейсы DAO; src/main/java/java_prac/dao/impl — реализации DAO; src/main/java/java_prac/util — утилита инициализации SessionFactory (HibernateUtil); src/main/resources/hibernate.cfg.xml — конфигурация Hibernate; src/test/java/java_prac/dao — TestNG-тесты для DAO-классов; db/create.sql, db/init.sql, db/cleanup.sql — SQL-скрипты создания схемы, заполнения и очистки; build.xml — Ant-задачи для работы с БД и запуска сборки/тестов; build.properties — параметры подключения к PostgreSQL; build.gradle.kts — Gradle-конфигурация зависимостей и настройки запуска TestNG.