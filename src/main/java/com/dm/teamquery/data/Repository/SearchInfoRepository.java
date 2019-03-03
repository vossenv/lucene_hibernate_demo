package com.dm.teamquery.data.repository;


import com.dm.teamquery.data.repository.Base.CustomRepository;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.entity.SearchInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface SearchInfoRepository extends PagingAndSortingRepository<Challenge, UUID>, CrudRepository<Challenge, UUID>, CustomRepository<Challenge, UUID> {


}



