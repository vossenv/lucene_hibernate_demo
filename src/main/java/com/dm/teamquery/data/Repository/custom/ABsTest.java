package com.dm.teamquery.data.repository.custom;

import lombok.Setter;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;

public abstract class ABsTest<T, ID> {

    @Setter
    private CrudRepository simpleJpaRepository;

    public void test(T t){
        this.simpleJpaRepository.save(t);
    }

    public ABsTest(CrudRepository simpleJpaRepository) {
        this.simpleJpaRepository = simpleJpaRepository;
    }
}
