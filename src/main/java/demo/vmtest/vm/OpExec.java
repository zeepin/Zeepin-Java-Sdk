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
package demo.vmtest.vm;

import demo.vmtest.utils.PushData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.zeepin.core.scripts.ScriptOp;

public class OpExec {
    public ScriptOp Opcode;
    public String Name;
    public Method ExecFunc;
    public Method ValidatorFunc;

    public OpExec(ScriptOp opcode, String name, Method execMethod, Method validatorMethod) throws Exception {
        Opcode = opcode;
        Name = name;
        ExecFunc = execMethod;
        ValidatorFunc = validatorMethod;
    }

    public VMState Exec(ExecutionEngine engine) {
        try {
            ExecFunc.invoke(PushData.class.newInstance(), engine);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return VMState.NONE;
    }

    boolean Validator(ExecutionEngine engine) {
        try {
            if (ValidatorFunc == null) {
                return true;
            }
            ValidatorFunc.invoke(OpExecList.class.newInstance(), engine);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }

}