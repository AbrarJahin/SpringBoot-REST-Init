package com.example.xml.xml_file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping(path = "/file")
public class XmlFileController {
    @Autowired
    private Environment env;

    @Autowired
    private XmlFileRepository xmlFileRepository;

    @PostMapping(path = "/add")
    public @ResponseBody XmlFile addNewUser(@RequestParam("file") MultipartFile file) {
        XmlFile xmlFile = new XmlFile();
        if (!file.isEmpty()) {
            //return new ModelAndView("status", "message", "Please select a file and try again");

            try {
                xmlFile.setFileName(file.getOriginalFilename(), Integer.parseInt(env.getProperty("file.upload-file-name-max-length-on-os").trim()));

                // read and write the file to the selected location-
                byte[] bytes = file.getBytes();
                Path path = Paths.get(env.getProperty("file.upload-dir") + xmlFile.getFileName());
                Files.write(path, bytes);
                xmlFile.setFileLocationInServer(env.getProperty("file.upload-dir") + xmlFile.getFileName());
                xmlFile.setFileHash(file.hashCode());
                xmlFile.setFileType(file.getContentType());
            } catch (IOException e) {
                xmlFile.setFileName(null, 0);
                e.printStackTrace();
            }
        }
        xmlFileRepository.save(xmlFile);
        return xmlFile;
    }

    // Not working
    @DeleteMapping(path = "/delete")
    @Transactional
    public @ResponseBody XmlFile deleteFile(@RequestParam String name) {
        return xmlFileRepository.deleteByFileName(name);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<XmlFile> getAllFiles() {
        return xmlFileRepository.findAll();
    }
}
