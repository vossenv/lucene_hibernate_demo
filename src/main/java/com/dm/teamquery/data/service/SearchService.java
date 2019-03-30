package com.dm.teamquery.data.service;

import com.dm.teamquery.entity.Challenge;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class SearchService {

    private final EntityManager entityManager;

    @Inject
    public SearchService(final EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public List<Challenge> search(String text) {

        text = "federated";

        List<String> terms = new ArrayList<>();

        terms.add("con");
        terms.add("federa");

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Challenge.class).get();

//
//        BooleanJunction b = queryBuilder.bool();
//
//        for (String t : terms) {
//
//            Query q = queryBuilder
//                    .keyword()
//                    .wildcard()
//                    .onFields("question", "answer", "author")
//                    .matching("*"+t+"*")
//                    .createQuery();
//
//            b = b.must(q);
//
//        }
//
//        Query query = b.createQuery();

//
//                queryBuilder.phrase()
//                .withSlop(10)
//                .onField("question")
//                .sentence(text)
//                .createQuery();

        Query query = queryBuilder
                .simpleQueryString()

                .onFields("challengeId","question", "answer", "author")
                .matching("ID + federated")
                .createQuery();

//        Query query = queryBuilder.keyword()
//                .wildcard()
//                .onFields("challengeId","question", "answer", "author")
//                .matching("\"")
//                .createQuery();


        // wrap Lucene query in an Hibernate Query object
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Challenge.class);

        // execute search and return results (sorted by relevance as default)
        List results = jpaQuery.getResultList();


        return results;
    } // method search
}
