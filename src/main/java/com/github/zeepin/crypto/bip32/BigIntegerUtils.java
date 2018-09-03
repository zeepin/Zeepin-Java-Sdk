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

package com.github.zeepin.crypto.bip32;

import java.math.BigInteger;
import java.util.Arrays;

final class BigIntegerUtils {

    static BigInteger parse256(final byte[] bytes) {
        return new BigInteger(1, bytes);
    }

    static void ser256(final byte[] target, final BigInteger integer) {
        if (integer.bitLength() > target.length * 8)
            throw new RuntimeException("ser256 failed, cannot fit integer in buffer");
        final byte[] modArr = integer.toByteArray();
        Arrays.fill(target, (byte) 0);
        copyTail(modArr, target);
        Arrays.fill(modArr, (byte) 0);
    }

    private static void copyTail(final byte[] src, final byte[] dest) {
        if (src.length < dest.length) {
            System.arraycopy(src, 0, dest, dest.length - src.length, src.length);
        } else {
            System.arraycopy(src, src.length - dest.length, dest, 0, dest.length);
        }
    }
}