package sem;

import ast.*;

import java.util.List;

public class TypeCheckVisitor extends BaseSemanticVisitor<Type> {

    @Override
    public Type visitBaseType(BaseType bt) {
        return null;
    }

    @Override
    public Type visitStructTypeDecl(StructTypeDecl st) {
        if (st.varDecls.size() != 0) {
            for (VarDecl vd : st.varDecls) {
                vd.accept(this);
            }
        }
        return null;
    }

    @Override
    public Type visitBlock(Block b) {
        if (b.varDecls.size() != 0) {
            for (VarDecl vd : b.varDecls) {
                vd.accept(this);
            }
        }
        if (b.Stmt.size() != 0) {
            for (Stmt s : b.Stmt) {
                s.accept(this);
            }
        }
        return null;
    }

    @Override
    public Type visitFunDecl(FunDecl p) {
        if (p.params.size() != 0) {
            for (VarDecl vd : p.params) {
                vd.accept(this);
            }
        }

        if (p.block.varDecls.size() != 0) {
            for (VarDecl vd : p.block.varDecls) {
                vd.accept(this);
            }
        }

        if (p.block.Stmt.size() != 0) {
            for (Stmt st : p.block.Stmt) {
                Type t = st.accept(this);
                if (st instanceof Return) {
                    if (t != p.type) {
                        error("return type error, expected " + p.type + " found" + t);
                    }
                }
            }
        } else {
            if (p.type != BaseType.VOID) {
                error("no return stmt found in funDecl!");
            }
        }

        return null;
    }

    @Override
    public Type visitProgram(Program p) {
        if (p.structTypeDecls.size() != 0) {
            for (StructTypeDecl std : p.structTypeDecls) {
                std.accept(this);
            }
        }

        if (p.varDecls.size() != 0) {
            for (VarDecl vd : p.varDecls) {
                vd.accept(this);
            }
        }

        if (p.funDecls.size() != 0) {
            for (FunDecl fd : p.funDecls) {
                fd.accept(this);
            }
        }
        return null;
    }

    @Override
    public Type visitVarDecl(VarDecl vd) {
        if (vd.type == BaseType.VOID)
            error("Cannot declare void type var!");
        return null;
    }

    @Override
    public Type visitVarExpr(VarExpr v) {
        try {
            if (v.vd == null) {
                v.type = v.std.st;
                return v.std.st;
            }
            v.type = v.vd.type;
            return v.vd.type;
        } catch (Exception e) {
            error("error in visitVarExpr!");
        }
        return null;
    }

    @Override
    public Type visitPointerType(PointerType pt) {
        return null;
    }

    @Override
    public Type visitStructType(StructType st) {
        return null;
    }

    @Override
    public Type visitArrayType(ArrayType at) {
        return null;
    }

    @Override
    public Type visitIntLiteral(IntLiteral il) {
        return BaseType.INT;
    }

    @Override
    public Type visitStrLiteral(StrLiteral sl) {
        return new ArrayType(BaseType.CHAR, sl.s.length() + 1);
    }

    @Override
    public Type visitChrLiteral(ChrLiteral cl) {
        return BaseType.CHAR;
    }

    @Override
    public Type visitFunCallExpr(FunCallExpr fc) {
        List<Expr> args = fc.Exprs;
        List<VarDecl> params = fc.fd.params;
        if (args.size() != params.size()) {
            error("wrong args size!");
            return null;
        }
        for (int i = 0; i < params.size(); i++) {
            Type t1 = args.get(i).accept(this);
            Type t2 = params.get(i).type;
            if (!t1.equals(t2)) {
                if (t1 instanceof PointerType && t2 instanceof PointerType) {
                    if (((PointerType) t1).t != ((PointerType) t2).t) {
                        error("error: pointer not match!");
                    }
                } else
                    error("params match failed!");
            }
        }
        fc.type = fc.fd.type;
        return fc.type;
    }

