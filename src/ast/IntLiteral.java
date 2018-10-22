package ast;

public class IntLiteral extends Expr {

    public final int i;

    public IntLiteral(int i) {
        this.i = i;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitIntLiteral(this);
    }
}
