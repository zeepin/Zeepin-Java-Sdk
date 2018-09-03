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
package com.github.zeepin.crypto;

import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.sdk.exception.SDKException;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.math.ec.ECCurve;

public enum Curve {
    P224(1, "P-224"),
    P256(2, "P-256"),
    P384(3, "P-384"),
    P521(4, "P-521"),
    SM2P256V1(20, "sm2p256v1"),
    ED25519(25, "ED25519");

    private int label;
    private String name;

    private Curve(int v0, String v1) {
        label = v0;
        name = v1;
    }

    public int getLabel() {
        return label;
    }
    @Override
    public String toString() {
        return name;
    }

    public static Curve valueOf(ECCurve v) throws Exception {
        for (Curve c : Curve.values()) {
            if (ECNamedCurveTable.getParameterSpec(c.toString()).getCurve().equals(v)) {
                return c;
            }
        }

        throw new Exception(ErrorCode.UnknownCurve);
    }

    public static Curve fromLabel(int v) throws Exception {
        for (Curve c : Curve.values()) {
            if (c.label == v) {
                return c;
            }
        }

        throw new SDKException(ErrorCode.UnknownCurveLabel);
    }
}
