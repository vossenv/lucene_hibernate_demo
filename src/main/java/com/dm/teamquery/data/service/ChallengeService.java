package com.dm.teamquery.data.service;

import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.data.repository.SearchInfoRepository;
import com.dm.teamquery.entity.Challenge;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.UUID;

@Component
public class ChallengeService extends TeamQueryService<Challenge, UUID> {
    ChallengeService(EntityManager em, ChallengeRepository cr){
        super(em, cr);
    }
}
