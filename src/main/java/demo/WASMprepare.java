package demo;

import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.common.Helper;
import com.github.zeepin.sdk.wallet.Account;
import com.github.zeepin.sdk.wallet.Identity;


import java.util.ArrayList;
import java.util.List;

public class WASMprepare {


    public static void main(String[] args) {
        try {
            ZPTSdk ontSdk = getZPTSdk();

      /*      if (ontSdk.getWalletMgr().getWallet().getIdentities().size() < 1) {

                ontSdk.getWalletMgr().createIdentity("passwordtest");
                ontSdk.getWalletMgr().writeWallet();
            }
*/

          /*  Identity id = ontSdk.getWalletMgr().getWallet().getIdentities().get(0);*/



          //String hash = ontSdk.neovm().record().sendPut(id.ontid, "passwordtest", new byte[]{}, "key", "value-test", 0, 0);

            List<String> arg1 = new ArrayList<>();
            arg1.add("haha");
//    //        arg1.add("eee");
//
            com.github.zeepin.account.Account  accountTest=ontSdk.getWalletMgr().getAccount("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X","11");
           String addr= accountTest.getAddressU160().toBase58();
            List<String> arg2 = new ArrayList<>();
            arg2.add("haha");
//
//         String testPreExc=   ontSdk.wasmvm().sendWasmTransactionPrepareExc("init","acc8506f157d1175c02d19b76432f61c2b98be66", arg1.toArray(),b,20000000,20000000,accountTest);
//           // String kkk=new String(Helper.hexToBytes(bbbpre.getString("Result")));
//         System.out.println(testPreExc);

          String bbb=   ontSdk.wasmvm().sendWasmTransaction("addStorage","acc8506f157d1175c02d19b76432f61c2b98be66", arg1.toArray(),addr,20000000,20000000,accountTest);
         long kkkk=   ontSdk.nativevm().zpt().queryBalanceOf(accountTest.getAddressU160().toBase58());
         System.out.println(bbb);
         Thread.sleep(10*1000);
         String aaa= ontSdk.wasmvm().sendWasmTransaction("getStorage","acc8506f157d1175c02d19b76432f61c2b98be66",arg2.toArray(),addr,20000000,20000000,accountTest);

            System.out.println(aaa);
           JSONObject A =(JSONObject) ontSdk.getRestful().getSmartCodeEvent(aaa);
           System.out.println(A.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static ZPTSdk getZPTSdk() throws Exception {
    	// String ip = "http://192.168.3.106";
    	  String ip = "http://127.0.0.1";
    	  // String ip = "http://192.168.199.244";
    	  // String ip = "http://54.222.182.88;
    	  // String ip = "http://101.132.193.149";
    	  String restUrl = ip + ":" + "20334";
    	  String rpcUrl = ip + ":" + "20336";
    	  String wsUrl = ip + ":" + "20335";

        ZPTSdk wm = ZPTSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        wm.setDefaultConnect(wm.getRestful());

        wm.openWalletFile("wallet.dat");

      //  wm.neovm().record().setContractAddress("80f6bff7645a84298a1a52aa3745f84dba6615cf");
        return wm;
    }
}

