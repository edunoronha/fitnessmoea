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
public class SetCoverageUnaVarios {
public static  SolutionSet poblacion;
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws FileNotFoundException, IOException, WriteException, BiffException {

        poblacion = new SolutionSet();
        int corrida =30;//Integer.parseInt(args[1]);
        String problema =  "fitness 5,7 sin especial/knapsackmuchos 6 850";//args[0];
        double[][] dominanciaSpeaElegido = new double[6][corrida];
        double[][] dominanciaCercanoElegido = new double[6][corrida];
         double[][] dominanciaSpeavsSpea = new double[corrida][corrida];
        double[][] dominanciaCercanovsCercano = new double[corrida][corrida];
        double[][] dominanciaNsgaElegido = new double[6][corrida];
        double[][] HV = new double[3][corrida];
        Hypervolume hyperVolumen = new Hypervolume();
       
            double[][] matrizSPEA;
            double[][] matrizSPEAanillos;
            double[][] matrizSPEASiguiente;
            double[][] matrizSPEAanterior;
            double[][] truepareto;
               double[][] matrizNSGA;
                double[][] matrizNSGAanterior;
//      

 matrizSPEA = archivoAMatriz(problema + "/SPEA2/FUNspea2_" +18);
   matrizNSGAanterior = archivoAMatriz(problema+"/NSGA2/FUNnsga2_"+13);
   matrizSPEAanterior = archivoAMatriz(problema+"/SPEAcercano/FUNspeacercano_"+9);
            for (int j = 0; j < corrida; j++) {
               
               
                  matrizSPEASiguiente = archivoAMatriz(problema + "/SPEA2/FUNspea2_" + j);
                    matrizNSGA = archivoAMatriz(problema+"/NSGA2/FUNnsga2_"+j);
                   matrizSPEAanillos = archivoAMatriz(problema+"/SPEAcercano/FUNspeacercano_"+j);
//                matrizSPEAanillos = archivoAMatriz("C:/tesis/" + problema + "/Speacercano/FUNspeacercano_" + j);
//                 matrizSPEAanterior = archivoAMatriz("C:/tesis/" + problema + "/Speacercano/FUNspeacercano_" + 0);
                int objetivos = objetivos("C:/tesis/" + problema + "/SPEA2/FUNspea2_" + 0);
                SetCoverageUnaVarios SC = new SetCoverageUnaVarios();
                dominanciaSpeaElegido[0][j] = SC.calcular(matrizSPEA, matrizNSGA, objetivos);
                dominanciaSpeaElegido[1][j] = SC.calcular(matrizNSGA,matrizSPEA, objetivos);
                dominanciaSpeaElegido[2][j] = SC.calcular(matrizSPEA,matrizSPEAanillos, objetivos);
                dominanciaSpeaElegido[3][j] = SC.calcular(matrizSPEAanillos,matrizSPEA, objetivos);
                dominanciaSpeaElegido[4][j] = SC.calcular(matrizSPEA,matrizSPEASiguiente, objetivos);
                dominanciaSpeaElegido[5][j] = SC.calcular(matrizSPEASiguiente,matrizSPEA, objetivos);
                dominanciaCercanoElegido[0][j] = SC.calcular(matrizSPEAanterior,matrizSPEASiguiente, objetivos);
                dominanciaCercanoElegido[1][j] = SC.calcular(matrizSPEASiguiente,matrizSPEAanterior, objetivos);
                dominanciaCercanoElegido[2][j] = SC.calcular(matrizSPEAanterior,matrizNSGA, objetivos);
                dominanciaCercanoElegido[3][j] = SC.calcular(matrizNSGA,matrizSPEAanterior, objetivos);
                dominanciaCercanoElegido[4][j] = SC.calcular(matrizSPEAanterior,matrizSPEAanillos, objetivos);
                dominanciaCercanoElegido[5][j] = SC.calcular(matrizSPEAanillos,matrizSPEAanterior, objetivos);

                dominanciaNsgaElegido[0][j] = SC.calcular(matrizNSGAanterior, matrizSPEASiguiente, objetivos);
                dominanciaNsgaElegido[1][j] = SC.calcular(matrizSPEASiguiente,matrizNSGAanterior, objetivos);
                dominanciaNsgaElegido[2][j] = SC.calcular(matrizNSGAanterior,matrizSPEAanillos, objetivos);
                dominanciaNsgaElegido[3][j] = SC.calcular(matrizSPEAanillos,matrizNSGAanterior, objetivos);
                dominanciaNsgaElegido[4][j] = SC.calcular(matrizNSGAanterior,matrizNSGA, objetivos);
                dominanciaNsgaElegido[5][j] = SC.calcular(matrizNSGA,matrizNSGAanterior, objetivos);
//                 dominanciaSpeavsSpea[0][j] = SC.calcular(matrizSPEA, matrizSPEASiguiente, objetivos);
//                dominanciaCercanovsCercano[0][j] = SC.calcular(matrizSPEAanterior, matrizSPEAanillos, objetivos);
//                dominanciaNsgavsNsga[0][j] = SC.calcular(matrizNSGAanterior,matrizNSGA, objetivos);
             
//      
            }

        
        int total = 0;
              poblacion.printPromToFile(problema+"/dominanciaSPEA",dominanciaSpeaElegido);
              poblacion.printPromToFile(problema+"/dominanciaNSGA",dominanciaNsgaElegido);
              poblacion.printPromToFile(problema+"/dominanciaCercanodos",dominanciaCercanoElegido);
   
           
       
            System.out.println("Dominancia Spea");
            int count = 0;
            int count2 = 0;
          for (int j = 0; j < corrida; j++) {
            if (dominanciaSpeaElegido[0][j] == dominanciaSpeaElegido[1][j]) {

            } else {
                if (dominanciaSpeaElegido[0][j] > dominanciaSpeaElegido[1][j]) {
                   
                        count++;

                } else {
                    count2++;
                }
            }
            }
            System.out.println("Spea Domino a nsga: " + count);
            System.out.println("Spea Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
             count = 0;
             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                if (dominanciaSpeaElegido[2][j] == dominanciaSpeaElegido[3][j]) {

            } else {
                if (dominanciaSpeaElegido[2][j] > dominanciaSpeaElegido[3][j]) {
                   
                    count++;
                } else {
                    count2++;
                }
                }
            }
            System.out.println("Spea Domino a afac: " + count);
            System.out.println("Spea Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
            count = 0;
             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                if (dominanciaSpeaElegido[4][j] == dominanciaSpeaElegido[5][j]) {
                      if (dominanciaSpeaElegido[4][j]!=0) count++;
            } else {
                if (dominanciaSpeaElegido[4][j] >= dominanciaSpeaElegido[5][j]) {
                  

                    count++;
                } else {
                    count2++;
                }}

            }
            System.out.println("Spea Domino a spea: " + count);
            System.out.println("Spea Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
        System.out.println("");
        System.out.println("Dominancia NSGA");
             count = 0;
             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                if (dominanciaNsgaElegido[0][j] == dominanciaNsgaElegido[1][j]) {
            } else {
                if (dominanciaNsgaElegido[0][j] >= dominanciaNsgaElegido[1][j]) {
                                     
                    count++;
                } else {
                    count2++;
                }
                }
            }
            System.out.println("nsga Domino a: " + count);
            System.out.println("nsga Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
              count = 0;
             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                 if (dominanciaNsgaElegido[2][j] == dominanciaNsgaElegido[3][j]) {
            } else {
                if (dominanciaNsgaElegido[2][j] >= dominanciaNsgaElegido[3][j]) {
                   count++;
                } else {
                    count2++;
                }
                }
            }
            System.out.println("nsga Domino a afac: " + count);
            System.out.println("nsga Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
             count = 0;
             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                 if (dominanciaNsgaElegido[4][j] == dominanciaNsgaElegido[5][j]) {
                       if (dominanciaNsgaElegido[4][j]!=0) count++;
            } else {
                if (dominanciaNsgaElegido[4][j] >= dominanciaNsgaElegido[5][j]) {
                    count++;
                } else {
                    count2++;
                }
                }
            }
            System.out.println("nsga Domino a nsga: " + count);
            System.out.println("nsga Me dominan:" + count2);
               System.out.println("");
        System.out.println("Dominancia AFAC");
            count = 0;
             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                 if (dominanciaCercanoElegido[0][j] == dominanciaCercanoElegido[1][j]) {
            } else {
                if (dominanciaCercanoElegido[0][j] >= dominanciaCercanoElegido[1][j]) {
                     count++;
                } else {
                    count2++;
                }
                }
            }
            System.out.println("afac Domino a spea: " + count);
            System.out.println("afac Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
        count = 0;

             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                if (dominanciaCercanoElegido[2][j] == dominanciaCercanoElegido[3][j]) {
            } else {
                if (dominanciaCercanoElegido[2][j] >= dominanciaCercanoElegido[3][j]) {
                   count++;
                } else {
                    count2++;
                }
                }
            }
            System.out.println("afac Domino a nsga: " + count);
            System.out.println("nsga Me dominan:" + count2);
             count = 0;

