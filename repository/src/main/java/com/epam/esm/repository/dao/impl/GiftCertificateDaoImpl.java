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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<GiftCertificate> Certificate_ = metamodel.entity(GiftCertificate.class);
        EntityType<Tag> Tag_ = metamodel.entity(Tag.class);

        Root<GiftCertificate> rootCert = criteriaQuery.from(GiftCertificate.class);
//        ListJoin<GiftCertificate, Tag> tags = rootCert.join(Certificate_.getList("tags", Tag.class));
//        // todo: list check
//        Predicate[] tagPredicates =  Arrays.stream(options.getTagNames()).map(tagName ->
//                criteriaBuilder.equal(tags.get(Tag_.getSingularAttribute("name")),
//                        tagName)).toArray(Predicate[]::new);
        System.out.println("searchParam: " + options.getSearchParam());
        Predicate searchPredicate =  criteriaBuilder.like(rootCert.get("name"), options.getSearchParam());

        criteriaQuery.select(rootCert);
        criteriaQuery.where(searchPredicate);
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<GiftCertificate> Certificate_ = metamodel.entity(GiftCertificate.class);
        EntityType<Tag> Tag_ = metamodel.entity(Tag.class);

        Root<GiftCertificate> rootCert = criteriaQuery.from(GiftCertificate.class);
//        ListJoin<GiftCertificate, Tag> tags = rootCert.join(Certificate_.getList("tags", Tag.class));
//        // todo: list check
//        Predicate[] tagPredicates = Arrays.stream(options.getTagNames()).map(tagName ->
//                criteriaBuilder.equal(tags.get(Tag_.getSingularAttribute("name")),
//                tagName)).toArray(Predicate[]::new);

        System.out.println("searchParam: " + options.getSearchParam());
        Predicate searchPredicate =  criteriaBuilder.like(rootCert.get("name"), options.getSearchParam());

        criteriaQuery.select(criteriaBuilder.count(rootCert));
        criteriaQuery.where(searchPredicate);
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
