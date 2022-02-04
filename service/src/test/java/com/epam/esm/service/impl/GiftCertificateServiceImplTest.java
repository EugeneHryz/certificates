package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GiftCertificateServiceImplTest {

    @Autowired
    private ConversionService conversionService;

    @Mock
    private GiftCertificateDao certificateDao;
    @Mock
    private TagDao tagDao;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCertificateShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        GiftCertificate certificate = new GiftCertificate("Free shopping", "good certificate", 45.0, 20,
                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"));
        certificate.setId(1);
        Tag tag1 = new Tag("vacation");
        tag1.setId(21);
        Tag tag2 = new Tag("next purchase is free");
        tag2.setId(22);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        Mockito.when(certificateDao.findById(1)).thenReturn(Optional.of(certificate));
        Mockito.when(tagDao.findTagsForCertificate(certificate)).thenReturn(tags);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        GiftCertificateDto expected = conversionService.convert(certificate, GiftCertificateDto.class);
        expected.setTags(tags.stream().map(t -> conversionService.convert(t, TagDto.class)).collect(Collectors.toList()));
        GiftCertificateDto actual = certificateService.getCertificate(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getCertificateShouldFailToFindIt() throws DaoException {
        Mockito.when(certificateDao.findById(1)).thenThrow(DaoException.class);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> certificateService.getCertificate(1));
    }

    @Test
    public void getCertificateShouldThrowNoSuchElementException() throws DaoException {
        Mockito.when(certificateDao.findById(11)).thenReturn(Optional.empty());
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        Assertions.assertThrows(NoSuchElementException.class, () -> certificateService.getCertificate(11));
    }

    @Test
    public void createCertificateShouldBeCorrect() throws DaoException, ServiceException, InvalidRequestDataException {
        Tag tag = new Tag("vacation");
        tag.setId(21);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        List<TagDto> tagsDto = tags.stream().map(t -> conversionService.convert(t, TagDto.class)).collect(Collectors.toList());
        GiftCertificateDto certificate = new GiftCertificateDto("Free shopping", "good certificate", 45.0, 20,
                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"), tagsDto);
        certificate.setId(5);

        Mockito.when(certificateDao.create(any())).thenReturn(5);
        Mockito.when(tagDao.findByName(tag.getName())).thenReturn(Optional.of(tag));
        Mockito.when(certificateDao.findById(5)).thenReturn(Optional.of(new GiftCertificate()));

        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);
        GiftCertificateDto actual = certificateService.createCertificate(certificate);

        Mockito.verify(certificateDao, times(1)).createCertificateTagMapping(5, 21);
        Assertions.assertEquals(certificate, actual);
    }

    @Test
    public void createCertificateThatAlreadyExists() throws DaoException, ServiceException, InvalidRequestDataException {
        GiftCertificateDto certificateDto = new GiftCertificateDto("Free shopping", "good certificate", 45.0, 20,
                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"), null);
        certificateDto.setId(5);
        GiftCertificate certificate = conversionService.convert(certificateDto, GiftCertificate.class);

        Mockito.when(certificateDao.findByName(certificateDto.getName())).thenReturn(Optional.of(certificate));
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> certificateService.createCertificate(certificateDto));
    }

    @Test
    public void createCertificateWithInvalidParams() throws DaoException {
        GiftCertificateDto certificateDto = new GiftCertificateDto("Free shopping", "good certificate", 45.0, 20,
                LocalDateTime.parse("2100-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"), null);

        Mockito.when(certificateDao.findByName(certificateDto.getName())).thenReturn(Optional.empty());
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        Assertions.assertThrows(InvalidRequestDataException.class, () -> certificateService.createCertificate(certificateDto));
    }

    @Test
    public void deleteCertificateShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        Mockito.when(certificateDao.deleteById(anyInt())).thenReturn(true);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        certificateService.deleteCertificate(4);
        Mockito.verify(certificateDao, times(1)).deleteById(4);
    }

    @Test
    public void deleteCertificateShouldThrowException() throws DaoException {
        Mockito.when(certificateDao.deleteById(anyInt())).thenReturn(false);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        Assertions.assertThrows(NoSuchElementException.class, () -> certificateService.deleteCertificate(4));
    }

    @Test
    public void getCertificatesShouldBeCorrect() throws DaoException, ServiceException, InvalidRequestDataException {
        GiftCertificate cert1 = new GiftCertificate("Free shopping", "good certificate", 45.0, 20,
                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"));
        cert1.setId(1);
        GiftCertificate cert2 = new GiftCertificate("New name!!!", "best certificate ever", 50.0,
                30, LocalDateTime.parse("2018-06-01T08:46:30"), LocalDateTime.parse("2018-06-01T08:46:30"));
        cert2.setId(2);

        List<GiftCertificate> certificates = new ArrayList<>();
        certificates.add(cert1);
        certificates.add(cert2);

        Tag tag1 = new Tag("vacation");
        tag1.setId(21);
        Tag tag2 = new Tag("next purchase is free");
        tag2.setId(22);
        List<Tag> tags1 = Arrays.asList(tag1);
        List<Tag> tags2 = Arrays.asList(tag2);

        CertificateSearchParameter searchParameter =
                new CertificateSearchParameter("", new String[] {"fun"}, "date", "desc");
        Mockito.when(certificateDao.findCertificates(searchParameter, 2, 0)).thenReturn(certificates);
        Mockito.when(tagDao.findTagsForCertificate(cert1)).thenReturn(tags1);
        Mockito.when(tagDao.findTagsForCertificate(cert2)).thenReturn(tags2);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        List<GiftCertificateDto> expected = certificates.stream()
                .map(c -> conversionService.convert(c, GiftCertificateDto.class)).collect(Collectors.toList());
        expected.get(0).setTags(tags1.stream().map(t -> conversionService.convert(t, TagDto.class)).collect(Collectors.toList()));
        expected.get(1).setTags(tags2.stream().map(t -> conversionService.convert(t, TagDto.class)).collect(Collectors.toList()));

        List<GiftCertificateDto> actual = certificateService.getCertificates(searchParameter,0, 2);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getCertificatesShouldFailToFindCertificates() throws DaoException {
        CertificateSearchParameter searchParameter =
                new CertificateSearchParameter("", new String[] {"fun"}, "date", "desc");

        Mockito.when(certificateDao.findCertificates(searchParameter, 2, 0)).thenThrow(DaoException.class);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> certificateService.getCertificates(searchParameter, 0, 2));
    }

    @Test
    public void getCertificatesShouldFailToFindTagsForCertificates() throws DaoException {
        GiftCertificate cert = new GiftCertificate("Free shopping", "good certificate", 45.0, 20,
                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"));
        cert.setId(1);
        List<GiftCertificate> certificates = new ArrayList<>();
        certificates.add(cert);
        CertificateSearchParameter searchParameter =
                new CertificateSearchParameter("", new String[] {"fun"}, "date", "desc");

        Mockito.when(certificateDao.findCertificates(searchParameter, 2, 0)).thenReturn(certificates);
        Mockito.when(tagDao.findTagsForCertificate(cert)).thenThrow(DaoException.class);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> certificateService.getCertificates(searchParameter, 0, 2));
    }

    @Test
    public void updateCertificateShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException, InvalidRequestDataException {
        GiftCertificate oldCert = new GiftCertificate("New name!!!", "best certificate ever", 50.0,
                30, LocalDateTime.parse("2018-06-01T08:46:30"), LocalDateTime.parse("2018-06-01T08:46:30"));
        oldCert.setId(7);
        Tag tag1 = new Tag("vacation");
        tag1.setId(21);
        Tag tag2 = new Tag("next purchase is free");
        tag2.setId(22);

        List<Tag> existingTags = new ArrayList<>();
        existingTags.add(tag1);
        existingTags.add(tag2);

        List<TagDto> suppliedTags = new ArrayList<>();
        TagDto tagDto1 = new TagDto("next purchase is free");
        tagDto1.setId(23);
        TagDto tagDto2 = new TagDto("massage");
        tagDto2.setId(24);
        suppliedTags.add(tagDto1);
        suppliedTags.add(tagDto2);

        Mockito.when(certificateDao.findById(7)).thenReturn(Optional.of(oldCert));
        Mockito.when(tagDao.findTagsForCertificate(oldCert)).thenReturn(existingTags);
        Mockito.when(tagDao.findByName("next purchase is free")).thenReturn(Optional.of(tag2));
        Mockito.when(tagDao.findByName("massage")).thenReturn(Optional.empty());
        Mockito.when(tagDao.create(new Tag(suppliedTags.get(1).getName()))).thenReturn(24);

        oldCert.setName("entirely new name!");
        oldCert.setPrice(100.0);
        oldCert.setLastUpdated(LocalDateTime.now());

        Mockito.when(certificateDao.update(any())).thenReturn(Optional.of(oldCert));
        Tag createdTag = new Tag("massage");
        createdTag.setId(24);
        existingTags.add(createdTag);
        Mockito.when(tagDao.findTagsForCertificate(oldCert)).thenReturn(existingTags);

        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        GiftCertificateDto expected = conversionService.convert(oldCert, GiftCertificateDto.class);
        expected.setTags(existingTags.stream().map(t -> conversionService.convert(t, TagDto.class))
                .collect(Collectors.toList()));

        GiftCertificateDto paramsToUpdate = new GiftCertificateDto("entirely new name!",
                null, 100.0, 0, null, null, suppliedTags);
        paramsToUpdate.setId(7);
        GiftCertificateDto actual = certificateService.updateCertificate(paramsToUpdate);
        boolean result = expected.getId() == actual.getId() && expected.getName().equals(actual.getName()) &&
                expected.getDescription().equals(actual.getDescription()) &&
                Double.compare(expected.getPrice(), actual.getPrice()) == 0 &&
                expected.getDuration() == actual.getDuration() && expected.getTags().equals(actual.getTags());

        Assertions.assertTrue(result);
    }

    @Test
    public void getCertificateCountShouldBeCorrect() throws DaoException, ServiceException, InvalidRequestDataException {
        CertificateSearchParameter searchParameter =
                new CertificateSearchParameter("", new String[] {"fun"}, "date", "desc");

        Mockito.when(certificateDao.getCount(searchParameter)).thenReturn(16L);
        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);

        long expected = 16;
        long actual = certificateService.getCertificateCount(searchParameter);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getCertificateCountShouldThrowServiceException() throws DaoException {
        CertificateSearchParameter searchParameter =
                new CertificateSearchParameter("", new String[] {"fun"}, "date", "desc");
        Mockito.when(certificateDao.getCount(searchParameter)).thenThrow(DaoException.class);

        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao, conversionService);
        Assertions.assertThrows(ServiceException.class, () -> certificateService.getCertificateCount(searchParameter));
    }
}
