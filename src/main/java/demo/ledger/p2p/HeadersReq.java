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

import com.github.zeepin.common.Helper;
import com.github.zeepin.common.UInt256;

/**
 *
 *
 */
public class HeadersReq {
    public byte len;
    public byte[] hashStart = new byte[32];
    public byte[] hashEnd =  new byte[32];
    public HeadersReq(){

    }
    public void deserialization(byte[] data){
        len = data[0];
        System.arraycopy(data,1,hashStart,0,hashStart.length);
        System.arraycopy(data,33,hashEnd,0,hashEnd.length);
        hashStart = Helper.reverse(hashStart);
        hashEnd = Helper.reverse(hashEnd);
    }
    public byte[] serialization(){
        byte[] data = new byte[65];
        data[0] = len;
        System.arraycopy(hashStart,0,data,1,hashStart.length);
        System.arraycopy(hashEnd,0,data,33,hashEnd.length);
        return data;
    }
    public byte[] msgSerialization(){
        Message msg = new Message(serialization());
        msg.header = new MessageHeader(Message.NETWORK_MAGIC_MAINNET,"getheaders".getBytes(),msg.message.length,Message.checkSum(msg.message));
        return msg.serialization();
    }
}
