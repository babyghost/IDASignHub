package com.fpt.ida.idasignhub.sheduler;
import com.fpt.ida.idasignhub.bussiness.SignHub;
import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.util.FileUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

@EnableScheduling
@Component
public class SignHubScheduler {

//    @Scheduled(fixedRate = 60 * 1000)
//    public void runKySoAuto() throws IOException {
//        System.out.println("Starting scheduled sign hub file...");
//        SignHub.signPDFAuto();
//        System.out.println("End scheduled sign hub file...");
//    }
//    @Scheduled(fixedRate = 60 * 1000)
    public void runDowloadFileAuto() throws IOException {
        String folderChuaKy = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filecanky";
        File folder_root = new File(folderChuaKy);
        System.out.println("Starting scheduled download file...");
        FileUtils.downloadFilesFromFTP(folder_root.toPath());
        System.out.println("End scheduled download file...");
    }
}
