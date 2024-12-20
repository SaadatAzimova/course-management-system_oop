package org.example.finaloop1;

import java.util.List;

public interface DAOInterface<T> {

        public int insert(T entity);
        public T read(int id);
        public boolean update(T entity);
        public void delete(T entity);

        void delete(int courseId);

        public List<T> findAll();

        List<Course> findCoursesByInstructorId(int instructorId);

        }
