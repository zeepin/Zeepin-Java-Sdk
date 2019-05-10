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
import com.alibaba.fastjson.annotation.JSONField;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.asset.State;
import com.github.zeepin.core.transaction.Transaction;

import java.math.BigInteger;
import java.util.*;

class UserAcct{
    String id;
    String address;
    String withdrawAddr;
    byte[] privkey;
    BigInteger zptBalance;
    BigInteger galaBalance;
}

class Balance{
    @JSONField(name="zpt")
    String zpt;
    @JSONField(name="gala")
    String gala;

    public String getZPT() {
        return zpt;
    }

    public void setZPT(String zpt) {
        this.zpt = zpt;
    }

    public String getGala() {
        return gala;
    }

    public void setGala(String ong) {
        this.gala = gala;
    }
}


class States{
    @JSONField(name="States")
    Object[] states;

    @JSONField(name="ContractAddress")
    String contractAddress;

    public Object[] getStates() {
        return states;
    }

    public void setStates(Object[] states) {
        this.states = states;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

}

class Event{
    @JSONField(name="GasConsumed")
    int gasConsumed;


    @JSONField(name="TxHash")
    String txHash;

    @JSONField(name="State")
    int state;

    @JSONField(name="Notify")
    States[] notify;

    public int getGasConsumed() {
        return gasConsumed;
    }

    public void setGasConsumed(int gasConsumed) {
        this.gasConsumed = gasConsumed;
    }


    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public States[] getNotify() {
        return notify;
    }

    public void setNotify(States[] notify) {
        this.notify = notify;
    }
}



public class ExchangeDemo {

    //init account should have some onts
    public static final String INIT_ACCT_ADDR = "Ad4pjz2bqep4RhQrUAzMuZJkBC3qJ1tZuT";
    public static final String INIT_ACCT_SALT = "OkX96EG0OaCNUFD3hdc50Q==";

    public static final String FEE_PROVIDER = "AS3SCXw8GKTEeXpdwVw7EcC4rqSebFYpfb";
    public static final String FEE_PROVIDER_SALT = "KvKkxNOGm4q4bLkD8TS2PA==";

    //for test all account's pwd is the same
    public static final String PWD = "123456";

    //for generate a multi-sig address
    public static final String MUTI_SIG_ACCT_SEED1_ADDR = "AK98G45DhmPXg4TFPG1KjftvkEaHbU8SHM";
    public static final String MUTI_SIG_ACCT_SEED1_SALT = "rD4ewxv4qHH8FbUkUv6ePQ==";

    public static final String MUTI_SIG_ACCT_SEED2_ADDR = "ALerVnMj3eNk9xe8BnQJtoWvwGmY3x4KMi";
    public static final String MUTI_SIG_ACCT_SEED2_SALT = "1K8a7joYQ+iwj3/+wGICrw==";

    public static final String MUTI_SIG_ACCT_SEED3_ADDR = "AKmowTi8NcAMjZrg7ZNtSQUtnEgdaC65wG";
    public static final String MUTI_SIG_ACCT_SEED3_SALT = "b9oBYBIPvZMw66q1ky+JDQ==";

    //withdraw address for test user
    public static final String WITHDRAW_ADDRESS = "AZbcPX7HyJTWjqogZhnr2qDTh6NNksGSE6";


    public static  String ZPT_NATIVE_ADDRESS = "";
    public static  String Gala_NATIVE_ADDRESS = "";

