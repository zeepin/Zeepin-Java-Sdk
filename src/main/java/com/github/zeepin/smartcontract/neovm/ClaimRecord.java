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

package com.github.zeepin.smartcontract.neovm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.Common;
import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;
import com.github.zeepin.sdk.exception.SDKException;
import com.github.zeepin.smartcontract.neovm.abi.AbiFunction;
import com.github.zeepin.smartcontract.neovm.abi.AbiInfo;
import com.github.zeepin.smartcontract.neovm.abi.BuildParams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static demo.NeoVmDemo.abi;

public class ClaimRecord {
    private ZPTSdk sdk;
    private String contractAddress = "36bb5c053b6b839c8f6b923fe852f91239b9fccc";

    private String abi = "{\"hash\":\"0x36bb5c053b6b839c8f6b923fe852f91239b9fccc\",\"entrypoint\":\"Main\",\"functions\":[{\"name\":\"Main\",\"parameters\":[{\"name\":\"operation\",\"type\":\"String\"},{\"name\":\"args\",\"type\":\"Array\"}],\"returntype\":\"Any\"},{\"name\":\"Commit\",\"parameters\":[{\"name\":\"claimId\",\"type\":\"ByteArray\"},{\"name\":\"commiterId\",\"type\":\"ByteArray\"},{\"name\":\"ownerId\",\"type\":\"ByteArray\"}],\"returntype\":\"Boolean\"},{\"name\":\"Revoke\",\"parameters\":[{\"name\":\"claimId\",\"type\":\"ByteArray\"},{\"name\":\"GId\",\"type\":\"ByteArray\"}],\"returntype\":\"Boolean\"},{\"name\":\"GetStatus\",\"parameters\":[{\"name\":\"claimId\",\"type\":\"ByteArray\"}],\"returntype\":\"ByteArray\"}],\"events\":[{\"name\":\"ErrorMsg\",\"parameters\":[{\"name\":\"id\",\"type\":\"ByteArray\"},{\"name\":\"error\",\"type\":\"String\"}],\"returntype\":\"Void\"},{\"name\":\"Push\",\"parameters\":[{\"name\":\"id\",\"type\":\"ByteArray\"},{\"name\":\"msg\",\"type\":\"String\"},{\"name\":\"args\",\"type\":\"ByteArray\"}],\"returntype\":\"Void\"}]}";

    public ClaimRecord(ZPTSdk sdk) {
        this.sdk = sdk;
    }

    public void setContractAddress(String codeHash) {
        this.contractAddress = codeHash.replace("0x", "");
    }

    public String getContractAddress() {
        return contractAddress;
    }

