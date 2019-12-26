package com.example.xml.xml_file;

import org.springframework.data.repository.CrudRepository;

public interface XmlFileRepository extends CrudRepository<XmlFile, Integer> {
    XmlFile deleteByFileName(String name);
}
