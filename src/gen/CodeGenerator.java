package gen;

import ast.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class CodeGenerator implements ASTVisitor<Register> {

    /*
     * Simple register allocator.
     */

    // contains all the free temporary registers
    private Stack<Register> freeRegs = new Stack<>();
    private boolean global = true;
    private int logicalTag = 0;
    private int ifTag = 0;
    private int whileTag = 0;
    private int stringTag = 0;
    private HashMap<String, Integer> structSize = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> structDefine = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> FunStructList = new HashMap<>();
    private HashMap<String, Integer> structOffset = new HashMap<>();
    private HashMap<String, Integer> localVarSize = new HashMap<>();
    private int localValOffset = 0;
    private boolean AssignAddress = false;
    private boolean isMain = false;
    private boolean isMainVoid = false;
    private String currentFun = "global";
    private int argsOffset = 0;
    private boolean voidReturn = false;

    public CodeGenerator() {
        freeRegs.addAll(Register.tmpRegs);
    }

    private class RegisterAllocationError extends Error {
    }

    private Register getRegister() {
        try {
            return freeRegs.pop();
        } catch (EmptyStackException ese) {
            throw new RegisterAllocationError(); // no more free registers, bad luck!
        }
    }

    private void freeRegister(Register reg) {
        freeRegs.push(reg);
    }


    private PrintWriter writer; // use this writer to output the assembly instructions


    public void emitProgram(Program program, File outputFile) throws FileNotFoundException {
        writer = new PrintWriter(outputFile);
        visitProgram(program);
        writer.close();
    }

    @Override
    public Register visitBaseType(BaseType bt) {
        return null;
    }

    @Override
    public Register visitPointerType(PointerType pt) {
        return null;
    }

    @Override
    public Register visitStructType(StructType st) {
        return null;
    }

    @Override
    public Register visitArrayType(ArrayType at) {
        return null;
    }

    @Override
    public Register visitStructTypeDecl(StructTypeDecl st) {
        HashMap<String, Integer> offset = new HashMap<>();
        if (st.varDecls.size() != 0) {
            for (VarDecl vd : st.varDecls) {
//                vd.accept(this);
                if (!(vd.type instanceof StructType) && !(vd.type instanceof ArrayType)) {
                    offset.put(vd.varName, st.size);
                    System.out.println("In " + st.st.s + ", " + vd.varName + " at position " + st.size);
                    st.size += 4;
                } else if (vd.type instanceof ArrayType) {
                    if (((ArrayType) vd.type).t == BaseType.CHAR) {
                        int size = ((ArrayType) vd.type).i;
                        offset.put(vd.varName, st.size);
                        System.out.println("In " + st.st.s + ", " + vd.varName + " at position " + st.size);
                        if (size % 4 != 0) {
                            st.size += 4 * (size / 4 + 1);
                        }
                    } else {
                        offset.put(vd.varName, st.size);
                        System.out.println("In " + st.st.s + ", " + vd.varName + " at position " + st.size);
                        st.size += 4 * ((ArrayType) vd.type).i;
                    }
                } else {
                    offset.put(vd.varName, st.size);
                    System.out.println("In " + st.st.s + ", " + vd.varName + " at position " + st.size);
                    st.size += structSize.get(((StructType) vd.type).s);
                    System.out.println("Found struct in struct!");
                }
            }
        }
        structDefine.put(st.st.s, offset);
        structSize.put(st.st.s, st.size);
        return null;
    }

    @Override
    public Register visitBlock(Block b) {
        localValOffset = 12;
        boolean addReturn = false;
        writer.println("###visit block###");
        for (VarDecl vd : b.varDecls) {
            vd.accept(this);
        }
        writer.println("###block vd↑###block stmt↓###");
        if (voidReturn) {
            voidReturn = false;
            addReturn = true;
        }
        for (Stmt st : b.Stmt) {
            st.accept(this);
        }
        if (addReturn) {
            new Return(null).accept(this);
        }
        writer.println("###visit block end###");
        return null;
    }

    @Override
    public Register visitFunDecl(FunDecl p) {
        if (p.name.equals("main")) {
            isMain = true;
            if (p.type == BaseType.VOID)
                isMainVoid = true;
        }
        System.out.println("At the beginning of funDecl of " + p.name + " freeregs.size() = " + freeRegs.size());
        if (freeRegs.size() != 18) {
            for (Register r : freeRegs) {
                System.out.print(r.toString() + " ");
            }
            System.out.println('\n');
        }
        currentFun = p.name;
        FunStructList.put(currentFun, new HashMap<>());
        writer.println("\n" + p.name + ":");
        if (!p.name.equals("main")) {
            writer.println("# Save fp,sp,rt");
            print("sw", "$fp", "0($sp)", null);
            print("sw", "$sp", "-4($sp)", null);
            writer.println("# End of save fp,sp,rt");
        }
        print("addi", "$sp", "$sp", String.valueOf(-argsOffset));
        argsOffset = 0;
        writer.println("move  " + Register.fp.toString() + ", " + Register.sp.toString());
//        for (VarDecl varDecl : p.params) {
//            varDecl.accept(this);
//        }
        if (!p.name.equals("print_s") && !p.name.equals("print_i") && !p.name.equals("print_c") && !p.name.equals("read_c") && !p.name.equals("read_i") && !p.name.equals("mcmalloc")) {
            if (p.type == BaseType.VOID)
                voidReturn = true;
            p.block.accept(this);
            voidReturn = false;
            System.out.println("FunDecl" + p.name);
        }
        System.out.println("At the end of funDecl of " + p.name + " freeregs.size() = " + freeRegs.size());
        return null;
    }

    @Override
    public Register visitIntLiteral(IntLiteral il) {
        Register r = getRegister();
        print("li", r.toString(), String.valueOf(il.i), null);
        return r;
    }

    @Override
    public Register visitStrLiteral(StrLiteral sl) {
        stringTag++;
        writer.println(".data\n" + "s" + stringTag + ": .asciiz \"" + sl.s + "\"\n.text");
        Register r = getRegister();
        print("la", r.toString(), "s" + stringTag, null);
        return r;
    }

    @Override
    public Register visitChrLiteral(ChrLiteral cl) {
        Register r = getRegister();
        print("li", r.toString(), "'" + String.valueOf(cl.c) + "'", null);
        return r;
    }

    @Override
    public Register visitFunCallExpr(FunCallExpr fc) {
        switch (fc.s) {
            case "print_i":
                writer.println("# print_i");
                Register i = fc.Exprs.get(0).accept(this);
                writer.println("li $v0, 1");
                writer.println("add $a0, $zero, " + i.toString());
                writer.println("syscall");
                writer.println("# print_i\n");
                freeRegister(i);
                break;
            case "print_s":
                Register s = fc.Exprs.get(0).accept(this);
                writer.println("# print_s");
                writer.println("li $v0, 4");
                writer.println("la $a0, (" + s.toString() + ")");
                writer.println("syscall");
                writer.println("# print_s\n");
                freeRegister(s);
                break;
            case "print_c":
                Register c = fc.Exprs.get(0).accept(this);
                writer.println("# print_c");
                writer.println("li $v0, 11");
                writer.println("move $a0, " + c.toString());
                writer.println("syscall");
                writer.println("# print_c\n");
                freeRegister(c);
                break;
            case "read_i": {
                Register r = getRegister();
                writer.println("# read_i");
                writer.println("li $v0, 5");
                writer.println("syscall");
                writer.println("# read_i\n");
                writer.println("add " + r.toString() + " $v0, $zero");
                return r;
            }
            case "read_c": {
                Register r = getRegister();
                writer.println("# read_c");
                writer.println("li $v0, 12");
                writer.println("syscall");
                writer.println("# read_c\n");
                writer.println("add " + r.toString() + " $v0, $zero");
                return r;
            }
            case "mcmalloc": {
                Register r = fc.Exprs.get(0).accept(this);
                writer.println("# mcmalloc");
                writer.println("li $v0, 9");
                writer.println("add $a0, $zero, " + r);
                writer.println("syscall");
                writer.println("add " + r.toString() + " $v0, $zero");
                writer.println("# mcmalloc\n");
                return r;
            }
            default:
                if (fc.Exprs == null || fc.Exprs.size() == 0) {
                    writer.println("jal " + fc.s);
                } else {
                    int totalLocalSize = localVarSize.get(fc.s);
                    System.out.println("totalLocalSize: " + totalLocalSize);
                    Register r = getRegister();
                    int offset = totalLocalSize - fc.Exprs.size() * 4;
                    for (Expr e : fc.Exprs) {
                        r = e.accept(this);
                        print("sw", r.toString(), -offset + "($sp)", null);
                        offset += 4;
                        argsOffset = offset;
                    }
                    writer.println("jal " + fc.s);
                    freeRegister(r);
                }
                if (fc.fd.type != BaseType.VOID) {
                    Register rr = getRegister();
                    print("add ", rr.toString(), "$zero", "$v0");
                    return rr;
                }
        }
        return null;
    }

    @Override
    public Register visitBinOp(BinOp bo) {
        Register lhs = bo.Exprs1.accept(this);
        Register rhs = bo.Exprs2.accept(this);
        System.out.println("BinOp registers:" + lhs.toString() + '\t' + rhs.toString());
        Register r = getRegister();
        switch (bo.op) {
            case ADD:
                print("add", r.toString(), lhs.toString(), rhs.toString());
                break;
            case SUB:
                print("sub", r.toString(), lhs.toString(), rhs.toString());
                break;
            case MUL:
                print("mul", r.toString(), lhs.toString(), rhs.toString());
                break;
            case DIV:
                print("div", lhs.toString(), rhs.toString(), null);
                print("mflo", r.toString(), null, null);
                break;
            case MOD:
                print("div", lhs.toString(), rhs.toString(), null);
                print("mfhi", r.toString(), null, null);
                break;
            case GT:
                print("sgt", r.toString(), lhs.toString(), rhs.toString());
                break;
            case LT:
                print("slt", r.toString(), lhs.toString(), rhs.toString());
                break;
            case GE:
                print("sge", r.toString(), lhs.toString(), rhs.toString());
                break;
            case LE:
                print("sle", r.toString(), lhs.toString(), rhs.toString());
                break;
            case NE:
                print("sne", r.toString(), lhs.toString(), rhs.toString());
                break;
            case EQ:
                print("seq", r.toString(), lhs.toString(), rhs.toString());
                break;
            case OR:
                logicalTag++;
                print("li", r.toString(), "1", null);
                print("beq", lhs.toString(), "1", "logicalTag" + logicalTag);
                print("beq", rhs.toString(), "1", "logicalTag" + logicalTag);
                print("li", r.toString(), "0", null);
                print("logicalTag" + logicalTag + ":", null, null, null);
                break;
            case AND:
                logicalTag++;
                print("li", r.toString(), "0", null);
                print("beq", lhs.toString(), "0", "logicalTag" + logicalTag);
                print("beq", rhs.toString(), "0", "logicalTag" + logicalTag);
                print("li", r.toString(), "1", null);
                print("logicalTag" + logicalTag + ":", null, null, null);
                break;
        }
        freeRegister(lhs);
        freeRegister(rhs);
        return r;
    }

    @Override
    public Register visitArrayAccessExpr(ArrayAccessExpr aae) {
        Register r = getRegister();
        Register index = aae.e2.accept(this);
        int size = 4;
        if (aae.e1.type instanceof ArrayType) {
            if (((ArrayType) aae.e1.type).t == BaseType.CHAR)
                size = 1;
        }
        print("li", r.toString(), String.valueOf(size), null);
        print("mult", r.toString(), index.toString(), null);
        print("mflo", r.toString(), null, null);
        Register name = aae.e1.accept(this);
        print("add", r.toString(), r.toString(), name.toString());
        if (!AssignAddress) {
            //load value
            if (size == 1) {
                print("lb", r.toString(), "0(" + r.toString() + ")", null);
            } else {
                print("lw", r.toString(), "0(" + r.toString() + ")", null);
            }
            writer.println("# load the value of array element!");
        }
        freeRegister(name);
        freeRegister(index);
        return r;
    }

    @Override
    public Register visitFieldAccessExpr(FieldAccessExpr fae) {
        System.out.println("Field access Expr " + fae.s);
        writer.println("# Field access Expr" + fae.s);
        Register r = getRegister();
        Register offset = getRegister();
        int position = structOffset.get(((VarExpr) fae.e).vd.varName);
        System.out.println("In struct position is " + position);
        print("li", offset.toString(), String.valueOf(position), null);
        print("add", r.toString(), "$fp", offset.toString());
        System.out.println(String.valueOf(structOffset.get(((VarExpr) fae.e).vd.varName)) + offset.toString());
        freeRegister(offset);
        if (AssignAddress) {
            return r;
        } else {
            print("lw", r.toString(), "(" + r.toString() + ")", null);
            return r;
        }
        //todo
    }

    @Override
    public Register visitValueAtExpr(ValueAtExpr vae) {
        return vae.e.accept(this);
    }

    @Override
    public Register visitSizeOfExpr(SizeOfExpr soe) {
        Register r = getRegister();
        if (soe.t == BaseType.CHAR)
            print("li", r.toString(), "1", null);
        else if (soe.t instanceof ArrayType) {
            if (((ArrayType) soe.t).t == BaseType.CHAR) {
                print("li", r.toString(), String.valueOf(((ArrayType) soe.t).i), null);
            } else {
                print("li", r.toString(), String.valueOf(((ArrayType) soe.t).i * 4), null);
            }
        } else {
            print("li", r.toString(), "4", null);
        }
        return r;
    }

    @Override
    public Register visitTypecastExpr(TypecastExpr te) {
        return te.e.accept(this);
    }

    @Override
    public Register visitExprStmt(ExprStmt es) {
        return es.e.accept(this);
    }

    @Override
    public Register visitWhile(While w) {
        whileTag++;
        int temp = whileTag;
        writer.println("whileStart" + temp + ":");
        Register expr = w.e.accept(this);
        print("beq", expr.toString(), "$zero", "whileEnd" + temp);
        w.s.accept(this);
        print("b", "whileStart" + temp, null, null);
        writer.println("whileEnd" + temp + ":");
        freeRegister(expr);
        return null;
    }

    @Override
    public Register visitIf(If i) {
        ifTag++;
        int temp = ifTag;
        Register expr = i.e.accept(this);
        print("beq", expr.toString(), "$zero", "ifTag" + temp);
        i.s1.accept(this);
        print("b", "endElse" + temp, null, null);
        writer.println("ifTag" + temp + ":");
        if (i.s2 != null) {
            ifTag++;
            i.s2.accept(this);
        }
        writer.println("endElse" + temp + ":");
        freeRegister(expr);
        return null;
    }

    @Override
    public Register visitAssign(Assign a) {
        Register r = a.e2.accept(this);
        if (a.e1 instanceof VarExpr) {
            VarDecl vd = ((VarExpr) a.e1).vd;
            if (vd.global) {
                System.out.println("assign a global value " + vd.varName);
                Register address = getRegister();
                print("la", address.toString(), vd.varName, null);
                if (vd.type != BaseType.CHAR)
                    print("sw", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 1");
                else
                    print("sb", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 2");
                freeRegister(address);
            } else {
                if (vd.type != BaseType.CHAR)
                    print("sw", r.toString(), (-vd.offset + 4) + "($fp)", "# visitAssign Tag 3");
                else
                    print("sb", r.toString(), (-vd.offset + 4) + "($fp)", "# visitAssign Tag 4");
            }
        } else if (a.e1 instanceof ValueAtExpr) {
            System.out.println("assign a ValueAtExpr");
            Register address = ((ValueAtExpr) a.e1).e.accept(this);
            print("sw", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 5");
            freeRegister(address);
        } else if (a.e1 instanceof ArrayAccessExpr) {
            AssignAddress = true;
            System.out.println("assign an ArrayAccessExpr");
            Register address = a.e1.accept(this);
            if (a.e1.type == BaseType.CHAR)
                print("sb", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 6");
            else
                print("sw", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 6");
            freeRegister(address);
            AssignAddress = false;
        } else if (a.e1 instanceof FieldAccessExpr) {
            AssignAddress = true;
            System.out.println("assign a FieldAccessExpr");
            writer.println("# assign a FieldAccessExpr");
            Register address = a.e1.accept(this);
            writer.println("sw  " + r.toString() + ", (" + address.toString() + ")");
            freeRegister(address);
            AssignAddress = false;
        }
        freeRegister(r);
        return null;
    }

    @Override
    public Register visitReturn(Return r) {
        if (r.e != null) {
            Register register = r.e.accept(this);
            print("add  ", "$v0", "$zero", register.toString());
            freeRegister(register);
        }

        if (isMain) {
            if (isMainVoid) {
                writer.println("li $v0, 10\nsyscall");
            } else {
                System.out.println("move $a0, $v0");
                writer.println("li $v0, 17\nsyscall");
            }
        }
        if(!isMain){
            writer.println("# restore fp,sp,rt");
            print("lw", "$fp", "0($sp)", null);
            print("lw", "$sp", "-4($sp)", null);
            writer.println("# restore of save fp,sp,rt");
        }
        print("jr", Register.ra.toString(), null, null);
        return null;
    }

    @Override
    public Register visitProgram(Program p) {
        print(".data", null, null, null);
        for (StructTypeDecl std : p.structTypeDecls) {
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            vd.accept(this);
        }
        global = false;
        print("######end of declaration######\n.text", null, null, null);
        print("jal", "main", null, null);
        print("######declaration of functions######", null, null, null);
        for (FunDecl fd : p.funDecls) {
            fd.accept(this);
        }
        return null;
    }

    @Override
    public Register visitVarDecl(VarDecl vd) {
        int size = 0;
        if (vd.type == BaseType.INT || vd.type == BaseType.CHAR || vd.type instanceof PointerType)
            size = 4;
        else if (vd.type instanceof StructType) {
            size = structSize.get(((StructType) vd.type).s);
        } else if (vd.type instanceof ArrayType) {
            if (((ArrayType) vd.type).t == BaseType.CHAR) {
                size = ((ArrayType) vd.type).i;
                if (size % 4 != 0) {
                    size = 4 * (size / 4 + 1);
                }
            } else {
                size = 4 * ((ArrayType) vd.type).i;
            }
        } else {
            System.out.println("visitVarDecl: unknown type " + vd.type.toString());
        }

        if (global) {
            writer.println(vd.varName + ": .space " + size);
            vd.global = true;
        } else {
            if (vd.type instanceof StructType) {
                FunStructList.get(currentFun).put(vd.varName, localValOffset);
                System.out.println("At " + currentFun + " defined Struct " + vd.varName + " at offset " + localValOffset);
                structOffset.put(vd.varName, localValOffset);
            }
            //localVar
            localValOffset = localValOffset + size;
            vd.offset = localValOffset;
            System.out.println("local var " + vd.varName + " defined. Offset = " + (vd.offset - size) + " size = " + size);
            print("add", "$sp", "$sp", "-" + size);
            vd.global = false;
        }
        vd.size = size;
        localVarSize.put(currentFun, localValOffset);
//        System.out.println("Local var size of "+ currentFun+": "+ localValOffset);
        return null;
    }

    @Override
    public Register visitVarExpr(VarExpr v) {
        Register r = getRegister();
        Register a = getRegister();
        if (v.vd != null) {
            if (v.vd.global) {
                writer.println("la " + a.toString() + "," + v.name);
                if (v.vd.type == BaseType.CHAR) {
                    writer.println("lb " + r.toString() + ",(" + a.toString() + ")");
                } else if (v.vd.type == BaseType.INT || v.vd.type instanceof PointerType) {
                    writer.println("lw " + r.toString() + ",(" + a.toString() + ")");
                } else {
                    r = a;
                }
            } else {
                if (v.vd.type == BaseType.CHAR) {
                    writer.println("lb " + r.toString() + ", " + (-v.vd.offset + 4) + "($fp)");
                } else if (v.vd.type == BaseType.INT || v.vd.type instanceof PointerType) {
                    writer.println("lw " + r.toString() + ", " + (-v.vd.offset + 4) + "($fp)");
                } else {
                    writer.println("la " + r.toString() + ", " + (-v.vd.offset + 4) + "($fp)");
                }
            }
        } else if (v.std != null) {
            writer.println("la " + a.toString() + "," + v.name);
            r = a;
        } else {
            System.out.println("Undefined Var!");
        }
        freeRegister(a);
        return r;
    }

    private void print(String fun, String s1, String s2, String s3) {
        writer.print(fun);
        if (s1 != null) {
            writer.print(" " + s1);
            if (s2 != null) {
                writer.print(", " + s2);
                if (s3 != null) {
                    writer.print(", " + s3);
                }
            }
        }
        writer.print("\n");
    }
}
