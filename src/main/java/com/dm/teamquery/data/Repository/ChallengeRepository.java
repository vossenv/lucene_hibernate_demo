package com.dm.teamquery.data.repository;

import com.dm.teamquery.data.repository.custom.TeamQueryRepository;
import com.dm.teamquery.entity.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface ChallengeRepository extends CrudRepository<Challenge, UUID>, TeamQueryRepository<Challenge, UUID> {

}



