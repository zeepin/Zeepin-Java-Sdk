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

import io.github.novacrypto.bip32.Networks;
import io.github.novacrypto.bip32.networks.DefaultNetworks;

import java.util.Arrays;

import static com.github.zeepin.crypto.bip32.Checksum.confirmExtendedKeyChecksum;
import static io.github.novacrypto.base58.Base58.base58Decode;

final class ExtendedPrivateKeyDeserializer implements Deserializer<ExtendedPrivateKey> {

    static final ExtendedPrivateKeyDeserializer DEFAULT = new ExtendedPrivateKeyDeserializer(DefaultNetworks.INSTANCE);

    private final Networks networks;

    ExtendedPrivateKeyDeserializer(final Networks networks) {
        this.networks = networks;
    }

    @Override
    public ExtendedPrivateKey deserialize(final CharSequence extendedBase58Key) {
        final byte[] extendedKeyData = base58Decode(extendedBase58Key);
        try {
            return deserialize(extendedKeyData);
        } finally {
            Arrays.fill(extendedKeyData, (byte) 0);
        }
    }

    @Override
    public ExtendedPrivateKey deserialize(final byte[] extendedKeyData) {
        confirmExtendedKeyChecksum(extendedKeyData);
        final ByteArrayReader reader = new ByteArrayReader(extendedKeyData);
        return new ExtendedPrivateKey(new HdKey
                .Builder()
                .network(networks.findByPrivateVersion(reader.readSer32()))
                .depth(reader.read())
                .parentFingerprint(reader.readSer32())
                .childNumber(reader.readSer32())
                .chainCode(reader.readRange(32))
                .key(getKey(reader))
                .neutered(false)
                .build()
        );
    }

    private byte[] getKey(final ByteArrayReader reader) {
        if (reader.read() != 0) {
            throw new BadKeySerializationException("Expected 0 padding at position 45");
        }
        return reader.readRange(32);
    }
}