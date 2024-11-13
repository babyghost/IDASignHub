package com.fpt.ida.idasignhub.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.util.List;

public class PDFUtil {
    public static PDPage pdfLandscapeMode(PDPage page) {
        PDRectangle mediaBox = page.getMediaBox();
        // Đổi chiều rộng và chiều cao để tạo trang landscape
        page.setMediaBox(new PDRectangle(mediaBox.getHeight(), mediaBox.getWidth()));
        return page;
    }

}
