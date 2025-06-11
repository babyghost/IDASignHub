package com.fpt.ida.idasignhub.bussiness;

import com.fpt.ida.idasignhub.bussiness.excel.ExcelGeneratorForSignData;
import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.*;
import com.fpt.ida.idasignhub.util.FileUtils;
import com.fpt.ida.idasignhub.util.PDFUtil;
import com.fpt.ida.idasignhub.util.SignHubUtill;
import com.fpt.ida.idasignhub.util.mock.FakeCertificate;
import com.fpt.ida.idasignhub.validation.PdfAValidator;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.pdfa.exceptions.PdfAConformanceException;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.simple.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.usb4java.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.itextpdf.forms.xfdf.XfdfConstants.DEST;

public class SignHub {

    public static KeyStore loadUsbToken() throws NoSuchAlgorithmException {
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
//        for (Provider p : providers) {
//            System.out.println("provider: " + p.getName());
//        }
        Provider provider = Security.getProvider("SunPKCS11");
        provider = provider.configure(pkcs11Config);
        Security.addProvider(provider);
        providers = Security.getProviders();
        for (Provider p : providers) {
            System.out.println("provider after add Config USB: " + p.getName());
            System.out.println("provider after add Config USB: " + p.getInfo());
            System.out.println(p);
        }
        // test slot usb
        Context context = new Context();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to initialize libusb", result);
        }
        DeviceList list = new DeviceList();
        result = LibUsb.getDeviceList(context, list);
        if (result < 0) {
            throw new LibUsbException("Unable to get device list", result);
        }
        try {
            // Iterate over all devices and print their information
            for (Device device : list) {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, descriptor);
                if (result != LibUsb.SUCCESS) {
                    throw new LibUsbException("Unable to read device descriptor", result);
                }
                System.out.format("Device %04x:%04x (bus %d, device %d)%n",
                        descriptor.idVendor(), descriptor.idProduct(),
                        LibUsb.getBusNumber(device), LibUsb.getDeviceAddress(device));
            }
        } finally {
            // Ensure the allocated device list is freed
            LibUsb.freeDeviceList(list, true);
            LibUsb.exit(context);
        }
        // end test slot usb

        // Kiểm tra thiết bị có hỗ trợ SHA256withRSA không
        System.out.println("test usb v2: " + provider.getName());
