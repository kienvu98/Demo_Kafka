package com.microservices.demo.dataccess.repositoty;

import java.util.Collection;

/*
    Spring data custom CRUD reposity for entity
 */
public interface AnalyticsCustomRepository<T, PK> {
    <S extends T> PK prersist(S entity);
    <S extends T> void batchPrersist(Collection<S> entity);
    <S extends T> S merge(S entity);
    <S extends T> void batchMerge(Collection<S> entity);
    void clear();

}
