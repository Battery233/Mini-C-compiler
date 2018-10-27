package sem;

public abstract class Symbol {
    public String name;

    abstract public boolean isVar();

    abstract public boolean isProc();

    abstract public boolean isStruct();

    public Symbol(String name) {
        this.name = name;
    }
}
