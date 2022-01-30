//package com.epam.esm.service.impl;
//
//import com.epam.esm.repository.dao.GiftCertificateDao;
//import com.epam.esm.repository.dto.SearchOption;
//import com.epam.esm.repository.dao.TagDao;
//import com.epam.esm.repository.entity.GiftCertificate;
//import com.epam.esm.repository.entity.Tag;
//import com.epam.esm.repository.exception.DaoException;
//import com.epam.esm.service.GiftCertificateService;
//import com.epam.esm.service.dto.GiftCertificateDto;
//import com.epam.esm.service.dto.TagDto;
//import com.epam.esm.service.exception.impl.NoSuchElementException;
//import com.epam.esm.service.exception.impl.ServiceException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.times;
//
//public class GiftCertificateServiceImplTest {
//
//    @Mock
//    private GiftCertificateDao certificateDao;
//
//    @Mock
//    private TagDao tagDao;
//
//    @BeforeEach
//    public void setupMocks() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void getCertificateShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
//        GiftCertificate certificate = new GiftCertificate("Free shopping", "good certificate", 45.0, 20,
//                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"));
//        certificate.setId(1);
//        Tag tag1 = new Tag("vacation");
//        tag1.setId(21);
//        Tag tag2 = new Tag("next purchase is free");
//        tag2.setId(22);
//
//        List<Tag> tags = new ArrayList<>();
//        tags.add(tag1);
//        tags.add(tag2);
//
//        Mockito.when(certificateDao.findById(1)).thenReturn(Optional.of(certificate));
//        Mockito.when(tagDao.findTagsForCertificate(certificate)).thenReturn(tags);
//        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao);
//
//        GiftCertificateDto expected = new GiftCertificateDto(certificate);
//        expected.setTags(tags.stream().map(TagDto::new).collect(Collectors.toList()));
//        GiftCertificateDto actual = certificateService.getCertificate(1);
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void createCertificateShouldBeCorrect() throws DaoException, ServiceException {
//        Tag tag = new Tag("vacation");
//        tag.setId(21);
//
//        List<Tag> tags = new ArrayList<>();
//        tags.add(tag);
//        List<TagDto> tagsDto = tags.stream().map(TagDto::new).collect(Collectors.toList());
//        GiftCertificateDto certificate = new GiftCertificateDto(5, "Free shopping", "good certificate", 45.0, 20,
//                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"), tagsDto);
//
//        Mockito.when(certificateDao.create(any())).thenReturn(5);
//        Mockito.when(tagDao.findByName(tag.getName())).thenReturn(Optional.of(tag));
//        Mockito.when(certificateDao.findById(5)).thenReturn(Optional.of(new GiftCertificate()));
//
//        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao);
//        GiftCertificateDto actual = certificateService.createCertificate(certificate);
//
//        Mockito.verify(certificateDao, times(1)).createCertificateTagMapping(5, 21);
//        Assertions.assertEquals(certificate, actual);
//    }
//
//    @Test
//    public void deleteCertificateShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
//        Mockito.when(certificateDao.deleteById(anyInt())).thenReturn(true);
//        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao);
//
//        certificateService.deleteCertificate(4);
//        Mockito.verify(certificateDao, times(1)).deleteById(4);
//    }
//
//    @Test
//    public void deleteCertificateShouldThrowException() throws DaoException {
//        Mockito.when(certificateDao.deleteById(anyInt())).thenReturn(false);
//        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao);
//
//        Assertions.assertThrows(NoSuchElementException.class, () -> certificateService.deleteCertificate(4));
//    }
//
//    @Test
//    public void getCertificatesShouldBeCorrect() throws DaoException, ServiceException {
//        GiftCertificate cert1 = new GiftCertificate("Free shopping", "good certificate", 45.0, 20,
//                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"));
//        cert1.setId(1);
//        GiftCertificate cert2 = new GiftCertificate("New name!!!", "best certificate ever", 50.0,
//                30, LocalDateTime.parse("2018-06-01T08:46:30"), LocalDateTime.parse("2018-06-01T08:46:30"));
//        cert2.setId(2);
//
//        List<GiftCertificate> certificates = new ArrayList<>();
//        certificates.add(cert1);
//        certificates.add(cert2);
//
//        Tag tag1 = new Tag("vacation");
//        tag1.setId(21);
//        Tag tag2 = new Tag("next purchase is free");
//        tag2.setId(22);
//
//        List<Tag> tags1 = Arrays.asList(tag1);
//        List<Tag> tags2 = Arrays.asList(tag2);
//
//        Mockito.when(certificateDao.findCertificates(any())).thenReturn(certificates);
//        Mockito.when(tagDao.findTagsForCertificate(cert1)).thenReturn(tags1);
//        Mockito.when(tagDao.findTagsForCertificate(cert2)).thenReturn(tags2);
//        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao);
//
//        List<GiftCertificateDto> expected = certificates.stream().map(GiftCertificateDto::new)
//                .collect(Collectors.toList());
//        expected.get(0).setTags(tags1.stream().map(TagDto::new).collect(Collectors.toList()));
//        expected.get(1).setTags(tags2.stream().map(TagDto::new).collect(Collectors.toList()));
//
//        List<GiftCertificateDto> actual = certificateService.getCertificates(new SearchOption("", "fun",
//                "date", "desc"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void updateCertificateShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
//        GiftCertificate oldCert = new GiftCertificate("New name!!!", "best certificate ever", 50.0,
//                30, LocalDateTime.parse("2018-06-01T08:46:30"), LocalDateTime.parse("2018-06-01T08:46:30"));
//        oldCert.setId(7);
//
//        Tag tag1 = new Tag("vacation");
//        tag1.setId(21);
//        Tag tag2 = new Tag("next purchase is free");
//        tag2.setId(22);
//
//        List<Tag> existingTags = new ArrayList<>();
//        existingTags.add(tag1);
//        existingTags.add(tag2);
//
//        List<TagDto> suppliedTags = new ArrayList<>();
//        suppliedTags.add(new TagDto(22, "next purchase is free"));
//        suppliedTags.add(new TagDto(24, "massage"));
//
//        Mockito.when(certificateDao.findById(7)).thenReturn(Optional.of(oldCert));
//        Mockito.when(tagDao.findTagsForCertificate(oldCert)).thenReturn(existingTags);
//        Mockito.when(tagDao.findByName("next purchase is free")).thenReturn(Optional.of(tag2));
//        Mockito.when(tagDao.findByName("massage")).thenReturn(Optional.empty());
//        Mockito.when(tagDao.create(new Tag(suppliedTags.get(1).getName()))).thenReturn(24);
//
//        oldCert.setName("entirely new name!");
//        oldCert.setPrice(100.0);
//        oldCert.setLastUpdated(LocalDateTime.now());
//
//        Mockito.when(certificateDao.update(any())).thenReturn(Optional.of(oldCert));
//        Tag createdTag = new Tag("massage");
//        createdTag.setId(24);
//        existingTags.add(createdTag);
//        Mockito.when(tagDao.findTagsForCertificate(oldCert)).thenReturn(existingTags);
//
//        GiftCertificateService certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao);
//
//        GiftCertificateDto expected = new GiftCertificateDto(oldCert);
//        expected.setTags(existingTags.stream().map(TagDto::new).collect(Collectors.toList()));
//
//        GiftCertificateDto paramsToUpdate = new GiftCertificateDto(7, "entirely new name!",
//                null, 100.0, 0, null, null, suppliedTags);
//        GiftCertificateDto actual = certificateService.updateCertificate(paramsToUpdate);
//        boolean result = expected.getId() == actual.getId() && expected.getName().equals(actual.getName()) &&
//                expected.getDescription().equals(actual.getDescription()) &&
//                Double.compare(expected.getPrice(), actual.getPrice()) == 0 &&
//                expected.getDuration() == actual.getDuration() && expected.getTags().equals(actual.getTags());
//
//        Assertions.assertTrue(result);
//    }
//}
