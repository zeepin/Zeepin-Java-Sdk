package com.github.zeepin.smartcontract;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;

public class Constract {
    private byte version;
    private byte[] conaddr;
    private  String  agrs;
    private   String method;

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte[] getConaddr() {
        return conaddr;
    }

    public void setConaddr(byte[] conaddr) {
        this.conaddr = conaddr;
    }

    public String getAgrs() {
        return agrs;
    }

    public void setAgrs(String agrs) {
        this.agrs = agrs;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public byte[] tobytes(){
        byte[] a=new byte[1];
        a[0]=(byte)'1';
        
        int lengthMethod=method.getBytes().length;
        byte[] lengthMethodBytes=intToBytes(lengthMethod);
        
        int argsLength = agrs.length();
        byte[] argLenthBytes= intToBytes(argsLength);
        
        int totalLengh=  1+conaddr.length+lengthMethodBytes.length+method.getBytes().length+argLenthBytes.length+argsLength;
        byte[] resultByte = new byte[totalLengh];
        
        System.arraycopy(a,0,resultByte,0,1);
        System.arraycopy(conaddr,0,resultByte,1,conaddr.length);
        System.arraycopy(lengthMethodBytes,0,resultByte,conaddr.length+1,lengthMethodBytes.length);
        System.arraycopy(method.getBytes(),0,resultByte,conaddr.length+lengthMethodBytes.length+1,method.length());
        System.arraycopy(argLenthBytes,0,resultByte,conaddr.length+lengthMethodBytes.length+method.length()+1,argLenthBytes.length);
        System.arraycopy(agrs.getBytes(),0,resultByte,conaddr.length+lengthMethodBytes.length+method.length()+argLenthBytes.length+1,agrs.getBytes().length);


      //  System.arraycopy();
        return resultByte;
    }

    public static byte[] intToBytes( int value )
    {
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        if(src[3]==0&&src[2]==0&&src[1]==0){
            byte[] src2 = new byte[1];
            src2[0]=src[0];
            return src2;
        }
        if(src[3]==0&&src[2]==0){
            byte[] src2 = new byte[3];
            src2[0]=(byte) 0xFD;
            src2[1]=src[0];
            src2[2]=src[1];
            return src2;
        }

        return src;
    }
    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用(打)
     */
    public static byte[] intToBytes2(int value)
    {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    public static void main(String[] args) {
        Constract constract = new Constract();
        constract.method ="1";
        constract.agrs="012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
        constract.version=1;
        constract.conaddr="00000000000000000000".getBytes();
        constract.tobytes();
    }
}
