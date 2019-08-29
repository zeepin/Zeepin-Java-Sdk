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
package demo.ledger.store;

import com.alibaba.fastjson.JSON;
import com.github.zeepin.common.Helper;
import com.github.zeepin.common.UInt256;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

import demo.ledger.common.DataEntryPrefix;
import demo.ledger.common.ExecuteNotify;
import org.iq80.leveldb.DB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 *
 */
public class EventStore {
    private DB db;

    public EventStore(DB db) {
        this.db = db;
    }
    public void SaveEventNotifyByTx(UInt256 txhash,ExecuteNotify notify){
        byte[] key = DataEntryPrefix.getEventNotifyByTxKey(txhash);
        db.put(key, JSON.toJSONBytes(notify));
    }
    public void SaveEventNotifyByBlock(int height,UInt256[] txhashes){
        byte[] key = DataEntryPrefix.getEventNotifyByBlockKey(height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeInt(txhashes.length);
            for(int i=0;i<txhashes.length;i++){
                bw.writeVarBytes(txhashes[i].toArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.put(key, baos.toByteArray());
    }

    public ExecuteNotify GetEventNotifyByTx(UInt256 txhash) {
        byte[] key = DataEntryPrefix.getEventNotifyByTxKey(txhash);
        byte[] value = db.get(key);
        ExecuteNotify notify = JSON.parseObject(new String(value),ExecuteNotify.class);
        return notify;
    }

    public ExecuteNotify[] GetEventNotifyByBlock(int height) {
        byte[] key = DataEntryPrefix.getEventNotifyByBlockKey(height);
        byte[] value = db.get(key);
        ByteArrayInputStream ms = new ByteArrayInputStream(value);
        BinaryReader reader = new BinaryReader(ms);
        try {
            int len = reader.readInt();
            ExecuteNotify[] executeNotifies = new ExecuteNotify[len];
            for (int i = 0; i < len; i++) {
                UInt256 txhash = reader.readSerializable(UInt256.class);
                executeNotifies[i] = GetEventNotifyByTx(txhash);
            }
            return executeNotifies;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

