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
import com.github.zeepin.sdk.exception.SDKException;

import org.bouncycastle.jcajce.spec.SM2ParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class Signature {
    private SignatureScheme scheme;
    private AlgorithmParameterSpec param;
    private byte[] value;

    public Signature(SignatureScheme scheme, AlgorithmParameterSpec param, byte[] signature) {
        this.scheme = scheme;
        this.param = param;
        this.value = signature;
    }

    // parse a serialized bytes to signature structure
    public Signature(byte[] data) throws Exception {
        if (data == null) {
            throw new SDKException(ErrorCode.ParamError);
        }

        if (data.length < 2) {
            throw new Exception(ErrorCode.InvalidSignatureDataLen);
        }

        this.scheme = SignatureScheme.values()[data[0]];
        if (scheme == SignatureScheme.SM3WITHSM2) {
            int i = 0;
            while (i < data.length && data[i] != 0){
                i++;
            }
            if (i >= data.length) {
                throw new Exception(ErrorCode.InvalidSignatureData);
            }
            this.param = new SM2ParameterSpec(Arrays.copyOfRange(data, 1, i));
            this.value = Arrays.copyOfRange(data, i + 1, data.length);
        } else {
            this.value = Arrays.copyOfRange(data, 1, data.length);
        }
    }

    // serialize to byte array
    public byte[] toBytes() {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            bs.write((byte)scheme.ordinal());
            if (scheme == SignatureScheme.SM3WITHSM2) {
                // adding the ID
                bs.write(((SM2ParameterSpec)param).getID());
                // padding a 0 as the terminator
                bs.write((byte)0);
            }
            bs.write(value);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return bs.toByteArray();
    }

    public SignatureScheme getScheme() { return scheme; }

    public byte[] getValue() {
        return this.value;
    }
}
