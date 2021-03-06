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

import io.github.novacrypto.toruntime.CheckedExceptionToRuntime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static io.github.novacrypto.toruntime.CheckedExceptionToRuntime.toRuntime;

final class HmacSha512 {
    private static final String HMAC_SHA512 = "HmacSHA512";

    static byte[] hmacSha512(final byte[] byteKey, final byte[] seed) {
        return initialize(byteKey)
                .doFinal(seed);
    }

    private static Mac initialize(final byte[] byteKey) {
        final Mac hmacSha512 = getInstance(HMAC_SHA512);
        final SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
        toRuntime(new CheckedExceptionToRuntime.Action() {
            @Override
            public void run() throws Exception {
                hmacSha512.init(keySpec);
            }
        });
        return hmacSha512;
    }

    private static Mac getInstance(final String HMAC_SHA256) {
        return toRuntime(new CheckedExceptionToRuntime.Func<Mac>() {
            @Override
            public Mac run() throws Exception {
                return Mac.getInstance(HMAC_SHA256);
            }
        });
    }
}