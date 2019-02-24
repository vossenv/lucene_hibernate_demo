package com.dm.teamquery.data.Repository;

import com.dm.teamquery.entity.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface ChallengeRepository extends PagingAndSortingRepository<Challenge, UUID>, CrudRepository<Challenge, UUID> {

}



