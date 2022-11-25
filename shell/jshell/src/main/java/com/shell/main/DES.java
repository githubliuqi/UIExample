package com.shell.main;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {
	
   private static boolean isOpenLog = false;
	
	public static int DES = 0x01;
	public static int DES112 = 0x02;
	public static int DES168 = 0x03;
	
	public static int ECB = 0x01;
	public static int CBC = 0x02;
	public static byte[] enc(int nAlg, int nMode, final byte[] pbKey, 
			final byte[] pbIV, final byte[] pbData) 
			throws Exception{
		byte[] pbEncData = null;
		String strAlg = (DES == nAlg)?"DES":"DESede";
		String strMode = (ECB==nMode)?"ECB":"CBC";
		byte[] pbKeyIn = null;
		byte[] pbPadding = null;
		int nPaddingLen;
		
		if(DES == nAlg){
			pbKeyIn = new byte[0x08];
			System.arraycopy(pbKey, 0, pbKeyIn, 0, 0x08);
		}
		else if(DES112 == nAlg){
			pbKeyIn = new byte[0x18];
			System.arraycopy(pbKey, 0, pbKeyIn, 0, 0x10);
			System.arraycopy(pbKey, 0, pbKeyIn, 0x10, 0x08);
		}
		else{
			pbKeyIn = new byte[0x18];
			System.arraycopy(pbKey, 0, pbKeyIn, 0, 0x18);
		}
			
		// pkcs5 
		nPaddingLen = (8-pbData.length%8);
		pbPadding = new byte[pbData.length+nPaddingLen];
		System.arraycopy(pbData, 0, pbPadding, 0, pbData.length);
		printHexBytes("pbPadding", pbPadding);
		
		memset(pbPadding, pbData.length, nPaddingLen, (byte)nPaddingLen);
		nPaddingLen += pbData.length;
			
		Key deskey = null;
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(strAlg);
		if(DES == nAlg){
			DESKeySpec spec = new DESKeySpec(pbKeyIn);
			deskey = keyfactory.generateSecret(spec);
		}
		else{
			DESedeKeySpec spec = new DESedeKeySpec(pbKeyIn);
			deskey = keyfactory.generateSecret(spec);
		}
      			
		Cipher cipher = Cipher.getInstance(strAlg+"/"+strMode+"/PKCS5Padding");
//		if (ECB == nMode)
//		{
//			cipher.init(Cipher.ENCRYPT_MODE, deskey);
//		}else
//		{
//			IvParameterSpec ips = new IvParameterSpec(pbIV);
//			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
//		}
		IvParameterSpec ips = new IvParameterSpec(pbIV);
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		
		byte[] pb = cipher.doFinal(pbPadding);
		int nLen = pb.length;
		if(DES112 == nAlg || DES168 == nAlg){
			nLen -= 0x08;
		}
		pbEncData = new byte[nLen];
		System.arraycopy(pb, 0, pbEncData, 0, nLen);
		return pbEncData;
	}
	
	public static byte[] dec(int nAlg, int nMode, final byte[] pbKey, final byte[] pbIV, final byte[] pbEncData) throws Exception{
		byte[] pbData = null;
		String strAlg = (DES == nAlg)?"DES":"DESede";
		String strMode = (ECB==nMode)?"ECB":"CBC";
		byte[] pbKeyIn = null;
		
		if(DES == nAlg){
			pbKeyIn = new byte[0x08];
			System.arraycopy(pbKey, 0, pbKeyIn, 0, 0x08);
		}
		else if(DES112 == nAlg){
			pbKeyIn = new byte[0x18];
			System.arraycopy(pbKey, 0, pbKeyIn, 0, 0x10);
			System.arraycopy(pbKey, 0, pbKeyIn, 0x10, 0x08);
		}
		else{
			pbKeyIn = new byte[0x18];
			System.arraycopy(pbKey, 0, pbKeyIn, 0, 0x18);
		}

		Key deskey = null;
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(strAlg);
		if(DES == nAlg){
			DESKeySpec spec = new DESKeySpec(pbKeyIn);
			deskey = keyfactory.generateSecret(spec);
		}
		else{
			DESedeKeySpec spec = new DESedeKeySpec(pbKeyIn);
			deskey = keyfactory.generateSecret(spec);
		}
      			
		Cipher cipher = Cipher.getInstance(strAlg+"/"+strMode+"/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(pbIV);
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
//		byte[] pbDataTmp = cipher.doFinal(pbEncData);
//		int len = pbDataTmp.length;
//		len -= pbDataTmp[len-1];
//		pbData = new byte[len];
//		System.arraycopy(pbDataTmp, 0, pbData, 0, len);
		
		pbData = cipher.doFinal(pbEncData);
		return pbData;
	}
	
	public static void memset(byte[] pbData, int nPos, int nLen, byte bData){
		if(null != pbData && nPos+nLen <= pbData.length ){
			for(int i = nPos; i < nPos+nLen; i++){
				pbData[i] = bData;
			}
		}
	}
	
	public static void printHexBytes(String tag, byte[] pbHex){
		if (!isOpenLog)
		{
			return ;
		}
		StringBuilder sbHex = new StringBuilder("");
    	for (int i = 0; i < pbHex.length; i++) {
    		String strTmp = Integer.toHexString((pbHex[i]&0xf0)>>0x04) +Integer.toHexString((pbHex[i]&0x0f));
    		sbHex.append(strTmp);
    		if(sbHex.toString().length() > 2*1024){
    			System.out.println(tag+":"+sbHex.toString());
    			sbHex = new StringBuilder("");
    		}
		}
    	System.out.println(tag+":"+sbHex.toString());
    }
}
