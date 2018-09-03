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
package demo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.sdk.wallet.Account;
import com.github.zeepin.sdk.wallet.Identity;
import com.github.zeepin.smartcontract.neovm.abi.AbiFunction;
import com.github.zeepin.smartcontract.neovm.abi.AbiInfo;
import com.github.zeepin.smartcontract.neovm.abi.BuildParams;

import java.util.ArrayList;
import java.util.List;

public class AuthDemo {

    public static void main(String[] args){
        ZPTSdk zptSdk;
        String password = "111111";
        String abi = "{\"hash\":\"0x4d0d780599010f943c37c795a22f6161d49436cf\",\"entrypoint\":\"Main\",\"functions\":[{\"name\":\"Main\",\"parameters\":[{\"name\":\"operation\",\"type\":\"String\"},{\"name\":\"token\",\"type\":\"Array\"},{\"name\":\"args\",\"type\":\"Array\"}],\"returntype\":\"Any\"},{\"name\":\"foo\",\"parameters\":[{\"name\":\"operation\",\"type\":\"ByteArray\"},{\"name\":\"token\",\"type\":\"Integer\"}],\"returntype\":\"Boolean\"},{\"name\":\"init\",\"parameters\":[],\"returntype\":\"Boolean\"}],\"events\":[]}";
        Account payer;
        try {
            zptSdk = getZptSdk();
//            System.out.println(Helper.toHexString("initContractAdmin".getBytes()));
//            System.exit(0);
            // 8007c33f29a892e3a36e2cfec657eff1d7431e8f
            String privatekey0 = "523c5fcf74823831756f0bcb3634234f10b3beb1c05595058534577752ad2d9f";
            String privatekey1 ="83614c773f668a531132e765b5862215741c9148e7b2f9d386b667e4fbd93e39";
            com.github.zeepin.account.Account acct0 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey0), zptSdk.defaultSignScheme);

