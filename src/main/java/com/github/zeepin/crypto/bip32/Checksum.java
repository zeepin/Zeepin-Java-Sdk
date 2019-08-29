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


import static io.github.novacrypto.hashing.Sha256.sha256Twice;

final class Checksum {

    static void confirmExtendedKeyChecksum(final byte[] extendedKeyData) {
        final byte[] expected = checksum(extendedKeyData);
        for (int i = 0; i < 4; i++) {
            if (extendedKeyData[78 + i] != expected[i])
                throw new BadKeySerializationException("Checksum error");
        }
    }

    static byte[] checksum(final byte[] privateKey) {
        return sha256Twice(privateKey, 0, 78);
    }
}