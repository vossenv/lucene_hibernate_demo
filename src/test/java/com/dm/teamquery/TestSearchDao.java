package com.dm.teamquery;


import com.dm.teamquery.data.ChallengeRepository;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.search.QueryGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestSearchDao {

    @Inject ChallengeService challengeService;
    @Inject ChallengeRepository challengeRepository;


    @Inject
    QueryGenerator dao;


    @Test
    public void TestBasicDao() {

        String t = extractQuery("a OR b");

        String q1 = extractQuery("a");
        String q2 = extractQuery("a b");
        String q3 = extractQuery("a OR b");
        String q4 = extractQuery("a AND b");
        String q6 = extractQuery("a AND b c");
        String q7 = extractQuery("a b AND c");
        String q8 = extractQuery("a AND c b AND d");
        String q9 = extractQuery("a AND c OR b AND d");
        String q5 = extractQuery("a AND b AND \"this one\"");
        String q10 = extractQuery("\"tell me\" AND a story");
        String q11 = extractQuery("question=alpha AND \"omega 2\" mysterious");



        assertEquals(q1,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%'");
        assertEquals(q2,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or challengeId like '%b%' or question like '%b%' or answer like '%b%' or author like '%b%' or lastAuthor like '%b%' or enabled like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%'");
        assertEquals(q3, "from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or challengeId like '%b%' or question like '%b%' or answer like '%b%' or author like '%b%' or lastAuthor like '%b%' or enabled like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%'");

        assertEquals(q4,"from Challenge where (challengeId like '%a%' and challengeId like '%b%') or (question like '%a%' and question like '%b%') or (answer like '%a%' and answer like '%b%') or (author like '%a%' and author like '%b%') or (lastAuthor like '%a%' and lastAuthor like '%b%') or (enabled like '%a%' and enabled like '%b%') or (dateCreated like '%a%' and dateCreated like '%b%') or (dateLastModified like '%a%' and dateLastModified like '%b%')");
        assertEquals(q5,"from Challenge where (challengeId like '%a%' and challengeId like '%b%' and challengeId like '%this one%') or (question like '%a%' and question like '%b%' and question like '%this one%') or (answer like '%a%' and answer like '%b%' and answer like '%this one%') or (author like '%a%' and author like '%b%' and author like '%this one%') or (lastAuthor like '%a%' and lastAuthor like '%b%' and lastAuthor like '%this one%') or (enabled like '%a%' and enabled like '%b%' and enabled like '%this one%') or (dateCreated like '%a%' and dateCreated like '%b%' and dateCreated like '%this one%') or (dateLastModified like '%a%' and dateLastModified like '%b%' and dateLastModified like '%this one%')");
        assertEquals(q6,"from Challenge where (challengeId like '%a%' and challengeId like '%b%') or (question like '%a%' and question like '%b%') or (answer like '%a%' and answer like '%b%') or (author like '%a%' and author like '%b%') or (lastAuthor like '%a%' and lastAuthor like '%b%') or (enabled like '%a%' and enabled like '%b%') or (dateCreated like '%a%' and dateCreated like '%b%') or (dateLastModified like '%a%' and dateLastModified like '%b%') or challengeId like '%c%' or question like '%c%' or answer like '%c%' or author like '%c%' or lastAuthor like '%c%' or enabled like '%c%' or dateCreated like '%c%' or dateLastModified like '%c%'");

        assertEquals(q7,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or (challengeId like '%b%' and challengeId like '%c%') or (question like '%b%' and question like '%c%') or (answer like '%b%' and answer like '%c%') or (author like '%b%' and author like '%c%') or (lastAuthor like '%b%' and lastAuthor like '%c%') or (enabled like '%b%' and enabled like '%c%') or (dateCreated like '%b%' and dateCreated like '%c%') or (dateLastModified like '%b%' and dateLastModified like '%c%')");
        assertEquals(q8,"from Challenge where (challengeId like '%a%' and challengeId like '%c%') or (question like '%a%' and question like '%c%') or (answer like '%a%' and answer like '%c%') or (author like '%a%' and author like '%c%') or (lastAuthor like '%a%' and lastAuthor like '%c%') or (enabled like '%a%' and enabled like '%c%') or (dateCreated like '%a%' and dateCreated like '%c%') or (dateLastModified like '%a%' and dateLastModified like '%c%') or (challengeId like '%b%' and challengeId like '%d%') or (question like '%b%' and question like '%d%') or (answer like '%b%' and answer like '%d%') or (author like '%b%' and author like '%d%') or (lastAuthor like '%b%' and lastAuthor like '%d%') or (enabled like '%b%' and enabled like '%d%') or (dateCreated like '%b%' and dateCreated like '%d%') or (dateLastModified like '%b%' and dateLastModified like '%d%')");
        assertEquals(q9,"from Challenge where (challengeId like '%a%' and challengeId like '%c%') or (question like '%a%' and question like '%c%') or (answer like '%a%' and answer like '%c%') or (author like '%a%' and author like '%c%') or (lastAuthor like '%a%' and lastAuthor like '%c%') or (enabled like '%a%' and enabled like '%c%') or (dateCreated like '%a%' and dateCreated like '%c%') or (dateLastModified like '%a%' and dateLastModified like '%c%') or (challengeId like '%b%' and challengeId like '%d%') or (question like '%b%' and question like '%d%') or (answer like '%b%' and answer like '%d%') or (author like '%b%' and author like '%d%') or (lastAuthor like '%b%' and lastAuthor like '%d%') or (enabled like '%b%' and enabled like '%d%') or (dateCreated like '%b%' and dateCreated like '%d%') or (dateLastModified like '%b%' and dateLastModified like '%d%')");

        assertEquals(q10,"from Challenge where (challengeId like '%tell me%' and challengeId like '%a%') or (question like '%tell me%' and question like '%a%') or (answer like '%tell me%' and answer like '%a%') or (author like '%tell me%' and author like '%a%') or (lastAuthor like '%tell me%' and lastAuthor like '%a%') or (enabled like '%tell me%' and enabled like '%a%') or (dateCreated like '%tell me%' and dateCreated like '%a%') or (dateLastModified like '%tell me%' and dateLastModified like '%a%') or challengeId like '%story%' or question like '%story%' or answer like '%story%' or author like '%story%' or lastAuthor like '%story%' or enabled like '%story%' or dateCreated like '%story%' or dateLastModified like '%story%'");
        assertEquals(q11,"from Challenge where (question like '%alpha%' and challengeId like '%omega 2%') or (question like '%alpha%' and question like '%omega 2%') or (question like '%alpha%' and answer like '%omega 2%') or (question like '%alpha%' and author like '%omega 2%') or (question like '%alpha%' and lastAuthor like '%omega 2%') or (question like '%alpha%' and enabled like '%omega 2%') or (question like '%alpha%' and dateCreated like '%omega 2%') or (question like '%alpha%' and dateLastModified like '%omega 2%') or challengeId like '%mysterious%' or question like '%mysterious%' or answer like '%mysterious%' or author like '%mysterious%' or lastAuthor like '%mysterious%' or enabled like '%mysterious%' or dateCreated like '%mysterious%' or dateLastModified like '%mysterious%'");




       System.out.println();
    }

    private  String extractQuery (String query){
        return null;
    }

}
