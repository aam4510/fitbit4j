package com.fitbit.api.client.subscription.dao.impl;

import com.fitbit.api.client.subscription.dao.IGenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public abstract class GenericDao<T, ID extends Serializable> implements IGenericDao<T, ID> {
    private static final Logger log = LoggerFactory.getLogger(GenericDao.class);

    private Class<T> persistentClass;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings({"unchecked"})
    public GenericDao() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (log.isInfoEnabled()) {
            log.info(String.format("New instance of Dao for '%s' class is created", persistentClass.getSimpleName()));
        }
    }

    public boolean contains(T entity) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.contains() with entity: %s", persistentClass.getSimpleName(), entity));
        }
        return entityManager.contains(entity);
    }


    public T find(ID id) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.find() with id: %s", persistentClass.getSimpleName(), id));
        }
        return entityManager.find(persistentClass, id);
    }

    public T getReference(ID id) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.getReference() with id: %s", persistentClass.getSimpleName(), id));
        }
        return entityManager.getReference(persistentClass, id);
    }


    public void refresh(T entity) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.refresh().", persistentClass.getSimpleName()));
        }
        entityManager.refresh(entity);
    }


    public void persist(T entity) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.persist() with entity: %s", persistentClass.getSimpleName(), entity));
        }
        entityManager.persist(entity);
    }

    public T merge(T entity) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.merge() with entity: %s", persistentClass.getSimpleName(), entity));
        }
        return entityManager.merge(entity);
    }


    public void remove(T entity) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.remove() with entity: %s", persistentClass.getSimpleName(), entity));
        }
        entityManager.remove(entity);
    }


    public List<T> findAll() {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Calling %sDao.findAll()", persistentClass.getSimpleName()));
        }
        return findByQuery("from " + persistentClass.getName());
    }


    @SuppressWarnings({"unchecked"})
    public List<T> findByQuery(String queryString) {
        return entityManager.createQuery(queryString).getResultList();
    }


    @SuppressWarnings({"unchecked"})
    public List<T> findByQuery(String queryString, Object... values) {
        Query query = entityManager.createQuery(queryString);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i + 1, values[i]);
        }
        return query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<T> findByQuery(String queryString, Map<String, ?> params) {
        Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public T findSingleByQuery(String queryString) {
        try {
            return (T) entityManager.createQuery(queryString).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings({"unchecked"})
    public T findSingleByQuery(String queryString, Object... values) {
        Query query = entityManager.createQuery(queryString);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i + 1, values[i]);
        }
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings({"unchecked"})
    public T findSingleByQuery(String queryString, Map<String, ?> params) {
        Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long getCount(String queryString) {
        if (queryString.toLowerCase().startsWith("from")) {
            queryString = "SELECT COUNT(*) " + queryString;
        }
        try {
            return (Long) entityManager.createQuery(queryString).getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public long getCount(String queryString, Object... values) {
        if (queryString.toLowerCase().startsWith("from")) {
            queryString = "SELECT COUNT(*) " + queryString;
        }

        Query query = entityManager.createQuery(queryString);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i + 1, values[i]);
        }
        try {
            return (Long) query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public long getCount(String queryString, Map<String, ?> params) {
        if (queryString.toLowerCase().startsWith("from")) {
            queryString = "SELECT COUNT(*) " + queryString;
        }

        Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        try {
            return (Long) query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public void flush() {
        entityManager.flush();
    }


    public int executeQuery(String queryString) {
        Query query = entityManager.createQuery(queryString);
        return query.executeUpdate();
    }

    public int executeQuery(String queryString, Object... values) {
        Query query = entityManager.createQuery(queryString);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i + 1, values[i]);
        }
        return query.executeUpdate();
    }

    public int executeQuery(String queryString, Map<String, ?> params) {
        Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.executeUpdate();
    }

    public List findCustomQuery(String queryString) {
        if (log.isDebugEnabled()) {
            // TODO: parameters enumeration
            log.debug(String.format("Calling %sDao.findByQuery() with queryString: %s", persistentClass.getSimpleName(), queryString));
        }
        return entityManager.createQuery(queryString).getResultList();
    }


    public List findCustomQuery(String queryString, Object... values) {
        return findByQuery(queryString, values);
    }

    public List findCustomQuery(String queryString, Map<String, ?> params) {
        return findByQuery(queryString, params, null);
    }
}
