package com.fitbit.api.client.subscription.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IGenericDao<T, ID extends Serializable> extends Serializable {
    boolean contains(T entity);

    T find(ID id);

    T getReference(ID id);

    void refresh(T entity);

    void persist(T entity);

    T merge(T entity);

    void remove(T entity);

    List<T> findAll();

    List<T> findByQuery(String queryString);

    List<T> findByQuery(String queryString, Object... values);

    List<T> findByQuery(String queryString, Map<String, ?> params);

    T findSingleByQuery(String queryString);

    T findSingleByQuery(String queryString, Object... values);

    T findSingleByQuery(String queryString, Map<String, ?> params);

    long getCount(String queryString);

    long getCount(String queryString, Object... values);

    long getCount(String queryString, Map<String, ?> params);

    void flush();

    List findCustomQuery(String queryString);

    List findCustomQuery(String queryString, Object... values);

    List findCustomQuery(String queryString, Map<String, ?> params);

    int executeQuery(String queryString);

    int executeQuery(String queryString, Object... values);

    int executeQuery(String queryString, Map<String, ?> params);
}