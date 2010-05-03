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

public class KnapsackMuchos extends Problem {

    double[] VarValor1 = {2, 8, 6, 12, 4, 7, 1, 3, 5, 14,
        15, 8, 1, 16, 5, 16, 11, 5, 2, 19};
    double[] VarValor2 = {3, 1, 7, 11, 4, 5, 2, 6, 8, 13,
        10, 9, 13, 20, 12, 12, 19, 1, 3, 19};
    double[] VarValor3 = {13, 20, 12, 3, 12, 19, 1, 7, 2, 6, 8, 13,
        10, 9, 3, 19, 11, 4, 5, 1,};
    double[] VarValor4 = {19, 1, 3, 19, 11, 4, 13, 13, 20, 12,
        10, 9, 5, 2, 3, 1, 7, 6, 8, 12};
    double[] VarValor5 = {5, 2, 6, 8, 11, 4, 1, 3, 13, 1,
        7, 19, 9, 13, 20, 10, 3, 12, 12, 19};
    double[] VarPeso = {4, 3, 6, 11, 2, 10, 1, 7, 9, 12,
        5, 13, 20, 16, 4, 16, 4, 14, 19, 10};
    double maxCapacidad1 = 50.0;
    double maxCapacidad2 = 44.0;
    double maxCapacidad3 = 49.0;
    double maxCapacidad4 = 55.0;
    double maxCapacidad5 = 33.0;

    public KnapsackMuchos(String solutionType, Integer numberOfObj) throws ClassNotFoundException {

        numberOfVariables_ = 20;
        numberOfObjectives_ = numberOfObj;
        numberOfConstraints_ = numberOfObj;
        problemName_ = "Knapsack";

        lowerLimit_ = new double[numberOfVariables_];
        upperLimit_ = new double[numberOfVariables_];

        for (int var = 0; var < numberOfVariables_; var++) {
            lowerLimit_[var] = 0.0;
            upperLimit_[var] = numberOfObjectives_;
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
                mochila[0] = mochila[0] + VarValor1[var];
            }
            if ((variables[var].getValue() == 2) && (mochila.length >= 2)) {
                mochila[1] = mochila[1] + VarValor2[var];
            }
            if ((variables[var].getValue() == 3) && (mochila.length >= 3)) {
                mochila[2] = mochila[2] + VarValor3[var];
            }
            if ((variables[var].getValue() == 4) && (mochila.length >= 4)) {
                mochila[3] = mochila[3] + VarValor4[var];
            }
            if ((variables[var].getValue() == 5) && (mochila.length >= 5)) {
                mochila[4] = mochila[4] + VarValor5[var];
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
                    pesos[0] = pesos[0] + VarPeso[var];
                    if (pesos[0] > this.maxCapacidad1) {
                        constraint[0] = pesos[0] - maxCapacidad1;
                    }
                }
            }
        }
        if (numberOfObjectives_ >= 2) {
            for (int var = 0; var < this.numberOfVariables_; var++) {
                if (variables[var].getValue() == 2) {
                    pesos[1] = pesos[1] + VarPeso[var];
                    if (pesos[1] > this.maxCapacidad2) {
                        constraint[1] = pesos[1] - maxCapacidad2;
                    }
                }
            }
        }
        if (numberOfObjectives_ >= 3) {
            for (int var = 0; var < this.numberOfVariables_; var++) {
                if (variables[var].getValue() == 3) {
                    pesos[2] = pesos[2] + VarPeso[var];
                    if (pesos[2] > this.maxCapacidad3) {
                        constraint[2] = pesos[2] - maxCapacidad3;
                    }
                }
            }
        }
        if (numberOfObjectives_ >= 4) {
            for (int var = 0; var < this.numberOfVariables_; var++) {
                if (variables[var].getValue() == 4) {
                    pesos[3] = pesos[3] + VarPeso[var];
                    if (pesos[3] > this.maxCapacidad4) {
                        constraint[3] = pesos[3] - maxCapacidad4;
                    }
                }
            }
        }
        if (numberOfObjectives_ >= 5) {
            for (int var = 0; var < this.numberOfVariables_; var++) {
                if (variables[var].getValue() == 5) {
                    pesos[4] = pesos[4] + VarPeso[var];
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
