package com.dm.teamquery.data;

import com.dm.teamquery.Execption.BadEntityException;
import com.dm.teamquery.model.SearchEntity;
import com.dm.teamquery.model.SearchEntity;
import com.dm.teamquery.search.Search;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SearchService {


    @Inject
    private SearchRepository searchRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public void deleteSearchEntityById(String id) throws BadEntityException{
        try {
            searchRepository.deleteById(UUID.fromString(id));
        } catch (Exception e) {
            throw new BadEntityException(e.getMessage());
        }
    }

    public List<SearchEntity> search (String query){
        return search(query, PageRequest.of(0,100));
    }

    public List<SearchEntity> search (String query, Pageable p){

        List<SearchEntity> results = new ArrayList<>();
        String dbQuery = new Search(SearchEntity.class, query).getDatabaseQuery();
        SearchEntity entity = new SearchEntity(query, dbQuery);

        try {
            results = entityManager
                    .createQuery(dbQuery)
                    .setMaxResults(p.getPageSize())
                    .setFirstResult(p.getPageNumber() * p.getPageSize())
                    .getResultList();
        } catch (Exception e) {
            entity.setErrors(e.getMessage());
        }

        try {
            searchRepository.save(entity);
        } catch (Exception e) {
            // Do nothing -- non-critical function.  Add logging later
        }
        return results;
    }
}
