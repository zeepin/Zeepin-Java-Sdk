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
package com.github.zeepin.common;

import java.math.BigInteger;
import java.util.Arrays;

import com.github.zeepin.crypto.Base58;

public class ConvertAddress {
    private static final int ADDR_LEN = 20;
    private static final int ADDR_PREFIX = 23;

    public static String convertAddress(String aAddress) throws Exception {
        if (aAddress == null){
            return null;
        }
        byte[] address = Base58.decode(aAddress);

        BigInteger bigInteger = new BigInteger(address).setBit(10);
        if (bigInteger == null){
            return null;
        }

        byte[] b = bigInteger.toByteArray();
        if (b.length != (ADDR_LEN+5) || b[0] != ADDR_PREFIX){
            return null;
        }

        Address add = Address.parse(Helper.toHexString(Arrays.copyOfRange(b,1,21)));
        return add.toBase58();
    }
}

