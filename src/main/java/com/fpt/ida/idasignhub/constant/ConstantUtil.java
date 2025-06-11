package com.fpt.ida.idasignhub.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConstantUtil {
    private static final Path rootLocation    = Paths.get("IDASignHubTool");
    public static String IDA_ROOT_FOLDER      = rootLocation.toString();
    public static String IDA_CUSTOM_FOLDER      = "F:/";
//    public static String IDA_FOLDER_ENDPOINT    = "/Done_ThanhKhe/Merge_done";
public static String IDA_FOLDER_ENDPOINT    = "/Done_HoaPhu";
    public static String IDA_FOLDER_ENDPOINT_2  = "F:\\Done_HoaPhu";
//    public static String IDA_FOLDER_ENDPOINT_2  = "F:\\Done_ThanhKhe\\Merge_done";
    public static String IDA_CONFIG_FOLDER    = IDA_ROOT_FOLDER + "/config";
    public static String IDA_DRIVER_TOKEN_USB_URL = IDA_ROOT_FOLDER + "/config/pkcs11.cfg";
    public static String IDA_FONT_URL = IDA_ROOT_FOLDER + "/font/arial-unicode-ms.ttf";
    public static String IDA_ZIP_SIGNATURE_RESULT_FOLDER = IDA_ROOT_FOLDER + "/sign";
    public static String IDA_TEST_INPUT_FILE        = IDA_ROOT_FOLDER + "/test_pdf.png";
    public static String IDA_TEST_OUTPUT_FILE       = IDA_ROOT_FOLDER + "/test_pdf_sign.png";
    public static String PROVIDER_NAME              = "SunPKCS11-VGCAToken";

    public static String IDA_BANCOYEU_TIMESTAMP_URL = "http://tsa.ca.gov.vn";
    public static String IDA_IMAGE_QUOCHUY_URL      = IDA_ROOT_FOLDER + "/source/quochuy.png";
    public static String IDA_IMAGE_LOGO_URL         = IDA_ROOT_FOLDER + "/source/logo.png";
    public static String IDA_IMAGE_LOGO_URL_A         = IDA_ROOT_FOLDER + "/source/logo.jpg";
    public static String IDA_IMAGE_LOGO_DRAW_URL    = IDA_ROOT_FOLDER + "/source/output_signature.png";
    public static String IDA_CONFIG_LOCAL  = IDA_ROOT_FOLDER + "/config/config.dat";
    public static String IDA_FILE_TYPE_PNG = "png";
    public static String IDA_SIMPLEDATEFORMAT_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
    public static String IDA_UPLOADFILE_FOLDER = IDA_ROOT_FOLDER + "/uploads";
    public static String IDA_TMP_FOLDER = IDA_ROOT_FOLDER + "/tmp";
}
