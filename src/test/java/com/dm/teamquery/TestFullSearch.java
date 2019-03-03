package com.dm.teamquery;

import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.generic.SearchRequest;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.SearchFailedException;
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
    public void testSimpleSearch () throws SearchFailedException {

//        Search s = new Search(Challenge.class);
//        String query = s.setQuery("question = adobe OR author = rhianna").getDatabaseQuery();
//        List<Challenge> cl = challengeService.basicSearch("question=\"users\" AND e");

        assertEquals(challengeService.basicSearch("").size(), 17);
        assertEquals(challengeService.basicSearch("question = adobe").size(), 1);
        assertEquals(challengeService.basicSearch("question = adobe OR author = rhianna").size(), 1);
        assertEquals(challengeService.basicSearch("37766f9a-9a5a-47d2-a22f-701986cb4d7f").size(), 1);
        assertEquals(challengeService.basicSearch("ID").size(), 8);
        assertEquals(challengeService.basicSearch("ID AND federated").size(), 4);
        assertEquals(challengeService.basicSearch("\"to the individual\"").size(), 1);
        assertEquals(challengeService.basicSearch("question=\"users\" AND e").size(), 2);
    }

    @Test
    public void testPagedSearch () throws SearchFailedException {

        List<Object> results = new ArrayList<>();

        for (int i = 0; i < 15; i ++ ){

            SearchRequest sr = new SearchRequest();
            sr.setPageable(PageRequest.of(i,5));
            List<?> nextPage = challengeService.search(sr).getResultsList();

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
    public void testGetDisabled() throws SearchFailedException {

        SearchRequest sr = new SearchRequest();
        sr.setIncDisabled(true);
        
        List<?> disabledResults = challengeService.search(sr).getResultsList();
        assertEquals(1, disabledResults.size());
        assertEquals(((Challenge) disabledResults.get(0)).getEnabled(), false);
    }
}