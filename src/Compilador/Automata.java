package Compilador;

import java.rmi.server.RemoteStub;
import java.util.ArrayList;

public class Automata {
    private int cabeza=0;
    private ArrayList<Token> tokenList= new ArrayList<Token>();
    private String programa;

    public Automata(String programa) {
        this.programa = programa+" ";
    }
    //"begin 2+2 a<5 end"
    public void getTokens() {
        boolean error = false;
        while(!error && cabeza<programa.length()) {
            if(!leer_blancos()) break;
            if(get_operador_relacional()) continue;
            if(get_operador_aritmetico()) continue;
            if(get_asignacion()) continue;
            if(get_punto_coma()) continue;
            if(get_coma()) continue;
            if(get_identificador()) continue;
            if(comentario()) continue;
            if(get_punto()) break;
            error = true;//no fue reconocido
        }
        if(error){
            System.out.println("ERROR LEXICO: Caracter no perteneciente al alfabeto del lenguaje");
        }
        //return tokenList;
    }
    private boolean get_operador_relacional(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        Token op_relacional_token = new Token("op_relacional");
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if (c == '=') state = 1;
                    else if (c == '<') state = 2; 
                    else if (c == '>') state = 3;
                    else not_stop = false;//no es op_relacional
                    break;
                case 1:
                    exito = true;
                    op_relacional_token.setValor("igual");
                    break;
                case 2: 
                    if(c == '>') state = 4;
                    else if(c == '=') state = 5;
                    else{//si es cualquier otra cosa q no sea > o =
                        exito = true;
                        op_relacional_token.setValor("menor");
                    }; 
                    break;
                case 3://> o >=
                    if(c == '=') state = 6;
                    else{//si es cualquier otra cosa q no sea =
                        exito = true;
                        op_relacional_token.setValor("mayor");
                    }
                    break;
                case 4:
                    exito = true;
                    op_relacional_token.setValor("distinto");
                    break;
                case 5:
                    exito = true;
                    op_relacional_token.setValor("menor_igual");
                    break;
                case 6:
                    exito = true;
                    op_relacional_token.setValor("mayor_igual");
                    break;
                default: not_stop = false;
            }
            cabeza++;
            lexema+=c;
            if(exito) {cabeza--; lexema=lexema.substring(0, lexema.length()-1);}
            //if(!exito) {cabeza++;lexema+=c;} //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(exito){
            tokenList.add(op_relacional_token);
            print_lexema_token(lexema, op_relacional_token.getNombre());
        }else{
            cabeza = inicio_cabeza;
        } 
        return exito;
    }

    private boolean get_operador_aritmetico(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        Token op_artimetico_token = new Token("op_aritmetico");
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if (c == '+') state = 1;
                    else if (c == '-') state = 2; 
                    else if (c == '*') state = 3;
                    else if (c == '/') state = 4; 
                    else not_stop = false;//no es op_arit
                    break;
                case 1:
                    exito = true;
                    op_artimetico_token.setValor("suma");
                    break;
                case 2:
                    exito = true;
                    op_artimetico_token.setValor("resta");
                    break;    
                case 3:
                    exito = true;
                    op_artimetico_token.setValor("multiplicacion");
                    break; 
                case 4:
                    exito = true;
                    op_artimetico_token.setValor("division");
                    break; 
                default: not_stop = false;
            }
            if(!exito) {cabeza++;lexema+=c;} //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(exito){
            tokenList.add(op_artimetico_token);
            print_lexema_token(lexema, op_artimetico_token.getNombre());
        }else{
            cabeza = inicio_cabeza;
        } 
        return exito;
    }   

    private boolean get_asignacion(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        Token token; // new Token("");
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if (c == ':') state = 1;
                    else not_stop = false;//FALLO
                    break;
                case 1:
                    if(c == '=') state = 2;
                    else{
                        exito = true;
                        token = new Token("asignacion_tipo");
                        tokenList.add(token);
                        print_lexema_token(lexema, token.getNombre());
                    }
                    break;
                case 2:
                    exito = true;
                    token = new Token("asignacion");
                    tokenList.add(token);
                    print_lexema_token(lexema, token.getNombre());
                    break;
                default: not_stop = false;
            }
            if(!exito) {cabeza++;lexema+=c;} //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(!exito) cabeza = inicio_cabeza;
        return exito;
    }   

    private boolean get_punto_coma(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        Token token = new Token("punto_coma"); // new Token("");
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if (c == ';') state = 1;
                    else not_stop = false;//FALLO
                    break;
                case 1:
                    exito = true;
                    tokenList.add(token);
                    print_lexema_token(lexema, token.getNombre());
                    break;
                default: not_stop = false;
            }
            if(!exito) {cabeza++;lexema+=c;} //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(!exito) cabeza = inicio_cabeza;
        return exito;
    }   

    private boolean get_coma(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        Token token = new Token("coma"); // new Token("");
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if (c == ',') state = 1;
                    else not_stop = false;//FALLO
                    break;
                case 1:
                    exito = true;
                    tokenList.add(token);
                    print_lexema_token(lexema, token.getNombre());
                    break;
                default: not_stop = false;
            }
            if(!exito) {cabeza++;lexema+=c;} //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(!exito) cabeza = inicio_cabeza;
        return exito;
    }   

    private boolean get_punto(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        Token token = new Token("punto"); // new Token("");
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if (c == '.') {state = 1;lexema+=c;}
                    else not_stop = false;//FALLO
                    break;
                case 1:
                    exito = true;
                    tokenList.add(token);
                    print_lexema_token(lexema, token.getNombre());
                    break;
                default: not_stop = false;
            }
            if(!exito) cabeza++; //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(!exito) cabeza = inicio_cabeza;
        else if(cabeza < programa.length()-1)System.out.println("WARNING: Caracteres ignorados despues del punto.");

        return exito;
    }   
    
    private boolean get_identificador(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        Token token = new Token("identificador"); // new Token("");
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if(!Character.toString(c).matches("^[a-zA-Z0-9_]+$")) state = 1;//leo hasta encontrar un espacio
                    else{
                        lexema+=c;
                        cabeza++;
                    }  
                    break;
                    case 1:
                    if(lexema.matches("^[a-zA-Z0-9_]+$")){//cualquier letra digito o guionbajo
                        exito = true;
                        tokenList.add(token);
                        print_lexema_token(lexema, token.getNombre());
                    }else not_stop = false;
                    break;
                default: not_stop = false;
            }
            //if(!exito && c != ' ' && c != ';') cabeza++; //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(!exito) cabeza = inicio_cabeza;
        return exito;
    }  

    private boolean comentario(){
        boolean exito = false;
        boolean not_stop = true;
        int inicio_cabeza = cabeza; //en caso de fallo cabeza debe retroceder a inicio
        String lexema = "";
        int state = 0;
        char c;
        while(!exito && not_stop && cabeza<programa.length()) {
            c = programa.charAt(cabeza);
            switch (state) {
                case 0:
                    if(c == '{') state = 1;
                    else not_stop = false;
                    break;
                case 1:
                    if(c == '}') state = 2;
                break;    
                case 2:       
                    exito = true;
                    print_lexema_token(lexema, "COMENTARIO-NO HAY TOKEN");
                break;
                default: not_stop = false;
            }
            if(!exito) {cabeza++;lexema+=c;} //la cabeza deber quedarse a la derecha del ultimo caracter del lexema 
        }//end_while
        if(!exito) cabeza = inicio_cabeza; 
        return exito;
    } 

    public boolean leer_blancos(){
        boolean flag = cabeza+1 < programa.length();
        char c = programa.charAt(cabeza);
        while(c == ' ' && flag){
            cabeza++;
            c = programa.charAt(cabeza);
            flag = cabeza+1 < programa.length();
        };
        return flag;
    }

    public void print_lexema_token(String lexema, String token){
        System.out.println("Lexema: "+lexema+" Token: "+token);
    }

}