    /**
     *
     * @param issuerGid
     * @param password
     * @param subjectGid
     * @param claimId
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String sendCommit(String issuerGid, String password, byte[] salt, String subjectGid, String claimId, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if(issuerGid==null||issuerGid.equals("")||password==null||password.equals("")||subjectGid==null||subjectGid.equals("")
                || claimId==null||claimId.equals("")||payerAcct == null){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("gaslimit or gasprice is less than 0"));
        }
        if (contractAddress == null) {
            throw new SDKException(ErrorCode.NullCodeHash);
        }
        String addr = issuerGid.replace(Common.didzpt,"");
        Transaction tx = makeCommit(issuerGid,subjectGid,claimId,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx, addr, password,salt);
        sdk.addSign(tx,payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (b) {
            return tx.hash().toString();
        }
        return null;
    }

    /**
     *
     * @param issuerGid
     * @param subjectGid
     * @param claimId
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public Transaction makeCommit(String issuerGid, String subjectGid, String claimId,String payer, long gaslimit, long gasprice) throws Exception {
        if(issuerGid==null||issuerGid.equals("")||subjectGid==null||subjectGid.equals("")||payer==null||payer.equals("")
                || claimId==null||claimId.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("gaslimit or gasprice is less than 0"));
        }

        AbiInfo abiinfo = JSON.parseObject(abi, AbiInfo.class);
        String name = "Commit";
        AbiFunction func = abiinfo.getFunction(name);
        func.name = name;
        func.setParamsValue(claimId.getBytes(),issuerGid.getBytes(),subjectGid.getBytes());
        byte[] params = BuildParams.serializeAbiFunction(func);
        Transaction tx = sdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractAddress), null, params, payer,gaslimit, gasprice);
        return tx;
    }

    /**
     *
     * @param issuerGid
     * @param password
     * @param claimId
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String sendRevoke(String issuerGid,String password,byte[] salt, String claimId,Account payerAcct,long gaslimit,long gasprice) throws Exception {
        if(issuerGid==null||issuerGid.equals("")||password==null||password.equals("")
                || claimId==null||claimId.equals("")||payerAcct == null){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        if(gaslimit < 0 || gasprice < 0){
            throw new SDKException(ErrorCode.ParamErr("gaslimit or gasprice is less than 0"));
        }
        if (contractAddress == null) {
            throw new SDKException(ErrorCode.NullCodeHash);
        }
        String addr = issuerGid.replace(Common.didzpt,"");
        Transaction tx = makeRevoke(issuerGid,claimId,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx, addr, password,salt);
        sdk.addSign(tx,payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (b) {
            return tx.hash().toString();
        }
        return null;
    }

    public Transaction makeRevoke(String issuerGid,String claimId,String payer,long gaslimit,long gasprice) throws Exception {
        AbiInfo abiinfo = JSON.parseObject(abi, AbiInfo.class);
        String name = "Revoke";
        AbiFunction func = abiinfo.getFunction(name);
        func.name = name;
        func.setParamsValue(claimId.getBytes(),issuerGid.getBytes());
        byte[] params = BuildParams.serializeAbiFunction(func);
        Transaction tx = sdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractAddress), null, params, payer,gaslimit, gasprice);
        return tx;
    }
    public String sendGetStatus(String claimId) throws Exception {
        if (contractAddress == null) {
            throw new SDKException(ErrorCode.NullCodeHash);
        }
        if (claimId == null || claimId == ""){
            throw new SDKException(ErrorCode.NullKeyOrValue);
        }
        AbiInfo abiinfo = JSON.parseObject(abi, AbiInfo.class);
        String name = "GetStatus";
        AbiFunction func = abiinfo.getFunction(name);
        func.name = name;
        func.setParamsValue(claimId.getBytes());
        Object obj =  sdk.neovm().sendTransaction(Helper.reverse(contractAddress),null,null,0,0,func, true);
        String res = ((JSONObject)obj).getString("Result");
        if(res.equals("")){
            return "";
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(Helper.hexToBytes(res));
        BinaryReader br = new BinaryReader(bais);
        ClaimTx claimTx = new ClaimTx();
        claimTx.deserialize(br);
        if(claimTx.status.length == 0){
            return new String(claimTx.claimId)+"."+"00"+"."+new String(claimTx.issuerGId)+"."+new String(claimTx.subjectGId);
        }
        return new String(claimTx.claimId)+"."+Helper.toHexString(claimTx.status)+"."+new String(claimTx.issuerGId)+"."+new String(claimTx.subjectGId);
    }
}

class ClaimTx implements Serializable {
    public byte[] claimId;
    public byte[] issuerGId;
    public byte[] subjectGId;
    public byte[] status;
    ClaimTx(){}
    ClaimTx(byte[] claimId,byte[] issuerGId,byte[] subjectGId,byte[] status){
        this.claimId = claimId;
        this.issuerGId = issuerGId;
        this.subjectGId = subjectGId;
        this.status = status;
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        byte dataType = reader.readByte();
        long length = reader.readVarInt();
        byte dataType2 = reader.readByte();
        this.claimId = reader.readVarBytes();
        byte dataType3 = reader.readByte();
        this.issuerGId = reader.readVarBytes();
        byte dataType4 = reader.readByte();
        this.subjectGId = reader.readVarBytes();
        byte dataType5 = reader.readByte();
        this.status = reader.readVarBytes();
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {

    }
}
