package com.mckinleyit.wcbml.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class JSoupUtil {

    public static Document getDocument(String url) throws IOException {
        try {
            enableSSLSocket();
            return Jsoup.connect(url).ignoreHttpErrors(true).validateTLSCertificates(true).followRedirects(true).get();
        } catch (KeyManagementException e) {
            throw new IOException(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    private static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
}
