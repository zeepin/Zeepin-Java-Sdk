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

package com.github.zeepin.smartcontract;

import com.github.zeepin.ZPTSdk;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.core.transaction.Transaction;
import com.github.zeepin.sdk.exception.SDKException;
import com.github.zeepin.smartcontract.neovm.ClaimRecord;
import com.github.zeepin.smartcontract.neovm.Nep5;
import com.github.zeepin.smartcontract.neovm.Record;
import com.github.zeepin.smartcontract.neovm.abi.AbiFunction;
import com.github.zeepin.smartcontract.neovm.abi.BuildParams;

public class NeoVm {
    private Nep5 nep5Tx = null;
    private Record recordTx = null;
    private ClaimRecord claimRecordTx = null;

    private ZPTSdk sdk;
    public NeoVm(ZPTSdk sdk){
        this.sdk = sdk;
    }
    /**
     *  get ZPTAsset Tx
     * @return instance
     */
    public Nep5 nep5() {
        if(nep5Tx == null){
            nep5Tx = new Nep5(sdk);
        }
        return nep5Tx;
    }

    /**
     * RecordTx
     * @return instance
     */
    public Record record() {
        if(recordTx == null){
            recordTx = new Record(sdk);
        }
        return recordTx;
    }

    public ClaimRecord claimRecord(){
        if (claimRecordTx == null){
            claimRecordTx = new ClaimRecord(sdk);
        }
        return claimRecordTx;
    }
    public Object sendTransaction(String contractAddr, Account acct,Account payerAcct, long gaslimit, long gasprice, AbiFunction func, boolean preExec) throws Exception {
        byte[] params;
        if(func != null) {
            params = BuildParams.serializeAbiFunction(func);
        } else {
            params = new byte[]{};
        }
        if (preExec) {
            Transaction tx = sdk.vm().makeInvokeCodeTransaction(contractAddr, null, params, null,0, 0);
            if (acct != null) {
                sdk.signTx(tx, new Account[][]{{acct}});
            }
            Object obj = sdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
            return obj;
        } else {
            String payer = payerAcct.getAddressU160().toBase58();
            Transaction tx = sdk.vm().makeInvokeCodeTransaction(contractAddr, null, params, payer,gaslimit, gasprice);
            sdk.signTx(tx, new Account[][]{{acct}});
            if(!acct.equals(payerAcct.getAddressU160().toBase58())){
                sdk.addSign(tx,payerAcct);
            }
            boolean b = sdk.getConnect().sendRawTransaction(tx.toHexString());
            if (!b) {
                throw new SDKException(ErrorCode.SendRawTxError);
            }
            return tx.hash().toHexString();
        }
    }
}
