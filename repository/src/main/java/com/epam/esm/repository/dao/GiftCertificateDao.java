package com.epam.esm.repository.dao;

import com.epam.esm.repository.dto.SearchOption;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao extends BaseDao<GiftCertificate> {

    List<GiftCertificate> findCertificates(SearchOption options, int limit, int offset) throws DaoException;

    boolean createCertificateTagMapping(int certId, int tagId) throws DaoException;

    Optional<GiftCertificate> findByName(String name) throws DaoException;

    long getCount(SearchOption options) throws DaoException;
}
