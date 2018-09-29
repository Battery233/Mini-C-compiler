package lexer;

import lexer.Token.TokenClass;

import java.io.EOFException;
import java.io.IOException;

/**
 * @author cdubach
 */
public class Tokeniser {

    private Scanner scanner;

    private int error = 0;

    public int getErrorCount() {
        return this.error;
    }

    public Tokeniser(Scanner scanner) {
        this.scanner = scanner;
    }

    private void error(char c, int line, int col) {
        System.out.println("Lexing error: unrecognised character (" + c + ") at " + line + ":" + col);
        error++;
    }


    public Token nextToken() {
        Token result;
        try {
            result = next();
        } catch (EOFException eof) {
            // end of file, nothing to worry about, just return EOF token
            return new Token(TokenClass.EOF, scanner.getLine(), scanner.getColumn());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // something went horribly wrong, abort
            System.exit(-1);
            return null;
        }
        return result;
    }

    private Token next() throws IOException {

        int line = scanner.getLine();
        int column = scanner.getColumn();

        // get the next character
        char c = scanner.next();
        // skip white spaces
        if (Character.isWhitespace(c))
            return next();

        // recognises operators
        if (c == '+')
            return new Token(TokenClass.PLUS, line, column);

        if (c == '-')
            return new Token(TokenClass.MINUS, line, column);

        if (c == '*')
            return new Token(TokenClass.ASTERIX, line, column);

        if (c == '/') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                return new Token(TokenClass.DIV, line, column);
            }
            if (c != '/' && c != '*')
                return new Token(TokenClass.DIV, line, column);
            else if (c == '/') {
                c = scanner.next();
                while (c != '\n') {
                    c = scanner.next();
                }
                return next();
            } else {
                scanner.next();
                while (true) {
                    try {
                        c = scanner.next();
                    } catch (EOFException eof) {
                        error(c, line, column);
                        return new Token(TokenClass.INVALID, line, column);
                    }
                    while (c != '*') {
                        try {
                            c = scanner.next();
                        } catch (EOFException eof) {
                            error(c, line, column);
                            return new Token(TokenClass.INVALID, line, column);
                        }
                    }
                    try {
                        c = scanner.peek();
                    } catch (EOFException eof) {
                        error(c, line, column);
                        return new Token(TokenClass.INVALID, line, column);
                    }
                    if (c == '/') {
                        scanner.next();
                        break;
                    }
                }
                return next();
            }
        }

        if (c == '%')
            return new Token(TokenClass.REM, line, column);

        if (c == '.')
            return new Token(TokenClass.DOT, line, column);

        if (Character.isLetter(c) || c == '_') {
            StringBuilder input = new StringBuilder();
            input.append(c);
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                return new Token(TokenClass.IDENTIFIER, String.valueOf(c), line, column);
            }
            while (!Character.isWhitespace(c)) {
                if (Character.isLetterOrDigit(c) || c == '_') {
                    input.append(c);
                } else {
                    break;
                }
                scanner.next();
                try {
                    c = scanner.peek();
                } catch (EOFException eof) {
                    break;
                }
            }

            String result = input.toString();

            switch (result) {
                case "int":
                    return new Token(TokenClass.INT, line, column);
                case "void":
                    return new Token(TokenClass.VOID, line, column);
                case "char":
                    return new Token(TokenClass.CHAR, line, column);
                case "if":
                    return new Token(TokenClass.IF, line, column);
                case "else":
                    return new Token(TokenClass.ELSE, line, column);
                case "while":
                    return new Token(TokenClass.WHILE, line, column);
                case "return":
                    return new Token(TokenClass.RETURN, line, column);
                case "struct":
                    return new Token(TokenClass.STRUCT, line, column);
                case "sizeof":
                    return new Token(TokenClass.SIZEOF, line, column);
            }
            return new Token(TokenClass.IDENTIFIER, result, line, column);
        }

        if (c == '#') {
            StringBuilder input = new StringBuilder();
            input.append(c);
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            while (!Character.isWhitespace(c)) {
                try {
                    c = scanner.next();
                    input.append(c);
                    c = scanner.peek();
                } catch (EOFException eof) {
                    break;
                }
            }
            if (input.toString().equals("#include")) {
                return new Token(TokenClass.INCLUDE, line, column);
            } else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
        }

        if (c == '=') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                return new Token(TokenClass.ASSIGN, line, column);
            }
            if (c == '=') {
                scanner.next();
                return new Token(TokenClass.EQ, line, column);
            } else
                return new Token(TokenClass.ASSIGN, line, column);
        }

//        // delimiters
//        LBRA,  // '{' // left brace
//        RBRA,  // '}' // right brace
//        LPAR,  // '(' // left parenthesis
//        RPAR,  // ')' // right parenthesis
//        LSBR,  // '[' // left square brace
//        RSBR,  // ']' // left square brace
//        SC,    // ';' // semicolon
//        COMMA, // ','

        if (c == '{') {
            return new Token(TokenClass.LBRA, line, column);
        }
        if (c == '}') {
            return new Token(TokenClass.RBRA, line, column);
        }
        if (c == '(') {
            return new Token(TokenClass.LPAR, line, column);
        }
        if (c == ')') {
            return new Token(TokenClass.RPAR, line, column);
        }
        if (c == '[') {
            return new Token(TokenClass.LSBR, line, column);
        }
        if (c == ']') {
            return new Token(TokenClass.RSBR, line, column);
        }
        if (c == ';') {
            return new Token(TokenClass.SC, line, column);
        }
        if (c == ',') {
            return new Token(TokenClass.COMMA, line, column);
        }

