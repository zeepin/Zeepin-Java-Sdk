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

package com.github.zeepin.core.payload;

import java.io.IOException;

import com.github.zeepin.common.Address;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.core.transaction.TransactionType;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

public class DeployCode extends Transaction {
    public byte[] code;
    public boolean needStorage;
    public String name;
    public String version;
    public String author;
    public String email;
    public String description;


    public DeployCode() {
        super(TransactionType.DeployCode);
    }

    @Override
    public void deserializeExclusiveData(BinaryReader reader) throws IOException {
        try {
            code = reader.readVarBytes();
            needStorage = reader.readBoolean();
            name = reader.readVarString();
            version = reader.readVarString();
            author = reader.readVarString();
            email = reader.readVarString();
            description = reader.readVarString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void serializeExclusiveData(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(code);
        writer.writeBoolean(needStorage);
        writer.writeVarString(name);
        writer.writeVarString(version);
        writer.writeVarString(author);
        writer.writeVarString(email);
        writer.writeVarString(description);
    }

    @Override
    public Address[] getAddressU160ForVerifying() {
        return null;
    }
}
