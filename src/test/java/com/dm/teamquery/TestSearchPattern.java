package com.dm.teamquery;


import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.search.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestSearchPattern {


    @Test
    public void TestSimple() {

        Search s = new Search(Challenge.class);

        chkSet(s.setQuery("a"), "[a]");
        chkSet(s.setQuery("a b"), "[a, b]");
        chkSet(s.setQuery("\"a b\""), "[a b]");
        chkSet(s.setQuery("\"a b\" c d"), "[a b, c, d]");

        chkSet(s.setQuery("a OR    b"), "[a, b]");
        chkSet(s.setQuery("a OR c   b"), "[a, b, c]");

    }

    @Test
    public void TestAnd() {

        Search s = new Search(Challenge.class);
        String and = s.getAND_HOLDER();

        chkSet(s.setQuery("a AND b"), "[a" + and + "b]");
        chkSet(s.setQuery("aANDa b AND c "), "[b" + and + "c, aANDa]");
        chkSet(s.setQuery("\"a b\" AND e c"), "[c, a b" + and + "e]");
        chkSet(s.setQuery("a AND b AND e OR c AND d"), "[a" + and + "b" + and + "e, c" + and + "d]");
        chkSet(s.setQuery("\"a AND b\""), "[a AND b]");
        chkSet(s.setQuery("\"a b\" AND c d AND p"), "[d" + and + "p, a b" + and + "c]");
        chkSet(s.setQuery("f a AND c OR d AND p"), "[d" + and + "p, f, a" + and + "c]");

        chkSet(s.setQuery("\"x y\" z OR a AND b c AND d hello = someone goodbye = \"a    wonder\" t u e"),
                "[x y, t, u, e, hello=someone, z, goodbye=a    wonder, c" + and + "d, a" + and + "b]");
    }

    private void chkSet(Search s, String expected) {
        assertEquals(s.getSearchTerms().toString(), expected);
    }

}