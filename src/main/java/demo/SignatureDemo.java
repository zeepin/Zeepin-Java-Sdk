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
import com.github.zeepin.core.DataSignature;
import com.github.zeepin.sdk.info.AccountInfo;
import com.github.zeepin.sdk.wallet.Account;

public class SignatureDemo {
    public static void main(String[] args) {
        try {
            ZPTSdk zptSdk = getZptSdk();
            if(true) {
                com.github.zeepin.account.Account acct = new com.github.zeepin.account.Account(zptSdk.defaultSignScheme);
                byte[] data = "12345".getBytes();
                byte[] signature = zptSdk.signatureData(acct, data);

                System.out.println(zptSdk.verifySignature(acct.serializePublicKey(), data, signature));
            }
            if(true) {
                com.github.zeepin.account.Account acct = new com.github.zeepin.account.Account(zptSdk.defaultSignScheme);
                byte[] data = "12345".getBytes();
                DataSignature sign = new DataSignature(zptSdk.defaultSignScheme, acct, data);
                byte[] signature = sign.signature();


                com.github.zeepin.account.Account acct2 = new com.github.zeepin.account.Account(false,acct.serializePublicKey());
                DataSignature sign2 = new DataSignature();
                System.out.println(sign2.verifySignature(acct2, data, signature));
            }
            System.exit(0);
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

        wm.openWalletFile("AccountDemo.json");

        return wm;
    }
}
