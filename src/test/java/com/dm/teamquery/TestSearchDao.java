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


// from Challenge where challengeid like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastauthor like '%a%' or datecreated like '%a%' or datelastmodified like '%a%'challengeid like '%c%' or question like '%c%' or answer like '%c%' or author like '%c%' or lastauthor like '%c%' or datecreated like '%c%' or datelastmodified like '%c%'
    @Test
    public void TestBasicDao() {

        Map<String, List<String>> searchMap = searchEngine.constructSearchMap("roll help federated");

        String SQLquery = dao.generateQuery(searchMap);


        String SQLquery2 = "from Challenge where challengeId like '%a%' or question like '%c' or challengeId like '%b%'";

        List<Challenge> l = dao.executeSearch(SQLquery);


       System.out.println();
    }

}
