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
package demo.ledger.p2p;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

/**
 *
 *
 */
public class PingReq {
    public long height;
    public PingReq(){

    }
    public PingReq(long height){
        this.height = height;
    }
    public byte[] serialization(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            bw.writeLong(height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
    public byte[] msgSerialization(){
        Message msg = new Message(serialization());
        msg.header = new MessageHeader(Message.NETWORK_MAGIC_MAINNET,"ping".getBytes(),msg.message.length,Message.checkSum(msg.message));
        return msg.serialization();
    }
    public void deserialization(byte[] data){
        ByteArrayInputStream ms = new ByteArrayInputStream(data);
        BinaryReader reader = new BinaryReader(ms);
        try {
            height = reader.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
