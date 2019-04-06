package com.dm.teamquery.test;


import com.dm.teamquery.data.service.SearchService;
import com.dm.teamquery.entity.Challenge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TestLuceneSearch {

    @Inject
    SearchService searchService;

    @Test
    void testNormalQueries() {
        Arrays.stream(queries).forEach(s -> {
            try {
                searchService.search(s, Challenge.class);
            } catch (Exception e){
                fail();
            }
        });
    }


    @Test
    void testSearchExceptions() throws Exception{

        List<Challenge> l = searchService.search("adoebe", Challenge.class);

        try {
            searchService.search("aaaaa \"", Challenge.class);
        } catch (Exception e) {
            String g = e.getMessage();
            System.out.println();
        }

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



