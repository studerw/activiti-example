package com.studerw.activiti.dao;

import com.studerw.activiti.web.PagingCriteria;

import java.util.List;

/**
 * BaseDao interface which all other interfaces should impmlement
 *
 * @param <T>
 * @author studerw
 */
public interface IBaseDao<T> {

    /**
     * @return count of all objects in the backend.
     */
    public int getCount();

    /**
     * Get all objects sorted using their native {@code compares} method.
     *
     * @return List (possibly empty) of all objects in the backend listed native sort order of type
     */
    public List<T> readAll();

    /**
     * Get a page of objects. Versioning of objects is undefined as some backends may not have a concept of versioned rows.
     *
     * @return List (possibly empty) of all objects in the backend listed by their native sort order.
     */
    public List<T> readPage(PagingCriteria criteria);

    /**
     * If the object to create already has an ID set, an {@link IllegalArgumentException} will be thrown as it is the responsibility
     * of the DAO implementation classes to deal with the ID for its own persistence backend.
     * <p/>
     * <p>The object passed into the method will have its ID set by the time the call returns.</p>
     *
     * @param obj without an ID
     * @return ID of newly created object
     */
    public String create(T obj);

    /**
     * This is really a one-off usable method that allows creation of the object with the ID to be set.
     * Currently used when inserting test data into Accumulo taken from Postgres / HSQL
     * See the unit tests in {@code mil.army.inscom.tcop.util.XmlToAccumuloTestjava} and {@code DbToXmlTestTest.java}
     */
    public void createWithId(T obj);

    /**
     * persist updates to a given Object.
     * If the object to create already does not already have an ID set, an {@link IllegalArgumentException} will be thrown
     * as it is the responsibility of the DAO implementation classes to deal with the ID for its own persistence backend.
     *
     * @param obj
     */
    public void update(T obj);

    /**
     * Remove a Object with passed ID.
     * Implementations shouldn't cascade deletes, and instead should let service methods use multiple daos
     *
     * @param id Object ID
     */
    public void delete(String id);

    /**
     * @param id of object ID
     * @return Object associated with given primary key ID.
     *         <p/>
     *         An unchecked excpetion will be thrown if the object with the given id does not exist.
     */
    public T read(String id);

}
