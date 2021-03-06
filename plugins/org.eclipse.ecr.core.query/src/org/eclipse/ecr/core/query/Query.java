/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bogdan Stefanescu
 *     Florent Guillaume
 */

package org.eclipse.ecr.core.query;

/**
 * @author Bogdan Stefanescu
 * @author Florent Guillaume
 */
public interface Query {

    /**
     * Defines general query types.
     * <p>
     * There could be Query implementations for one or another Query Type. If
     * the query factory instantiating a specific implementation of this class
     * does not support a given Query Type than a {@code
     * UnsupportedQueryTypeException} should be thrown.
     */
    enum Type {
        NXQL("NXQL"), XPATH("XPATH");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Query#Type: " + name;
        }
    }

    /**
     * Makes a query to the backend. No filter, permission or policy filtering
     * are done.
     *
     * @return a query result object describing the resulting documents
     * @throws QueryException
     * @see {@link FilterableQuery#execute(QueryFilter,boolean)}
     */
    QueryResult execute() throws QueryException;

    /**
     * Makes a query to the backend. No filter, permission or policy filtering
     * are done.
     * <p>
     * The total number of documents can also be retrieved, it is then stored in
     * the {@link DocumentModelList} returned by
     * {@link QueryResult#getDocumentModels}.
     *
     * @param countTotal if {@code true}, also count the total number of
     *            documents when no limit/offset is passed
     * @return a query result object describing the resulting documents
     * @throws QueryException
     * @see {@link FilterableQuery#execute(QueryFilter,boolean)}
     */
    QueryResult execute(boolean countTotal) throws QueryException;

}
