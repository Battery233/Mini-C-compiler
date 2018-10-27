package ast;

import java.util.List;

public class FunCallExpr extends Expr {

    public final String s;
    public final List<Expr> Exprs;
    public FunDecl fd;

    public FunCallExpr(String s, List<Expr> Exprs) {
        this.s = s;
        this.Exprs = Exprs;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFunCallExpr(this);
    }
}
