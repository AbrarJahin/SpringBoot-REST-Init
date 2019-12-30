package com.ex;

//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;

import java.util.Date;

 
//@ToString
public class SignFile {

    private long id;
    private String filename;
    private long companyId;
    private Date modificationDate;
    private byte[] data;

    public SignFile(long id, String filename, long companyId, Date modificationDate, byte[] data) {
        this.id = id;
        this.filename = filename;
        this.companyId = companyId;
        this.modificationDate = modificationDate;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public long getCompanyId() {
        return companyId;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public byte[] getData() {
        return data;
    }
}
