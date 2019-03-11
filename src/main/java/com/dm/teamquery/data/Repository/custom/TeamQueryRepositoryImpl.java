package com.dm.teamquery.data.repository.custom;

import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.TeamQueryException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

public class TeamQueryRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements TeamQueryRepository<T, ID>{

    private final EntityManager em;

    public TeamQueryRepositoryImpl(JpaEntityInformation entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;
    }

    @Override
    public List<T> findAll() {
       return em.createQuery("from " + getDomainClass().getSimpleName()).getResultList();
    }

    @Override
    public T findByIdThrows(ID id) throws TeamQueryException {
        try {
            return super.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException(e.getStackTrace(), "No entity was found for id: " + id.toString());
        }
    }

    @Override
    public List<T> search(String query, Pageable p) {
        return em
                .createQuery(query)
                .setMaxResults(p.getPageSize())
                .setFirstResult(p.getPageNumber() * p.getPageSize())
                .getResultList();
    }

    @Override
    public long count (String query) {
        return (long) em.createQuery("select count(*) " + query).getResultList().get(0);
    }
}