//        Signature sig = Signature.getInstance("SHA256withRSA", provider);
        try {
            Signature sig = Signature.getInstance("SHA256withRSA", provider);
            System.out.println("Thiet bi ho tro SHA256withRSA");
        } catch (Exception e) {
            System.out.println("Thiet bi khong ho tro SHA256withRSA hoặc chưa kết nối USB or slot usb from config not true");
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

        String alias = null;
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            alias = aliases.nextElement();
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

    public static void signPDFAutoWithPath(String pathFolder) throws IOException{
        try {
            System.out.println("Bước 1: load dữ liệu từ folder");
//            String folderDaKy = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filedakyso";
            String folderDaKy = FileUtils.normalizeToForwardSlash(pathFolder);
            String folderChuaKy = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filecanky";
            // step 1: đọc danh sách file đệ quy và list ra danh sách
            if (!Files.exists(Paths.get(folderChuaKy))) {
                Files.createDirectories(Paths.get(folderChuaKy));
            }

            List<FileEntry> fileEntries = new ArrayList<>();
            File folder_root = new File(folderChuaKy);
            if(folder_root.exists()) {
                fileEntries = FileUtils.getFilesRecursively(folder_root); //  sẽ lấy file pdf, nếu file zip thì unzip và dequy tiếp để lấy hết file
            }
            // step 2: từ danh sách có được, ký số theo config default
            System.out.println(" ====> Bước 1: done");
            if(!fileEntries.isEmpty()) {
                System.out.println(" ====> Tổng số file chuẩn bị ký số : "+fileEntries.size());
                System.out.println("Bước 2: Bắt đầu ký số từng file");
            } else {
                System.out.println(" ====> Chưa có dữ liệu để ký số : ");
            }

            List<FileEntry> filesSignFinish = new ArrayList<>();
            for(FileEntry fileEntry : fileEntries) {
                // tiến hành ký số từng file;
                System.out.println(" ========> Bước 2.1: Xử lý file : "+fileEntry.getFilePathOnServer());
                System.out.println(" ============= name = "+fileEntry.getFileName());
                String extesion = fileEntry.getFileName().substring(fileEntry.getFileName().lastIndexOf(".") + 1);
                String fileName = fileEntry.getFileName().substring(0, fileEntry.getFileName().lastIndexOf("."));
//                IDASignHubTool\folder_filecanky\2420\2420.6648\000.00.18.H17.2017.6648.01.pdf
                String pathOld = fileEntry.getFilePathOnServer().substring(0, fileEntry.getFilePathOnServer().lastIndexOf("\\")).replace("IDASignHubTool\\folder_filecanky\\", "");
                System.out.println("pathOld: "+pathOld);
                String signResult = folderDaKy + "/" + pathOld + "/"+fileName+"_signed."+extesion;
                System.out.println("signResult: "+signResult);
                File fileResult = new File(signResult);
                if(!Files.exists(fileResult.toPath())) {
                    FileUtils.createFileAndFolder(signResult);
                    File file = new File(fileEntry.getFilePath());
                    System.out.println(" ============= file = "+file.getAbsolutePath());
                    if(extesion.equalsIgnoreCase("pdf")) {
                        System.out.println(" ============= signing = "+file.getAbsolutePath());
                        SignHub.signPDFAutoFilePDF(file);
                    }
                    filesSignFinish.add(fileEntry);
                }
            }
            System.out.println(" ====> Bước 2: done " + filesSignFinish.size());
            // step 3: tracking file excel
            if(!filesSignFinish.isEmpty()) {
                System.out.println("Create report excel ...");
                byte[] data  = ExcelGeneratorForSignData.exportReportSignData(fileEntries);
                Path path    = Paths.get(ConstantUtil.IDA_ROOT_FOLDER);
                ExcelGeneratorForSignData.saveFileToPath(data, path);
            }


            // step 4: xóa data đã ký xong
//            ExcelGeneratorForSignData.deleteFolder(Paths.get(folderChuaKy));
            // step 6: download cục mới từ fpt

        } catch (RuntimeException e) {
            System.out.println("Chưa gắn thiết bị USB Token");
            e.printStackTrace();
        }
    }
    // dùng để scan folder theo file config
    public static void signPDFAuto() throws IOException{
        try {
            System.out.println("Bước 1: load dữ liệu từ folder");
//            String folderDaKy = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filedakyso";
//            String folderChuaKy = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filecanky";
            String folderDaKy = ConstantUtil.IDA_CUSTOM_FOLDER + "/folder_filedakyso";
//            String folderChuaKy = ConstantUtil.IDA_CUSTOM_FOLDER + "/Done_HoaPhu";
            String folderChuaKy = ConstantUtil.IDA_CUSTOM_FOLDER +  ConstantUtil.IDA_FOLDER_ENDPOINT;


            // step 1: đọc danh sách file đệ quy và list ra danh sách
            if (!Files.exists(Paths.get(folderChuaKy))) {
                Files.createDirectories(Paths.get(folderChuaKy));
            }

            List<FileEntry> fileEntries = new ArrayList<>();
            File folder_root = new File(folderChuaKy);
            if(folder_root.exists()) {
                fileEntries = FileUtils.getFilesRecursively(folder_root); //  sẽ lấy file pdf, nếu file zip thì unzip và dequy tiếp để lấy hết file
            }
            // step 2: từ danh sách có được, ký số theo config default
            System.out.println(" ====> Bước 1: done");
            if(!fileEntries.isEmpty()) {
                System.out.println(" ====> Tổng số file chuẩn bị ký số : "+fileEntries.size());
                System.out.println("Bước 2: Bắt đầu ký số từng file");
            } else {
                System.out.println(" ====> Chưa có dữ liệu để ký số : ");
            }

            List<FileEntry> filesSignFinish = new ArrayList<>();
            for(FileEntry fileEntry : fileEntries) {
                // tiến hành ký số từng file;
                System.out.println(" ========> Bước 2.1: Xử lý file : "+fileEntry.getFilePathOnServer());
                System.out.println(" ============= name = "+fileEntry.getFileName());
                String extesion = fileEntry.getFileName().substring(fileEntry.getFileName().lastIndexOf(".") + 1);
                String fileName = fileEntry.getFileName().substring(0, fileEntry.getFileName().lastIndexOf("."));
//                IDASignHubTool\folder_filecanky\2420\2420.6648\000.00.18.H17.2017.6648.01.pdf
                String pathOld = fileEntry.getFilePathOnServer().substring(0, fileEntry.getFilePathOnServer().lastIndexOf("\\")).replace("IDASignHubTool\\folder_filecanky\\", "");

                String signResult = folderDaKy + "/" + pathOld.replace(ConstantUtil.IDA_FOLDER_ENDPOINT_2,"") + "/"+fileName+"_signed."+extesion;

                File fileResult = new File(signResult);
                System.out.println(" fileResult = "+fileResult.toPath());
                if(!Files.exists(fileResult.toPath())) {
                    System.out.println(" File exists ");
                    FileUtils.createFileAndFolder(signResult);
                    File file = new File(fileEntry.getFilePath());
                    System.out.println(" File extesion "+extesion);
                    if(extesion.equalsIgnoreCase("pdf")) {
                    //check file có phải la file pdf chuân A/1B ko
//                        Boolean flag = PdfAValidator.isPdfA(file);
//                        System.out.println(" KIỂM TRA " + flag);
                        try {
//                            SignHub.signPDFAutoFilePDF(file);
                            SignHub.signPDFAutoFilePDFNotHaveUsbToken(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        if(flag) {
//                            try {
////                                SignHub.signPDFAutoFilePDFTypeA1B(file);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            try {
//                                SignHub.signPDFAutoFilePDF(file);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        SignHub.testPDFChuanA(file);
                    }
                    filesSignFinish.add(fileEntry);
                }
            }
            System.out.println(" ====> Bước 2: done " + filesSignFinish.size());
            // step 3: tracking file excel
            if(!filesSignFinish.isEmpty()) {
                System.out.println("Create report excel ...");
                byte[] data  = ExcelGeneratorForSignData.exportReportSignData(fileEntries);
                Path path    = Paths.get(ConstantUtil.IDA_ROOT_FOLDER);
                ExcelGeneratorForSignData.saveFileToPath(data, path);
            }


            // step 4: xóa data đã ký xong
//            ExcelGeneratorForSignData.deleteFolder(Paths.get(folderChuaKy));
            // step 6: download cục mới từ fpt

        } catch (RuntimeException  e) {
            System.out.println("Chưa gắn thiết bị USB Token");
            e.printStackTrace();
        }
    }


    public static void signPDFAutoFilePDFTypeA1B(File file) throws IOException, GeneralSecurityException {
        try {
        String iccPath = "";
        //Bước 1: Chuẩn bị ICC profile (ví dụ sRGB.icc).
        Resource iccResource = new ClassPathResource("icc/sRGB_CS_profile.icm");
        iccPath = iccResource.getFile().getAbsolutePath();
        // -----------
        PdfOutputIntent outputIntent = new PdfOutputIntent(
                "Custom",                  // Tên “Profile” (tùy ý)
                "",                        // outputConditionIdentifier (có thể để trống)
                "http://www.color.org",    // outputCondition (URI hoặc mô tả)
                "sRGB IEC61966-2.1",       // registryName (tên profile)
                new FileInputStream(iccPath) // Luồng đọc file ICC
        );

//        SimpleDateFormat df = new SimpleDateFormat(ConstantUtil.IDA_SIMPLEDATEFORMAT_DATE_TIME);
        String imageLogoPath = ConstantUtil.IDA_IMAGE_LOGO_URL_A;
        String urlfont = ConstantUtil.IDA_FONT_URL;
        String folderDaKy = ConstantUtil.IDA_CUSTOM_FOLDER + "/folder_filedakyso";
        String folderDaKy_tmp = ConstantUtil.IDA_CUSTOM_FOLDER + "/folder_tmp";
        // set config default
        ConfigSignature config = new ConfigSignature();
        config.setCoutryLocation("VN");
        config.setHeightSignature(50);
        config.setWidthSignature(100);
        config.setFontSizeSignature(7);
        config.setReasonSign("Sign PDF");
        config.setImgSignBase64(null);
//        config.setxLocation(480); // trên trái
//        config.setyLocation(740); // trên trái
        config.setPageSign(1);
        config.setCryptoStandard("CMS");
        config.setTimeStampServer(ConstantUtil.IDA_BANCOYEU_TIMESTAMP_URL);
        ShowSignature showSignature = new ShowSignature();
        showSignature.setShowType(0); //1 là có hình ảnh. 2 là ko
        showSignature.setShowInfoExtends("INFOLABEL_ORGANIZATION_TIMESTAMP");
        config.setShowSignature(showSignature);
        config.setSignatureType("KY_SOHOA");
        config.setLocationSign("tren_phai");

        if (!Files.exists(Paths.get(folderDaKy))) {
            Files.createDirectories(Paths.get(folderDaKy));
        }

        String extesion = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));

        String pathOld = file.toPath().toString().substring(0, file.toPath().toString().lastIndexOf("\\")).replace("IDASignHubTool\\folder_filecanky\\", "");
        System.out.println("pathOld: "+pathOld);
        String signResult = folderDaKy + "/" + pathOld.replace(ConstantUtil.IDA_FOLDER_ENDPOINT_2,"") + "/"+fileName+"_signed."+extesion;
        String signResult_tmp = folderDaKy + "/" + pathOld.replace(ConstantUtil.IDA_FOLDER_ENDPOINT_2,"") + "/"+fileName+"_tmp."+extesion;
        System.out.println("signResult_tmp: "+signResult_tmp);

            String alias = null;
            KeyStore keyStore = SignHub.loadUsbToken();
            alias = SignHub.getAliasByKeyStore(keyStore);
            if (Objects.nonNull(alias)) {
                //  Lấy certificate từ alias;
                Certificate[] chain   = keyStore.getCertificateChain(alias);
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);

                // Bước 2: Tạo PdfReader cho file PDF/A-1b gốc
                PdfDocument srcDoc = null;
                srcDoc = new PdfDocument(new PdfReader(file));

                WriterProperties writerProps = new WriterProperties()
                        .setPdfVersion(PdfVersion.PDF_1_4);

                PdfWriter writer = new PdfWriter(signResult_tmp, writerProps);
//                PdfWriter writer = new PdfWriter(signResult_tmp, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_4));
                // 4. Tạo PdfSigner ở chế độ “append mode”
                StampingProperties props = new StampingProperties().useAppendMode();
                // 5. Mở PdfADocument ở chế độ stamping mode (append)
                PdfADocument pdfADoc =  new PdfADocument(
                        writer,
                        PdfAConformanceLevel.PDF_A_1B,
                        outputIntent
                );

                int nPages = srcDoc.getNumberOfPages();

                srcDoc.copyPagesTo(1, nPages, pdfADoc);


                int numberOfPages = pdfADoc.getNumberOfPages(); // số lượng file của file
                int pageSign = config.getPageSign(); // page ký số
                System.out.println("pageSign: "+pageSign);
                if(pageSign > numberOfPages) {
                    pageSign = numberOfPages;
                }
                float pageHeight = pdfADoc.getPage(pageSign).getPageSize().getHeight();
                float pageWidth  = pdfADoc.getPage(pageSign).getPageSize().getWidth();

                pdfADoc.close();
                srcDoc.close();

                PdfReader reader = null;
                PdfSigner signer = null;
                reader = new PdfReader(signResult_tmp);
                PdfWriter writer_2 = new PdfWriter(signResult);
                signer = new PdfSigner(
                        reader,
                        writer_2,
                        props
                );
                // lay config default
                float xLocation   = config.getxLocation();
                float yLocation   = config.getyLocation();

                String nguoiky  = "Ho Dac Tai";
                String email    = "dactaiit@gmail.com";
                String coquan   = "Trường THPT Phạm Văn Đồng";
                String thoigian = "Ngày 13/9/2024 09:48";

                Certificate certificate         = keyStore.getCertificate(alias);
                X509Certificate x509Certificate = (X509Certificate) certificate;
                String[] arrSplit = x509Certificate.getSubjectDN().getName().split(",");
                //version 1
                System.out.println(Arrays.stream(arrSplit).toList());
//                nguoiky = arrSplit[0].replace("CN=", "");
//                coquan  = arrSplit[2].replace("O=", "");
                // version 2
//                nguoiky = arrSplit[0].replace("EMAILADDRESS=", "");
//                coquan  = arrSplit[1].replace("CN=", "");  //Hòa Phú
                coquan  = arrSplit[3].replace("CN=", "") + ", "+arrSplit[4].replace("OU=", "");
                System.out.println(Arrays.stream(arrSplit).toList());
//                ShowSignature showSignature = config.getShowSignature();
                // Lấy dấu timestamping Authority
                // URL của dịch vụ TSA (Time Stamping Authority)
                String tsaUrl = config.getTimeStampServer(); // Bạn có thể thay bằng URL của TSA bạn sử dụng
                TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle(tsaUrl, null, null);
                Calendar signingDate = Calendar.getInstance();
                ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHours(7));
                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:ss XXX");
                thoigian = now.format(f);
//                thoigian    = df.format(signingDate.getTime());
                email       = x509Certificate.getSubjectAlternativeNames().stream().toList().get(0).get(1).toString();

//                String noidungky =  nguoiky + "," +"\n" + coquan +"\n"+thoigian;
//                [EMAILADDRESS=syt@danang.gov.vn,  UID=MST:0400260612,  UID=MNS:1047665,  CN=S? Y T?,  O=?Y BAN NH?N D?N TH?NH PH? ?? N?NG,  L=?à N?ng,  C=VN]
                String noidungky = coquan +"\n"+thoigian;
                // kiểu hiển thị. 0,1,2 => 0 = hiển thị ảnh và mô tả, 1 chỉ hiển thị ảnh, 2 chỉ hiển thị mô tả.
                int showType = showSignature.getShowType();
                // KY_SAOY; KY_SAOLUC; KY_TRICKSAO;
                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_SAOY")) {
                    noidungky = "SAO Y;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_SAOLUC")) {
                    noidungky = "SAO LỤC;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_TRICKSAO")) {
                    noidungky = "TRÍCH SAO;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
//                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_SOHOA")) {
//                    noidungky = "SỐ HÓA;\n" + nguoiky + ";\n" + thoigian+";";
//                }
                // END LOAI KY
                System.out.println(noidungky);
                ImageData signatureImage = ImageDataFactory.create(imageLogoPath);
                if(config.getImgSignBase64() != null) {
                    // convert base64 sang ImageData
                    String dataImg = config.getImgSignBase64();
                    String ext = "png"; // hardcode tạm thòi;
                    String[] extArr = {"png"};
                    if(!dataImg.isEmpty()) {
                        signatureImage = SignHubUtill.convertBase64ToImageData(dataImg);
                    }
                }
//                float width  = PDFUtil.scaleWidth(pageWidth, 5);
//                float height = PDFUtil.scaleHeight(pageHeight, 4);
                float width  = 140;
                float height = 40;
                if(!config.getLocationSign().isEmpty()) {
                    LocationSignConvertXY locationSignConvertXY = new LocationSignConvertXY(config.getLocationSign(), pageHeight, pageWidth, height, width);
                    xLocation = locationSignConvertXY.getxLocation();
                    yLocation = locationSignConvertXY.getyLocation();
                }

                // convert tọa độ
                PdfSigner.CryptoStandard cryptoStandard = PdfSigner.CryptoStandard.CMS;
                if(config.getCryptoStandard().equalsIgnoreCase("CADES")) {
                    cryptoStandard = PdfSigner.CryptoStandard.CADES;
                }
                Rectangle rect = new Rectangle(xLocation, yLocation, width, height);
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

                int fontSize = 6;
                float resolution  = 300;
//                float scaledFontSize = fontSize * (resolution / 72f);
                appearance.setLayer2FontSize(fontSize);
                PdfFont font = PdfFontFactory.createFont(urlfont);
                appearance.setLayer2Font(font);
                appearance.setPageNumber(pageSign);

                signer.setFieldName("Signature");
                IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, ConstantUtil.PROVIDER_NAME);
                IExternalDigest digest = new BouncyCastleDigest();
                signer.signDetached(digest, pks, chain, null, null, tsaClient, 0, cryptoStandard);
                Security.removeProvider(ConstantUtil.PROVIDER_NAME);
                //giải phóng bộ nhớ
                reader.close();
                pdfADoc.close();
                System.out.println("Done Signing ...");
            }
        } catch (PdfAConformanceException e) {
            throw new RuntimeException(e);
        }
    }

    public static void signPDFAutoFilePDFNotHaveUsbToken(File file) throws IOException{
        SimpleDateFormat df = new SimpleDateFormat(ConstantUtil.IDA_SIMPLEDATEFORMAT_DATE_TIME);
        String imageLogoPath = ConstantUtil.IDA_IMAGE_LOGO_URL;
        String urlfont = ConstantUtil.IDA_FONT_URL;
        String folderDaKy = ConstantUtil.IDA_CUSTOM_FOLDER + "/folder_filedakyso";

        // set config default
        ConfigSignature config = new ConfigSignature();
        config.setCoutryLocation("VN");
        config.setHeightSignature(50);
        config.setWidthSignature(180);
        config.setFontSizeSignature(7);
        config.setReasonSign("Sign PDF");
        config.setImgSignBase64(null);
//        config.setxLocation(480); // trên trái
//        config.setyLocation(740); // trên trái
        config.setPageSign(1);
        config.setCryptoStandard("CMS");
        config.setTimeStampServer(ConstantUtil.IDA_BANCOYEU_TIMESTAMP_URL);
        ShowSignature showSignature = new ShowSignature();
        showSignature.setShowType(0); //1 là có hình ảnh. 2 là ko
        showSignature.setShowInfoExtends("INFOLABEL_ORGANIZATION_TIMESTAMP");
        config.setShowSignature(showSignature);
        config.setSignatureType("KY_SOHOA");
        config.setLocationSign("tren_phai");

        if (!Files.exists(Paths.get(folderDaKy))) {
            Files.createDirectories(Paths.get(folderDaKy));
        }
        String extesion = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        String pathOld = file.toPath().toString().substring(0, file.toPath().toString().lastIndexOf("\\")).replace("IDASignHubTool\\folder_filecanky\\", "");
        String signResult = folderDaKy + "/" + pathOld.replace(ConstantUtil.IDA_FOLDER_ENDPOINT_2,"") + "/"+fileName+"_signed."+extesion;
        try {

            PdfReader reader = new PdfReader(file);
            PdfWriter writer = new PdfWriter(signResult);
            PdfSigner signer = new PdfSigner(reader, writer, new StampingProperties().useAppendMode());
            PdfDocument pdfDoc = signer.getDocument();
            // lay config default
            int numberOfPages = pdfDoc.getNumberOfPages(); // số lượng file của file
            float xLocation   = config.getxLocation();
            float yLocation   = config.getyLocation();

            int pageSign = config.getPageSign(); // page ký số
            if(pageSign > numberOfPages) {
                pageSign = numberOfPages;
            }
            float pageHeight = pdfDoc.getPage(pageSign).getPageSize().getHeight();
            float pageWidth  = pdfDoc.getPage(pageSign).getPageSize().getWidth();

            String coquan   = "Trường THPT Phạm Văn Đồng";
            String thoigian = "Ngày 13/9/2024 09:48";

            String pkcs12   = "content/fakekeystore.jks";
            String keystorePassword = "changeit";
            String keyAlias = "testkey";
            String keyPassword = "changeit";

            Security.addProvider(new BouncyCastleProvider());
            Resource fileResource = new ClassPathResource(pkcs12);
            String pkcs12Path = fileResource.getFile().getAbsolutePath();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(pkcs12Path), keystorePassword.toCharArray());


            // URL của dịch vụ TSA (Time Stamping Authority)
//            String tsaUrl = config.getTimeStampServer(); // Bạn có thể thay bằng URL của TSA bạn sử dụng
//            TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle(tsaUrl, null, null);
            Calendar signingDate = Calendar.getInstance();
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHours(7));
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:ss XXX");
            thoigian = now.format(f);

            PrivateKey pk = (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
            Certificate[] chain = ks.getCertificateChain(keyAlias);

            Certificate certificate         = ks.getCertificate(keyAlias);
            X509Certificate x509Certificate = (X509Certificate) certificate;
            String[] arrSplit = x509Certificate.getSubjectDN().getName().split(",");
//            version 1
            System.out.println(Arrays.stream(arrSplit).toList());

            String noidungky = coquan +"\n"+thoigian;
            // kiểu hiển thị. 0,1,2 => 0 = hiển thị ảnh và mô tả, 1 chỉ hiển thị ảnh, 2 chỉ hiển thị mô tả.
            int showType = 0;
            // END LOAI KY
            System.out.println(noidungky);
            ImageData signatureImage = ImageDataFactory.create(imageLogoPath);

            float width  = 180;
            float height = 50;

            if(!config.getLocationSign().isEmpty()) {
                LocationSignConvertXY locationSignConvertXY = new LocationSignConvertXY(config.getLocationSign(), pageHeight, pageWidth, height, width);
                xLocation = locationSignConvertXY.getxLocation();
                yLocation = locationSignConvertXY.getyLocation();
            }

            // convert tọa độ
//
            PdfSigner.CryptoStandard cryptoStandard = PdfSigner.CryptoStandard.CMS;

//            cryptoStandard = PdfSigner.CryptoStandard.CADES;

            Rectangle rect = new Rectangle(xLocation, yLocation, width, height);
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

            int fontSize = 7;
            float resolution  = 300;
//                float scaledFontSize = fontSize * (resolution / 72f);
            appearance.setLayer2FontSize(fontSize);
            PdfFont font = PdfFontFactory.createFont(urlfont);
            appearance.setLayer2Font(font);
            appearance.setPageNumber(pageSign);

            signer.setFieldName("Signature");
            IExternalSignature pks = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, BouncyCastleProvider.PROVIDER_NAME);
            IExternalDigest digest = new BouncyCastleDigest();
            signer.signDetached(digest, pks, chain, null, null, null, 0, cryptoStandard);
            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
            //giải phóng bộ nhớ
            reader.close();

            System.out.println("Done Signing ...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void signPDFAutoFilePDF(File file) throws IOException{
        SimpleDateFormat df = new SimpleDateFormat(ConstantUtil.IDA_SIMPLEDATEFORMAT_DATE_TIME);
        String imageLogoPath = ConstantUtil.IDA_IMAGE_LOGO_URL;
        String urlfont = ConstantUtil.IDA_FONT_URL;
        String folderDaKy = ConstantUtil.IDA_CUSTOM_FOLDER + "/folder_filedakyso";
        // set config default
        ConfigSignature config = new ConfigSignature();
        config.setCoutryLocation("VN");
        config.setHeightSignature(50);
        config.setWidthSignature(180);
        config.setFontSizeSignature(7);
        config.setReasonSign("Sign PDF");
        config.setImgSignBase64(null);
//        config.setxLocation(480); // trên trái
//        config.setyLocation(740); // trên trái
        config.setPageSign(1);
        config.setCryptoStandard("CMS");
        config.setTimeStampServer(ConstantUtil.IDA_BANCOYEU_TIMESTAMP_URL);
        ShowSignature showSignature = new ShowSignature();
        showSignature.setShowType(0); //1 là có hình ảnh. 2 là ko
        showSignature.setShowInfoExtends("INFOLABEL_ORGANIZATION_TIMESTAMP");
        config.setShowSignature(showSignature);
        config.setSignatureType("KY_SOHOA");
        config.setLocationSign("tren_phai");

        if (!Files.exists(Paths.get(folderDaKy))) {
            Files.createDirectories(Paths.get(folderDaKy));
        }
        String extesion = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));

        String pathOld = file.toPath().toString().substring(0, file.toPath().toString().lastIndexOf("\\")).replace("IDASignHubTool\\folder_filecanky\\", "");
        System.out.println("pathOld: "+pathOld);
        String signResult = folderDaKy + "/" + pathOld.replace(ConstantUtil.IDA_FOLDER_ENDPOINT_2,"") + "/"+fileName+"_signed."+extesion;
        System.out.println("signResult3: "+signResult);
        try {
            String alias = null;
            KeyStore keyStore = SignHub.loadUsbToken();
            alias = SignHub.getAliasByKeyStore(keyStore);
//            if (Objects.nonNull(alias)) {
//                System.out.println("alias: "+alias);
//                Certificate[] chain   = keyStore.getCertificateChain(alias);
//            }
            if (Objects.nonNull(alias)) {
                //  Lấy certificate từ alias;
                Certificate[] chain   = keyStore.getCertificateChain(alias);
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
                PdfReader reader = new PdfReader(file);
                PdfWriter writer = new PdfWriter(signResult);
                PdfSigner signer = new PdfSigner(reader, writer, new StampingProperties().useAppendMode());
                PdfDocument pdfDoc = signer.getDocument();
                // lay config default
                int numberOfPages = pdfDoc.getNumberOfPages(); // số lượng file của file
                float xLocation   = config.getxLocation();
                float yLocation   = config.getyLocation();
                int pageSign = config.getPageSign(); // page ký số
                if(pageSign > numberOfPages) {
                    pageSign = numberOfPages;
                }

                float pageHeight = pdfDoc.getPage(pageSign).getPageSize().getHeight();
                float pageWidth  = pdfDoc.getPage(pageSign).getPageSize().getWidth();

                String nguoiky  = "Ho Dac Tai";
                String email    = "dactaiit@gmail.com";
                String coquan   = "Trường THPT Phạm Văn Đồng";
                String thoigian = "Ngày 13/9/2024 09:48";

                Certificate certificate         = keyStore.getCertificate(alias);
                X509Certificate x509Certificate = (X509Certificate) certificate;
                String[] arrSplit = x509Certificate.getSubjectDN().getName().split(",");
                //version 1
                System.out.println(Arrays.stream(arrSplit).toList());
//                nguoiky = arrSplit[0].replace("CN=", "");
//                coquan  = arrSplit[2].replace("O=", "");
                // version 2
//                nguoiky = arrSplit[0].replace("EMAILADDRESS=", "");
//                coquan  = arrSplit[1].replace("CN=", "");
                coquan = arrSplit[0].replace("CN=", "") + ", Quận Cẩm Lệ, Thành phố Đà Nẵng";
//                coquan  = arrSplit[3].replace("CN=", "") + ", "+arrSplit[4].replace("OU=", "");
                System.out.println(Arrays.stream(arrSplit).toList());
//                ShowSignature showSignature = config.getShowSignature();
                // Lấy dấu timestamping Authority
                // URL của dịch vụ TSA (Time Stamping Authority)
                String tsaUrl = config.getTimeStampServer(); // Bạn có thể thay bằng URL của TSA bạn sử dụng
                TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle(tsaUrl, null, null);
                Calendar signingDate = Calendar.getInstance();
                ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHours(7));
                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:ss XXX");
                thoigian = now.format(f);
                email       = x509Certificate.getSubjectAlternativeNames().stream().toList().get(0).get(1).toString();

//                String noidungky =  nguoiky + "," +"\n" + coquan +"\n"+thoigian;
//                [EMAILADDRESS=syt@danang.gov.vn,  UID=MST:0400260612,  UID=MNS:1047665,  CN=S? Y T?,  O=?Y BAN NH?N D?N TH?NH PH? ?? N?NG,  L=?à N?ng,  C=VN]
                String noidungky = coquan +"\n"+thoigian;
                // kiểu hiển thị. 0,1,2 => 0 = hiển thị ảnh và mô tả, 1 chỉ hiển thị ảnh, 2 chỉ hiển thị mô tả.
                int showType = showSignature.getShowType();
                // KY_SAOY; KY_SAOLUC; KY_TRICKSAO;
                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_SAOY")) {
                    noidungky = "SAO Y;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_SAOLUC")) {
                    noidungky = "SAO LỤC;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_TRICKSAO")) {
                    noidungky = "TRÍCH SAO;\n" + nguoiky + ";\nThời gian ký: " + thoigian+";";
                }
