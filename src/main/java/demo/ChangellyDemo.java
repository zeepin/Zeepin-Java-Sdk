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
import com.github.zeepin.common.Helper;
import com.github.zeepin.crypto.Digest;
import com.github.zeepin.network.rest.http;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 */

public class ChangellyDemo {
    //https://changelly.com/developers#keys
    static String API_KEY = "1903148a33714fbdb783de1e6b67dbd4";
    static String API_SECRET = "44fe665d0d0c208f8e5efd46c5f6da146135c870d61c9c66d7223103309f73c8";
    public static void main(String[] args) {

        try {
            Map map = new LinkedHashMap();
            if(false){
                Map req =new HashMap();
                req.put("from","btc");
                req.put("to","eth");
                req.put("address","0xE1B305994aFa1EadAd5EB1e534b6CF9b9E76e2a8");
                req.put("amount",1);
                req.put("extraId",2);

                map.put("jsonrpc","2.0");
                map.put("id",1);
                map.put("method","createTransaction");
                map.put("params",req);
            }
            if(false){
                Map req =new HashMap();
                //req.put("from","btc");
               // req.put("to","eth");
                req.put("address","0xE1B305994aFa1EadAd5EB1e534b6CF9b9E76e2a8");
                req.put("limit",10);
                req.put("offset",0);

                map.put("jsonrpc","2.0");
                map.put("id",1);
                map.put("method","getTransactions");
                map.put("params",req);
            }
            if(true){
                Map req =new HashMap();
                req.put("id","aa85eb250b6d");

                map.put("jsonrpc","2.0");
                map.put("id",1);
                map.put("method","getStatus");
                map.put("params",req);
            }
            if (false) {
                Map req =new HashMap();
                req.put("from","btc");
                req.put("to","eth");
                req.put("amount",1);

                map.put("jsonrpc", "2.0");
                map.put("id", 1);
                map.put("method","getExchangeAmount");
                map.put("params",req);
            }
            if (false) {
                map.put("jsonrpc", "2.0");
                map.put("id", 1);
                map.put("method", "getCurrencies");
                map.put("params", new Object[]{});
            }
            String text = JSON.toJSONString(map);//.replace(":",": ").replace(",",", ");
            byte[] sign =  Digest.hmacSha512(API_SECRET.getBytes(),text.getBytes());
            System.out.println(Helper.toHexString(sign));
            Map<String,String> header = new HashMap<>();
            header.put("api-key",API_KEY);
            header.put("sign",Helper.toHexString(sign));
            String result = http.post("https://api.changelly.com",header,text,false);
            System.out.println(result);
        }catch (Exception e){
        e.printStackTrace();}
    }
}
