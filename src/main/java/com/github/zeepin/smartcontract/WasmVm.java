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

package com.github.zeepin.smartcontract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.sdk.exception.SDKException;
import com.github.zeepin.smartcontract.nativevm.abi.AbiFunction;
import com.github.zeepin.smartcontract.nativevm.abi.BuildParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WasmVm {

    private ZPTSdk sdk;
    public WasmVm(ZPTSdk sdk){
        this.sdk = sdk;
    }

    public String sendTransaction(String contractAddr,String payer, String password,byte[] salt, long gaslimit, long gas, AbiFunction func, boolean preExec) throws Exception {
        byte[] params = BuildParams.serializeAbiFunction(func);
        if (preExec) {
            Transaction tx = sdk.vm().makeInvokeCodeTransaction(contractAddr, null, params, payer, gaslimit,gas);
            Object obj = (String) sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
            String result = ((JSONObject) obj).getString("Result");
            if (Integer.parseInt(result) == 0) {
                throw new SDKException(ErrorCode.OtherError("sendRawTransaction PreExec error: "+ obj));
            }
            return result;
        } else {
            Transaction tx = sdk.vm().makeInvokeCodeTransaction(contractAddr, null, params, payer, gaslimit,gas);
            sdk.signTx(tx, payer, password,salt);
            boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
            if (!b) {
                throw new SDKException(ErrorCode.SendRawTxError);
            }
            return tx.hash().toString();
        }
    }
    
    //将参数对象转化为Json字符串
    public String buildWasmContractJsonParam(Object[] objs) {
        List params = new ArrayList();
        for (int i = 0; i < objs.length; i++) {
            Object val = objs[i];
            if (val instanceof String) {      //判断该参数是否是String类型
                Map map = new HashMap();
                map.put("type","string");
                map.put("value",val);
                params.add(map);
            } else if (val instanceof Integer) {
                Map map = new HashMap();
                map.put("type","int");
                map.put("value",String.valueOf(val));
                params.add(map);
            } else if (val instanceof Long) {
                Map map = new HashMap();
                map.put("type","int64");
                map.put("value",String.valueOf(val));
                params.add(map);
            } else if (val instanceof int[]) {
                Map map = new HashMap();
                map.put("type","int_array");
                map.put("value",val);
                params.add(map);
            } else if (val instanceof long[]) {
                Map map = new HashMap();
                map.put("type","int_array");
                map.put("value",val);
                params.add(map);
            } else {
                continue;
            }
        }
        Map result = new HashMap();
        result.put("Params",params);
        return JSON.toJSONString(result);
    }

    public String sendWasmTransaction(String method,String addr,Object[] inputarg,String payer,long gaslimit, long gas,Account account) throws Exception{
        Constract constract = new Constract();
        constract.setMethod(method);                                         //存入合约调用方法
        constract.setConaddr(Helper.reverse(Helper.hexToBytes(addr)));       //将合约地址转成byte数组
        constract.setArgs(buildWasmContractJsonParam(inputarg));             //将合约参数序列化
        Transaction tx = sdk.vm().makeInvokeCodeTransactionWasm(addr,null,constract.tobytes(), payer,gaslimit,gas);      //构造交易内容 ，constract.tobytes()将constract内容转为byte数组
        sdk.signTx(tx,new Account[][]{{account}});                           //构造签名


        String result = sdk.getConnect().sendRawTransactionString(tx.toHexString());

       // Transaction tx = sdk.vm().makeInvokeCodeTransaction(addr, null, constract.tobytes(), payer, gaslimit,gas);
        return result;



    }

    public String sendWasmTransactionPrepareExc(String method,String addr,Object[] inputarg,String payer,long gaslimit, long gas,Account account) throws Exception{
        Constract constract = new Constract();
        constract.setMethod(method);
        constract.setConaddr(Helper.reverse(Helper.hexToBytes(addr)));
        constract.setArgs(buildWasmContractJsonParam(inputarg));
        Transaction tx = sdk.vm().makeInvokeCodeTransactionWasm(addr,null,constract.tobytes(), payer,gaslimit,gas);
        sdk.signTx(tx,new Account[][]{{account}});


        JSONObject result =(JSONObject) sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = result.getString("Result");
        return new String(Helper.hexToBytes(res));

        // Transaction tx = sdk.vm().makeInvokeCodeTransaction(addr, null, constract.tobytes(), payer, gaslimit,gas);
        //return result;



    }
    public String sendWasmTransactionByOthersPay(String method,String addr,Object[] inputarg,long gaslimit, long gas, Account account,Account payaccount) throws Exception{
    	
    	String payer = payaccount.getAddressU160().toBase58();
        Constract constract = new Constract();
        constract.setMethod(method);
        constract.setConaddr(Helper.reverse(Helper.hexToBytes(addr)));
        constract.setArgs(buildWasmContractJsonParam(inputarg));
        Transaction tx = sdk.vm().makeInvokeCodeTransactionWasm(addr,null,constract.tobytes(), payer,gaslimit,gas);
        
        sdk.signTx(tx, new Account[][]{{account}});
        sdk.addMultiSign(tx, 1, new byte[][]{payaccount.serializePublicKey()}, payaccount);

        String result = sdk.getConnect().sendRawTransactionString(tx.toHexString());

        return result;
    }
     
}
