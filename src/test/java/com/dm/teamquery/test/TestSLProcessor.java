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
public class TestSLProcessor {

    SLProcessor slp;

    @BeforeEach
    public void setup() {
        slp = new SLProcessor();
    }

    @Test
    public void TestSimple() {

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
    public void TestQuotes() {

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
        assertEquals(q4, "\"~ a~ \\\\\"~");
        assertEquals(q5, "\" a and b \"~ \"c and d\"~");
        assertEquals(q6, "\" a and  \"c and d\"~");
        assertEquals(q7, "\" a and  \"~ \" c and d\"~");
    }

    @Test
    public void TestAndOrOr() {

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

//
//    @Test
//    public void TestBoolean() {
//
//        String q0 = slp.format("a OR b c");
//        String q1 = slp.format("a AND b OR c d");
//        String q2 = slp.format("OR AND OR OR AND OR");
//        String q3 = slp.format("OR a AND b c OR d");
//        String q4 = slp.format("c d OR e");
//
//        assertEquals(q0, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
//        assertEquals(q1, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)OR-{OR} (4)TEXT-{c} (5)AND-{AND} (6)TEXT-{d}");
//        assertEquals(q2, "");
//        assertEquals(q3, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c} (5)OR-{OR} (6)TEXT-{d}");
//        assertEquals(q4, "(0)TEXT-{c} (1)AND-{AND} (2)TEXT-{d} (3)OR-{OR} (4)TEXT-{e}");
//
//    }
//
//    @Test
//    public void TestKeyword() {
//
//        String q0 = slp.format("author = a");
//        String q1 = slp.format("author=a");
//        String q2 = slp.format("author =a");
//        String q3 = slp.format("author =a ");
//        String q4 = slp.format("author = ");
//        String q5 = slp.format("author = content = a");
//        String q6 = slp.format("=");
//        String q7 = slp.format("= a ");
//        String q8 = slp.format("b == a ");
//        String q9 = slp.format("= b = a = ");
//        String q10 = slp.format("==== =");
//        String q11 = slp.format("a = \"x y z\"");
//        String q12 = slp.format("\"a b c\" = \"x y z\"");
//        String q13 = slp.format("\"a b c\" = x");
//        String q14 = slp.format("\"a b c\" = \"666\"  5 =");
//        String q15 = slp.format("a = b = c = d =");
//        String q16 = slp.format("= a = b = c = d =");
//
//        assertEquals(q0, "(0)KEYWORD-{key: author, val: a}");
//        assertEquals(q1, "(0)KEYWORD-{key: author, val: a}");
//        assertEquals(q2, "(0)KEYWORD-{key: author, val: a}");
//        assertEquals(q3, "(0)KEYWORD-{key: author, val: a}");
//        assertEquals(q4, "(0)KEYWORD-{key: author, val: }");
//        assertEquals(q5, "(0)KEYWORD-{key: author, val: content} (1)AND-{AND} (2)TEXT-{=} (3)AND-{AND} (4)TEXT-{a}");
//        assertEquals(q6, "(0)TEXT-{=}");
//        assertEquals(q7, "(0)TEXT-{=} (1)AND-{AND} (2)TEXT-{a}");
//        assertEquals(q8, "(0)KEYWORD-{key: b, val: =} (1)AND-{AND} (2)TEXT-{a}");
//        assertEquals(q9, "(0)TEXT-{=} (1)AND-{AND} (2)KEYWORD-{key: b, val: a} (3)AND-{AND} (4)TEXT-{=}");
//        assertEquals(q10, "(0)TEXT-{==== =}");
//        assertEquals(q11, "(0)KEYWORD-{key: a, val: x y z}");
//        assertEquals(q12, "(0)KEYWORD-{key: a b c, val: x y z}");
//        assertEquals(q13, "(0)KEYWORD-{key: a b c, val: x}");
//        assertEquals(q14, "(0)KEYWORD-{key: a b c, val: 666} (1)AND-{AND} (2)KEYWORD-{key: 5, val: }");
//        assertEquals(q15, "(0)KEYWORD-{key: a, val: b} (1)AND-{AND} (2)TEXT-{=} (3)AND-{AND} (4)KEYWORD-{key: c, val: d} (5)AND-{AND} (6)TEXT-{=}");
//        assertEquals(q16, "(0)TEXT-{=} (1)AND-{AND} (2)KEYWORD-{key: a, val: b} (3)AND-{AND} (4)TEXT-{=} (5)AND-{AND} (6)KEYWORD-{key: c, val: d} (7)AND-{AND} (8)TEXT-{=}");
//
//
//    }
//
//
//    @Test
//    public void TestFull() {
//
//        String q0 = slp.format("\"a a\" \"a a\" \"b b\"");
//        String q1 = slp.format("\"a a\" AND \"b b\" AND \"c c\"");
//        String q2 = slp.format("OR \"a a\" AND b OR \"c c\"");
//
//        assertEquals(q0, "(0)QUOTED-{a a} (1)AND-{AND} (2)QUOTED-{a a} (3)AND-{AND} (4)QUOTED-{b b}");
//        assertEquals(q1, "(0)QUOTED-{a a} (1)AND-{AND} (2)QUOTED-{b b} (3)AND-{AND} (4)QUOTED-{c c}");
//        assertEquals(q2, "(0)QUOTED-{a a} (1)AND-{AND} (2)TEXT-{b} (3)OR-{OR} (4)QUOTED-{c c}");
//
//    }
//
//    @Test
//    public void TestSpecialChars() {
//
//        String q0 = slp.format("@!# $() @#*&(^ AND &*^%????\\\\\\ ");
//        String q1 = slp.format("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"");
//        String q2 = slp.format("~!@#$%^&*()_+-/*-+<>?:{}|\\]`[';/.,']");
//        String q3 = slp.format(TestResources.SPECIAL);
//
//        assertEquals(q0, "(0)TEXT-{@!#} (1)AND-{AND} (2)TEXT-{$()} (3)AND-{AND} (4)TEXT-{@#*&(^} (5)AND-{AND} (6)TEXT-{&*^%????\\\\\\}");
//        assertEquals(q1, "(0)TEXT-{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}");
//        assertEquals(q2, "(0)TEXT-{~!@#$%^&*()_+-/*-+<>?:{}|\\]`[';/.,']}");
//        assertEquals(q3, TestResources.SPECIAL_MATCH);
//    }
//
}