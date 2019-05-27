/*******************************************************************************
 * Copyright (C) 2018 The Zeepin Authors
 * This file is part of The Zeepin library.
 *
 * The Zeepin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Zeepin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The Zeepin.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <e <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.github.zeepin.smartcontract.nativevm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.VmType;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;
import com.github.zeepin.sdk.exception.SDKException;
import com.github.zeepin.smartcontract.nativevm.abi.NativeBuildParams;
import com.github.zeepin.smartcontract.nativevm.abi.Struct;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Auth {
    private ZPTSdk sdk;
    private final String contractAddress = "0000000000000000000000000000000000000006";
    public Auth(ZPTSdk sdk) {
        this.sdk = sdk;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    /**
     *
     * @param adminGId
     * @param password
     * @param contractAddr
     * @param newAdminGID
     * @param keyNo
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String sendTransfer(String adminGId, String password,byte[] salt,long keyNo,  String contractAddr, String newAdminGID, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if(adminGId ==null || adminGId.equals("") || contractAddr == null || contractAddr.equals("") || newAdminGID==null || newAdminGID.equals("")||payerAcct==null){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo <0 || gaslimit <0 || gasprice <0){
            throw new SDKException(ErrorCode.ParamErr("keyNo or gaslimit or gasprice should not be less than 0"));
        }
        Transaction tx = makeTransfer(adminGId,contractAddr,newAdminGID,keyNo,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx,adminGId,password,salt);
        sdk.addSign(tx,payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (!b) {
            throw new SDKException(ErrorCode.SendRawTxError);
        }
        return tx.hash().toHexString();
    }

    /**
     *
     * @param adminGID
     * @param contractAddr
     * @param newAdminGID
     * @param keyNo
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws SDKException
     */
    public Transaction makeTransfer(String adminGID,String contractAddr, String newAdminGID,long keyNo,String payer,long gaslimit,long gasprice) throws SDKException {
        if(adminGID ==null || adminGID.equals("") || contractAddr == null || contractAddr.equals("") || newAdminGID==null || newAdminGID.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo <0 || gaslimit <0 || gasprice <0){
            throw new SDKException(ErrorCode.ParamErr("keyNo or gaslimit or gasprice should not be less than 0"));
        }

        List list = new ArrayList();
        list.add(new Struct().add(Helper.hexToBytes(contractAddr),newAdminGID.getBytes(),keyNo));
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(contractAddress)),"transfer",arg,payer,gaslimit,gasprice);
        return tx;
    }

    /**
     *
     * @param gid
     * @param password
     * @param contractAddr
     * @param funcName
     * @param keyNo
     * @return
     * @throws Exception
     */
    public String verifyToken(String gid,String password,byte[] salt,long keyNo, String contractAddr,String funcName) throws Exception {
        if(gid ==null || gid.equals("") || password ==null || password.equals("")|| contractAddr == null || contractAddr.equals("") || funcName==null || funcName.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo < 0){
            throw new SDKException(ErrorCode.ParamErr("key or gaslimit or gas price should not be less than 0"));
        }
        Transaction tx = makeVerifyToken(gid,contractAddr,funcName,keyNo);
        sdk.signTx(tx,gid,password,salt);
        Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        if (obj == null){
            throw new SDKException(ErrorCode.OtherError("sendRawTransaction PreExec error: "));
        }
        return ((JSONObject)obj).getString("Result");
    }

    /**
     *
     * @param gid
     * @param contractAddr
     * @param funcName
     * @param keyNo
     * @return
     * @throws SDKException
     */
    public Transaction makeVerifyToken(String gid,String contractAddr,String funcName,long keyNo) throws SDKException {
        if(gid ==null || gid.equals("")|| contractAddr == null || contractAddr.equals("") || funcName==null || funcName.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo < 0){
            throw new SDKException(ErrorCode.ParamErr("key or gaslimit or gas price should not be less than 0"));
        }
        List list = new ArrayList();
        list.add(new Struct().add(Helper.hexToBytes(contractAddr),gid.getBytes(),funcName.getBytes(),keyNo));
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(contractAddress)),"verifyToken",arg,null,0,0);
        return tx;
    }

    /**
     *
     * @param adminGID
     * @param password
     * @param contractAddr
     * @param role
     * @param funcName
     * @param keyNo
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String assignFuncsToRole(String adminGID,String password,byte[] salt, long keyNo,String contractAddr,String role,String[] funcName,Account payerAcct,long gaslimit,long gasprice) throws Exception {
        if(adminGID ==null || adminGID.equals("") || contractAddr == null || contractAddr.equals("") || role==null || role.equals("") || funcName == null || funcName.length == 0||payerAcct==null){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo < 0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("keyNo or gaslimit or gas price should not be less than 0"));
        }
        Transaction tx = makeAssignFuncsToRole(adminGID,contractAddr,role,funcName,keyNo,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx,adminGID,password,salt);
        sdk.addSign(tx,payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if(b){
            return tx.hash().toHexString();
        }
        return null;
    }

    /**
     *
     * @param adminGID
     * @param contractAddr
     * @param role
     * @param funcName
     * @param keyNo
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws SDKException
     */
    public Transaction makeAssignFuncsToRole(String adminGID,String contractAddr,String role,String[] funcName,long keyNo,String payer,long gaslimit,long gasprice) throws SDKException {
        if(adminGID ==null || adminGID.equals("") || contractAddr == null || contractAddr.equals("") || role==null || role.equals("") || funcName == null || funcName.length == 0
                || payer==null || payer.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo < 0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("keyNo or gaslimit or gas price should not be less than 0"));
        }

        List list = new ArrayList();
        Struct struct = new Struct();
        struct.add(Helper.hexToBytes(contractAddr),adminGID.getBytes(),role.getBytes());
        struct.add(funcName.length);
        for (int i = 0; i < funcName.length; i++) {
            struct.add(funcName[i]);
        }
        struct.add(keyNo);
        list.add(struct);
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(contractAddress)),"assignFuncsToRole",arg,payer,gaslimit,gasprice);
        return tx;
    }

    /**
     *
     * @param adminGId
     * @param password
     * @param contractAddr
     * @param role
     * @param GIDs
     * @param keyNo
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String assignGIdsToRole(String adminGId,String password,byte[] salt,long keyNo, String contractAddr,String role,String[] GIDs,Account payerAcct,long gaslimit,long gasprice) throws Exception {
        if(adminGId == null || adminGId.equals("") || password==null || password.equals("") || contractAddr== null || contractAddr.equals("") ||
                role == null || role.equals("") || GIDs==null || GIDs.length == 0){
            throw  new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo<0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("keyNo or gaslimit or gasprice should not be less than 0"));
        }
        Transaction tx = makeAssignGIDsToRole(adminGId,contractAddr,role,GIDs,keyNo,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx,adminGId,password,salt);
        sdk.addSign(tx,payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if(b){
            return tx.hash().toHexString();
        }
        return null;
    }

    /**
     *
     * @param adminGId
     * @param contractAddr
     * @param role
     * @param GIDs
     * @param keyNo
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws SDKException
     */
    public Transaction makeAssignGIDsToRole(String adminGId,String contractAddr,String role,String[] GIDs,long keyNo,String payer,long gaslimit,long gasprice) throws SDKException {
        if(adminGId == null || adminGId.equals("") || contractAddr== null || contractAddr.equals("") ||
                role == null || role.equals("") || GIDs==null || GIDs.length == 0){
            throw  new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo <0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("keyNo or gaslimit or gasprice should not be less than 0"));
        }
        byte[][] GId = new byte[GIDs.length][];
        for(int i=0; i< GIDs.length ; i++){
        	GId[i] = GIDs[i].getBytes();
        }
        List list = new ArrayList();
        Struct struct = new Struct();
        struct.add(Helper.hexToBytes(contractAddr),adminGId.getBytes(),role.getBytes());
        struct.add(GId.length);
        for(int i =0;i<GId.length;i++){
            struct.add(GId[i]);
        }
        struct.add(keyNo);
        list.add(struct);
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(contractAddress)),"assignGIDsToRole",arg,payer,gaslimit,gasprice);
        return tx;
    }

    /**
     *
     * @param Gid
     * @param password
     * @param contractAddr
     * @param toGId
     * @param role
     * @param period
     * @param level
     * @param keyNo
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String delegate(String Gid,String password,byte[] salt, long keyNo,String contractAddr,String toGId,String role,long period,long level,Account payerAcct,long gaslimit,long gasprice) throws Exception {
        if(Gid == null || Gid.equals("") ||password == null || password.equals("") || contractAddr == null || contractAddr.equals("") ||toGId==null || toGId.equals("")||
                role== null || role.equals("") ||payerAcct==null){
            throw  new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(period<0 || level <0 || keyNo <0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("period level key gaslimit or gasprice should not be less than 0"));
        }
        Transaction tx = makeDelegate(Gid,contractAddr,toGId,role,period,level,keyNo,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx,Gid,password,salt);
        sdk.addSign(tx,payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if(b){
            return tx.hash().toHexString();
        }
        return null;
    }

    /**
     *
     * @param Gid
     * @param contractAddr
     * @param toAddr
     * @param role
     * @param period
     * @param level
     * @param keyNo
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws SDKException
     */
    public Transaction makeDelegate(String Gid,String contractAddr,String toAddr,String role,long period,long level,long keyNo,String payer,long gaslimit,long gasprice) throws SDKException {
        if(Gid == null || Gid.equals("")|| contractAddr == null || contractAddr.equals("") ||toAddr==null || toAddr.equals("")||
                role== null || role.equals("") || payer ==null || payer.equals("")){
            throw  new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(period<0 || level <0 || keyNo <0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("period level keyNo gaslimit or gasprice should not be less than 0"));
        }

        List list = new ArrayList();
        list.add(new Struct().add(Helper.hexToBytes(contractAddr),Gid.getBytes(),toAddr.getBytes(),role.getBytes(),period,level,keyNo));
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(contractAddress)),"delegate",arg,payer,gaslimit,gasprice);
        return tx;
    }

    /**
     *
     * @param initiatorGid
     * @param password
     * @param contractAddr
     * @param delegate
     * @param role
     * @param keyNo
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String withdraw(String initiatorGid,String password,byte[] salt,long keyNo, String contractAddr,String delegate, String role,Account payerAcct,long gaslimit,long gasprice) throws Exception {
        if(initiatorGid == null || initiatorGid.equals("")|| password ==null|| password.equals("") || contractAddr == null || contractAddr.equals("") ||
                role== null || role.equals("") || payerAcct==null){
            throw  new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo <0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("keyNo or gaslimit or gasprice should not be less than 0"));
        }
        Transaction tx = makeWithDraw(initiatorGid,contractAddr,delegate,role,keyNo,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx,initiatorGid,password,salt);
        sdk.addSign(tx,payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if(b){
            return tx.hash().toHexString();
        }
        return null;
    }

    /**
     *
     * @param Gid
     * @param contractAddr
     * @param delegate
     * @param role
     * @param keyNo
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws SDKException
     */
    public Transaction makeWithDraw(String Gid,String contractAddr,String delegate, String role,long keyNo,String payer,long gaslimit,long gasprice) throws SDKException {
        if(Gid == null || Gid.equals("")|| contractAddr == null || contractAddr.equals("") ||
                role== null || role.equals("") || payer ==null || payer.equals("")){
            throw  new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(keyNo <0 || gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("key gaslimit or gasprice should not be less than 0"));
        }
        List list = new ArrayList();
        list.add(new Struct().add(Helper.hexToBytes(contractAddr),Gid.getBytes(),delegate.getBytes(),role.getBytes(),keyNo));
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(contractAddress)),"withdraw",arg,payer,gaslimit,gasprice);
        return tx;
    }

    public Object queryAuth(String contractAddr,String Gid) throws Exception {
        Object obj = sdk.getConnect().getStorage(contractAddr,contractAddr+Helper.toHexString("role".getBytes())+Helper.toHexString(Gid.getBytes()));
        return obj;
    }
}
class TransferParam implements Serializable {
    byte[] contractAddr;
    byte[] newAdminGID;
    long KeyNo;
    TransferParam(byte[] contractAddr,byte[] newAdminGID,long keyNo){
        this.contractAddr = contractAddr;
        this.newAdminGID = newAdminGID;
        KeyNo = keyNo;
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        this.contractAddr = reader.readVarBytes();
        this.newAdminGID = reader.readVarBytes();
        KeyNo = reader.readVarInt();
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(this.contractAddr);
        writer.writeVarBytes(this.newAdminGID);
        writer.writeVarInt(KeyNo);
    }
}
class VerifyTokenParam implements Serializable{
    byte[] contractAddr;
    byte[] caller;
    byte[] fn;
    long keyNo;
    VerifyTokenParam(byte[] contractAddr,byte[] caller,byte[] fn,long keyNo){
        this.contractAddr = contractAddr;
        this.caller = caller;
        this.fn = fn;
        this.keyNo = keyNo;
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {

    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(this.contractAddr);
        writer.writeVarBytes(this.caller);
        writer.writeVarBytes(this.fn);
        writer.writeVarInt(keyNo);
    }
}

class FuncsToRoleParam implements Serializable{
    byte[] contractAddr;
    byte[] adminGID;
    byte[] role;
    String[] funcNames;
    long keyNo;

