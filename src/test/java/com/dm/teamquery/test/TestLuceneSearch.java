package com.dm.teamquery.test;


import com.dm.teamquery.data.service.ChallengeService;
import com.dm.teamquery.data.service.SearchService;
import com.dm.teamquery.entity.Challenge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TestLuceneSearch {

    @Inject
    SearchService searchService;

    @Inject
    ChallengeService challengeService;

    @PostConstruct
    private void setType(){
        searchService.setEntityType(Challenge.class);
    }

    @Test
    void testNormalQueries() {
        Arrays.stream(queries).forEach(s -> {
            try {
                searchService.search(s);
            } catch (Exception e){
                fail();
            }
        });
    }

    @Test
    void testSearchExceptions() {
        try {
            searchService.search("aaaaa \"");
        } catch (Exception e) {
            String g = e.getMessage();
            System.out.println();
        }
    }

    @Test
    void indexNewItem() throws Exception{
        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = challengeService.save(c);
        assertEquals(searchService.search(c.getChallengeId().toString()).size(),1);
    }

    @Test
    void indexDeletedItem() throws Exception{
        Challenge c = challengeService.findAll().get(0);
        challengeService.deleteById(c.getChallengeId());
        assertEquals(searchService.search(c.getChallengeId().toString()).size(),0);
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
}



