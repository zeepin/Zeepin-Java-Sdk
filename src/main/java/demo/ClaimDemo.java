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
import com.github.zeepin.common.Helper;
import com.github.zeepin.common.UInt256;
import com.github.zeepin.merkle.MerkleVerifier;
import com.github.zeepin.network.rpc.*;
import com.github.zeepin.sdk.exception.SDKException;
import com.github.zeepin.sdk.wallet.Identity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class ClaimDemo {

    public static void main(String[] args) {

        try {
            ZPTSdk zptSdk = getZptSdk();
            String privatekey0 = "c19f16785b8f3543bbaf5e1dbb5d398dfa6c85aaad54fc9d71203ce83e505c07";
            com.github.zeepin.account.Account acct0 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey0), zptSdk.defaultSignScheme);
            List<Identity> dids = zptSdk.getWalletMgr().getWallet().getIdentities();
            if (dids.size() < 2) {
                Identity identity = zptSdk.getWalletMgr().createIdentity("passwordtest");
                zptSdk.nativevm().GId().sendRegister(identity,"passwordtest",acct0,0,0);
                identity = zptSdk.getWalletMgr().createIdentity("passwordtest");
                zptSdk.nativevm().GId().sendRegister(identity,"passwordtest",acct0,0,0);
                dids = zptSdk.getWalletMgr().getWallet().getIdentities();
                Thread.sleep(6000);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Issuer", dids.get(0).gid);
            map.put("Subject", dids.get(1).gid);


            String claim = zptSdk.nativevm().GId().createGIdClaim(dids.get(0).gid,"passwordtest",new byte[]{}, "claim:context", map, map,map,0);
            System.out.println(claim);
            boolean b = zptSdk.nativevm().GId().verifyGIdClaim(claim);
            System.out.println(b);

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

        wm.openWalletFile("ClaimDemo.json");

        return wm;
    }
}
