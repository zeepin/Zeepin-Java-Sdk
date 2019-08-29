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
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.common.Helper;
import com.github.zeepin.sdk.manager.ECIES;

/**
 *
 */
public class ECIESDemo {
    public static void main(String[] args) {

        try {
            ZPTSdk zptSdk = getZptSdk();

//            com.github.zeepin.account.Account account = new com.github.zeepin.account.Account(Helper.hexToBytes("9a31d585431ce0aa0aab1f0a432142e98a92afccb7bcbcaff53f758df82acdb3"), zptSdk.keyType, zptSdk.curveParaSpec);
//            System.out.println(Helper.toHexString(account.serializePublicKey()));
//            System.out.println(Helper.toHexString(account.serializePrivateKey()));

            byte[] msg = new String("1234567890").getBytes();
            String[] ret = ECIES.Encrypt("1202021401156f187ec23ce631a489c3fa17f292171009c6c3162ef642406d3d09c74d",msg);
            byte[] msg2 = ECIES.Decrypt("9a31d585431ce0aa0aab1f0a432142e98a92afccb7bcbcaff53f758df82acdb3",ret);
//            byte[] msg3 = ECIES.Decrypt(account,ret);
            System.out.println(Helper.toHexString(msg));
            System.out.println(JSON.toJSONString(ret));
            System.out.println(Helper.toHexString(msg2));
//            System.out.println(Helper.toHexString(msg3));

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

        wm.openWalletFile("Demo3.json");

        return wm;
    }
}
