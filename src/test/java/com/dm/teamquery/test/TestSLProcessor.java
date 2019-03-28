package com.dm.teamquery.test;

import com.dm.teamquery.search.SLProcessor;
import com.dm.teamquery.test.util.TestResources;
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
    String andFlag;
    String orFlag;

    @BeforeEach
    public void setup() {
        slp = new SLProcessor();
        andFlag = slp.getAND_FLAG();
        orFlag = slp.getOR_FLAG();
    }

    @Test
    public void TestSimple() {

        String q0 = slp.analyze("").getDebugQuery();
        String q1 = slp.analyze("a b").getDebugQuery();
        String q2 = slp.analyze("a b c c").getDebugQuery();
        String q3 = slp.analyze("c a b c").getDebugQuery();
        String q4 = slp.analyze("a   b     c").getDebugQuery();
        String q5 = slp.analyze("    a   b     c ").getDebugQuery();
        String q6 = slp.analyze("    ").getDebugQuery();

        assertEquals(q0, "");
        assertEquals(q1, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b}");
        assertEquals(q2, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c} (5)AND-{AND} (6)TEXT-{c}");
        assertEquals(q3, "(0)TEXT-{c} (1)AND-{AND} (2)TEXT-{a} (3)AND-{AND} (4)TEXT-{b} (5)AND-{AND} (6)TEXT-{c}");
        assertEquals(q4, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        assertEquals(q5, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        assertEquals(q6, "");

    }

    @Test
    public void TestQuotes() {

        String q0 = slp.analyze("\"").getDebugQuery();
        String q1 = slp.analyze("\"a b\"").getDebugQuery();
        String q2 = slp.analyze("\"a b\" c d").getDebugQuery();
        String q3 = slp.analyze("\" \\\" test quoted term in quotes \\\" \"").getDebugQuery();
        String q4 = slp.analyze("\" a \\\"").getDebugQuery();
        String q5 = slp.analyze("\" a and b \" \"c and d\"").getDebugQuery();
        String q6 = slp.analyze("\" a and  \"c and d\"").getDebugQuery();
        String q7 = slp.analyze("\" a and  \" c and d\"").getDebugQuery();
        String q8 = slp.analyze("    \"\"\" \"\"\"\\\"\"\" \"\" \\\"\\\" ").getDebugQuery();
        String q9 = slp.analyze("\"\" \" \"\"\"\\\"\"\" \"\" \\\" \\\"").getDebugQuery();
        String q10 = slp.analyze("\" \" \" \" \"").getDebugQuery();
        String q11 = slp.analyze("\" \\\" \\\" \\\" \"").getDebugQuery();
        String q12 = slp.analyze("\\\" \\\" \\\"").getDebugQuery();
        String q13 = slp.analyze("\\\" \\\" \" \\\"").getDebugQuery();

        assertEquals(q0, "");
        assertEquals(q1, "(0)QUOTED-{a b}");
        assertEquals(q2, "(0)QUOTED-{a b} (1)AND-{AND} (2)TEXT-{c} (3)AND-{AND} (4)TEXT-{d}");
        assertEquals(q3, "(0)QUOTED-{\" test quoted term in quotes \"}");
        assertEquals(q4, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{\"}");
        assertEquals(q5, "(0)QUOTED-{a and b} (1)AND-{AND} (2)QUOTED-{c and d}");
        assertEquals(q6, "(0)QUOTED-{a and  \"c and d}");
        assertEquals(q7, "(0)QUOTED-{a and} (1)AND-{AND} (2)TEXT-{c} (3)AND-{AND} (4)TEXT-{and} (5)AND-{AND} (6)TEXT-{d}");
        assertEquals(q8, "(0)QUOTED-{\"} (1)AND-{AND} (2)QUOTED-{\"\"\"\"} (3)AND-{AND} (4)TEXT-{\"\"}");
        assertEquals(q9, "(0)QUOTED-{\"} (1)AND-{AND} (2)QUOTED-{\"\"\"\"} (3)AND-{AND} (4)TEXT-{\"} (5)AND-{AND} (6)TEXT-{\"}");
        assertEquals(q10, "(0)QUOTED-{ } (1)AND-{AND} (2)QUOTED-{ }");
        assertEquals(q11, "(0)QUOTED-{\" \" \"}");
        assertEquals(q12, "(0)TEXT-{\"} (1)AND-{AND} (2)TEXT-{\"} (3)AND-{AND} (4)TEXT-{\"}");
        assertEquals(q13, "(0)TEXT-{\"} (1)AND-{AND} (2)TEXT-{\"} (3)AND-{AND} (4)TEXT-{\"}");



    }

    @Test
    public void TestAnd() {


        String q0 = slp.analyze("a AND b").getDebugQuery();
        String q1 = slp.analyze("a AND b AND c").getDebugQuery();
        String q2 = slp.analyze("AND a AND b AND c").getDebugQuery();
        String q3 = slp.analyze("AND AND AND").getDebugQuery();
        String q4 = slp.analyze("ANDANDAND").getDebugQuery();
        String q5 = slp.analyze("a AND bANDc ANDd").getDebugQuery();
        String q6 = slp.analyze("a ANDb c").getDebugQuery();

        assertEquals(q0, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b}");
        assertEquals(q1, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        assertEquals(q2, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        assertEquals(q3, "");
        assertEquals(q4, "");
        assertEquals(q5, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{bANDc} (3)AND-{AND} (4)TEXT-{ANDd}");
        assertEquals(q6, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{ANDb} (3)AND-{AND} (4)TEXT-{c}");

    }

    @Test
    public void TestOr() {

        String q0 = slp.analyze("a OR b").getDebugQuery();
        String q1 = slp.analyze("a OR b OR c").getDebugQuery();
        String q2 = slp.analyze("OR a OR b OR c").getDebugQuery();
        String q3 = slp.analyze("OR OR OR").getDebugQuery();
        String q4 = slp.analyze("OROROR").getDebugQuery();
        String q5 = slp.analyze("a OR bORc OR ORd").getDebugQuery();
        String q6 = slp.analyze("a ORb c").getDebugQuery();

        assertEquals(q0, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b}");
        assertEquals(q1, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b} (3)OR-{OR} (4)TEXT-{c}");
        assertEquals(q2, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b} (3)OR-{OR} (4)TEXT-{c}");
        assertEquals(q3, "");
        assertEquals(q4, "");
        assertEquals(q5, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{bORc} (3)OR-{OR} (4)TEXT-{ORd}");
        assertEquals(q6, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{ORb} (3)AND-{AND} (4)TEXT-{c}");


    }

    @Test
    public void TestBoolean() {

        String q0 = slp.analyze("a OR b c").getDebugQuery();
        String q1 = slp.analyze("a AND b OR c d").getDebugQuery();
        String q2 = slp.analyze("OR AND OR OR AND OR").getDebugQuery();
        String q3 = slp.analyze("OR a AND b c OR d").getDebugQuery();
        String q4 = slp.analyze("c d OR e").getDebugQuery();

        assertEquals(q0, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        assertEquals(q1, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)OR-{OR} (4)TEXT-{c} (5)AND-{AND} (6)TEXT-{d}");
        assertEquals(q2, "");
        assertEquals(q3, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c} (5)OR-{OR} (6)TEXT-{d}");
        assertEquals(q4, "(0)TEXT-{c} (1)AND-{AND} (2)TEXT-{d} (3)OR-{OR} (4)TEXT-{e}");

    }

    @Test
    public void TestKeyword() {

        String q0 = slp.analyze("author = a").getDebugQuery();
        String q1 = slp.analyze("author=a").getDebugQuery();
        String q2 = slp.analyze("author =a").getDebugQuery();
        String q3 = slp.analyze("author =a ").getDebugQuery();
        String q4 = slp.analyze("author = ").getDebugQuery();
        String q5 = slp.analyze("author = content = a").getDebugQuery();
        String q6 = slp.analyze("=").getDebugQuery();
        String q7 = slp.analyze("= a ").getDebugQuery();
        String q8 = slp.analyze("b == a ").getDebugQuery();
        String q9 = slp.analyze("= b = a = ").getDebugQuery();
        String q10 = slp.analyze("==== =").getDebugQuery();
        String q11 = slp.analyze("a = \"x y z\"").getDebugQuery();
        String q12 = slp.analyze("\"a b c\" = \"x y z\"").getDebugQuery();
        String q13 = slp.analyze("\"a b c\" = x").getDebugQuery();
        String q14 = slp.analyze("\"a b c\" = \"666\"  5 =").getDebugQuery();
        String q15 = slp.analyze("a = b = c = d =").getDebugQuery();
        String q16 = slp.analyze("= a = b = c = d =").getDebugQuery();

        assertEquals(q0, "(0)KEYWORD-{key: author, val: a}");
        assertEquals(q1, "(0)KEYWORD-{key: author, val: a}");
        assertEquals(q2, "(0)KEYWORD-{key: author, val: a}");
        assertEquals(q3, "(0)KEYWORD-{key: author, val: a}");
        assertEquals(q4, "(0)KEYWORD-{key: author, val: }");
        assertEquals(q5, "(0)KEYWORD-{key: author, val: content} (1)AND-{AND} (2)TEXT-{=} (3)AND-{AND} (4)TEXT-{a}");
        assertEquals(q6, "(0)TEXT-{=}");
        assertEquals(q7, "(0)TEXT-{=} (1)AND-{AND} (2)TEXT-{a}");
        assertEquals(q8, "(0)KEYWORD-{key: b, val: =} (1)AND-{AND} (2)TEXT-{a}");
        assertEquals(q9, "(0)TEXT-{=} (1)AND-{AND} (2)KEYWORD-{key: b, val: a} (3)AND-{AND} (4)TEXT-{=}");
        assertEquals(q10, "(0)TEXT-{==== =}");
        assertEquals(q11, "(0)KEYWORD-{key: a, val: x y z}");
        assertEquals(q12, "(0)KEYWORD-{key: a b c, val: x y z}");
        assertEquals(q13, "(0)KEYWORD-{key: a b c, val: x}");
        assertEquals(q14, "(0)KEYWORD-{key: a b c, val: 666} (1)AND-{AND} (2)KEYWORD-{key: 5, val: }");
        assertEquals(q15, "(0)KEYWORD-{key: a, val: b} (1)AND-{AND} (2)TEXT-{=} (3)AND-{AND} (4)KEYWORD-{key: c, val: d} (5)AND-{AND} (6)TEXT-{=}");
        assertEquals(q16, "(0)TEXT-{=} (1)AND-{AND} (2)KEYWORD-{key: a, val: b} (3)AND-{AND} (4)TEXT-{=} (5)AND-{AND} (6)KEYWORD-{key: c, val: d} (7)AND-{AND} (8)TEXT-{=}");


    }


    @Test
    public void TestFull() {

        String q0 = slp.analyze("\"a a\" \"a a\" \"b b\"").getDebugQuery();
        String q1 = slp.analyze("\"a a\" AND \"b b\" AND \"c c\"").getDebugQuery();
        String q2 = slp.analyze("OR \"a a\" AND b OR \"c c\"").getDebugQuery();

        assertEquals(q0, "(0)QUOTED-{a a} (1)AND-{AND} (2)QUOTED-{a a} (3)AND-{AND} (4)QUOTED-{b b}");
        assertEquals(q1, "(0)QUOTED-{a a} (1)AND-{AND} (2)QUOTED-{b b} (3)AND-{AND} (4)QUOTED-{c c}");
        assertEquals(q2, "(0)QUOTED-{a a} (1)AND-{AND} (2)TEXT-{b} (3)OR-{OR} (4)QUOTED-{c c}");

    }

    @Test
    public void TestSpecialChars() {

        String q0 = slp.analyze("@!# $() @#*&(^ AND &*^%????\\\\\\ ").getDebugQuery();
        String q1 = slp.analyze("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"").getDebugQuery();
        String q2 = slp.analyze("~!@#$%^&*()_+-/*-+<>?:{}|\\]`[';/.,']").getDebugQuery();
        String q3 = slp.analyze(TestResources.SPECIAL).getDebugQuery();

        assertEquals(q0, "(0)TEXT-{@!#} (1)AND-{AND} (2)TEXT-{$()} (3)AND-{AND} (4)TEXT-{@#*&(^} (5)AND-{AND} (6)TEXT-{&*^%????\\\\\\}");
        assertEquals(q1, "(0)TEXT-{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}");
        assertEquals(q2, "(0)TEXT-{~!@#$%^&*()_+-/*-+<>?:{}|\\]`[';/.,']}");
        assertEquals(q3, TestResources.SPECIAL_MATCH);
    }

}