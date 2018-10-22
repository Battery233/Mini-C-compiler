package ast;

public class Assign extends Stmt {

    public final Expr e1;
    public final Expr e2;

    public Assign(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitAssign(this);
    }
}
