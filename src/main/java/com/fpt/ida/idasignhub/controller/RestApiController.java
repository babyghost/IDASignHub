package com.fpt.ida.idasignhub.controller;

import com.fpt.ida.idasignhub.bussiness.SignHub;
import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.ConfigDataRequest;
import com.fpt.ida.idasignhub.data.ConfigSignature;
import com.fpt.ida.idasignhub.data.ShowSignature;
import com.fpt.ida.idasignhub.data.model.SignHubLocal;
import com.fpt.ida.idasignhub.util.SignHubLocalUtill;
import com.fpt.ida.idasignhub.util.SignHubUtill;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class RestApiController {

    @GetMapping("/api/makePDF2Layer")
    public ResponseEntity<JSONObject> makePDF2Layer() throws IOException {
        SignHubLocalUtill.makePDF2LayerFromMockData();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Tao file PDF thanh cong");
        return ResponseEntity.ok((JSONObject) rep);
    }
    @GetMapping("/api/list/config")
    public ResponseEntity<JSONObject> getConfigLocal() throws IOException {
        SignHubLocal obj = SignHubLocalUtill.getConfigFromFileConfig();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Danh sách cấu hình");
        rep.put("requestObject", obj);
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping(value = "/api/switch/trangthai", consumes = {"application/json"})
    public ResponseEntity<JSONObject> switchTrangThaiHoatDong(@RequestBody ConfigDataRequest param) {
        String mess =  SignHubLocalUtill.swtichTrangThaiHoatDong(param.getIndex());
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", mess);
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("/api/signPDFs")
    public ResponseEntity<JSONObject> signPDFs(
            @RequestParam(value = "xLocation", required = false) String xLocation,
            @RequestParam(value = "yLocation", required = false) String yLocation,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "fontSize", required = false) Integer fontSize,
            @RequestParam(value = "pageSign", required = false) Integer pageSign,
            @RequestParam(value = "showType", required = false) String showType,
            @RequestParam(value = "locationSign", required = false) String locationSign,
            @RequestParam(value = "signatureType", required = false) String signatureType,  // KY_SAOY; KY_SAOLUC; KY_TRICHSAO;
            @RequestParam(value = "signatureImg", required = false) MultipartFile signatureImg,
            @RequestParam(value = "signatureImgBase64", required = false) String signatureImgBase64,
            @RequestParam("file") MultipartFile uploadFile,
            HttpServletRequest request) {
        int st = 0;
        // kiểu hiển thị. 0,1,2 => 0 = hiển thị ảnh và mô tả, 1 chỉ hiển thị ảnh, 2 chỉ hiển thị mô tả.
        if(showType.equals("THONGTIN")) {
            st = 2;
        } else if(showType.equals("HINHANH")) {
            st = 1;
        }
        ConfigSignature configObject = SignHubUtill.generateConfigSignature(height, width, fontSize, xLocation, yLocation, pageSign, signatureImgBase64, signatureType, st, locationSign);

        JSONObject rep  = new JSONObject();
        String ext      = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
        String fileName = FilenameUtils.getName(uploadFile.getOriginalFilename());
        Path pathSaveFileTmp = Paths.get(ConstantUtil.IDA_UPLOADFILE_FOLDER);
        BufferedOutputStream stream = null;
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        String file_tmp = randomUUIDString + "." + ext;

        try {
            // Bước 1: Save file zip vao thư mục upload
            String filePath = Paths.get(ConstantUtil.IDA_UPLOADFILE_FOLDER, file_tmp).toString();
            Files.createDirectories(pathSaveFileTmp);
            stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(uploadFile.getBytes());
            stream.close();
            List<String> arr = new ArrayList<String>();
            if(ext != null && ext.equalsIgnoreCase("zip")) {
                String folder_tmp_path = SignHubUtill.unzipFile(filePath);
                arr = SignHub.signPDFFromFolder(folder_tmp_path, configObject);
                String zipFolder = ConstantUtil.IDA_ROOT_FOLDER + "/result";
                SignHubUtill.zipArrayResult(zipFolder, arr);
                SignHubUtill.deleteFileFromParentFolder(ConstantUtil.IDA_ROOT_FOLDER + "/sign");
            } else if(ext != null && ext.equalsIgnoreCase("pdf")) {
                String tmpFolder  = ConstantUtil.IDA_TMP_FOLDER;
                int soLuongFolder = SignHubUtill.countFolderFromParentFolder(tmpFolder);
                String nameFolder = tmpFolder+"/tmp_"+soLuongFolder;
                File dir = new File(nameFolder);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                UUID uuids = UUID.randomUUID();
                String randomUUIDStrings = uuids.toString();
                stream = new BufferedOutputStream(new FileOutputStream(new File(nameFolder+"/"+uploadFile.getName()+"_"+randomUUIDStrings+"."+ext)));
                stream.write(uploadFile.getBytes());
                stream.close();
                arr = SignHub.signPDFFromFolder(nameFolder, configObject);
                String zipFolder = ConstantUtil.IDA_ROOT_FOLDER + "/result";
                //zippppppppppp
                SignHubUtill.zipArrayResult(zipFolder, arr, fileName);
                SignHubUtill.deleteFileFromParentFolder(ConstantUtil.IDA_ROOT_FOLDER + "/sign");
            }

            rep.put("status", 200);
            rep.put("message", "Ký số thành công");
            rep.put("count", arr.size());
        } catch (IOException e) {
            e.printStackTrace();
            rep.put("status", 500);
            rep.put("message", "Ký số thất bại");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.ok((JSONObject) rep);
    }
}
