package com.dm.teamquery;


import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.data.repository.ChallengeService;
import com.dm.teamquery.data.repository.custom.ABsTest;
import com.dm.teamquery.entity.Challenge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamqueryApplicationTests {


    @Inject
    ChallengeService challengeService;

    @Inject
    ABsTest ttt;

    @Inject
    ChallengeRepository challengeRepository;

    @Test
    public void contextLoads() {

        ttt.setSimpleJpaRepository(challengeRepository);

        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        ttt.test(c);

    }
}
