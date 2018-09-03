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
package demo.vmtest.utils;

import com.github.zeepin.common.Address;
import com.github.zeepin.core.payload.InvokeCode;
import com.github.zeepin.core.transaction.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public String ContractAddress = "ContractAddress";
    public Map<String, byte[]> storageMap = new HashMap<>();
    public Transaction tx = new InvokeCode();

    public Map<String, byte[]> getStorageMap() {
        return storageMap;
    }

    public List<Address> GetSignatureAddresses() {
        if (tx.sigs == null) {
            return null;
        }
        List<Address> list = new ArrayList();
        for (int i = 0; i < tx.sigs.length; i++) {
            for (int j = 0; j < tx.sigs[i].pubKeys.length; j++) {
                if (tx.sigs[i].M == 1) {
                    Address address = Address.addressFromPubKey(tx.sigs[i].pubKeys[0]);
                    list.add(address);
                }
            }
        }
        return list;
    }
}
