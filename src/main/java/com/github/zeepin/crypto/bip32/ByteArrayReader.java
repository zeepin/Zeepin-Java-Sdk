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

package com.github.zeepin.crypto.bip32;

import java.util.Arrays;

final class ByteArrayReader {

    private final byte[] bytes;
    private int idx = 0;

    ByteArrayReader(final byte[] source) {
        this.bytes = source;
    }

    byte[] readRange(final int length) {
        final byte[] range = Arrays.copyOfRange(this.bytes, idx, idx + length);
        idx += length;
        return range;
    }

    /**
     * deserialize a 32-bit unsigned integer i as a 4-byte sequence, most significant byte first.
     */
    int readSer32() {
        int result = read();
        result <<= 8;
        result |= read();
        result <<= 8;
        result |= read();
        result <<= 8;
        result |= read();
        return result;
    }

    int read() {
        return 0xff & bytes[idx++];
    }
}