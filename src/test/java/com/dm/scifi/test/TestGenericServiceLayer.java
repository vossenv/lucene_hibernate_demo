package com.dm.scifi.test;


import com.dm.scifi.data.service.ChallengeService;
import com.dm.scifi.entity.Challenge;
import com.dm.scifi.execption.customexception.SearchFailedException;
import com.dm.scifi.execption.customexception.TeamQueryException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TestGenericServiceLayer {

    @Inject
    private ChallengeService gd;

    @FunctionalInterface
    interface ExceptionCheck<T> {
        void execute(T t) throws TeamQueryException;
    }

    private void MatchException(ExceptionCheck ec, Object o, Class c) {
        try {
            ec.execute(o);
        } catch (Exception e) {
            Throwable t = ExceptionUtils.getRootCause(e);
            assertEquals((null == t ? e : t).getClass(), c);
        }
    }

    @Test
    void TestFind() {
        ExceptionCheck<UUID> ec = (e) -> gd.findById(e);
        assertEquals(17, gd.findAll().size());
        MatchException(ec, UUID.randomUUID(), EntityNotFoundException.class);
        MatchException(ec, null, IllegalArgumentException.class);
    }

    @Test
    void TestAddSimple() {
        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = gd.save(c);
        assertNotNull(c.getChallengeId());
        assertNotNull(c.getLastAuthor());
        assertNotNull(c.getEnabled());
        assertNotNull(c.getCreatedDate());
        assertNotNull(c.getLastModifiedDate());
    }

    @Test
    void TestBadData() {
        ExceptionCheck<Challenge> ec = (e) -> gd.save(e);
        Challenge c = gd.findAll().get(3);
        c.setQuestion("");
        MatchException(ec, c, ConstraintViolationException.class);
        c.setQuestion(null);
        MatchException(ec, c, ConstraintViolationException.class);
        MatchException(ec, new Challenge(), ConstraintViolationException.class);
    }

    @Test
    void TestUpdate(){
        Challenge c = gd.findAll().get(0);
        c.setQuestion("A new one");
        gd.save(c);
        assertEquals(c, gd.findById(c.getChallengeId()));
    }

    @Test
    void TestImmutableUpdate() {

        Challenge c = gd.findAll().get(0);
        String authorOriginal = c.getAuthor();
        LocalDateTime createdOriginal = c.getCreatedDate();

        c.setAuthor("A new author");
        assertEquals(gd.save(c).getAuthor(), authorOriginal);

        c.setCreatedDate(LocalDateTime.MIN);
        assertEquals(gd.save(c).getCreatedDate(), createdOriginal);

        int cursize = gd.findAll().size();
        c.setChallengeId(UUID.randomUUID());
        c.setQuestion("Different");
        c = gd.save(c);

        assertTrue(gd.existsById(c.getChallengeId()));
        assertEquals(cursize + 1, gd.findAll().size());
    }

    @Test
    void TestAudit() throws Exception {

        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = gd.save(c);

        c.setQuestion("An update!");
        Thread.sleep(500);
        c = gd.save(c);

        Challenge cur = gd.findById(c.getChallengeId());
        assertNotEquals(cur.getLastModifiedDate(), cur.getCreatedDate());
    }

    @Test
    void TestDelete() {

        UUID id = gd.findAll().get(5).getChallengeId();
        gd.deleteById(id);
        assertFalse(gd.existsById(id));

        ExceptionCheck<UUID> ec = (e) -> gd.deleteById(e);
        MatchException(ec, id, EntityNotFoundException.class);
        MatchException(ec, UUID.randomUUID(), EntityNotFoundException.class);
        MatchException(ec, null, IllegalArgumentException.class);

    }

    @Test
    void testSearch() throws SearchFailedException {

        List<Challenge> challengeList0 = gd.basicSearch("sso");
        List<Challenge> challengeList1 = gd.basicSearch("federated");
        List<Challenge> challengeList2 = gd.basicSearch("*ID*");
        List<Challenge> challengeList3 = gd.basicSearch("phonybalogna@yourdomain.com");
        List<Challenge> challengeList4 = gd.basicSearch("*adobe*");
        List<Challenge> challengeList5 = gd.basicSearch("");
        List<Challenge> challengeList6 = gd.basicSearch("*http*");
        List<Challenge> challengeList7 = gd.basicSearch("ID AND federated");
        List<Challenge> challengeList8 = gd.basicSearch("\"to the individual\"");
        List<Challenge> challengeList9 = gd.basicSearch("question:\"users\" AND adobe");
        List<Challenge> challengeList10 = gd.basicSearch("question:\"my users\"");
        List<Challenge> challengeList11 = gd.basicSearch("http");
        List<Challenge> challengeList12 = gd.basicSearch("*@*");

        System.out.println();
    }


}
