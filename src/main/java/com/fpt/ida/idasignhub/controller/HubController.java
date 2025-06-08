package com.fpt.ida.idasignhub.controller;

import com.fpt.ida.idasignhub.bussiness.SignHub;
import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.ConfigSignature;
import com.fpt.ida.idasignhub.data.ShowSignature;
import com.fpt.ida.idasignhub.data.model.MenuObject;
import com.fpt.ida.idasignhub.data.model.SignHubLocal;
import com.fpt.ida.idasignhub.util.SignHubLocalUtill;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import com.fpt.ida.idasignhub.data.SignDataRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.security.*;
import java.util.*;

@Controller
public class HubController {
    @GetMapping("/home")
    public String home(Model model) {
        List<MenuObject> menu = new ArrayList<>();
        MenuObject menu1 = new MenuObject();
        menu1.setSelecter(1);
        menu1.setActive(1);
        menu1.setTitle("Dashboard");
        menu1.setUrl("/home");
        menu.add(menu1);

        MenuObject menu2 = new MenuObject();
        menu2.setSelecter(0);
        menu2.setActive(0);
        menu2.setTitle("Cấu hình hệ thống");
        menu2.setUrl("/cau-hinh-ky-so");
        menu.add(menu2);

        MenuObject current_menu = menu1;
        model.addAttribute("menu",  menu);
        model.addAttribute("current_menu",  current_menu);
        return "dashboard/index";  // This will map to the home.html template
    }
    @GetMapping("/")
    public String homePage(Model model) {
        List<MenuObject> menu = new ArrayList<>();
        MenuObject menu1 = new MenuObject();
        menu1.setSelecter(1);
        menu1.setActive(1);
        menu1.setTitle("Dashboard");
        menu1.setUrl("/home");
        menu.add(menu1);

        MenuObject menu2 = new MenuObject();
        menu2.setSelecter(0);
        menu2.setActive(0);
        menu2.setTitle("Cấu hình hệ thống");
        menu2.setUrl("/cau-hinh-ky-so");
        menu.add(menu2);

        MenuObject current_menu = menu1;
        model.addAttribute("menu",  menu);
        model.addAttribute("current_menu",  current_menu);
        return "dashboard/index";  // This will map to the home.html template
    }

    @GetMapping("/cau-hinh-ky-so")
    public String cauHinhKySo(Model model) {
        List<MenuObject> menu = new ArrayList<>();
        MenuObject menu1 = new MenuObject();
        menu1.setSelecter(0);
        menu1.setActive(0);
        menu1.setTitle("Dashboard");
        menu1.setUrl("/home");
        menu.add(menu1);

        MenuObject menu2 = new MenuObject();
        menu2.setSelecter(1);
        menu2.setActive(1);
        menu2.setTitle("Cấu hình hệ thống");
        menu2.setUrl("/cau-hinh-ky-so");
        menu.add(menu2);

        MenuObject current_menu = menu2;
        model.addAttribute("menu",  menu);
        model.addAttribute("current_menu",  current_menu);
        return "config/index";  // This will map to the home.html template
    }

    @PostMapping("/saveSignType")
    public String saveStudent( @RequestParam(value = "maukyso", required = false) String maukyso,
                               @RequestParam(value = "thongtin_hienthi", required = false) String thongtin_hienthi,
                               @RequestParam(value = "trang", required = false) int trang,
                               @RequestParam(value = "vitri_trentrang", required = false) String vitri_trentrang,
                               @RequestParam("signImage") MultipartFile signImage) throws IOException {

        // bước 1 lưu file signImage vào folder chứa ảnh.
        String base64File = "";
        if(!signImage.isEmpty()) {
            byte[] imageBytes = SignHubLocalUtill.convertInputStreamToByteArray(signImage.getInputStream());
            // Chuyển đổi mảng byte thành chuỗi Base64
            base64File = Base64.getEncoder().encodeToString(imageBytes);
        }
        SignHubLocal op = SignHubLocalUtill.saveCofig(vitri_trentrang, maukyso, trang, base64File, thongtin_hienthi);
        return "redirect:/cau-hinh-ky-so";
    }
//    @MessageMapping("/signPDF")
//    @SendTo("/listener/signPDFResponse")
//    public ResponseEntity<JSONObject> signPDFResponse(SignDataRequest param) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException {
//        // Code giả lập config
//        ConfigSignature configExample = new ConfigSignature();
//        configExample.setCoutryLocation("VN");
//        configExample.setHeightSignature(50);
//        configExample.setWidthSignature(150);
//        configExample.setFontSizeSignature(7);
//        configExample.setReasonSign("Sign PDF");
//        configExample.setImgSignBase64(null);
//        configExample.setxLocation(480); // quy dinh ky so sao y la goc phai tren cung
//        configExample.setyLocation(740); // quy dinh ky so sao y la goc phai tren cung
//        configExample.setPageSign(configExample.getPageSign());
//        configExample.setCryptoStandard("CMS");
//        configExample.setTimeStampServer(ConstantUtil.IDA_BANCOYEU_TIMESTAMP_URL);
//        ShowSignature showSignature = new ShowSignature();
//        showSignature.setShowType(0);
//        showSignature.setShowInfoExtends("INFOLABEL_EMAIL_ORGANIZATION_TIMESTAMP");
//        configExample.setShowSignature(showSignature);
//        configExample.setSignatureType("KY_SAOY");
//        //End code giả lập config
//        // giả lập nguồn pdf
//        String input = ConstantUtil.IDA_TEST_INPUT_FILE;
//        //End giả lập
//        // giả lập output.
//        String output = ConstantUtil.IDA_TEST_OUTPUT_FILE;
//        // End giả lập output
//        String alias = null;
//        try {
//            KeyStore keyStore = SignHub.loadUsbToken();
//            // Lấy khóa riêng từ KeyStore
//            alias = SignHub.getAliasByKeyStore(keyStore);
//            System.out.println("alias: " + alias);
//            if (Objects.nonNull(alias)) {
//                SignHub.signPDF(keyStore, alias, input, output, configExample);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject rep = new JSONObject();
//        rep.put("status", 200);
//        rep.put("message", "Thành công");
//        rep.put("data", param);
//        return ResponseEntity.ok((JSONObject) rep);
//    }

}
