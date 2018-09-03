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
package demo.ledger.store;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

/**
 *
 *
 */
public class LedgerStore {
    public BlockStore blockStore = null;
    public StateStore stateStore = null;
    public EventStore eventStore = null;
    private DB blockDb;
    private DB stateDb;
    private DB eventDb;
    public LedgerStore(String filePath){
        blockDb = init(filePath+"/block");
        stateDb = init(filePath+"/states");
        eventDb = init(filePath+"/ledgerevent");
        blockStore = new BlockStore(blockDb);
        stateStore = new StateStore(stateDb);
        eventStore = new EventStore(eventDb);
    }
    public DB init(String filePath){
        Options options = new Options();
        options.createIfMissing(true);
        File file = new File(filePath);
        if (!file.exists()) {
            file = new File(filePath);
        }
        try {
            return factory.open(file, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
