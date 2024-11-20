package org.example.finaloop1;

import java.util.List;

public interface DAOInterface<T> {


        int insert(T entity);
        void update(T entity);
        void delete(int id);
        List<T> findAll();


}