             count2 = 0;
            for (int j = 0; j < corrida; j++) {
                if (dominanciaCercanoElegido[4][j] == dominanciaCercanoElegido[5][j]) {
                       if (dominanciaCercanoElegido[4][j]!=0) count++;
            } else {
                if (dominanciaCercanoElegido[4][j] >= dominanciaCercanoElegido[5][j]) {
                    count++;
                } else {
                     count2++;
                }
                }
            }
            System.out.println("afac Domino a afac: " + count);
            System.out.println("afac Me dominan:" + count2);
            if (count > count2) {
                total++;
            }
//        System.out.println(total + " de los 10 dominan a cercano");
//        System.out.println(" ");
//        total = 0;
//        for (int i = 0; i < corrida; i++) {
//            System.out.println("Dominancia Cercano");
//            int count = 0;
//            int count2 = 0;
//            for (int j = 0; j < corrida; j++) {
//                if (dominanciaSpea[i][j] < dominanciaCercano[j][i]) {
//                    count++;
//                } else {
//                    count2++;
//                }
//
//            }
//            System.out.println(i + "Cercano Domino a: " + count);
//            System.out.println("Cercano Me dominan:" + count2);
//            System.out.println("");
//            if (count > count2) {
//                total++;
//            }
//        }
//        System.out.println(total + " de cercano dominan a spea");
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
        FileOutputStream fos = new FileOutputStream(pathPareto+0);
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
