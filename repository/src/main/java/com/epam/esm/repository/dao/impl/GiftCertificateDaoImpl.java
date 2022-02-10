package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.repository.entity.GiftCertificate;
import static com.epam.esm.repository.dao.query.DatabaseName.*;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int create(GiftCertificate certificate) throws DaoException {
        entityManager.persist(certificate);

        return certificate.getId();
    }

    @Override
    public Optional<GiftCertificate> findById(int id) throws DaoException {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);

        return Optional.ofNullable(certificate);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> rootCert = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(rootCert).where(criteriaBuilder.equal(rootCert.get(CERTIFICATE_NAME), name));
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);

        return query.getResultStream().findFirst();
    }

    @Override
    public List<GiftCertificate> findCertificates(CertificateSearchParameter options, int limit, int offset) throws DaoException {

    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<GiftCertificate> criteriaDelete = criteriaBuilder.createCriteriaDelete(GiftCertificate.class);

        Root<GiftCertificate> rootCert = criteriaDelete.from(GiftCertificate.class);
        criteriaDelete.where(criteriaBuilder.equal(rootCert.get(CERTIFICATE_ID), id));

        Query query = entityManager.createQuery(criteriaDelete);
        return query.executeUpdate() > 0;
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate cert) throws DaoException {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, cert.getId());
        if (certificate != null) {
            certificate.setName(cert.getName());
            certificate.setDescription(cert.getDescription());
            certificate.setDuration(cert.getDuration());
            certificate.setPrice(cert.getPrice());
            certificate.setCreated(cert.getCreated());
            certificate.setLastUpdated(cert.getLastUpdated());
        }
        return Optional.ofNullable(certificate);
    }

    @Override
    public long getCount(CertificateSearchParameter options) throws DaoException {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> mainQuery = criteriaBuilder.createQuery(Long.class);
//        Subquery<Integer> subquery = mainQuery.subquery(Integer.class);
//
//        Metamodel metamodel = entityManager.getMetamodel();
//        EntityType<GiftCertificate> Certificate_ = metamodel.entity(GiftCertificate.class);
//
//        Root<GiftCertificate> rootCert = subquery.from(GiftCertificate.class);
//        if (options.getTagNames() != null && options.getTagNames().length > 0) {
//            ListJoin<GiftCertificate, Tag> certTags = rootCert.join(Certificate_.getList("tags", Tag.class));
//
//            subquery.where(certTags.get("name").in((Object[]) options.getTagNames()));
//            subquery.groupBy(rootCert.get("id"));
//            subquery.having(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.count(certTags.get("id")),
//                    options.getTagNames().length)));
//        }
//        subquery.select(rootCert.get("id"));
//
//        Root<GiftCertificate> mainRoot = mainQuery.from(GiftCertificate.class);
//        String searchPattern = "%" + options.getSearchParam() + "%";
//        Predicate searchPredicate = criteriaBuilder.or(criteriaBuilder.like(mainRoot.get("name"), searchPattern),
//                criteriaBuilder.like(mainRoot.get("description"), searchPattern));
//        mainQuery.select(criteriaBuilder.count(mainRoot));
//        if (options.getTagNames() != null && options.getTagNames().length > 0) {
//            mainQuery.where(criteriaBuilder.and(mainRoot.get("id").in(subquery), searchPredicate));
//        } else {
//            mainQuery.where(searchPredicate);
//        }
//
//        return entityManager.createQuery(mainQuery).getSingleResult();
    }
}
