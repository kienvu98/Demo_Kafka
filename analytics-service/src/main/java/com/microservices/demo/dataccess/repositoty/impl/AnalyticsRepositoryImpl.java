package com.microservices.demo.dataccess.repositoty.impl;

import com.microservices.demo.dataccess.entity.BaseEntity;
import com.microservices.demo.dataccess.repositoty.AnalyticsCustomRepository;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

public class AnalyticsRepositoryImpl<T extends BaseEntity<PK>, PK> implements AnalyticsCustomRepository<T, PK> {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsRepositoryImpl.class);

    @PersistenceContext
    protected EntityManager en;

    @Value("$(spring.jpa.properties.hibernate.jdbc.batch_size:50)")
    protected int batchSize;

    @Override
    @Transactional
    public <S extends T> PK prersist(S entity) {
        this.en.persist(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public <S extends T> void batchPrersist(Collection<S> entity) {
        if (entity.isEmpty()){
            LOG.info("No entity found to insert");
            return;
        }
        int batchCnt = 0;
        for (S e : entity) {
            LOG.trace("Persisting entity with id {}", e.getId());
            this.en.persist(e);
            batchCnt ++;
            if (batchCnt % batchSize == 0) {
                this.en.flush();
                this.en.clear();
            }
        }
        if (batchCnt % batchSize != 0) {
            this.en.flush();
            this.en.clear();
        }
    }

    @Override
    @Transactional
    public <S extends T> S merge(S entity) {
        return this.en.merge(entity);
    }

    @Override
    @Transactional
    public <S extends T> void batchMerge(Collection<S> entity) {
        if (entity.isEmpty()) {
            LOG.info("No entity found to insert");
            return;
        }
        int batchCnt = 0;
        for (S s : entity) {
            LOG.trace("Merging entity with id {}", s.getId());
            this.en.merge(s);
            batchCnt ++;
            if (batchCnt % batchSize == 0) {
                this.en.flush();
                this.en.clear();
            }
        }
        if (batchCnt % batchSize != 0) {
            this.en.flush();
            this.en.clear();
        }
    }

    @Override
    public void clear() {
        this.en.clear();
    }
}
