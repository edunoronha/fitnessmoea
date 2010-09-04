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

    public static int numberOfCities_ = 12;
    public double ciudadInicio_;
    private double[][] matrizDistancia_;
    private double[][] matrizGasolina_;
    private double[][] matrizX1;
    private double[][] matrizX2;
    private double[][] matrizX3;

    public TSP(double ciudadInicio, int numeroDeObjetivos) throws FileNotFoundException, IOException, ClassNotFoundException {

        numberOfVariables_ = 1;
        numberOfObjectives_ = numeroDeObjetivos;
        numberOfConstraints_ = 0;
        ciudadInicio_ = ciudadInicio;
        problemName_ = "TSP";
        matrizDistancia_ = readProblem("C:/tesis/TSP porblems/Distancia.tsp", matrizDistancia_);
        matrizGasolina_ = readProblem("C:/tesis/TSP porblems/Gasolina.tsp", matrizGasolina_);
        matrizX1 = readProblem("C:/tesis/TSP porblems/MatrizX1.tsp", matrizX1);
        matrizX2 = readProblem("C:/tesis/TSP porblems/MatrizX2.tsp", matrizX2);
        matrizX3 = readProblem("C:/tesis/TSP porblems/MatrizX3.tsp", matrizX3);

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
        double xCosa2 = 0;
        double xCosa3 = 0;
        for (int i = 0; i < ciudades.length; i++) {
            if ((i + 1) < ciudades.length) {
                distanciaRecorrida += matrizDistancia_[ciudades[i]][ciudades[i + 1]];
                gasolinaGastada += matrizGasolina_[ciudades[i]][ciudades[i + 1]];
                if (numberOfObjectives_ == 3) {
                    xCosa += matrizX1[ciudades[i]][ciudades[i + 1]];
                } else if (numberOfObjectives_ == 4) {
                    xCosa += matrizX1[ciudades[i]][ciudades[i + 1]];
                    xCosa2 += matrizX2[ciudades[i]][ciudades[i + 1]];
                } else if (numberOfObjectives_ == 5) {
                    xCosa += matrizX1[ciudades[i]][ciudades[i + 1]];
                    xCosa2 += matrizX2[ciudades[i]][ciudades[i + 1]];
                    xCosa3 += matrizX3[ciudades[i]][ciudades[i + 1]];
                }
            } else {
                distanciaRecorrida += matrizDistancia_[ciudades[i]][ciudades[0]];
                gasolinaGastada += matrizGasolina_[ciudades[i]][ciudades[0]];
                if (numberOfObjectives_ == 3) {
                    xCosa += matrizX1[ciudades[i]][ciudades[0]];
                } else if (numberOfObjectives_ == 4) {
                    xCosa += matrizX1[ciudades[i]][ciudades[0]];
                    xCosa2 += matrizX2[ciudades[i]][ciudades[0]];
                } else if (numberOfObjectives_ == 5) {
                    xCosa += matrizX1[ciudades[i]][ciudades[0]];
                    xCosa2 += matrizX2[ciudades[i]][ciudades[0]];
                    xCosa3 += matrizX3[ciudades[i]][ciudades[0]];
                }
            }
        }
        solution.setObjective(0, gasolinaGastada);
        solution.setObjective(1, distanciaRecorrida);
        switch (numberOfObjectives_) {
            case 3:
                solution.setObjective(2, xCosa);
                break;
            case 4:
                solution.setObjective(2, xCosa);
                solution.setObjective(3, xCosa2);
                break;
            case 5:
                solution.setObjective(2, xCosa);
                solution.setObjective(3, xCosa2);
                solution.setObjective(4, xCosa3);
                break;
        }
    } // evaluate

    private double[][] readProblem(String fileName, double[][] matriz) throws FileNotFoundException, IOException {

        Reader inputFile = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(fileName)));
        StreamTokenizer token = new StreamTokenizer(inputFile);




        try {
            boolean found;
            found = false;
            token.nextToken();




            while (!found) {
                if ((token.sval != null) && ((token.sval.compareTo("DIMENSION") == 0))) {
                    found = true;




                } else {
                    token.nextToken();




                }
            } // while
            token.nextToken();
            token.nextToken();
            numberOfCities_ = (int) token.nval;
            matriz = new double[numberOfCities_][numberOfCities_];
            found = false;
            token.nextToken();




            while (!found) {
                if ((token.sval != null)
                        && ((token.sval.compareTo("SECTION") == 0))) {
                    found = true;




                } else {
                    token.nextToken();




                }
            }
            double[] c = new double[2 * numberOfCities_];




            for (int i = 0; i
                    < numberOfCities_; i++) {
                token.nextToken();




                int j = (int) token.nval;
                token.nextToken();
                c[

2 * (j - 1)] = token.nval;
                token.nextToken();
                c[

2 * (j - 1) + 1] = token.nval;




            } // for
            double dist;




            for (int k = 0; k
                    < numberOfCities_; k++) {
                matriz[k][k] = 0;




                for (int j = k + 1; j
                        < numberOfCities_; j++) {
                    dist = Math.sqrt(Math.pow((c[k * 2] - c[j * 2]), 2.0)
                            + Math.pow((c[k * 2 + 1] - c[j * 2 + 1]), 2));
                    dist = (int) (dist + .5);
                    matriz[k][j] = dist;
                    matriz[j][k] = dist;




                } // for
            } // for
        } // try
        catch (Exception e) {
            System.err.println("TSP.readProblem(): error when reading data file " + e);
            System.exit(1);




        } // catch
        return matriz;


    }
}
