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
 * $Id: QuoteTest.java,v 1.2 2007-07-16 16:41:26 ofung Exp $
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

package bugfixes;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

public class QuoteTest extends TestCase {
    
    public QuoteTest(String name) {
        super(name);
    }    
    
    /*
     * getValue returned on element should not get truncated after 
     * encountering quot;
     */ 
    public void testQuoteGetValue() {
        String arg = "src/test/bugfixes/data/quot.xml";
        try {
            doit1(arg);
        } catch (Throwable t) {
            fail("Get Quote test failed" + t.getMessage());
            // t.printStackTrace();
        }
    }
    
    static String  x509IssuerName = 
        "CN=VeriSign Class 3 Code Signing 2001 CA," +
        " OU=Terms of use at https://www.verisign.com/rpa (c)01," +
        " OU=VeriSign Trust Network, O=\"VeriSign, Inc.\"";

    public void doit1(String fileName) throws Exception {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        /*
         * This next statement will affect how the quote entities are
         * processed by the parser
         */
        
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        
        StreamSource source = new StreamSource(new FileInputStream(fileName));
        soapPart.setContent(source);
        message.saveChanges();
        
        SOAPHeader soapHeader = message.getSOAPPart().getEnvelope().getHeader();
        //System.out.println("\nnodeToString=" + nodeToString(soapHeader) + "\n\n");        
        
        Iterator iterator = null;
        SOAPElement keyInfoElement = null;  
        
        Name issuerSerial = SOAPFactory.newInstance()
            .createName("X509IssuerSerial", null, 
            "http://www.w3.org/2000/09/xmldsig#");
        iterator = soapHeader.getChildElements();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof SOAPElement) {
                SOAPElement soapElement = (SOAPElement)o;
                keyInfoElement = findElement((SOAPElement)o, issuerSerial);
                //System.out.println("\nnodeToString(keyInfoElement)=" + 
                //    nodeToString(keyInfoElement) + "\n\n");
                Iterator iterator2 = keyInfoElement.getChildElements();
                while (iterator2.hasNext()) {
                    o = iterator2.next();
                    if (o instanceof SOAPElement) {
                        soapElement = (SOAPElement)o;
                        if ("X509IssuerName".
                                equalsIgnoreCase(soapElement.getLocalName())) {
                            if (!x509IssuerName.equalsIgnoreCase(
                            soapElement.getValue()))
                                fail("Wrong IssuerName returned \n" + 
                                soapElement.getValue());
                        }
                    }
                }
                break;
            }
        }
        
    }
    
    /** Convert a node tree into a STRING representation */
    public String nodeToString(org.w3c.dom.Node node) throws Exception {
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        StringWriter stringWriter = new StringWriter();
        
        DOMSource source = new DOMSource(node);
        StreamResult result = new StreamResult(stringWriter);
        
        transformer.transform(source, result);
        return stringWriter.toString();
    }
    
    public SOAPElement findElement(SOAPElement soapElement, Name name) {
        Name n = soapElement.getElementName();
        if (n.equals(name)) {
            return soapElement;
        }
        Iterator iterator = soapElement.getChildElements();
        while (iterator.hasNext()) {
            Object o = (Object)iterator.next();
            if (o instanceof SOAPElement) {
                SOAPElement result = findElement((SOAPElement)o, name);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
    
}