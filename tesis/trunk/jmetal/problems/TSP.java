/**
 * TSP.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.problems;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.PermutationSolutionType;
import jmetal.base.variable.Binary;
import jmetal.base.variable.Permutation;

import java.io.*;

/**
 * Class representing a TSP (Traveling Salesman Problem) problem.
 */
public class TSP extends Problem {

    public int numberOfCities_ = 12;
    public double ciudadInicio_;
    double[][] matrizDistancia_ = new double[numberOfCities_][numberOfCities_];
    double[][] matrizGasolina_ = new double[numberOfCities_][numberOfCities_];
    double[][] matrizX_ = new double[numberOfCities_][numberOfCities_];
    /**
     * Creates a new TSP problem instance. It accepts data files from TSPLIB
     * @param filename The file containing the definition of the problem
     */
    public TSP(double ciudadInicio, int numeroDeObjetivos) throws FileNotFoundException, IOException, ClassNotFoundException {

        numberOfVariables_ = 1;
        numberOfObjectives_ = numeroDeObjetivos;
        numberOfConstraints_ = 0;
        ciudadInicio_ = ciudadInicio;
        problemName_ = "TSP";
        readProblem("C:/tesis/Matriz Distancia.txt", matrizDistancia_);
        readProblem("C:/tesis/Matriz Gasolina.txt", matrizGasolina_);
        readProblem("C:/tesis/Matriz X.txt", matrizX_);

        solutionType_ = new PermutationSolutionType(this);
        variableType_ = new Class[numberOfVariables_];
        length_ = new int[1];
        variableType_[0] = Class.forName("jmetal.base.variable.Permutation");
        length_[0] = numberOfCities_;


    } // TSP

    public void evaluate(Solution solution) {
        int[] ciudades = new int[numberOfCities_];
        int posicionInicial = 0;
        for (int i = 0; i < ((Permutation) solution.getDecisionVariables()[0]).vector_.length; i++) {
            if (((Permutation) solution.getDecisionVariables()[0]).vector_[i] == ciudadInicio_) {
                posicionInicial = i;
            }
        }
        ciudades[0] = ((Permutation) solution.getDecisionVariables()[0]).vector_[posicionInicial];
        int contadorCiudades = 0;
        for (int i = 1; i < ((Permutation) solution.getDecisionVariables()[0]).vector_.length; i++) {
            if (posicionInicial + i < ciudades.length) {
                ciudades[i] = ((Permutation) solution.getDecisionVariables()[0]).vector_[posicionInicial + i];
            } else {
                ciudades[i] = ((Permutation) solution.getDecisionVariables()[0]).vector_[contadorCiudades];
                contadorCiudades++;
            }
        }
        double distanciaRecorrida = 0;
        double gasolinaGastada = 0;
        double xCosa = 0;
        for (int i = 0; i < ciudades.length; i++) {
            if ((i + 1) < ciudades.length) {
                distanciaRecorrida += matrizDistancia_[ciudades[i]][ciudades[i + 1]];
                gasolinaGastada += matrizGasolina_[ciudades[i]][ciudades[i + 1]];
            switch(numberOfObjectives_){
                case 3:
                    xCosa += matrizX_[ciudades[i]][ciudades[i + 1]];
                }
            } else {
                distanciaRecorrida += matrizDistancia_[ciudades[i]][ciudades[0]];
                gasolinaGastada += matrizGasolina_[ciudades[i]][ciudades[0]];
                switch(numberOfObjectives_){
                case 3:
                    xCosa += matrizX_[ciudades[i]][ciudades[0]];
                }
            }
        }
        solution.setObjective(0, distanciaRecorrida);
        solution.setObjective(1, gasolinaGastada);
        solution.setObjective(2, xCosa);
    } // evaluate

    public static void readProblem(String fileName, double[][] matriz) throws FileNotFoundException, IOException {
        int fila = 0;
        String linea = "";
        FileInputStream fis = new FileInputStream(fileName);
        InputStreamReader isw = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isw);
        try {
            while (br.ready()) {
                linea = br.readLine();
                String[] filanumeros = linea.split(" ");
                String[] numeros = filanumeros[0].split(",");
                for (int i = 0; i < numeros.length; i++) {
                    matriz[fila][i] = Integer.parseInt(numeros[i].toString());
                }
                fila++;

            }
        } catch (Exception e) {
            System.err.println("TSP.readProblem(): error when reading data file " + e);
            System.exit(1);
        }
    }
} // TSP

