package com.system.controller.img;


import com.system.domain.Process;
import com.system.domain.Result;
import com.system.domain.User;
import com.system.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * 上传文件映射表
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private ProcessService processService;


    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping("/Adminupload")
    public Result Adminupload(@RequestParam("file") MultipartFile file,
                              String description, String submitter, Integer id
    , HttpSession session) throws Exception {
        if (file.isEmpty()) {

        }

        String userDir = System.getProperty("user.dir");

        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String type = file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf('.'));
        String value = "\\src\\main\\resources\\image\\";
        String valueClass = "\\target\\classes\\image\\";

        InputStream in = file.getInputStream();
        FileOutputStream out = new FileOutputStream(userDir + value + name + type);
        for (int c = 0; (c = in.read()) != -1; ) {
            out.write(c);
        }

        FileOutputStream out2 = new FileOutputStream(userDir + valueClass + name + type);
        InputStream in2 = file.getInputStream();

        try {
            for (int c = 0; (c = in2.read()) != -1; ) {
                out2.write(c);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User info = (User)session.getAttribute("info");

        String str = "http://localhost:8081/" + "image\\" + name + type;
        Process process = new Process();
        process.setName(submitter);
        process.setRid(id);
        process.setUid(info.getId());
        process.setNode(description);
        process.setFile(str);
        process.setApptime(new Date());
        processService.save(process);


        return Result.ok(str);
    }


    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("avatar") MultipartFile file) throws Exception {
        if (file.isEmpty()) {

        }

        String userDir = System.getProperty("user.dir");

        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String type = file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf('.'));
        String value = "\\src\\main\\resources\\image\\";
        String valueClass = "\\target\\classes\\image\\";

        InputStream in = file.getInputStream();
        FileOutputStream out = new FileOutputStream(userDir + value + name + type);
        for (int c = 0; (c = in.read()) != -1; ) {
            out.write(c);
        }

        FileOutputStream out2 = new FileOutputStream(userDir + valueClass + name + type);
        InputStream in2 = file.getInputStream();

        try {
            for (int c = 0; (c = in2.read()) != -1; ) {
                out2.write(c);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String str = "http://localhost:8081/" + "image\\" + name + type;
        return Result.ok(str);
    }


}
