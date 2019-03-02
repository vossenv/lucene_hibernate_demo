package com.dm.teamquery;


import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.EntityUpdateException;
import com.dm.teamquery.execption.SearchFailedException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.UUID;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestCustomRepository {

    @Inject
    ChallengeService challengeService;

    @Inject
    ChallengeRepository cr;

    @Test
    public void TestUpdate() throws EntityUpdateException, SearchFailedException, Exception {

        Challenge c = cr.findAll().iterator().next();

        try {



        } catch ( Exception e ){
            System.out.println();
        }

    }

    @Test
    public void TestAdd() throws EntityUpdateException {
        Challenge c = new Challenge();
    }

    @Test
    public void TestDelete() throws  Exception {

        UUID id  = challengeService.basicSearch("").get(0).getChallengeId();
        cr.deleteEntityById(id);
        assertFalse(cr.existsEntity(id));

        Assertions.assertThrows(EntityNotFoundException.class, () -> challengeService.deleteChallengeById(id));
        Assertions.assertThrows(EntityNotFoundException.class, () -> challengeService.deleteChallengeById(UUID.randomUUID()));
        Assertions.assertThrows(EntityNotFoundException.class, () -> challengeService.deleteChallengeById(null));
    }


}
