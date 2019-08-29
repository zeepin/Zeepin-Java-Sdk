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

import com.github.zeepin.common.Helper;
import com.github.zeepin.io.BinaryReader;

import demo.ledger.common.BlockHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 *
 */
public class BlkHeader {
    public BlockHeader[] headers;
    public BlkHeader(){

    }
    public void deserialization(byte[] data){
        ByteArrayInputStream ms = new ByteArrayInputStream(data);
        BinaryReader reader = new BinaryReader(ms);
        try {
            int count = reader.readInt();
            headers = new BlockHeader[count];
            for(int i=0;i<count;i++){
                headers[i] = reader.readSerializable(BlockHeader.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
