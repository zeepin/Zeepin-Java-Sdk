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


import com.alibaba.fastjson.JSON;
import com.github.zeepin.crypto.SignatureScheme;
import com.github.zeepin.sdk.wallet.*;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class WalletQR {
    public static Map exportIdentityQRCode(Wallet walletFile, Identity identity) throws Exception {
        Control control = identity.controls.get(0);
        String address = identity.gid.substring(8);
        Map map = new HashMap();
        map.put("type", "I");
        map.put("label",identity.label);
        map.put("key", control.key);
        map.put("parameters", control.parameters);
        map.put("algorithm", "ECDSA");
        map.put("scrypt", walletFile.getScrypt());
        map.put("address",address);
        map.put("salt", control.salt);
        return map;
    }
    public static Map exportIdentityQRCode(Scrypt scrypt,Identity identity) throws Exception {
        Control control = identity.controls.get(0);
        String address = identity.gid.substring(8);
        Map map = new HashMap();
        map.put("type", "I");
        map.put("label",identity.label);
        map.put("key", control.key);
        map.put("parameters", control.parameters);
        map.put("algorithm", "ECDSA");
        map.put("scrypt", scrypt);
        map.put("address",address);
        map.put("salt", control.salt);
        return map;
    }
    public static Map exportAccountQRCode(Wallet walletFile,Account account) throws Exception {
        Map map = new HashMap();
        map.put("type", "A");
        map.put("label", account.label);
        map.put("key", account.key);
        map.put("parameters", account.parameters);
        map.put("algorithm", "ECDSA");
        map.put("scrypt", walletFile.getScrypt());
        map.put("address",account.address);
        map.put("salt", account.salt);
        return map;
    }
    public static Map exportAccountQRCode(Scrypt scrypt, Account account) throws Exception {
        Map map = new HashMap();
        map.put("type", "A");
        map.put("label", account.label);
        map.put("key", account.key);
        map.put("parameters", account.parameters);
        map.put("algorithm", "ECDSA");
        map.put("scrypt", scrypt);
        map.put("address",account.address);
        map.put("salt", account.salt);
        return map;
    }
    public static String getPriKeyFromQrCode(String qrcode,String password){
        Map map = JSON.parseObject(qrcode,Map.class);
        String key = (String)map.get("key");
        String address = (String)map.get("address");
        String salt = (String)map.get("salt");
        int n = (int)((Map)map.get("scrypt")).get("n");
        try {
            return com.github.zeepin.account.Account.getGcmDecodedPrivateKey(key,password,address, Base64.getDecoder().decode(salt),n, SignatureScheme.SHA256WITHECDSA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
