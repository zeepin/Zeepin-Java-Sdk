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

package com.github.zeepin.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.github.zeepin.account.Account;
import com.github.zeepin.common.Address;
import com.github.zeepin.crypto.Digest;
import com.github.zeepin.crypto.SignatureScheme;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;
import com.github.zeepin.io.Serializable;


public interface Signable extends Serializable {

    void deserializeUnsigned(BinaryReader reader) throws IOException;

    void serializeUnsigned(BinaryWriter writer) throws IOException;

    Address[] getAddressU160ForVerifying();
    
    default byte[] getHashData() {
    	try (ByteArrayOutputStream ms = new ByteArrayOutputStream()) {
	    	try (BinaryWriter writer = new BinaryWriter(ms)) {
	            serializeUnsigned(writer);
	            writer.flush();
	            return ms.toByteArray();
	        }
    	} catch (IOException ex) {
    		throw new UnsupportedOperationException(ex);
    	}
    }
	default byte[] sign(Account account, SignatureScheme scheme) throws Exception {
		return account.generateSignature(Digest.hash256(getHashData()), scheme,null);
	}
	default boolean verifySignature(Account account, byte[] data, byte[] signature) throws Exception {
		return account.verifySignature(Digest.hash256(Digest.sha256(data)), signature);
	}

}
