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
package demo.neo;


import com.alibaba.fastjson.JSON;
import com.github.neo.core.NeoRpc;
import com.github.neo.core.Program;
import com.github.neo.core.transaction.TransactionNeo;
import com.github.neo.core.SmartContract;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.common.Helper;
import com.github.zeepin.crypto.SignatureScheme;
import com.github.zeepin.network.exception.RpcException;
import com.github.zeepin.smartcontract.neovm.abi.AbiFunction;
import com.github.zeepin.smartcontract.neovm.abi.AbiInfo;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * neo sdk Demo
 * 
 *
 * 
 */
public class Nep5TransferDemo {
	public static String privatekey1 = "1094e90dd7c4fdfd849c14798d725ac351ae0d924b29a279a9ffa77d5737bd96";
	public static String privatekey2 = "bc254cf8d3910bc615ba6bf09d4553846533ce4403bc24f58660ae150a6d64cf";
	public static String contractAddr = "5bb169f915c916a5e30a3c13a5e0cd228ea26826";
	public static String nodeUrl = "http://seed2.neo.org:20332";
	public static String nep5abi = "{\"hash\":\"0x5bb169f915c916a5e30a3c13a5e0cd228ea26826\",\"entrypoint\":\"Main\",\"functions\":[{\"name\":\"Name\",\"parameters\":[],\"returntype\":\"String\"},{\"name\":\"Symbol\",\"parameters\":[],\"returntype\":\"String\"},{\"name\":\"Decimals\",\"parameters\":[],\"returntype\":\"Integer\"},{\"name\":\"Main\",\"parameters\":[{\"name\":\"operation\",\"type\":\"String\"},{\"name\":\"args\",\"type\":\"Array\"}],\"returntype\":\"Any\"},{\"name\":\"Init\",\"parameters\":[],\"returntype\":\"Boolean\"},{\"name\":\"TotalSupply\",\"parameters\":[],\"returntype\":\"Integer\"},{\"name\":\"Transfer\",\"parameters\":[{\"name\":\"from\",\"type\":\"ByteArray\"},{\"name\":\"to\",\"type\":\"ByteArray\"},{\"name\":\"value\",\"type\":\"Integer\"}],\"returntype\":\"Boolean\"},{\"name\":\"BalanceOf\",\"parameters\":[{\"name\":\"address\",\"type\":\"ByteArray\"}],\"returntype\":\"Integer\"}],\"events\":[{\"name\":\"transfer\",\"parameters\":[{\"name\":\"arg1\",\"type\":\"ByteArray\"},{\"name\":\"arg2\",\"type\":\"ByteArray\"},{\"name\":\"arg3\",\"type\":\"Integer\"}],\"returntype\":\"Void\"}]}";
	public static void main(String[] args) throws Exception {
        System.out.println("Hi NEO, Nep-5 smartcontract invoke test!");
		com.github.zeepin.account.Account acct1 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey1), SignatureScheme.SHA256WITHECDSA);
		com.github.zeepin.account.Account acct2 = new com.github.zeepin.account.Account(Helper.hexToBytes(privatekey2), SignatureScheme.SHA256WITHECDSA);
		Address multiSignAddr = Address.addressFromMultiPubKeys(2,acct1.serializePublicKey(),acct2.serializePublicKey());

		//read smarcontract abi file