    public static void main(String[] args) {
        try{
            //simulate a database using hashmap
            HashMap<String,UserAcct> database = new HashMap<String,UserAcct>();

            ZPTSdk zptSdk = getZptSdk();
            ZPT_NATIVE_ADDRESS = Helper.reverse(zptSdk.nativevm().zpt().getContractAddress());
            Gala_NATIVE_ADDRESS = Helper.reverse(zptSdk.nativevm().gala().getContractAddress());

            printlog("++++ starting simulate exchange process ...========");
            printlog("++++ 1. create a random account for user ====");
            String id1 = "id1";
            Account acct1 = new Account(zptSdk.defaultSignScheme);
            String pubkey =  acct1.getAddressU160().toBase58();
        //    byte[] privkey = acct1.serializePrivateKey();
            byte[] privkey = acct1.serializePublicKey();
            printlog("++++ public key is " + acct1.getAddressU160().toBase58());

            UserAcct usr =getNewUserAcct(id1,pubkey,privkey,BigInteger.valueOf(0),BigInteger.valueOf(0));
            usr.withdrawAddr = WITHDRAW_ADDRESS;
            database.put(acct1.getAddressU160().toBase58(),usr);
            //all transfer fee is provide from this account
            Account feeAct = zptSdk.getWalletMgr().getAccount(FEE_PROVIDER,PWD,Base64.getDecoder().decode(FEE_PROVIDER_SALT));

            //create a multi-sig account as a main account
            Account mutiSeedAct1 = zptSdk.getWalletMgr().getAccount(MUTI_SIG_ACCT_SEED1_ADDR,PWD,Base64.getDecoder().decode(MUTI_SIG_ACCT_SEED1_SALT));
            Account mutiSeedAct2 = zptSdk.getWalletMgr().getAccount(MUTI_SIG_ACCT_SEED2_ADDR,PWD,Base64.getDecoder().decode(MUTI_SIG_ACCT_SEED2_SALT));
            Account mutiSeedAct3 = zptSdk.getWalletMgr().getAccount(MUTI_SIG_ACCT_SEED3_ADDR,PWD,Base64.getDecoder().decode(MUTI_SIG_ACCT_SEED3_SALT));

            Address mainAccountAddr = Address.addressFromMultiPubKeys(3,mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey());
            printlog("++++ Main Account Address is :" + mainAccountAddr.toBase58());


            //monitor the charge and withdraw thread
            Thread t = new Thread(new Runnable() {

                long lastblocknum = 0 ;

                @Override
                public void run() {
                    while(true){
                        try{
                            //get latest blocknum:
                            //TODO fix lost block
                            int height = zptSdk.getConnect().getBlockHeight();
                            if (height > lastblocknum){
                                printlog("====== new block sync :" + height);

                                Object  event = zptSdk.getConnect().getSmartCodeEvent(height);
                                if(event == null){
                                    lastblocknum = height;
                                    Thread.sleep(1000);
                                    continue;
                                }
                                printlog("====== event is " + event.toString());

                                List<Event> events = JSON.parseArray(event.toString(), Event.class);
                                if(events == null){
                                    lastblocknum = height;
                                    Thread.sleep(1000);
                                    continue;
                                }
                                if (events.size()> 0){
                                    for(Event ev:events){
                                        printlog("===== State:" + ev.getState());
                                        printlog("===== TxHash:" + ev.getTxHash());
                                        printlog("===== GasConsumed:" + ev.getGasConsumed());

                                        for(States state:ev.notify){

                                            printlog("===== Notify - ContractAddress:" + state.getContractAddress());
                                            printlog("===== Notify - States[0]:" + state.getStates()[0]);
                                            printlog("===== Notify - States[1]:" + state.getStates()[1]);
                                            printlog("===== Notify - States[2]:" + state.getStates()[2]);
                                            printlog("===== Notify - States[3]:" + state.getStates()[3]);

                                            if (ev.getState() == 1){  //exec succeed
                                                Set<String> keys = database.keySet();
                                                //
                                                if ("transfer".equals(state.getStates()[0]) && keys.contains(state.getStates()[2])){
                                                    BigInteger amount = new BigInteger(state.getStates()[3].toString());
                                                    if (ZPT_NATIVE_ADDRESS.equals(state.getContractAddress())){
                                                        printlog("===== charge ZPT :"+state.getStates()[2] +" ,amount:"+amount);
                                                        database.get(state.getStates()[2]).zptBalance = amount.add(database.get(state.getStates()[2]).zptBalance);
                                                    }
                                                    if (Gala_NATIVE_ADDRESS.equals(state.getContractAddress())){
                                                        printlog("===== charge Gala :"+state.getStates()[2] +" ,amount:"+amount);
                                                        database.get(state.getStates()[2]).galaBalance = amount.add(database.get(state.getStates()[2]).galaBalance);
                                                    }
                                                }

                                                //withdraw case
                                                if("transfer".equals(state.getStates()[0]) && mainAccountAddr.toBase58().equals(state.getStates()[1])){

                                                    for(UserAcct ua: database.values()){
                                                        if (ua.withdrawAddr.equals((state.getStates()[2]))){
                                                            BigInteger amount = new BigInteger(state.getStates()[3].toString());
                                                            if (ZPT_NATIVE_ADDRESS.equals(state.getContractAddress())){
                                                                printlog("===== widtdraw "+ amount +" zpt to " + ua.withdrawAddr + " confirmed!");
                                                            }
                                                            if (Gala_NATIVE_ADDRESS.equals(state.getContractAddress())){
                                                                printlog("===== widtdraw "+ amount +" gala to " + ua.withdrawAddr + " confirmed!");
                                                            }

                                                        }
                                                    }

                                                }

                                            }

                                        }

                                    }
                                }

                                lastblocknum = height;


                            }
                            Thread.sleep(1000);

                        }catch(Exception e){
                            printlog("exception 1:"+ e.getMessage());
                        }
                    }
                }
            });

            //monitor the collect
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        while (true){

                            Set<String> keys = database.keySet();

                            List<Account> zptAccts = new ArrayList<Account>() ;
                            List<State> zptStates = new ArrayList<State>();
                            List<Account> galaAccts = new ArrayList<Account>() ;
                            List<State> galaStates = new ArrayList<State>();


                            for(String key:keys){
                                Object balance = zptSdk.getConnect().getBalance(key);
                                printlog("----- balance of " + key + " : " + balance);
                                Balance b = JSON.parseObject(balance.toString(),Balance.class);
                                BigInteger zptbalance = new BigInteger(b.zpt);
                                BigInteger galabalance = new BigInteger(b.gala);

                                if (zptbalance.compareTo(new BigInteger("0")) > 0){
                                    //transfer zpt to main wallet
                                    UserAcct ua = database.get(key);
                                    Account acct = new Account(ua.privkey,zptSdk.defaultSignScheme);
                                    zptAccts.add(acct);
                                    State st = new State(Address.addressFromPubKey(acct.serializePublicKey()),mainAccountAddr,ua.galaBalance.longValue());
                                    zptStates.add(st);
                                }

                                if (galabalance.compareTo(new BigInteger("0")) > 0){
                                    //transfer gala to main wallet
                                    UserAcct ua = database.get(key);
                                    Account acct = new Account(ua.privkey,zptSdk.defaultSignScheme);
                                    galaAccts.add(acct);
                                    State st = new State(Address.addressFromPubKey(acct.serializePublicKey()),mainAccountAddr,ua.galaBalance.longValue());
                                    galaStates.add(st);
                                }
                            }

                            //construct zpt transfer tx
                            if (zptStates.size() > 0) {
                                printlog("----- Will collect zpt to main wallet");
                                Transaction zptTx = zptSdk.nativevm().zpt().makeTransfer(zptStates.toArray(new State[zptStates.size()]), FEE_PROVIDER, 30000, 0);
                                for (Account act : zptAccts) {
                                    zptSdk.addSign(zptTx, act);
                                }
                                //add fee provider account sig
                                zptSdk.addSign(zptTx, feeAct);
                                zptSdk.getConnect().sendRawTransaction(zptTx.toHexString());
                            }

                            //construct gala transfer tx
                            if(galaStates.size() > 0) {
                                printlog("----- Will collect gala to main wallet");
                                Transaction galaTx = zptSdk.nativevm().gala().makeTransfer(galaStates.toArray(new State[galaStates.size()]), FEE_PROVIDER, 30000, 0);
                                for (Account act : galaAccts) {
                                    zptSdk.addSign(galaTx, act);
                                }
                                //add fee provider account sig
                                zptSdk.addSign(galaTx, feeAct);
                                zptSdk.getConnect().sendRawTransaction(galaTx.toHexString());
                            }

                            Thread.sleep(10000);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        printlog("exception 2:"+e.getMessage());
                    }

                }
            });

