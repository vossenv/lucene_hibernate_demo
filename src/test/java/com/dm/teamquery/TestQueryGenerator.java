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

        assertEquals(q1,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%'");
        assertEquals(q2,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or challengeId like '%b%' or question like '%b%' or answer like '%b%' or author like '%b%' or lastAuthor like '%b%' or enabled like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%'");
        assertEquals(q3,"from Challenge where challengeId like '%a b%' or question like '%a b%' or answer like '%a b%' or author like '%a b%' or lastAuthor like '%a b%' or enabled like '%a b%' or dateCreated like '%a b%' or dateLastModified like '%a b%'");
        assertEquals(q4,"from Challenge where challengeId like '%c%' or question like '%c%' or answer like '%c%' or author like '%c%' or lastAuthor like '%c%' or enabled like '%c%' or dateCreated like '%c%' or dateLastModified like '%c%' or challengeId like '%d%' or question like '%d%' or answer like '%d%' or author like '%d%' or lastAuthor like '%d%' or enabled like '%d%' or dateCreated like '%d%' or dateLastModified like '%d%' or challengeId like '%a b%' or question like '%a b%' or answer like '%a b%' or author like '%a b%' or lastAuthor like '%a b%' or enabled like '%a b%' or dateCreated like '%a b%' or dateLastModified like '%a b%'");
        assertEquals(q5,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or challengeId like '%b%' or question like '%b%' or answer like '%b%' or author like '%b%' or lastAuthor like '%b%' or enabled like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%'");
        assertEquals(q6,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or challengeId like '%b%' or question like '%b%' or answer like '%b%' or author like '%b%' or lastAuthor like '%b%' or enabled like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or challengeId like '%c%' or question like '%c%' or answer like '%c%' or author like '%c%' or lastAuthor like '%c%' or enabled like '%c%' or dateCreated like '%c%' or dateLastModified like '%c%'");

    }


    @Test
    public void TestBooleanQuery(){

        Search s = new Search(Challenge.class);

        String q1 = s.setQuery("a AND b").getDatabaseQuery();
        String q2 = s.setQuery("aANDa b AND c ").getDatabaseQuery();
        String q3 = s.setQuery("\"a b\" AND e c").getDatabaseQuery();
        String q4 = s.setQuery("a AND b AND e OR c AND d").getDatabaseQuery();
        String q5 = s.setQuery("a b\" AND c d AND p").getDatabaseQuery();
        String q6 = s.setQuery("f a AND c OR d AND p").getDatabaseQuery();

        assertEquals(q1,"from Challenge where (challengeId like '%a%' and challengeId like '%b%') or (question like '%a%' and question like '%b%') or (answer like '%a%' and answer like '%b%') or (author like '%a%' and author like '%b%') or (lastAuthor like '%a%' and lastAuthor like '%b%') or (enabled like '%a%' and enabled like '%b%') or (dateCreated like '%a%' and dateCreated like '%b%') or (dateLastModified like '%a%' and dateLastModified like '%b%')");
        assertEquals(q2,"from Challenge where (challengeId like '%b%' and challengeId like '%c%') or (question like '%b%' and question like '%c%') or (answer like '%b%' and answer like '%c%') or (author like '%b%' and author like '%c%') or (lastAuthor like '%b%' and lastAuthor like '%c%') or (enabled like '%b%' and enabled like '%c%') or (dateCreated like '%b%' and dateCreated like '%c%') or (dateLastModified like '%b%' and dateLastModified like '%c%') or challengeId like '%aanda%' or question like '%aanda%' or answer like '%aanda%' or author like '%aanda%' or lastAuthor like '%aanda%' or enabled like '%aanda%' or dateCreated like '%aanda%' or dateLastModified like '%aanda%'");
        assertEquals(q3,"from Challenge where (challengeId like '%a b%' and challengeId like '%e%') or (question like '%a b%' and question like '%e%') or (answer like '%a b%' and answer like '%e%') or (author like '%a b%' and author like '%e%') or (lastAuthor like '%a b%' and lastAuthor like '%e%') or (enabled like '%a b%' and enabled like '%e%') or (dateCreated like '%a b%' and dateCreated like '%e%') or (dateLastModified like '%a b%' and dateLastModified like '%e%') or challengeId like '%c%' or question like '%c%' or answer like '%c%' or author like '%c%' or lastAuthor like '%c%' or enabled like '%c%' or dateCreated like '%c%' or dateLastModified like '%c%'");
        assertEquals(q4,"from Challenge where (challengeId like '%a%' and challengeId like '%b%' and challengeId like '%e%') or (question like '%a%' and question like '%b%' and question like '%e%') or (answer like '%a%' and answer like '%b%' and answer like '%e%') or (author like '%a%' and author like '%b%' and author like '%e%') or (lastAuthor like '%a%' and lastAuthor like '%b%' and lastAuthor like '%e%') or (enabled like '%a%' and enabled like '%b%' and enabled like '%e%') or (dateCreated like '%a%' and dateCreated like '%b%' and dateCreated like '%e%') or (dateLastModified like '%a%' and dateLastModified like '%b%' and dateLastModified like '%e%') or (challengeId like '%c%' and challengeId like '%d%') or (question like '%c%' and question like '%d%') or (answer like '%c%' and answer like '%d%') or (author like '%c%' and author like '%d%') or (lastAuthor like '%c%' and lastAuthor like '%d%') or (enabled like '%c%' and enabled like '%d%') or (dateCreated like '%c%' and dateCreated like '%d%') or (dateLastModified like '%c%' and dateLastModified like '%d%')");
        assertEquals(q5,"from Challenge where challengeId like '%a%' or question like '%a%' or answer like '%a%' or author like '%a%' or lastAuthor like '%a%' or enabled like '%a%' or dateCreated like '%a%' or dateLastModified like '%a%' or (challengeId like '%b\"%' and challengeId like '%c%') or (question like '%b\"%' and question like '%c%') or (answer like '%b\"%' and answer like '%c%') or (author like '%b\"%' and author like '%c%') or (lastAuthor like '%b\"%' and lastAuthor like '%c%') or (enabled like '%b\"%' and enabled like '%c%') or (dateCreated like '%b\"%' and dateCreated like '%c%') or (dateLastModified like '%b\"%' and dateLastModified like '%c%') or (challengeId like '%p%' and challengeId like '%d%') or (question like '%p%' and question like '%d%') or (answer like '%p%' and answer like '%d%') or (author like '%p%' and author like '%d%') or (lastAuthor like '%p%' and lastAuthor like '%d%') or (enabled like '%p%' and enabled like '%d%') or (dateCreated like '%p%' and dateCreated like '%d%') or (dateLastModified like '%p%' and dateLastModified like '%d%')");
        assertEquals(q6,"from Challenge where challengeId like '%f%' or question like '%f%' or answer like '%f%' or author like '%f%' or lastAuthor like '%f%' or enabled like '%f%' or dateCreated like '%f%' or dateLastModified like '%f%' or (challengeId like '%a%' and challengeId like '%c%') or (question like '%a%' and question like '%c%') or (answer like '%a%' and answer like '%c%') or (author like '%a%' and author like '%c%') or (lastAuthor like '%a%' and lastAuthor like '%c%') or (enabled like '%a%' and enabled like '%c%') or (dateCreated like '%a%' and dateCreated like '%c%') or (dateLastModified like '%a%' and dateLastModified like '%c%') or (challengeId like '%p%' and challengeId like '%d%') or (question like '%p%' and question like '%d%') or (answer like '%p%' and answer like '%d%') or (author like '%p%' and author like '%d%') or (lastAuthor like '%p%' and lastAuthor like '%d%') or (enabled like '%p%' and enabled like '%d%') or (dateCreated like '%p%' and dateCreated like '%d%') or (dateLastModified like '%p%' and dateLastModified like '%d%')");
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
        assertEquals(q3,"from Challenge where challengeId like '%b%' or question like '%b%' or answer like '%b%' or author like '%b%' or lastAuthor like '%b%' or enabled like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or question like '%a%'");
        assertEquals(q4,"from Challenge where answer like '%b%' or question like '%a%'");
        assertEquals(q5,"from Challenge where challengeId like '%b%' or question like '%b%' or answer like '%b%' or author like '%b%' or lastAuthor like '%b%' or enabled like '%b%' or dateCreated like '%b%' or dateLastModified like '%b%' or answer like '%b%' or question like '%a%'");
        assertEquals(q6,"from Challenge where answer like '%b%' or question like '%a t%'");
        assertEquals(q7,"from Challenge where (answer like '%b%') and ((challengeId like '%a%' and challengeId like '%d%') or (question like '%a%' and question like '%d%') or (answer like '%a%' and answer like '%d%') or (author like '%a%' and author like '%d%') or (lastAuthor like '%a%' and lastAuthor like '%d%') or (enabled like '%a%' and enabled like '%d%') or (dateCreated like '%a%' and dateCreated like '%d%') or (dateLastModified like '%a%' and dateLastModified like '%d%')) ");

    }

    @Test
    public void TestInvalidSearches(){

        Search s = new Search(Challenge.class);

        String q1 = s.setQuery("").getDatabaseQuery();
        String q2 = s.setQuery("AND").getDatabaseQuery();
        String q3 = s.setQuery("OR").getDatabaseQuery();
        String q4 = s.setQuery("AND AND OR").getDatabaseQuery();
        String q5 = s.setQuery("question= ").getDatabaseQuery();
        String q6 = s.setQuery("=question= ").getDatabaseQuery();
        String q7 = s.setQuery("= abc ").getDatabaseQuery();
        String q8 = s.setQuery("answer").getDatabaseQuery();
        String q9 = s.setQuery(" ").getDatabaseQuery();
        String q10 = s.setQuery("\"").getDatabaseQuery();
        String q11 = s.setQuery("\"=\"").getDatabaseQuery();
        String q12 = s.setQuery("~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-").getDatabaseQuery();
        String q13 = s.setQuery("\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"").getDatabaseQuery();

        assertEquals(q1,"from Challenge");
        assertEquals(q2,"from Challenge");
        assertEquals(q3,"from Challenge");
        assertEquals(q4,"from Challenge");
        assertEquals(q5,"from Challenge where challengeId like '%question=%' or question like '%question=%' or answer like '%question=%' or author like '%question=%' or lastAuthor like '%question=%' or enabled like '%question=%' or dateCreated like '%question=%' or dateLastModified like '%question=%'");
        assertEquals(q6,"from Challenge where challengeId like '%=question=%' or question like '%=question=%' or answer like '%=question=%' or author like '%=question=%' or lastAuthor like '%=question=%' or enabled like '%=question=%' or dateCreated like '%=question=%' or dateLastModified like '%=question=%'");
        assertEquals(q7,"from Challenge where challengeId like '%= abc%' or question like '%= abc%' or answer like '%= abc%' or author like '%= abc%' or lastAuthor like '%= abc%' or enabled like '%= abc%' or dateCreated like '%= abc%' or dateLastModified like '%= abc%'");
        assertEquals(q8,"from Challenge where challengeId like '%answer%' or question like '%answer%' or answer like '%answer%' or author like '%answer%' or lastAuthor like '%answer%' or enabled like '%answer%' or dateCreated like '%answer%' or dateLastModified like '%answer%'");
        assertEquals(q9,"from Challenge");
        assertEquals(q10,"from Challenge where challengeId like '%\"%' or question like '%\"%' or answer like '%\"%' or author like '%\"%' or lastAuthor like '%\"%' or enabled like '%\"%' or dateCreated like '%\"%' or dateLastModified like '%\"%'");
        assertEquals(q11,"from Challenge where challengeId like '%=%' or question like '%=%' or answer like '%=%' or author like '%=%' or lastAuthor like '%=%' or enabled like '%=%' or dateCreated like '%=%' or dateLastModified like '%=%'");
        assertEquals(q12,"from Challenge where challengeId like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%' or question like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%' or answer like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%' or author like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%' or lastAuthor like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%' or enabled like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%' or dateCreated like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%' or dateLastModified like '%~!@#$%^&*()_+{}|:?><,./';[]\\=-*/+-%'");
        assertEquals(q13,"from Challenge");

    }

}

