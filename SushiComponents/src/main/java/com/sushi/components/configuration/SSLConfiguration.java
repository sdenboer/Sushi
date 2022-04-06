package com.sushi.components.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SSLConfiguration {

    private static final Logger logger = Logger.getLogger(SSLConfiguration.class);

    public static SSLContext authenticatedContext() {
        try (InputStream keystoreFile = SSLConfiguration.class.getClassLoader()
            .getResourceAsStream("ssl/certificate.pfx")) {

            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(keystoreFile, "Tr%8=JKVktQem%r6Ut3^*^4MQdC2jF".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "Tr%8=JKVktQem%r6Ut3^*^4MQdC2jF".toCharArray());

            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            return sslContext;
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Problem creating SSL context");
            System.exit(1);
            return null;
        }

    }
}
