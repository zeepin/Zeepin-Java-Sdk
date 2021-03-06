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
import com.github.zeepin.common.UInt256;
import com.github.zeepin.merkle.MerkleVerifier;
import com.github.zeepin.sdk.wallet.Identity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class merkleProofDemo {
    public static void main(String[] args) {

        try {
            ZPTSdk zptSdk = getZptSdk();
            //System.out.println(zptSdk.getConnectMgr().getMerkleProof(""));

            if(false) {
                UInt256 txroot = UInt256.parse("731be6e82cfe0382bdf04e891fdab2fd1a3cd1b97628ef8498c85789f8c798ba");
                UInt256 curBlkRoot = UInt256.parse("34aad54259addee02df4636b3d6b57cbe3398725847f05f94ea35e8356ab46b3");
                UInt256[] targetHashes = new UInt256[]{
                        UInt256.parse("b4295f52004d01be2a459b297aaa3ddc397560dddc13a917b047d8daa6bf5b2b"),
                        UInt256.parse("6b8e9b09ac23b532f9391f57047cc226eaafcf7531fba63714ba2aa7f0ffc291"),
                        UInt256.parse("863c0210cedf57a9f999f36d34b86760054234aab72eba67433ee51f6504e63a"),

                        UInt256.parse("47025ef9977449b90ea58ddf820852a04a35f0827c691365a97122141e201305"),
                        UInt256.parse("712a75cd6759c69e704353a8b3735274dcdc9026fdce7dc3f1320123c49d6938"),
                        UInt256.parse("d6a3f1cb1270c5ef4e721aabc45d4894315b268875ca850653164e31e4fe64e2"),

                        UInt256.parse("cb93a80f22bda0563e317b96332f989b0cc6764b84a41f7e22eb6025d3c63e94")
                };
                int blockHeight = 3970;
                int curBlockHeight = 3971;
                boolean b = MerkleVerifier.VerifyLeafHashInclusion(txroot, blockHeight, targetHashes, curBlkRoot, curBlockHeight+1);

                System.out.println(b);
                System.exit(0);
            }
            String txhash = "76763a5f9fc6b68d54463933e51c4b4dbce6732146294525c09b167637f2facf";
            Object proof = zptSdk.nativevm().GId().getMerkleProof(txhash);
            System.out.println(proof);
            System.out.println(zptSdk.getConnect().getMerkleProof(txhash));
            System.out.println(zptSdk.nativevm().GId().verifyMerkleProof(JSON.toJSONString(proof)));
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ZPTSdk getZptSdk() throws Exception {

        String ip = "http://127.0.0.1";
//        String ip = "http://54.222.182.88;
//        String ip = "http://101.132.193.149";
        String restUrl = ip + ":" + "20334";
        String rpcUrl = ip + ":" + "20336";
        String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        wm.setDefaultConnect(wm.getRestful());

        wm.openWalletFile("ClaimDemo.json");

        return wm;
    }
}
