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
import com.github.zeepin.smartcontract.nativevm.*;

public class NativeVm {
    private ZPT zpt = null;
    private Gala gala = null;
    private GId GId = null;
    private GlobalParams globalParams = null;
    private Auth auth = null;
    private Governance governance = null;
    private ZPTSdk sdk;
    public NativeVm(ZPTSdk sdk){
        this.sdk = sdk;
    }
    /**
     *  get ZPTAsset Tx
     * @return instance
     */
    public ZPT zpt() {
        if(zpt == null){
        	zpt = new ZPT(sdk);
        }
        return zpt;
    }
    public Gala gala() {
        if(gala == null){
        	gala = new Gala(sdk);
        }
        return gala;
    }
    public GId GId(){
        if (GId == null){
        	GId = new GId(sdk);
        }
        return GId;
    }
    public GlobalParams gParams(){
        if (globalParams == null){
            globalParams = new GlobalParams(sdk);
        }
        return globalParams;
    }
    public Auth auth(){
        if (auth == null){
            auth = new Auth(sdk);
        }
        return auth;
    }
    public Governance governance(){
        if (governance == null){
            governance = new Governance(sdk);
        }
        return governance;
    }
}
