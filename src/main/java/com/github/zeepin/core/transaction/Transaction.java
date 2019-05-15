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

package com.github.zeepin.core.transaction;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.*;

import com.alibaba.fastjson.JSON;
import com.github.zeepin.common.*;
import com.github.zeepin.core.Inventory;
import com.github.zeepin.core.InventoryType;
import com.github.zeepin.core.asset.Sig;
import com.github.zeepin.io.BinaryReader;
import com.github.zeepin.io.BinaryWriter;

/**
 *
 */
public abstract class Transaction extends Inventory {

    public byte version = 0;
    public TransactionType txType;
    public int nonce = new Random().nextInt();
    public long gasPrice = 0;
    public long gasLimit = 0;
    public Address payer = new Address();
    public byte attributes;
    public Sig[] sigs = new Sig[0];
    protected Transaction(TransactionType type) {
        this.txType = type;
    }
    
    public static Transaction deserializeTxString(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try {
    				byte ver = reader.readByte();
    				TransactionType type = TransactionType.valueOf(reader.readByte());
    	            String typeName = "com.github.zeepin.core.payload." + type.toString();
    	            Transaction transaction = (Transaction) Class.forName(typeName).newInstance();
    	            transaction.version = ver;
    	            transaction.nonce = reader.readInt();
    	            transaction.gasPrice = reader.readLong();
    	            transaction.gasLimit = reader.readLong();
    	            transaction.payer = reader.readSerializable(Address.class);
    	            transaction.deserializeExclusiveData(reader);
    	            transaction.attributes = reader.readByte();
    	            transaction.sigs = new Sig[(int) reader.readVarInt()];   	            
    	            for (int i = 0; i < transaction.sigs.length; i++) {
    	                transaction.sigs[i] = reader.readSerializable(Sig.class);
    	            }            
    	            return transaction;
    			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static boolean isNative(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try {
    				reader.readBytes(94);
    				int len = (int) reader.readByte();
    				reader.readBytes(len+38);
    				byte[] ss = reader.readBytes(28);
    				if(new String(ss).contains("ZeepinChain.Native.Invoke"))
    					return true;
    				else
    					return false;    				
    			} catch (IOException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static String getNativeFromAddr(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try { 				
    				reader.readBytes(47);
    				return reader.readSerializable(Address.class).toBase58();
    			} catch (InstantiationException | IllegalAccessException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static String getNativeToAddr(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try { 				
    				reader.readBytes(71);
    				return reader.readSerializable(Address.class).toBase58();
    			} catch (InstantiationException | IllegalAccessException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static String getNativeTransAmount(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try { 				
    				reader.readBytes(94);
    				int len = (int) reader.readByte();
    				double amount = Double.parseDouble(Helper.BigIntFromNeoBytes(reader.readBytes(len)).toString());
    				DecimalFormat format = new DecimalFormat("#.####");
    				return format.format(amount/10000);
    			} catch (IOException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static String getWasmContractAddr(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try { 				
    				reader.readBytes(44);
    				String con = Helper.toHexString(reader.readBytes(20));
    				return Helper.reverse(con);
    			} catch (IOException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static String getWasmFromAddr(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try { 				
    				reader.readBytes(111);
    				byte[] fromAddr = reader.readBytes(34);
    				return new String(fromAddr);
    			} catch (IOException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static String getWasmToAddr(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader = new BinaryReader(ms)) {
    			try { 				
    				reader.readBytes(174);
    				byte[] toAddr = reader.readBytes(34);
    				return new String(toAddr);
    			} catch (IOException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }
    
    public static String getWasmTransAmount(byte[] value) throws IOException {
    	try (ByteArrayInputStream ms1 = new ByteArrayInputStream(value, 0, value.length)) {
    		try (BinaryReader reader1 = new BinaryReader(ms1)) {
    			try { 				
    				reader1.readBytes(237);
    				int len = 0;
    				String mark = new String(reader1.readBytes(1));
    				for(int i = 0; i < 30; i++) {
    					if(mark.equals("\""))
    						break;
    					else {
    						mark = new String(reader1.readBytes(1));
    						len++;
    					}
    				}
    				try (ByteArrayInputStream ms2 = new ByteArrayInputStream(value, 0, value.length)) {
    					try (BinaryReader reader2 = new BinaryReader(ms2)) {
        					reader2.readBytes(237);
        					double amount = Double.parseDouble(new String(reader2.readBytes(len)));
            				DecimalFormat format = new DecimalFormat("#.####");
            				return format.format(amount/10000);
        				}    					
    				}    									
    			} catch (IOException ex) {
    	            throw new IOException(ex);
    	        }
    		}
    	}
    }

    public static Transaction deserializeFrom(byte[] value) throws IOException {
        return deserializeFrom(value, 0);
    }

    public static Transaction deserializeFrom(byte[] value, int offset) throws IOException {
        try (ByteArrayInputStream ms = new ByteArrayInputStream(value, offset, value.length - offset)) {
            try (BinaryReader reader = new BinaryReader(ms)) {
                return deserializeFrom(reader);
            }
        }
    }

    public static Transaction deserializeFrom(BinaryReader reader) throws IOException {
        try {
            byte ver = reader.readByte();
            TransactionType type = TransactionType.valueOf(reader.readByte());
            String typeName = "com.github.zeepin.core.payload." + type.toString();
            Transaction transaction = (Transaction) Class.forName(typeName).newInstance();
            transaction.nonce = reader.readInt();
            transaction.version = ver;
            transaction.gasPrice = reader.readLong();
            transaction.gasLimit = reader.readLong();
            transaction.payer = reader.readSerializable(Address.class);
            transaction.deserializeUnsignedWithoutType(reader);
            transaction.sigs = new Sig[(int) reader.readVarInt()];
            for (int i = 0; i < transaction.sigs.length; i++) {
                transaction.sigs[i] = reader.readSerializable(Sig.class);
            }
            return transaction;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new IOException(ex);
        }
    }
    
    //反序列化
    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        deserializeUnsigned(reader);
        try {
            sigs = reader.readSerializableArray(Sig.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    //反序列化参数
    @Override
    public void deserializeUnsigned(BinaryReader reader) throws IOException {
        txType = TransactionType.valueOf(reader.readByte());
        nonce = reader.readInt();
        version = reader.readByte();
        gasPrice = reader.readLong();
        gasLimit = reader.readLong();
        try {
            payer = reader.readSerializable(Address.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        deserializeUnsignedWithoutType(reader);
    }

    private void deserializeUnsignedWithoutType(BinaryReader reader) throws IOException {
        //try {
            deserializeExclusiveData(reader);
            attributes = 1;

    }

    protected void deserializeExclusiveData(BinaryReader reader) throws IOException {
    }

    //序列化
    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        serializeUnsigned(writer);
        writer.writeSerializableArray(sigs);
    }

    //序列化参数
    @Override
    public void serializeUnsigned(BinaryWriter writer) throws IOException {
        writer.writeByte(version);
        writer.writeByte(txType.value());
        writer.writeInt(nonce);
        writer.writeLong(gasPrice);
        writer.writeLong(gasLimit);
        writer.writeSerializable(payer);
        serializeExclusiveData(writer);
        writer.writeByte(attributes);
       // writer.writeSerializableArray(attributes);
    }

    protected void serializeExclusiveData(BinaryWriter writer) throws IOException {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Transaction)) {
            return false;
        }
        Transaction tx = (Transaction) obj;
        return hash().equals(tx.hash());
    }

    @Override
    public int hashCode() {
        return hash().hashCode();
    }


    @Override
    public Address[] getAddressU160ForVerifying() {
        return null;
    }

    @Override
    public final InventoryType inventoryType() {
        return InventoryType.TX;
    }

    public Object json() {
        Map json = new HashMap();
        json.put("Hash", hash().toString());
        json.put("Version", (int) version);
        json.put("Nonce", nonce& 0xFFFFFFFF);
        json.put("TxType", txType.value() & 0xFF);
        json.put("GasPrice",gasPrice);
        json.put("GasLimit",gasLimit);
        json.put("Payer",payer.toBase58());
       // json.put("Attributes", Arrays.stream(attributes).map(p -> p.json()).toArray(Object[]::new));
        json.put("Sigs", Arrays.stream(sigs).map(p -> p.json()).toArray(Object[]::new));
        return json;
    }

    @Override
    public boolean verify() {
        return true;
    }

}
