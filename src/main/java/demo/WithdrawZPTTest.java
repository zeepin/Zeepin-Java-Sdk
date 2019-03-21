package demo;

import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.common.Address;
import com.github.zeepin.common.Helper;
import com.github.zeepin.core.governance.VoteInfo;
import com.github.zeepin.crypto.SignatureScheme;

public class WithdrawZPTTest {

	public static void main(String[] args) {
		String strAddr = "ZZeme18pBEbnLyDo9RfVvT1xe3nmp6WMQW";
		String pk = "79fad30cc680bafc206c765c2dc6d5eb2397de1540e62a4b04019ffda6df2ca5";
		
		try {	
			ZPTSdk zptSdk = getZptSdk();
			com.github.zeepin.account.Account account = new com.github.zeepin.account.Account(Helper.hexToBytes(pk), SignatureScheme.SHA256WITHECDSA);
			Address addr = account.getAddressU160();
			
            String res = zptSdk.nativevm().governance().getPeerInfoAll();
            JSONObject jsr = JSONObject.parseObject(res);
            System.out.println("it's getPeerInfoAll:"+jsr.toJSONString());
            System.out.println(Helper.toHexString(account.serializePublicKey()));
            String[] pubkeyList = new String[1];
    //        pubkeyList[0] = "02d2592e4f78ee8230ffff2f02d63939fb7045b48d294917033a009702ebb6c88a";
            pubkeyList[0] = "030f5467d7679ece475d931bcfc7b23d04c4435aad926b8a2cbc0563b6d22c8bc8";
            
            long[] withdrawList = new long[1];
            withdrawList[0] = 10000;
            
            VoteInfo res2 = zptSdk.nativevm().governance().getVoteInfo(pubkeyList[0],addr);
            System.out.println("---"+res2.json());
            
            String Hash = zptSdk.nativevm().governance().withdrawZPT(account, pubkeyList, withdrawList, account, 20000, 1);
            System.out.println(Hash);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static ZPTSdk getZptSdk() throws Exception {
		String ip = "http://192.168.199.244";
		String restUrl = ip + ":" + "20334";
		String rpcUrl = ip + ":" + "20336";
		String wsUrl = ip + ":" + "20335";

		ZPTSdk wm = ZPTSdk.getInstance();
		wm.setRpc(rpcUrl);
		wm.setRestful(restUrl);

		wm.setDefaultConnect(wm.getRestful());
		wm.openWalletFile("Demo3.json");

		return wm;
	}
}
