package com.dm.teamquery.data.repository;


import com.dm.teamquery.data.repository.custom.TeamQueryRepository;
import com.dm.teamquery.entity.SearchInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface SearchInfoRepository extends CrudRepository<SearchInfo, UUID>, TeamQueryRepository<SearchInfo, UUID> {


}



