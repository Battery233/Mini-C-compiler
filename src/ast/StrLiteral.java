package ast;

public class StrLiteral extends Expr {

    public final String s;

    public StrLiteral(String s) {
        this.s = s;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStrLiteral(this);
    }
}
