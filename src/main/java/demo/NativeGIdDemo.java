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
package demo;

import com.github.zeepin.ZPTSdk;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Common;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.gid.Attribute;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.crypto.SignatureScheme;
import com.github.zeepin.sdk.info.AccountInfo;
import com.github.zeepin.sdk.info.IdentityInfo;
import com.github.zeepin.sdk.wallet.Account;
import com.github.zeepin.sdk.wallet.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeGIdDemo {

    public static void main(String[] args) {

        String password = "111111";

        try {
            ZPTSdk zptSdk = getZptSdk();

            Account payer = zptSdk.getWalletMgr().createAccount(password);

            com.github.zeepin.account.Account payerAcct = zptSdk.getWalletMgr().getAccount(payer.address,password,zptSdk.getWalletMgr().getWallet().getAccount(payer.address).getSalt());
            String privatekey0 = "c19f16785b8f3543bbaf5e1dbb5d398dfa6c85aaad54fc9d71203ce83e505c07";
            String privatekey1 = "2ab720ff80fcdd31a769925476c26120a879e235182594fbb57b67c0743558d7";
            com.github.zeepin.account.Account account1 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey1),SignatureScheme.SHA256WITHECDSA);


            if(true){
                Identity identity3 = zptSdk.getWalletMgr().createIdentity(password);
                Attribute[] attributes = new Attribute[1];
                attributes[0] = new Attribute("key1".getBytes(),"String".getBytes(),"value1".getBytes());
                zptSdk.nativevm().GId().sendRegisterWithAttrs(identity3,password,attributes,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                zptSdk.getWalletMgr().writeWallet();
                Thread.sleep(6000);
                String ddo = zptSdk.nativevm().GId().sendGetDDO(identity3.gid);
                System.out.println(ddo);
                System.exit(0);
            }
            if(false){
                if(zptSdk.getWalletMgr().getWallet().getIdentities().size() < 1){
                    Identity identity = zptSdk.getWalletMgr().createIdentity(password);
                    zptSdk.nativevm().GId().sendRegister(identity,password,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                    zptSdk.getWalletMgr().writeWallet();
                    Thread.sleep(6000);
                }
                Identity identity = zptSdk.getWalletMgr().getWallet().getIdentities().get(0);
//                String ddo = zptSdk.nativevm().GId().sendGetDDO(identity.gid);
//                System.out.println(ddo);

                Attribute[] attributes = new Attribute[1];
                attributes[0] = new Attribute("key1".getBytes(),"String".getBytes(),"value1".getBytes());
                byte[] salt = identity.controls.get(0).getSalt();
//                zptSdk.nativevm().GId().sendAddAttributes(identity.gid,password,identity.controls.get(0).getSalt(),attributes,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
//                zptSdk.nativevm().GId().sendRemoveAttribute(identity.gid,password,identity.controls.get(0).getSalt(),"key1",payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
//                zptSdk.nativevm().GId().sendAddRecovery(identity.gid,password,salt,account1.getAddressU160().toBase58(),payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
//                zptSdk.nativevm().GId().sendAddPubKey(identity.gid,password,salt,Helper.toHexString(account1.serializePublicKey()),payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                zptSdk.nativevm().GId().sendRemovePubKey(identity.gid,password,salt,Helper.toHexString(account1.serializePublicKey()),payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                Thread.sleep(6000);
                String ddo2 = zptSdk.nativevm().GId().sendGetDDO(identity.gid);
                System.out.println(ddo2);
                System.out.println(account1.getAddressU160().toBase58());
                System.exit(0);
            }
            Account account = zptSdk.getWalletMgr().createAccountFromPriKey(password,privatekey0);
            if(zptSdk.getWalletMgr().getWallet().getIdentities().size() < 3){
                Identity identity = zptSdk.getWalletMgr().createIdentity(password);
                Transaction tx = zptSdk.nativevm().GId().makeRegister(identity.gid,password,new byte[]{},payer.address,zptSdk.DEFAULT_GAS_LIMIT,0);
                zptSdk.signTx(tx,identity.gid.replace(Common.didzpt,""),password,new byte[]{});
                zptSdk.addSign(tx,payerAcct);
                zptSdk.getConnect().sendRawTransaction(tx);

                Identity identity2 = zptSdk.getWalletMgr().createIdentity(password);
                zptSdk.nativevm().GId().sendRegister(identity2,password,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);

                Identity identity3 = zptSdk.getWalletMgr().createIdentity(password);
                Attribute[] attributes = new Attribute[1];
                attributes[0] = new Attribute("key1".getBytes(),"String".getBytes(),"value1".getBytes());
                zptSdk.nativevm().GId().sendRegisterWithAttrs(identity3,password,attributes,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                zptSdk.getWalletMgr().writeWallet();
                Thread.sleep(6000);

            }
            List<Identity> dids = zptSdk.getWalletMgr().getWallet().getIdentities();
            System.out.println("dids.get(0).gid:" + dids.get(0).gid);
//            System.out.println("dids.get(1).gid:" + dids.get(1).gid);
//            System.out.println("dids.get(2).gid:" + dids.get(2).gid);
            String ddo1 = zptSdk.nativevm().GId().sendGetDDO(dids.get(0).gid);
//            String publicKeys = zptSdk.nativevm().GId().sendGetPublicKeys(dids.get(0).gid);
//            String ddo2 = zptSdk.nativevm().GId().sendGetDDO(dids.get(1).gid);
//            String ddo3 = zptSdk.nativevm().GId().sendGetDDO(dids.get(2).gid);

            System.out.println("ddo1:" + ddo1);
//            System.out.println("ddo2:" + ddo2);
//            System.out.println("ddo3:" + ddo3);

            IdentityInfo info2 = zptSdk.getWalletMgr().getIdentityInfo(dids.get(1).gid,password,new byte[]{});
            IdentityInfo info3 = zptSdk.getWalletMgr().getIdentityInfo(dids.get(2).gid,password,new byte[]{});

            com.github.zeepin.account.Account acct = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey0),SignatureScheme.SHA256WITHECDSA);
            com.github.zeepin.account.Account acct2 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey1),SignatureScheme.SHA256WITHECDSA);
            Address multiAddr = Address.addressFromMultiPubKeys(2,acct.serializePublicKey(),acct2.serializePublicKey());

            if(false){
                Account account2 = zptSdk.getWalletMgr().createAccountFromPriKey(password, privatekey1);
//                zptSdk.nativevm().GId().sendChangeRecovery(dids.get(0).gid,account2.address,account.address,password,zptSdk.DEFAULT_GAS_LIMIT,0);
                String txhash2 = zptSdk.nativevm().GId().sendAddRecovery(dids.get(0).gid,password,new byte[]{},multiAddr.toBase58(),payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                Thread.sleep(6000);
                Object obj = zptSdk.getConnect().getSmartCodeEvent(txhash2);
                System.out.println(obj);
                System.out.println(zptSdk.nativevm().GId().sendGetDDO(dids.get(0).gid));
            }

            if(false){
                zptSdk.nativevm().GId().sendAddPubKey(dids.get(0).gid,password,new byte[]{},info3.pubkey,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                zptSdk.nativevm().GId().sendRemovePubKey(dids.get(0).gid,account.address,password,new byte[]{},info2.pubkey,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                zptSdk.nativevm().GId().sendAddPubKey(dids.get(0).gid,account.address,password,new byte[]{},info2.pubkey,payerAcct,zptSdk.DEFAULT_GAS_LIMIT,0);
                Transaction tx = zptSdk.nativevm().GId().makeAddPubKey(dids.get(0).gid,multiAddr.toBase58(),null,info2.pubkey,payer.address,zptSdk.DEFAULT_GAS_LIMIT,0);
    //          zptSdk.signTx(tx,new com.github.zeepin.account.Account[][]{{acct,acct2}});
    //          zptSdk.addSign(tx,payerAcc.address,password);
    //          zptSdk.getConnect().sendRawTransaction(tx.toHexString());
            }


            if(false){
                String ddo4 = zptSdk.nativevm().GId().sendGetDDO(dids.get(0).gid);
                System.out.println("ddo4:" + ddo4);
                System.exit(0);
                System.out.println("ddo1:" + ddo1);
                System.out.println("publicKeysState:" + zptSdk.nativevm().GId().sendGetKeyState(dids.get(0).gid,1));
                System.out.println("attributes:" + zptSdk.nativevm().GId().sendGetAttributes(dids.get(0).gid));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static ZPTSdk getZptSdk() throws Exception {
        String ip = "http://127.0.0.1";
//        String ip = "http://139.219.129.55";
//        String ip = "http://101.132.193.149";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        wm.setDefaultConnect(wm.getRestful());

        wm.openWalletFile("NativeGIdDemo.json");
        return wm;
    }
}
