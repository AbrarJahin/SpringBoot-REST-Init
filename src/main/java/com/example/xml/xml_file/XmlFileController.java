package com.example.xml.xml_file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/file")
public class XmlFileController {
    @Autowired
    private XmlFileRepository xmlFileRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    XmlFile addNewUser(@RequestParam String name
            , @RequestParam String upload_location) {
        XmlFile xmlFile = new XmlFile();
        xmlFile.setFileName(name);
        xmlFile.setFileLocationInServer(upload_location);
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
