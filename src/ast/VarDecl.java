package ast;

public class VarDecl implements ASTNode {
    public final Type type;
    public final String varName;
    public int size = 0;
    public int offset = 0;
    public boolean global = false;
    public String strName;

    public VarDecl(Type type, String varName) {
        this.type = type;
        this.varName = varName;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitVarDecl(this);
    }
}
