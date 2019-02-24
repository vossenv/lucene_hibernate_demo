package com.dm.teamquery.data.Repository;


import com.dm.teamquery.entity.SearchInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface SearchInfoRepository extends PagingAndSortingRepository<SearchInfo, UUID>, CrudRepository<SearchInfo, UUID> {

}



