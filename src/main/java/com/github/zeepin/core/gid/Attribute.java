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

package com.github.zeepin.core.gid;

import java.io.IOException;

import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;

public class Attribute implements Serializable {
    public byte[] key;
    public byte[] valueType;
    public byte[] value;
    public Attribute(){}
    public Attribute(byte[] key,byte[] valueType,byte[] value){
        this.key = key;
        this.valueType = valueType;
        this.value = value;
    }
    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        this.key = reader.readVarBytes();
        this.valueType = reader.readVarBytes();
        this.value = reader.readVarBytes();
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(key);
        writer.writeVarBytes(valueType);
        writer.writeVarBytes(value);
    }
}
