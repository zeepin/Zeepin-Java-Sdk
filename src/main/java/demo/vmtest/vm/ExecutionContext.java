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

import com.github.zeepin.core.scripts.ScriptOp;

import demo.vmtest.utils.VmReader;

public class ExecutionContext {
    public byte[] Code;
    public VmReader OpReader;
    public int InstructionPointer;
    public ExecutionEngine engine;

    public ExecutionContext(ExecutionEngine engine, byte[] code) {
        this.engine = engine;
        Code = code;
        OpReader = new VmReader(code);
        InstructionPointer = 0;
    }

    public int GetInstructionPointer() {
        return OpReader.Position();
    }

    public long SetInstructionPointer(long offset) {
        return OpReader.Seek(offset);
    }

    public ScriptOp NextInstruction() {
        return ScriptOp.valueOf(Code[OpReader.Position()]);
    }

    public ExecutionContext Clone() {
        ExecutionContext executionContext = new ExecutionContext(engine, Code);
        executionContext.InstructionPointer = this.InstructionPointer;
        executionContext.SetInstructionPointer(this.GetInstructionPointer());
        return executionContext;
    }
}