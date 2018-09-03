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
import com.github.zeepin.core.VmType;
import com.github.zeepin.core.asset.Contract;
import com.github.zeepin.core.asset.State;
import com.github.zeepin.core.asset.Transfers;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.crypto.SignatureScheme;

import java.security.Signature;

public class ZPTAssetSM2Demo {
    public static String privatekey1 = "49855b16636e70f100cc5f4f42bc20a6535d7414fb8845e7310f8dd065a97221";
    public static String privatekey2 = "1094e90dd7c4fdfd849c14798d725ac351ae0d924b29a279a9ffa77d5737bd96";
    public static String privatekey3 = "bc254cf8d3910bc615ba6bf09d4553846533ce4403bc24f58660ae150a6d64cf";
    public static String privatekey4 = "06bda156eda61222693cc6f8488557550735c329bc7ca91bd2994c894cd3cbc8";
    public static String privatekey5 = "f07d5a2be17bde8632ec08083af8c760b41b5e8e0b5de3703683c3bdcfb91549";
    public static String zptContractAddr = "ff00000000000000000000000000000000000001";

    public static void main(String[] args) throws Exception {
        ZPTSdk zptSdk = getZptSdk();
        String password = "111111";
        String privatekey0 = "d6aae3603a82499062fe2ddd68840dce417e2e9e7785fbecb3100dd68c4e2d44";

        com.github.zeepin.account.Account acct0 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey0), SignatureScheme.SM3WITHSM2);
        com.github.zeepin.account.Account acct1 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey1), SignatureScheme.SM3WITHSM2);
        com.github.zeepin.account.Account acct2 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey2), SignatureScheme.SM3WITHSM2);
        com.github.zeepin.account.Account acct3 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey3), SignatureScheme.SHA256WITHECDSA);
        com.github.zeepin.account.Account acct4 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey4), SignatureScheme.SHA256WITHECDSA);
        com.github.zeepin.account.Account acct5 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey5), SignatureScheme.SHA256WITHECDSA);

        zptSdk.setSignatureScheme(SignatureScheme.SM3WITHSM2);
        if (false) {

            //transer
            Address sender = acct0.getAddressU160();
//            Address recvAddr = acct5.getAddressU160();
//                Address recvAddr = Address.addressFromMultiPubKeys(2, acct1.serializePublicKey(), acct3.serializePublicKey());
//                recvAddr = Address.decodeBase58("TA5SgQXTeKWyN4GNfWGoXqioEQ4eCDFMqE");
            Address recvAddr123 = Address.addressFromMultiPubKeys(2, acct1.serializePublicKey(), acct2.serializePublicKey(),acct3.serializePublicKey());
            System.out.println("senderAd:" + sender.toBase58());
            System.out.println("recvAddr:" + recvAddr123.toBase58());

            System.out.println("senderAd : zpt :" + zptSdk.nativevm().zpt().queryBalanceOf(sender.toBase58()));
            System.out.println("recvAddr : zpt :" + zptSdk.nativevm().zpt().queryBalanceOf(recvAddr123.toBase58()));

            int amount = 10;

            State state = new State(acct0.getAddressU160(), recvAddr123, amount);
            Transfers transfers = new Transfers(new State[]{state});
            Contract contract = new Contract((byte) 0, Address.parse(zptContractAddr), "transfer", transfers.toArray());
            Transaction tx = zptSdk.vm().makeInvokeCodeTransaction(zptContractAddr, null, contract.toArray(), sender.toBase58(),0,0);
            System.out.println(tx.json());
            zptSdk.signTx(tx, new com.github.zeepin.account.Account[][]{{acct0}});

            System.out.println(tx.hash().toHexString());
//                zptSdk.getConnectMgr().sendRawTransaction(tx.toHexString());

        }

        if (true) {
            //sender address From MultiPubKeys
            Address multiAddr = Address.addressFromMultiPubKeys(2, acct1.serializePublicKey(),acct2.serializePublicKey(), acct3.serializePublicKey());
            System.out.println("sender:" + multiAddr);
            Address recvAddr = acct5.getAddressU160();
            System.out.println("recvAddr:" + recvAddr.toBase58());


            System.out.println("senderAd : zpt :" + zptSdk.nativevm().zpt().queryBalanceOf(multiAddr.toBase58()));
            System.out.println("recvAddr : zpt :" + zptSdk.nativevm().zpt().queryBalanceOf(recvAddr.toBase58()));

            int amount = 1;

            State state = new State(multiAddr, recvAddr, amount);
            Transfers transfers = new Transfers(new State[]{state});
            Contract contract = new Contract((byte) 0, Address.parse(zptContractAddr), "transfer", transfers.toArray());
            String addr = Address.addressFromMultiPubKeys(2, acct1.serializePublicKey(),acct3.serializePublicKey()).toBase58();
            Transaction tx = zptSdk.vm().makeInvokeCodeTransaction(zptContractAddr, null, contract.toArray(), addr,0,0 );
//            System.out.println(tx.json());
            zptSdk.signTx(tx, new com.github.zeepin.account.Account[][]{{acct1, acct3}});
            System.out.println("tx.sigs.length:" + tx.sigs.length);
            System.out.println("tx.sigs.length:" + tx.sigs[0].pubKeys.length);
//            System.out.println(tx.hash().toHexString());
            zptSdk.getConnect().sendRawTransaction(tx.toHexString());


        }

        if (false) {
            //2 sender transfer to 1 reveiver
            Address sender1 = acct0.getAddressU160();
            Address sender2 = Address.addressFromMultiPubKeys(2, acct1.serializePublicKey(), acct2.serializePublicKey(),acct3.serializePublicKey());
            Address recvAddr = acct5.getAddressU160();
            System.out.println("sender1:" + sender1.toBase58());
            System.out.println("sender2:" + sender2.toBase58());
            System.out.println("recvAddr:" + recvAddr.toBase58());

            System.out.println("sender1 : zpt :" + zptSdk.nativevm().zpt().queryBalanceOf(sender1.toBase58()));
            System.out.println("sender2 : zpt :" + zptSdk.nativevm().zpt().queryBalanceOf(sender2.toBase58()));
            System.out.println("recvAddr : zpt :" + zptSdk.nativevm().zpt().queryBalanceOf(recvAddr.toBase58()));

            int amount = 10;
            int amount2 = 20;
            State state = new State(sender1, recvAddr, amount);
            State state2 = new State(sender2, recvAddr, amount2);

            Transfers transfers = new Transfers(new State[]{state, state2});
            Contract contract = new Contract((byte) 0, Address.parse(zptContractAddr), "transfer", transfers.toArray());

            Transaction tx = zptSdk.vm().makeInvokeCodeTransaction(zptContractAddr, null, contract.toArray(),sender1.toBase58(),0,0);
            System.out.println(tx.json());
            zptSdk.signTx(tx, new com.github.zeepin.account.Account[][]{{acct0}, {acct1, acct2}});

            System.out.println(tx.hash().toHexString());
            zptSdk.getConnect().sendRawTransaction(tx.toHexString());

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
        wm.openWalletFile("ZPTAssetSM2Demo.json");
        return wm;
    }
}
