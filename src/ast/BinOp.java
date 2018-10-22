package ast;

import java.util.List;

public class BinOp extends Expr {

    public final List<Expr> Exprs1;
    public final Op op;
    public final List<Expr> Exprs2;

    public BinOp(List<Expr> Exprs1, Op op, List<Expr> Exprs2) {
        this.Exprs1 = Exprs1;
        this.op = op;
        this.Exprs2 = Exprs2;
    }


    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitBinOp(this);
    }
}
