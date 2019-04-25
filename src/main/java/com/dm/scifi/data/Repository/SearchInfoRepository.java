package com.dm.scifi.data.repository;


import com.dm.scifi.entity.SearchInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface SearchInfoRepository extends PagingAndSortingRepository<SearchInfo, UUID> {


}



