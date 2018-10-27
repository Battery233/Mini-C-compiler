package sem;

import ast.VarDecl;

public class VarSymbol extends Symbol {

    public VarDecl vd;

    public VarSymbol(VarDecl vd) {
        super(vd.varName);
        this.vd = vd;
    }

    @Override
    public boolean isVar() {
        return true;
    }

    @Override
    public boolean isProc() {
        return false;
    }

    @Override
    public boolean isStruct() {
        return false;
    }
}
