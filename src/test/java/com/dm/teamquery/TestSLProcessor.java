package com.dm.teamquery;


import com.dm.teamquery.search.SLProcessor;
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

        System.out.println();
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

        System.out.println();

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

        System.out.println();

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

        System.out.println();

    }

    @Test
    public void TestBoolean() {

        String q0 = slp.analyze("a OR b c").getDebugQuery();
        String q1 = slp.analyze("a AND b OR c d").getDebugQuery();
        String q2 = slp.analyze("OR AND OR OR AND OR").getDebugQuery();
        String q3 = slp.analyze("OR a AND b c OR d").getDebugQuery();
        String q4 = slp.analyze("c d OR e").getDebugQuery();

        System.out.println();

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

        System.out.println();

    }


    @Test
    public void TestFull() {

        String q0 = slp.analyze("\"a a\" \"a a\" \"b b\"").getDebugQuery();
        String q1 = slp.analyze("\"a a\" AND \"b b\" AND \"c c\"").getDebugQuery();
        String q2 = slp.analyze("OR \"a a\" AND b OR \"c c\"").getDebugQuery();

        System.out.println();
    }


}