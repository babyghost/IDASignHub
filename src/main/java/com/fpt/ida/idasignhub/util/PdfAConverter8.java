package com.fpt.ida.idasignhub.util;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.signatures.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

public class PdfAConverter8 {

    /**
     * Ký số trên file PDF/A-1b mà vẫn giữ conformance.
     *
     * @param srcPdfA       file PDF/A-1b gốc (output_pdfa.pdf)
     * @param destSigned    file đầu ra có chữ ký (signed_output.pdf)
     * @param keystorePath  path tới keystore .p12
     * @param keystorePass  mật khẩu keystore
     * @param keyAlias      alias private key
     * @param keyPass       mật khẩu private key
     * @param iccPath       đường dẫn tới file ICC (sRGB.icc)
     */
    public static void signPdfA(String srcPdfA,
                                String destSigned,
                                String keystorePath,
                                String keystorePass,
                                String keyAlias,
                                String keyPass,
                                String iccPath) throws Exception {
        // 1. Đăng ký BouncyCastle provider (cần cho SHA256withRSA)
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        // 2. Load keystore PKCS#12
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            ks.load(fis, keystorePass.toCharArray());
        }
        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, keyPass.toCharArray());
        Certificate[] chain = ks.getCertificateChain(keyAlias);

        // 3. Mở lại file PDF/A-1b bằng PdfADocument và nạp lại ICC để validate
        PdfReader reader = new PdfReader(srcPdfA);
        PdfWriter writer = new PdfWriter(destSigned);
        PdfOutputIntent outputIntent;
        try (FileInputStream iccStream = new FileInputStream(iccPath)) {
            outputIntent = new PdfOutputIntent(
                    "sRGB IEC61966-2.1",
                    "sRGB IEC61966-2.1",
                    "http://www.color.org",
                    "sRGB IEC61966-2.1",
                    iccStream
            );
        }
        PdfADocument pdfADoc = new PdfADocument(
                reader,
                writer,
                new com.itextpdf.kernel.pdf.StampingProperties().useAppendMode()
        );

        // 4. Tạo PdfSigner ở chế độ append mode (giữ nguyên conformance PDF/A)
        PdfSigner signer = new PdfSigner(
                reader,
                new FileOutputStream(destSigned),
                new StampingProperties().useAppendMode()
        );


        // 5. Thiết lập SignatureAppearance (vị trí hiển thị chữ ký)
        Rectangle rect = new Rectangle(36, 36, 200, 100);
        PdfSignatureAppearance appearance = signer.getSignatureAppearance()
                .setReason("Ký số PDF/A-1b")
                .setLocation("Đà Nẵng")
                .setPageRect(rect)
                .setPageNumber(1)
                .setReuseAppearance(false);
        signer.setFieldName("SignatureField1");

        // 6. Chọn thuật toán ký (SHA256withRSA) và digest (BouncyCastle)
        IExternalSignature pks    = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, provider.getName());
        IExternalDigest   digest = new BouncyCastleDigest();

        // 7. Thực hiện ký Detached (CAdES). Nếu muốn CMS, đổi tham số cuối thành PdfSigner.CryptoStandard.CMS
        signer.signDetached(
                digest,
                pks,
                chain,
                null,  // ocspClient
                null,  // crlList
                null,  // tsaClient
                0,     // estimatedSize (0 để iText tự tính)
                PdfSigner.CryptoStandard.CADES
        );

        // 8. Đóng PdfADocument
        pdfADoc.close();
    }

    public static void main(String[] args) {
        try {
            String srcPdfA      = "output_pdfa.pdf";
            String destSigned   = "signed_output.pdf";
            String keystorePath = "keystore.p12";
            String keystorePass = "password_keystore";
            String keyAlias     = "myalias";
            String keyPass      = "password_keystore";
            String iccPath      = "src/main/resources/sRGB.icc";

            signPdfA(srcPdfA, destSigned,
                    keystorePath, keystorePass,
                    keyAlias, keyPass,
                    iccPath);

            System.out.println("Ký số PDF/A-1b thành công: " + destSigned);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi ký PDF/A: " + e.getMessage());
        }
    }
}

