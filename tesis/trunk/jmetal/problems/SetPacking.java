/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.problems;

import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.*;
import jmetal.base.solutionType.ArrayIntSolutionType;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.base.solutionType.ArrayRealSolutionType;
import jmetal.base.solutionType.BinarySolutionType;
import jmetal.base.solutionType.IntRealSolutionType;
import jmetal.base.solutionType.IntSolutionType;
import jmetal.base.solutionType.PermutationSolutionType;
import jmetal.base.variable.Binary;
import jmetal.base.variable.Int;
import jmetal.base.variable.Real;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class SetPacking extends Problem {

    double[][][] valores;
    double[][] beneficios;
    double[] VarValor1 = {17, 17, 21, 20, 11, 24, 16,
        8, 22, 1, 10, 24, 10, 13,
        7, 1, 17, 16, 3, 10, 2,
        12, 14, 3, 13, 2, 19, 13,
        12, 9, 15, 15, 2, 13, 14,
        12, 15, 10, 22, 18, 12, 24,
        1, 16, 21, 22, 8, 17, 19,
        19, 7, 6, 2, 19, 20, 24,
        2, 5, 17, 3, 17, 18, 8,
        7, 16, 13, 25, 23, 12, 19
    };
    double[] VarValor2 = {3, 12, 17, 8, 25, 3, 20,
        20, 20, 1, 2, 20, 22, 9,
        6, 2, 9, 22, 16, 8, 8,
        9, 1, 3, 18, 23, 9, 2,
        25, 14, 15, 16, 19, 4, 8,
        2, 12, 17, 15, 10, 22, 4,
        4, 21, 5, 18, 14, 18, 11,
        2, 7, 8, 16, 1, 15, 15,
        11, 7, 20, 14, 14, 15, 15,
        5, 16, 3, 18, 18, 2, 25
    };
    double[] VarValor3 = {22, 24, 8, 16, 25, 9, 12,
        17, 2, 10, 17, 25, 12, 18,
        11, 19, 11, 6, 22, 6, 8,
        8, 19, 3, 6, 14, 3, 2,
        10, 1, 19, 6, 13, 10, 5,
        1, 7, 18, 8, 11, 5, 5,
        18, 6, 7, 8, 4, 24, 3,
        10, 22, 23, 6, 9, 9, 8,
        14, 20, 21, 18, 18, 23, 20,
        22, 9, 8, 21, 15, 2, 24
    };
    double[] VarValor4 = {13, 4, 1, 8, 21, 23, 3,
        23, 10, 10, 16, 4, 7, 12,
        19, 14, 18, 19, 21, 10, 18,
        5, 1, 15, 1, 7, 18, 25,
        10, 24, 3, 9, 10, 1, 8,
        4, 9, 4, 2, 21, 2, 20,
        21, 19, 13, 19, 12, 6, 1,
        13, 14, 1, 13, 17, 24, 16,
        11, 24, 5, 24, 9, 22, 19,
        25, 8, 2, 25, 8, 15, 10
    };
    double[] VarValor5 = {6, 22, 2, 13, 16, 14, 25,
        23, 7, 11, 7, 5, 23, 24,
        24, 21, 14, 12, 2, 19, 7,
        19, 23, 15, 15, 16, 15, 21,
        9, 3, 11, 2, 22, 8, 24,
        12, 9, 1, 10, 20, 19, 11,
        19, 9, 9, 8, 11, 8, 1,
        16, 11, 17, 7, 18, 15, 1,
        23, 10, 24, 5, 15, 16, 11,
        25, 21, 17, 24, 15, 20, 18
    };
    double[] VarPeso;
    int maxCapacidad1 = 19;
    int maxCapacidad2 = 23;
    int maxCapacidad3 = 32;
    int maxCapacidad4 = 18;
    int maxCapacidad5 = 19;
    int maxCapacidad6 = 30;
    int[][] subGrupo;

    public void llenarValores() {
    }

    public SetPacking(Integer numberOfObj) throws ClassNotFoundException {
        numberOfVariables_ = 500;
        numberOfObjectives_ = numberOfObj+1;
        valores = new double[numberOfObj][numberOfVariables_][PseudoRandom.randInt(4, 10)];
        beneficios = new double[numberOfObj][valores[0][0].length];
        subGrupo = new int[numberOfVariables_ / 2][];
        for (int i = 0; i < numberOfVariables_; i++) {
   for (int objetivos=0;objetivos<numberOfObj;objetivos++){
            for (int j = 0; j < valores[0][objetivos].length; j++) {
                valores[objetivos][i][j] = PseudoRandom.randInt(0, 1);
       }
            }
        }

        problemName_ = "SetPacking";

        lowerLimit_ = new double[numberOfVariables_];
        upperLimit_ = new double[numberOfVariables_];

        for (int var = 0; var < numberOfVariables_; var++) {
            lowerLimit_[var] = 0;
            upperLimit_[var] = (numberOfVariables_ / 2)-1;
        }

        solutionType_ = new IntSolutionType(this);

    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();
        // function values
        for (int obj = 0; obj < subGrupo.length; obj++) {

            int contGrupo = 0;
            for (int var = 0; var < numberOfVariables_; var++) {
                if ((variables[var].getValue() == obj)) {
                    contGrupo++;


                }

            }

            int[] sub = new int[contGrupo]; // function values
            contGrupo = 0;
            for (int var = 0; var < numberOfVariables_; var++) {
                if ((variables[var].getValue() == obj)) {

                    sub[contGrupo] = var;
                    contGrupo++;
                }

            }
            subGrupo[obj] = sub;

//        for (int recorrerSubGrupo=0;recorrerSubGrupo<numberOfVariables_-1;recorrerSubGrupo++){
//
//             for (int i=0; i<valores[var].length;i++){
//                valores[var][i]
//                }
//            if (valores[i][recorrerSubGrupo]=
//        }

        }
        // contar miembors y ocurrencias de subgrupos
        int[] contCoincidencias = new int[solution.numberOfObjectives()-1];


        int gruposValidos=0;
        for (int i = 0; i < subGrupo.length; i++) {
            if (subGrupo[i].length > 1) {
                 gruposValidos++;
                for (int j = 0; j < subGrupo[i].length; j++) {

                    for (int h = j + 1; h < subGrupo[i].length; h++) {
                        for (int objetivos=0;objetivos<solution.numberOfObjectives()-1;objetivos++){


                        for (int compararCaracteristicas = 0; compararCaracteristicas < valores[0][0].length; compararCaracteristicas++) {
                            int a = subGrupo[i][j];
                            int b = subGrupo[i][h];
                            double c= valores[objetivos][a][compararCaracteristicas];
                            double d= valores[objetivos][b][compararCaracteristicas];
                            if ((c == d)&&(c==1)) {

                                contCoincidencias[objetivos]+=(1);
                            }



                        }
                        }
                    }
                }
            }




        }
        
        for (int i =0 ; i<numberOfObjectives_-1;i++){


       
        solution.setObjective(i, contCoincidencias[i]);
        }
         solution.setObjective(numberOfObjectives_-1, (-1)* gruposValidos);
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
//        int constraints = 0;
//        for (int i = 0; i < subGrupo.length; i++) {
//            if (subGrupo[i].length == 1) {
//                constraints++;
//            }
//        }
//        solution.setOverallConstraintViolation(constraints);
//        solution.setNumberOfViolatedConstraint(1);//por ahora 1, al agregar demas caracteristicas aumenta
    }
}
