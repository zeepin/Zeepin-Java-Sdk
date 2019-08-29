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

public enum SignatureScheme {
    SHA224WITHECDSA("SHA224withECDSA"),
    SHA256WITHECDSA("SHA256withECDSA"),
    SHA384WITHECDSA("SHA384withECDSA"),
    SHA512WITHECDSA("SHA512withECDSA"),
    SHA3_224WITHECDSA("SHA3-224withECDSA"),
    SHA3_256WITHECDSA("SHA3-256withECDSA"),
    SHA3_384WITHECDSA("SHA3-384withECDSA"),
    SHA3_512WITHECDSA("SHA3-512withECDSA"),
    RIPEMD160WITHECDSA("RIPEMD160withECDSA"),

    SM3WITHSM2("SM3withSM2");

    private String name;

    private SignatureScheme(String v) {
        name = v;
    }
    @Override
    public String toString() {
        return name;
    }

    public static SignatureScheme fromScheme(String name) throws Exception {
        for (SignatureScheme k : SignatureScheme.values()) {
            if (k.name().equals(name.toUpperCase())) {
                return k;
            }
        }
        throw new Exception(ErrorCode.UnknownAsymmetricKeyType);
    }
}
