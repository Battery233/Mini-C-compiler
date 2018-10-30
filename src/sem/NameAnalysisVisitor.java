package sem;

import ast.*;

import java.util.ArrayList;
import java.util.List;

public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {

    private Scope scope = new Scope();
    private Scope rootScope = scope;

    @Override
    public Void visitBaseType(BaseType bt) {
        return null;
    }

    @Override
    public Void visitStructTypeDecl(StructTypeDecl std) {
        String name = std.st.s;
        if (scope.lookupCurrent(name) != null) {
            error("Struct name already exists in the same scope: " + name);
        } else {
            scope.put((new StructSymbol(std)));
            if (std.varDecls.size() != 0) {
                Scope oldScope = scope;
                scope = new Scope(oldScope);
                for (VarDecl vd : std.varDecls) {
                    vd.accept(this);
                }
                scope = oldScope;
            }
        }
        return null;
    }

    @Override
    public Void visitBlock(Block b) {
        Scope oldScope = scope;
        scope = new Scope(oldScope);
        for (VarDecl v : b.varDecls) {
            v.accept(this);
        }
        for (Stmt s : b.Stmt) {
            s.accept(this);
        }
        scope = oldScope;
        return null;
    }

    @Override
    public Void visitFunDecl(FunDecl p) {
        if (scope.lookupCurrent(p.name) != null) {
            error("FunDecl already exist:" + p.name);
        } else {
            scope.put(new ProcSymbol(p));
            if (p.params.size() != 0) {
                if (p.block.varDecls.size() != 0) {
                    p.block.varDecls.addAll(p.params);
                } else {
                    p.block.varDecls = p.params;
                }
            }
            visitBlock(p.block);
        }
        return null;
    }


    @Override
    public Void visitProgram(Program p) {
        // Add default functions
        Block eb = new Block(null, null);

        List<VarDecl> params = new ArrayList<>();
        params.add(new VarDecl(new PointerType(BaseType.CHAR), "s"));
        scope.put(new ProcSymbol(new FunDecl(BaseType.VOID, "print_s", params, eb)));

        params = new ArrayList<>();
        params.add(new VarDecl(BaseType.INT, "i"));
        scope.put(new ProcSymbol(new FunDecl(BaseType.VOID, "print_i", params, eb)));

        params = new ArrayList<>();
        params.add(new VarDecl(BaseType.CHAR, "c"));
        scope.put(new ProcSymbol(new FunDecl(BaseType.VOID, "print_c", params, eb)));

        scope.put(new ProcSymbol(new FunDecl(BaseType.CHAR, "read_c", new ArrayList<>(), eb)));

        scope.put(new ProcSymbol(new FunDecl(BaseType.INT, "read_i", new ArrayList<>(), eb)));

        params = new ArrayList<>();
        params.add(new VarDecl(BaseType.INT, "size"));
        scope.put(new ProcSymbol(new FunDecl(new PointerType(BaseType.VOID), "mcmalloc", params, eb)));

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
    public Void visitVarDecl(VarDecl vd) {
        if (scope.lookupCurrent(vd.varName) != null) {
            error("var declared twice: " + vd.varName);
        } else {
            scope.put(new VarSymbol(vd));
        }

        return null;
    }

    @Override
    public Void visitVarExpr(VarExpr v) {
        Symbol s = scope.lookup(v.name);
        if (s == null) {
            error("Var " + v.name + " not declared!");
        } else if (s instanceof ProcSymbol) {
            error(v.name + " is not declared as a var!");
        } else {
            if (s instanceof VarSymbol) {
                v.vd = ((VarSymbol) s).vd;
                return null;
            } else {
                v.std = ((StructSymbol) s).std;
                Symbol structSymbol = rootScope.lookup(v.std.st.s);
                if (structSymbol == null) {
                    error("undefined struct!");
                } else {
                    v.std = ((StructSymbol) structSymbol).std;
                }
            }
        }
        return null;
    }

    @Override
    public Void visitPointerType(PointerType pt) {
        pt.t.accept(this);
        return null;
    }

    @Override
    public Void visitStructType(StructType st) {
        return null;
    }

    @Override
    public Void visitArrayType(ArrayType at) {
        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteral il) {
        return null;
    }

    @Override
    public Void visitStrLiteral(StrLiteral sl) {
        return null;
    }

    @Override
    public Void visitChrLiteral(ChrLiteral cl) {
        return null;
    }

    @Override
    public Void visitFunCallExpr(FunCallExpr fc) {
        Symbol s = scope.lookup(fc.s);
        if (s == null)
            error("Function: " + fc.s + " not exist!");
        else {
            if (!(s instanceof ProcSymbol)) {
                error(fc.s + " is not defined as a Fun!");
            }
            for (Expr e : fc.Exprs) {
                e.accept(this);
            }
            try {
                fc.fd = ((ProcSymbol) s).fd;
            } catch (ClassCastException e) {
                e.printStackTrace();
                error("ClassCastException");
            }
        }
        return null;
    }

    @Override
    public Void visitBinOp(BinOp bo) {
        bo.Exprs1.accept(this);
        bo.Exprs2.accept(this);
        return null;
    }

    @Override
    public Void visitArrayAccessExpr(ArrayAccessExpr aae) {
        aae.e1.accept(this);
        aae.e2.accept(this);
        return null;
    }

    @Override
    public Void visitFieldAccessExpr(FieldAccessExpr fae) {
        fae.e.accept(this);
        return null;
    }

    @Override
    public Void visitValueAtExpr(ValueAtExpr vae) {
        vae.e.accept(this);
        return null;
    }

    @Override
    public Void visitSizeOfExpr(SizeOfExpr soe) {
        return null;
    }

    @Override
    public Void visitTypecastExpr(TypecastExpr te) {
        te.e.accept(this);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt es) {
        es.e.accept(this);
        return null;
    }

    @Override
    public Void visitWhile(While w) {
        w.e.accept(this);
        w.s.accept(this);
        return null;
    }

    @Override
    public Void visitIf(If i) {
        i.e.accept(this);
        i.s1.accept(this);
        if (i.s2 != null)
            i.s2.accept(this);
        return null;
    }

    @Override
    public Void visitAssign(Assign a) {
        a.e1.accept(this);
        a.e2.accept(this);
        return null;
    }

    @Override
    public Void visitReturn(Return r) {
        if (r.e != null)
            r.e.accept(this);
        return null;
    }
}
