/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
/*
 * @(#)MessagingException.java        1.10 02/06/13
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.xml.messaging.saaj.packaging.mime;


/**
 * The base class for all exceptions thrown by the Messaging classes
 *
 * @author John Mani
 * @author Bill Shannon
 */

public class MessagingException extends Exception {

    /**
     * The next exception in the chain.
     *
     * @serial
     */
    private Exception next;

    /**
     * Constructs a MessagingException with no detail message.
     */
    public MessagingException() {
	super();
    }

    /**
     * Constructs a MessagingException with the specified detail message.
     * @param s		the detail message
     */
    public MessagingException(String s) {
	super(s);
    }

    /**
     * Constructs a MessagingException with the specified 
     * Exception and detail message. The specified exception is chained
     * to this exception.
     * @param s		the detail message
     * @param e		the embedded exception
     * @see	#getNextException
     * @see	#setNextException
     */
    public MessagingException(String s, Exception e) {
	super(s);
	next = e;
    }

    /**
     * Get the next exception chained to this one. If the
     * next exception is a MessagingException, the chain
     * may extend further.
     *
     * @return	next Exception, null if none.
     */
    public Exception getNextException() {
	return next;
    }

    /**
     * Add an exception to the end of the chain. If the end
     * is <strong>not</strong> a MessagingException, this 
     * exception cannot be added to the end.
     *
     * @param	ex	the new end of the Exception chain
     * @return		<code>true</code> if the this Exception
     *			was added, <code>false</code> otherwise.
     */
    public synchronized boolean setNextException(Exception ex) {
	Exception theEnd = this;
	while (theEnd instanceof MessagingException &&
	       ((MessagingException)theEnd).next != null) {
	    theEnd = ((MessagingException)theEnd).next;
	}
	// If the end is a MessagingException, we can add this 
	// exception to the chain.
	if (theEnd instanceof MessagingException) {
	    ((MessagingException)theEnd).next = ex;
	    return true;
	} else
	    return false;
    }

    /**
     * Produce the message, include the message from the nested
     * exception if there is one.
     */
    public String getMessage() {
	if (next == null)
	    return super.getMessage();
	Exception n = next;
	String s = super.getMessage();
	StringBuffer sb = new StringBuffer(s == null ? "" : s);
	while (n != null) {
	    sb.append(";\n  nested exception is:\n\t");
	    if (n instanceof MessagingException) {
		MessagingException mex = (MessagingException)n;
		sb.append(n.getClass().toString());
		String msg = mex.getSuperMessage();
		if (msg != null) {
		    sb.append(": ");
		    sb.append(msg);
		}
		n = mex.next;
	    } else {
		sb.append(n.toString());
		n = null;
	    }
	}
	return sb.toString();
    }

    private String getSuperMessage() {
	return super.getMessage();
    }
}