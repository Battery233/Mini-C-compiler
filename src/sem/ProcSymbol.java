package sem;

import ast.FunDecl;

public class ProcSymbol extends Symbol {

    final FunDecl fd;

    public ProcSymbol(FunDecl fd) {
        super(fd.name);
        this.fd = fd;
    }

    @Override
    public boolean isVar() {
        return false;
    }

    @Override
    public boolean isProc() {
        return true;
    }

    @Override
    public boolean isStruct() {
        return false;
    }
}
