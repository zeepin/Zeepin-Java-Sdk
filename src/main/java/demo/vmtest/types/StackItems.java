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
package demo.vmtest.types;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class StackItems {
    public boolean Equals(StackItems other) {
        return false;
    }

    public BigInteger GetBigInteger() {
        return new BigInteger("");
    }

    public boolean GetBoolean() {
        return false;
    }

    public byte[] GetByteArray() {
        return new byte[]{};
    }

    public InteropItem GetInterface() {
        return null;
    }

    public StackItems[] GetArray() {
        return new StackItems[0];
    }

    public StackItems[] GetStruct() {
        return new StackItems[0];
    }

    public Map<StackItems, StackItems> GetMap() {
        return new HashMap<>();
    }
}