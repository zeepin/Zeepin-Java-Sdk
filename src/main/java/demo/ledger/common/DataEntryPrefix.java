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
package demo.ledger.common;

import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.common.UInt256;
import com.github.zeepin.io.BinaryWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 *
 */
public enum DataEntryPrefix {
    DATA_BLOCK(0x00), //Block height => block hash key prefix
    DATA_HEADER(0x01), //Block hash => block hash key prefix
    DATA_TRANSACTION(0x02), //Transction hash = > transaction key prefix

    // Transaction
    ST_BOOKKEEPER(0x03), //BookKeeper state key prefix
    ST_CONTRACT(0x04), //Smart contract state key prefix
    ST_STORAGE(0x05), //Smart contract storage key prefix
    ST_VALIDATOR(0x07), //no use
    ST_VOTE(0x08), //Vote state key prefix

    IX_HEADER_HASH_LIST(0x09), //Block height => block hash key prefix

    //SYSTEM
    SYS_CURRENT_BLOCK(0x10), //Current block key prefix
    SYS_VERSION(0x11), //Store version key prefix
    SYS_CURRENT_STATE_ROOT(0x12), //no use
    SYS_BLOCK_MERKLE_TREE(0x13), // Block merkle tree root key prefix

    EVENT_NOTIFY(0x14); //Event notify key prefix
    private byte value;
    DataEntryPrefix(int v) {
        value = (byte)v;
    }
    public byte value() {
        return value;
    }

    public static DataEntryPrefix valueOf(byte v) {
        for (DataEntryPrefix e : DataEntryPrefix.values()) {
            if (e.value == v) {
                return e;
            }
        }
        throw new IllegalArgumentException();
    }
    public static byte[] getTransactionKey(UInt256 hash){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.DATA_TRANSACTION.value());
            bw.writeSerializable(hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getHeaderKey(UInt256 blockHash){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.DATA_HEADER.value());
            bw.writeSerializable(blockHash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getBlockHashKey(long height){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.DATA_BLOCK.value());
            bw.writeLong(height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getCurrentBlockKey(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.SYS_CURRENT_BLOCK.value());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getBlockMerkleTreeKey(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.SYS_BLOCK_MERKLE_TREE.value());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getVersionKey(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.SYS_VERSION.value());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getHeaderIndexListKey(long startHeight ){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.IX_HEADER_HASH_LIST.value());
            bw.writeLong(startHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static long getStartHeightByHeaderIndexKey(byte[] key){
        return 0;
    }

    public static byte[] getEventNotifyByTxKey(UInt256 txhash) {
        return Helper.addBytes(new byte[]{DataEntryPrefix.EVENT_NOTIFY.value()}, txhash.toArray());
    }
    public static byte[] getEventNotifyByBlockKey(int height) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeByte(DataEntryPrefix.EVENT_NOTIFY.value());
            bw.writeInt(height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getContractStateKey(Address addresshash) {
        return Helper.addBytes(new byte[]{DataEntryPrefix.ST_CONTRACT.value()}, addresshash.toArray());
    }
    public static byte[] getBookkeeperKey() {
        return Helper.addBytes(new byte[]{DataEntryPrefix.ST_BOOKKEEPER.value()}, "Bookkeeper".getBytes());
    }
    public static byte[] getMerkleTreeKey() {
        return new byte[]{DataEntryPrefix.SYS_BLOCK_MERKLE_TREE.value()};
    }
}
