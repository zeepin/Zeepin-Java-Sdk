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

import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

final class Secp256r1SC {

    static final X9ECParameters CURVE = CustomNamedCurves.getByName("secp256r1");

    static BigInteger n() {
        return CURVE.getN();
    }

    static byte[] pointSerP(final ECPoint point) {
        return point.getEncoded(true);
    }

    static byte[] pointSerP_gMultiply(final BigInteger p) {
        return pointSerP(gMultiply(p));
    }

    static ECPoint gMultiplyAndAddPoint(final BigInteger p, final byte[] toAdd) {
        return gMultiply(p).add(decode(toAdd));
    }

    private static ECPoint decode(final byte[] toAdd) {
        return CURVE.getCurve().decodePoint(toAdd);
    }

    private static ECPoint gMultiply(BigInteger p) {
        return CURVE.getG()
                .multiply(p);
    }
}