            t.start();
            t2.start();

            Thread.sleep(2000);
            printlog("++++ 2. charge some zpt to acct1 from init account");
            Account initAccount = zptSdk.getWalletMgr().getAccount(INIT_ACCT_ADDR,PWD,Base64.getDecoder().decode(INIT_ACCT_SALT));
            State st = new State(initAccount.getAddressU160(),acct1.getAddressU160(),1000L);
            Transaction tx = zptSdk.nativevm().zpt().makeTransfer(new State[]{st}, FEE_PROVIDER, 30000, 0);
            zptSdk.addSign(tx,initAccount);
            zptSdk.addSign(tx, feeAct);

            zptSdk.getConnect().sendRawTransaction(tx.toHexString());
            // test is the tx in txpool
            String txhash = tx.hash().toHexString();
            printlog("++++ txhash :"+txhash);
            Object event = zptSdk.getConnect().getMemPoolTxState(txhash);
            printlog(event.toString());


            printlog("++++ 3. charge some gala to acct1 from init account");
            st = new State(initAccount.getAddressU160(),acct1.getAddressU160(),1200L);
            tx = zptSdk.nativevm().gala().makeTransfer(new State[]{st}, FEE_PROVIDER, 30000, 0);
            zptSdk.addSign(tx,initAccount);
            zptSdk.addSign(tx, feeAct);
            zptSdk.getConnect().sendRawTransaction(tx.toHexString());

