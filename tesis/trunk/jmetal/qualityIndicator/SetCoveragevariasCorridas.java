/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.qualityIndicator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.SolutionSet;
import jmetal.metaheuristics.spea2.SPEA2_main;

/**
 *
 * @author Amir
 */
public class SetCoveragevariasCorridas {

    @SuppressWarnings("static-access")
    public static void main(String[] args) {
        int corrida=1;
        double[][] dominanciaSpea=new double[corrida][corrida];
        double[][] dominanciaCercano=new double[corrida][corrida];
        double[][] HV=new double[2][corrida];
          Hypervolume toma = new Hypervolume();
        for (int i =0;i<corrida;i++){
            double[][] matrizSPEA;
            double[][] matrizSPEAanillos;
            double[][]truepareto;
        for (int j=0;j<corrida;j++){
        matrizSPEA = archivoAMatriz("C:/tesis/FUNspea"+i);
        truepareto = archivoAMatriz("C:/tesis/FUN3ob");
//        double[][] matrizNSGA = archivoAMatriz("C:/tesis/NSGAIIFUN");
//        double[][] matrizSPEAdis = archivoAMatriz("C:/tesis/SPEAFUNDISTANCIA");
//        double[][] matrizSPEAcua = archivoAMatriz("C:/tesis/SPEAFUNCUADRANTE");
        matrizSPEAanillos = archivoAMatriz("C:/tesis/FUNspeaCercano"+j);
        int objetivos = objetivos("C:/tesis/FUNspea"+i);
        SetCoveragevariasCorridas SC = new SetCoveragevariasCorridas();
        dominanciaSpea[i][j]= SC.calcular(matrizSPEA,matrizSPEAanillos, objetivos);
        dominanciaCercano[j][i]=SC.calcular(matrizSPEAanillos, matrizSPEA, objetivos);
            System.out.println("");
        HV[0][j] = toma.hypervolume(matrizSPEA,truepareto,3);
         HV[1][j] = toma.hypervolume(matrizSPEAanillos,truepareto,3);
//        SC.calcular(matrizNSGA, matrizSPEAdis, objetivos);
//        SC.calcular(matrizSPEAdis, matrizNSGA, objetivos);
//        SC.calcular(matrizSPEAdis, matrizSPEA, objetivos);
//        SC.calcular(matrizSPEA, matrizSPEAdis, objetivos);
//        SC.calcular(matrizSPEAcua, matrizSPEA, objetivos);
//        SC.calcular(matrizSPEA, matrizSPEAcua, objetivos);
        }
            
        }
        int total=0;

        for (int i =0;i<corrida;i++){
            System.out.println("Dominancia Spea");
            int count=0;
            int count2=0;
            for (int j =0;j<corrida;j++){
                if (dominanciaSpea[i][j]>dominanciaCercano[j][i]){
                    count++;
                }
 else
                {
                    count2++;
 }
               
            }
             System.out.println(i+ "Spea Domino a: "+count);
                System.out.println("Spea Me dominan:" +count2);
                if (count>count2) total++;
                System.out.println("");
                System.out.println("HV de spea "+i+": "+HV[0][i]);
                System.out.println("HV de cercano "+i+": "+HV[1][i]);
        }
        System.out.println(total+" de los 10 dominan a cercano");
        System.out.println(" ");
        total=0;
        for (int i =0;i<corrida;i++){
            System.out.println("Dominancia Cercano");
            int count=0;
            int count2=0;
            for (int j =0;j<corrida;j++){
                if (dominanciaSpea[i][j]<dominanciaCercano[j][i]){
                    count++;
                }
 else
                {
                    count2++;
 }
               
            }
             System.out.println(i+"Cercano Domino a: "+count);
                System.out.println("Cercano Me dominan:" +count2);
                System.out.println("");
                if (count>count2) total++;
        }
        System.out.println(total+" de cercano dominan a spea");
    }

    public static double calcular(double[][] X, double[][] Y, int numberOfObjectives) {
        int xObj = 0;
        double xDomy = 0.0;
        double[] dominancia = new double[X.length];
        double totalDom = 0.0;

        for (int i = 0; i < X.length; i++) {
            for (int k = 0; k < Y.length; k++) {
                for (int j = 0; j < numberOfObjectives; j++) {
                    double x = X[i][j];
                    double y = Y[k][j];
                    if (x <= y) {
                        xObj += 1;
                    }
                    if (xObj == numberOfObjectives) {
                        dominancia[i] = dominancia[i] + 1;
                    }
                }
                xObj = 0;
            }
        }
        for (int i = 0; i < dominancia.length; i++) {
            totalDom += dominancia[i];
        }
        totalDom = totalDom / 100;

       // System.out.println(totalDom + "%");
        return totalDom;
    }

    public static double[][] archivoAMatriz(String path) {
        String linea = "";
        int espacio = 0;
        int contador = 0;
        int poblacion = contadorPoblacion(path);
        int objetivos = objetivos(path);
        double[][] matrizRetorno = new double[poblacion][objetivos];
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isw = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isw);
            while (br.ready()) {
                linea = br.readLine();
                String[] individuo = linea.split(" ");

                for (int i = 0; i < objetivos; i++) {
                   
                    
                    matrizRetorno[contador][i] = Double.parseDouble(individuo[i]);
                }
                contador++;
            }
        } catch (Exception ex) {
            Logger.getLogger(SPEA2_main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return matrizRetorno;


    }

    public static int contadorPoblacion(String path) {
        int tamanoPoblacion = 0;
        String linea = "";
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isw = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isw);
            while (br.ready()) {
                linea = br.readLine();
                tamanoPoblacion++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tamanoPoblacion;
    }

    public static int objetivos(String path) {
        int objetivos = 0;
        String linea = "";
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isw = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isw);
            linea = br.readLine();
            String[] obj = linea.split(" ");
            objetivos = obj.length;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return objetivos;
    }
}
