package com.dm.teamquery;


import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.execption.EntityUpdateException;
import com.dm.teamquery.execption.SearchFailedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestChallenges {



    @Inject
    ChallengeRepository cr;

    @Test
    public void TestUpdate() throws EntityUpdateException, SearchFailedException, Exception {

//        List<Challenge> allChallenges = challengeService.basicSearch("");
//        int initialSize = allChallenges.size();
//        Challenge c = allChallenges.get(0);
//        String originalQuestion = c.getQuestion();
//        UUID id = c.getChallengeId();
//        LocalDateTime time = c.getDateLastModified();
//
//        c.setQuestion("ABCEFS");
//        assertEquals(c,  challengeService.updateChallenge(c));
//
//        c.setAuthor("ASDASDASD");
//
//        Challenge a = new Challenge();
//        a.setQuestion("ASDASDASD");
//        a.setAnswer("GGGGGGG");
//
//        Challenge x = challengeService.updateChallenge(a);
//
//        a.setAuthor("000");
//        a.setAnswer("55555555555555");
//        challengeService.updateChallenge(a);
//        Challenge t = challengeService.getById(a.getChallengeId());
//
//        Challenge e = challengeService.basicSearch(id.toString()).get(0);
//        assertEquals(c, e);
//        assertNotEquals(time, e.getDateLastModified());
//        assertEquals(initialSize, challengeService.basicSearch("").size());
//
//        c.setQuestion(originalQuestion);
//        challengeService.updateChallenge(c);
    }

//    @Test
//    public void TestAddSimple() throws EntityUpdateException, BadEntityException {
//
//        Challenge c = new Challenge();
//        Challenge d = challengeService.updateChallenge(c);
//
//        c.setChallengeId(d.getChallengeId());
//        assertEquals(c, d);
//
//        challengeService.deleteById(d.getChallengeId().toString());
//    }
//
//    @Test
//    public void TestAddNullField() {
//
//        Challenge c = new Challenge();
//        c.setAuthor(null);
//
//        try {
//            challengeService.updateChallenge(c);
//            fail("Should have failed due to null field");
//        } catch (EntityUpdateException e) {
//            // pass
//        }
//    }
//
//    @Test
//    public void TestDelete() throws  Exception {
//
//        UUID id  = challengeService.basicSearch("").get(0).getChallengeId();
//        cr.deleteEntityById(id);
//        assertFalse(cr.existsEntity(id));
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> challengeService.deleteChallengeById(id));
//        Assertions.assertThrows(EntityNotFoundException.class, () -> challengeService.deleteChallengeById(UUID.randomUUID()));
//        Assertions.assertThrows(EntityNotFoundException.class, () -> challengeService.deleteChallengeById(null));
//    }


}
