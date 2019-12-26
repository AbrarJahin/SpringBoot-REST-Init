package com.example.xml.xml_file;

import com.example.xml.BaseEntityAudit;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity // This tells Hibernate to make a table out of this class
public class XmlFile extends BaseEntityAudit {
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocationInServer() {
        return fileLocationInServer;
    }

    public void setFileLocationInServer(String fileLocationInServer) {
        this.fileLocationInServer = fileLocationInServer;
    }

    @Column(name="file_name", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false, unique=true)
    private String fileName;

    @Column(name="physical_file_location", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false)
    private String fileLocationInServer;
}
