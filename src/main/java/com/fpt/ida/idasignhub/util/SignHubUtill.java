package com.fpt.ida.idasignhub.util;

import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.ConfigSignature;
import com.fpt.ida.idasignhub.data.LocationSignConvertXY;
import com.fpt.ida.idasignhub.data.ShowSignature;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class SignHubUtill {
    /*
        ConfigSignature config : data config,
        String nguoiKy: thong tin nguoi ky lay tu Usb ky so,
        String Email: thong tin email cua nguoi ky,
        String coquan: ten to chuc,
        String thoigianky: timestamp duoc lay khi ky so.
     */
    public static ImageData drawImageSignature(ConfigSignature config, String nguoiKy, String email, String coquan, String thoigianky) throws IOException {
        String imageQuocHuyPath = ConstantUtil.IDA_IMAGE_QUOCHUY_URL; // Thay đường dẫn tới file chữ ký
        // Tạo ImageData từ file ảnh

        BufferedImage imageQH = ImageIO.read(new File(imageQuocHuyPath));
        BufferedImage resizedImage = resizeImage(imageQH, 150, 150);

        // tạo 1 đối tượng graphics2D để vẽ.
        BufferedImage bufferedImage = new BufferedImage(config.getWidthSignature(), config.getHeightSignature(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setBackground(java.awt.Color.WHITE);
        g2d.clearRect(0, 0, config.getWidthSignature(), config.getHeightSignature());
        // int x = (config.getWidthSignature() - 150) / 2;  // Tọa độ X
        int y = (config.getHeightSignature() - 150) / 2; // Tọa độ Y
        g2d.drawImage(resizedImage, 4, y, null);
        // Cài đặt màu và font chữ
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Serif", Font.PLAIN, config.getFontSizeSignature()));
        // Vẽ văn bản lên hình ảnh
        int _nguoikyHeight  = 50;
        int _emailHeight    = 0;
        int _coquanHeight   = 0;
        int _thoigianHeight = 0;
        int maxWidth = 150;
        _emailHeight = drawWrappedString(g2d, "Ký bởi: " + nguoiKy, 155, _nguoikyHeight, maxWidth);
        _coquanHeight = drawWrappedString(g2d, "Email: " + email, 155, _emailHeight, maxWidth);
        _thoigianHeight = drawWrappedString(g2d, "Cơ quan: " + coquan, 155, _coquanHeight, maxWidth);
        drawWrappedString(g2d, "Thời gian ký: " + thoigianky, 155, _thoigianHeight, maxWidth);
        g2d.dispose();
        // ----- test hinh image sau khi draw
        File outputfile = new File(ConstantUtil.IDA_IMAGE_LOGO_DRAW_URL);
        ImageIO.write(bufferedImage, ConstantUtil.IDA_IMAGE_LOGO_DRAW_URL, outputfile);
        // ---------------
        try {
            // Chuyển đổi BufferedImage thành mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, ConstantUtil.IDA_FILE_TYPE_PNG, baos);
            byte[] imageBytes = baos.toByteArray();
            // Tạo đối tượng ImageData từ mảng byte
            return ImageDataFactory.create(imageBytes);
        } catch (Exception e) {
            return null;
        }
    }

    // Phương thức thay đổi kích thước của BufferedImage
    public static BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        // Tạo một BufferedImage mới với kích thước mới
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        // Lấy đối tượng Graphics2D từ BufferedImage mới
        Graphics2D g2d = resizedImage.createGraphics();

        // Vẽ lại hình ảnh gốc lên BufferedImage mới với kích thước được thay đổi
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);

        // Giải phóng tài nguyên đồ họa
        g2d.dispose();

        return resizedImage;
    }
    // Phương thức vẽ chuỗi văn bản nhiều dòng
    public static void drawMultilineString(Graphics2D g2d, String text, int x, int y) {
        // Chia văn bản theo ký tự xuống dòng "\n"
        String[] lines = text.split("\n");
        int lineHeight = g2d.getFontMetrics().getHeight();
        for (int i = 0; i < lines.length; i++) {
            g2d.drawString(lines[i], x, y + (i * lineHeight));
        }
    }

    // Phương thức vẽ chuỗi văn bản với tự động xuống dòng khi vượt quá chiều rộng, return về chiều cao của text
    public static int drawWrappedString(Graphics2D g2d, String text, int x, int y, int maxWidth) {
        int result = 0;
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String testLine = line + word + " ";
            int lineWidth = fontMetrics.stringWidth(testLine);

            // Kiểm tra xem độ rộng của dòng có vượt quá maxWidth không
            if (lineWidth > maxWidth) {
                // Vẽ dòng hiện tại nếu vượt quá chiều rộng
                g2d.drawString(line.toString(), x, y);
                // Tăng tọa độ Y để xuống dòng tiếp theo
                y += lineHeight;
                // Bắt đầu dòng mới với từ hiện tại
                line = new StringBuilder(word + " ");
            } else {
                // Thêm từ vào dòng hiện tại nếu chưa vượt quá chiều rộng
                line.append(word).append(" ");
            }
        }
        // Vẽ dòng cuối cùng
        g2d.drawString(line.toString(), x, y);
        result = y + 20;
        return result;
    }
    // Hàm nén một thư mục
    public static void zipDirectory(Path folder, Path zipFilePath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFilePath));
             BufferedOutputStream bos = new BufferedOutputStream(zos)) {
            Files.walk(folder)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(folder.relativize(path).toString().replace("\\", "/"));
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Failed to zip file: " + path + " " + e);
                        }
                    });
        }
    }


    public static X509Certificate[] generateSelfSignedChain(int chainLength) throws Exception {
        X509Certificate[] chain = new X509Certificate[chainLength];
        // Chuỗi keyPairs
        KeyPair[] keyPairs = new KeyPair[chainLength];
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        for (int i = 0; i < chainLength; i++) {
            keyPairs[i] = keyGen.generateKeyPair();
        }
        SecureRandom random = new SecureRandom();
        Date notBefore = new Date(System.currentTimeMillis() - 3600_000);
        Date notAfter  = new Date(System.currentTimeMillis() + 365L*24*3600_000);
        // Sinh mỗi certificate, ký bởi key tiếp theo (hoặc tự ký nếu root)
        for (int i = 0; i < chainLength; i++) {
            X500Name issuer  = new X500Name("CN=CA" + i);
            X500Name subject = new X500Name("CN=Cert" + i);
            BigInteger serial = BigInteger.valueOf(random.nextLong()).abs();
            X509v3CertificateBuilder certBuilder =
                    new JcaX509v3CertificateBuilder(
                            issuer, serial, notBefore, notAfter, subject, keyPairs[i].getPublic()
                    );
            ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                    .build(i+1 < chainLength ? keyPairs[i+1].getPrivate() : keyPairs[i].getPrivate());
            X509Certificate cert = new JcaX509CertificateConverter()
                    .getCertificate(certBuilder.build(signer));
            chain[i] = cert;
        }
        return chain;
    }

    public static PrivateKey generateTestPrivateKey() throws Exception {
        // Tạo bộ sinh khóa RSA 2048 bit
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        // Sinh cặp khóa
        KeyPair keyPair = keyGen.generateKeyPair();
        // Lấy PrivateKey
        return keyPair.getPrivate();
    }
    public static void zipArrayResult(String zipFolder, List<String> srcFiles, String nameFile) {
        System.out.println("zipArrayResult....");
        int count = SignHubUtill.countFileFromParentFolder(zipFolder);
        String filePath = zipFolder + "/[IDA-signer]." +count+ "_" + nameFile + ".zip";
        System.out.println(filePath);
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (String srcFile : srcFiles) {
                FileInputStream fis = new FileInputStream(srcFile);
                ZipEntry zipEntry = new ZipEntry(srcFile);
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            System.out.println("Zip done ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void zipArrayResult(String zipFolder, List<String> srcFiles) {
        System.out.println("zipArrayResult....");
        int count = SignHubUtill.countFileFromParentFolder(zipFolder);
        String filePath = zipFolder + "/IDAsignResult_"+count + ".zip";
        System.out.println(filePath);
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (String srcFile : srcFiles) {
                FileInputStream fis = new FileInputStream(srcFile);
                ZipEntry zipEntry = new ZipEntry(srcFile);
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            System.out.println("Zip done ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int countFolderFromParentFolder(String parentFolderPath) {
        File directory = new File(parentFolderPath);
        int folderCount = 0;
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        folderCount++;
                    }
                }
            }
        }
        return folderCount;
    }
    public static int countFileFromParentFolder(String parentFolderPath) {
        File directory = new File(parentFolderPath);
        int fileCount = 0;
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory()) {
                        fileCount++;
                    }
                }
            }
        }
        return fileCount;
    }
    public static void deleteFileFromParentFolder(String parentFolderPath) {
        System.out.println("deleteFileFromParentFolder...");
        File directory = new File(parentFolderPath);
        int fileCount = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                boolean delete = file.delete();
                fileCount++;
            }
        }
        System.out.println("Done: delete " + fileCount + " file from " + parentFolderPath);
    }
    // return path tmp folder
    public static String unzipFile(String filePath) {
        String tmpFolder = ConstantUtil.IDA_TMP_FOLDER;
        int soLuongFolder = SignHubUtill.countFolderFromParentFolder(tmpFolder);
        //tmp_1, tmp_2
        String nameFolder = tmpFolder+"/tmp_"+soLuongFolder;
        File dir = new File(nameFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(nameFolder, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            return nameFolder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    * type = 0 => create only folder
    * type = 1 => create
    * */
    public static String createFolder(String folderName, int type) {
        // Tên của thư mục bạn muốn tạo
        String dirPath = "newDirectory";

        // Tạo đối tượng File với đường dẫn thư mục
        File directory = new File(dirPath);

        // Tạo thư mục bằng mkdir() hoặc mkdirs()
        if (directory.mkdir()) {
            System.out.println("Directory created successfully!");
        } else {
            System.out.println("Failed to create directory!");
        }
        return "";
    }
    public static Boolean checkHasDirectoryByPath(String path) {
        try {
            File directory = new File(path);
            if (directory.exists() && directory.isDirectory()) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static ConfigSignature generateConfigSignature(Integer height, Integer width, Integer fontsize,
                                                          String xLocation, String yLocation,
                                                          Integer pageSign, String signatureImg, String signatureType, Integer showType, String locationSign) {
        // locationSign dung de xac dinh ky mac dinh theo goc nao

        ConfigSignature configExample = new ConfigSignature();
        configExample.setCoutryLocation("VN");
        configExample.setHeightSignature(50);
        if(height != null && height > 0) {
            configExample.setHeightSignature(height);
        }
        configExample.setWidthSignature(150);
        if(width != null && width > 0) {
            configExample.setWidthSignature(width);
        }
        configExample.setFontSizeSignature(8);
        if(fontsize != null && fontsize > 0) {
            configExample.setFontSizeSignature(fontsize);
        }
        configExample.setReasonSign("");
        configExample.setxLocation(480);
        if(xLocation != null && !xLocation.isEmpty()) {
            configExample.setxLocation(Integer.parseInt(xLocation));
        }
        configExample.setyLocation(740);
        if(yLocation != null && !yLocation.isEmpty()) {
            configExample.setyLocation(Integer.parseInt(yLocation));
        }
        configExample.setPageSign(1);
        if(pageSign != null && pageSign > 0) {
            configExample.setPageSign(pageSign);
        }
        // lưu base64
        if(signatureImg != null && !signatureImg.isEmpty()) {
            configExample.setImgSignBase64(signatureImg);
        }
        configExample.setCryptoStandard("CMS");
        configExample.setTimeStampServer(ConstantUtil.IDA_BANCOYEU_TIMESTAMP_URL);
        configExample.setSignatureType("KY_CANHAN");
        if(!signatureType.isEmpty()) {
            configExample.setSignatureType(signatureType);
        }

        // config mo rong
        ShowSignature showSignature = new ShowSignature();
        if(showType > 0) {
            showSignature.setShowType(showType); // 0,1,2
        }

        showSignature.setShowInfoExtends("INFOLABEL_EMAIL_ORGANIZATION_TIMESTAMP");
        configExample.setShowSignature(showSignature);
        configExample.setLocationSign(locationSign);

        return configExample;
    }
    public static ImageData convertBase64ToImageData(String imgPNGBase64) {
        ImageData result = null;
        try {
            // Decode the Base64 string
            byte[] imageBytes = Base64.getDecoder().decode(imgPNGBase64);
            result = ImageDataFactory.create(imageBytes);
            // Write the decoded bytes to a file
            return result;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }
}

