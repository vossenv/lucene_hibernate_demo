package com.dm.teamquery;


import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.InvalidEntityIdException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestCustomRepository {

    @Inject
    ChallengeRepository cr;

    @Test
    public void TestUpdate() throws Exception {

        Challenge c = cr.findAll().iterator().next();
        c.setQuestion("A new question");
        cr.saveEntity(c);
        Challenge b = cr.findEntityById(c.getChallengeId());
        Assert.assertEquals(b, c);

    }

    @Test
    public void TestFind() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> cr.findEntityById(UUID.randomUUID()));
        Assertions.assertThrows(InvalidEntityIdException.class, () -> cr.findEntityById(null));
    }

    @Test
    public void TestImmutableUpdate() throws Exception {

        Challenge c = cr.findAllEntities().get(0);
        String original = c.getAuthor();

        c.setAuthor("A new author");
        cr.saveEntity(c);
        Challenge b = cr.findEntityById(c.getChallengeId());
        Assert.assertEquals(b.getAuthor(), original);

        LocalDateTime createdOriginal = b.getCreatedDate();
        b.setCreatedDate(LocalDateTime.MIN);
        cr.saveEntity(b);
        b = cr.findEntityById(c.getChallengeId());
        Assert.assertEquals(b.getCreatedDate(), createdOriginal);

        UUID oldID = b.getChallengeId();
        int cursize = cr.findAllEntities().size();
        b.setChallengeId(UUID.randomUUID());
        b.setQuestion("Different");
        cr.saveEntity(b);

        assertTrue(cr.existsEntity(oldID));
        Assert.assertEquals(cursize + 1, cr.findAllEntities().size());
    }

    @Test
    public void TestBadData() {

        Challenge c = cr.findAllEntities().get(3);

        c.setQuestion("");
        Assertions.assertThrows(ConstraintViolationException.class, () -> cr.saveEntity(c));

        c.setQuestion(null);
        Assertions.assertThrows(ConstraintViolationException.class, () -> cr.saveEntity(c));

        try {
            cr.save(new Challenge());
        } catch (Exception e) {
            Throwable a = ExceptionUtils.getRootCause(e);
            assertTrue(a instanceof ConstraintViolationException);
            assertEquals(3, ((ConstraintViolationException) a).getConstraintViolations().size());
        }
    }


    @Test
    public void TestAudit() throws Exception {

        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = cr.saveEntity(c);

        c.setQuestion("An update!");
        Thread.sleep(500);
        c = cr.saveEntity(c);

        Challenge cur = cr.findEntityById(c.getChallengeId());
        assertNotEquals(cur.getLastModifiedDate(), cur.getCreatedDate());
    }

    @Test
    public void TestAddSimple() {
        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = cr.saveEntity(c);
        Assertions.assertNotNull(c.getChallengeId());
        Assertions.assertNotNull(c.getLastAuthor());
        Assertions.assertNotNull(c.getEnabled());
        Assertions.assertNotNull(c.getCreatedDate());
        Assertions.assertNotNull(c.getLastModifiedDate());
    }

    @Test
    public void TestDelete() throws Exception {

        UUID id = cr.findAllEntities().get(5).getChallengeId();
        cr.deleteEntityById(id);
        assertFalse(cr.existsEntity(id));

        Assertions.assertThrows(EntityNotFoundException.class, () -> cr.deleteEntityById(id));
        Assertions.assertThrows(EntityNotFoundException.class, () -> cr.deleteEntityById(UUID.randomUUID()));
        Assertions.assertThrows(EntityNotFoundException.class, () -> cr.deleteEntityById(null));
    }

}
