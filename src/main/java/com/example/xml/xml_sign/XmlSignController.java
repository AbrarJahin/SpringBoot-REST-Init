package com.example.xml.xml_sign;

import com.ex.Util;
import com.example.xml.xml_file.XmlFile;
import com.example.xml.xml_file.XmlFileRepository;
import com.service.GlobalVariableService;
import com.service.SignState;
import eu.europa.esig.dss.*;
import eu.europa.esig.dss.client.tsp.OnlineTSPSource;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.util.Base64;

@Controller    // This means that this class is a Controller
@RequestMapping(path = "/xml") // This means URL's start with /demo (after Application path)
public class XmlSignController {
    @Autowired // This means to get the bean called xmlSignRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private XmlSignRepository xmlSignRepository;

    @Autowired
    private XmlFileRepository xmlFileRepository;

    //////    1st Rest Call From Client     //////////////////////////////////////////////////////////
    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    XmlSign addNewXmlSign(@RequestParam String file_name
            ,@RequestParam String certificate_chain) throws CertificateException, UnsupportedEncodingException {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        XmlFile xmlFile = xmlFileRepository.findByFileName(file_name);
        if(xmlFile == null) {
            return null;
        }
        XmlSign xmlSign = new XmlSign();
        xmlSign.setFileName(xmlFile.getFileName()); /////////////////////////////
        xmlSign.setFileSignerCertificateChain(certificate_chain);

        File file = new File(xmlFile.getFileLocationInServer());
        CertificateToken[] certificateTokens = Util.getCertificateTokensFromJsonString(certificate_chain);

        DSSDocument toSignDocument = new FileDocument(file);
        XAdESSignatureParameters parameters = new XAdESSignatureParameters ();

        parameters.setSignatureLevel(SignatureLevel.XAdES_BASELINE_T);
        parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED); // signature part of PDF
        // get digest algorithm from client
        parameters.setDigestAlgorithm(DigestAlgorithm.SHA256);

        // get certificates from client
        parameters.setSigningCertificate(certificateTokens[0]);
        parameters.setCertificateChain(certificateTokens);

        // For LT-level signatures, we would need a TrustedListCertificateVerifier, but for level T,
        // a CommonCertificateVerifier is enough. (CookBook v 2.2 pg 28)
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        XAdESService service = new XAdESService (commonCertificateVerifier);

        // For now, just hard-code one specific time stamp server (the same as DSS demo app uses by default)

        OnlineTSPSource tspSource = new OnlineTSPSource("http://tsa.belgium.be/connect");
        service.setTspSource(tspSource);

        // Get the SignedInfo XML segment that need to be signed.
        ToBeSigned dataToSign = service.getDataToSign(toSignDocument, parameters);
        SignState signState = new SignState(parameters, service);
        GlobalVariableService.putStateByUid(xmlSign.getToken(),signState);
        xmlSign.setXmlUnsignedDigest(new String(dataToSign.getBytes(), "UTF-8"));
        xmlSignRepository.save(xmlSign);
        return xmlSign;
    }

    //////    2nd Rest Call From Client     //////////////////////////////////////////////////////////
    @PostMapping(path = "/get") // Map ONLY POST Requests
    public @ResponseBody    //Optional<XmlSign>
    XmlSign getXmlSignByToken(@RequestParam String token, @RequestParam String signed_digest) throws CertificateException, IOException {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        XmlSign xmlSign =  xmlSignRepository.findByToken(token);
        if(xmlSign == null) {
            return null;
        }
        xmlSign.setXmlSignedDigest(signed_digest);
        String xmlFileServerLocation = xmlFileRepository.findByFileName(xmlSign.getFileName()).getFileLocationInServer();
        File file = new File(xmlFileServerLocation);
        DSSDocument toSignDocument = new FileDocument(file);

        /* client should send the certificateChain as Json... suppose the string is certsJson */
        SignState signState = GlobalVariableService.getStateByUid(token);

        XAdESSignatureParameters parameters = signState.xAdESSignatureParameters;

        /* use the certificate chain that we obtained from the client and use the first element of the chain as signing certificate */
        // For LT-level signatures, we would need a TrustedListCertificateVerifier, but for level T,
        // a CommonCertificateVerifier is enough. (CookBook v 2.2 pg 28)
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        XAdESService service = signState.xAdESService;

        DigestAlgorithm digestAlgorithm = parameters.getDigestAlgorithm();

        byte[] bytes = Base64.getDecoder().decode(signed_digest);
        SignatureValue signatureValueNew = new SignatureValue(SignatureAlgorithm.RSA_SHA256, bytes);
            // We invoke the service to sign the document with the signature value obtained in
            // the previous step.
        DSSDocument signedDocument = service.signDocument(toSignDocument, parameters, signatureValueNew);

        signedDocument.save(xmlFileServerLocation + ".sig");
        xmlSignRepository.save(xmlSign);
        return xmlSign;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<XmlSign> getAllUsers() {
        return xmlSignRepository.findAll();
    }
}
