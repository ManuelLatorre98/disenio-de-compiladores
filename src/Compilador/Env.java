package Compilador;
import java.util.HashMap;
import java.util.Hashtable;

public class Env {
    private Hashtable<String,Symbol> table;
    protected Env prev;

    public Env(Env p) {
        table = new Hashtable<String,Symbol>();
        prev = p;
    }

    public void put(String s, Symbol symb) {
        table.put(s, symb);
    }

    public Symbol get(String s) {
        //todo: check NO USAR GET en declaraciones
        for (Env e = this; e != null; e = e.prev) {
            Symbol found = (Symbol) (e.table.get(s));
            if (found != null) return found;
        }
        return null;
    }

    public boolean colision(String id){
        boolean resultado = false;
        if(this.table.containsKey(id)){
            resultado = true;
        }
        return resultado;
    }
}
