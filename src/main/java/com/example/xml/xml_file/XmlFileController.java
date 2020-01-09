package com.example.xml.xml_file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/file")
public class XmlFileController {
    @Autowired
    private Environment env;

    @Autowired
    private XmlFileRepository xmlFileRepository;

    @PostMapping(path = "/upload")
    public @ResponseBody XmlFile addNewFile(@RequestParam("file") MultipartFile file) {
        XmlFile xmlFile = new XmlFile();
        if (!file.isEmpty()) {
            try {
                xmlFile.setFileName(file.getOriginalFilename(), Integer.parseInt(env.getProperty("file.upload-file-name-max-length-on-os").trim()));
                // read and write the file to the selected location-
                byte[] bytes = file.getBytes();
                Path path = Paths.get(env.getProperty("file.upload-dir") + xmlFile.getFileName());
                Files.write(path, bytes);
                xmlFile.setFileLocationInServer(path.toString());
                xmlFile.setFileHash(file.hashCode());
                xmlFile.setFileType(file.getContentType());
            } catch (IOException e) {
                xmlFile.setFileName(null, 0);
                e.printStackTrace();
            }
            xmlFileRepository.save(xmlFile);
        }
        return xmlFile;
    }

    @RequestMapping(value="/download/{file_name}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Resource> download(@PathVariable("file_name") String file_name) throws IOException {
        XmlFile xmlFile = xmlFileRepository.findByFileName(file_name);
        if(xmlFile == null) {
            return null;
        }
        File file = new File(xmlFile.getFileLocationInServer());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+xmlFile.getOriginalName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.valueOf(xmlFile.getFileType()))
                .body(resource);
    }

    @PostMapping(path = "/upload_signed")
    public @ResponseBody XmlFile addSignedFile(@RequestParam("file") MultipartFile file, @RequestParam("file_name") String fileName) {
        XmlFile xmlFile = xmlFileRepository.findByFileName(fileName);
        if(xmlFile == null) {
            return null;
        }
        if (!file.isEmpty()) {
            try {
                // read and write the file to the selected location-
                byte[] bytes = file.getBytes();
                Path path = Paths.get(env.getProperty("file.upload-dir") + xmlFile.randomSalt()+".sig");
                Files.write(path, bytes);
                xmlFile.setSignedFileLocationInServer(path.toString());
            } catch (IOException e) {
                xmlFile.setSignedFileLocationInServer(null);
                e.printStackTrace();
            }
            xmlFileRepository.save(xmlFile);
        }
        return xmlFile;
    }

    @RequestMapping(value="/download_signed/{file_name}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Resource> downloadSigned(@PathVariable("file_name") String file_name) throws IOException {
        XmlFile xmlFile = xmlFileRepository.findByFileName(file_name);
        if(xmlFile.getSignedFileLocationInServer() == null) {
            return null;
        }
        File file = new File(xmlFile.getSignedFileLocationInServer());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+xmlFile.getOriginalName()+".signed");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.valueOf(xmlFile.getFileType()))
                .body(resource);
    }

    // Not working
    @DeleteMapping(path = "/delete")
    @Transactional
    public @ResponseBody
    Map<String, String> deleteFile(@RequestParam String name) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        XmlFile xmlFile = xmlFileRepository.findByFileName(name);
        if(xmlFile == null) {
            map.put("error", "File Not Found in Database");
        } else {
            File file = new File(xmlFile.getFileLocationInServer());
            if(file.exists()) {
                file.delete();
                map.put("message_file", "File deleted successfully from disk");
            } else {
                map.put("message_file", "File does not exists");
            }
            xmlFileRepository.delete(xmlFile);
            map.put("message_db", "File location deleted successfully from database");
        }
        return map;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<XmlFile> getAllFiles() {
        return xmlFileRepository.findAll();
    }
}
