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
            // fixme:
            generatedId = certificateDao.create(conversionService.convert(gcDto, GiftCertificate.class));
            gcDto.setId(generatedId);

        } catch (DaoException e) {
            throw new ServiceException("Error occurred during certificate creation", e, CERTIFICATE_CODE);
        }
        return gcDto;
    }

    @Override
    public GiftCertificateDto getCertificate(int id) throws NoSuchElementException, ServiceException {
        Optional<GiftCertificate> cert;
        try {
            cert = certificateDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Unable to find certificate (id = " + id + ")", e, CERTIFICATE_CODE);
        }

        GiftCertificateDto gcDto;
        if (cert.isPresent()) {
            gcDto = conversionService.convert(cert.get(), GiftCertificateDto.class);

            List<Tag> tags;
            try {
                tags = tagDao.findTagsForCertificate(cert.get());
            } catch (DaoException e) {
                throw new ServiceException("Unable to find tags for certificate with id = " +
                        cert.get().getId(), e, CERTIFICATE_CODE);
            }
            List<TagDto> tagsDto = tags.stream().map(t -> conversionService.convert(t, TagDto.class))
                    .collect(Collectors.toList());
            gcDto.setTags(tagsDto);
        } else {
            throw new NoSuchElementException("Unable to get certificate (id = " + id + ")", CERTIFICATE_CODE);
        }
        return gcDto;
    }

    @Override
    public List<GiftCertificateDto> getCertificates(CertificateSearchParameter options, int pageNumber, int pageSize) throws ServiceException {
        List<GiftCertificate> certificates;
        try {
            certificates = certificateDao.findCertificates(options, pageSize, pageNumber * pageSize);
        } catch (DaoException e) {
            throw new ServiceException("Unable to get certificates", e, CERTIFICATE_CODE);
        }

        List<GiftCertificateDto> result = new ArrayList<>();
        for (GiftCertificate cert : certificates) {
            GiftCertificateDto certDto = conversionService.convert(cert, GiftCertificateDto.class);

            List<Tag> tags = cert.getTags();
            List<TagDto> tagsDto = tags.stream().map(t -> conversionService.convert(t, TagDto.class))
                    .collect(Collectors.toList());
            certDto.setTags(tagsDto);
            result.add(certDto);
        }
        return result;
    }

    @Override
    public void deleteCertificate(int id) throws ServiceException, NoSuchElementException {
        try {
            if (!certificateDao.deleteById(id)) {
                throw new NoSuchElementException("Unable to delete certificate (id = " + id + ")", CERTIFICATE_CODE);
            }
        } catch (DaoException e) {
            throw new ServiceException("Unable to delete certificate (id = " + id + ")", e, CERTIFICATE_CODE);
        }
    }

    @Transactional
    @Override
    // fixme:
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
            List<Tag> existingTags = tagDao.findTagsForCertificate(newCert);

            if (certDto.getTags() != null) {
                for (TagDto newTag : certDto.getTags()) {
                    Optional<Tag> tag = tagDao.findByName(newTag.getName());

                    // if supplied tag already exists
                    if (tag.isPresent()) {

                        // if the certificate is not associated with that tag
                        if (existingTags.stream().noneMatch(t -> t.equals(tag.get()))) {
                            // create mapping
//                            certificateDao.createCertificateTagMapping(newCert.getId(), tag.get().getId());
                        }
                    } else {    // if supplied tag doesn't exist
                        // create new tag
                        int generatedId = tagDao.create(new Tag(newTag.getName()));
                        if (generatedId > 0) {

                            // create mapping for that tag
//                            certificateDao.createCertificateTagMapping(newCert.getId(), generatedId);
                        }
                    }
                }
            }
            // setting only those fields, that were passed in request (createDate cannot be changed)
            if (certDto.getName() != null) {
                newCert.setName(certDto.getName());
            }
            if (certDto.getDescription() != null) {
                newCert.setDescription(certDto.getDescription());
            }
            if (certDto.getPrice() != 0.0) {
                newCert.setPrice(certDto.getPrice());
            }
            if (certDto.getDuration() != 0) {
                newCert.setDuration(certDto.getDuration());
            }
            // lastUpdated date is automatically updated
            newCert.setLastUpdated(LocalDateTime.now());
            Optional<GiftCertificate> updatedCert = certificateDao.update(newCert);
            if (updatedCert.isPresent()) {
                return constructCertificateDto(updatedCert.get());
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

    private GiftCertificateDto constructCertificateDto(GiftCertificate certificate) throws DaoException {
        List<Tag> updatedTags = tagDao.findTagsForCertificate(certificate);
        List<TagDto> updatedTagsDto = updatedTags.stream().map(t -> conversionService.convert(t, TagDto.class))
                .collect(Collectors.toList());

        GiftCertificateDto result = conversionService.convert(certificate, GiftCertificateDto.class);
        result.setTags(updatedTagsDto);
        return result;
    }
}
