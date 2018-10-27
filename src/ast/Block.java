package ast;

import java.util.List;

public class Block extends Stmt {

    public List<VarDecl> varDecls;
    public final List<Stmt> Stmt;

    public Block(List<VarDecl> varDecls, List<Stmt> Stmt) {
        this.varDecls = varDecls;
        this.Stmt = Stmt;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitBlock(this);
    }
}
