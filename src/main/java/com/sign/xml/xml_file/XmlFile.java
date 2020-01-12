package com.sign.xml.xml_file;

import com.sign.xml.BaseEntityAudit;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity // This tells Hibernate to make a table out of this class
public class XmlFile extends BaseEntityAudit {
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName, int osMaxFileNameLength) {
        this.OriginalName = fileName;
        if(osMaxFileNameLength==0) {
            this.fileName = null;
        }
        String tempFileName = OriginalName.replaceAll(" ", "");
        int tempFileNameLength = tempFileName.length();
        if(tempFileNameLength>(osMaxFileNameLength-128)) {
            tempFileName = tempFileName.substring(tempFileNameLength-(osMaxFileNameLength-128), tempFileNameLength);
            tempFileNameLength = tempFileName.length();
        }
        String randString = randomSalt(osMaxFileNameLength - tempFileNameLength);
        this.fileName = randString + tempFileName;
    }

    public String getFileLocationInServer() {
        return fileLocationInServer;
    }

    public void setFileLocationInServer(String fileLocationInServer) {
        this.fileLocationInServer = fileLocationInServer;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getFileHash() {
        return fileHash;
    }

    public void setFileHash(int fileHash) {
        this.fileHash = fileHash;
    }

    public String getOriginalName() {
        return OriginalName;
    }

    public String getSignedFileLocationInServer() {
        return signedFileLocationInServer;
    }

    public void setSignedFileLocationInServer(String signedFileLocationInServer) {
        this.signedFileLocationInServer = signedFileLocationInServer;
    }

    @Column(name="original_name", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false)
    private String OriginalName;

    @Column(name="file_name", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false, unique=true)
    private String fileName;

    @Column(name="physical_file_location", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false, unique=true)
    private String fileLocationInServer;

    @Column(name="file_type", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false)
    private String fileType;

    @Column(name="hash_code", columnDefinition = "BIGINT DEFAULT 0", insertable=true, updatable = false)
    private int fileHash;

    @Column(name="signed_physical_file_location", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, unique=true)
    private String signedFileLocationInServer;
}
