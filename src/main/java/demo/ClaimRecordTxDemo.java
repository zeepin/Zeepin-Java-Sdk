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


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.block.Block;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Common;
import com.github.zeepin.common.Helper;
import com.github.zeepin.crypto.SignatureScheme;
import com.github.zeepin.sdk.info.AccountInfo;
import com.github.zeepin.sdk.wallet.Account;
import com.github.zeepin.sdk.wallet.Identity;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimRecordTxDemo {
    public static void main(String[] args) {

        try {
            ZPTSdk zptSdk = getZptSdk();




            String password = "111111";

            Account payerAccInfo = zptSdk.getWalletMgr().createAccount(password);
            com.github.zeepin.account.Account payerAcc = zptSdk.getWalletMgr().getAccount(payerAccInfo.address,password,payerAccInfo.getSalt());


            if (zptSdk.getWalletMgr().getWallet().getIdentities().size() < 2) {
                Identity identity = zptSdk.getWalletMgr().createIdentity(password);

                zptSdk.nativevm().GId().sendRegister(identity,password,payerAcc,zptSdk.DEFAULT_GAS_LIMIT,0);

                Identity identity2 = zptSdk.getWalletMgr().createIdentity(password);

                zptSdk.nativevm().GId().sendRegister(identity2,password,payerAcc,zptSdk.DEFAULT_GAS_LIMIT,0);

                zptSdk.getWalletMgr().writeWallet();

                Thread.sleep(6000);
            }

            List<Identity> dids = zptSdk.getWalletMgr().getWallet().getIdentities();


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Issuer", dids.get(0).gid);
            map.put("Subject", dids.get(1).gid);

            Map clmRevMap = new HashMap();
            clmRevMap.put("typ","AttestContract");
            clmRevMap.put("addr",dids.get(1).gid.replace(Common.didzpt,""));

            String claim = zptSdk.nativevm().GId().createGIdClaim(dids.get(0).gid,password,dids.get(0).controls.get(0).getSalt(), "claim:context", map, map,clmRevMap,System.currentTimeMillis()/1000 +100000);
            System.out.println(claim);

            boolean b = zptSdk.nativevm().GId().verifyGIdClaim(claim);
            System.out.println(b);

//            System.exit(0);

            Account account = zptSdk.getWalletMgr().importAccount("blDuHRtsfOGo9A79rxnJFo2iOMckxdFDfYe2n6a9X+jdMCRkNUfs4+C4vgOfCOQ5","111111","AazEvfQPcQ2GEFFPLF1ZLwQ7K5jDn81hve",Base64.getDecoder().decode("0hAaO6CT+peDil9s5eoHyw=="));
            AccountInfo info = zptSdk.getWalletMgr().getAccountInfo(account.address,"111111",account.getSalt());
            com.github.zeepin.account.Account account1 = new com.github.zeepin.account.Account(Helper.hexToBytes("75de8489fcb2dcaf2ef3cd607feffde18789de7da129b5e97c81e001793cb7cf"),SignatureScheme.SHA256WITHECDSA);


            String[] claims = claim.split("\\.");

            JSONObject payload = JSONObject.parseObject(new String(Base64.getDecoder().decode(claims[1].getBytes())));

            System.out.println("ClaimId:" + payload.getString("jti"));

     


            System.out.println(Helper.toHexString(dids.get(0).gid.getBytes()));
            System.out.println(Helper.toHexString(dids.get(1).gid.getBytes()));
            System.out.println(Helper.toHexString(payload.getString("jti").getBytes()));
            System.out.println(payload.getString("jti"));




//            boolean b = zptSdk.getGIdTx().verifyGIdClaim(claim);
//            System.out.println(b);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ZPTSdk getZptSdk() throws Exception {

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

        wm.openWalletFile("ClaimRecordTxDemo.json");

        return wm;
    }
}

