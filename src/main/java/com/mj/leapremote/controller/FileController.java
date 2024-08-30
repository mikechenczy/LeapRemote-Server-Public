package com.mj.leapremote.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

/**
 * @author Mike_Chen
 * @date 2023/7/10
 * @apiNote
 */
@RestController
public class FileController {
    public ResponseEntity<Resource> getFile(String name) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, name)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource("static/"+name));
    }

    public ResponseEntity<FileSystemResource> getFile(FileSystemResource fileSystemResource) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileSystemResource.getFilename());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        try {
            return ResponseEntity.ok().headers(headers).contentLength(fileSystemResource.contentLength()).contentType(MediaType.parseMediaType("application/octet-stream")).body(fileSystemResource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/{file_name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<FileSystemResource> file(@PathVariable("file_name") String fileName) {
        FileSystemResource fileSystemResource = new FileSystemResource("static/"+fileName);
        if(fileSystemResource.exists() && fileSystemResource.isFile())
            return getFile(fileSystemResource);
        return null;
    }
}
