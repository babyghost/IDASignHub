package com.fpt.ida.idasignhub.validation;

import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.preflight.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * PDF/A Validator utility
 * Validates if a PDF file conforms to PDF/A standard
 */
public class PdfAValidator {

    private static final Logger logger = LoggerFactory.getLogger(PdfAValidator.class);

    /**
     * Check if a PDF file is PDF/A compliant
     *
     * @param pdfFile The PDF file to validate
     * @return true if the file is PDF/A compliant, false otherwise
     */
    public static boolean isPdfA(File pdfFile) {
        if (pdfFile == null || !pdfFile.exists()) {
            logger.warn("PDF file does not exist: {}", pdfFile);
            return false;
        }

        PreflightParser parser = null;
        PreflightDocument preflightDocument = null;

        try {
            logger.debug("Validating PDF/A compliance for file: {}", pdfFile.getAbsolutePath());

            // Create parser for the PDF file
            parser = new PreflightParser(pdfFile);

            // Parse the file and build PreflightDocument
            parser.parse();
            preflightDocument = parser.getPreflightDocument();

            // Validate the document
            preflightDocument.validate();

            // Get validation result
            ValidationResult result = preflightDocument.getResult();

            if (result.isValid()) {
                logger.debug("PDF file is PDF/A compliant: {}", pdfFile.getName());
                return true;
            } else {
                logger.debug("PDF file is not PDF/A compliant: {}. Errors: {}",
                        pdfFile.getName(), result.getErrorsList().size());
                return false;
            }

        } catch (IOException e) {
            logger.error("IO error while validating PDF/A compliance for file: {}",
                    pdfFile.getAbsolutePath(), e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while validating PDF/A compliance for file: {}",
                    pdfFile.getAbsolutePath(), e);
            return false;
        } finally {
            // Clean up resources
            if (preflightDocument != null) {
                try {
                    preflightDocument.close();
                } catch (IOException e) {
                    logger.warn("Error closing PreflightDocument", e);
                }
            }
        }
    }

    /**
     * Get detailed validation result for a PDF file
     *
     * @param pdfFile The PDF file to validate
     * @return ValidationResult with detailed information
     */
    public static ValidationResult getDetailedValidationResult(File pdfFile) {
        if (pdfFile == null || !pdfFile.exists()) {
            throw new IllegalArgumentException("PDF file does not exist: " + pdfFile);
        }

        PreflightParser parser = null;
        PreflightDocument preflightDocument = null;

        try {
            parser = new PreflightParser(pdfFile);
            parser.parse();
            preflightDocument = parser.getPreflightDocument();
            preflightDocument.validate();

            return preflightDocument.getResult();

        } catch (IOException e) {
            throw new RuntimeException("Failed to validate PDF file: " + pdfFile.getAbsolutePath(), e);
        } finally {
            if (preflightDocument != null) {
                try {
                    preflightDocument.close();
                } catch (IOException e) {
                    logger.warn("Error closing PreflightDocument", e);
                }
            }
        }
    }

    /**
     * Check if PDF/A validation is available
     * This method can be used to test if the required libraries are present
     *
     * @return true if PDF/A validation is available
     */
    public static boolean isValidationAvailable() {
        try {
            // Try to create a PreflightParser to test if libraries are available
            Class.forName("org.apache.pdfbox.preflight.PreflightDocument");
            Class.forName("org.apache.pdfbox.preflight.parser.PreflightParser");
            return true;
        } catch (ClassNotFoundException e) {
            logger.warn("PDF/A validation libraries not available", e);
            return false;
        }
    }

    /**
     * Validate PDF/A compliance with custom error handling
     *
     * @param pdfFile The PDF file to validate
     * @param throwOnError Whether to throw exception on validation errors
     * @return true if valid, false if invalid or error occurred
     */
    public static boolean isPdfA(File pdfFile, boolean throwOnError) {
        try {
            return isPdfA(pdfFile);
        } catch (Exception e) {
            if (throwOnError) {
                throw new RuntimeException("PDF/A validation failed", e);
            } else {
                logger.error("PDF/A validation failed for file: {}", pdfFile, e);
                return false;
            }
        }
    }
}