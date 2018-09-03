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
package demo.vmtest.types;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructItem extends StackItems {
    public List<StackItems> stackItems = new ArrayList<>();

    public StructItem(List<StackItems> stackItems) {
        this.stackItems = stackItems;
    }

    @Override
    public boolean Equals(StackItems item) {
        return this.equals(item);
    }

    @Override
    public BigInteger GetBigInteger() {
        return null;
    }

    @Override
    public boolean GetBoolean() {
        return true;
    }

    @Override
    public byte[] GetByteArray() {
        return null;
    }

    @Override
    public InteropItem GetInterface() {
        return null;
    }

    @Override
    public StackItems[] GetArray() {
        return null;
    }

    @Override
    public StackItems[] GetStruct() {
        return stackItems.toArray(new StackItems[stackItems.size()]);
    }

    @Override
    public Map<StackItems, StackItems> GetMap() {
        return null;
    }

    public void Add(StackItems items) {
        stackItems.add(items);
    }

    public int Count() {
        return stackItems.size();
    }
}
