package com.sign.xml.xml_sign;

import com.sign.xml.BaseEntityAudit;
import javax.persistence.Column;
import javax.persistence.Entity;

//Sign entity for split mode

@Entity // This tells Hibernate to make a table out of this class
public class XmlSign extends BaseEntityAudit {
    @Column(unique=true, length=128, updatable = false)
    private String token;

    @Column(name="file_name", columnDefinition = "varchar(1000) DEFAULT 'not_set'", insertable=true, updatable = false)
    private String fileName;

    @Column(name="signer_certificate_chain", columnDefinition = "TEXT", insertable=true, updatable = false)
    private String fileSignerCertificateChain;

    @Column(name="xml_digest", columnDefinition = "TEXT")
    private String xmlUnsignedDigest;

    @Column(name="signed_xml_digest", columnDefinition = "TEXT")
    private String xmlSignedDigest;

    public XmlSign() {
        token = randomSalt();
    }

    public String getXmlSignedDigest() {
        return xmlSignedDigest;
    }

    public void setXmlSignedDigest(String xmlSignedDigest) {
        this.xmlSignedDigest = xmlSignedDigest;
    }

    public String getXmlUnsignedDigest() {
        return xmlUnsignedDigest;
    }

    public void setXmlUnsignedDigest(String xmlUnsignedDigest) {
        this.xmlUnsignedDigest = xmlUnsignedDigest;
    }

    public String getToken() {
        return token;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSignerCertificateChain() {
        return fileSignerCertificateChain;
    }

    public void setFileSignerCertificateChain(String fileSignerCertificateChain) {
        this.fileSignerCertificateChain = fileSignerCertificateChain;
    }
}
