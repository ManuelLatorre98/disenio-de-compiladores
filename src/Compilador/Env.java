package Compilador;
import java.util.HashMap;
import java.util.Hashtable;

public class Env {
    private Hashtable table;
    protected Env prev;

    public Env(Env p) {
        table = new Hashtable();
        prev = p;
    }

    public void put(String s, Symbol symb) {
        table.put(s, symb);
    }

    public Symbol get(String s) {
        for (Env e = this; e != null; e = e.prev) {
            Symbol found = (Symbol) (e.table.get(s));
            if (found != null) return found;
        }
        return null;
    }
}
