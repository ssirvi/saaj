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
package com.sun.xml.messaging.saaj.soap;

import javax.xml.soap.SAAJMetaFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPException;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class SAAJMetaFactoryImpl extends SAAJMetaFactory {

    protected static Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.LocalStrings");

    protected  MessageFactory newMessageFactory(String protocol)
        throws SOAPException {
        if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl();
        } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl();
        } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.dynamic.SOAPMessageFactoryDynamicImpl(); 
        } else {
            log.log(
                Level.SEVERE,
                "SAAJ0569.soap.unknown.protocol",
                new Object[] {protocol, "MessageFactory"});
            throw new SOAPException("Unknown Protocol: " + protocol + 
                                        "  specified for creating MessageFactory");
        }
    }

    protected  SOAPFactory newSOAPFactory(String protocol)
        throws SOAPException {
        if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl();
        } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.ver1_2.SOAPFactory1_2Impl();
        } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.dynamic.SOAPFactoryDynamicImpl(); 
        } else {
            log.log(
                Level.SEVERE,
                "SAAJ0569.soap.unknown.protocol",
                new Object[] {protocol, "SOAPFactory"});
            throw new SOAPException("Unknown Protocol: " + protocol + 
                                        "  specified for creating SOAPFactory");
        }
    }

}