    @Override
    public Type visitBinOp(BinOp bo) {
        Type lhsT = bo.Exprs1.accept(this);
        Type rhsT = bo.Exprs2.accept(this);
        if (bo.op != Op.NE && bo.op != Op.EQ) {
            if (lhsT == BaseType.INT && rhsT == BaseType.INT)
                return BaseType.INT;
            else {
                error("Type should be INT in BinOP!");
                return null;
            }
        } else {
            if (lhsT instanceof StructType || lhsT instanceof ArrayType || lhsT == BaseType.VOID) {
                error("wrong lhs type in BinOp!");
            }
        }
        bo.type = BaseType.INT;
        return BaseType.INT;
    }

    @Override
    public Type visitArrayAccessExpr(ArrayAccessExpr aae) {
        try {
            Type t = aae.e1.accept(this);
            Type indexT = aae.e2.accept(this);
            if ((t instanceof PointerType || t instanceof ArrayType) && indexT == BaseType.INT) {
                if (t instanceof PointerType) {
                    aae.type = ((PointerType) t).t;
                } else {
                    aae.type = ((ArrayType) t).t;
                }
            } else {
                error("ArrayAccess type error!");
                return null;
            }
            return aae.type;
        } catch (Exception e) {
            error("Exception found in ArrayAccessExpr!");
        }
        return null;
    }

    @Override
    public Type visitFieldAccessExpr(FieldAccessExpr fae) {
        Type t = fae.e.accept(this);
        if (!(t instanceof StructType)) {
            error("Field access must be for struct!");
        } else {
            if (fae.e instanceof VarExpr) {
                if (((VarExpr) fae.e).std != null) {
                    List<VarDecl> vds = ((VarExpr) fae.e).std.varDecls;
                    for (VarDecl vd : vds) {
                        if (vd.varName.equals(fae.s)) {
                            fae.type = vd.type;
                            return vd.type;
                        }
                    }
                }
            }
        }
        error("field access error");
        return null;
    }

    @Override
    public Type visitValueAtExpr(ValueAtExpr vae) {
        Type t = vae.e.accept(this);
        if (t instanceof PointerType) {
            vae.type = ((PointerType) t).t;
            return ((PointerType) t).t;
        } else {
            error("pointer error");
        }
        return null;
    }

    @Override
    public Type visitSizeOfExpr(SizeOfExpr soe) {
        soe.type = BaseType.INT;
        return BaseType.INT;
    }

    @Override
    public Type visitTypecastExpr(TypecastExpr te) {
        Type t = te.e.accept(this);
        if (t == BaseType.CHAR && te.t == BaseType.INT) {
            te.type = BaseType.INT;
            return BaseType.INT;
        } else if (t instanceof ArrayType && te.t instanceof PointerType) {
            te.type = te.t;
            return te.t;
        } else if (t instanceof PointerType && te.t instanceof PointerType) {
            te.type = te.t;
            return te.t;
        } else {
            error("type cast error!");
            return null;
        }
    }

    @Override
    public Type visitExprStmt(ExprStmt es) {
        es.e.accept(this);
        return null;
    }

    @Override
    public Type visitWhile(While w) {
        Type t = w.e.accept(this);
        w.s.accept(this);
        if (t != BaseType.INT)
            error("While condition should be int!");
        return null;
    }

    @Override
    public Type visitIf(If i) {
        Type t = i.e.accept(this);
        i.s1.accept(this);
        if (i.s2 != null)
            i.s2.accept(this);
        if (t != BaseType.INT)
            error("While condition should be int!");
        return null;
    }

    @Override
    public Type visitAssign(Assign a) {
        Type t1 = a.e1.accept(this);
        Type t2 = a.e2.accept(this);
        if (t1 == BaseType.VOID || t1 instanceof ArrayType) {
            error("Assign error! void or array");
            return null;
        } else {
            if (a.e1 instanceof VarExpr || a.e1 instanceof FieldAccessExpr || a.e1 instanceof ArrayAccessExpr || a.e1 instanceof ValueAtExpr) {
                if (t1 != t2) {
                    error("Assign type not match!");
                } else {
                    return t1;
                }
            } else {
                error("wrong left type");
            }
        }
        return null;
    }

    @Override
    public Type visitReturn(Return r) {
        if (r.e != null)
            return r.e.accept(this);
        return BaseType.VOID;
    }
}
