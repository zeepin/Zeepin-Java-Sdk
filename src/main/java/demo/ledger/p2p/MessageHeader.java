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
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

import java.util.Arrays;

public class MessageHeader {
    public int magic;
    public byte[] cmd = new byte[12];
    public int length;
    public byte[] checksum = new byte[4];
    public MessageHeader(){

    }
    public MessageHeader(int magic,byte[] cmd,int length,byte[] checksum) {
        this.magic = magic;
        System.arraycopy(cmd,0,cmd,0,cmd.length);
        this.length = length;
        this.checksum = checksum;
    }
    public void readMessageHeader(BinaryReader reader) throws Exception {
        magic = reader.readInt();
        cmd = reader.readBytes(cmd.length);
        length = reader.readInt();
        checksum = reader.readBytes(checksum.length);
    }
    public void writeMessageHeader(BinaryWriter writer) throws Exception {
        writer.writeInt(magic);
        writer.write(cmd);
        writer.writeInt(length);
        writer.write(checksum);
    }
    public String cmdType(){
        return new String(cmd);
    }

}
