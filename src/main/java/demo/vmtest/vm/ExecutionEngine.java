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
package demo.vmtest.vm;

import com.github.zeepin.common.Helper;
import com.github.zeepin.core.scripts.ScriptOp;

import demo.vmtest.utils.PushData;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecutionEngine {

    public RandomAccessStack EvaluationStack = new RandomAccessStack();
    public RandomAccessStack AltStack = new RandomAccessStack();
    public VMState State;
    public List<ExecutionContext> Contexts = new ArrayList<>();
    public ExecutionContext Context;
    public ScriptOp OpCode;
    public byte OpCodeValue;
    public OpExec OpExec;

    public ExecutionEngine() {

    }

    public ExecutionContext CurrentContext() {
        if (this.Contexts.size() == 0) {
            return null;
        }
        return this.Contexts.get(this.Contexts.size() - 1);
    }

    public void PopContext() {
        if (this.Contexts.size() > 0) {
            this.Contexts.remove(this.Contexts.size() - 1);
        }
        this.Context = CurrentContext();
    }

    public void PushContext(ExecutionContext ctx) {
        this.Contexts.add(ctx);
        this.Context = CurrentContext();
    }

    public boolean Execute() throws Exception {
        this.State = VMState.valueOf(this.State.getValue() & VMState.BREAK.getValue());
        while (true) {
            if (this.State == VMState.FAULT || this.State == VMState.HALT || this.State == VMState.BREAK) {
                break;
            }
            if (!StepInto()) {
                return false;
            }
        }
        return true;
    }

    public boolean ExecuteCode() throws Exception {
        byte code = this.Context.OpReader.reader.readByte();
        this.OpCode = ScriptOp.valueOf(code);
        OpCodeValue = code;
        return true;
    }

    public boolean ValidateOp() {
        OpExec = OpExecList.getOpExec(this.OpCode);
        if (OpExec == null) {
            System.out.println(this.OpCode + " does not support the operation code");
            return false;
        }
        return true;
    }

    public boolean StepInto() {
        try {
            this.State = ExecuteOp();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public VMState ExecuteOp() {
        if (this.OpCodeValue >= ScriptOp.OP_PUSHBYTES1.getByte() && this.OpCodeValue <= ScriptOp.OP_PUSHBYTES75.getByte()) {
            PushData.PushData(this, this.Context.OpReader.ReadBytes(this.OpCodeValue));
            return VMState.NONE;
        }
        if (!this.OpExec.Validator(this)) {
            return VMState.FAULT;
        }
        return this.OpExec.Exec(this);
    }
}






