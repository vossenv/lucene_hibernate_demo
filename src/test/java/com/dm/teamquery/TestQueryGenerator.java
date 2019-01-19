package com.dm.teamquery;


import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.search.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestQueryGenerator {

    @Test
    public void TestBasicQuery() {

        Search s = new Search(Challenge.class);

        String q1 = s.setQuery("a").getDatabaseQuery();
        String q2 = s.setQuery("a b").getDatabaseQuery();
        String q3 = s.setQuery("\"a b\"").getDatabaseQuery();
        String q4 = s.setQuery("\"a b\" c d").getDatabaseQuery();
        String q5 = s.setQuery("a OR    b").getDatabaseQuery();
        String q6 = s.setQuery("a OR c   b").getDatabaseQuery();

        assertEquals(q1, "from Challenge where challengeId like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or question like '%a%' or answer like '%a%' or lastAuthor like '%a%' or author like '%a%' or enabled like '%a%'");
        assertEquals(q2, "from Challenge where challengeId like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or question like '%a%' or answer like '%a%' or lastAuthor like '%a%' or author like '%a%' or enabled like '%a%' or challengeId like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or question like '%b%' or answer like '%b%' or lastAuthor like '%b%' or author like '%b%' or enabled like '%b%'");
        assertEquals(q3, "from Challenge where challengeId like '%a b%' or dateCreated like '%a b%' or dateLastModified like '%a b%' or question like '%a b%' or answer like '%a b%' or lastAuthor like '%a b%' or author like '%a b%' or enabled like '%a b%'");
        assertEquals(q4, "from Challenge where challengeId like '%a b%' or dateCreated like '%a b%' or dateLastModified like '%a b%' or question like '%a b%' or answer like '%a b%' or lastAuthor like '%a b%' or author like '%a b%' or enabled like '%a b%' or challengeId like '%c%' or dateCreated like '%c%' or dateLastModified like '%c%' or question like '%c%' or answer like '%c%' or lastAuthor like '%c%' or author like '%c%' or enabled like '%c%' or challengeId like '%d%' or dateCreated like '%d%' or dateLastModified like '%d%' or question like '%d%' or answer like '%d%' or lastAuthor like '%d%' or author like '%d%' or enabled like '%d%'");
        assertEquals(q5, "from Challenge where challengeId like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or question like '%a%' or answer like '%a%' or lastAuthor like '%a%' or author like '%a%' or enabled like '%a%' or challengeId like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or question like '%b%' or answer like '%b%' or lastAuthor like '%b%' or author like '%b%' or enabled like '%b%'");
        assertEquals(q6, "from Challenge where challengeId like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or question like '%a%' or answer like '%a%' or lastAuthor like '%a%' or author like '%a%' or enabled like '%a%' or challengeId like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or question like '%b%' or answer like '%b%' or lastAuthor like '%b%' or author like '%b%' or enabled like '%b%' or challengeId like '%c%' or dateCreated like '%c%' or dateLastModified like '%c%' or question like '%c%' or answer like '%c%' or lastAuthor like '%c%' or author like '%c%' or enabled like '%c%'");
    }


    @Test
    public void TestBooleanQuery(){

        Search s = new Search(Challenge.class);

        String q7 = s.setQuery("a AND b").getDatabaseQuery();
        String q8 = s.setQuery("aANDa b AND c ").getDatabaseQuery();
        String q9 = s.setQuery("\"a b\" AND e c").getDatabaseQuery();
        String q10 = s.setQuery("a AND b AND e OR c AND d").getDatabaseQuery();
        String q11 = s.setQuery("a b\" AND c d AND p").getDatabaseQuery();
        String q12 = s.setQuery("f a AND c OR d AND p").getDatabaseQuery();

        assertEquals(q7,"from Challenge where (challengeId like '%a%' and challengeId like '%b%') or (dateCreated like '%a%' and dateCreated like '%b%') or (dateLastModified like '%a%' and dateLastModified like '%b%') or (question like '%a%' and question like '%b%') or (answer like '%a%' and answer like '%b%') or (lastAuthor like '%a%' and lastAuthor like '%b%') or (author like '%a%' and author like '%b%') or (enabled like '%a%' and enabled like '%b%')");
        assertEquals(q8,"from Challenge where (challengeId like '%b%' and challengeId like '%c%') or (dateCreated like '%b%' and dateCreated like '%c%') or (dateLastModified like '%b%' and dateLastModified like '%c%') or (question like '%b%' and question like '%c%') or (answer like '%b%' and answer like '%c%') or (lastAuthor like '%b%' and lastAuthor like '%c%') or (author like '%b%' and author like '%c%') or (enabled like '%b%' and enabled like '%c%') or challengeId like '%aanda%' or dateCreated like '%aanda%' or dateLastModified like '%aanda%' or question like '%aanda%' or answer like '%aanda%' or lastAuthor like '%aanda%' or author like '%aanda%' or enabled like '%aanda%'");
        assertEquals(q9,"from Challenge where challengeId like '%c%' or dateCreated like '%c%' or dateLastModified like '%c%' or question like '%c%' or answer like '%c%' or lastAuthor like '%c%' or author like '%c%' or enabled like '%c%' or (challengeId like '%a b%' and challengeId like '%e%') or (dateCreated like '%a b%' and dateCreated like '%e%') or (dateLastModified like '%a b%' and dateLastModified like '%e%') or (question like '%a b%' and question like '%e%') or (answer like '%a b%' and answer like '%e%') or (lastAuthor like '%a b%' and lastAuthor like '%e%') or (author like '%a b%' and author like '%e%') or (enabled like '%a b%' and enabled like '%e%')");
        assertEquals(q10,"from Challenge where (challengeId like '%a%' and challengeId like '%b%' and challengeId like '%e%') or (dateCreated like '%a%' and dateCreated like '%b%' and dateCreated like '%e%') or (dateLastModified like '%a%' and dateLastModified like '%b%' and dateLastModified like '%e%') or (question like '%a%' and question like '%b%' and question like '%e%') or (answer like '%a%' and answer like '%b%' and answer like '%e%') or (lastAuthor like '%a%' and lastAuthor like '%b%' and lastAuthor like '%e%') or (author like '%a%' and author like '%b%' and author like '%e%') or (enabled like '%a%' and enabled like '%b%' and enabled like '%e%') or (challengeId like '%c%' and challengeId like '%d%') or (dateCreated like '%c%' and dateCreated like '%d%') or (dateLastModified like '%c%' and dateLastModified like '%d%') or (question like '%c%' and question like '%d%') or (answer like '%c%' and answer like '%d%') or (lastAuthor like '%c%' and lastAuthor like '%d%') or (author like '%c%' and author like '%d%') or (enabled like '%c%' and enabled like '%d%')");
        assertEquals(q11,"from Challenge where challengeId like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or question like '%a%' or answer like '%a%' or lastAuthor like '%a%' or author like '%a%' or enabled like '%a%' or (challengeId like '%p%' and challengeId like '%d%') or (dateCreated like '%p%' and dateCreated like '%d%') or (dateLastModified like '%p%' and dateLastModified like '%d%') or (question like '%p%' and question like '%d%') or (answer like '%p%' and answer like '%d%') or (lastAuthor like '%p%' and lastAuthor like '%d%') or (author like '%p%' and author like '%d%') or (enabled like '%p%' and enabled like '%d%') or (challengeId like '%b\"%' and challengeId like '%c%') or (dateCreated like '%b\"%' and dateCreated like '%c%') or (dateLastModified like '%b\"%' and dateLastModified like '%c%') or (question like '%b\"%' and question like '%c%') or (answer like '%b\"%' and answer like '%c%') or (lastAuthor like '%b\"%' and lastAuthor like '%c%') or (author like '%b\"%' and author like '%c%') or (enabled like '%b\"%' and enabled like '%c%')");
        assertEquals(q12,"from Challenge where (challengeId like '%p%' and challengeId like '%d%') or (dateCreated like '%p%' and dateCreated like '%d%') or (dateLastModified like '%p%' and dateLastModified like '%d%') or (question like '%p%' and question like '%d%') or (answer like '%p%' and answer like '%d%') or (lastAuthor like '%p%' and lastAuthor like '%d%') or (author like '%p%' and author like '%d%') or (enabled like '%p%' and enabled like '%d%') or challengeId like '%f%' or dateCreated like '%f%' or dateLastModified like '%f%' or question like '%f%' or answer like '%f%' or lastAuthor like '%f%' or author like '%f%' or enabled like '%f%' or (challengeId like '%a%' and challengeId like '%c%') or (dateCreated like '%a%' and dateCreated like '%c%') or (dateLastModified like '%a%' and dateLastModified like '%c%') or (question like '%a%' and question like '%c%') or (answer like '%a%' and answer like '%c%') or (lastAuthor like '%a%' and lastAuthor like '%c%') or (author like '%a%' and author like '%c%') or (enabled like '%a%' and enabled like '%c%')");

    }

    @Test
    public void TestKeywordQuery() {

        Search s = new Search(Challenge.class);

        String q1 = s.setQuery("question=a").getDatabaseQuery();
        String q2 = s.setQuery("answer=\"a b\"").getDatabaseQuery();
        String q3 = s.setQuery("question = a b").getDatabaseQuery();
        String q4 = s.setQuery("question=a answer = b").getDatabaseQuery();
        String q5 = s.setQuery("question=a b answer = b").getDatabaseQuery();
        String q6 = s.setQuery("question= \"a t\" answer = b").getDatabaseQuery();
        String q7 = s.setQuery("a AND answer = b AND d").getDatabaseQuery();

        assertEquals(q1,"from Challenge where question like '%a%'");
        assertEquals(q2,"from Challenge where answer like '%a b%'");
        assertEquals(q3,"from Challenge where challengeId like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or question like '%b%' or answer like '%b%' or lastAuthor like '%b%' or author like '%b%' or enabled like '%b%' or question like '%a%'");
        assertEquals(q4,"from Challenge where question like '%a%' or answer like '%b%'");
        assertEquals(q5,"from Challenge where challengeId like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or question like '%b%' or answer like '%b%' or lastAuthor like '%b%' or author like '%b%' or enabled like '%b%' or question like '%a%' or answer like '%b%'");
        assertEquals(q6,"from Challenge where answer like '%b%' or question like '%a t%'");
        assertEquals(q7,"from Challenge where (answer like '%b%') and ((challengeId like '%a%' and challengeId like '%d%') or (dateCreated like '%a%' and dateCreated like '%d%') or (dateLastModified like '%a%' and dateLastModified like '%d%') or (question like '%a%' and question like '%d%') or (answer like '%a%' and answer like '%d%') or (lastAuthor like '%a%' and lastAuthor like '%d%') or (author like '%a%' and author like '%d%') or (enabled like '%a%' and enabled like '%d%')) ");
    }

    @Test
    public void TestInvalidSearches(){

        Search s = new Search(Challenge.class);

        String q1 = s.setQuery("").getDatabaseQuery();
        String q2 = s.setQuery("AND").getDatabaseQuery();
        String q3 = s.setQuery("OR").getDatabaseQuery();
        String q4 = s.setQuery("AND AND OR").getDatabaseQuery();
        String q5 = s.setQuery("question= ").getDatabaseQuery();
//        String q6 = s.setQuery(" = a").getDatabaseQuery();
//        String q7 = s.setQuery(" = ").getDatabaseQuery();
//        String q8 = s.setQuery("    ").getDatabaseQuery();
//        String q9 = s.setQuery("\" \"   ").getDatabaseQuery();
//        String q10 = s.setQuery("\" AND \" OR  ").getDatabaseQuery();

        System.out.println();

    }



}

