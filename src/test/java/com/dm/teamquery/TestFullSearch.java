package com.dm.teamquery;

import com.dm.teamquery.data.service.ChallengeService;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.customexception.SearchFailedException;
import com.dm.teamquery.data.SearchRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
public class TestFullSearch {

    @Inject
    ChallengeService cs;

    @Test
    public void testSimpleSearch () throws SearchFailedException {

        assertEquals(cs.basicSearch("").size(), 16);
        assertEquals(cs.basicSearch("question = adobe").size(), 1);
        assertEquals(cs.basicSearch("question = adobe OR author = rhianna").size(), 1);
        assertEquals(cs.basicSearch("37766f9a-9a5a-47d2-a22f-701986cb4d7f").size(), 1);
        assertEquals(cs.basicSearch("ID").size(), 8);
        assertEquals(cs.basicSearch("ID AND federated").size(), 4);
        assertEquals(cs.basicSearch("\"to the individual\"").size(), 1);
        assertEquals(cs.basicSearch("question=\"users\" AND e").size(), 2);
    }

    @Test
    public void testPagedSearch () throws SearchFailedException {

        List<Object> results = new ArrayList<>();

        for (int i = 0; i < 15; i ++ ){

            SearchRequest sr = new SearchRequest();
            sr.setPageable(PageRequest.of(i,5));
            List<?> nextPage = cs.search(sr).getResultsList();

            if (nextPage.size() == 0) {
                assertEquals(4, i);
                break;
            }
            results.addAll(nextPage);
            if (i < 3) assertEquals(5, nextPage.size());
        }

        assertEquals(results.size(), 16);
    }

    @Test
    public void testGetDisabled() throws SearchFailedException {

        SearchRequest sr = new SearchRequest();
        sr.setIncDisabled(true);

        List<?> disabledResults = cs.search(sr).getResultsList();
        assertEquals(1, disabledResults.size());
        assertEquals(((Challenge) disabledResults.get(0)).getEnabled(), false);
    }
}