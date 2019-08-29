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
package com.github.zeepin.crypto;

import com.github.zeepin.common.ErrorCode;

public enum KeyType {
    ECDSA(0x12),
    SM2(0x13),
    EDDSA(0x14);


    private int label;

    private KeyType(int b) {
        this.label = b;
    }


    // get the crypto.KeyType according to the input label
    public static KeyType fromLabel(byte label) throws Exception {
        for (KeyType k : KeyType.values()) {
            if (k.label == label) {
                return k;
            }
        }
        throw new Exception(ErrorCode.UnknownAsymmetricKeyType);
    }
    public static KeyType fromPubkey(byte[] pubkey)  {
        try {
            if(pubkey.length == 33){
                return KeyType.ECDSA;
            }else {
                return KeyType.fromLabel(pubkey[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getLabel() {
        return label;
    }
}