//                if(config.getSignatureType() != null && config.getSignatureType().equals("KY_SOHOA")) {
//                    noidungky = "SỐ HÓA;\n" + nguoiky + ";\n" + thoigian+";";
//                }
                // END LOAI KY
                System.out.println(noidungky);
                ImageData signatureImage = ImageDataFactory.create(imageLogoPath);
                if(config.getImgSignBase64() != null) {
                    // convert base64 sang ImageData
                    String dataImg = config.getImgSignBase64();
                    String ext = "png"; // hardcode tạm thòi;
                    String[] extArr = {"png"};
                    if(!dataImg.isEmpty()) {
                        signatureImage = SignHubUtill.convertBase64ToImageData(dataImg);
                    }
                }
//                float width  = PDFUtil.scaleWidth(pageWidth, 5);
//                float height = PDFUtil.scaleHeight(pageHeight, 4);
                float width  = 180;
                float height = 50;
                if(!config.getLocationSign().isEmpty()) {
                    LocationSignConvertXY locationSignConvertXY = new LocationSignConvertXY(config.getLocationSign(), pageHeight, pageWidth, height, width);
                    xLocation = locationSignConvertXY.getxLocation();
                    yLocation = locationSignConvertXY.getyLocation();
                }

                // convert tọa độ
                PdfSigner.CryptoStandard cryptoStandard = PdfSigner.CryptoStandard.CMS;
                if(config.getCryptoStandard().equalsIgnoreCase("CADES")) {
                    cryptoStandard = PdfSigner.CryptoStandard.CADES;
                }
                Rectangle rect = new Rectangle(xLocation, yLocation, width, height);
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

                int fontSize = 7;
                float resolution  = 300;
//                float scaledFontSize = fontSize * (resolution / 72f);
                appearance.setLayer2FontSize(fontSize);
                PdfFont font = PdfFontFactory.createFont(urlfont);
                appearance.setLayer2Font(font);
                appearance.setPageNumber(pageSign);

                signer.setFieldName("Signature");
                IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, ConstantUtil.PROVIDER_NAME);
                IExternalDigest digest = new BouncyCastleDigest();
                signer.signDetached(digest, pks, chain, null, null, tsaClient, 0, cryptoStandard);
                Security.removeProvider(ConstantUtil.PROVIDER_NAME);
                //giải phóng bộ nhớ
                reader.close();

                System.out.println("Done Signing ...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                float pageWidth  = pdfDoc.getPage(pageSign).getPageSize().getWidth();
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

                String noidungky =  nguoiky +"\n" +email+"\n Cơ quan: "+coquan+"\n Thời gian: "+thoigian;
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
                if(config.getSignatureType().equals("KY_SOHOA")) {
                    noidungky = "SỐ HÓA;\n" + nguoiky + ";\n" + thoigian+";";
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



}
