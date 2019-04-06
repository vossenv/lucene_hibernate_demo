package com.dm.teamquery.test;

import com.dm.teamquery.search.SLProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
class TestSLProcessor {

    private SLProcessor slp = new SLProcessor();

    @Test
    void TestSimple() {

        String q0 = slp.format("");
        String q1 = slp.format("a b");
        String q2 = slp.format("a b c c");
        String q3 = slp.format("c a b c");
        String q4 = slp.format("a   b     c");
        String q5 = slp.format("    a   b     c ");
        String q6 = slp.format("    ");

        assertEquals(q0, "*");
        assertEquals(q1, "a~ b~");
        assertEquals(q2, "a~ b~ c~ c~");
        assertEquals(q3, "c~ a~ b~ c~");
        assertEquals(q4, "a~ b~ c~");
        assertEquals(q5, "a~ b~ c~");
        assertEquals(q6, "*");

    }

    @Test
    void TestQuotes() {

        String q0 = slp.format("\"");
        String q1 = slp.format("\"a b\"");
        String q2 = slp.format("\"a b\" c d");
        String q3 = slp.format("\" \\\" test quoted term in quotes \\\" \"");
        String q4 = slp.format("\" a \\\"");
        String q5 = slp.format("\" a and b \" \"c and d\"");
        String q6 = slp.format("\" a and  \"c and d\"");
        String q7 = slp.format("\" a and  \" \" c and d\"");

        assertEquals(q0, "\"~");
        assertEquals(q1, "\"a b\"~");
        assertEquals(q2, "\"a b\"~ c~ d~");
        assertEquals(q3, "\" \\\" test quoted term in quotes \\\" \"~");
        assertEquals(q4, "\"~ a~ \\\"~");
        assertEquals(q5, "\" a and b \"~ \"c and d\"~");
        assertEquals(q6, "\" a and  \"c and d\"~");
        assertEquals(q7, "\" a and  \"~ \" c and d\"~");
    }

    @Test
    void TestAndOrOr() {

        String q0 = slp.format("a AND b");
        String q1 = slp.format("a AND b AND c");
        String q2 = slp.format("AND a AND b AND c");
        String q3 = slp.format("AND AND AND");
        String q4 = slp.format("ANDANDAND");
        String q5 = slp.format("a AND bANDc ANDd");
        String q6 = slp.format("a ANDb c");
        String q7 = slp.format("OR a OR b OR c OR");

        assertEquals(q0, "a~ AND b~");
        assertEquals(q1, "a~ AND b~ AND c~");
        assertEquals(q2, "a~ AND b~ AND c~");
        assertEquals(q3, "");
        assertEquals(q4, "");
        assertEquals(q5, "a~ AND bANDc~ ANDd~");
        assertEquals(q5, "a~ AND bANDc~ ANDd~");
        assertEquals(q7, "a~ OR b~ OR c~");

    }


    @Test
    void TestKeyword() {

        String q0 = slp.format("author : a");
        String q1 = slp.format("author:a");
        String q2 = slp.format("author :a");
        String q3 = slp.format("author :a ");
        String q4 = slp.format("author : ");
        String q5 = slp.format("author : content : a");
        String q6 = slp.format(":");
        String q7 = slp.format(": a ");
        String q8 = slp.format("b :: a ");
        String q9 = slp.format(": b : a : ");
        String q10 = slp.format(":::: :");
        String q11 = slp.format("a : \"x y z\"");
        String q12 = slp.format("\"a b c\" : \"x y z\"");
        String q13 = slp.format("\"a b c\" : x");
        String q14 = slp.format("\"a b c\" : \"666\"  5 :");
        String q15 = slp.format("a : b : c : d :");
        String q16 = slp.format(": a : b : c : d :");

        assertEquals(q0, "author:a~");
        assertEquals(q1, "author:a~");
        assertEquals(q2, "author:a~");
        assertEquals(q3, "author:a~");
        assertEquals(q4, "author:");
        assertEquals(q5, "author:content~ : a~");
        assertEquals(q6, ":");
        assertEquals(q7, ": a~");
        assertEquals(q8, "b:: a~");
        assertEquals(q9, ": b:a~ :");
        assertEquals(q10, ":::: :");
        assertEquals(q11, "a:\"x y z\"~");
        assertEquals(q12, "\"a b c\":\"x y z\"~");
        assertEquals(q13, "\"a b c\":x~");
        assertEquals(q14, "\"a b c\":\"666\"~ 5:");
        assertEquals(q15, "a:b~ : c:d~ :");
        assertEquals(q16, ": a:b~ : c:d~ :");


    }


    @Test
    void TestFull() {

        String q0 = slp.format("\"a a\" \"a a\" \"b b\"");
        String q1 = slp.format("\"a a\" AND \"b b\" AND \"c c\"");
        String q2 = slp.format("OR \"a a\" AND b OR \"c c\"");
        String q3 = slp.format("(this OR that) (and this) AND abcd");
        String q4 = slp.format("((a nested) group)");
        String q5 = slp.format("((\"a nested\" planet) group)");
        String q6 = slp.format("this OR (a:keyword in)");
        String q7 = slp.format("this OR (a : keyword in)");
        String q8 = slp.format("this OR (a : \"keyword in\" here)");

        assertEquals(q0, "\"a a\"~ \"a a\"~ \"b b\"~");
        assertEquals(q1, "\"a a\"~ AND \"b b\"~ AND \"c c\"~");
        assertEquals(q2, "\"a a\"~ AND b~ OR \"c c\"~");
        assertEquals(q3, "(this~ OR that~) (and~ this~) AND abcd~");
        assertEquals(q4, "((a~ nested~) group~)");
        assertEquals(q5, "((\"a nested\"~ planet~) group~)");
        assertEquals(q6, "this~ OR (a:keyword~ in~)");
        assertEquals(q7, "this~ OR (a:keyword~ in~)");
        assertEquals(q8, "this~ OR (a:\"keyword in\"~ here~)");

    }

    @Test
    void TestSpecialChars() {
        slp.format("~!@#$%^&*()_+-/*-+<>?:{}|\\]`[';/.,']");
        String q0 = slp.format("@!# $() @#*&(^ AND &*^%????\\\\\\ ");
        String q1 = slp.format("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"");
        String q2 = slp.format("~!@#$%^&*()_+-/*-+<>?:{}|\\]`[';/.,']");
        String q3 = slp.format("\\");
        String q4 = slp.format("(a AND b) OR c");

        assertEquals(q0, "@\\!#~ $() @#*&(\\^ AND &*\\^%\\?\\?\\?\\?\\\\\\\\\\\\");
        assertEquals(q1, "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"~");
        assertEquals(q2, "~\\!@#$%\\^&*()_\\+\\-\\/*\\-\\+<>\\?:\\{\\}|\\\\\\]`\\[';\\/.,'\\]");
        assertEquals(q3, "\\\\");
        assertEquals(q4, "(a~ AND b~) OR c~");

    }

}