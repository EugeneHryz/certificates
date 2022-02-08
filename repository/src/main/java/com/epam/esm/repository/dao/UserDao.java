package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;

import java.util.List;

public interface UserDao extends BaseDao<User> {

    List<User> getUsers(int limit, int offset) throws DaoException;

    long getCount() throws DaoException;

}
