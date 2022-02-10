package com.epam.esm.repository.dao;

import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao extends BaseDao<GiftCertificate> {

    List<GiftCertificate> findCertificates(CertificateSearchParameter options, int limit, int offset) throws DaoException;

    Optional<GiftCertificate> findByName(String name) throws DaoException;

    long getCount(CertificateSearchParameter options) throws DaoException;
}
