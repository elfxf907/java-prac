CREATE TABLE companies (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL UNIQUE,
                           address VARCHAR(500)
);

CREATE TABLE teachers (
                          id BIGSERIAL PRIMARY KEY,
                          full_name VARCHAR(255) NOT NULL,
                          company_id BIGINT REFERENCES companies(id) ON DELETE SET NULL
);

CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          full_name VARCHAR(255) NOT NULL
);

CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         company_id BIGINT REFERENCES companies(id) ON DELETE SET NULL,
                         duration_type VARCHAR(50) NOT NULL
                             CHECK (duration_type IN ('DAY', 'SEVERAL_DAYS', 'TWO_WEEKS', 'MONTH')),
                         hours_per_day INTEGER NOT NULL CHECK (hours_per_day > 0),
                         description TEXT
);

CREATE TABLE course_teachers (
                                 course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
                                 teacher_id BIGINT NOT NULL REFERENCES teachers(id) ON DELETE CASCADE,
                                 PRIMARY KEY (course_id, teacher_id)
);

CREATE TABLE student_courses (
                                 student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
                                 course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
                                 enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (student_id, course_id)
);

CREATE TABLE lessons (
                         id BIGSERIAL PRIMARY KEY,
                         course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
                         teacher_id BIGINT NOT NULL REFERENCES teachers(id) ON DELETE RESTRICT,
                         start_time TIMESTAMP NOT NULL,
                         end_time TIMESTAMP NOT NULL,
                         CHECK (end_time > start_time)
);

CREATE TABLE lesson_students (
                                 lesson_id BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
                                 student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
                                 PRIMARY KEY (lesson_id, student_id)
);

CREATE INDEX idx_teachers_company_id ON teachers(company_id);
CREATE INDEX idx_courses_company_id ON courses(company_id);
CREATE INDEX idx_course_teachers_teacher_id ON course_teachers(teacher_id);
CREATE INDEX idx_student_courses_course_id ON student_courses(course_id);
CREATE INDEX idx_lessons_course_id ON lessons(course_id);
CREATE INDEX idx_lessons_teacher_id ON lessons(teacher_id);
CREATE INDEX idx_lessons_start_time ON lessons(start_time);
CREATE INDEX idx_lessons_end_time ON lessons(end_time);
CREATE INDEX idx_lesson_students_student_id ON lesson_students(student_id);