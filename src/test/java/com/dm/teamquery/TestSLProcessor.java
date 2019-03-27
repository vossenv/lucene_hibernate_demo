package com.dm.teamquery;


import com.dm.teamquery.search.SLProcessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
public class TestSLProcessor {

    SLProcessor slp;

    @Before
    public void setup() {
        this.slp = new SLProcessor();
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

        Assert.assertEquals(q0, "");
        Assert.assertEquals(q1, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b}");
        Assert.assertEquals(q2, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c} (5)AND-{AND} (6)TEXT-{c}");
        Assert.assertEquals(q3, "(0)TEXT-{c} (1)AND-{AND} (2)TEXT-{a} (3)AND-{AND} (4)TEXT-{b} (5)AND-{AND} (6)TEXT-{c}");
        Assert.assertEquals(q4, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        Assert.assertEquals(q5, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        Assert.assertEquals(q6, "");

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

        Assert.assertEquals(q0, "");
        Assert.assertEquals(q1, "(0)QUOTED-{a b}");
        Assert.assertEquals(q2, "(0)QUOTED-{a b} (1)AND-{AND} (2)TEXT-{c} (3)AND-{AND} (4)TEXT-{d}");
        Assert.assertEquals(q3, "(0)QUOTED-{\" test quoted term in quotes \"}");
        Assert.assertEquals(q4, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{\"}");
        Assert.assertEquals(q5, "(0)QUOTED-{a and b} (1)AND-{AND} (2)QUOTED-{c and d}");
        Assert.assertEquals(q6, "(0)QUOTED-{a and  \"c and d}");
        Assert.assertEquals(q7, "(0)QUOTED-{a and} (1)AND-{AND} (2)TEXT-{c} (3)AND-{AND} (4)TEXT-{and} (5)AND-{AND} (6)TEXT-{d}");
        Assert.assertEquals(q8, "(0)QUOTED-{\"} (1)AND-{AND} (2)QUOTED-{\"\"\"\"} (3)AND-{AND} (4)TEXT-{\"\"}");
        Assert.assertEquals(q9, "(0)QUOTED-{\"} (1)AND-{AND} (2)QUOTED-{\"\"\"\"} (3)AND-{AND} (4)TEXT-{\"} (5)AND-{AND} (6)TEXT-{\"}");
        Assert.assertEquals(q10, "(0)QUOTED-{ } (1)AND-{AND} (2)QUOTED-{ }");
        Assert.assertEquals(q11, "(0)QUOTED-{\" \" \"}");
        Assert.assertEquals(q12, "(0)TEXT-{\"} (1)AND-{AND} (2)TEXT-{\"} (3)AND-{AND} (4)TEXT-{\"}");
        Assert.assertEquals(q13, "(0)TEXT-{\"} (1)AND-{AND} (2)TEXT-{\"} (3)AND-{AND} (4)TEXT-{\"}");



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

        Assert.assertEquals(q0, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b}");
        Assert.assertEquals(q1, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        Assert.assertEquals(q2, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        Assert.assertEquals(q3, "");
        Assert.assertEquals(q4, "");
        Assert.assertEquals(q5, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{bANDc} (3)AND-{AND} (4)TEXT-{ANDd}");
        Assert.assertEquals(q6, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{ANDb} (3)AND-{AND} (4)TEXT-{c}");

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

        Assert.assertEquals(q0, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b}");
        Assert.assertEquals(q1, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b} (3)OR-{OR} (4)TEXT-{c}");
        Assert.assertEquals(q2, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b} (3)OR-{OR} (4)TEXT-{c}");
        Assert.assertEquals(q3, "");
        Assert.assertEquals(q4, "");
        Assert.assertEquals(q5, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{bORc} (3)OR-{OR} (4)TEXT-{ORd}");
        Assert.assertEquals(q6, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{ORb} (3)AND-{AND} (4)TEXT-{c}");


    }

    @Test
    public void TestBoolean() {

        String q0 = slp.analyze("a OR b c").getDebugQuery();
        String q1 = slp.analyze("a AND b OR c d").getDebugQuery();
        String q2 = slp.analyze("OR AND OR OR AND OR").getDebugQuery();
        String q3 = slp.analyze("OR a AND b c OR d").getDebugQuery();
        String q4 = slp.analyze("c d OR e").getDebugQuery();

        Assert.assertEquals(q0, "(0)TEXT-{a} (1)OR-{OR} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c}");
        Assert.assertEquals(q1, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)OR-{OR} (4)TEXT-{c} (5)AND-{AND} (6)TEXT-{d}");
        Assert.assertEquals(q2, "");
        Assert.assertEquals(q3, "(0)TEXT-{a} (1)AND-{AND} (2)TEXT-{b} (3)AND-{AND} (4)TEXT-{c} (5)OR-{OR} (6)TEXT-{d}");
        Assert.assertEquals(q4, "(0)TEXT-{c} (1)AND-{AND} (2)TEXT-{d} (3)OR-{OR} (4)TEXT-{e}");

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

        Assert.assertEquals(q0, "(0)KEYWORD-{key: author, val: a}");
        Assert.assertEquals(q1, "(0)KEYWORD-{key: author, val: a}");
        Assert.assertEquals(q2, "(0)KEYWORD-{key: author, val: a}");
        Assert.assertEquals(q3, "(0)KEYWORD-{key: author, val: a}");
        Assert.assertEquals(q4, "(0)KEYWORD-{key: author, val: }");
        Assert.assertEquals(q5, "(0)KEYWORD-{key: author, val: content} (1)AND-{AND} (2)TEXT-{=} (3)AND-{AND} (4)TEXT-{a}");
        Assert.assertEquals(q6, "(0)TEXT-{=}");
        Assert.assertEquals(q7, "(0)TEXT-{=} (1)AND-{AND} (2)TEXT-{a}");
        Assert.assertEquals(q8, "(0)KEYWORD-{key: b, val: =} (1)AND-{AND} (2)TEXT-{a}");
        Assert.assertEquals(q9, "(0)TEXT-{=} (1)AND-{AND} (2)KEYWORD-{key: b, val: a} (3)AND-{AND} (4)TEXT-{=}");
        Assert.assertEquals(q10, "(0)TEXT-{==== =}");
        Assert.assertEquals(q11, "(0)KEYWORD-{key: a, val: x y z}");
        Assert.assertEquals(q12, "(0)KEYWORD-{key: a b c, val: x y z}");
        Assert.assertEquals(q13, "(0)KEYWORD-{key: a b c, val: x}");
        Assert.assertEquals(q14, "(0)KEYWORD-{key: a b c, val: 666} (1)AND-{AND} (2)KEYWORD-{key: 5, val: }");
        Assert.assertEquals(q15, "(0)KEYWORD-{key: a, val: b} (1)AND-{AND} (2)TEXT-{=} (3)AND-{AND} (4)KEYWORD-{key: c, val: d} (5)AND-{AND} (6)TEXT-{=}");
        Assert.assertEquals(q16, "(0)TEXT-{=} (1)AND-{AND} (2)KEYWORD-{key: a, val: b} (3)AND-{AND} (4)TEXT-{=} (5)AND-{AND} (6)KEYWORD-{key: c, val: d} (7)AND-{AND} (8)TEXT-{=}");


    }


    @Test
    public void TestFull() {

        String q0 = slp.analyze("\"a a\" \"a a\" \"b b\"").getDebugQuery();
        String q1 = slp.analyze("\"a a\" AND \"b b\" AND \"c c\"").getDebugQuery();
        String q2 = slp.analyze("OR \"a a\" AND b OR \"c c\"").getDebugQuery();

        Assert.assertEquals(q0, "(0)QUOTED-{a a} (1)AND-{AND} (2)QUOTED-{a a} (3)AND-{AND} (4)QUOTED-{b b}");
        Assert.assertEquals(q1, "(0)QUOTED-{a a} (1)AND-{AND} (2)QUOTED-{b b} (3)AND-{AND} (4)QUOTED-{c c}");
        Assert.assertEquals(q2, "(0)QUOTED-{a a} (1)AND-{AND} (2)TEXT-{b} (3)OR-{OR} (4)QUOTED-{c c}");

    }


}