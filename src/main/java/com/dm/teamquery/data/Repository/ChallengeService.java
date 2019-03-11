package com.dm.teamquery.data.repository;

import com.dm.teamquery.data.repository.custom.ABsTest;
import com.dm.teamquery.entity.Challenge;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChallengeService extends ABsTest<Challenge, UUID>{

    ChallengeService(ChallengeRepository challengeRepository){
        super(challengeRepository);
    }
}
