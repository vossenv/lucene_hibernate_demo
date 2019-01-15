package com.dm.teamquery;


import com.dm.teamquery.data.ChallengeCustomDao;
import com.dm.teamquery.data.ChallengeRepository;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchEngine;
import com.dm.teamquery.model.Challenge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestSearchDao {

    @Inject ChallengeService challengeService;
    @Inject ChallengeRepository challengeRepository;
    @Inject SearchEngine searchEngine;
    @Inject ChallengeCustomDao dao;


    @Test
    public void TestBasicDao() {

        Map<String, List<String>> searchMap = searchEngine.constructSearchMap("question=alpha AND author=god \"omega 2\" mysterious");

        String SQLquery = dao.generateQuery(searchMap);


        List<Challenge> l = dao.executeSearch(SQLquery);


       System.out.println();
    }

}
