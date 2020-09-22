/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.transport.stomp;

import java.io.IOException;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.security.JaasCertificateAuthenticationPlugin;

public class StompSslAuthTest extends StompTest {

    @Override
    public void setUp() throws Exception {

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/client.keystore");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        System.setProperty("javax.net.ssl.keyStore", "src/test/resources/server.keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.keyStoreType", "jks");
        //System.setProperty("javax.net.debug","ssl,handshake");
        super.setUp();
    }

    @Override
    protected BrokerPlugin configureAuthentication() throws Exception {
        JaasCertificateAuthenticationPlugin plugin = new JaasCertificateAuthenticationPlugin();
        plugin.setConfiguration("cert-login");
        return plugin;
    }

    @Override
    protected Socket createSocket() throws IOException {
        SocketFactory factory = SSLSocketFactory.getDefault();
        return factory.createSocket("127.0.0.1", this.sslPort);
    }

    @Override
    protected void addOpenWireConnector() throws Exception {
        TransportConnector connector = brokerService.addConnector("ssl://0.0.0.0:0?transport.needClientAuth=true&transport.verifyHostName=false");
        jmsUri = connector.getPublishableConnectString() + "?socket.verifyHostName=false";
    }

    @Override
    protected void addStompConnector() throws Exception {
        TransportConnector connector = brokerService.addConnector(
                "stomp+ssl://0.0.0.0:"+port+"?needClientAuth=true&transport.verifyHostName=false");
        sslPort = connector.getConnectUri().getPort();
    }

    // NOOP - These operations handled by jaas cert login module
    @Override
    public void testSubscribeWithReceiptNotAuthorized() throws Exception {
    }

    @Override
    public void testConnectNotAuthenticatedWrongUser() throws Exception {
    }

    @Override
    public void testConnectNotAuthenticatedWrongPassword() throws Exception {
    }

    @Override
    public void testSendNotAuthorized() throws Exception {
    }

    @Override
    public void testSubscribeNotAuthorized() throws Exception {
    }

    @Override
    public void testJMSXUserIDIsSetInMessage() throws Exception {
    }

    @Override
    public void testJMSXUserIDIsSetInStompMessage() throws Exception {
    }

    @Override
    public void testClientSetMessageIdIsIgnored() throws Exception {
    }
}
