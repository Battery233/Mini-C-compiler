package sem;

import ast.StructTypeDecl;

public class StructSymbol extends Symbol {

    public final StructTypeDecl std;

    public StructSymbol(StructTypeDecl std) {
        super(std.st.s);
        this.std = std;
    }

    @Override
    public boolean isVar() {
        return false;
    }

    @Override
    public boolean isProc() {
        return false;
    }

    @Override
    public boolean isStruct() {
        return true;
    }
}
