package com.fpt.ida.idasignhub.test;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * Utility to generate a fake keystore (JKS or PKCS12) with a self-signed certificate.
 */
public class KeystoreGenerator {
    public static void main(String[] args) throws Exception {
        // Register BouncyCastle
        Security.addProvider(new BouncyCastleProvider());
        // Keystore parameters
        String keyStorePath = "fakekeystore.jks";
        char[] storePassword = "changeit".toCharArray();
        char[] keyPassword = "changeit".toCharArray();
        String alias = "testkey";

        // Generate RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privKey = keyPair.getPrivate();

        // Build self-signed certificate
        long now = System.currentTimeMillis();
        Date notBefore = new Date(now - 1000L * 60 * 60 * 24);
        Date notAfter = new Date(now + (long)3650 * 24 * 60 * 60 * 1000);
        X500Name issuer = new X500Name("CN=Test, OU=Dev, O=MyCompany, L=Hanoi, ST=Hanoi, C=VN");
        BigInteger serial = BigInteger.valueOf(now);

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                issuer,
                keyPair.getPublic()
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC").build(privKey);

        X509Certificate cert = new JcaX509CertificateConverter()
                .setProvider("BC").getCertificate(certBuilder.build(signer));
        cert.checkValidity(new Date());
        cert.verify(keyPair.getPublic());

        // Create and load keystore
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, storePassword);

        // Set entry
        ks.setKeyEntry(alias, privKey, keyPassword, new Certificate[]{cert});

        // Save to file
        try (FileOutputStream fos = new FileOutputStream(keyStorePath)) {
            ks.store(fos, storePassword);
        }

        System.out.println("Keystore generated: " + keyStorePath);
    }
}