            com.github.zeepin.account.Account account = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey1), zptSdk.defaultSignScheme);
            System.out.println(acct0.getAddressU160().toBase58());
            System.out.println(account.getAddressU160().toBase58());
            
            Address sender = acct0.getAddressU160();
            
            System.out.println(sender.toBase58());

            //base58地址解码
            sender = Address.decodeBase58("ZC3Fmgr3oS56Rg9vxZeVo2mwMMcUiYGcPp");
            System.out.println(sender.toBase58());
            //多签地址生成：
            Address recvAddr = Address.addressFromMultiPubKeys(2, acct0.serializePublicKey(), account.serializePublicKey());
            System.out.println("it's double:"+recvAddr.toBase58());
            Address recvAddr2 = Address.addressFromMultiPubKeys(2, acct0.serializePublicKey(), account.serializePublicKey());
            System.out.println("it's double2:"+recvAddr2.toBase58());
            
            payer = zptSdk.getWalletMgr().createAccount(password);
            com.github.zeepin.account.Account payerAcct = zptSdk.getWalletMgr().getAccount(payer.address,password,payer.getSalt());
            Identity identity = null;
            Identity identity2 = null;
            Identity identity3 = null;
            List<Identity> dids = zptSdk.getWalletMgr().getWallet().getIdentities();
            if(zptSdk.getWalletMgr().getWallet().getIdentities().size() < 3){
               // Identity identity1 = zptSdk.getWalletMgr().importIdentity("",password,"".getBytes(),acct0.getAddressU160().toBase58());
                identity = zptSdk.getWalletMgr().createIdentityFromPriKey(password,privatekey0);

                zptSdk.nativevm().GId().sendRegister(identity,password,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
              //  zptSdk.nativevm().GId().sendRegister(identity1,password,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                identity2 = zptSdk.getWalletMgr().createIdentity(password);
                zptSdk.nativevm().GId().sendRegister(identity2,password,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);

                identity3 = zptSdk.getWalletMgr().createIdentity(password);
                zptSdk.nativevm().GId().sendRegister(identity3,password,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);

                zptSdk.getWalletMgr().writeWallet();

                Thread.sleep(6000);
            }else {
                identity = zptSdk.getWalletMgr().getWallet().getIdentity(dids.get(0).gid);
                identity2 = zptSdk.getWalletMgr().getWallet().getIdentity(dids.get(1).gid);
                identity3 = zptSdk.getWalletMgr().getWallet().getIdentity(dids.get(2).gid);
            }



            System.out.println("GId1:" +dids.get(0).gid+" "+Helper.toHexString(dids.get(0).gid.getBytes()));
            System.out.println("GId2:" +dids.get(1).gid);
            System.out.println("GId3:" +dids.get(2).gid);
            Account account1 = zptSdk.getWalletMgr().createAccount(password);
            System.out.println("####" + account1.address);

//            System.out.println("ddo1:" + zptSdk.nativevm().GId().sendGetDDO(dids.get(0).GId));
//            System.out.println("ddo2:" + zptSdk.nativevm().GId().sendGetDDO(dids.get(1).GId));
//            System.out.println("ddo3:" + zptSdk.nativevm().GId().sendGetDDO(dids.get(2).GId));

            String contractAddr = "b93f1d81a00f95d09228f1f8934a71dd0e89999f";

            if(false){
                 identity = zptSdk.getWalletMgr().createIdentityFromPriKey(password,privatekey1);
                zptSdk.nativevm().GId().sendRegister(identity,password,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                System.out.println(Helper.toHexString(identity.gid.getBytes()));

            }
            //Identity identity = zptSdk.getWalletMgr().createIdentityFromPriKey(password,privatekey1);
            System.out.println(account.getAddressU160().toBase58());
            System.out.println(identity.gid);
            System.out.println(Helper.toHexString(account.getAddressU160().toArray()));
            System.out.println(Helper.toHexString(identity.gid.getBytes()));

            if(false){
                AbiInfo abiinfo = JSON.parseObject(abi, AbiInfo.class);
                String name = "init";
                AbiFunction func = abiinfo.getFunction(name);
                func.name = name;
                System.out.println(func);
                func.setParamsValue();
                //Object obj =  zptSdk.neovm().sendTransaction(Helper.reverse(contractAddr),null,null,0,0,func, true);
                Object obj =  zptSdk.neovm().sendTransaction(Helper.reverse(contractAddr),acct0,acct0,30000,0,func, false);
                System.out.println(obj);
            }

            if(false){

//                String txhash = zptSdk.nativevm().auth().sendInit(dids.get(0).GId,password,codeaddress,account,zptSdk.DEFAULT_GAS_LIMIT,0);

                //String txhash = zptSdk.nativevm().auth().sendTransfer(identity.GId,password,identity.controls.get(0).getSalt(),1,Helper.reverse(contractAddr),identity2.GId,account,zptSdk.DEFAULT_GAS_LIMIT,0);

              //  String txhash = zptSdk.nativevm().auth().assignFuncsToRole(identity2.GId,password,identity2.controls.get(0).getSalt(),1,Helper.reverse(contractAddr),"role",new String[]{"foo"},account,zptSdk.DEFAULT_GAS_LIMIT,0);
                String txhash = zptSdk.nativevm().auth().assignGIdsToRole(identity2.gid,password,identity2.controls.get(0).getSalt(),1,Helper.reverse(contractAddr),"role",new String[]{identity2.gid},account,zptSdk.DEFAULT_GAS_LIMIT,0);
                //String txhash = zptSdk.nativevm().auth().delegate(identity2.GId,password,identity2.controls.get(0).getSalt(),1,Helper.reverse(contractAddr),identity3.GId,"role",6000,1,account,zptSdk.DEFAULT_GAS_LIMIT,0);
               //String txhash = zptSdk.nativevm().auth().withdraw(identity2.GId,password,identity2.controls.get(0).getSalt(),1,Helper.reverse(contractAddr),identity3.GId,"role",account,zptSdk.DEFAULT_GAS_LIMIT,0);
              //  String txhash = zptSdk.nativevm().auth().verifyToken(identity2.GId,password,identity2.controls.get(0).getSalt(),1,Helper.reverse(contractAddr),"foo");
//                Thread.sleep(6000);
//                Object object = zptSdk.getConnect().getSmartCodeEvent(txhash);
//                System.out.println(object);


//     String txhash2 = zptSdk.nativevm().auth().withdraw(dids.get(0).GId,password,contractAddr,dids.get(1).GId,"role",1,payer.address,password,zptSdk.DEFAULT_GAS_LIMIT,0);
                Thread.sleep(6000);
                Object object2 = zptSdk.getConnect().getSmartCodeEvent(txhash);
                System.out.println(object2);
            }
            if(true){
                zptSdk.nativevm().auth().queryAuth(contractAddr,identity2.gid);
            }
            if(false){
                AbiInfo abiinfo = JSON.parseObject(abi, AbiInfo.class);
                String name = "foo";
                AbiFunction func = abiinfo.getFunction(name);
                func.name = name;
                System.out.println(func);
                func.setParamsValue(identity2.gid.getBytes(),Long.valueOf(1));

                acct0 = zptSdk.getWalletMgr().getAccount(identity2.gid,password,identity2.controls.get(0).getSalt());
                System.out.println("pk:"+Helper.toHexString(acct0.serializePublicKey()));
                Object obj =  zptSdk.neovm().sendTransaction(Helper.reverse(contractAddr),acct0,account,30000,0,func, true);
                System.out.println(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ZPTSdk getZptSdk() throws Exception {

//        String ip = "http://test1.zeepin.net";
        String ip = "http://127.0.0.1";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRestful(restUrl);

//        wm.setRestful("http://test1.zeepin.net:20334");
//        wm.setRestful("http://192.168.50.121:9099");
        //
        wm.setDefaultConnect(wm.getRestful());
        wm.openWalletFile("AuthDemo.json");
        return wm;
    }
}
