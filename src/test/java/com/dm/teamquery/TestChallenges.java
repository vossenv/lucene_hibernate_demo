package com.dm.teamquery;



import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.BadEntityException;
import com.dm.teamquery.execption.EntityUpdateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestChallenges {

    @Inject
    ChallengeService challengeService;
//
//    @Test
//    public void TestUpdae() throws EntityUpdateException {
//
//        List<Challenge> allChallenges = challengeService.search("").getResultsList();
//        int initialSize = allChallenges.size();
//        Challenge c = allChallenges.get(0);
//        String originalQuestion = c.getQuestion();
//        UUID id = c.getChallengeId();
//        LocalDateTime time = c.getDateLastModified();
//
//        c.setQuestion("ABC");
//        assertEquals(c,  challengeService.updateChallenge(c));
//
//        Challenge e = challengeService.search(id).getResultsList().get(0);
//        assertEquals(c, e);
//        assertNotEquals(time, e.getDateLastModified());
//        assertEquals(initialSize, challengeService.search("").getResultsList().size());
//
//        c.setQuestion(originalQuestion);
//        challengeService.updateChallenge(c);
//    }

    @Test
    public void TestAdd() throws EntityUpdateException, BadEntityException {

        Challenge c = new Challenge();
        Challenge d = challengeService.updateChallenge(c);

        c.setChallengeId(d.getChallengeId());
        assertEquals(c, d);

        challengeService.deleteChallengeById(d.getChallengeId().toString());
    }

    @Test
    public void TestAddNullField() {

        Challenge c = new Challenge();
        c.setAuthor(null);

        try {
            challengeService.updateChallenge(c);
            fail("Should have failed due to null field");
        } catch (EntityUpdateException e) {
            // pass
        }
    }

//    @Test
//    public void TestDelete() throws BadEntityException, EntityUpdateException {
//
//        Challenge c = challengeService.search("").getResultsList().get(0);
//        challengeService.deleteChallengeById(c.getChallengeId().toString());
//
//        List<Challenge> results = challengeService.search(c.getChallengeId()).getResultsList();
//        assertEquals(0, results.size());
//
//        Challenge d = challengeService.updateChallenge(c);
//        assertEquals(d.getChallengeId(), c.getChallengeId());
//
//    }

    @Test
    public void TestDeleteNull(){
        try {
            challengeService.deleteChallengeById("abc");
            fail("Should have failed due to nonexistent ID");
        } catch (BadEntityException e){
            // pass
        }
    }

}
