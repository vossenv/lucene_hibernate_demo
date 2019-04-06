package com.dm.teamquery.data.service;

import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.search.SLProcessor;
import lombok.SneakyThrows;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;

@Repository
@Transactional
public class SearchService {

    private FullTextEntityManager fullTextEm;

    @Inject
    public SearchService(EntityManagerFactory emf) {
        this.fullTextEm = Search.getFullTextEntityManager(emf.createEntityManager());
    }

    @SneakyThrows
    public List<Challenge> search(String query, Class entityType) {

        MultiFieldQueryParser queryParser =
                new MultiFieldQueryParser(getEntityFields(entityType),
                fullTextEm.getSearchFactory().getAnalyzer(entityType));
        
        queryParser.setAllowLeadingWildcard(true);

        query = new SLProcessor().format(query);
        if (query.isEmpty()) return new ArrayList<>();

        try {
            Query r = queryParser.parse(query);
            FullTextQuery jpaQuery = fullTextEm.createFullTextQuery(r, entityType);
            List results = jpaQuery.getResultList();
            return results;
        } catch (ParseException e) {
           throw new SearchException(e.getMessage().replace("~",""));
        }
    }

    private String [] getEntityFields (Class c){
        Set<String> fieldNames = new HashSet<>();
        while (c != null){
            stream(c.getDeclaredFields()).map(Field::getName).forEach(fieldNames::add);
            c = c.getSuperclass();
        }        
        return fieldNames.toArray(new String[0]);
    }
}
