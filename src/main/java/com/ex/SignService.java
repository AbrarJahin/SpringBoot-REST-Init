package com.ex;

import com.google.common.io.Files;
import eu.europa.esig.dss.*;
import eu.europa.esig.dss.client.tsp.OnlineTSPSource;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.util.HashSet;
import java.util.Set;



public class SignService {

    public String XML_FOLDER_PATH= "E:\\XML\\keystore-demo\\";
    public static String XML_FILE_PATH= "E:\\XML\\keystore-demo\\big.xml" ;
    public String DIGEST_PATH= "E:\\XML\\keystore-demo\\big_digest.xml";
    public String SIGN_VALUE_PATH= "E:\\XML\\keystore-demo\\big_inter_signed.xml";
    public String SIGNED_XML_PATH= "E:\\XML\\keystore-demo\\big_signed.xml" ;
// where do i get keystore service
    private KeyStoreService keyStoreService;

    public Set<String> signedFiles = new HashSet();

    public void signFiles() throws IOException, CertificateException {
    	
        for(File file : listFilesForFolder(new File(XML_FOLDER_PATH))) {
            String fileName = file.getName();
            String extension = Files.getFileExtension(fileName);


            if(signedFiles.contains(file.getName()) || extension.equals("pades") || extension.equals("xades"))
              continue;

            if(extension.equals("xml")) {
                signedFiles.add(fileName);
                signXML(file);
            }

        }
    }

    private File[] listFilesForFolder(final File folder) {
       return folder.listFiles();
    }

    private void signXML(File file) throws IOException, CertificateException {
    	  keyStoreService = new KeyStoreService();
    	  DSSPrivateKeyEntry privateKey= keyStoreService.getPrivateKey() ;
    	  DSSDocument toSignDocument = new FileDocument(file);
        
        /* client should send the certificateChain as Json... suppose the string is certsJson
          String  certsJson= "";
          // call the util method to convert the certsJson string to CertificateTokens
          CertificateToken[] certificateChain=Util.getCertificateTokensFromJsonString(certsJson);
          */
          
        XAdESSignatureParameters parameters = new XAdESSignatureParameters ();
       
        parameters.setSignatureLevel(SignatureLevel.XAdES_BASELINE_T);
        parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED); // signature part of PDF
        // get digest algorithm from client
        parameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        
        // get certificates from client
        parameters.setSigningCertificate(privateKey.getCertificate());
        parameters.setCertificateChain(privateKey.getCertificateChain());
        
        /* use the certificate chain that we obtained from the client and use the first element of the chain as signing certificate 
        parameters.setCertificateChain(certificateChain);
        parameters.setSigningCertificate(certificateChain[0]);
*/


        // For LT-level signatures, we would need a TrustedListCertificateVerifier, but for level T,
        // a CommonCertificateVerifier is enough. (CookBook v 2.2 pg 28)
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        XAdESService service = new XAdESService (commonCertificateVerifier);

        // For now, just hard-code one specific time stamp server (the same as DSS demo app uses by default)
       
        OnlineTSPSource tspSource = new OnlineTSPSource("http://tsa.belgium.be/connect");
        service.setTspSource(tspSource);
        
        
      //SERVER SIDE.
        // Get the SignedInfo XML segment that need to be signed.
        ToBeSigned dataToSign = service.getDataToSign(toSignDocument, parameters);
        FileUtils.writeByteArrayToFile(new File(DIGEST_PATH), dataToSign.getBytes()) ;
        // send the bytes value of dataToSign to the client

        DigestAlgorithm digestAlgorithm = parameters.getDigestAlgorithm();

      //CLIENT SIDE
     // This function obtains the signature value for signed information using the
     // private key and specified algorithm
        // replace the following with a client call that sends the bytes value of dataToSign and gets back the signed value

       // SignatureValue signatureValue = keyStoreService.getSigningToken().sign(dataToSign, digestAlgorithm, privateKey);
       // FileUtils.writeByteArrayToFile(new File("D:\\CA materials\\keystore-demo\\big_inter_signed_real.xml"), signatureValue.getValue()) ;

        System.in.read();
      byte[] bytes=  FileUtils.readFileToByteArray(new File(SIGN_VALUE_PATH)) ;
        SignatureValue signatureValueNew = new SignatureValue(SignatureAlgorithm.RSA_SHA256, bytes);
  
    
     // SERVER SIDE
     // We invoke the service to sign the document with the signature value obtained in
     // the previous step.
        DSSDocument signedDocument = service.signDocument(toSignDocument, parameters, signatureValueNew);

        signedDocument.save(SIGNED_XML_PATH);
        System.out.println(XML_FOLDER_PATH +  "big_signed.xml");
        //signedDocument.save(FOLDER_PATH + file.getName() + ".xades");
    }
 
    public static void main(String[] args) throws Exception {
 			// key-store. private key and cert
 				//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
// 		        FileInputStream keystoreStream = new FileInputStream("D:\\CA git repo\\xml signing\\Java-XML-Signature\\target\\test-classes\\certificate.p12");
// 		      System.out.println(keystoreStream);
    	System.out.println(org.apache.xpath.compiler.FunctionTable. class.getProtectionDomain().getCodeSource());
    	for (Method method : org.apache.xpath.compiler.FunctionTable.class.getDeclaredMethods()) {
    	    String name = method.getName();
    	    System.out.println(name);
    	}
    			SignService test= new SignService();

    			File unsignedXml= new File(XML_FILE_PATH);
 				test.signXML(unsignedXml);
 	        }
}
