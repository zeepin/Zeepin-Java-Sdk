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
package com.github.neo.core;


import com.github.zeepin.common.UInt256;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;

import java.io.IOException;

/**
 *
 */
public class TransactionInput implements Serializable {
    /**
     *
     */
    public UInt256 prevHash;
    /**
     *
     */
    public short prevIndex;

    public TransactionInput() {
    }

    public TransactionInput(UInt256 prevHash, int prevIndex) {
        this.prevHash = prevHash;
        this.prevIndex = (short) prevIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
        	return true;
        }
        if (null == obj) {
        	return false;
        }
        if (!(obj instanceof TransactionInput)) {
        	return false;
        }
        TransactionInput other = (TransactionInput) obj;
        return prevHash.equals(other.prevHash) && prevIndex == other.prevIndex;
    }

    @Override
    public int hashCode() {
        return prevHash.hashCode() + prevIndex;
    }

    /**
	 *
	 */
    @Override
	public void deserialize(BinaryReader reader) throws IOException {
		try {
			prevHash = reader.readSerializable(UInt256.class);
			prevIndex = reader.readShort();
//			prevIndex = (short) reader.readVarInt();
		} catch (InstantiationException | IllegalAccessException e) {
		}
	}
	@Override
	public void serialize(BinaryWriter writer) throws IOException {
		writer.writeSerializable(prevHash);
		writer.writeShort(prevIndex);
//		writer.writeVarInt(prevIndex);
	}


	@Override
	public String toString() {
		return "TransactionInput [prevHash=" + prevHash + ", prevIndex="
				+ prevIndex + "]";
	}
}
