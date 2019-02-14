package com.dm.teamquery;

import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.search.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestFullSearch {

    @Inject
    ChallengeService challengeService;

    @Test
    public void testSimpleSearch () {

        Search s = new Search(Challenge.class);

        String query = s.setQuery("question = adobe").getDatabaseQuery();

        assertEquals(challengeService.search("").getResultsList().size(), 17);
        assertEquals(challengeService.search("question = adobe").getResultsList().size(), 1);
        assertEquals(challengeService.search("question = adobe OR author = rhianna").getResultsList().size(), 17);
        assertEquals(challengeService.search("37766f9a-9a5a-47d2-a22f-701986cb4d7f").getResultsList().size(), 1);
        assertEquals(challengeService.search("ID").getResultsList().size(), 8);
        assertEquals(challengeService.search("ID AND federated").getResultsList().size(), 4);
        assertEquals(challengeService.search("\"to the individual\"").getResultsList().size(), 1);
        assertEquals(challengeService.search("question=\"users\" AND e").getResultsList().size(), 2);
    }

    @Test
    public void testPagedSearch (){

        List<Challenge> results = new ArrayList<>();

        for (int i = 0; i < 15; i ++ ){

            List<Challenge> nextPage = challengeService.search("", PageRequest.of(i, 5)).getResultsList();

            if (nextPage.size() == 0) {
                assertEquals(4, i);
                break;
            }
            results.addAll(nextPage);
            if (i < 3) assertEquals(5, nextPage.size());
        }

        assertEquals(results.size(), 17);
    }

    @Test
    public void testGetDisabled() {

        List<Challenge> disabledResults = challengeService.search("", true).getResultsList();
        assertEquals(1, disabledResults.size());
        assertEquals(disabledResults.get(0).getEnabled(), false);
    }
}