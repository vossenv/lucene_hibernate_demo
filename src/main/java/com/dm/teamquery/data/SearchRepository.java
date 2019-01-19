package com.dm.teamquery.data;


import com.dm.teamquery.model.SearchEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface SearchRepository extends PagingAndSortingRepository<SearchEntity, UUID>, CrudRepository<SearchEntity, UUID> {

}



