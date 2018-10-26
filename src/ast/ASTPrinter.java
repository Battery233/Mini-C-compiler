package ast;

import java.io.PrintWriter;

public class ASTPrinter implements ASTVisitor<Void> {

    private PrintWriter writer;

    public ASTPrinter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public Void visitBlock(Block b) {
        writer.print("Block(");
        String delimiter = "";
        for (VarDecl v : b.varDecls) {
            writer.print(delimiter);
            delimiter = ",";
            v.accept(this);
        }
        if (b.Stmt.size() != 0) {
            writer.print(delimiter);
        }
        delimiter = "";
        for (Stmt s : b.Stmt) {
            writer.print(delimiter);
            delimiter = ",";
            s.accept(this);
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitFunDecl(FunDecl fd) {
        writer.print("FunDecl(");
        fd.type.accept(this);
        writer.print("," + fd.name + ",");
        for (VarDecl vd : fd.params) {
            vd.accept(this);
            writer.print(",");
        }
        fd.block.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteral il) {
        writer.print("IntLiteral(" + il.i + ")");
        return null;
    }

    @Override
    public Void visitStrLiteral(StrLiteral sl) {
        writer.print("StrLiteral(" + sl.s + ")");
        return null;
    }

    @Override
    public Void visitChrLiteral(ChrLiteral cl) {
        writer.print("ChrLiteral(" + cl.c + ")");
        return null;
    }

    @Override
    public Void visitFunCallExpr(FunCallExpr fc) {
        writer.print("FunCallExpr(" + fc.s);
        if (fc.Exprs.size() != 0) {
            for (Expr e : fc.Exprs) {
                writer.print(",");
                e.accept(this);
            }
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitBinOp(BinOp bo) {
        writer.print("BinOp(");
        bo.Exprs1.accept(this);
        writer.print("," + bo.op + ",");
        bo.Exprs2.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitArrayAccessExpr(ArrayAccessExpr aae) {
        writer.print("ArrayAccessExpr(");
        aae.e1.accept(this);
        writer.print(",");
        aae.e2.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitFieldAccessExpr(FieldAccessExpr fae) {
        writer.print("FieldAccessExpr(");
        fae.e.accept(this);
        writer.print("," + fae.s + ")");
        return null;
    }

    @Override
    public Void visitValueAtExpr(ValueAtExpr vae) {
        writer.print("ValueAtExpr(");
        vae.e.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitSizeOfExpr(SizeOfExpr soe) {
        writer.print("SizeOfExpr(");
        soe.t.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitTypecastExpr(TypecastExpr te) {
        writer.print("TypecastExpr(");
        te.t.accept(this);
        writer.print(",");
        te.e.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt es) {
        writer.print("ExprStmt(");
        es.e.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitWhile(While w) {
        writer.print("While(");
        w.e.accept(this);
        writer.print(",");
        w.s.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitIf(If i) {
        writer.print("If(");
        i.e.accept(this);
        writer.print(",");
        i.s1.accept(this);
        if (i.s2 != null) {
            writer.print(",");
            i.s2.accept(this);
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitAssign(Assign a) {
        writer.print("Assign(");
        a.e1.accept(this);
        writer.print(",");
        a.e2.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitReturn(Return r) {
        writer.print("Return(");
        if (r.e != null) {
            r.e.accept(this);
        }
        writer.print(")");
        return null;
    }

    @Override
    public Void visitProgram(Program p) {
        writer.print("Program(");
        String delimiter = "";
        for (StructTypeDecl std : p.structTypeDecls) {
            writer.print(delimiter);
            delimiter = ",";
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            writer.print(delimiter);
            delimiter = ",";
            vd.accept(this);
        }
        for (FunDecl fd : p.funDecls) {
            writer.print(delimiter);
            delimiter = ",";
            fd.accept(this);
        }
        writer.print(")");
        writer.flush();
        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl vd) {
        writer.print("VarDecl(");
        vd.type.accept(this);
        writer.print("," + vd.varName);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitVarExpr(VarExpr v) {
        writer.print("VarExpr(");
        writer.print(v.name);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitBaseType(BaseType bt) {
        writer.print(bt);
        return null;
    }

    @Override
    public Void visitPointerType(PointerType pt) {
        writer.print("PointerType(");
        pt.t.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitStructType(StructType st) {
        writer.print("StructType(" + st.s + ")");
        return null;
    }

    @Override
    public Void visitArrayType(ArrayType at) {
        writer.print("ArrayType(");
        at.t.accept(this);
        writer.print("," + at.i + ")");
        return null;
    }

    @Override
    public Void visitStructTypeDecl(StructTypeDecl std) {
        writer.print("StructTypeDecl(");
        std.st.accept(this);
        for (VarDecl e : std.varDecls) {
            writer.print(",");
            e.accept(this);
        }
        writer.print(")");
        return null;
    }
}
