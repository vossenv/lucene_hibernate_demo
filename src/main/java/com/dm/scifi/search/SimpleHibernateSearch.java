package com.dm.scifi.search;

import com.dm.scifi.entity.Movie;
import lombok.Setter;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class SimpleHibernateSearch<T> {

    @Setter
    private Class entityType;
    private FullTextEntityManager fullTextEm;

    @Inject
    public SimpleHibernateSearch(EntityManagerFactory emf) {
        this.fullTextEm = Search.getFullTextEntityManager(emf.createEntityManager());
    }

    public List<T> search(SearchParameters sp){
        return parseQuery(sp).getResultList();
    }

    public int count(SearchParameters sp){
        return parseQuery(sp).getResultSize();
    }

    private FullTextQuery parseQuery(SearchParameters parameters)  {

        String query = parameters.getQuery();
        Pageable p = parameters.getPageable();

        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(Movie.class).get();

        Query luceneQuery = qb
                .simpleQueryString()
                .onFields("title", "description", "catchPhrase", "producer", "genre", "country", "rating")
                .matching(query)
                .createQuery();

        FullTextQuery jpaQuery = fullTextEm.createFullTextQuery(luceneQuery, Movie.class);

        List<Movie> results = jpaQuery.getResultList();
        return jpaQuery.setMaxResults(p.getPageSize()).setFirstResult(p.getPageNumber() * p.getPageSize());
    }
}


