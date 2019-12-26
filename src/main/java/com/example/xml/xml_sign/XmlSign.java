package com.example.xml.xml_sign;

import com.example.xml.BaseEntityAudit;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity // This tells Hibernate to make a table out of this class
public class XmlSign extends BaseEntityAudit {

    @Column(unique=true, length=256, updatable = false)
    private String token;

    @Column(name="file_name", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false)
    private String fileName;

    @Column(name="physical_file_location", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false)
    private String fileLocationInServer;

    @Column(name="signer_name", columnDefinition = "varchar(255) DEFAULT 'not_set'", insertable=true, updatable = false)
    private String fileSignerName;

    @Column(name="signature", columnDefinition = "TEXT", insertable=true, updatable = false)
    private String fileSignerSignature;

    @Column(name="xml_digest", columnDefinition = "TEXT")
    private String xmlDigest;

    @Column(name="signed_xml_digest", columnDefinition = "TEXT")
    private String signedXMLDigest;

    public String getSignedXMLDigest() {
        return signedXMLDigest;
    }

    public void setSignedXMLDigest(String signedXMLDigest) {
        this.signedXMLDigest = signedXMLDigest;
    }

    public String getXmlDigest() {
        return xmlDigest;
    }

    public void setXmlDigest(String xmlDigest) {
        this.xmlDigest = xmlDigest;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getFileSignerName() {
        return fileSignerName;
    }

    public void setFileSignerName(String fileSignerName) {
        this.fileSignerName = fileSignerName;
    }

    public String getFileSignerSignature() {
        return fileSignerSignature;
    }

    public void setFileSignerSignature(String fileSignerSignature) {
        this.fileSignerSignature = fileSignerSignature;
    }
}
