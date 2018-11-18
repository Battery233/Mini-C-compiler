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
    private int localValOffset = 0;
    private boolean AssignAddress = false;
    private boolean isMain = false;
    private boolean isMainVoid = false;
    private String currentFun = "global";

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
        if (st.varDecls.size() != 0) {
            for (VarDecl vd : st.varDecls) {
//                vd.accept(this);
                if (!(vd.type instanceof StructType) && !(vd.type instanceof ArrayType))
                    st.size += 4;
                else if (vd.type instanceof ArrayType) {
                    if (((ArrayType) vd.type).t == BaseType.CHAR) {
                        int size = ((ArrayType) vd.type).i;
                        if (size % 4 != 0) {
                            st.size += 4 * (size / 4 + 1);
                        }
                    } else {
                        st.size += 4 * ((ArrayType) vd.type).i;
                    }
                } else {
                    st.size += structSize.get(((StructType) vd.type).s);
                    System.out.println("Found struct in struct!");
                }
            }
        }
        structSize.put(st.st.s, st.size);
        return null;
    }

    @Override
    public Register visitBlock(Block b) {
        localValOffset = 0;
        writer.println("###visit block###");
        for (VarDecl vd : b.varDecls) {
            vd.accept(this);
        }
        writer.println("###block vd↑###block stmt↓###");
        for (Stmt st : b.Stmt) {
            st.accept(this);
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
        currentFun = p.name;
        writer.println("\n" + p.name + ":");
        writer.println("move  " + Register.fp.toString() + ", " + Register.sp.toString());

        // TODO: to complete
        p.block.accept(this);
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
                Register i = fc.Exprs.get(0).accept(this);
                writer.println("# print_i");
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
                writer.println("la $a0, " + s.toString());
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
                //todo
                break;
        }

        return null;
    }

    @Override
    public Register visitBinOp(BinOp bo) {
        Register lhs = bo.Exprs1.accept(this);
        Register rhs = bo.Exprs2.accept(this);
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
        Register name = aae.e1.accept(this);
        Register index = aae.e2.accept(this);
        int size = 4;
        if (aae.e1.type == BaseType.CHAR) {
            size = 1;
        }
        print("li", r.toString(), String.valueOf(size), null);
        print("mult", r.toString(), index.toString(), null);
        print("mflo", r.toString(), null, null);
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
        //todo
        return null;
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
        es.e.accept(this);
        return null;
    }

    @Override
    public Register visitWhile(While w) {
        whileTag++;
        writer.println("whileStart" + whileTag + ":");
        Register expr = w.e.accept(this);
        print("beq", expr.toString(), "$zero", "whileEnd" + whileTag);
        w.s.accept(this);
        print("b", "whileStart" + whileTag, null, null);
        writer.println("whileEnd" + whileTag + ":");
        freeRegister(expr);
        return null;
    }

    @Override
    public Register visitIf(If i) {
        ifTag++;
        Register expr = i.e.accept(this);
        print("beq", expr.toString(), "$zero", "ifTag" + ifTag);
        i.s1.accept(this);
        print("b", "endElse" + ifTag, null, null);
        writer.println("ifTag" + ifTag + ":");
        if (i.s2 != null) {
            i.s2.accept(this);
        }
        writer.println("endElse" + ifTag + ":");
        freeRegister(expr);
        return null;
    }

    @Override
    public Register visitAssign(Assign a) {
        Register r = a.e2.accept(this);
        if (a.e1 instanceof VarExpr) {
            VarDecl vd = ((VarExpr) a.e1).vd;
            if (vd.global) {
                System.out.println("assign a global value" + vd.varName);
                Register address = getRegister();
                print("la", address.toString(), vd.varName, null);
                if (vd.type != BaseType.CHAR)
                    print("sw", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 1");
                else
                    print("sb", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 2");
                freeRegister(address);
            } else {
                if (vd.type != BaseType.CHAR)
                    print("sw", r.toString(), vd.offset + "($fp)", "# visitAssign Tag 3");
                else
                    print("sb", r.toString(), vd.offset + "($fp)", "# visitAssign Tag 4");
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
            print("sw", r.toString(), "(" + address.toString() + ")", "# visitAssign Tag 6");
            freeRegister(address);
            AssignAddress = false;
        } else if (a.e1 instanceof FieldAccessExpr) {
            System.out.println("assign a FieldAccessExpr");
            Register address = a.e1.accept(this);
            writer.println("sw  " + r.toString() + ", (" + address.toString() + ")");
            freeRegister(address);
        }
        //To be tested
        freeRegister(r);
        return null;
    }

    @Override
    public Register visitReturn(Return r) {
        Register register = null;
        if (r.e != null) {
            register = r.e.accept(this);
            print("add  ", "$v0", "$zero", register.toString());
        }
        if (isMain) {
            if (isMainVoid) {
                print("li", "$v0", "10", "\nsyscall");
            } else {
                print("move", "$a0", "$v0", null);
                print("li", "$v0", "17", "\nsyscall");
            }
        }
        print("jr", Register.ra.toString(), null, null);
        freeRegister(register);
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
        else if (vd.type instanceof StructType)
            size = structSize.get(((StructType) vd.type).s);
        else if (vd.type instanceof ArrayType) {
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
            //localVar
            localValOffset = localValOffset + size;
            vd.offset = localValOffset;
            System.out.println("local var " + vd.varName + " defined. Offset = " + vd.offset + " size = " + size);
            print("add", "$sp", "$sp", "-" + size);
            vd.global = false;
        }
        vd.size = size;

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
                    writer.println("lb" + r.toString() + ",(" + a.toString() + ")");
                } else if (v.vd.type == BaseType.INT || v.vd.type instanceof PointerType) {
                    writer.println("lw " + r.toString() + ",(" + a.toString() + ")");
                } else {
                    r = a;
                }
            } else {
                if (v.vd.type == BaseType.CHAR) {
                    writer.println("lb " + r.toString() + ", " + v.vd.offset + "($fp)");
                } else if (v.vd.type == BaseType.INT || v.vd.type instanceof PointerType) {
                    writer.println("lw " + r.toString() + ", " + v.vd.offset + "($fp)");
                } else {
                    writer.println("la " + r.toString() + ", " + v.vd.offset + "($fp)");
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
