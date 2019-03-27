package com.dm.teamquery;


import com.dm.teamquery.search.SearchBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestSearchBuilder {


    @Test
    public void TestSimple() {

        SearchBuilder s = new SearchBuilder();

        String q0 = s.setQuery("").getSLProcessor().getDebugQuery();
        String q1 = s.setQuery("a b").getSLProcessor().getDebugQuery();
        String q2 = s.setQuery("a b c c").getSLProcessor().getDebugQuery();
        String q3 = s.setQuery("c a b c").getSLProcessor().getDebugQuery();
        String q4 = s.setQuery("a   b     c").getSLProcessor().getDebugQuery();
        String q5 = s.setQuery("    a   b     c ").getSLProcessor().getDebugQuery();
        String q6 = s.setQuery("    ").getSLProcessor().getDebugQuery();

        System.out.println();
    }

    @Test
    public void TestQuotes() {

        SearchBuilder s = new SearchBuilder();

        String q0 = s.setQuery("\"").getSLProcessor().getDebugQuery();
        String q1 = s.setQuery("\"a b\"").getSLProcessor().getDebugQuery();
        String q2 = s.setQuery("\"a b\" c d").getSLProcessor().getDebugQuery();
        String q3 = s.setQuery("\" \\\" test quoted term in quotes \\\" \"").getSLProcessor().getDebugQuery();
        String q4 = s.setQuery("\" a \\\"").getSLProcessor().getDebugQuery();
        String q5 = s.setQuery("\" a and b \" \"c and d\"").getSLProcessor().getDebugQuery();
        String q6 = s.setQuery("\" a and  \"c and d\"").getSLProcessor().getDebugQuery();
        String q7 = s.setQuery("\" a and  \" c and d\"").getSLProcessor().getDebugQuery();
        String q8 = s.setQuery("    \"\"\" \"\"\"\\\"\"\" \"\" \\\"\\\" ").getSLProcessor().getDebugQuery();
        String q9 = s.setQuery("\"\" \" \"\"\"\\\"\"\" \"\" \\\" \\\"").getSLProcessor().getDebugQuery();
        String q10 = s.setQuery("\" \" \" \" \"").getSLProcessor().getDebugQuery();
        String q11 = s.setQuery("\" \\\" \\\" \\\" \"").getSLProcessor().getDebugQuery();
        String q12 = s.setQuery("\\\" \\\" \\\"").getSLProcessor().getDebugQuery();
        String q13 = s.setQuery("\\\" \\\" \" \\\"").getSLProcessor().getDebugQuery();

        System.out.println();

    }

    @Test
    public void TestAnd() {

        SearchBuilder s = new SearchBuilder();

        String q0 = s.setQuery("a AND b").getSLProcessor().getDebugQuery();
        String q1 = s.setQuery("a AND b AND c").getSLProcessor().getDebugQuery();
        String q2 = s.setQuery("AND a AND b AND c").getSLProcessor().getDebugQuery();
        String q3 = s.setQuery("AND AND AND").getSLProcessor().getDebugQuery();
        String q4 = s.setQuery("ANDANDAND").getSLProcessor().getDebugQuery();
        String q5 = s.setQuery("a AND bANDc ANDd").getSLProcessor().getDebugQuery();
        String q6 = s.setQuery("a ANDb c").getSLProcessor().getDebugQuery();

        System.out.println();

    }

    @Test
    public void TestOr() {

        SearchBuilder s = new SearchBuilder();

        String q0 = s.setQuery("a OR b").getSLProcessor().getDebugQuery();
        String q1 = s.setQuery("a OR b OR c").getSLProcessor().getDebugQuery();
        String q2 = s.setQuery("OR a OR b OR c").getSLProcessor().getDebugQuery();
        String q3 = s.setQuery("OR OR OR").getSLProcessor().getDebugQuery();
        String q4 = s.setQuery("OROROR").getSLProcessor().getDebugQuery();
        String q5 = s.setQuery("a OR bORc OR ORd").getSLProcessor().getDebugQuery();
        String q6 = s.setQuery("a ORb c").getSLProcessor().getDebugQuery();

        System.out.println();

    }

    @Test
    public void TestBoolean() {

        SearchBuilder s = new SearchBuilder();

        String q0 = s.setQuery("a OR b c").getSLProcessor().getDebugQuery();
        String q1 = s.setQuery("a AND b OR c d").getSLProcessor().getDebugQuery();
        String q2 = s.setQuery("OR AND OR OR AND OR").getSLProcessor().getDebugQuery();
        String q3 = s.setQuery("OR a AND b c OR d").getSLProcessor().getDebugQuery();
        String q4 = s.setQuery("c d OR e").getSLProcessor().getDebugQuery();

        System.out.println();

    }

    @Test
    public void TestKeyword() {

        SearchBuilder s = new SearchBuilder();
        //while (true) {

        String q0 = s.setQuery("author = a").getSLProcessor().getDebugQuery();
        String q1 = s.setQuery("author=a").getSLProcessor().getDebugQuery();
        String q2 = s.setQuery("author =a").getSLProcessor().getDebugQuery();
        String q3 = s.setQuery("author =a ").getSLProcessor().getDebugQuery();
        String q4 = s.setQuery("author = ").getSLProcessor().getDebugQuery();
        String q5 = s.setQuery("author = content = a").getSLProcessor().getDebugQuery();
        String q6 = s.setQuery("=").getSLProcessor().getDebugQuery();
        String q7 = s.setQuery("= a ").getSLProcessor().getDebugQuery();
        String q8 = s.setQuery("b == a ").getSLProcessor().getDebugQuery();
        String q9 = s.setQuery("= b = a = ").getSLProcessor().getDebugQuery();
        String q10 = s.setQuery("==== =").getSLProcessor().getDebugQuery();
        String q11 = s.setQuery("a = \"x y z\"").getSLProcessor().getDebugQuery();
        String q12 = s.setQuery("\"a b c\" = \"x y z\"").getSLProcessor().getDebugQuery();
        String q13 = s.setQuery("\"a b c\" = x").getSLProcessor().getDebugQuery();
        String q14 = s.setQuery("\"a b c\" = \"666\"  5 =").getSLProcessor().getDebugQuery();
        String q15 = s.setQuery("a = b = c = d =").getSLProcessor().getDebugQuery();


            String q16 = s.setQuery("= a = b = c = d =").getSLProcessor().getDebugQuery();
            System.out.printf("");
       // }


    //    System.out.println();

    }


    @Test
    public void TestFull() {

        SearchBuilder s = new SearchBuilder();

        String q0 = s.setQuery("\"a a\" \"a a\" \"b b\"").getSLProcessor().getDebugQuery();
        String q1 = s.setQuery("\"a a\" AND \"b b\" AND \"c c\"").getSLProcessor().getDebugQuery();
        String q2 = s.setQuery("OR \"a a\" AND b OR \"c c\"").getSLProcessor().getDebugQuery();
        System.out.println();

    }






}