package com.cbq.yatori.core.utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class CustomTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
        // Do nothing (accept all client certificates)
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        // Do nothing (accept all server certificates)
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public static SSLContext getSSLContext() {
        TrustManager[] trustManagers = new TrustManager[]{new CustomTrustManager()};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, trustManagers, null);
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
