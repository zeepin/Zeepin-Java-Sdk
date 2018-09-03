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
import com.github.zeepin.sdk.wallet.Identity;

public class RecordTxDemo {


    public static void main(String[] args){
        try {
            ZPTSdk zptSdk = getZptSdk();

            if(zptSdk.getWalletMgr().getWallet().getIdentities().size() < 1) {

                zptSdk.getWalletMgr().createIdentity("passwordtest");
                zptSdk.getWalletMgr().writeWallet();
            }


            Identity id = zptSdk.getWalletMgr().getWallet().getIdentities().get(0);

            String hash = zptSdk.neovm().record().sendPut(id.gid,"passwordtest",new byte[]{},"key","value-test",0,0);
            System.out.println(hash);
            Thread.sleep(6000);
            String res = zptSdk.neovm().record().sendGet(id.gid,"passwordtest",new byte[]{},"key");
            System.out.println("result:"+res);

            //System.out.println(zptSdk.getConnectMgr().getSmartCodeEvent(hash));

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

        wm.openWalletFile("RecordTxDemo.json");

        wm.neovm().record().setContractAddress("80f6bff7645a84298a1a52aa3745f84dba6615cf");
        return wm;
    }
}
