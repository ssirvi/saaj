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


package saaj.httpget;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.soap.*;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class SoapHttpGetTest {
    
    public static void main(String args[]) throws Exception {

        try {

            // Create a SOAP connection
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection con = scf.createConnection();

            String to = System.getProperty("to");
            System.out.println("Sending resuest to : " + to);
	
	    SOAPMessage reply = con.get(new URL(to));
            if (!(reply.getSOAPPart().getEnvelope() instanceof 
                 com.sun.xml.messaging.saaj.soap.ver1_1.Envelope1_1Impl)) {
                 throw new Exception("expected a 1.1 message");
            }

            try {
	        reply = con.get(new URL(to));
                throw new Exception("Should throw an error");
            } catch (SOAPException e) {

            }

	    reply = con.get(new URL(to));
            if (!(reply.getSOAPPart().getEnvelope() instanceof 
                 com.sun.xml.messaging.saaj.soap.ver1_2.Envelope1_2Impl)) {
                 throw new Exception("expected a 1.2 message");
            }
            SOAPEnvelope env = reply.getSOAPPart().getEnvelope();

	} catch(Exception e) {
            e.printStackTrace();
	}
    }
}