            Thread.sleep(15000);

            //simulate a withdraw
            //todo must add check the user balance of database
            printlog("++++ withdraw 500 zpts to " + usr.withdrawAddr );
            //reduce the withdraw amount first
            BigInteger wdAmount = new BigInteger("500");
            if(usr.zptBalance.compareTo(wdAmount) > 0) {
                database.get(usr.address).zptBalance = database.get(usr.address).zptBalance.subtract(wdAmount);
                printlog("++++  " + usr.address + " zpt balance : " + database.get(usr.address).zptBalance);
                State wdSt = new State(mainAccountAddr, Address.decodeBase58(usr.withdrawAddr), 500);
                Transaction wdTx = zptSdk.nativevm().zpt().makeTransfer(new State[]{wdSt}, FEE_PROVIDER, 30000, 0);
                zptSdk.addMultiSign(wdTx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct1);
                zptSdk.addMultiSign(wdTx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct2);
                zptSdk.addMultiSign(wdTx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct3);
                zptSdk.addSign(wdTx, feeAct);
                zptSdk.getConnect().sendRawTransaction(wdTx.toHexString());

            }


            //simulate a withdraw
            printlog("++++ withdraw 500 gala to " + usr.withdrawAddr );
            wdAmount = new BigInteger("500");
            //reduce the withdraw amount first
            if(usr.galaBalance.compareTo(wdAmount) > 0) {
                database.get(usr.address).galaBalance = database.get(usr.address).galaBalance.subtract(wdAmount);
                printlog("++++  " + usr.address + " gala balance : " + database.get(usr.address).galaBalance);

                State wdSt = new State(mainAccountAddr, Address.decodeBase58(usr.withdrawAddr), 500);
                Transaction wdTx = zptSdk.nativevm().gala().makeTransfer(new State[]{wdSt}, FEE_PROVIDER, 30000, 0);
                zptSdk.addMultiSign(wdTx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct1);
                zptSdk.addMultiSign(wdTx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct2);
                zptSdk.addMultiSign(wdTx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct3);
                zptSdk.addSign(wdTx, feeAct);
                zptSdk.getConnect().sendRawTransaction(wdTx.toHexString());
            }



            //claim gala
            Object balance = zptSdk.getConnect().getBalance(mainAccountAddr.toBase58());
            printlog("++++ before claime gala ,balance of "+ mainAccountAddr.toBase58() +" is " + balance);
            String uGalaAmt = zptSdk.nativevm().gala().unboundGala(mainAccountAddr.toBase58());
            printlog("++++ unclaimed gala is " + uGalaAmt);
            if(new BigInteger(uGalaAmt).compareTo(new BigInteger("0")) > 0) {
                tx = zptSdk.nativevm().gala().makeWithdrawGala(mainAccountAddr.toBase58(), mainAccountAddr.toBase58(), new BigInteger(uGalaAmt).longValue(), FEE_PROVIDER, 30000, 0);
                zptSdk.addMultiSign(tx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct1);
                zptSdk.addMultiSign(tx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct2);
                zptSdk.addMultiSign(tx, 3, new byte[][]{mutiSeedAct1.serializePublicKey(),mutiSeedAct2.serializePublicKey(),mutiSeedAct3.serializePublicKey()},mutiSeedAct3);
                zptSdk.addSign(tx, feeAct);
                zptSdk.getConnect().sendRawTransaction(tx.toHexString());
                balance = zptSdk.getConnect().getBalance(mainAccountAddr.toBase58());

                Thread.sleep(10000);
                printlog("++++ after claime gala ,balance of " + mainAccountAddr.toBase58() + " is " + balance);
                //distribute gala to users in database
            }

            t.join();
            t2.join();
        }catch (Exception e){
            e.printStackTrace();
            printlog("exception 3:" + e.getMessage());
        }


    }




    public static ZPTSdk getZptSdk() throws Exception {

        String ip = "http://127.0.0.1";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        wm.setDefaultConnect(wm.getRestful());

        String walletfile = "wallet.dat";
        wm.openWalletFile(walletfile);

        return wm;
    }

    public static void printlog(String msg){
        System.out.println(msg);
    }

    public  static UserAcct getNewUserAcct(String id ,String pubkey,byte[] privkey,BigInteger zpt,BigInteger gala){
        UserAcct acct = new UserAcct();
        acct.id = id;
        acct.privkey = privkey;
        acct.address = pubkey;
        acct.zptBalance = zpt;
        acct.galaBalance = gala;

        return acct;
    }
}
