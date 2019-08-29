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

import com.github.zeepin.common.Helper;

public class BoolItem extends StackItems {
    public boolean value;

    public BoolItem(boolean val) {
        value = val;
    }

    @Override
    public boolean GetBoolean() {
        return value;
    }

    @Override
    public BigInteger GetBigInteger() {
        if (value) {
            return BigInteger.valueOf(1);
        } else {
            return BigInteger.valueOf(0);
        }
    }

    @Override
    public byte[] GetByteArray() {
        return value == true ? new byte[]{1} : new byte[]{0};
    }
}
