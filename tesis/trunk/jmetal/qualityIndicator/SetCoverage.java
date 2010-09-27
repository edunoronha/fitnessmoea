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
public class SetCoverage {

    @SuppressWarnings("static-access")
    public static void main(String[] args) {
         String problema = "setpacking 4 500";

        double[][] matrizSPEA = archivoAMatriz("C:/tesis/"+problema+"/SPEA2/FUNspea2_"+0);
          double[][] matrizNSGA = archivoAMatriz(problema+"/NSGA2/FUNnsga2_"+0);
            double[][] matrizSPEAcercano =archivoAMatriz("C:/tesis/"+problema+"/SpeaCercano/FUNspeaCercano_"+0);
//     double[][] matrizSPEA = archivoAMatriz("C:/tesis/"+problema+"/promSPEA");
//          double[][] matrizNSGA = archivoAMatriz(problema+"/PromNSGA");
//            double[][] matrizSPEAcercano =archivoAMatriz("C:/tesis/"+problema+"/promCercano");

      
        int objetivos = objetivos("C:/tesis/"+problema+"/SPEA2/FUNspea2_"+0);
        SetCoverage SC = new SetCoverage();
        SC.calcular(matrizSPEA,matrizSPEAcercano, objetivos);
        SC.calcular(matrizSPEAcercano, matrizSPEA, objetivos);
         SC.calcular(matrizNSGA, matrizSPEAcercano, objetivos);
        SC.calcular(matrizSPEAcercano, matrizNSGA, objetivos);
       
//        SC.calcular(matrizNSGA, matrizSPEAdis, objetivos);
//        SC.calcular(matrizSPEAdis, matrizNSGA, objetivos);
//        SC.calcular(matrizSPEAdis, matrizSPEA, objetivos);
//        SC.calcular(matrizSPEA, matrizSPEAdis, objetivos);
//        SC.calcular(matrizSPEAcua, matrizSPEA, objetivos);
//        SC.calcular(matrizSPEA, matrizSPEAcua, objetivos);
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

        System.out.println(totalDom + "%");
        return totalDom;
    }
    public void paretoNoDominado(double[][] X, int numberOfObjectives) {
        int xObj = 0;
        double xDomy = 0.0;
        double[] dominados = new double[X.length];
        double totalDom = 0.0;
        int cont=0;
        for (int i = 0; i < X.length; i++) {
            for (int k = 0; k < X.length; k++) {
                for (int j = 0; j < numberOfObjectives; j++) {
                    double x = X[i][j];
                    double y = X[k][j];
                    if (x <= y) {
                        xObj += 1;
                    }
                    if (xObj == numberOfObjectives) {
                        dominados[cont] = k;
                        cont++;
                    }
                }
                xObj = 0;
            }
        }
        double[][] noDominado = new double[X.length][X[0].length];
         for (int i = 0; i < X.length; i++) {
             int sacar=0;
              for (int j= 0; j< dominados.length; j++) {
            if (i==dominados[j])
                sacar=1;
        }
             if (sacar==0){
                 noDominado[i]=X[i];

             }
         }



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
