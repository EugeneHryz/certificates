package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface TagDao extends BaseDao<Tag> {

    Optional<Tag> findByName(String name) throws DaoException;

    List<Tag> getTags(int limit, int offset) throws DaoException;

    long getCount() throws DaoException;


    Optional<Tag> findMostWidelyUsedTagOfUserWithHighestSpending() throws DaoException;
}
