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
package demo;

import com.alibaba.fastjson.JSON;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.account.Account;
import com.github.zeepin.common.Helper;
import com.github.zeepin.common.WalletQR;
import com.github.zeepin.crypto.MnemonicCode;
import com.github.zeepin.crypto.SignatureScheme;
import com.github.zeepin.sdk.wallet.Scrypt;

import java.util.Map;

/**
 *
 *
 */
public class MnemonicDemo {
    public static void main(String[] args) {

        try {
            //Mnemonic Codes
            String code = MnemonicCode.generateMnemonicCodesStr();

            //get prikey from FromMnemonicCodes
            byte[] prikey = MnemonicCode.getPrikeyFromMnemonicCodesStrBip44(code);
            System.out.println(Helper.toHexString(prikey));

            //get keystore
            Scrypt scrypt = new Scrypt();
            com.github.zeepin.sdk.wallet.Account account = new com.github.zeepin.sdk.wallet.Account();
            //TODO change scrypt and account value
            Map keystore = WalletQR.exportAccountQRCode(scrypt,account);
            System.out.println(JSON.toJSONString(keystore));

            //import keystore
            String prikey2 = WalletQR.getPriKeyFromQrCode(JSON.toJSONString(keystore),"password");

            //import from WIF
            byte[] prikey3 = Account.getPrivateKeyFromWIF("");

            //create account or from prikey
            Account acct = new Account(SignatureScheme.SHA256WITHECDSA);
            Account acct2 = new Account(prikey3,SignatureScheme.SHA256WITHECDSA);

            //WalletQR.exportAccountQRCode()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
