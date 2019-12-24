package com.example.xml.xml_sign;

import com.example.xml.BaseEntityAudit;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity // This tells Hibernate to make a table out of this class
public class XmlSign extends BaseEntityAudit {

    @Column(unique=true, length=256)
    private String token;

    @Column(columnDefinition = "TEXT")
    private String xmlDigest;

    @Column(columnDefinition = "TEXT")
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
}
