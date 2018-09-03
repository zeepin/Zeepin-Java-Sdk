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
package com.github.neo.core.transaction;



import com.github.zeepin.common.Address;
import com.github.zeepin.common.Fixed8;
import com.github.zeepin.core.transaction.TransactionType;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

import java.io.IOException;

public class InvocationTransaction extends TransactionNeo {
	public byte[] script;
	public Fixed8 gas;

	public InvocationTransaction() {
		super(TransactionType.InvokeCode);
	}

	@Override
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException {
		try {
			script = reader.readVarBytes();
			gas = reader.readSerializable(Fixed8.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException {
		writer.writeVarBytes(script);
		writer.writeSerializable(gas);
	}
	@Override
	public Address[] getAddressU160ForVerifying() {
		return null;
	}
}
