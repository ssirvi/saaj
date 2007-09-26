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
 * $Id: FaultElement1_2Impl.java,v 1.2 2007-07-16 16:41:24 ofung Exp $
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

/**
*
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.soap.ver1_2;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.Name;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.FaultElementImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.SOAPExceptionImpl;

public class FaultElement1_2Impl extends FaultElementImpl {

    public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, NameImpl qname) {
        super(ownerDoc, qname);
    }

    public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, String localName) {
        super(ownerDoc, NameImpl.createSOAP12Name(localName));
    }

    protected boolean isStandardFaultElement() {
        String localName = elementQName.getLocalPart();
        if (localName.equalsIgnoreCase("code") ||
            localName.equalsIgnoreCase("reason") ||
            localName.equalsIgnoreCase("node") ||
            localName.equalsIgnoreCase("role")) {
            return true;
        }
        return false;
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        if (!isStandardFaultElement()) {
            FaultElement1_2Impl copy =
                new FaultElement1_2Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
            return replaceElementWithSOAPElement(this,copy);
        } else {
            return super.setElementQName(newName);
        }
    }

    public void setEncodingStyle(String encodingStyle) throws SOAPException {        
        log.severe("SAAJ0408.ver1_2.no.encodingStyle.in.fault.child");
        throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on a Fault child element");
    }

    public SOAPElement addAttribute(Name name, String value)
        throws SOAPException {
        if (name.getLocalName().equals("encodingStyle")
            && name.getURI().equals(NameImpl.SOAP12_NAMESPACE)) {                
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    public SOAPElement addAttribute(QName name, String value)
        throws SOAPException {
        if (name.getLocalPart().equals("encodingStyle")
            && name.getNamespaceURI().equals(NameImpl.SOAP12_NAMESPACE)) {                
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }
}