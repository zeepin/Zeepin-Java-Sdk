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

public interface Deserializer<T> {

    /**
     * Deserializes the data into a {@link T}.
     *
     * @param extendedBase58Key Base58 CharSequence containing the serialized extended key.
     * @return The {@link T}
     */
    T deserialize(CharSequence extendedBase58Key);

    /**
     * Deserializes the data into a {@link T}.
     *
     * @param extendedKeyData Byte array containing the serialized extended key.
     * @return The {@link T}
     */
    T deserialize(byte[] extendedKeyData);
}