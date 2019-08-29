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
package demo.vmtest.utils;

import com.github.zeepin.common.Helper;
import com.github.zeepin.io.BinaryReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class VmReader {
    ByteArrayInputStream ms;
    public BinaryReader reader;
    public byte[] code;

    public VmReader(byte[] bys) {
        ms = new ByteArrayInputStream(bys);
        reader = new BinaryReader(ms);
        code = bys;
    }

    public BinaryReader Reader() {
        return reader;
    }

    public byte ReadByte() {
        try {
            return reader.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean ReadBool() {
        try {
            byte b = reader.readByte();
            return b == 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] ReadBytes(int count) {
        try {
            return reader.readBytes(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] readVarBytes() {
        try {
            return reader.readVarBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int ReadUint16() throws Exception {
        return reader.readInt();
    }

    public int ReadUInt32() throws Exception {
        return reader.readInt();
    }

    public long ReadUInt64() throws Exception {
        return reader.readLong();
    }

    public int Position() {
        try {
            return code.length - reader.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int Length() {
        return code.length;
    }

    public int readVarInt() {
        try {
            return (int) reader.readVarInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int readInt16() {
        try {
            return Integer.parseInt(Helper.reverse(Helper.toHexString(reader.readBytes(2))), 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long readVarInt(long max) {
        try {
            return reader.readVarInt(max);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long Seek(long offset) {
        try {
            return reader.Seek(offset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
