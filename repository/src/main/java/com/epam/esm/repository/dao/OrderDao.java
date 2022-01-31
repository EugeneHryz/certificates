package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends BaseDao<Order> {

    List<Order> getUserOrders(int userId, int limit, int offset) throws DaoException;

}