    FuncsToRoleParam(byte[] contractAddr,byte[] adminGID,byte[] role,String[] funcNames,long keyNo){
        this.contractAddr =contractAddr;
        this.adminGID = adminGID;
        this.role =role;
        this.funcNames = funcNames;
        this.keyNo = keyNo;
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        this.contractAddr = reader.readVarBytes();
        this.adminGID = reader.readVarBytes();
        this.role = reader.readVarBytes();
        int length = (int)reader.readVarInt();
        this.funcNames = new String[length];
        for(int i = 0;i< length;i++){
            this.funcNames[i] = reader.readVarString();
        }
        this.keyNo = reader.readVarInt();
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(this.contractAddr);
        writer.writeVarBytes(this.adminGID);
        writer.writeVarBytes(this.role);
        writer.writeVarInt(this.funcNames.length);
        for(String name:this.funcNames){
            writer.writeVarString(name);
        }
        writer.writeVarInt(this.keyNo);
    }
}
class GIDsToRoleParam implements Serializable{
    byte[] contractAddr;
    byte[] adminGID;
    byte[] role;
    byte[][] persons;
    long keyNo;
    GIDsToRoleParam( byte[] contractAddr,byte[] adminGID,byte[] role,byte[][] persons,long keyNo){
        this.contractAddr = contractAddr;
        this.adminGID = adminGID;
        this.role = role;
        this.persons = persons;
        this.keyNo = keyNo;
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        this.contractAddr = reader.readVarBytes();
        this.adminGID = reader.readVarBytes();
        this.role = reader.readVarBytes();
        int length = (int)reader.readVarInt();
        this.persons = new byte[length][];
        for(int i = 0; i< length;i++){
            this.persons[i] = reader.readVarBytes();
        }
        this.keyNo = reader.readVarInt();
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(this.contractAddr);
        writer.writeVarBytes(this.adminGID);
        writer.writeVarBytes(this.role);
        writer.writeVarInt(this.persons.length);
        for(byte[] p: this.persons){
            writer.writeVarBytes(p);
        }
        writer.writeVarInt(this.keyNo);
    }
}

class DelegateParam implements  Serializable{
    byte[] contractAddr;
    byte[] from;
    byte[] to;
    byte[] role;
    long period;
    long level;
    long keyNo;
    DelegateParam(byte[] contractAddr,byte[] from,byte[] to,byte[] role, long period, long level,long keyNo){
        this.contractAddr = contractAddr;
        this.from = from;
        this.to = to;
        this.role = role;
        this.period = period;
        this.level = level;
        this.keyNo = keyNo;
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {

    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(this.contractAddr);
        writer.writeVarBytes(this.from);
        writer.writeVarBytes(this.to);
        writer.writeVarBytes(this.role);
        writer.writeVarInt(this.period);
        writer.writeVarInt(this.level);
        writer.writeVarInt(this.keyNo);
    }
}

class AuthWithdrawParam implements Serializable{
    byte[] contractAddr;
    byte[] initiator;
    byte[] delegate;
    byte[] role;
    long keyNo;
    public AuthWithdrawParam(byte[] contractAddr,byte[] initiator, byte[] delegate,byte[] role,long keyNo){
        this.contractAddr = contractAddr;
        this.initiator = initiator;
        this.delegate = delegate;
        this.role = role;
        this.keyNo = keyNo;
    }
    @Override
    public void deserialize(BinaryReader reader) throws IOException {

    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(this.contractAddr);
        writer.writeVarBytes(this.initiator);
        writer.writeVarBytes(this.delegate);
        writer.writeVarBytes(this.role);
        writer.writeVarInt(this.keyNo);
    }
}


