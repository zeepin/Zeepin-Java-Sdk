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

package com.github.zeepin.core;


public enum InventoryType {
    TX(0x01),
    Block(0x02),
    Consensus(0xe0),
    ;
    private byte value;
    private InventoryType(int v) {
        value = (byte)v;
    }
    public int value() {
        return value;
    }
    
    public static InventoryType from(byte b) {
    	for(InventoryType t: InventoryType.values()) {
    		if(t.value() == b) {
    			return t;
    		}
    	}
    	throw new IllegalArgumentException();
    }
}

