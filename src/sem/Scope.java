package sem;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    public Scope outer;
    private Map<String, Symbol> symbolTable;

    public Scope(Scope outer) {
        this.outer = outer;
        symbolTable = new HashMap<>();
    }

    public Scope() {
        this(null);
        symbolTable = new HashMap<>();
    }

    public Symbol lookup(String name) {
        Symbol s = lookupCurrent(name);
        if (s != null) {
            return s;
        } else {
            if (outer == null) {
                return null;
            } else {
                return outer.lookup(name);
            }
        }
    }

    public Symbol lookupCurrent(String name) {
        return symbolTable.get(name);
    }

    public void put(Symbol sym) {
        symbolTable.put(sym.name, sym);
    }
}
