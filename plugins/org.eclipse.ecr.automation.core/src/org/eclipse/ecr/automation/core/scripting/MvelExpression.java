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
 */
package org.eclipse.ecr.automation.core.scripting;

import java.io.Serializable;

import org.eclipse.ecr.automation.OperationContext;
import org.mvel2.MVEL;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class MvelExpression implements Expression {

    private static final long serialVersionUID = 1L;

    protected transient volatile Serializable compiled;

    protected final String expr;

    public MvelExpression(String expr) {
        this.expr = expr;
    }

    public Object eval(OperationContext ctx) throws Exception {
        if (compiled == null) {
            compiled = MVEL.compileExpression(expr);
        }
        return MVEL.executeExpression(compiled, Scripting.initBindings(ctx));
    }

}