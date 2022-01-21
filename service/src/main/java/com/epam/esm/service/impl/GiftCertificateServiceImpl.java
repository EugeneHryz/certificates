package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.SearchOption;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.InvalidRequestDataException;
import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.SearchOptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDao certificateDao;
    private TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    @Transactional
    @Override
    public GiftCertificateDto createCertificate(GiftCertificateDto gcDto) throws ServiceException {
        int generatedId;
        try {
            Optional<GiftCertificate> existingCert = certificateDao.findByName(gcDto.getName());
            if (existingCert.isPresent()) {
                throw new ServiceException("Certificate with name '" + gcDto.getName() + "' already exists");
            }
            generatedId = certificateDao.create(new GiftCertificate(gcDto.getName(), gcDto.getDescription(), gcDto.getPrice(),
                    gcDto.getDuration(), gcDto.getCreated(), gcDto.getLastUpdated()));
            gcDto.setId(generatedId);
        } catch (DaoException e) {
            throw new ServiceException("Unable to create certificate", e);
        }

        if (gcDto.getTags() != null && !gcDto.getTags().isEmpty()) {
            for (TagDto tag : gcDto.getTags()) {
                int id;
                try {
                    Optional<Tag> existingTag = tagDao.findByName(tag.getName());
                    if (existingTag.isPresent()) {
                        id = existingTag.get().getId();
                    } else {
                        id = tagDao.create(new Tag(tag.getName()));
                    }
                } catch (DaoException e) {
                    throw new ServiceException("Unable to find or create tag " + tag.getName() +
                            " for certificate", e);
                }
                tag.setId(id);
            }
            try {
                Optional<GiftCertificate> createdCert = certificateDao.findById(generatedId);
                if (createdCert.isPresent()) {
                    for (TagDto tag : gcDto.getTags()) {
                        certificateDao.createCertificateTagMapping(generatedId, tag.getId());
                    }
                }
            } catch (DaoException e) {
                throw new ServiceException("Error occurred during certificate creation", e);
            }
        }
        return gcDto;
    }

    @Override
    public GiftCertificateDto getCertificate(int id) throws NoSuchElementException, ServiceException {
        Optional<GiftCertificate> cert;
        try {
            cert = certificateDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Unable to find certificate (id = " + id + ")", e);
        }

        GiftCertificateDto gcDto;
        if (cert.isPresent()) {
            gcDto = new GiftCertificateDto(cert.get());

            List<Tag> tags;
            try {
                tags = tagDao.findTagsForCertificate(cert.get());
            } catch (DaoException e) {
                throw new ServiceException("Unable to find tags for certificate with id = " + cert.get().getId(), e);
            }
            List<TagDto> tagsDto = tags.stream().map(tag -> new TagDto(tag.getId(), tag.getName()))
                    .collect(Collectors.toList());
            gcDto.setTags(tagsDto);
        } else {
            throw new NoSuchElementException("Unable to get certificate (id = " + id + ")");
        }
        return gcDto;
    }

    @Override
    public List<GiftCertificateDto> getCertificates(String searchParam, String tagName, String sortBy, String sortOrder)
            throws ServiceException, InvalidRequestDataException {

        SearchOptionValidator validator = new SearchOptionValidator();
        if (!validator.validateSortType(sortBy) || !validator.validateSortOrder(sortOrder)) {
            throw new InvalidRequestDataException("Invalid sort parameters");
        }
        SearchOption options = new SearchOption(searchParam, tagName, sortBy, sortOrder);
        List<GiftCertificate> certificates;
        try {
            certificates = certificateDao.findCertificates(options);
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all certificates", e);
        }

        List<GiftCertificateDto> result = new ArrayList<>();
        for (GiftCertificate cert : certificates) {
            GiftCertificateDto certDto = new GiftCertificateDto(cert);

            List<Tag> tags;
            try {
                tags = tagDao.findTagsForCertificate(cert);
            } catch (DaoException e) {
                throw new ServiceException("Unable to find tags for certificate with id = " + cert.getId(), e);
            }
            List<TagDto> tagsDto = tags.stream().map(tag -> new TagDto(tag.getId(), tag.getName()))
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
                throw new NoSuchElementException("Unable to delete certificate (id = " + id + ")");
            }
        } catch (DaoException e) {
            throw new ServiceException("Unable to delete certificate (id = " + id + ")", e);
        }
    }

    @Transactional
    @Override
    public GiftCertificateDto updateCertificate(GiftCertificateDto certDto) throws ServiceException, NoSuchElementException {
        Optional<GiftCertificate> oldCert;
        try {
            oldCert = certificateDao.findById(certDto.getId());
            // check if certificate with given id exists
            if (!oldCert.isPresent()) {
                throw new NoSuchElementException("Unable to update certificate (id = " + certDto.getId() + ")");
            }
            GiftCertificate newCert = oldCert.get();

            // get all tags associated with that certificate
            List<Tag> existingTags = tagDao.findTagsForCertificate(newCert);

            for (TagDto newTag : certDto.getTags()) {
                Optional<Tag> tag = tagDao.findByName(newTag.getName());

                // if supplied tag already exists
                if (tag.isPresent()) {

                    // if the certificate is not associated with that tag
                    if (existingTags.stream().noneMatch(t -> t.equals(tag.get()))) {

                        // create mapping
                        certificateDao.createCertificateTagMapping(newCert.getId(), tag.get().getId());
                    }
                } else {    // if supplied tag doesn't exist
                    // create new tag
                    int generatedId = tagDao.create(new Tag(newTag.getName()));
                    if (generatedId > 0) {

                        // create mapping for that tag
                        certificateDao.createCertificateTagMapping(newCert.getId(), generatedId);
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
            newCert.setLastUpdated(LocalDateTime.now());
            Optional<GiftCertificate> updatedCert = certificateDao.update(newCert);
            if (updatedCert.isPresent()) {

                List<Tag> updatedTags = tagDao.findTagsForCertificate(updatedCert.get());
                List<TagDto> updatedTagsDto = updatedTags.stream().map(tag -> new TagDto(tag.getId(), tag.getName()))
                        .collect(Collectors.toList());

                GiftCertificateDto result = new GiftCertificateDto(updatedCert.get());
                result.setTags(updatedTagsDto);
                return result;
            } else {
                throw new ServiceException("Something went wrong while updating certificate (id = " + newCert.getId() + ")");
            }
        } catch (DaoException e) {
            throw new ServiceException("Error while updating certificate with id = " + certDto.getId(), e);
        }
    }
}
