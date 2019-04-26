package com.dm.scifi.config;


import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


@Configuration
public class LuceneIndexConfiguration {

    private final EntityManager entityManager;

    @Autowired
    public LuceneIndexConfiguration(final EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @PostConstruct
    public void onApplicationEvent() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager =  Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }
}