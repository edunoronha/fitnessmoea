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
 import jmetal.base.variable.Real;
 import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

 public class KnapsackMuchos extends Problem {

     double[] VarValor1 = {17,   17,     21,     20,     11,     24,     16,
 8,      22,     1,      10,     24,     10,     13,
 7,      1,      17,     16,     3,      10,     2,
 12,     14,     3,      13,     2,      19,     13,
 12,     9,      15,     15,     2,      13,     14,
 12,     15,     10,     22,     18,     12,     24,
 1,      16,     21,     22,     8,      17,     19,
 19,     7,      6,      2,      19,     20,     24,
 2,      5,      17,     3,      17,     18,     8,
 7,      16,     13,     25,     23,     12,     19
 };

     double[] VarValor2 = {3,    12,     17,     8,      25,     3,      20,
 20,     20,     1,      2,      20,     22,     9,
 6,      2,      9,      22,     16,     8,      8,
 9,      1,      3,      18,     23,     9,      2,
 25,     14,     15,     16,     19,     4,      8,
 2,      12,     17,     15,     10,     22,     4,
 4,      21,     5,      18,     14,     18,     11,
 2,      7,      8,      16,     1,      15,     15,
 11,     7,      20,     14,     14,     15,     15,
 5,      16,     3,      18,     18,     2,      25
 };

     double[] VarValor3 = {22,   24,     8,      16,     25,     9,      12,
 17,     2,      10,     17,     25,     12,     18,
 11,     19,     11,     6,      22,     6,      8,
 8,      19,     3,      6,      14,     3,      2,
 10,     1,      19,     6,      13,     10,     5,
 1,      7,      18,     8,      11,     5,      5,
 18,     6,      7,      8,      4,      24,     3,
 10,     22,     23,     6,      9,      9,      8,
 14,     20,     21,     18,     18,     23,     20,
 22,     9,      8,      21,     15,     2,      24
 };

     double[] VarValor4 = {13,   4,      1,      8,      21,     23,     3,
 23,     10,     10,     16,     4,      7,      12,
 19,     14,     18,     19,     21,     10,     18,
 5,      1,      15,     1,      7,      18,     25,
 10,     24,     3,      9,      10,     1,      8,
 4,      9,      4,      2,      21,     2,      20,
 21,     19,     13,     19,     12,     6,      1,
 13,     14,     1,      13,     17,     24,     16,
 11,     24,     5,      24,     9,      22,     19,
 25,     8,      2,      25,     8,      15,     10
 };

     double[] VarValor5 = {6,    22,     2,      13,     16,     14,     25,
 23,     7,      11,     7,      5,      23,     24,
 24,     21,     14,     12,     2,      19,     7,
 19,     23,     15,     15,     16,     15,     21,
 9,      3,      11,     2,      22,     8,      24,
 12,     9,      1,      10,     20,     19,     11,
 19,     9,      9,      8,      11,     8,      1,
 16,     11,     17,     7,      18,     15,     1,
 23,     10,     24,     5,      15,     16,     11,
 25,     21,     17,     24,     15,     20,     18
 };

     double[] VarPeso = {6,      7,      2,      14,     4,      21,     10,
 14,     15,     22,     16,     24,     17,     12,
 23,     5,      9,      11,     9,      9,      12,
 21,     9,      7,      24,     21,     20,     22,
 16,     6,      13,     5,      23,     10,     15,
 25,     15,     15,     15,     11,     3,      10,
 2,      20,     6,      6,      20,     20,     18,
 2,      9,      13,     14,     24,     14,     21,
 5,      14,     2,      6,      15,     2,      21,
 9,      21,     9,      4,      18,     4,      4
 };
     double maxCapacidad1 = 800.0;
     double maxCapacidad2 = 740.0;
     double maxCapacidad3 = 1010.0;
     double maxCapacidad4 = 960.0;
     double maxCapacidad5 = 750.0;
    private double[][] valores;

     public KnapsackMuchos(String solutionType, Integer numberOfObj) throws ClassNotFoundException {

         numberOfVariables_ = 200;
         numberOfObjectives_ = numberOfObj;
         numberOfConstraints_ = numberOfObj;
         problemName_ = "Knapsack";
        valores = new double[numberOfObj+1][numberOfVariables_];
         lowerLimit_ = new double[numberOfVariables_];
         upperLimit_ = new double[numberOfVariables_];

         for (int var = 0; var < numberOfVariables_; var++) {
             lowerLimit_[var] = 0.0;
             upperLimit_[var] = numberOfObjectives_;
         }
  for (int i =0;i<numberOfObj+1;i++){

         for (int j=0;j<numberOfVariables_;j++){
            valores[i][j]=  PseudoRandom.randInt(1, 50) ;

         }
     }
         if (solutionType.compareTo("Real") == 0) {
             solutionType_ = new IntSolutionType(this);
         } else {
             System.out.println("Error: solution type " + solutionType + " invalid");
             System.exit(-1);
         }
     }

     @Override
     public void evaluate(Solution solution) throws JMException {
         Variable[] variables = solution.getDecisionVariables();
         double[] Peso = new double[numberOfObjectives_];
         double[] mochila = new double[numberOfObjectives_]; // function values
         for (int var = 0; var < numberOfVariables_; var++) {
             if ((variables[var].getValue() == 1) && (mochila.length >= 1)) {
                 mochila[0] = mochila[0] + valores[1][var];
             }
             if ((variables[var].getValue() == 2) && (mochila.length >= 2)) {
                 mochila[1] = mochila[1] + valores[2][var];
             }
             if ((variables[var].getValue() == 3) && (mochila.length >= 3)) {
                 mochila[2] = mochila[2] + valores[3][var];
             }
             if ((variables[var].getValue() == 4) && (mochila.length >= 4)) {
                 mochila[3] = mochila[3] + valores[4][var];
             }
             if ((variables[var].getValue() == 5) && (mochila.length >= 5)) {
                 mochila[4] = mochila[4] + valores[5][var];
             }
         } // for
         for (int i = 0; i < numberOfObjectives_; i++) {
             if (i == 0) {
                 solution.setObjective(0, (-1) * mochila[0]);
             }
             if (i == 1) {
                 solution.setObjective(1, (-1) * mochila[1]);
             }
             if (i == 2) {
                 solution.setObjective(2, (-1) * mochila[2]);
             }
             if (i == 3) {
                 solution.setObjective(3, (-1) * mochila[3]);
             }
             if (i == 4) {
                 solution.setObjective(4, (-1) * mochila[4]);
             }
         }
     }

     @Override
     public void evaluateConstraints(Solution solution) throws JMException {
         Variable[] variables = solution.getDecisionVariables();
         int number = 0;
         double total = 0.0;
         double[] pesos = new double[numberOfConstraints_];
         double[] constraint = new double[numberOfConstraints_];
         if (numberOfObjectives_ >= 1) {
             for (int var = 0; var < this.numberOfVariables_; var++) {
                 if (variables[var].getValue() == 1) {
                     pesos[0] = pesos[0] + valores[0][var];
                     if (pesos[0] > this.maxCapacidad1) {
                         constraint[0] = pesos[0] - maxCapacidad1;
                     }
                 }
             }
         }
         if (numberOfObjectives_ >= 2) {
             for (int var = 0; var < this.numberOfVariables_; var++) {
                 if (variables[var].getValue() == 2) {
                     pesos[1] = pesos[1] +valores[0][var];
                     if (pesos[1] > this.maxCapacidad2) {
                         constraint[1] = pesos[1] - maxCapacidad2;
                     }
                 }
             }
         }
         if (numberOfObjectives_ >= 3) {
             for (int var = 0; var < this.numberOfVariables_; var++) {
                 if (variables[var].getValue() == 3) {
                     pesos[2] = pesos[2] + valores[0][var];
                     if (pesos[2] > this.maxCapacidad3) {
                         constraint[2] = pesos[2] - maxCapacidad3;
                     }
                 }
             }
         }
         if (numberOfObjectives_ >= 4) {
             for (int var = 0; var < this.numberOfVariables_; var++) {
                 if (variables[var].getValue() == 4) {
                     pesos[3] = pesos[3] + valores[0][var];
                     if (pesos[3] > this.maxCapacidad4) {
                         constraint[3] = pesos[3] - maxCapacidad4;
                     }
                 }
             }
         }
         if (numberOfObjectives_ >= 5) {
             for (int var = 0; var < this.numberOfVariables_; var++) {
                 if (variables[var].getValue() == 5) {
                     pesos[4] = pesos[4] + valores[0][var];
                     if (pesos[4] > this.maxCapacidad5) {
                         constraint[4] = pesos[4] - maxCapacidad5;
                     }
                 }
             }
         }
         for (int i = 0; i < this.numberOfConstraints_; i++) {
             if (constraint[i] > 0.0) {
                 number++;
                 total += constraint[i];
             }
         }

         solution.setOverallConstraintViolation((-1) * total);
         solution.setNumberOfViolatedConstraint(number);
     }
 }
