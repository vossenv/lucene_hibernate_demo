package com.dm.teamquery;

import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.model.Challenge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestFullSearch {

    @Inject
    ChallengeService challengeService;

    @Test
    public void testSimpleSearch () {


        List<Challenge> result =  challengeService.search("");

        result = challengeService.search("a AND b");

        System.out.println();

    }

}