package com.tsystems.simplepusher.context;

import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import okhttp3.OkHttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Configuration
public class DsHttpContextConfiguration {

    @Bean
    public Serializer serializer() {
        return new Serializer();
    }

    @Bean
    public OkHttpClient okHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];


        return new OkHttpClient.Builder()
                .sslSocketFactory(SSLContextBuilder.create()
                        .loadTrustMaterial(new TrustSelfSignedStrategy()).build().getSocketFactory(), trustManager)
                .build();
    }
}
