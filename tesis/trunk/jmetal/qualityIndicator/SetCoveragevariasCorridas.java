/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.qualityIndicator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.SolutionSet;
import jmetal.metaheuristics.spea2.SPEA2_main;
import java.lang.Object;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

/**
 *
 * @author Amir
 */
public class SetCoveragevariasCorridas {
public static  SolutionSet poblacion;
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws FileNotFoundException, IOException, WriteException, BiffException {

        poblacion = new SolutionSet();
        int corrida =30;//Integer.parseInt(args[1]);
        String problema = "fitness 5,7 sin especial/tsp 5 1";//args[0];
        double[][] dominanciaSpea = new double[corrida][corrida];
        double[][] dominanciaCercano = new double[corrida][corrida];
         double[][] dominanciaSpeavsSpea = new double[corrida][corrida];
        double[][] dominanciaCercanovsCercano = new double[corrida][corrida];
        double[][] dominanciaNsgavsNsga = new double[corrida][corrida];
        double[][] HV = new double[3][corrida];
        Hypervolume hyperVolumen = new Hypervolume();
        for (int i = 0; i < corrida; i++) {
            double[][] matrizSPEA;
            double[][] matrizSPEAanillos;
            double[][] matrizSPEASiguiente;
            double[][] matrizSPEAanterior;
            double[][] truepareto;
               double[][] matrizNSGA;
                double[][] matrizNSGAanterior;
            truePareto(problema+"/SPEACercano/FUNspeaCercano_",problema+"/pareto1",corrida);
            truePareto(problema+"/SPEA2/FUNspea2_",problema+"/pareto2",corrida);
            truePareto(problema+"/NSGA2/FUNnsga2_",problema+"/pareto3",corrida);
//            promedioCorrida(problema+"/SPEACercano/FUNspeaCercano_",problema+"/promCercano",corrida);
//            promedioCorrida(problema+"/SPEA2/FUNspea2_",problema+"/promSPEA",corrida);
//            promedioCorrida(problema+"/NSGA2/FUNnsga2_",problema+"/PromNSGA",corrida);
//            paretoFinal(problema+"/pareto", problema+"/truepareto");
             paretoFinal(problema+"/pareto", problema+"/truepareto0");
//


            for (int j = 0; j < corrida; j++) {
                matrizSPEA = archivoAMatriz(problema + "/SPEA2/FUNspea2_" + i);
               
                 truepareto = archivoAMatriz(problema + "/truepareto0");
//        double[][] matrizNSGA = archivoAMatriz("C:/tesis/NSGAIIFUN");
//        double[][] matrizSPEAdis = archivoAMatriz("C:/tesis/SPEAFUNDISTANCIA");
//        double[][] matrizSPEAcua = archivoAMatriz("C:/tesis/SPEAFUNCUADRANTE");
                  matrizSPEASiguiente = archivoAMatriz(problema + "/SPEA2/FUNspea2_" + j);
                    matrizNSGA = archivoAMatriz(problema+"/NSGA2/FUNnsga2_"+j);
                    matrizNSGAanterior = archivoAMatriz(problema+"/NSGA2/FUNnsga2_"+i);
                matrizSPEAanillos = archivoAMatriz("C:/tesis/" + problema + "/Speacercano/FUNspeacercano_" + j);
                 matrizSPEAanterior = archivoAMatriz("C:/tesis/" + problema + "/Speacercano/FUNspeacercano_" + i);
                int objetivos = objetivos("C:/tesis/" + problema + "/SPEA2/FUNspea2_" + i);
                SetCoveragevariasCorridas SC = new SetCoveragevariasCorridas();
                dominanciaSpea[i][j] = SC.calcular(matrizSPEA, matrizSPEAanillos, objetivos);
                dominanciaCercano[j][i] = SC.calcular(matrizSPEAanillos, matrizSPEA, objetivos);
                dominanciaSpeavsSpea[i][j] = SC.calcular(matrizSPEA, matrizSPEASiguiente, objetivos);
                dominanciaCercanovsCercano[i][j] = SC.calcular(matrizSPEAanterior, matrizSPEAanillos, objetivos);
                dominanciaNsgavsNsga[i][j] = SC.calcular(matrizNSGAanterior,matrizNSGA, objetivos);
                HV[0][i] = hyperVolumen.hypervolume(matrizSPEA, truepareto, objetivos);
                HV[1][j] = hyperVolumen.hypervolume(matrizSPEAanillos, truepareto, objetivos);
                HV[2][j] = hyperVolumen.hypervolume(matrizNSGA, truepareto, objetivos);
//        SC.calcular(matrizNSGA, matrizSPEAdis, objetivos);
//        SC.calcular(matrizSPEAdis, matrizNSGA, objetivos);
//        SC.calcular(matrizSPEAdis, matrizSPEA, objetivos);
//        SC.calcular(matrizSPEA, matrizSPEAdis, objetivos);
//        SC.calcular(matrizSPEAcua, matrizSPEA, objetivos);
//        SC.calcular(matrizSPEA, matrizSPEAcua, objetivos);
            }

        }
        int total = 0;
              poblacion.printPromToFile(problema+"/dominanciaSPEAVSSPEA",dominanciaSpeavsSpea);
               poblacion.printPromToFile(problema+"/dominanciaCercanovsCercano",dominanciaCercanovsCercano);
                poblacion.printPromToFile(problema+"/dominanciaNsgavsNsga",dominanciaNsgavsNsga);
              poblacion.printPromToFile(problema+"/hvspea",HV);
        for (int i = 0; i < corrida; i++) {
            System.out.println("Dominancia Spea");
            int count = 0;
            int count2 = 0;
            for (int j = 0; j < corrida; j++) {
                if (dominanciaSpea[i][j] > dominanciaCercano[j][i]) {
                    count++;
                } else {
                    count2++;
                }

            }
            System.out.println(i + "Spea Domino a: " + count);
            System.out.println("Spea Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
            System.out.println("");
            System.out.println("HV de spea " + i + ": " + HV[0][i]);
            System.out.println("HV de cercano " + i + ": " + HV[1][i]);
        }
        System.out.println(total + " de los 10 dominan a cercano");
        System.out.println(" ");
        total = 0;
        for (int i = 0; i < corrida; i++) {
            System.out.println("Dominancia Cercano");
            int count = 0;
            int count2 = 0;
            for (int j = 0; j < corrida; j++) {
                if (dominanciaSpea[i][j] < dominanciaCercano[j][i]) {
                    count++;
                } else {
                    count2++;
                }

            }
            System.out.println(i + "Cercano Domino a: " + count);
            System.out.println("Cercano Me dominan:" + count2);
            System.out.println("");
            if (count > count2) {
                total++;
            }
        }
        System.out.println(total + " de cercano dominan a spea");
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

    public static void truePareto(String path,String pathPareto, int corridas) throws FileNotFoundException, IOException, WriteException, BiffException {
        double[][] pareto = null;
        FileOutputStream fos = new FileOutputStream(pathPareto);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);

        for (int i = 0; i < corridas; i++) {
            pareto = archivoAMatriz(path + i);
//            pareto = paretoNoDominado(pareto);
            for (int j = 0; j < pareto.length; j++) {
                 String f = "";
                 for (int h=0;h<pareto[j].length;h++){
                     f = f+pareto[j][h]+" ";
                 }
                bw.write(f);
                bw.newLine();
            }
        }
        /* Close the file */
        bw.close();

    }

    public static void promedioCorrida(String path,String pathPareto, int corridas) throws FileNotFoundException, IOException, WriteException, BiffException {
        double[][] pareto = null;
        FileOutputStream fos = new FileOutputStream(pathPareto);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        double[][] fin = null;
        for (int i = 0; i < corridas; i++) {

            pareto = archivoAMatriz(path + i);
              if (i==0){
                 fin = new double[pareto.length][pareto[0].length];
            }

            for (int j = 0; j < pareto.length; j++) {

                 for (int h=0;h<pareto[j].length;h++){
                     fin[j][h] += pareto[j][h];
                 }

            }
        }
        /* Close the file */
      for (int j = 0; j < fin.length; j++) {

                 for (int h=0;h<fin[j].length;h++){
                     fin[j][h] /= corridas;
                 }

            }
            poblacion.printPromToFile(pathPareto, fin);
    }
  public static double[][] paretoNoDominado(double[][] X) throws WriteException, BiffException {
        int xObj = 0;
        double xDomy = 0.0;
        double[] dominados = new double[X.length];
        double totalDom = 0.0;

        int cont=0;
        for (int i = 0; i < X.length; i++) {
            for (int k = 0; k < X.length; k++) {
                if (i!=k){
                for (int j = 0; j < X[0].length; j++) {
                    double x = X[i][j];
                    double y = X[k][j];
                    if (x <= y) {
                        xObj += 1;
                    }
                    if (xObj == X[0].length) {
                        int sacar = 0;
                        for (int h = 0; h < dominados.length; h++) {
                            if (k == dominados[h]) {
                                sacar = 1;
                            }
                        }
                        if (sacar == 0) {
                            dominados[cont] = k;
                            cont++;
                        }
                    }
                }
                xObj = 0;
                }
            }
        }
        double[][] noDominado = new double[X.length-cont][X[0].length];
         cont=0;
         for (int i = 0; i < X.length; i++) {

             int sacar=0;
              for (int j= 0; j< dominados.length; j++) {
            if (i==dominados[j])
                sacar=1;
        }
             if (sacar==0){
                 noDominado[cont]=X[i];
                 cont++;
             }
         }
   return noDominado;


    }

   public static void paretoFinal(String path,String pathPareto) throws FileNotFoundException, IOException, WriteException, BiffException {
        double[][] pareto = null;
        FileOutputStream fos = new FileOutputStream(pathPareto);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        for (int i = 1; i <= 3; i++) {

            pareto = archivoAMatriz(path + i);
            
            for (int j = 0; j < pareto.length; j++) {
               String f = "";
                 for (int h=0;h<pareto[j].length;h++){
                     f = f+pareto[j][h]+" ";
                 }
                bw.write(f);
                bw.newLine();
            }
        }
        /* Close the file */
        bw.close();

//        truePareto(pathPareto, pathPareto,1);

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
