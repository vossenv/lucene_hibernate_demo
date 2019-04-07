package com.dm.teamquery.test;


import com.dm.teamquery.data.service.ChallengeService;
import com.dm.teamquery.data.service.SearchService;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.customexception.SearchFailedException;
import com.dm.teamquery.search.SearchParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TestLuceneSearch {

    @Inject
    SearchService searchService;

    @Inject
    ChallengeService challengeService;

    @PostConstruct
    private void setType() {
        searchService.setEntityType(Challenge.class);
    }

    @Test
    void testNormalQueries() {
        Arrays.stream(queries).forEach(s ->
            assertDoesNotThrow(() -> searchService.search(s))
        );
    }

    @Test
    void testSearchExceptions() {
        Arrays.stream(failqueries).forEach(s ->
           assertThrows(SearchFailedException.class, () -> searchService.search(s)));
    }

    @Test
    void testFilter() throws Exception {
        assertEquals(searchService.count(""), 17);
        assertEquals(searchService.count(new SearchParameters.Builder().enabledOnly().build()),16);
        assertEquals(searchService.count("phonybalogna@yourdomain.com"), 1);
        assertEquals(searchService.count(
                new SearchParameters.Builder().withQuery("phonybalogna@yourdomain.com").enabledOnly().build()), 0);
    }

    @Test
    void indexNewItem() throws Exception {
        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = challengeService.save(c);
        assertEquals(searchService.search(c.getChallengeId().toString()).size(), 1);
        challengeService.deleteById(c.getChallengeId());
    }

    @Test
    void indexDeletedItem() throws Exception {
        Challenge c = challengeService.findAll().get(0);
        challengeService.deleteById(c.getChallengeId());
        assertEquals(searchService.search(c.getChallengeId().toString()).size(), 0);
        challengeService.save(c);
    }

    @Test
    void testResultCount() throws Exception {
        int count = searchService.count(new SearchParameters.Builder().withPageSize(1).build());
        assertEquals(count, 17);
    }

    @Test
    void testPagedSearch() throws Exception {

        int total = 0;
        for (int i = 0; i < 15; i++) {
            int pageSize = searchService.search(
                    new SearchParameters.Builder().withPageable(PageRequest.of(i, 5)).build()).size();
            if (pageSize == 0) {
                assertEquals(4, i);
                break;
            }
            total += pageSize;
            if (i < 3) assertEquals(5, pageSize);
        }
        assertEquals(total, 17);
    }

    private static final String[] queries = {
            "",
            "a b",
            "a b c c",
            "c a b c",
            "a   b     c",
            "    a   b     c ",
            "    ",
            "\"a b\"",
            "\"a b\" c d",
            "\" \\\" test quoted term in quotes \\\" \"",
            "\" a and b \" \"c and d\"",
            "a AND b",
            "a AND b AND c",
            "AND a AND b AND c",
            "a AND bANDc ANDd",
            "a ANDb c",
            "OR a OR b OR c OR",
            "author : a",
            "author:a",
            "author :a",
            "author :a ",
            "notfield:c",
            "a : \"x y z\"",
            "\"a a\" \"a a\" \"b b\"",
            "\"a a\" AND \"b b\" AND \"c c\"",
            "OR \"a a\" AND b OR \"c c\"",
            "(this OR that) (and this) AND abcd",
            "((a nested) group)",
            "((\"a nested\" planet) group)",
            "this OR (a:keyword in)",
            "this OR (a : keyword in)",
            "this OR (a : \"keyword in\" here)",
            "(a AND b) OR c",
    };

    private static final String[] failqueries = {
            "!#$%&(",
            ":: : ",
            "\"",
            ":",
            "a:",
            ":b",
            "C:D:E:F:G",
            "() ()()()",
            "AND AND AND",
            "ANDANDAND"
    };
}



