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
package demo.ledger;


import com.alibaba.fastjson.JSON;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.common.UInt256;
import com.github.zeepin.core.payload.DeployCode;

import demo.ledger.common.BookkeeperState;
import demo.ledger.common.ExecuteNotify;
import demo.ledger.store.BlockStore;
import demo.ledger.store.LedgerStore;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Map;

import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;


/**
 *
 *
 */
//https://github.com/fusesource/leveldbjni
public class StoreDemo {
    static String filePath = "leveldb/test";

    public static void main(String[] args) {


        try {

            LedgerStore ledgerStore = new LedgerStore(filePath);
            if (false) {

                    Options options = new Options();
                    options.createIfMissing(true);

                    File file = new File(filePath+"/test");
                    if (!file.exists()) {
                        file = new File(filePath+"/test");
                    }
                    DB db = factory.open(file, options);
                    db.put(bytes("Tampa"), bytes("rocks"));
                    String value = asString(db.get(bytes("Tampa")));
                    db.delete(bytes("Tampa"));
                    System.exit(0);
            }
            if (false) {
                //    blockStore.SaveCurrentBlock(100,UInt256.parse("bda76bbff13b6228a1b69445dfa3cb523613dc2a606eaacbe90b93b588eb8877"));
                Map map = ledgerStore.blockStore.GetCurrentBlock();
                System.out.println(map);
//                blockStore.GetTransaction(UInt256.parse("65d3b2d3237743f21795e344563190ccbe50e9930520b8525142b075433fdd74"));
                // ledgerStore.blockStore.GetBlock(UInt256.parse("234d54e03429e2fdf9f315a648c5e83bb0eecbb06d68c0ed118449e31cf8dfed"));
                //System.out.println(value);
            }


            if (false) {
                DeployCode deployCode = ledgerStore.stateStore.GetContractState(Address.AddressFromVmCode(ZPTSdk.getInstance().nativevm().zpt().getContractAddress()));
                System.out.println(deployCode.description);
                System.out.println(Helper.toHexString(deployCode.code));
            }
            if (false) {
                BookkeeperState state = ledgerStore.stateStore.GetBookkeeperState();
                System.out.println(state.CurrBookkeeper.length);
                System.out.println(state.NextBookkeeper.length);
                for (int i = 0; i < state.CurrBookkeeper.length; i++) {
                    System.out.println(Helper.toHexString(state.CurrBookkeeper[i]));
                }
            }
            if (false) {
                Map map = ledgerStore.stateStore.GetMerkleTree();
                System.out.println(JSON.toJSONString(map));
            }
            if (true) {
                ExecuteNotify notify = ledgerStore.eventStore.GetEventNotifyByTx(UInt256.parse("7e8c19fdd4f9ba67f95659833e336eac37116f74ea8bf7be4541ada05b13503e"));
                System.out.println(notify.Notify[0].States);
                System.out.println(JSON.toJSONString(notify));
            }
            if (true) {
                ExecuteNotify[] notifies = ledgerStore.eventStore.GetEventNotifyByBlock(0);
                System.out.println(JSON.toJSONString(notifies));
                System.out.println(Helper.toHexString(Base64.getDecoder().decode("AAAAAAAAAAAAAAAAAAAAAAAAAAI=")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}