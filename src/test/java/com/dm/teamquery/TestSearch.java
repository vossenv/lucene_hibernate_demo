package com.dm.teamquery;


import com.dm.teamquery.data.ChallengeRepository;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchEngine;
import com.dm.teamquery.model.Challenge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestSearch {

    @Inject ChallengeService challengeService;
    @Inject ChallengeRepository challengeRepository;
    @Inject SearchEngine searchEngine;

    private List<String> keyWords = new ArrayList<>();
    private Map<String, List<String>> expected;
    private Map<String, List<String>> result;

    @PostConstruct
    private void buildSearchKeywordList() {
        asList(Challenge.class.getDeclaredFields()).forEach(f -> keyWords.add(f.getName().toLowerCase()));
        keyWords.add("terms");
    }

    @Test
    public void  TestBooleanAdd() {

        result = searchEngine.constructSearchMap("\"x y\" z OR a AND b c AND d AND e author=someone");
        expected = getEmptyMap();
        expected.get("terms").addAll(asList("x y", "z", "aANDb", "cANDdANDe"));
        expected.get("author").add("someone");
        assertEquals(expected, result);

        result = searchEngine.constructSearchMap("\"x y\" z a AND b c AND d AND e author=someone");
        expected = getEmptyMap();
        expected.get("terms").addAll(asList("x y", "z", "aANDb", "cANDdANDe"));
        expected.get("author").add("someone");
        assertEquals(expected, result);
    }

    @Test
    public void TestBooleanOr() {

        result = searchEngine.constructSearchMap("a AND b OR c AND d author=someone");
        expected = getEmptyMap();
        expected.get("terms").addAll(asList("aANDb", "cANDd"));
        expected.get("author").add("someone");
        assertEquals(expected, result);

        result = searchEngine.constructSearchMap("x y z OR a AND b OR c AND d author=someone");
        expected = getEmptyMap();
        expected.get("terms").addAll(asList("x", "y", "z", "aANDb", "cANDd"));
        expected.get("author").add("someone");
        assertEquals(expected, result);

        result = searchEngine.constructSearchMap("\"x y\" z OR a AND b OR c AND d author=someone");
        expected = getEmptyMap();
        expected.get("terms").addAll(asList("x y", "z", "aANDb", "cANDd"));
        expected.get("author").add("someone");
        assertEquals(expected, result);
    }

    @Test
    public void TestStandardFilter() {

        result = searchEngine.constructSearchMap("hello there author=someone");
        expected = getEmptyMap();
        expected.get("terms").addAll(asList("hello", "there"));
        expected.get("author").add("someone");
        assertEquals(expected, result);

        result = searchEngine.constructSearchMap("question=\"anyone else\" hello there \"new face\" author=someone");
        expected = getEmptyMap();
        expected.get("terms").addAll(asList("new face", "hello", "there"));
        expected.get("author").add("someone");
        expected.get("question").add("anyone else");
        assertEquals(expected, result);

    }

    private Map<String, List<String>> getEmptyMap(){

        Map<String, List<String>> searchPatterns = new HashMap<>();
        keyWords.forEach(k -> searchPatterns.put(k, new ArrayList<>()));
        return searchPatterns;
    }
}