//        // logical operators
//        AND, // "&&"
//        OR,  // "||"

        if (c == '&') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            if (c == '&') {
                scanner.next();
                return new Token(TokenClass.AND, line, column);
            } else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
        }

        if (c == '|') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            if (c == '|') {
                scanner.next();
                return new Token(TokenClass.OR, line, column);
            } else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
        }

//        // comparisons
//                EQ, // "=="
//                NE, // "!="
//                LT, // '<'
//                GT, // '>'
//                LE, // "<="
//                GE, // ">="
        if (c == '!') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            if (c == '=') {
                scanner.next();
                return new Token(TokenClass.NE, line, column);
            } else {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
        }

        if (c == '<') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                return new Token(TokenClass.LT, line, column);
            }
            if (c == '=') {
                scanner.next();
                return new Token(TokenClass.LE, line, column);
            } else
                return new Token(TokenClass.LT, line, column);
        }

        if (c == '>') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                return new Token(TokenClass.GT, line, column);
            }
            if (c == '=') {
                scanner.next();
                return new Token(TokenClass.GE, line, column);
            } else
                return new Token(TokenClass.GT, line, column);
        }

        // literals
        // STRING_LITERAL, // \".*\"  any sequence of characters enclosed within two double quote " (please be aware of the escape character backslash \)
        // INT_LITERAL,    // ('0'|...|'9')+
        // CHAR_LITERAL,   // \'('a'|...|'z'|'A'|...|'Z'|'\t'|'\b'|'\n'|'\r'|'\f'|'\''|'\"'|'\\'|'.'|','|'_'|...)\'  a character starts and end with a single quote '
        if (c == '"') {
            try {
                StringBuilder input = new StringBuilder();
                while (true) {
                    c = scanner.peek();
                    if (c != '"' && c != '\\' && c != '\n') {
                        input.append(c);
                        scanner.next();
                    } else if (c == '\\') {
                        scanner.next();
                        char escape = scanner.peek();
                        switch (escape) {
                            case '0': {
                                escape = '\0';
                                break;
                            }
                            case 't': {
                                escape = '\t';
                                break;
                            }
                            case 'b': {
                                escape = '\b';
                                break;
                            }
                            case 'n': {
                                escape = '\n';
                                break;
                            }
                            case 'r': {
                                escape = '\r';
                                break;
                            }
                            case 'f': {
                                escape = '\f';
                                break;
                            }
                            case '\'': {
                                escape = '\'';
                                break;
                            }
                            case '"': {
                                escape = '"';
                                break;
                            }
                            case '\\': {
                                escape = '\\';
                                break;
                            }
                            default: {
                                error(c, line, column);
                                return new Token(TokenClass.INVALID, line, column);
                            }
                        }
                        input.append(escape);
                        scanner.next();
                    } else if (c == '"') {
                        scanner.next();
                        return new Token(TokenClass.STRING_LITERAL, input.toString(), line, column);
                    } else {
                        scanner.next();
                        error(c, line, column);
                        return new Token(TokenClass.INVALID, line, column);
                    }
                }
            } catch (EOFException e) {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
        }

        if (Character.isDigit(c)) {
            StringBuilder input = new StringBuilder();
            input.append(c);
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                return new Token(TokenClass.INT_LITERAL, input.toString(), line, column);
            }
            while (Character.isDigit(c)) {
                try {
                    scanner.next();
                    input.append(c);
                    c = scanner.peek();
                } catch (EOFException eof) {
                    break;
                }
            }
            return new Token(TokenClass.INT_LITERAL, input.toString(), line, column);
        }

        if (c == '\'') {
            try {
                c = scanner.peek();
            } catch (EOFException eof) {
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            }
            if (c == '\'') {
                scanner.next();
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
            } else if (c != '\\') {
                char input = scanner.next();
                try {
                    c = scanner.peek();
                } catch (EOFException eof) {
                    error(c, line, column);
                    return new Token(TokenClass.INVALID, line, column);
                }
                if (c == '\'') {
                    scanner.next();
                    return new Token(TokenClass.CHAR_LITERAL, String.valueOf(input), line, column);
                } else {
                    try {
                        c = scanner.next();
                    } catch (EOFException eof) {
                        error(c, line, column);
                        return new Token(TokenClass.INVALID, line, column);
                    }
                    error(c, line, column);
                    return new Token(TokenClass.INVALID, line, column);
                }
            } else {
                scanner.next();
                char input;
                try{
                input = scanner.next();}
                catch (EOFException eof){
                    error(c, line, column);
                    return new Token(TokenClass.INVALID, line, column);
                }
                try {
                    c = scanner.peek();
                } catch (EOFException eof) {
                    error(c, line, column);
                    return new Token(TokenClass.INVALID, line, column);
                }
                switch (input) {
                    case '0': {
                        input = '\0';
                        break;
                    }
                    case 't': {
                        input = '\t';
                        break;
                    }
                    case 'b': {
                        input = '\b';
                        break;
                    }
                    case 'n': {
                        input = '\n';
                        break;
                    }
                    case 'r': {
                        input = '\r';
                        break;
                    }
                    case 'f': {
                        input = '\f';
                        break;
                    }
                    case '\'': {
                        input = '\'';
                        break;
                    }
                    case '"': {
                        input = '"';
                        break;
                    }
                    case '\\': {
                        input = '\\';
                        break;
                    }
                    default: {
                        error(c, line, column);
                        return new Token(TokenClass.INVALID, line, column);
                    }
                }
                if (c == '\'') {
                    scanner.next();
                    return new Token(TokenClass.CHAR_LITERAL, String.valueOf(input), line, column);
                } else {
                    scanner.next();
                    error(c, line, column);
                    return new Token(TokenClass.INVALID, line, column);
                }
            }
        }

        // if we reach this point, it means we did not recognise a valid token
        error(c, line, column);
        return new Token(TokenClass.INVALID, line, column);
    }
}
