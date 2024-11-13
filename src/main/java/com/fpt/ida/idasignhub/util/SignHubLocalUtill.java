package com.fpt.ida.idasignhub.util;

import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.mock.BoundingBox;
import com.fpt.ida.idasignhub.data.mock.OCRGoogleMockData;
import com.fpt.ida.idasignhub.data.model.SignHubLocal;
import com.fpt.ida.idasignhub.data.model.SignHubLocalType;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class SignHubLocalUtill {
    public static void makePDF2LayerFromMockData() throws IOException {
        // mockup
        List<OCRGoogleMockData> orc_result = new ArrayList<>();
        OCRGoogleMockData data1 = new OCRGoogleMockData();
        data1.setText("虞經刑論遺味未富造");
        BoundingBox box1 = new BoundingBox();
        box1.setX(10);
        box1.setY(20);
        box1.setWidth(50);
        box1.setHeight(20);
        data1.setBoundingBox(box1);
        orc_result.add(data1);
        OCRGoogleMockData data2 = new OCRGoogleMockData();
        data2.setText("處於幽遠之天邊希文");
        BoundingBox box2 = new BoundingBox();
        box2.setX(70);
        box2.setY(20);
        box2.setWidth(60);
        box2.setHeight(20);
        data2.setBoundingBox(box2);
        orc_result.add(data2);
        OCRGoogleMockData data3 = new OCRGoogleMockData();
        data3.setText("上表其事失未推概稱");
        BoundingBox box3 = new BoundingBox();
        box3.setX(10);
        box3.setY(60);
        box3.setWidth(70);
        box3.setHeight(20);
        data3.setBoundingBox(box3);
        orc_result.add(data3);
        // tạo file pdf
        String fileInput = "D:/IDASignHubTool/pdf_mock/bandap_02.png";
        String fileOutput = "D:/IDASignHubTool/pdf_mock/bandap_2lop.pdf";
        String fontPath = "D:/IDASignHubTool/font/hannom/NomNaTongLight.ttf";
//        PdfReader reader = new PdfReader(fileInput);
//        ImageData bandap_png = ImageDataFactory.create(fileInput);

//        PdfWriter writer = new PdfWriter(fileOutput);
//        PdfSigner signer = new PdfSigner(reader, writer, new StampingProperties());
//        PdfDocument pdfDocument = new PdfDocument(writer);
//        pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
//        Document document = new Document(pdfDocument);
//        Image image = new Image(bandap_png);
        // Lấy chiều rộng và chiều cao tối đa có thể của ảnh trong trang PDF
//        float maxWidth = pdfDocument.getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin();
//        float maxHeight = pdfDocument.getDefaultPageSize().getHeight() - document.getTopMargin() - document.getBottomMargin();
//        image.scaleToFit(maxHeight, maxWidth);


//        document.add(image);
//
//        PdfPage page = pdfDocument.addNewPage();
//        PdfCanvas canvas = new PdfCanvas(page);
//
//        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
//        for (OCRGoogleMockData ocrData : orc_result) {
//            // Đặt màu chữ và font
//            canvas.beginText()
//                    .setFontAndSize(font, 10)
//                    .setColor(ColorConstants.BLACK, true);
//            // Đặt tọa độ và nội dung văn bản từ dữ liệu OCR
//            canvas.moveText(ocrData.getBoundingBox().getX(), ocrData.getBoundingBox().getY())
//                    .showText(ocrData.getText())
//                    .endText();
//        }
//        document.close();
        try (PDDocument document = new PDDocument()) {
            // Tạo trang PDF và thêm vào tài liệu
            PDPage page = new PDPage(PDRectangle.A4);
            PDFUtil.pdfLandscapeMode(page);
            document.addPage(page);

            // Thêm hình ảnh nền vào trang PDF
            PDImageXObject image = PDImageXObject.createFromFile(fileInput, document);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(image, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

            // Đặt font và kích thước cho lớp văn bản\
            PDType0Font customFont = PDType0Font.load(document, new File(fontPath));
            contentStream.setFont(customFont, 12);
            contentStream.setNonStrokingColor(0, 0, 0); // Đặt màu đen cho văn bản

            // Thêm từng đoạn văn bản vào vị trí cụ thể
            for (OCRGoogleMockData ocrData : orc_result) {
                contentStream.beginText();
                contentStream.newLineAtOffset(ocrData.getBoundingBox().getX(), ocrData.getBoundingBox().getY());
                contentStream.showText(ocrData.getText());
                contentStream.endText();
            }

            // Đóng contentStream để lưu thay đổi vào trang
            contentStream.close();

            // Lưu tài liệu PDF
            document.save(fileOutput);

            System.out.println("PDF with searchable layer created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("PDF with searchable text layer created successfully.");
    }
    public static String swtichTrangThaiHoatDong(int index) {
        //B1. Kiem tra có file config ko, có thì lấy dữ liệu ra insert thêm, ko thì tạo mới file rùi insert zô
        String pathFile = ConstantUtil.IDA_CONFIG_LOCAL;
        String result = "Update thành công";
        try {
            File file = new File(pathFile);
            SignHubLocal object = new SignHubLocal();
            if(file.exists()) {
                try (FileInputStream fis = new FileInputStream(pathFile);
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    // Đọc object từ file
                    object = (SignHubLocal) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            List<SignHubLocalType> arr = new ArrayList<>();
            if(object != null && object.getConfig() != null && !object.getConfig().isEmpty()) {
                arr = object.getConfig();
            }
            for(int i = 0; i < arr.size(); i++) {
                if(i == index) {
                    arr.get(i).setTrangThaiHoatDong(true);
                } else {
                    arr.get(i).setTrangThaiHoatDong(false);
                }
            }
            object.setConfig(arr);
            try (FileOutputStream fileOut = new FileOutputStream(pathFile);
                 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
                objectOut.writeObject(object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            result = e.getMessage();
        }

        return result;
    }
    public static SignHubLocal getConfigFromFileConfig() throws IOException {
        String pathFile = ConstantUtil.IDA_CONFIG_LOCAL;
        try (FileInputStream fis = new FileInputStream(pathFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // Đọc object từ file
            SignHubLocal obj = (SignHubLocal) ois.readObject();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static SignHubLocal saveCofig(String vitri, String tenMauChuKy, int trang,
                                       String imagePath, String showInfo) throws IOException {
        String pathFile = ConstantUtil.IDA_CONFIG_LOCAL;
        //B1. Kiem tra có file config ko, có thì lấy dữ liệu ra insert thêm, ko thì tạo mới file rùi insert zô
        File file = new File(pathFile);
        SignHubLocal object = new SignHubLocal();
        if(file.exists()) {
            try (FileInputStream fis = new FileInputStream(pathFile);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                // Đọc object từ file
                object = (SignHubLocal) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        List<SignHubLocalType> arr = new ArrayList<>();
        if(object != null && object.getConfig() != null && !object.getConfig().isEmpty()) {
            arr = object.getConfig();
        }
        SignHubLocalType item = new SignHubLocalType();
        item.setLocationSignHubLocalType(vitri);
        item.setNameSignHubLocalType(tenMauChuKy);
        item.setPageNumberSignHubLocalType(trang);
        item.setPathImageSignHubLocalType(imagePath);
        item.setShowInfoSignHubLocalType(showInfo);
        item.setTrangThaiHoatDong(false);
        arr.add(item);

        object.setConfig(arr);
        try (FileOutputStream fileOut = new FileOutputStream(pathFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }
    // Hàm chuyển InputStream thành byte[]
    public static byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // Bộ đệm tạm thời để đọc dữ liệu
        int bytesRead;

        // Đọc dữ liệu từ InputStream và ghi vào ByteArrayOutputStream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // Đóng ByteArrayOutputStream và trả về mảng byte
        return byteArrayOutputStream.toByteArray();
    }
}

