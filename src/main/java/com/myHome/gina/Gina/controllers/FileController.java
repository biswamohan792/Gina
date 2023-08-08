package com.myHome.gina.Gina.controllers;

import com.myHome.gina.Gina.constants.FileConstants;
import com.myHome.gina.Gina.utils.AuthUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RequestMapping("/file")
@CrossOrigin(value = "*")
@RestController
public class FileController {

    @PostMapping("/upload")
    public Map upload(@RequestParam("file")  MultipartFile file){
        try {
            String fileName = AuthUtils.getRandomId(10).concat(".png");
            file.transferTo(Path.of(FileConstants.FILE_SAVE_PATH, fileName));
            return Map.of("success",true,"data",fileName);
        }catch (Exception e){
            e.printStackTrace();
            return Map.of("success",false);
        }
    }

    @GetMapping("/pic")
    public void getImage(@RequestParam("name") String name, HttpServletResponse res){
        try {
            Path imagePath = Paths.get(FileConstants.FILE_SAVE_PATH, name);
            InputStream inputStream = new FileInputStream(imagePath.toString());
            res.setContentType(MediaType.IMAGE_PNG_VALUE);
            res.setStatus(HttpStatus.OK.value());
            StreamUtils.copy(inputStream,res.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            res.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}
