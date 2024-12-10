package br.com.controlepeso;

import static java.lang.Integer.parseInt;

public class CalculaUtils {

    public static String calculaIMC(String altura,String peso){
        int alturaAoQuadrado = Integer.parseInt(altura) *  Integer.parseInt(altura);
        int imc = Integer.parseInt(peso)/alturaAoQuadrado;

       if(imc <= 18.5){
           return "Abaixo do normal";
       }
       if((imc >= 18.6) && (imc <= 24.9)){
           return "Normal";
       }
       if((imc >= 25) && (imc <= 29.9)){
            return "Sobrepeso";
       }
       return "";
    }

    public static String calculaIDA(String peso){
        int valor = 35 * Integer.parseInt(peso);
        return String.valueOf(valor);
    }


}
