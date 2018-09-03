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

import io.github.novacrypto.bip32.Network;

public interface ExtendedKey {

    /**
     * The network of this extended key
     *
     * @return The network of this extended key
     */
    Network network();

    /**
     * 1 byte: 0 for master nodes, 1 for level-1 derived keys, etc.
     *
     * @return the depth of this key node
     */
    int depth();

    /**
     * 4 bytes: child number. e.g. 3 for m/3, hard(7) for m/7'
     * 0 if master key
     *
     * @return the child number
     */
    int childNumber();

    /**
     * Serialized Base58 String of this extended key
     *
     * @return the Base58 String representing this key
     */
    String extendedBase58();

    /**
     * Serialized data of this extended key
     *
     * @return the byte array representing this key
     */
    byte[] extendedKeyByteArray();

    /**
     * Coerce this key on to another network.
     *
     * @param otherNetwork Network to put key on.
     * @return A new extended key, or this instance if key already on the other Network.
     */
    ExtendedKey toNetwork(final Network otherNetwork);
}