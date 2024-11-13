package com.fpt.ida.idasignhub.bussiness;

import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.ConfigSignature;
import com.fpt.ida.idasignhub.data.LocationSignConvertXY;
import com.fpt.ida.idasignhub.data.ShowSignature;
import com.fpt.ida.idasignhub.data.SignHubCallback;
import com.fpt.ida.idasignhub.util.SignHubUtill;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.simple.JSONObject;

import java.io.File;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

public class SignHub {
    public static KeyStore loadUsbToken() {
        for (String algorithm : Security.getAlgorithms("Signature")) {
            System.out.println(algorithm);
        }
        // Đăng ký Bouncy Castle provider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        System.out.println("loadUsbToken: ");
        String pkcs11Config = ConstantUtil.IDA_DRIVER_TOKEN_USB_URL;
        Provider[] providers = Security.getProviders();
        for (Provider p : providers) {
            System.out.println("provider: " + p.getName());
        }
        Provider provider = Security.getProvider("SunPKCS11");
        provider = provider.configure(pkcs11Config);
        Security.addProvider(provider);

        for (Provider p : providers) {
            System.out.println("provider: " + p.getName());
        }
        // Kiểm tra thiết bị có hỗ trợ SHA256withRSA không
        try {
            Signature sig = Signature.getInstance("SHA256withRSA", provider);
            System.out.println("Thiet bi ho tro SHA256withRSA");
        } catch (Exception e) {
            System.out.println("Thiet bi khong ho tro SHA256withRSA");
        }
        KeyStore.CallbackHandlerProtection callbackHandlerProtection = new KeyStore.CallbackHandlerProtection(new SignHubCallback() {});
        KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", provider, callbackHandlerProtection);
        try {
            return builder.getKeyStore();
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getAliasByKeyStore(KeyStore keyStore) throws KeyStoreException {
        System.out.println("keyStore: " + keyStore.getType());
        String alias = null;
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            alias = aliases.nextElement();
            System.out.println("alias: " + alias);
        }
        return alias;
    }

    public static List<String> signPDFFromFolder(String folder,ConfigSignature config) {
        List<String> list = new ArrayList<>();
        File directory = new File(folder);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(signPDFFromFolder(file.getAbsolutePath(), config));
                } else {
                    JSONObject rep = SignHub.signPDFV2(file, config);
                    if(rep != null && (int)rep.get("status") == 200) {
                        list.add((String)rep.get("fileName"));
                    }
                }
            }
        }
        return list;
    }
    public static JSONObject signPDFV2(File file, ConfigSignature config) {
        SimpleDateFormat df = new SimpleDateFormat(ConstantUtil.IDA_SIMPLEDATEFORMAT_DATE_TIME);
        String imageQuocHuyPath = ConstantUtil.IDA_IMAGE_QUOCHUY_URL;
        String urlfont = ConstantUtil.IDA_FONT_URL;
        String folderChua = ConstantUtil.IDA_ROOT_FOLDER + "/sign";
        String signResult = folderChua + "/"+file.getName()+"_signed";
        if (file.getName().contains(".")) {
            // Lấy phần mở rộng
            String extesion = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            signResult = folderChua + "/"+fileName+"_signed"+"."+extesion;
        }
        try {
            String alias = null;
            KeyStore keyStore = SignHub.loadUsbToken();
            // Lấy khóa riêng từ KeyStore
            alias = SignHub.getAliasByKeyStore(keyStore);
            if (Objects.nonNull(alias)) {
                //  Lấy certificate từ alias;
                Certificate[] chain     = keyStore.getCertificateChain(alias);
                PrivateKey privateKey   = (PrivateKey) keyStore.getKey(alias, null);
                PdfReader reader = new PdfReader(file);
                PdfWriter writer = new PdfWriter(signResult);
                PdfSigner signer = new PdfSigner(reader, writer, new StampingProperties());

                PdfDocument pdfDoc = signer.getDocument();
                int numberOfPages = pdfDoc.getNumberOfPages();
                int pageSign = config.getPageSign();
                if(pageSign > numberOfPages) {
                    pageSign = numberOfPages;
                }
                float pageHeight = pdfDoc.getPage(pageSign).getPageSize().getHeight();
                float pageWidth = pdfDoc.getPage(pageSign).getPageSize().getWidth();
                float xLocation  = config.getxLocation();
                float yLocation  = config.getyLocation();

                int numberOfPageShow    = config.getPageSign() > 0 ? config.getPageSign() : 1;
                int fontSize            = config.getFontSizeSignature();

                String nguoiky = "Ho Dac Tai";
                String email = "dactaiit@gmail.com";
                String coquan = "Trường THPT Phạm Văn Đồng";
                String thoigian = "Ngày 13/9/2024 09:48";

                Certificate certificate = keyStore.getCertificate(alias);
                X509Certificate x509Certificate = (X509Certificate) certificate;
                String[] arrSplit = x509Certificate.getSubjectDN().getName().split(",");
                nguoiky = arrSplit[0].replace("CN=", "");
                coquan = arrSplit[3].replace("OU=", "");

                // LAY CONFIG
                ShowSignature showSignature = config.getShowSignature();

                // Lấy dấu timestamping Authority
                // URL của dịch vụ TSA (Time Stamping Authority)
                String tsaUrl = config.getTimeStampServer(); // Bạn có thể thay bằng URL của TSA bạn sử dụng
                TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle(tsaUrl, null, null);
                Calendar signingDate = Calendar.getInstance();
                thoigian    = df.format(signingDate.getTime());
                email       = x509Certificate.getSubjectAlternativeNames().stream().toList().get(0).get(1).toString();

                String noidungky = "Ký bởi: "+ nguoiky +"\n Email: " +email+"\n Cơ quan: "+coquan+"\n Thời gian: "+thoigian;
                // kiểu hiển thị. 0,1,2 => 0 = hiển thị ảnh và mô tả, 1 chỉ hiển thị ảnh, 2 chỉ hiển thị mô tả.
                int showType = showSignature.getShowType();

                // KY_SAOY; KY_SAOLUC; KY_TRICKSAO;
                if(config.getSignatureType().equals("KY_SAOY")) {
                    noidungky = "SAO Y;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
                if(config.getSignatureType().equals("KY_SAOLUC")) {
                    noidungky = "SAO LỤC;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
                if(config.getSignatureType().equals("KY_TRICKSAO")) {
                    noidungky = "TRÍCH SAO;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
                // END LOAI KY

                ImageData signatureImage = ImageDataFactory.create(imageQuocHuyPath);
                if(config.getImgSignBase64() != null) {
                    // convert base64 sang ImageData
                    String dataImg = config.getImgSignBase64();
                    String ext = "png"; // hardcode tạm thòi;
                    String[] extArr = {"png"};
                    if(!dataImg.isEmpty()) {
                        signatureImage = SignHubUtill.convertBase64ToImageData(dataImg);
                    }
                }

                if(!config.getLocationSign().isEmpty()) {
                    LocationSignConvertXY locationSignConvertXY = new LocationSignConvertXY(config.getLocationSign(), pageHeight, pageWidth);
                    xLocation = locationSignConvertXY.getxLocation();
                    yLocation = locationSignConvertXY.getyLocation();
                }
                int width   = config.getWidthSignature();
                int height  = config.getHeightSignature();

                // convert tọa độ
                float transformedY = pageHeight - yLocation - (height * 2); // height là độ cao của block chu ky so
                float transformedx = pageWidth - (width * 2);
                if(transformedY < 0) {
                    transformedY = 0;
                }
                if(transformedx < 0) {
                    transformedx = 0;
                }
                if(!config.getLocationSign().isEmpty()) {
                    LocationSignConvertXY locationSignConvertXY = new LocationSignConvertXY(config.getLocationSign(), pageHeight, pageWidth);
                    xLocation = locationSignConvertXY.getxLocation();
                    yLocation = locationSignConvertXY.getyLocation();
                }
                // convert tọa độ
                if(transformedY < 0) {
                    transformedY = 0;
                }
//                String cryptoConfig = config.getCryptoStandard().isEmpty() ? "CMS" : config.getCryptoStandard(); // CMS or CADES
                PdfSigner.CryptoStandard cryptoStandard = PdfSigner.CryptoStandard.CMS;
                if(config.getCryptoStandard().equalsIgnoreCase("CADES")) {
                    cryptoStandard = PdfSigner.CryptoStandard.CADES;
                }
                Rectangle rect = new Rectangle(xLocation, transformedY, width, height);
                //Rectangle rect = new Rectangle(xLocation, yLocation, width, height); // (x, y, width, height) - vị trí chữ ký trên trang PDF
                PdfSignatureAppearance appearance = signer.getSignatureAppearance();
                appearance.setReason("");
                appearance.setLocation("");
                appearance.setContact("");
                appearance.setLayer2Text(noidungky);
                appearance.setReuseAppearance(false);
                appearance.setPageRect(rect);

                // 0 = ImageAndInfo, 1 = Image, 2 = Info
                if(showType == 1) {
                    appearance.setSignatureGraphic(signatureImage);
                    appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
                } else if(showType == 2) {
                    appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
                } else {
                    appearance.setSignatureGraphic(signatureImage);
                    appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
                }
                appearance.setImageScale(-1); // Điều chỉnh tỷ lệ hình ảnh nếu cần
                appearance.setLayer2FontSize(fontSize);
                PdfFont font = PdfFontFactory.createFont(urlfont);
                appearance.setLayer2Font(font);
                appearance.setPageNumber(numberOfPageShow);

                signer.setFieldName("Signature");
                IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, ConstantUtil.PROVIDER_NAME);
                IExternalDigest digest = new BouncyCastleDigest();
//                signer.signDetached(digest, pks, chain, null, null, tsaClient, 0, cryptoStandard);
//                Security.removeProvider(ConstantUtil.PROVIDER_NAME);
                JSONObject rep = new JSONObject();
                rep.put("status", 200);
                rep.put("message", "Ký thành công");
                rep.put("fileName", signResult);
                return rep;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void signPDF(KeyStore keyStore, String alias,
                               String fileInput, String fileOutput, ConfigSignature config) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(ConstantUtil.IDA_SIMPLEDATEFORMAT_DATE_TIME);
        String imageQuocHuyPath = ConstantUtil.IDA_IMAGE_QUOCHUY_URL;
        String urlfont = ConstantUtil.IDA_FONT_URL;
        ShowSignature showSignature = config.getShowSignature();
        int showType = showSignature.getShowType();
//  Lấy certificate từ alias;
//  Certificate certificate = keyStore.getCertificate(alias);
        Certificate[] chain     = keyStore.getCertificateChain(alias);
        PrivateKey privateKey   = (PrivateKey) keyStore.getKey(alias, null);
//
//  phần 1: lay file PDF ra
        PdfReader reader = new PdfReader(fileInput);
        PdfWriter writer = new PdfWriter(fileOutput);
        PdfSigner signer = new PdfSigner(reader, writer, new StampingProperties());
//  phần 2: setup Config, hinh anh cho chu ky


//      ConfigSignature config = param.getConfigSignature();
        int xLocation = config.getxLocation();
        int yLocation = config.getyLocation();
        int width = config.getWidthSignature();
        int height = config.getHeightSignature();
        int numberOfPageShow = config.getPageSign() > 0 ? config.getPageSign() : 1;
        int fontSize = config.getFontSizeSignature();

//      draw SignatureImage

        // thong tin certificate
        String nguoiky = "Ho Dac Tai";
        String email = "dactaiit@gmail.com";
        String coquan = "Trường THPT Phạm Văn Đồng";
        String thoigian = "Ngày 13/9/2024 09:48";

        Certificate certificate = keyStore.getCertificate(alias);
        X509Certificate x509Certificate = (X509Certificate) certificate;
        System.out.println("SubjectDN: " + x509Certificate.getSubjectDN().getName());
        String[] arrSplit = x509Certificate.getSubjectDN().getName().split(",");
        nguoiky = arrSplit[0].replace("CN=", "");
        coquan = arrSplit[3].replace("OU=", "");

        // Lấy dấu timestamping Authority
        // URL của dịch vụ TSA (Time Stamping Authority)
        String tsaUrl = config.getTimeStampServer(); // Bạn có thể thay bằng URL của TSA bạn sử dụng
        TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle(tsaUrl, null, null);
        Calendar signingDate = Calendar.getInstance();
        thoigian = df.format(signingDate.getTime());
        email = x509Certificate.getSubjectAlternativeNames().stream().toList().get(0).get(1).toString();

        String noidungky = "Ký bởi: "+ nguoiky +"\n Email: " +email+"\n Cơ quan: "+coquan+"\n Thời gian: "+thoigian;

//        KY_SAOY; KY_SAOLUC; KY_TRICHSAO;
        if(config.getSignatureType().equals("KY_SAOY")) {
            noidungky = "SAO Y;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
            showType = 2;
        }
        if(config.getSignatureType().equals("KY_SAOLUC")) {
            noidungky = "SAO LỤC;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
            showType = 2;
        }
        if(config.getSignatureType().equals("KY_TRICHSAO")) {
            noidungky = "TRÍCH SAO;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
            showType = 2;
        }
//        ImageData signatureImage = SignHubUtill.drawImageSignature(config, nguoiky, email, coquan, thoigian);
//        BufferedImage imageQH = ImageIO.read(new File(imageQuocHuyPath));
//        BufferedImage resizeQuocHuy = SignHubUtill.resizeImage(imageQH, 50, 50);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(resizeQuocHuy, "png", baos);
//        byte[] imageBytes = baos.toByteArray();
        // Tạo đối tượng ImageData từ mảng byte
//        ImageData signatureImage =  ImageDataFactory.create(imageBytes);

        ImageData signatureImage = ImageDataFactory.create(imageQuocHuyPath);
        String cryptoConfig = config.getCryptoStandard().isEmpty() ? "CMS" : config.getCryptoStandard(); // CMS or CADES
        PdfSigner.CryptoStandard cryptoStandard = PdfSigner.CryptoStandard.CMS;
        if(cryptoConfig.equalsIgnoreCase("CADES")) {
            cryptoStandard = PdfSigner.CryptoStandard.CADES;
        }
        System.out.println("x = " + xLocation + "y = "+ yLocation);
        Rectangle rect = new Rectangle(xLocation, yLocation, width, height); // (x, y, width, height) - vị trí chữ ký trên trang PDF
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance.setReason("");
        appearance.setLocation("");
        appearance.setContact("");
        appearance.setLayer2Text(noidungky);
        appearance.setReuseAppearance(false);
        appearance.setPageRect(rect);
//        appearance.setImage(signatureImage);

        // 0 = ImageAndInfo, 1 = Image, 2 = Info
        if(showType == 1) {
            appearance.setSignatureGraphic(signatureImage);
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
        } else if(showType == 2) {
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
        } else {
            appearance.setSignatureGraphic(signatureImage);
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
        }
        appearance.setImageScale(-1); // Điều chỉnh tỷ lệ hình ảnh nếu cần
        appearance.setLayer2FontSize(fontSize);
        PdfFont font = PdfFontFactory.createFont(urlfont);
        appearance.setLayer2Font(font);
        appearance.setPageNumber(numberOfPageShow);

        signer.setFieldName("Signature:");
        IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, "SunPKCS11-VGCAToken");
        IExternalDigest digest = new BouncyCastleDigest();


//        PdfDocument pdfDoc = signer.getDocument();
//        pdfDoc.getCatalog().getPdfObject().put(PdfName.Lang, new PdfString("vi-VN")); // Hoặc tiếng Việt
        // Ký số file PDFkkb
        signer.signDetached(digest, pks, chain, null, null, tsaClient, 0, cryptoStandard);

        System.out.println("Ký số thành công.");
        Security.removeProvider("SunPKCS11-VGCAToken");
    }

}
