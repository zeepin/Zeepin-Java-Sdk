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
import com.github.zeepin.sdk.info.AccountInfo;
import com.github.zeepin.sdk.wallet.Account;

public class WalletDemo {
    public static void main(String[] args) {
        try {
            ZPTSdk zptSdk = getZptSdk();
            if (zptSdk.getWalletMgr().getWallet().getAccounts().size() > 0) {
                zptSdk.getWalletMgr().getWallet().clearAccount();
                zptSdk.getWalletMgr().getWallet().clearIdentity();
                zptSdk.getWalletMgr().writeWallet();
            }
            zptSdk.getWalletMgr().createAccounts(1, "passwordtest");
            zptSdk.getWalletMgr().writeWallet();

            System.out.println("init size: "+zptSdk.getWalletMgr().getWallet().getAccounts().size()+" " +zptSdk.getWalletMgr().getWalletFile().getAccounts().size());
            System.out.println(zptSdk.getWalletMgr().getWallet().toString());
            System.out.println(zptSdk.getWalletMgr().getWalletFile().toString());

            System.out.println();
            zptSdk.getWalletMgr().getWallet().removeAccount(zptSdk.getWalletMgr().getWallet().getAccounts().get(0).address);
            zptSdk.getWalletMgr().getWallet().setVersion("2.0");
            System.out.println("removeAccount size: "+zptSdk.getWalletMgr().getWallet().getAccounts().size()+" " +zptSdk.getWalletMgr().getWalletFile().getAccounts().size());
            System.out.println(zptSdk.getWalletMgr().getWallet().toString());
            System.out.println(zptSdk.getWalletMgr().getWalletFile().toString());

            System.out.println();
            zptSdk.getWalletMgr().resetWallet();
            System.out.println("resetWallet size: "+zptSdk.getWalletMgr().getWallet().getAccounts().size()+" " +zptSdk.getWalletMgr().getWalletFile().getAccounts().size());
            System.out.println(zptSdk.getWalletMgr().getWallet().toString());
            System.out.println(zptSdk.getWalletMgr().getWalletFile().toString());


            System.out.println();
            zptSdk.getWalletMgr().getWallet().removeAccount(zptSdk.getWalletMgr().getWallet().getAccounts().get(0).address);
            zptSdk.getWalletMgr().getWallet().setVersion("2.0");
            System.out.println("removeAccount size: "+zptSdk.getWalletMgr().getWallet().getAccounts().size()+" " +zptSdk.getWalletMgr().getWalletFile().getAccounts().size());
            System.out.println(zptSdk.getWalletMgr().getWallet().toString());
            System.out.println(zptSdk.getWalletMgr().getWalletFile().toString());

            //write wallet
            zptSdk.getWalletMgr().writeWallet();
            System.out.println();
            System.out.println("writeWallet size: "+zptSdk.getWalletMgr().getWallet().getAccounts().size()+" " +zptSdk.getWalletMgr().getWalletFile().getAccounts().size());
            System.out.println(zptSdk.getWalletMgr().getWallet().toString());
            System.out.println(zptSdk.getWalletMgr().getWalletFile().toString());
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

        wm.openWalletFile("WalletDemo.json");

        return wm;
    }
}