//		InputStream is2 = new FileInputStream("nep-5.abi.json");
//		byte[] bys2 = new byte[is2.available()];
//		is2.read(bys2);
//		is2.close();
//		String nep5abi = new String(bys2);

		AbiInfo abiinfo = JSON.parseObject(nep5abi, AbiInfo.class);
		System.out.println("Entrypoint:" + abiinfo.getEntrypoint());
		System.out.println("contractAddress:"+abiinfo.getHash());
		System.out.println("Functions:" + abiinfo.getFunctions());

		System.out.println("acct1 address:" + acct1.getAddressU160().toBase58()+" "+Helper.toHexString(acct1.getAddressU160().toArray()));
		System.out.println("acct2 address:" + acct2.getAddressU160().toBase58()+" "+Helper.toHexString(acct2.getAddressU160().toArray()));
		System.out.println("multi address:" + multiSignAddr.toBase58()+" "+Helper.toHexString(multiSignAddr.toArray()));
		if(false) {
			Address zeroAddr = Address.parse("0000000000000000000000000000000000000000");//AFmseVrdL9f9oyCzZefL9tG6UbvhPbdYzM
			System.out.println(zeroAddr.toBase58());
			System.out.println(Address.decodeBase58("AFmseVrdL9f9oyCzZefL9tG6UbvhPbdYzM"));
			System.exit(0);
		}
		if(true) {
			String balance = (String)  NeoRpc.getBalance(nodeUrl, contractAddr, Helper.toHexString(acct1.getAddressU160().toArray()));
			System.out.println("acct1: " + balance);
			System.out.println(new BigInteger(Helper.reverse(Helper.hexToBytes(balance))).longValue());
//			balance = (String) getBalance(nodeUrl, contractAddr, Helper.toHexString(acct2.getAddressU160().toArray()));
//			System.out.println("acct2: " + balance);
//			balance = (String) getBalance(nodeUrl, contractAddr, Helper.toHexString(multiSignAddr.toArray()));
//			System.out.println("multiSignAddr: " + balance);
		}
		if(false) {
			Address recv = multiSignAddr;//acct2.getAddressU160()
			AbiFunction func = abiinfo.getFunction("Transfer");
			func.name = func.name.toLowerCase();
			func.setParamsValue(acct1.getAddressU160().toArray(), recv.toArray(), Long.valueOf(19*10000000));

			//make transaction
			TransactionNeo tx = SmartContract.makeInvocationTransaction(Helper.reverse(contractAddr), acct1.getAddressU160().toArray(), func);
			tx.scripts = new Program[1];
			tx.scripts[0] = new Program();
			tx.scripts[0].parameter = Program.ProgramFromParams(new byte[][]{tx.sign(acct1, SignatureScheme.SHA256WITHECDSA)});
			tx.scripts[0].code =  Program.ProgramFromPubKey(acct1.serializePublicKey());

			System.out.println(tx.toHexString());
			System.out.println(tx.hash().toString());
			System.out.println(Helper.toHexString(Program.ProgramFromPubKey(acct1.serializePublicKey())));
			//send tx to neo node
			Object obj =  NeoRpc.sendRawTransaction(nodeUrl,tx.toHexString());
			System.out.println(obj);
		}

		if(false) { //multiSignAddr
			AbiFunction func = abiinfo.getFunction("Transfer");//BalanceOf
			func.name = func.name.toLowerCase();
			func.setParamsValue(multiSignAddr.toArray(), acct2.getAddressU160().toArray(), Long.valueOf(1));

			//make transaction
			TransactionNeo tx = SmartContract.makeInvocationTransaction(Helper.reverse(contractAddr), multiSignAddr.toArray(), func);
			tx.scripts = new Program[1];
			tx.scripts[0] = new Program();
            tx.scripts[0].parameter = Program.ProgramFromParams(new byte[][]{tx.sign(acct1, SignatureScheme.SHA256WITHECDSA),tx.sign(acct2, SignatureScheme.SHA256WITHECDSA)});
			tx.scripts[0].code =  Program.ProgramFromMultiPubKey(2,acct1.serializePublicKey(),acct2.serializePublicKey());

			System.out.println(tx.toHexString());
			System.out.println(tx.hash().toString());
			System.out.println(Helper.toHexString(Program.ProgramFromPubKey(acct1.serializePublicKey())));
			//send tx to neo node
			Object obj = NeoRpc.sendRawTransaction(nodeUrl,tx.toHexString());
			System.out.println(obj);
		}

	}


}
