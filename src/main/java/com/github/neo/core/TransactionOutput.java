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



import com.github.zeepin.common.Address;
import com.github.zeepin.common.Fixed8;
import com.github.zeepin.common.UInt256;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;

import java.io.IOException;

/**
 *
 */
public class TransactionOutput implements Serializable {
    /**
     *
     */
    public UInt256 assetId;
    /**
     *
     */
    public Fixed8 value;
    /**
     *
     */
    public Address scriptHash;
    
    /**
	 * byte
	 */
	@Override
	public void serialize(BinaryWriter writer) throws IOException {
		writer.writeSerializable(assetId);
		writer.writeSerializable(value);
		writer.writeSerializable(scriptHash);
	}
	
	@Override
	public void deserialize(BinaryReader reader) throws IOException {
		try {
			assetId = reader.readSerializable(UInt256.class);
			value = reader.readSerializable(Fixed8.class);
			scriptHash = reader.readSerializable(Address.class);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IOException();
		}
	}


	@Override
	public String toString() {
		return "TransactionOutput [assetId=" + assetId + ", value=" + value
				+ ", scriptHash=" + scriptHash + "]";
	}
}
