package org.example.finaloop1;

import java.util.List;

public interface StudentDAOInterface extends DAOInterface<Student> {
        List<Student> findUnenrolledStudents();
}
