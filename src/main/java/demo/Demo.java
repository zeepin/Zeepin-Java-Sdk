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
import com.github.zeepin.block.Block;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.governance.VoteInfo;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.sdk.wallet.Account;
import com.github.zeepin.sdk.wallet.Identity;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Demo {
    public static void main(String[] args) {
        try {
            ZPTSdk zeepinSdk = getZptSdk();
//            String ss="d4e24e08b3521066678f0f7e39471da053f0d903e24f601e406e6368abfc7b72";
//            System.out.println(ss.length());

            
            System.out.println(zeepinSdk.getConnect().getBalance("ZTSPC1PEhXHZZDTFtvRDjoKSZrgYboBwDM"));
            System.out.println(zeepinSdk.getConnect().getAllowance("zpt","from","to"));
            System.out.println(zeepinSdk.getConnect().getSmartCodeEvent("00d9336a5e83754815fdd609f7ecce31135428d4fcc40469082658cfdb8b62c4"));
            com.github.zeepin.account.Account acct11M = new com.github.zeepin.account.Account(zeepinSdk.defaultSignScheme);
       //     String prikey111=(  Helper.toHexString( acct11M.serializePrivateKey()));
         //   System.out.println(prikey111);
//            System.out.println(Helper.toHexString(zptSdk.getConnect().getBlock(1).transactions[0].sigs[0].sigData[0]));
            System.out.println(zeepinSdk.getConnect().getBlock(15));

         //   System.out.println(zptSdk.getConnect().getBlockHeight());
            System.out.println("!!!!"+zeepinSdk.getConnect().getBlockJson(0).toString());
            System.out.println("--------------------");
            System.out.println(zeepinSdk.getConnect().getNodeCount());
            System.out.println("--------------------");
            //System.out.println(zptSdk.getConnect().getContractJson("AFmseVrdL9f9oyCzZefL9tG6UbviEH9ugK"));
            String res = zeepinSdk.nativevm().governance().getPeerInfoAll();
            JSONObject jsr = JSONObject.parseObject(res);
            System.out.println("it's getPeerInfoAll:"+jsr.toJSONString());
            System.out.println("--------------------");
            res = zeepinSdk.nativevm().governance().getContractAddress();
            
          //  System.out.println(zptSdk.getConnect().getContractJson("0700000000000000000000000000000000000000").toString());
            
            res = zeepinSdk.nativevm().governance().getPeerInfo("02e9e3a3838612cb5ec05289ea989e52fc11c95cef7d387ebd10a1fbb6621528be");
            System.out.println("getPeerInfo"+res);
            //res = zptSdk.nativevm().governance().voteForPeer(account, peerPubkey, posList, payerAcct, gaslimit, gasprice);
            System.out.println("--------------------");
            Address test = Address.addressFromPubKey("035ad9d8b8350b113cbb3d541e0a89dfd10c981702eb59bce4d1a2bbd13b103a39");
            System.out.println(test.toBase58());
            VoteInfo res2 = zeepinSdk.nativevm().governance().getVoteInfo("035ad9d8b8350b113cbb3d541e0a89dfd10c981702eb59bce4d1a2bbd13b103a39",test);
            System.out.println("---"+res2.json());
       //     System.out.println(zptSdk.getConnect().getStorage("contractaddress", "key"));
            
            

            
//            System.out.println(zptSdk.getConnect().getBlockJson("ee2d842fe7cdf48bc39b34d616a9e8f7f046970ed0a988dde3fe05c9126cce74"));
            //System.out.println(zptSdk.getConnect().getNodeCount());
//            System.out.println(((InvokeCodeTransaction)zptSdk.getConnect().getRawTransaction("c2592940837c2347f6a7b391d4940abb7171dd5dd156b7c031d20a5940142b5a")));
//            System.out.println((zptSdk.getConnect().getTransaction("d441a967315989116bf0afad498e4016f542c1e7f8605da943f07633996c24cc")));
            //System.out.println(zptSdk.getConnect().getSmartCodeEvent(0));
//            System.out.println(zptSdk.getConnect().getContractJson("803ca638069742da4b6871fe3d7f78718eeee78a"));
//            System.out.println(zptSdk.getConnect().getMerkleProof("0087217323d87284d21c3539f216dd030bf9da480372456d1fa02eec74c3226d"));
            //System.out.println(zptSdk.getConnect().getBlockHeightByTxHash("7c3e38afb62db28c7360af7ef3c1baa66aeec27d7d2f60cd22c13ca85b2fd4f3"));
            //String v = (String)zptSdk.getConnect().getStorage("ff00000000000000000000000000000000000001", Address.decodeBase58("TA63xZXqdPLtDeznWQ6Ns4UsbqprLrrLJk").toHexString());
            //System.out.println(v);
            /*
            Block block = zptSdk.getConnect().getBlock(zptSdk.getConnect().getBlockHeight());
            String hash = block.transactions[0].hash().toHexString();
            System.out.println(zptSdk.getConnect().getMerkleProof(hash));
            Object proof = zptSdk.nativevm().GId().getMerkleProof(hash);
            System.out.println(proof);
            System.out.println(zptSdk.nativevm().GId().verifyMerkleProof(JSON.toJSONString(proof)));
            System.exit(0);
            List list = (List) zptSdk.getConnect().getSmartCodeEvent("a12117c319aa6906efd8869ba65c221f4e2ee44a8a2766fd326c8d7125beffbf");

            List states = (List) ((Map) (list.get(0))).get("States");
            List state1 = (List) states.get(0);

            byte[] bys = new byte[state1.toArray().length];
            for (int i = 0; i < bys.length; i++) {
                bys[i] = (byte) ((int) state1.get(i) & 0xff);
            }
            System.out.println(Address.parse(Helper.toHexString(bys)).toBase58());
            System.exit(0);


            zptSdk.getWalletMgr().createAccount("123456");
            System.out.println(zptSdk.getWalletMgr().getWallet());
            System.out.println(zptSdk.getWalletMgr().getWalletFile());
            System.exit(0);
            System.out.println(zptSdk.getWalletMgr().getWallet().getAccounts().get(0));
            zptSdk.getWalletMgr().getWallet().removeAccount(zptSdk.getWalletMgr().getWallet().getAccounts().get(0).address);
            zptSdk.getWalletMgr().writeWallet();
            System.out.println(zptSdk.getWalletMgr().getWallet());
            zptSdk.getWalletMgr().getWallet().setName("name");

            System.exit(0);
            Account acct = zptSdk.getWalletMgr().createAccount("password");
            Identity identity = zptSdk.getWalletMgr().createIdentity("password");
            //Block block = zptSdk.getConnectManager().getBlock(757);
            System.out.println(zptSdk.getConnect().getNodeCount());
            // zptSdk.getOepMgr().getAccount(zptSdk.getOepMgr().getAccounts().get(0).address,"1234567");

            Account info = zptSdk.getWalletMgr().createAccount("123456");
            zptSdk.getWalletMgr().writeWallet();
            */
            //   zptSdk.getOepMgr().createGId("123456");
            //  AccountInfo info2 = zptSdk.getWalletMgr().getAccountInfo(info.address,"123456");
            //  System.out.println(info2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ZPTSdk getZptSdk() throws Exception {
//        String ip = "http://139.219.108.204";
    	//String ip = "http://192.168.199.244";
    	//String ip = "http://18.222.106.205";
    		String ip = "http://127.0.0.1";
        //String ip = "http://18.222.106.205";
//        String ip = "http://101.132.193.149";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        
        wm.setDefaultConnect(wm.getRpc());
        System.out.println(restUrl);
        wm.openWalletFile("Demo3.json");

        return wm;
    }
}
