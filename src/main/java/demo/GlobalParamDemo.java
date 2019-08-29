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
import com.github.zeepin.account.Account;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.globalparams.Params;
import com.github.zeepin.crypto.SignatureScheme;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;
import com.github.zeepin.sdk.wallet.Identity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class GlobalParamDemo {
    public static void main(String[] args) throws Exception {

        ZPTSdk zptsdk = getZptsdk();
        String password = "111111";
        String privateKey  = "f9d2d30ffb22dffdf4f14ad6f1303460efc633ea8a3014f638eaa19c259bada1";
        String privateKey2 = "75de8489fcb2dcaf2ef3cd607feffde18789de7da129b5e97c81e001793cb7cf";
        String privateKey3 ="24ab4d1d345be1f385c75caf2e1d22bdb58ef4b650c0308d9d69d21242ba8618";
        Account account = new Account(Helper.hexToBytes(privateKey),SignatureScheme.SHA256WITHECDSA);
        Account account1 = new Account(Helper.hexToBytes(privateKey2),SignatureScheme.SHA256WITHECDSA);
        Account account2 = new Account(Helper.hexToBytes(privateKey3),SignatureScheme.SHA256WITHECDSA);

        Account account1s = getAccount("YfOr9im4rOciy3cV7JkVo9QCfrRT4IGLa/CZKUJfL29pM6Zi1oVEM67+8MezMIro","1","AXmQDzzvpEtPkNwBEFsREzApTTDZFW6frD","RCIo60eCJAwzkTYmIfp3GA==");
        Account account2s = getAccount("gpgMejEHzawuXG+ghLkZ8/cQsOJcs4BsFgFjSaqE7SC8zob8hqc6cDNhJI/NBkk+","1","AY5W6p4jHeZG2jjW6nS1p4KDUhcqLkU6jz","tuLGZOimilSnypT91WrenQ==");
        Account account3s = getAccount("guffI05Eafq9F0j3/eQxHWGo1VN/xpeIkXysEPeH51C2YHYCNnCWTWAdqDB7lonl","1","ALZVrZrFqoSvqyi38n7mpPoeDp7DMtZ9b6","oZPg+5YotRWStVsRMYlhfg==");
        Account account4s = getAccount("fAknSuXzMMC0nJ2+YuTpTLs6Hl5Dc0c2zHZBd2Q7vCuv8Wt97uYz1IU0t+AtrWts","1","AMogjmLf2QohTcGST7niV75ekZfj44SKme","0BVIiUf46rb/e5dVZIwfrg==");
        Account account5s = getAccount("IufXVQfrL3LI7g2Q7dmmsdoF7BdoI/vHIsXAxd4qkqlkGBYj3pcWHoQgdCF+iVOv","1","AZzQTkZvjy7ih9gjvwU8KYiZZyNoy6jE9p","zUtzh0B4UW0wokzL+ILdeg==");
        Account account6s = getAccount("PYEJ1c79aR7bxdzvBlj3lUMLp0VLKQHwSe+/OS1++1qa++gBMJJmJWJXUP5ZNhUs","1","AKEqQKmxCsjWJz8LPGryXzb6nN5fkK1WDY","uJhjsfcouCGZQUdHO2TZZQ==");
        Account account7s = getAccount("ZG/SfHRArUkopwhQS1MW+a0fvQvyN1NnwonU0oZH8y1bGqo5T+dQz3rz1qsXqFI2","1","AQNpGWz4oHHFBejtBbakeR43DHfen7cm8L","6qiU9bgK/+1T2V8l14mszg==");

        Address multiAddress = Address.addressFromMultiPubKeys(5,account1s.serializePublicKey(),account2s.serializePublicKey(),account3s.serializePublicKey(),account4s.serializePublicKey(),account5s.serializePublicKey(),account6s.serializePublicKey(),account7s.serializePublicKey());


        Address multiAddress2 = Address.addressFromMultiPubKeys(2,account1s.serializePublicKey(),account2s.serializePublicKey(),account3s.serializePublicKey());
        if(zptsdk.getWalletMgr().getWallet().getIdentities().size() < 1) {
            Identity identity = zptsdk.getWalletMgr().createIdentityFromPriKey(password,privateKey2);
            String txhash = zptsdk.nativevm().GId().sendRegister(identity,password,account,zptsdk.DEFAULT_GAS_LIMIT,0);
            Thread.sleep(6000);
            System.out.println(zptsdk.getConnect().getSmartCodeEvent(txhash));
            System.out.println(privateKey);
        }

        if(false){
//            String txhash = zptsdk.nativevm().gParams().transferAdmin(account,account1.getAddressU160(),account,zptsdk.DEFAULT_GAS_LIMIT,0);
            String txhash = zptsdk.nativevm().gParams().transferAdmin(5,new Account[]{account1s,account2s,account3s,account4s,account5s,account6s,account7s},
                    multiAddress2,account,zptsdk.DEFAULT_GAS_LIMIT,0);
            Thread.sleep(6000);
            System.out.println(zptsdk.getConnect().getSmartCodeEvent(txhash));

        }
        if(false){
//            String txhash = zptsdk.nativevm().gParams().acceptAdmin(account1,account,zptsdk.DEFAULT_GAS_LIMIT,0);

            String txhash = zptsdk.nativevm().gParams().acceptAdmin(multiAddress2,2,new Account[]{account1s,account2s,account3s},account,zptsdk.DEFAULT_GAS_LIMIT,0);
            Thread.sleep(6000);
            System.out.println(zptsdk.getConnect().getSmartCodeEvent(txhash));
        }
        if(false){
            Address multiAddr3 = Address.addressFromMultiPubKeys(2,account2s.serializePublicKey(),account3s.serializePublicKey(),account4s.serializePublicKey());
//            String txhash = zptsdk.nativevm().gParams().setOperator(account1,account2.getAddressU160(),account,zptsdk.DEFAULT_GAS_LIMIT,0);
            String txhash = zptsdk.nativevm().gParams().setOperator(2,new Account[]{account2s,account3s,account4s},
                    multiAddr3,account,zptsdk.DEFAULT_GAS_LIMIT,0);
            Thread.sleep(6000);
            System.out.println(zptsdk.getConnect().getSmartCodeEvent(txhash));
        }

        if(false){
            Params params = new Params(new com.github.zeepin.core.globalparams.Param[]{new com.github.zeepin.core.globalparams.Param("key2","value2")});
            System.out.println(Helper.toHexString(params.toArray()));
//            String txhash = zptsdk.nativevm().gParams().setGlobalParam(account1,params,account,zptsdk.DEFAULT_GAS_LIMIT,0);
            String txhash = zptsdk.nativevm().gParams().setGlobalParam(2,new Account[]{account1s,account2s,account3s},
                    params,account,zptsdk.DEFAULT_GAS_LIMIT,0);
            Thread.sleep(6000);
            System.out.println(zptsdk.getConnect().getSmartCodeEvent(txhash));
        }
        if(false){
//            String txhash = zptsdk.nativevm().gParams().createSnapshot(account2,account,zptsdk.DEFAULT_GAS_LIMIT,0);
            String txhash = zptsdk.nativevm().gParams().createSnapshot(2,new Account[]{account1s,account2s,account3s},account,zptsdk.DEFAULT_GAS_LIMIT,0);
            Thread.sleep(6000);
            System.out.println(zptsdk.getConnect().getSmartCodeEvent(txhash));
        }
        if(true){
            String res = zptsdk.nativevm().gParams().getGlobalParam(new String[]{"key2"});
            System.out.println("res:" + res);
            byte[] resbytes = Helper.hexToBytes(res);
            ByteArrayInputStream ms = new ByteArrayInputStream(resbytes);
            BinaryReader reader = new BinaryReader(ms);
            long paramNum = Helper.BigIntFromNativeBytes(reader.readVarBytes()).longValue();
            String key = null;
            String value = null;
            for(int i = 0;i<paramNum;i++){
                key = reader.readVarString();
                value = reader.readVarString();
            }

            System.out.println("key:" + key);
            System.out.println("value:" + value);
        }
    }

    public static Account getAccount(String enpri,String password,String address,String salt) throws Exception {
        String privateKey = Account.getGcmDecodedPrivateKey(enpri,password,address,Base64.getDecoder().decode(salt),16384,SignatureScheme.SHA256WITHECDSA);
        Account account = new Account(Helper.hexToBytes(privateKey),SignatureScheme.SHA256WITHECDSA);
//        System.out.println(Helper.toHexString(account.serializePublicKey()));
        return account;
    }


    public static ZPTSdk getZptsdk() throws Exception {

        String ip = "http://127.0.0.1";
//        String ip = "http://54.222.182.88;
//        String ip = "http://101.132.193.149";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        wm.setDefaultConnect(wm.getRestful());

        wm.openWalletFile("GlobalParamDemo.json");

        return wm;
    }
}

