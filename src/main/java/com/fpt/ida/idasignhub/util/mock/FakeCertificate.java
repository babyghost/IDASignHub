package com.fpt.ida.idasignhub.util.mock;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

public class FakeCertificate extends X509Certificate {
    private final String subjectDN;
    private final String issuerDN;
    private final byte[] encoded;
    private final PublicKey publicKey;

    public FakeCertificate(String subjectDN,
                           String issuerDN,
                           byte[] encoded,
                           PublicKey publicKey) {
        this.subjectDN  = subjectDN;
        this.issuerDN   = issuerDN;
        this.encoded    = encoded;
        this.publicKey  = publicKey;
    }

    @Override
    public void checkValidity() { /* no-op */ }

    @Override
    public void checkValidity(Date date) { /* no-op */ }

    @Override
    public int getVersion() { return 3; }

    @Override
    public BigInteger getSerialNumber() { return BigInteger.ONE; }

    @Override
    public Principal getIssuerDN() { return () -> issuerDN; }

    @Override
    public Principal getSubjectDN() { return () -> subjectDN; }

    @Override
    public Date getNotBefore() { return new Date(0); }

    @Override
    public Date getNotAfter()  { return new Date(System.currentTimeMillis() + 10_000); }

    @Override
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getSignature() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSigAlgName() { return "SHA256withRSA"; }

    @Override
    public String getSigAlgOID()  { return "1.2.840.113549.1.1.11"; }

    @Override
    public byte[] getSigAlgParams() { return null; }

    @Override
    public boolean[] getIssuerUniqueID() { return null; }

    @Override
    public boolean[] getSubjectUniqueID() { return null; }

    @Override
    public boolean[] getKeyUsage() { return null; }

    @Override
    public int getBasicConstraints() { return -1; }

    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
        return encoded.clone();
    }

    @Override
    public void verify(PublicKey key) throws CertificateException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            NoSuchProviderException,
            SignatureException {
        // no-op
    }

    @Override
    public void verify(PublicKey key, String sigProvider) throws CertificateException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            NoSuchProviderException,
            SignatureException {
        // no-op
    }

    @Override
    public String toString() {
        return "FakeCertificate[subject=" + subjectDN + ", issuer=" + issuerDN + "]";
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public boolean hasUnsupportedCriticalExtension() {
        return false;
    }

    @Override
    public Set<String> getCriticalExtensionOIDs() {
        return Set.of();
    }

    @Override
    public Set<String> getNonCriticalExtensionOIDs() {
        return Set.of();
    }

    @Override
    public byte[] getExtensionValue(String oid) {
        return new byte[0];
    }
}
