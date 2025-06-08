package com.fpt.ida.idasignhub.validation;

import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.parser.PreflightParser;

import java.io.File;
import java.io.IOException;

public class PdfAValidator {
    public static boolean isPdfA(File pdfFile) {
        PreflightParser parser = null;
        PreflightDocument preflightDocument = null;

        try {
            parser = new PreflightParser(pdfFile);
            // Phân tích file, build PreflightDocument
            parser.parse();

            preflightDocument = parser.getPreflightDocument();
            preflightDocument.validate();
            // Nếu chạy tới đây mà không ném exception nghĩa là hợp lệ PDF/A
            return true;
        } catch (IOException e) {
            // Lỗi IO (file không tồn tại, không đọc được)
            e.printStackTrace();
            return false;
        } finally {
            if (preflightDocument != null) {
                try {
                    preflightDocument.close();
                } catch (IOException ignored) { }
            }
        }
    }

}
