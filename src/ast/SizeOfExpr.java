package ast;

public class SizeOfExpr extends Expr {

    public final Type t;

    public SizeOfExpr(Type t) {
        this.t = t;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitSizeOfExpr(this);
    }
}
