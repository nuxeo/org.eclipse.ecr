/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     bstefanescu
 *
 * $Id: DocumentModelUpdater.java 29029 2008-01-14 18:38:14Z ldoguin $
 */

package org.eclipse.ecr.core.io.impl.plugins;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ecr.core.api.ClientException;
import org.eclipse.ecr.core.api.CoreSession;
import org.eclipse.ecr.core.api.DocumentLocation;
import org.eclipse.ecr.core.api.DocumentModel;
import org.eclipse.ecr.core.api.IdRef;
import org.eclipse.ecr.core.io.DocumentTranslationMap;
import org.eclipse.ecr.core.io.ExportedDocument;
import org.eclipse.ecr.core.io.impl.AbstractDocumentModelWriter;
import org.eclipse.ecr.core.io.impl.DocumentTranslationMapImpl;

/**
 * A writer that only updates existing documents. The doc ID is used to identity documents.
 * The imported tree structure is ignored.
 *
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
// TODO: improve it ->
// modify core session to add a batch create method and use it
public class DocumentModelUpdater extends AbstractDocumentModelWriter {

    private static final Log log = LogFactory.getLog(DocumentModelUpdater.class);

    /**
     * @param session the session to the repository where to write
     * @param parentPath where to write the tree. this document will be used as
     *            the parent of all top level documents passed as input. Note
     *            that you may have
     */
    public DocumentModelUpdater(CoreSession session, String parentPath) {
        super(session, parentPath);
    }

    public DocumentModelUpdater(CoreSession session, String parentPath,
            int saveInterval) {
        super(session, parentPath, saveInterval);
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Override
    public DocumentTranslationMap write(ExportedDocument xdoc) throws IOException {
        if (xdoc.getDocument() == null) {
            // not a valid doc -> this may be a regular folder for example the
            // root of the tree
            return null;
        }

        DocumentModel doc = null;
        String id = xdoc.getId();
        try {
            doc = session.getDocument(new IdRef(id));
        } catch (Exception e) {
            log.error("Cannot update document. No such document: "
                    + id);
            return null;
        }

        try {
            doc = updateDocument(xdoc, doc);
            DocumentLocation source = xdoc.getSourceLocation();
            DocumentTranslationMap map = new DocumentTranslationMapImpl(
                    source.getServerName(), doc.getRepositoryName());
            map.put(source.getDocRef(), doc.getRef());
            return map;
        } catch (ClientException e) {
            IOException ioe = new IOException(
                    "Failed to import document in repository: "
                            + e.getMessage());
            ioe.setStackTrace(e.getStackTrace());
            log.error(e);
            return null;
        }
    }

}
