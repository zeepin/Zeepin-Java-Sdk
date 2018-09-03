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
package demo;

import com.github.zeepin.common.WalletQR;
import com.github.zeepin.sdk.wallet.Wallet;

/**
 *
 *
 */
public class QrCodeDemo {
    public static void main(String[] args) {
        String keystore = "{\"address\":\"AG9W6c7nNhaiywcyVPgW9hQKvUYQr5iLvk\",\"key\":\"+UADcReBcLq0pn/2Grmz+UJsKl3ryop8pgRVHbQVgTBfT0lho06Svh4eQLSmC93j\",\"parameters\":{\"curve\":\"secp256r1\"},\"label\":\"11111\",\"scrypt\":{\"dkLen\":64,\"n\":4096,\"p\":8,\"r\":8},\"salt\":\"IfxFV0Fer5LknIyCLP2P2w==\",\"type\":\"I\",\"algorithm\":\"ECDSA\"}";
        try {
            String prikey = WalletQR.getPriKeyFromQrCode(keystore,"111111");
            System.out.println(prikey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
