package com.dm.teamquery.test;

import com.dm.teamquery.data.service.ChallengeService;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.customexception.SearchFailedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.List;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class TestFullSearch {

    @Inject
    ChallengeService cs;

    @Test
    void testSimpleSearch () throws SearchFailedException {

       List<Challenge> challengeList0 = cs.basicSearch("sso");
       List<Challenge> challengeList = cs.basicSearch("federated");
       List<Challenge> challengeList1 = cs.basicSearch("*ID*");
       List<Challenge> challengeList2 = cs.basicSearch("phonybalogna@yourdomain.com");
       List<Challenge> challengeList3 = cs.basicSearch("*adobe*");
       List<Challenge> challengeList31 = cs.basicSearch("");
       List<Challenge> challengeList4 = cs.basicSearch("*http*");
       List<Challenge> challengeList5 = cs.basicSearch("ID AND federated");
       List<Challenge> challengeList6 = cs.basicSearch("\"to the individual\"");
       List<Challenge> challengeList7 = cs.basicSearch("question:\"users\" AND adobe");
       List<Challenge> challengeList8 = cs.basicSearch("question:\"my users\"");
       List<Challenge> challengeList9 = cs.basicSearch("http");
       List<Challenge> challengeList10 = cs.basicSearch("*@*");

        System.out.println();

//        assertEquals(cs.basicSearch("").size(), 17);
//        assertEquals(cs.basicSearch("question: adobe").size(), 1);
//        assertEquals(cs.basicSearch("question: adobe OR author: rhianna").size(), 17);
//        assertEquals(cs.basicSearch("37766f9a-9a5a-47d2-a22f-701986cb4d7f").size(), 1);
//        assertEquals(cs.basicSearch("ID").size(), 4);
//        assertEquals(cs.basicSearch("*ID*").size(), 8);
//        assertEquals(cs.basicSearch("ID AND federated").size(), 4);
//        assertEquals(cs.basicSearch("\"to the individual\"").size(), 1);
//        assertEquals(cs.basicSearch("question:\"users\" AND adobe").size(), 1);
//        assertEquals(cs.basicSearch("question:\"my users\"").size(), 1);
    }

}