package ast;

public class FieldAccessExpr extends Expr {

    public final Expr e;
    public final String s;

    public FieldAccessExpr(Expr e, String s) {
        this.e = e;
        this.s = s;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFieldAccessExpr(this);
    }
}
