package parser;

import ast.*;
import lexer.Token;
import lexer.Token.TokenClass;
import lexer.Tokeniser;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * @author cdubach
 */
public class Parser {

    private Token token;

    // use for backtracking (useful for distinguishing decls from procs when parsing a program for instance)
    private Queue<Token> buffer = new LinkedList<>();

    private final Tokeniser tokeniser;


    public Parser(Tokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    public Program parse() {
        // get the first token
        nextToken();

        return parseProgram();
    }

    public int getErrorCount() {
        return error;
    }

    private int error = 0;
    private Token lastErrorToken;

    private void error(TokenClass... expected) {

        if (lastErrorToken == token) {
            // skip this error, same token causing trouble
            return;
        }

        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (TokenClass e : expected) {
            sb.append(sep);
            sb.append(e);
            sep = "|";
        }
        System.out.println("Parsing error: expected (" + sb + ") found (" + token + ") at " + token.position);

        error++;
        lastErrorToken = token;
    }

    /*
     * Look ahead the i^th element from the stream of token.
     * i should be >= 1
     */
    private Token lookAhead(int i) {
        // ensures the buffer has the element we want to look ahead
        while (buffer.size() < i)
            buffer.add(tokeniser.nextToken());
        assert buffer.size() >= i;

        int cnt = 1;
        for (Token t : buffer) {
            if (cnt == i)
                return t;
            cnt++;
        }

        assert false; // should never reach this
        return null;
    }


    /*
     * Consumes the next token from the tokeniser or the buffer if not empty.
     */
    private void nextToken() {
        if (!buffer.isEmpty())
            token = buffer.remove();
        else
            token = tokeniser.nextToken();
    }

    /*
     * If the current token is equals to the expected one, then skip it, otherwise report an error.
     * Returns the expected token or null if an error occurred.
     */
    private Token expect(TokenClass... expected) {
        for (TokenClass e : expected) {
            if (e == token.tokenClass) {
                Token cur = token;
                nextToken();
                return cur;
            }
        }

        error(expected);
        return null;
    }

    /*
     * Returns true if the current token is equals to any of the expected ones.
     */
    private boolean accept(TokenClass... expected) {
        boolean result = false;
        for (TokenClass e : expected)
            result |= (e == token.tokenClass);
        return result;
    }


    private Program parseProgram() {
        parseIncludes();
        List<StructTypeDecl> stds = parseStructDecls();
        List<VarDecl> vds = parseVarDecls(false);
        List<FunDecl> fds = parseFunDecls(false);
        expect(TokenClass.EOF);
        return new Program(stds, vds, fds);
    }

    // includes are ignored, so does not need to return an AST node
    private void parseIncludes() {
        if (accept(TokenClass.INCLUDE)) {
            nextToken();
            expect(TokenClass.STRING_LITERAL);
            parseIncludes();
        }
    }

    private List<StructTypeDecl> parseStructDecls() {
        if (accept(TokenClass.STRUCT) && lookAhead(2).tokenClass == TokenClass.LBRA) {
            parseStructType();
            expect(TokenClass.LBRA);
            parseVarDecls(true);
            expect(TokenClass.RBRA);
            expect(TokenClass.SC);
            parseStructDecls();
        }
    }

    private List<VarDecl> parseVarDecls(boolean noneZero) {
        if (noneZero) {
            parseType();
            expect(TokenClass.IDENTIFIER);
            if (accept(TokenClass.LSBR)) {
                nextToken();
                expect(TokenClass.INT_LITERAL);
                expect(TokenClass.RSBR);
            }
            expect(TokenClass.SC);
            parseVarDecls(false);
        } else if (acceptTypeNotPointerNotStruct()) {
            if (lookAhead(1).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(2).tokenClass == TokenClass.SC || lookAhead(2).tokenClass == TokenClass.LSBR)
                    parseVarDecls(true);
        } else if (acceptTypeNotPointerStruct() || acceptPointerTypeNotStruct()) {
            if (lookAhead(2).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(3).tokenClass == TokenClass.SC || lookAhead(3).tokenClass == TokenClass.LSBR)
                    parseVarDecls(true);
        } else if (acceptPointerTypeStruct()) {
            if (lookAhead(3).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(4).tokenClass == TokenClass.SC || lookAhead(4).tokenClass == TokenClass.LSBR)
                    parseVarDecls(true);
        }
    }

    private List<FunDecl> parseFunDecls(boolean noneZero) {
        if (noneZero) {
            parseType();
            expect(TokenClass.IDENTIFIER);
            expect(TokenClass.LPAR);
            parseParams();
            expect(TokenClass.RPAR);
            parseBlock();
            parseFunDecls(false);
        } else if (acceptTypeNotPointerNotStruct()) {
            if (lookAhead(1).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(2).tokenClass == TokenClass.LPAR)
                    parseFunDecls(true);
        } else if (acceptTypeNotPointerStruct() || acceptPointerTypeNotStruct()) {
            if (lookAhead(2).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(3).tokenClass == TokenClass.LPAR)
                    parseFunDecls(true);
        } else if (acceptPointerTypeStruct()) {
            if (lookAhead(3).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(4).tokenClass == TokenClass.LPAR)
                    parseFunDecls(true);
        }
    }

    private void parseType() {
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID)) {
            nextToken();
        } else {
            parseStructType();
        }

