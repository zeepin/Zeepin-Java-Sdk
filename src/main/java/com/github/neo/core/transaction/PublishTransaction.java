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



import com.github.neo.core.ContractParameterType;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.transaction.TransactionType;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PublishTransaction extends TransactionNeo {
	public byte[] script;
	public ContractParameterType[] parameterList;
	public ContractParameterType returnType;
	public boolean needStorage;
	public String name;
	public String codeVersion;
	public String author;
	public String email;
	public String description;
	
	public PublishTransaction() {
		super(TransactionType.DeployCode);
	}
	@Override
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException {
		script = reader.readVarBytes();
		byte[] param = reader.readVarBytes();
		parameterList = toEnum(param);
		returnType = toEnum(reader.readByte());
		needStorage = reader.readBoolean();
		name = new String(reader.readVarBytes(252));
		codeVersion = new String(reader.readVarBytes(252));
		author = new String(reader.readVarBytes(252));
		email = new String(reader.readVarBytes(252));
		description = new String(reader.readVarBytes(65535));
	}
	@Override
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException {
		writer.writeVarBytes(script);
		writer.writeVarBytes(toByte(parameterList));
		writer.writeByte((byte)returnType.ordinal());
		writer.writeBoolean(needStorage);
		writer.writeVarString(name);
		writer.writeVarString(codeVersion);
		writer.writeVarString(author);
		writer.writeVarString(email);
		writer.writeVarString(description);
	}
	@Override
	public Address[] getAddressU160ForVerifying() {
		return null;
	}
	private ContractParameterType toEnum(byte bt) {
		return Arrays.stream(ContractParameterType.values()).filter(p -> p.ordinal() == bt).findAny().get();
	}
	private ContractParameterType[] toEnum(byte[] bt) {
		if(bt == null) {
			return null;
		}
		List<ContractParameterType> list = new ArrayList<ContractParameterType>();
		for(byte b: bt) {
			ContractParameterType type = toEnum(b);
			list.add(type);
		}
		return list.stream().toArray(ContractParameterType[]::new);
	}
	private byte[] toByte(ContractParameterType[] types) {
		if(types == null) {
			return new byte[0];
		}
		int len = types.length;
		byte[] bt = new byte[len];
		for(int i=0; i<len; ++i) {
			bt[i] = (byte) types[i].ordinal();
		}
		return bt;
	}
}
