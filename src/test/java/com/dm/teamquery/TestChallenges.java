package com.dm.teamquery;


import com.dm.teamquery.data.ChallengeRepository;
import com.dm.teamquery.data.ChallengeService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestChallenges {

    @Inject ChallengeService challengeService;
    @Inject ChallengeRepository challengeRepository;



//    @Test
//    public void TestGet() {
//
//        Challenge c = origninalList.get(5);
//        Challenge d = challengeRepository.findChallengeByChallengeId(c.getChallengeId());
//        Challenge e = challengeService.findChallengeByChallengeId(c.getChallengeId().toString());
//        assertEquals(c, d);
//        assertEquals(d, e);
//
//    }
//
//    @Test
//    public void TestDelete() {
//
//        Challenge c = origninalList.get(2);
//        challengeService.deleteChallengeById(c.getChallengeId().toString());
//        assertNull(challengeService.findChallengeByChallengeId(c.getChallengeId().toString()));
//
//    }
//
//    @Test
//    public void TestUpdate() {
//
//        Challenge c = origninalList.get(3);
//        c.setAnswer("There is no answer...");
//        Challenge d = challengeService.updateChallenge(c);
//        assertEquals(d, challengeService.findChallengeByChallengeId(c.getChallengeId().toString()));
//
//        Challenge e = new Challenge();
//        e.setAnswer("This is the final answer!");
//        e.setQuestion("Was there a question");
//        e.setAuthor("Vossen");
//        e.setDateLastModified(LocalDateTime.now());
//        e.setDateCreated(LocalDateTime.now());
//
//        e = challengeService.updateChallenge(e);
//        assertEquals(e, challengeService.findChallengeByChallengeId(e.getChallengeId().toString()));

//    }


}