        if (accept(TokenClass.ASTERIX))
            nextToken();
    }

    private void parseStructType() {
        expect(TokenClass.STRUCT);
        expect(TokenClass.IDENTIFIER);
    }

    private void parseParams() {
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
            parseType();
            expect(TokenClass.IDENTIFIER);
            while (true) {
                if (accept(TokenClass.COMMA)) {
                    nextToken();
                    parseType();
                    expect(TokenClass.IDENTIFIER);
                } else
                    break;
            }
        }
    }

    private List<Stmt> parseStmt(boolean noneZero) {
        if (noneZero) {
            if (accept(TokenClass.LBRA)) {
                parseBlock();
            } else if (accept(TokenClass.WHILE)) {
                nextToken();
                expect(TokenClass.LPAR);
                parseExp();
                expect(TokenClass.RPAR);
                parseStmt(true);
            } else if (accept(TokenClass.IF)) {
                nextToken();
                expect(TokenClass.LPAR);
                parseExp();
                expect(TokenClass.RPAR);
                parseStmt(true);
                if (accept(TokenClass.ELSE)) {
                    nextToken();
                    parseStmt(true);
                }
            } else if (accept(TokenClass.RETURN)) {
                nextToken();
                if (accept(TokenClass.LPAR, TokenClass.IDENTIFIER, TokenClass.INT_LITERAL, TokenClass.MINUS, TokenClass.CHAR_LITERAL,
                        TokenClass.STRING_LITERAL, TokenClass.ASTERIX, TokenClass.SIZEOF))
                    parseExp();
                expect(TokenClass.SC);
            } else {
                parseExp();
                if (!accept(TokenClass.SC)) {
                    expect(TokenClass.ASSIGN);
                    parseExp();
                }
                expect(TokenClass.SC);
            }
            parseStmt(false);
        } else {
            if (accept(TokenClass.LBRA, TokenClass.WHILE, TokenClass.IF, TokenClass.RETURN,
                    TokenClass.LPAR, TokenClass.IDENTIFIER, TokenClass.INT_LITERAL, TokenClass.MINUS, TokenClass.CHAR_LITERAL, TokenClass.STRING_LITERAL, TokenClass.ASTERIX, TokenClass.SIZEOF)) {
                parseStmt(true);
            }
        }
    }

    private Block parseBlock() {
        expect(TokenClass.LBRA);
        List<VarDecl> vd = parseVarDecls(false);
        List<Stmt> ps = parseStmt(false);
        expect(TokenClass.RBRA);
        return new Block(vd, ps);
    }

    private Expr parseExp() {
        Expr e = parseLv7();
        while (true) {
            if (accept(TokenClass.OR)) {
                nextToken();
                e = new BinOp(e, Op.OR, parseLv7());
            } else {
                break;
            }
        }
        return e;
    }

    private Expr parseLv7() {
        Expr e = parseLv6();
        while (true) {
            if (accept(TokenClass.AND)) {
                nextToken();
                e = new BinOp(e, Op.AND, parseLv6());
            } else {
                break;
            }
        }
        return e;
    }

    private Expr parseLv6() {
        Expr e = parseLv5(), e2;
        Op op;
        while (true) {
            if (accept(TokenClass.EQ, TokenClass.NE)) {
                if (token.tokenClass == TokenClass.EQ) {
                    op = Op.EQ;
                } else {
                    op = Op.NE;
                }
                nextToken();
                e2 = parseLv5();
                e = new BinOp(e, op, e2);
            } else {
                break;
            }
        }
        return e;
    }

    private Expr parseLv5() {
        Expr e = parseLv4(), e2;
        Op op;
        while (true) {
            if (accept(TokenClass.LT, TokenClass.GT, TokenClass.LE, TokenClass.GE)) {
                switch (token.tokenClass) {
                    case LT:
                        op = Op.LT;
                        break;
                    case GT:
                        op = Op.GT;
                        break;
                    case LE:
                        op = Op.LE;
                        break;
                    case GE:
                        op = Op.GE;
                        break;
                    default:
                        op = null;
                        error();
                }
                nextToken();
                e2 = parseLv4();
                e = new BinOp(e, op, e2);
            } else {
                break;
            }
        }
        return e;
    }

    private Expr parseLv4() {
        Expr e = parseLv3(), e2;
        Op op;
        while (true) {
            if (accept(TokenClass.PLUS, TokenClass.MINUS)) {
                if (token.tokenClass == TokenClass.PLUS) {
                    op = Op.ADD;
                } else {
                    op = Op.SUB;
                }
                nextToken();
                e2 = parseLv3();
                e = new BinOp(e, op, e2);
            } else {
                break;
            }
        }
        return e;
    }

    private Expr parseLv3() {
        Expr e = parseLv2(), e2;
        Op op;
        while (true) {
            if (accept(TokenClass.ASTERIX, TokenClass.DIV, TokenClass.REM)) {
                if (token.tokenClass == TokenClass.ASTERIX) {
                    op = Op.MUL;
                } else if (token.tokenClass == TokenClass.DIV) {
                    op = Op.DIV;
                } else {
                    op = Op.MOD;
                }
                nextToken();
                e2 = parseLv2();
                e = new BinOp(e, op, e2);
            } else {
                break;
            }
        }
        return e;
    }

    private Expr parseLv2() {
        Expr e = null;
        boolean raw = true;
        if (accept(TokenClass.SIZEOF)) {
            return parseSizeof();
        } else {
            while (true) {
                if (accept(TokenClass.ASTERIX, TokenClass.MINUS)) {
                    raw = false;
                    boolean flag;
                    if (token.tokenClass == TokenClass.ASTERIX) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                    nextToken();
                    e = parseLv1();
                    if (flag)
                        e = new ValueAtExpr(e);
                    else
                        e = new BinOp(new IntLiteral(0), Op.SUB, e);
                } else if (accept(TokenClass.LPAR)
                        && (lookAhead(1).tokenClass == TokenClass.STRUCT ||
                        lookAhead(1).tokenClass == TokenClass.INT ||
                        lookAhead(1).tokenClass == TokenClass.CHAR ||
                        lookAhead(1).tokenClass == TokenClass.VOID)) {
                    raw = false;
                    nextToken();
                    Type t = parseType();
                    nextToken();
                    e = parseLv1();
                    e = new TypecastExpr(t, e);
                } else
                    break;
            }
            if (raw) {
                e = parseLv1();
            }
            return e;
        }
    }

    private Expr parseLv1() {
        Expr e, follow;
        if (accept(TokenClass.LPAR)) {
            nextToken();
            e = parseExp();
            expect(TokenClass.RPAR);
        } else {
            if (lookAhead(1).tokenClass == TokenClass.LPAR && accept(TokenClass.IDENTIFIER)) {
                e = parseFuncall();
            } else
                e = parseFactor();
            while (true) {
                if (accept(TokenClass.LSBR)) {
                    nextToken();
                    follow = parseExp();
                    e = new ArrayAccessExpr(e, follow);
                    expect(TokenClass.RSBR);
                } else if (accept(TokenClass.DOT)) {
                    nextToken();
                    String name = token.data;
                    expect(TokenClass.IDENTIFIER);
                    e = new FieldAccessExpr(e, name);
                } else
                    break;
            }
        }
        return e;
    }

    private Expr parseFactor() {
        Expr e = null;
        if (accept(TokenClass.INT_LITERAL)) {
            e = parseIntLiteral();
        } else if (accept(TokenClass.STRING_LITERAL)) {
            e = new StrLiteral(token.data);
            nextToken();
        } else if (accept(TokenClass.CHAR_LITERAL)) {
            e = new ChrLiteral(token.data.charAt(0));
            nextToken();
        } else {
            if (accept(TokenClass.IDENTIFIER)) {
                e = new VarExpr(token.data);
                nextToken();
            } else {
                error();
            }
        }
        return e;
    }

    private IntLiteral parseIntLiteral() {
        Token n = expect(TokenClass.INT_LITERAL);
        assert n != null;
        int i = Integer.parseInt(n.data);
        return new IntLiteral(i);
    }

    private FunCallExpr parseFuncall() {
        expect(TokenClass.IDENTIFIER);
        String id = token.data;
        expect(TokenClass.LPAR);
        List<Expr> es = null;
        Expr e;
        if (accept(TokenClass.RPAR)) {
            nextToken();
        } else {
            while (true) {
                e = parseExp();
                es.add(e);
                if (!accept(TokenClass.COMMA))
                    break;
                else
                    nextToken();
            }
            expect(TokenClass.RPAR);
        }
        return new FunCallExpr(id, es);
    }

    private SizeOfExpr parseSizeof() {
        expect(TokenClass.SIZEOF);
        expect(TokenClass.LPAR);
        Type t = parseType();
        expect(TokenClass.RPAR);
        return new SizeOfExpr(t);
    }

    private boolean acceptTypeNotPointerNotStruct() {
        boolean result = false;
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID)) {
            if (lookAhead(1).tokenClass != TokenClass.ASTERIX)
                result = true;
        }
        return result;
    }

    private boolean acceptTypeNotPointerStruct() {
        boolean result = false;
        if (accept(TokenClass.STRUCT)) {
            if (lookAhead(1).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(2).tokenClass != TokenClass.ASTERIX)
                    result = true;
        }
        return result;
    }

    private boolean acceptPointerTypeNotStruct() {
        boolean result = false;
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID)) {
            if (lookAhead(1).tokenClass == TokenClass.ASTERIX)
                result = true;
        }
        return result;
    }

    private boolean acceptPointerTypeStruct() {
        boolean result = false;
        if (accept(TokenClass.STRUCT)) {
            if (lookAhead(1).tokenClass == TokenClass.IDENTIFIER)
                if (lookAhead(2).tokenClass == TokenClass.ASTERIX)
                    result = true;
        }
        return result;
    }

    /*
    private void parseExp() {
        if (accept(TokenClass.LPAR)) {
            if (lookAhead(1).tokenClass == TokenClass.CHAR || lookAhead(1).tokenClass == TokenClass.INT
                    || lookAhead(1).tokenClass == TokenClass.VOID || lookAhead(1).tokenClass == TokenClass.STRUCT) {
                parseTypecast();
                parseExp_();
            } else {
                nextToken();
                parseExp();
                expect(TokenClass.RPAR);
                parseExp_();
            }
        } else if (accept(TokenClass.IDENTIFIER)) {
            if (lookAhead(1).tokenClass == TokenClass.LPAR) {
                parseFuncall();
                parseExp_();
            } else {
                nextToken();
                parseExp_();
            }
        } else if (accept(TokenClass.INT_LITERAL)) {
            nextToken();
            parseExp_();
        } else if (accept(TokenClass.MINUS)) {
            nextToken();
            parseExp();
            parseExp_();
        } else if (accept(TokenClass.CHAR_LITERAL)) {
            nextToken();
            parseExp_();
        } else if (accept(TokenClass.STRING_LITERAL)) {
            nextToken();
            parseExp_();
        } else if (accept(TokenClass.ASTERIX)) {
            parseValueat();
            parseExp_();
        } else if (accept(TokenClass.SIZEOF)) {
            parseSizeof();
            parseExp_();
        } else {
            error();
        }
    }

    private void parseExp_() {
        if (accept(TokenClass.GT, TokenClass.GE, TokenClass.LT, TokenClass.LE, TokenClass.EQ, TokenClass.NE,
                TokenClass.PLUS, TokenClass.MINUS, TokenClass.ASTERIX, TokenClass.DIV, TokenClass.REM, TokenClass.AND, TokenClass.OR)) {
            nextToken();
            parseExp();
            parseExp_();
        } else if (accept(TokenClass.LSBR)) {
            nextToken();
            parseExp();
            expect(TokenClass.RSBR);
            parseExp_();
        } else if (accept(TokenClass.DOT)) {
            nextToken();
            expect(TokenClass.IDENTIFIER);
            parseExp_();
        }
    }

     private void parseValueat() {
        expect(TokenClass.ASTERIX);
        parseExp();
    }

    private void parseTypecast() {
        expect(TokenClass.LPAR);
        parseType();
        expect(TokenClass.RPAR);
        parseExp();
    }

    */
}
