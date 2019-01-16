package com.dm.teamquery;


import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.search.Search;
import com.dm.teamquery.search.SearchBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestSearchPattern {

    @Inject SearchBuilder searchBuilder;


    @Test
    public void  TestBoolean() {

        Search s = new Search(Challenge.class, "\"x y\" z OR a AND b c AND d hello = someone goodbye = \"a wonder\" t u e");


        System.out.println();
    }



//    @Test
//    public void  TestBooleanAdd() {
//
//        searchBuilder.searchChallenges("\"x y\" z OR a AND b c AND d t u e");
//
//
//        result = searchBuilder.constructSearchMap("\"x y\" z OR a AND b c AND d AND e author=someone");
//        expected = getEmptyMap();
//        expected.get("terms").addAll(asList("x y", "z", "aANDb", "cANDdANDe"));
//        expected.get("author").add("someone");
//        assertEquals(expected, result);
//
//        result = searchBuilder.constructSearchMap("\"x y\" z a AND b c AND d AND e author=someone");
//        expected = getEmptyMap();
//        expected.get("terms").addAll(asList("x y", "z", "aANDb", "cANDdANDe"));
//        expected.get("author").add("someone");
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void TestBooleanOr() {
//
//        result = searchBuilder.constructSearchMap("a AND b OR c AND d author=someone");
//        expected = getEmptyMap();
//        expected.get("terms").addAll(asList("aANDb", "cANDd"));
//        expected.get("author").add("someone");
//        assertEquals(expected, result);
//
//        result = searchBuilder.constructSearchMap("x y z OR a AND b OR c AND d author=someone");
//        expected = getEmptyMap();
//        expected.get("terms").addAll(asList("x", "y", "z", "aANDb", "cANDd"));
//        expected.get("author").add("someone");
//        assertEquals(expected, result);
//
//        result = searchBuilder.constructSearchMap("\"x y\" z OR a AND b OR c AND d author=someone");
//        expected = getEmptyMap();
//        expected.get("terms").addAll(asList("x y", "z", "aANDb", "cANDd"));
//        expected.get("author").add("someone");
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void TestStandardFilter() {
//
//        result = searchBuilder.constructSearchMap("hello there author=someone");
//        expected = getEmptyMap();
//        expected.get("terms").addAll(asList("hello", "there"));
//        expected.get("author").add("someone");
//        assertEquals(expected, result);
//
//        result = searchBuilder.constructSearchMap("question=\"anyone else\" hello there \"new face\" author=someone");
//        expected = getEmptyMap();
//        expected.get("terms").addAll(asList("new face", "hello", "there"));
//        expected.get("author").add("someone");
//        expected.get("question").add("anyone else");
//        assertEquals(expected, result);
//    }
//
//    private Map<String, List<String>> getEmptyMap(){
//        Map<String, List<String>> searchPatterns = new HashMap<>();
//        fieldNames.forEach(k -> searchPatterns.put(k, new ArrayList<>()));
//        return searchPatterns;
//    }
}
