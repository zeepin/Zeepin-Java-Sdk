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
 *******************************************************************************/

package com.github.zeepin.smartcontract.nativevm;

import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.asset.State;
import com.github.zeepin.core.asset.TransferFrom;
import com.github.zeepin.core.asset.Transfers;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.sdk.exception.SDKException;
import com.github.zeepin.smartcontract.nativevm.abi.NativeBuildParams;
import com.github.zeepin.smartcontract.nativevm.abi.Struct;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class Gala {
    private ZPTSdk sdk;
    private final String ZPTContract = "0000000000000000000000000000000000000001";
    private final String GalaContract = "0000000000000000000000000000000000000002";

    public Gala(ZPTSdk sdk) {
        this.sdk = sdk;
    }

    public String getContractAddress() {
        return GalaContract;
    }

    /**
     *
     * @param sendAcct
     * @param recvAddr
     * @param amount
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String sendTransfer(Account sendAcct, String recvAddr, long amount, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if (sendAcct == null || payerAcct == null ) {
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if (amount <= 0 || gasprice < 0 || gaslimit < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gasprice or gaslimit should not be less than 0"));
        }
        Transaction tx = makeTransfer(sendAcct.getAddressU160().toBase58(), recvAddr, amount, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        sdk.signTx(tx, new Account[][]{{sendAcct}});
        if(!sendAcct.equals(payerAcct)){
            sdk.addSign(tx,payerAcct);
        }
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (b) {
            return tx.hash().toString();
        }
        return null;
    }

    /**
     *
     * @param M
     * @param pubKeys
     * @param sendAccts
     * @param recvAddr
     * @param amount
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String sendTransferFromMultiSignAddr(int M,byte[][] pubKeys,Account[] sendAccts,String recvAddr, long amount, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if (sendAccts == null || sendAccts.length <= 1 || payerAcct == null ) {
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if (amount <= 0 || gasprice < 0 || gaslimit < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gasprice or gaslimit should not be less than 0"));
        }

        Address multiAddr = Address.addressFromMultiPubKeys(sendAccts.length,pubKeys);
        Transaction tx = makeTransfer(multiAddr.toBase58(), recvAddr, amount, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        for(int i=0;i<sendAccts.length;i++){
            sdk.addMultiSign(tx, M, pubKeys, sendAccts[i]);
        }
        sdk.addSign(tx, payerAcct);
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (b) {
            return tx.hash().toString();
        }
        return null;
    }

    /**
     * @param sendAddr
     * @param recvAddr
     * @param amount
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public Transaction makeTransfer(String sendAddr, String recvAddr, long amount, String payer, long gaslimit, long gasprice) throws Exception {
        if(sendAddr==null || sendAddr.equals("")|| recvAddr==null||recvAddr.equals("") ||
                payer==null||payer.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if (amount <= 0 || gasprice < 0 || gaslimit < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gasprice or gaslimit should not be less than 0"));
        }


        List list = new ArrayList();
        List listStruct = new ArrayList();
        listStruct.add(new Struct().add(Address.decodeBase58(sendAddr),Address.decodeBase58(recvAddr),amount));
        list.add(listStruct);
        byte[] args = NativeBuildParams.createCodeParamsScript(list);
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"transfer",args,payer,gaslimit, gasprice);
        return tx;
    }

    public Transaction makeTransfer(State[] states, String payer, long gaslimit, long gasprice) throws Exception {
        if (states == null || payer == null || payer.equals("")) {
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if(gasprice < 0 || gaslimit < 0){
            throw new SDKException(ErrorCode.ParamError);
        }

        List list = new ArrayList();
        List listStruct = new ArrayList();
        for (int i = 0; i < states.length; i++) {
            listStruct.add(new Struct().add(states[i].from, states[i].to, states[i].value));
        }
        list.add(listStruct);
        byte[] args = NativeBuildParams.createCodeParamsScript(list);
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)), "transfer", args, payer, gaslimit, gasprice);
        return tx;
    }
    /**
     * @param address
     * @return
     * @throws Exception
     */
    public long queryBalanceOf(String address) throws Exception {
        if(address == null|| address.equals("")){
            throw new SDKException(ErrorCode.ParamErr("address should not be null"));
        }
        List list = new ArrayList();
        list.add(Address.decodeBase58(address));
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"balanceOf",arg,null,0,0);
        Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = ((JSONObject) obj).getString("Result");
        if (res==null||res.equals("")) {
            return 0;
        }
        return Long.valueOf(Helper.reverse(res), 16);
    }

    /**
     * @param fromAddr
     * @param toAddr
     * @return
     * @throws Exception
     */
    public long queryAllowance(String fromAddr, String toAddr) throws Exception {
        if(fromAddr==null||fromAddr.equals("")||toAddr==null||toAddr.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameter should not be null"));
        }
        List list = new ArrayList();
        list.add(new Struct().add(Address.decodeBase58(fromAddr),Address.decodeBase58(toAddr)));
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"allowance",arg,null,0,0);
        Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = ((JSONObject) obj).getString("Result");
        if (res==null||res.equals("")) {
            return 0;
        }
        return Long.valueOf(Helper.reverse(res), 16);
    }

    /**
     *
     * @param sendAcct
     * @param recvAddr
     * @param amount
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String sendApprove(Account sendAcct, String recvAddr, long amount, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if(sendAcct==null || payerAcct==null){
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if (amount <= 0 || gasprice < 0 || gaslimit < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gasprice or gaslimit should not be less than 0"));
        }
        Transaction tx = makeApprove(sendAcct.getAddressU160().toBase58(),recvAddr,amount,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx, new Account[][]{{sendAcct}});
        if(!sendAcct.equals(payerAcct)){
            sdk.addSign(tx,payerAcct);
        }
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (b) {
            return tx.hash().toHexString();
        }
        return null;
    }

    /**
     *
     * @param sendAddr
     * @param recvAddr
     * @param amount
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public Transaction makeApprove(String sendAddr,String recvAddr,long amount,String payer,long gaslimit,long gasprice) throws Exception {
        if(sendAddr==null || sendAddr.equals("")||recvAddr==null || recvAddr.equals("")||
                payer==null||payer.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if (amount <= 0 || gasprice < 0 || gaslimit < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gasprice or gaslimit should not be less than 0"));
        }
        List list = new ArrayList();
        list.add(new Struct().add(Address.decodeBase58(sendAddr),Address.decodeBase58(recvAddr),amount));
        byte[] args = NativeBuildParams.createCodeParamsScript(list);
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"approve",args,payer,gaslimit, gasprice);
        return tx;
    }

    /**
     *
     * @param sendAcct
     * @param fromAddr
     * @param toAddr
     * @param amount
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String sendTransferFrom(Account sendAcct, String fromAddr, String toAddr, long amount, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if(sendAcct==null || payerAcct==null){
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if (amount <= 0 || gasprice < 0 || gaslimit < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gasprice or gaslimit should not be less than 0"));
        }
        Transaction tx = makeTransferFrom(sendAcct.getAddressU160().toBase58(),fromAddr,toAddr,amount,payerAcct.getAddressU160().toBase58(),gaslimit,gasprice);
        sdk.signTx(tx, new Account[][]{{sendAcct}});
        if(!sendAcct.equals(payerAcct)){
            sdk.addSign(tx,payerAcct);
        }
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (b) {
            return tx.hash().toHexString();
        }
        return null;
    }

    /**
     *
     * @param sendAddr
     * @param fromAddr
     * @param toAddr
     * @param amount
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public Transaction makeTransferFrom(String sendAddr, String fromAddr, String toAddr,long amount,String payer,long gaslimit,long gasprice) throws Exception {
        if(sendAddr==null || sendAddr.equals("")||fromAddr==null||fromAddr.equals("")||toAddr==null||toAddr.equals("")){
            throw new SDKException(ErrorCode.ParamErr("parameters should not be null"));
        }
        if (amount <= 0 || gasprice < 0 || gaslimit < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gasprice or gaslimit should not be less than 0"));
        }
        List list = new ArrayList();
        list.add(new Struct().add(Address.decodeBase58(sendAddr), Address.decodeBase58(fromAddr), Address.decodeBase58(toAddr), amount));
        byte[] args = NativeBuildParams.createCodeParamsScript(list);
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"transferFrom",args,payer,gaslimit, gasprice);
        return tx;
    }

    /**
     * @return
     * @throws Exception
     */
    public String queryName() throws Exception {
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"name",new byte[]{0},null,0,0);
        Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = ((JSONObject) obj).getString("Result");
        return new String(Helper.hexToBytes(res));
    }

    /**
     * @return
     * @throws Exception
     */
    public String querySymbol() throws Exception {
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"symbol",new byte[]{0},null,0,0);
        Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = ((JSONObject) obj).getString("Result");
        return new String(Helper.hexToBytes(res));
    }

    /**
     * @return
     * @throws Exception
     */
    public long queryDecimals() throws Exception {
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"decimals",new byte[]{0},null,0,0);
        Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = ((JSONObject) obj).getString("Result");
        if (("").equals(res)) {
            return 0;
        }
        return Long.valueOf(Helper.reverse(res), 16);
    }

    /**
     * @return
     * @throws Exception
     */
    public long queryTotalSupply() throws Exception {
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"totalSupply",new byte[]{0},null,0,0);
        Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = ((JSONObject) obj).getString("Result");
        if (res==null||res.equals("")) {
            return 0;
        }
        return Long.valueOf(Helper.reverse(res), 16);
    }

    /**
     * @param address
     * @return
     * @throws Exception
     */
    public String unboundGala(String address) throws Exception {
        if(address==null||address.equals("")){
            throw new SDKException(ErrorCode.ParamErr("address should not be null"));
        }
        String unboundGalaStr = sdk.getConnect().getAllowance("Gala", Address.parse(ZPTContract).toBase58(), address);
        long unboundGala = Long.parseLong(unboundGalaStr);
        return unboundGalaStr;
    }

    /**
     *
     * @param sendAcct
     * @param toAddr
     * @param amount
     * @param payerAcct
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public String withdrawGala(Account sendAcct, String toAddr, long amount, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if (sendAcct == null ||  payerAcct == null ) {
            throw new SDKException(ErrorCode.ParamError);
        }
        if (amount <= 0 || gaslimit<0||gasprice < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gaslimit gasprice should not be less than 0"));
        }
        Transaction tx = makeWithdrawGala(sendAcct.getAddressU160().toBase58(), toAddr, amount, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        sdk.signTx(tx, new Account[][]{{sendAcct}});
        if(!sendAcct.equals(payerAcct)){
            sdk.addSign(tx,payerAcct);
        }
        boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
        if (b) {
            return tx.hash().toString();
        }
        return null;
    }

    /**
     * @param claimer
     * @param toAddr
     * @param amount
     * @param payer
     * @param gaslimit
     * @param gasprice
     * @return
     * @throws Exception
     */
    public Transaction makeWithdrawGala(String claimer, String toAddr, long amount, String payer, long gaslimit, long gasprice) throws Exception {
        if(claimer==null||claimer.equals("")||toAddr==null||toAddr.equals("")||payer==null||payer.equals("")){
            throw new SDKException(ErrorCode.ParamError);
        }
        if (amount <= 0 || gaslimit<0||gasprice < 0) {
            throw new SDKException(ErrorCode.ParamErr("amount or gaslimit gasprice should not be less than 0"));
        }
        List list = new ArrayList();
        list.add(new Struct().add(Address.decodeBase58(claimer), Address.parse(ZPTContract), Address.decodeBase58(toAddr), amount));
        byte[] args = NativeBuildParams.createCodeParamsScript(list);
        Transaction tx = sdk.vm().buildNativeParams(new Address(Helper.hexToBytes(GalaContract)),"transferFrom",args,payer,gaslimit, gasprice);
        return tx;
    }
}
