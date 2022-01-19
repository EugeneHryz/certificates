package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.AbstractEntity;
import com.epam.esm.repository.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {

    int create(T entity) throws DaoException;

    Optional<T> findById(int id) throws DaoException;

    List<T> findAll() throws DaoException;

    boolean deleteById(int id) throws DaoException;

    Optional<T> update(T entity) throws DaoException;
}
