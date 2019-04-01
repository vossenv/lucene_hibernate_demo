package com.dm.teamquery.test;


import com.dm.teamquery.data.service.SearchService;
import com.dm.teamquery.entity.Challenge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.List;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestLuceneSearch {

    @Inject
    SearchService searchService;

    @Test
    public void notests() throws Exception{


        List<Challenge> l = searchService.search("", Challenge.class);

        System.out.println();


    }

    @Test
    public void TestSLProcessor(){



    }
}
