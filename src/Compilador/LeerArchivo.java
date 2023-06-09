package Compilador;

import java.io.*;
import java.util.Scanner;

public class LeerArchivo {
    
    private InputStreamReader input;
    private Scanner sc;
    
    public LeerArchivo(InputStreamReader input){
        this.input = input;
    } 

    public String getPrograma() throws IOException {
        BufferedReader buffer = new BufferedReader(input);
        String line;
        StringBuilder resultStringBuilder = new StringBuilder();
        while((line = buffer.readLine())!=null) {
            resultStringBuilder.append(line);
        }
        return resultStringBuilder.toString();
    }

}
