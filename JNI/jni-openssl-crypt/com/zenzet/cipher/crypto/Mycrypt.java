package com.zenzet.cipher.crypto;
import java.util.*;

public class Mycrypt
{
    static { System.loadLibrary("jnicrypt");}

    public native static void LOCKetAESCFB();
    public native static Map<String, String> LKTGenerateKeyPair(int keyBytes);
    public native static long getInstance(String algor);
    public native static int digestUpdate(long ctx, byte[] in);
    public native static byte [] digestFinal(long ctx);
	public native static long  OpenSslRSANativeCryptInitContext(int cryptMode, int padding, byte[] key);
	public native static byte [] OpenSslRSANativeCryptUpdate(long ctx, int mode, byte[] in);
	public native static byte [] OpenSslRSANativeCryptdoFinal(long ctx, int mode, byte[] in);
	public native static long  OpenSslRSANativeSignInitContext(String hashalg, byte[] key);
	public native static int   OpenSslRSANativeSignUpdate(long ctx, byte[] in);
	public native static byte [] OpenSslRSANativeSigndoFinal(long ctx);

	public native static long  OpenSslRSANativeVerifyInitContext(String hashalg, byte[] key);
	public native static int   OpenSslRSANativeVerifyUpdate(long ctx, byte[] in);
	public native static int   OpenSslRSANativeVerifydoFinal(long ctx, byte[] sign);

    public native static Map<String, String> OpenSslNativegenerateSM2KeyPair();
	public native static long  OpenSslNativeSM2CryptInitContext(int cryptMode, byte[] key);
	//public native static byte [] OpenSslNativeSM2CryptUpdate(long ctx, int mode, byte[] in);
	public native static byte [] OpenSslNativeSM2CryptdoFinal(long ctx, int mode, byte[] in);

	public native static long  OpenSslNativeSM2SignInitContext(String hashalg, byte[] key);
	public native static int   OpenSslNativeSM2SignUpdate(long ctx, byte[] in);
	public native static byte [] OpenSslNativeSM2SigndoFinal(long ctx);
	public native static long  OpenSslNativeSM2VerifyInitContext(String hashalg, byte[] key);
	public native static int   OpenSslNativeSM2VerifyUpdate(long ctx, byte[] in);
	public native static int   OpenSslNativeSM2VerifydoFinal(long ctx, byte[] sign);

    public static void main (String[] args)
    {
        //test_rsa ();
        test_sm2 ();
    }

    public static void test_sm2 ()
    {
        long ctx = 0;
        Object object = Mycrypt.OpenSslNativegenerateSM2KeyPair ();
        Map<String, String>map=(Map<String, String>)object;
        String pubkey = map.get("pk");
        String privatekey = map.get("pv");
        System.out.println("[java]sm2-pk:"+map.get("pk"));
        System.out.println("[java]sm2-pv:"+map.get("pv"));

        byte[] bPubkey = pubkey.getBytes();
        byte[] bPrivateKey = privatekey.getBytes();

        /*
        ctx = OpenSslNativeSM2CryptInitContext (0, bPubkey);
        System.out.println (ctx);

        String in = "hello,world";
        byte[] bin = in.getBytes();
         
        byte[] boutput1 = OpenSslNativeSM2CryptdoFinal (ctx, 0, bin);
        String soutput1 = new String(boutput1);
        System.out.println (soutput1);

        long ctx1 = OpenSslNativeSM2CryptInitContext (1,  bPrivateKey);
        System.out.println (ctx1);

        byte[] boutput2 = OpenSslNativeSM2CryptdoFinal (ctx1, 1, boutput1);
        String soutput2 = new String (boutput2);
        System.out.println (soutput2);
        */

        /* 注意:ECDSA不支持MD5摘要 */
        String md = "SM3";
        String in11 = "hello,world";
        byte[] bin11 = in11.getBytes();

        long ctx11 = OpenSslNativeSM2SignInitContext (md,  bPrivateKey);
        System.out.println (ctx11);
        int ret = OpenSslNativeSM2SignUpdate (ctx11, bin11);
        byte[] boutput11 = OpenSslNativeSM2SigndoFinal (ctx11);
        String soutput11 = new String(boutput11);
        System.out.println (soutput11);

        long ctx22 = OpenSslNativeSM2VerifyInitContext(md, bPubkey);
        System.out.println (ctx22);
        ret = OpenSslNativeSM2VerifyUpdate (ctx22, bin11);
        System.out.println (ret);

        ret = OpenSslNativeSM2VerifydoFinal (ctx22, boutput11);
        System.out.println ("verify :" + ret);
    }

    public static void test_rsa ()
    {
        long ctx = 0;
        Object object = Mycrypt.LKTGenerateKeyPair (1024);
        Map<String, String>map=(Map<String, String>)object;
        String pubkey = map.get("pk");
        String privatekey = map.get("pv");
        System.out.println("[java]pk:"+map.get("pk"));
        System.out.println("[java]pv:"+map.get("pv"));

        /*
        byte[] sPubkey = pubkey.getBytes();
        ctx = OpenSslRSANativeCryptInitContext (0, 1,  sPubkey);
        System.out.println (ctx);

        String sinput = "hello,world";
        byte[] binput = sinput.getBytes();
        byte[] boutput = OpenSslRSANativeCryptdoFinal (ctx, 0, binput);
        String soutput = new String(boutput);
        System.out.println (soutput);

        byte[] sPrivateKey = privatekey.getBytes();
        long ctx1 = OpenSslRSANativeCryptInitContext (1, 1, sPrivateKey);
        System.out.println (ctx1);

        byte[] boutput1 = OpenSslRSANativeCryptdoFinal (ctx1, 1,boutput); 
        String soutput1 = new String(boutput1);
        System.out.println (soutput1);
        */

        String md = "SHA512";
        String in = "hello,world";
        byte[] bin = in.getBytes();

        byte[] sPrivateKey = privatekey.getBytes();
        long ctx2 = OpenSslRSANativeSignInitContext (md,  sPrivateKey);
        int ret = OpenSslRSANativeSignUpdate (ctx2, bin);
        byte[] boutput2 = OpenSslRSANativeSigndoFinal (ctx2);

         
        byte[] sPublicKey = pubkey.getBytes();
        long ctx3 = OpenSslRSANativeVerifyInitContext(md,  sPublicKey);
        ret = OpenSslRSANativeVerifyUpdate (ctx3, bin);

        //System.out.println ("boutput2 len :" +boutput2.length);
        ret = OpenSslRSANativeVerifydoFinal (ctx3, boutput2);
        System.out.println ("verify :" + ret);
    }

    public static void test_aes ()
    {
        //LOCKetAESCFB ();
    }

    public static void test_md ()
    {
        long ctx = 0;
        /*
        long ctx = getInstance ("MD5");
        System.out.println("ctx" + ctx);

        byte [] input = {'h', 'e'};
        byte [] input2 = {'l', 'l', 'o'};

        digestUpdate(ctx, input);
        digestUpdate(ctx, input2);
        
        byte[] a = digestFinal (ctx);
        System.out.println (a);
        */
    }
}


