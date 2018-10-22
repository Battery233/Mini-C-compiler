package ast;

public interface ASTVisitor<T> {
    public T visitBaseType(BaseType bt);

    public T visitPointerType(PointerType pt);

    public T visitStructType(StructType st);

    public T visitArrayType(ArrayType at);

    public T visitStructTypeDecl(StructTypeDecl std);

    public T visitBlock(Block b);

    public T visitFunDecl(FunDecl p);

    public T visitIntLiteral(IntLiteral il);

    public T visitStrLiteral(StrLiteral sl);

    public T visitChrLiteral(ChrLiteral cl);

    public T visitFunCallExpr(FunCallExpr fc);

    public T visitBinOp(BinOp bo);

    public T visitArrayAccessExpr(ArrayAccessExpr aae);

    public T visitFieldAccessExpr(FieldAccessExpr fae);

    public T visitValueAtExpr(ValueAtExpr vae);

    public T visitSizeOfExpr(SizeOfExpr soe);

    public T visitTypecastExpr(TypecastExpr te);

    public T visitExprStmt(ExprStmt es);

    public T visitWhile(While w);

    public T visitIf(If i);

    public T visitAssign(Assign a);

    public T visitReturn(Return r);

    public T visitProgram(Program p);

    public T visitVarDecl(VarDecl vd);

    public T visitVarExpr(VarExpr v);
    // (should have one visit method for each concrete AST node class)
}
