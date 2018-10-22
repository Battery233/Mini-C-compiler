package ast;

public class StructType implements Type{

    public final String s;

    public StructType(String s) {
        this.s = s;
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStructType(this);
    }
}
