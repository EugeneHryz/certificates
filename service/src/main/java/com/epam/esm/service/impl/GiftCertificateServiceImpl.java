package com.epam.esm.service.impl;

import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    public static final int CERTIFICATE_CODE = 1;

    private GiftCertificateDao certificateDao;
    private TagDao tagDao;

    private ConversionService conversionService;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, TagDao tagDao,
                                      ConversionService conversionService) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.conversionService = conversionService;
    }

    @Transactional
    @Override
    public GiftCertificateDto createCertificate(GiftCertificateDto gcDto) throws ServiceException, InvalidRequestDataException {
        int generatedId;
        try {
            Optional<GiftCertificate> existingCert = certificateDao.findByName(gcDto.getName());
            if (existingCert.isPresent()) {
                throw new ServiceException("Certificate with name '" + gcDto.getName() + "' already exists", CERTIFICATE_CODE);
            }
            LocalDateTime currentDate = LocalDateTime.now();
            if ((gcDto.getCreated() != null && gcDto.getCreated().isAfter(currentDate))
                    || gcDto.getCreated() == null || gcDto.getLastUpdated() == null) {

                throw new InvalidRequestDataException("Invalid date parameters specified", CERTIFICATE_CODE);
            }
        } catch (DaoException e) {
            throw new ServiceException("Unable to check certificate uniqueness", e, CERTIFICATE_CODE);
        }

        if (gcDto.getTags() != null && !gcDto.getTags().isEmpty()) {
            for (TagDto tag : gcDto.getTags()) {
                int id;
                try {
                    Optional<Tag> existingTag = tagDao.findByName(tag.getName());
                    if (existingTag.isPresent()) {
                        id = existingTag.get().getId();
                    } else {
                        id = tagDao.create(conversionService.convert(tag, Tag.class));
                    }
                } catch (DaoException e) {
                    throw new ServiceException("Unable to find or create tag " + tag.getName() +
                            " for certificate", e, CERTIFICATE_CODE);
                }
                tag.setId(id);
            }
        }
        try {
            generatedId = certificateDao.create(conversionService.convert(gcDto, GiftCertificate.class));
            gcDto.setId(generatedId);

        } catch (DaoException e) {
            throw new ServiceException("Error occurred during certificate creation", e, CERTIFICATE_CODE);
        }
        return gcDto;
    }

    @Override
    public GiftCertificateDto getCertificate(int id) throws NoSuchElementException, ServiceException {
        try {
            Optional<GiftCertificate> cert = certificateDao.findById(id);
            if (cert.isPresent()) {
                return conversionService.convert(cert.get(), GiftCertificateDto.class);
            } else {
                throw new NoSuchElementException("Unable to get certificate (id = " + id + ")", CERTIFICATE_CODE);
            }
        } catch (DaoException e) {
            throw new ServiceException("Unable to find certificate (id = " + id + ")", e, CERTIFICATE_CODE);
        }
    }

    @Override
    public List<GiftCertificateDto> findCertificates(CertificateSearchParameter options, int pageNumber, int pageSize) throws ServiceException {
        try {
            List<GiftCertificate> certificates = certificateDao.findCertificates(options, pageSize, pageNumber * pageSize);

            return certificates.stream().map(c -> conversionService.convert(c, GiftCertificateDto.class))
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Unable to get certificates", e, CERTIFICATE_CODE);
        }
    }

    @Override
    public void deleteCertificate(int id) throws ServiceException, NoSuchElementException {
        try {
            if (!certificateDao.deleteById(id)) {
                throw new NoSuchElementException("Unable to delete certificate (id = " + id + ")", CERTIFICATE_CODE);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("Unable to delete certificate (id = " + id + ")", e, CERTIFICATE_CODE);
        }
    }

    @Transactional
    @Override
    public GiftCertificateDto updateCertificate(GiftCertificateDto certDto) throws ServiceException, NoSuchElementException, InvalidRequestDataException {
        Optional<GiftCertificate> oldCert;
        try {
            oldCert = certificateDao.findById(certDto.getId());
            // check if certificate with given id exists
            if (!oldCert.isPresent()) {
                throw new NoSuchElementException("Unable to update certificate (id = "
                        + certDto.getId() + ")", CERTIFICATE_CODE);
            }
            GiftCertificate newCert = oldCert.get();

            // get all tags associated with that certificate
            List<Tag> existingTags = oldCert.get().getTags();
            if (certDto.getTags() != null) {
                for (TagDto newTag : certDto.getTags()) {
                    Optional<Tag> tag = tagDao.findByName(newTag.getName());

                    // if supplied tag already exists
                    if (tag.isPresent()) {
                        if (existingTags.stream().noneMatch(t -> t.equals(tag.get()))) {
                            existingTags.add(tag.get());
                        }
                    } else {    // if supplied tag doesn't exist
                        // create new tag
                        Tag createdTag = new Tag(newTag.getName());
                        int generatedId = tagDao.create(createdTag);
                        createdTag.setId(generatedId);
                        if (generatedId > 0) {
                            existingTags.add(createdTag);
                        }
                    }
                }
                newCert.setTags(existingTags);
            }
            // setting only those fields, that were passed in request (createDate cannot be changed)
            if (certDto.getName() != null) {
                newCert.setName(certDto.getName());
            }
            if (certDto.getDescription() != null) {
                newCert.setDescription(certDto.getDescription());
            }
            if (certDto.getPrice() != null) {
                newCert.setPrice(certDto.getPrice());
            }
            if (certDto.getDuration() != 0) {
                newCert.setDuration(certDto.getDuration());
            }
            // lastUpdated date is automatically updated
            newCert.setLastUpdated(LocalDateTime.now());
            Optional<GiftCertificate> updatedCert = certificateDao.update(newCert);
            if (updatedCert.isPresent()) {
                return conversionService.convert(updatedCert.get(), GiftCertificateDto.class);
            } else {
                throw new ServiceException("Something went wrong while updating certificate (id = "
                        + newCert.getId() + ")", CERTIFICATE_CODE);
            }
        } catch (DaoException e) {
            throw new ServiceException("Error while updating certificate with id = "
                    + certDto.getId(), e, CERTIFICATE_CODE);
        }
    }

    @Override
    public long getCertificateCount(CertificateSearchParameter options) throws ServiceException {
        try {
            return certificateDao.getCount(options);
        } catch (DaoException e) {
            throw new ServiceException("Unable to count certificates", e, CERTIFICATE_CODE);
        }
    }

}
