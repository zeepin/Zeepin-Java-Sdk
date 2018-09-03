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
package com.github.neo.core;

import com.alibaba.fastjson.JSON;
import com.github.zeepin.common.ErrorCode;
import com.github.zeepin.network.exception.RpcException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NeoRpc {

    public static Object sendRawTransaction(String url,String sData) throws Exception {
        Object result = call(url,"sendrawtransaction", new Object[]{sData});
        return result;
    }
    public static Object getBalance(String url,String contractAddr,String addr) throws Exception {
        Object result = call(url,"getstorage", new Object[]{contractAddr,addr});
        return result;
    }
    public static Object call(String url,String method, Object... params) throws RpcException, IOException {
        Map req = makeRequest(method, params);
        Map response = (Map) send(url,req);
        if (response == null) {
            throw new RpcException(0, ErrorCode.ConnectUrlErr(  url + "response is null. maybe is connect error"));
        }
        else if (response.get("result")  != null) {
            return response.get("result");
        }
        else if (response.get("Result")  != null) {
            return response.get("Result");
        }
        else if (response.get("error") != null) {
            throw new RpcException(0, JSON.toJSONString(response));
        }
        else {
            throw new RpcException(0,JSON.toJSONString(response));
        }
    }

    private static Map makeRequest(String method, Object[] params) {
        Map request = new HashMap();
        request.put("jsonrpc", "2.0");
        request.put("method", method);
        request.put("params", params);
        request.put("id", 1);
        System.out.println(String.format("POST %s", JSON.toJSONString(request)));
        return request;
    }


    public static Object send(String url,Object request) throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection)  new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            try (OutputStreamWriter w = new OutputStreamWriter(connection.getOutputStream())) {
                w.write(JSON.toJSONString(request));
            }
            try (InputStreamReader r = new InputStreamReader(connection.getInputStream())) {
                StringBuffer temp = new StringBuffer();
                int c = 0;
                while ((c = r.read()) != -1) {
                    temp.append((char) c);
                }
                //System.out.println("result:"+temp.toString());
                return JSON.parseObject(temp.toString(), Map.class);
            }
        } catch (IOException e) {
        }
        return null;
    }
}
