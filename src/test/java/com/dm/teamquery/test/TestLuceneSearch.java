package com.dm.teamquery.test;


import com.dm.teamquery.data.service.SearchService;
import com.dm.teamquery.entity.Challenge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Arrays;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class TestLuceneSearch {

    @Inject
    SearchService searchService;

    @Test
    void testSearchFailure() {
        Arrays.asList(queries).forEach(s -> searchService.search(s, Challenge.class));
    }

    @Test
    void TestSLProcessor() {


    }

    private static final String[] queries = {
            "",
            "?",
            "*",
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
            "AND AND AND",
            "ANDANDAND",
            "a AND bANDc ANDd",
            "a ANDb c",
            "OR a OR b OR c OR",
            "author : a",
            "author:a",
            "author :a",
            "author :a ",
            "notfield:c",
            ":",
            ":::: :",
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
            "@!# $() @#*&(^ AND &*^%????\\\\\\ ",
            "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"",
            "~!@#$%^&*()_+-/*-+<>?:{}|\\]`[';/.,']",
            "\\",
            "(a AND b) OR c",
    };
}



