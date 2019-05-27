package demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zeepin.ZPTSdk;
import com.github.zeepin.sdk.wallet.Account;
import com.github.zeepin.sdk.wallet.Identity;
import com.github.zeepin.common.Helper;

import java.util.ArrayList;
import java.util.List;

public class WASMTest {

 public static void main(String[] args) {
  try {
   ZPTSdk ZptSdk = getZPTSdk();
   com.github.zeepin.account.Account account = ZptSdk.getWalletMgr().getAccount("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X", "11"); //调用合约的账户地址和密码
   String addr = account.getAddressU160().toBase58();
   
   String myContractAddr = "acc8506f157d1175c02d19b76432f61c2b98be66"; //部署之后的合约地址
   
   //////////////////////////////////////////////// 调用每一个方法时，请注释掉其余所有方法的代码块再运行 ///////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   //初始化合约
   List<String> arg = new ArrayList<>();
   arg.add("1000000"); //总发行量（注意双引号不能省略，下同）   
  
   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("init", myContractAddr, arg.toArray(), addr, 20000, 1, account);
   Thread.sleep(7*1000);
   System.out.println(TxHash);   
   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
   System.out.println(result.toJSONString());
   
   
//   //查询总发行量
//   List<String> arg = new ArrayList<>();
//   arg.add("");   
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("totalSupply", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//   
//   
//   //增加总发行量
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //ceo地址（仅ceo地址有权限增减发行量）
//   arg.add("10000"); //增加值
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("increaseTotal", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//   
//   
//   //减少总发行量
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //ceo地址（仅ceo地址有权限增减发行量）
//   arg.add("10000"); //减少值
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("decreaseTotal", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//
//   
//   //向数据库里添加一组“地址-余额”（key-value对）
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //地址
//   arg.add("1000"); //余额
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("addStorage", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//   
//   
//   //查询余额
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //被查询地址
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("balanceOf", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//   
//   
//   //转账（transfer）
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //转出账户
//   arg.add("ZNEo7CMRpQXGDgSwvhm2iDGPTXhVRJcMfc"); //转入账户
//   arg.add("50"); //转账金额
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("transfer", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//   
//   
//   //授权转账（transferFrom）
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //转出账户
//   arg.add("ZZeme18pBEbnLyDo9RfVvT1xe3nmp6WMQW"); //被授权账户
//   arg.add("ZNEo7CMRpQXGDgSwvhm2iDGPTXhVRJcMfc"); //转入账户
//   arg.add("50"); //转账金额
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("transferFrom", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//   
//   
//   //授权
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //授权账户
//   arg.add("ZZeme18pBEbnLyDo9RfVvT1xe3nmp6WMQW"); //被授权账户
//   arg.add("100"); //授权金额
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("approve", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
//   
//   
//   //查询授权金额
//   List<String> arg = new ArrayList<>();
//   arg.add("ZEuzshrCsE1cnvPuuRrDYgnVYNDtyt5d3X"); //授权账户
//   arg.add("ZZeme18pBEbnLyDo9RfVvT1xe3nmp6WMQW"); //被授权账户
//  
//   String TxHash = ZptSdk.wasmvm().sendWasmTransaction("allowance", myContractAddr, arg.toArray(), addr, 20000, 1, account);
//   Thread.sleep(7*1000);
//   System.out.println(TxHash);   
//   JSONObject result =(JSONObject) ZptSdk.getRpc().getSmartCodeEvent(TxHash);
//   System.out.println(result.toJSONString());
   
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   

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
  // wm.setDefaultConnect(wm.getRestful());
  wm.setDefaultConnect(wm.getRpc());

  wm.openWalletFile("wallet.dat");

  return wm;
 }
}