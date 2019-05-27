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
package demo.vmtest;

import com.github.zeepin.common.Helper;

import demo.vmtest.vm.ExecutionContext;
import demo.vmtest.vm.ExecutionEngine;

public class VmDemo {
    public static void main(String[] args) {

        try {
            String str = "52c56b61536c766b00527ac46c766b00c36c766b51527ac46203006c766b51c3616c7566";
            byte[] code = Helper.hexToBytes(str);
            ExecutionEngine engine = new ExecutionEngine();
            engine.PushContext(new ExecutionContext(engine, code));
            while (true) {
                if (engine.Contexts.size() == 0 || engine.Context == null) {
                    break;
                }
                engine.ExecuteCode();
                if (!engine.ValidateOp()) {
                    break;
                }
                System.out.println(engine.EvaluationStack.Count() + "  " + Helper.toHexString(new byte[]{engine.OpCode.getByte()}) + " " + engine.OpExec.Name + "     " + engine.EvaluationStack.info());
                engine.StepInto();
            }
            System.out.println("Stack Count:" + engine.EvaluationStack.Count());
            System.out.println("Result:" + engine.EvaluationStack.Peek(0).GetBigInteger().longValue());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

