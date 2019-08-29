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
package demo.ledger.p2p;

import com.alibaba.fastjson.JSON;
import com.github.zeepin.common.Helper;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

import demo.ledger.common.BlockHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 *
 *
 */
public class VersionReq {
    public int version = 0;
    public long services = 2;
    public long timeStamp = System.currentTimeMillis();
    public short syncPort = 20338;
    public short httpInfoPort = 0;
    public short consPort = 0;
    public byte[] cap = new byte[32];
    public long nonce = new Random().nextInt();
    public long startHeight = 10;
    public byte relay = 1;
    public boolean isConsensus = false;
    public byte[] serialization(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeInt(version);
            bw.writeLong(services);
            bw.writeLong(timeStamp);
            bw.writeShort(syncPort);
            bw.writeShort(httpInfoPort);
            bw.writeShort(consPort);
            bw.write(cap);
            bw.writeLong(nonce);
            bw.writeLong(startHeight);
            bw.writeByte(relay);
            bw.writeBoolean(isConsensus);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
    public byte[] msgSerialization(){
        Message msg = new Message(serialization());
        msg.header = new MessageHeader(Message.NETWORK_MAGIC_MAINNET,"version".getBytes(),msg.message.length,Message.checkSum(msg.message));
        return msg.serialization();
    }
    public void deserialization(byte[] data){
        ByteArrayInputStream ms = new ByteArrayInputStream(data);
        BinaryReader reader = new BinaryReader(ms);
        try {
            version = reader.readInt();
            services = reader.readLong();
            timeStamp = reader.readLong();
            syncPort = reader.readShort();
            httpInfoPort = reader.readShort();
            consPort = reader.readShort();
            cap = reader.readBytes(32);
            nonce = reader.readLong();
            startHeight = reader.readLong();
            relay = reader.readByte();
            isConsensus = reader.readBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
