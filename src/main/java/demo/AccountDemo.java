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

import com.github.zeepin.ZPTSdk;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.crypto.Digest;
import com.github.zeepin.crypto.ECC;
import com.github.zeepin.sdk.info.AccountInfo;
import com.github.zeepin.sdk.wallet.Account;
import com.github.zeepin.sdk.wallet.Identity;
import com.github.zeepin.smartcontract.nativevm.Governance;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDemo {
    public static void main(String[] args) {

        try {
            ZPTSdk zptSdk = getZPTSdk();
            
            com.github.zeepin.account.Account acct = new com.github.zeepin.account.Account(zptSdk.defaultSignScheme);
            //acct.serializePrivateKey();//私钥
            acct.serializePublicKey();//公钥
            acct.getAddressU160().toBase58();//base58地址
            
            //System.out.println("it's acct.serializePrivateKey():"+acct.serializePrivateKey());
            System.out.println("it's acct.serializePublicKey():"+acct.serializePublicKey());
            System.out.println("it's acct.getAddressU160().toBase58():"+acct.getAddressU160().toBase58());
            
            AccountInfo info0 = zptSdk.getWalletMgr().createAccountInfo("11");
            System.out.println(info0.addressBase58);
            zptSdk.getWalletMgr().writeWallet();
            
//            AccountInfo info1 = zptSdk.getWalletMgr().createAccountInfoFromPriKey("password","00d9336a5e83754815fdd609f7ecce31135428d4fcc40469082658cf");
//            System.out.println(info1.addressBase58);
//            com.github.zeepin.account.Account acct00 = zptSdk.getWalletMgr().getAccount(info1.addressBase58,"11");
            
            //1.新建账户，设置密码123456
//            zptSdk.getWalletMgr().createAccounts(2,"11");
//            zptSdk.getWalletMgr().writeWallet();
            

           // Transaction

//            com.github.zeepin.account.Account  accountTest=zptSdk.getWalletMgr().getAccount("ZZc2Nx6aTeqvJcGjCAEYFBqpppP6MhaWYx","123456");
//            long balance = zptSdk.nativevm().gala().queryBalanceOf(accountTest.getAddressU160().toBase58());
//            Governance governance=new  Governance(zptSdk);
           // String result =governance.registerCandidate(accountTest,Helper.toHexString(accountTest.serializePublicKey()),1000000,"GID:ZPT:" + accountTest.getAddressU160().toBase58(),1,20000,0);
          //  System.out.println(result);

/*           String result2= governance.unRegisterCandidate(accountTest,Helper.toHexString(accountTest.serializePublicKey()),accountTest,1000000,0);
            System.out.println(result2);*/

         /* String voteResult=  governance.voteForPeer(accountTest,new String[]{Helper.toHexString(accountTest.serializePublicKey())},new long[]{100},accountTest,1000000,0);
          System.out.println(voteResult);*/

//            String unvoteResult=  governance.unVoteForPeer(accountTest,new String[]{Helper.toHexString(accountTest.serializePublicKey())},new long[]{100},accountTest,1000000,0);
//          System.out.println(unvoteResult);
//
//
//            Address recvAddr = Address.addressFromPubKey("03e90fd233c0e18170d67eca1abc7a182194311ec0ab87bbba7aa84e9e2f0073ac");
//          String kjlk=  recvAddr.toBase58();
//          System.out.println(kjlk);
//
//
//            com.github.zeepin.account.Account acct11M = new com.github.zeepin.account.Account(zptSdk.defaultSignScheme);
//            String prikey111=(  Helper.toHexString( acct11M.serializePrivateKey()));
            //acct11M.serializePrivateKey();//私钥
           // acct11M.serializePublicKey();//公钥
//           System.out.println(Helper.toHexString(acct11M.serializePublicKey()));//
//            String B58=acct11M.getAddressU160().toBase58();//base58地址
//            System.out.println(B58);
//
//            com.github.zeepin.account.Account acct0MM = new com.github.zeepin.account.Account(Helper.hexToBytes(prikey111), zptSdk.defaultSignScheme);

//            System.out.println( Helper.toHexString(acct0MM.serializePublicKey()));//
//            System.out.println(acct0MM.getAddressU160().toBase58());
//
//            byte[] saltt = Base64.getDecoder().decode("0X3NC1UHQGltHc4ikzgzmA==");
//            String prikeyg = com.github.zeepin.account.Account.getGcmDecodedPrivateKey("7a1ccOWFQUGl0HQmc+PSLeKMwbVZ45/YDHTH/+um4O1z/YAWuv+vsr9zusvYXWbj", "1","ANH5bHrrt111XwNEnuPZj6u95Dd6u7G4D6",saltt,16384,zptSdk.defaultSignScheme);
//            com.github.zeepin.account.Account a = new com.github.zeepin.account.Account(Helper.hexToBytes(prikeyg),zptSdk.defaultSignScheme);
//            System.out.println(Helper.toHexString(a.serializePrivateKey()));
//            System.out.println(a.getAddressU160().toBase58());
            //com.github.zeepin.account.Account b = new com.github.zeepin.account.Account(false,a.serializePublicKey());

            //System.out.println(Helper.toHexString(b.serializePublicKey()));
//            System.out.println( a.exportGcmEncryptedPrikey("1",saltt,16384));
            //            zptSdk.getWalletMgr().createAccount("password");
//            zptSdk.getWalletMgr().writeWallet();
            //zptSdk.getWalletMgr().getAccount("AUxEWKBM7zaU8iPSdymNSaZt7Dt9yB1KU6","1", Base64.getDecoder().decode("q6FCsP3XKxaeZaj15QZRqA=="));
           // zptSdk.getWalletMgr().getAccount("AHvSop5MbUX6pnqbXnFC5t3yjqVV5DiL7w","password", Base64.getDecoder().decode("ylsxIy8xq0uh4KjjbhxVLw=="));
          // zptSdk.getWalletMgr().getAccount("ANRoMGmxSLtWyzcDcnfCVnJw3FXdNuC9Vq","passwordtest", Base64.getDecoder().decode("ACm4B8Jr1oBPu++e7YIHow=="));
//            System.exit(0);
//            if(true){
//            	zptSdk.getWalletMgr().createAccount("1");
//                System.exit(0);
//            }
//
//            byte[] salt0 = java.util.Base64.getDecoder().decode("+AX/Aa8VXp0h74PZySZ9RA==");
//            String key0 = "+TDw5opWl5HfGEWUpxblVa5BqVKF2962DoCwi1GYidwWMKvOj7mqaUVx3k/utGLx";
//            System.out.println(Helper.toHexString(salt0)+" "+salt0.length);
//            System.out.println(Helper.toHexString(java.util.Base64.getDecoder().decode(key0)));
//            String prikey0 = com.github.zeepin.account.Account.getGcmDecodedPrivateKey(key0,"1","APrfMuKrAQB5sSb5GF8tx96ickZQJjCvwG", salt0,16384,zptSdk.defaultSignScheme);
//            com.github.zeepin.account.Account acct11 = new com.github.zeepin.account.Account(Helper.hexToBytes(prikey0), zptSdk.defaultSignScheme);
//            System.out.println(acct11.getAddressU160().toBase58());
           // System.exit(0);
            if (false){
   //             AccountInfo info0 = zptSdk.getWalletMgr().createAccountInfo("passwordtest");
                AccountInfo info = zptSdk.getWalletMgr().createAccountInfoFromPriKey("passwordtest","e467a2a9c9f56b012c71cf2270df42843a9d7ff181934068b4a62bcdd570e8be");
                System.out.println(info.addressBase58);
                Account accountInfo = zptSdk.getWalletMgr().importAccount("3JZLD/X45qSFjmRRvRVhcEjKgCJQDPWOsjx2dcTEj58=", "passwordtest",info.addressBase58,new byte[]{});

                com.github.zeepin.account.Account acct0 = zptSdk.getWalletMgr().getAccount(info.addressBase58, "passwordtest",new byte[]{});
            }
//            System.out.println();
            if(true){

   //             byte[] salt = salt0;
//                salt = ECC.generateKey(16);
//                com.github.zeepin.account.Account acct = new com.github.zeepin.account.Account(Helper.hexToBytes("a1a38ccff49fa6476e737d66ef9f18c7507b50eb4804ed8e077744a4a2a74bb6"),zptSdk.defaultSignScheme);
 //               String key = acct.exportGcmEncryptedPrikey("1",salt,16384);
 //               System.out.println(key);
 //               System.out.println(acct.getAddressU160().toBase58());
 //               String prikey = com.github.zeepin.account.Account.getGcmDecodedPrivateKey(key, "1",acct.getAddressU160().toBase58(),salt,16384,zptSdk.defaultSignScheme);
 //               System.out.println(prikey);
            }

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ZPTSdk getZPTSdk() throws Exception {

        String ip = "http://127.0.0.1";
//        String ip = "http://54.222.182.88;
//        String ip = "http://101.132.193.149";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";
        //生成一个新的 zptSdk
        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        wm.setDefaultConnect(wm.getRestful());
        //设置钱包
        wm.openWalletFile("wallet.dat");

        return wm;
    }
}
