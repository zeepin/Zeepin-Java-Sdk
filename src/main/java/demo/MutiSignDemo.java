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

/**
 *
 *
 */
public class MutiSignDemo {
    public static String privatekey1 = "49855b16636e70f100cc5f4f42bc20a6535d7414fb8845e7310f8dd065a97221";
    public static String privatekey2 = "1094e90dd7c4fdfd849c14798d725ac351ae0d924b29a279a9ffa77d5737bd96";
    public static String privatekey3 = "bc254cf8d3910bc615ba6bf09d4553846533ce4403bc24f58660ae150a6d64cf";
    public static String privatekey4 = "06bda156eda61222693cc6f8488557550735c329bc7ca91bd2994c894cd3cbc8";
    public static String privatekey5 = "f07d5a2be17bde8632ec08083af8c760b41b5e8e0b5de3703683c3bdcfb91549";
    public static String privatekey6 = "6c2c7eade4c5cb7c9d4d6d85bfda3da62aa358dd5b55de408d6a6947c18b9279";
    public static String privatekey7 = "24ab4d1d345be1f385c75caf2e1d22bdb58ef4b650c0308d9d69d21242ba8618";
    public static String privatekey8 = "87a209d232d6b4f3edfcf5c34434aa56871c2cb204c263f6b891b95bc5837cac";
    public static String privatekey9 = "1383ed1fe570b6673351f1a30a66b21204918ef8f673e864769fa2a653401114";
    public static void main(String[] args) {

        try {
            ZPTSdk zptSdk = getZptSdk();
            com.github.zeepin.account.Account acct1 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey1), zptSdk.defaultSignScheme);
            com.github.zeepin.account.Account acct2 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey2), zptSdk.defaultSignScheme);
            com.github.zeepin.account.Account acct3 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey3), zptSdk.defaultSignScheme);
            com.github.zeepin.account.Account acct4 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey4), zptSdk.defaultSignScheme);
            com.github.zeepin.account.Account acct5 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey5), zptSdk.defaultSignScheme);

            com.github.zeepin.account.Account[] accounts = new com.github.zeepin.account.Account[]{acct1,acct2,acct3};
            int M = 2;
            byte[][] pks = new byte[accounts.length][];
            for(int i=0;i<pks.length;i++){
                pks[i] = accounts[i].serializePublicKey();
            }
            Address sender = Address.addressFromMultiPubKeys(M, pks);
            Address recvAddr = acct5.getAddressU160();

            System.out.println("sender:" + sender.toBase58());
            System.out.println("recvAddr:" + recvAddr.toBase58());
            long amount = 100000;

            Transaction tx = zptSdk.nativevm().zpt().makeTransfer(sender.toBase58(),recvAddr.toBase58(), amount,sender.toBase58(),30000,0);

            zptSdk.addMultiSign(tx,M,pks,acct1);

            String txHex1 = tx.toHexString();
            Transaction tx1 = Transaction.deserializeFrom(Helper.hexToBytes(txHex1));
            System.out.println(tx1.sigs[0].json());

            tx.sigs = null;
            zptSdk.addMultiSign(tx,M,pks,acct2);
            String txHex2 = tx.toHexString();
            Transaction tx2 = Transaction.deserializeFrom(Helper.hexToBytes(txHex2));
            System.out.println(tx2.sigs[0].json());

            tx.sigs = null;
            zptSdk.addMultiSign(tx,M,pks,acct3);
            String txHex3 = tx.toHexString();
            Transaction tx3 = Transaction.deserializeFrom(Helper.hexToBytes(txHex3));
            System.out.println(tx3.sigs[0].json());

            tx.sigs = null;
            zptSdk.addMultiSign(tx,M,pks,tx1.sigs[0].sigData[0]);
            zptSdk.addMultiSign(tx,M,pks,tx2.sigs[0].sigData[0]);
            zptSdk.addMultiSign(tx,M,pks,tx3.sigs[0].sigData[0]);
            String txHex4 = tx.toHexString();
            Transaction tx4 = Transaction.deserializeFrom(Helper.hexToBytes(txHex4));
            System.out.println(tx4.sigs[0].json());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ZPTSdk getZptSdk() throws Exception {
        String ip = "http://127.0.0.1";
//        String ip = "http://101.132.193.149";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        wm.setDefaultConnect(wm.getRestful());

        wm.openWalletFile("MutiSignDemo.json");
        return wm;
    }
}
