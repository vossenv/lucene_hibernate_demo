package com.dm.teamquery;


import com.dm.teamquery.data.ChallengeDao;
import com.dm.teamquery.data.ChallengeRepository;
import com.dm.teamquery.model.Challenge;
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
    ChallengeDao challengeDao;

    @Inject
    ChallengeRepository challengeRepository;

    @Test
    public void TestCRUD() {

        Challenge c = new Challenge();

        c.setAnswer("CCC");
        c.setQuestion("BBB");
        c.setAuthor("DDD");

       // Challenge d = challengeRepository.save(c);

       // challengeRepository.deleteById();

        Object o = challengeDao.findChallengeByAnswerContains("What");



        System.out.println();


    }


}
