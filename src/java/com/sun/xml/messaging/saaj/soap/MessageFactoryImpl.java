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
 * $Id: MessageFactoryImpl.java,v 1.2 2007-07-16 16:41:21 ofung Exp $
 * $Revision: 1.2 $
 * $Date: 2007-07-16 16:41:21 $
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
package com.sun.xml.messaging.saaj.soap;

import java.io.*;
import java.util.logging.Logger;

import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ParseException;
import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Message1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Message1_2Impl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.messaging.saaj.util.TeeInputStream;

/**
 * A factory for creating SOAP messages.
 *
 * Converted to a placeholder for common functionality between SOAP
 * implementations.
 *
 * @author Phil Goodwin (phil.goodwin@sun.com)
 */
public class MessageFactoryImpl extends MessageFactory {

    protected static Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.LocalStrings");

    protected static OutputStream listener;

    protected boolean lazyAttachments = false;
    
    public static OutputStream listen(OutputStream newListener) {
        OutputStream oldListener = listener;
        listener = newListener;
        return oldListener;
    }
    
    public SOAPMessage createMessage() throws SOAPException {
        throw new UnsupportedOperationException();
    }

    public SOAPMessage createMessage(boolean isFastInfoset, 
        boolean acceptFastInfoset) throws SOAPException 
    {
        throw new UnsupportedOperationException();
    }
    
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in)
        throws SOAPException, IOException {
        String contentTypeString = MessageImpl.getContentType(headers);

        if (listener != null) {
            in = new TeeInputStream(in, listener);
        }

        try {
            ContentType contentType = new ContentType(contentTypeString);
            int stat = MessageImpl.identifyContentType(contentType);

            if (MessageImpl.isSoap1_1Content(stat)) {
                return new Message1_1Impl(headers,contentType,stat,in);
            } else if (MessageImpl.isSoap1_2Content(stat)) {
                return new Message1_2Impl(headers,contentType,stat,in);
            } else {
                log.severe("SAAJ0530.soap.unknown.Content-Type");
                throw new SOAPExceptionImpl("Unrecognized Content-Type");
            }
        } catch (ParseException e) {            
            log.severe("SAAJ0531.soap.cannot.parse.Content-Type");
            throw new SOAPExceptionImpl(
                "Unable to parse content type: " + e.getMessage());
        }
    }

    protected static final String getContentType(MimeHeaders headers) {
        String[] values = headers.getHeader("Content-Type");
        if (values == null)
            return null;
        else
            return values[0];
    }

    public void setLazyAttachmentOptimization(boolean flag) {
        lazyAttachments = flag;
    }

}