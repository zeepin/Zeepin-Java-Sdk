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

import com.github.zeepin.block.Block;
import com.github.zeepin.common.Helper;
import com.github.zeepin.crypto.Digest;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 *
 */
public class Message {
    public static int NETWORK_MAGIC_MAINNET = 0x8c77ab60;
    public static int NETWORK_MAGIC_POLARIS = 0x2d8829df;
    public MessageHeader header ;
    public byte[] message ;
    public Message(){

    }
    public Message(byte[] msg){
        message = msg;
    }

    public void deserialization(byte[] data){
        ByteArrayInputStream ms = new ByteArrayInputStream(data);
        BinaryReader reader = new BinaryReader(ms);
        try {
            header = new MessageHeader();
            header.readMessageHeader(reader);
            int len = reader.available();
            message = new byte[len];
            System.arraycopy(data,data.length-len,message,0,len);
            System.out.println(header.cmdType());
            if(header.cmdType().contains("block")){
                Block block = Serializable.from(message, Block.class);
                System.out.println(block.json());
            }else if(header.cmdType().contains("getheaders")){
                HeadersReq headersReq = new HeadersReq();
                headersReq.deserialization(message);
            } else if(header.cmdType().contains("headers")){
                BlkHeader header = new BlkHeader();
                header.deserialization(message);
            } else if(header.cmdType().contains("version")){
                VersionReq version = new VersionReq();
                version.deserialization(message);
            } else if(header.cmdType().contains("getdata")){
                DataReq dataReq = new DataReq();
                dataReq.deserialization(message);
            } else if(header.cmdType().contains("ping")){
                PingReq pingReq = new PingReq();
                pingReq.deserialization(message);
            } else if(header.cmdType().contains("pong")){
                PongRsp pongRsp = new PongRsp();
                pongRsp.deserialization(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public byte[] serialization(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryWriter bw = new BinaryWriter(baos);
        try {
            header.writeMessageHeader(bw);
            bw.write(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] checkSum(byte[] data){
        byte[] hash = Digest.hash256(data);
        byte[] checksum = new byte[4];
        System.arraycopy(hash,0,checksum,0,checksum.length);
        return checksum;
    }
}
