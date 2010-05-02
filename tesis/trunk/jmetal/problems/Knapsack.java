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

public class Knapsack extends Problem {

    double[] VarValor1 = {2, 8, 6, 12, 4, 7, 1, 3, 5, 14,
                          15, 8, 1, 16, 5, 16, 11, 5, 2, 19};
    double[] VarValor2 = {3, 1, 7, 11, 4, 5, 2, 6, 8, 13,
                          10, 9, 13, 20, 12, 12, 19, 1, 3, 19};
    double[] VarPeso = {4, 3, 6, 11, 2, 10, 1, 7, 9, 12,
                        5, 13, 20, 16, 4, 16, 4, 14, 19, 10};
    double maxCapacidad1 = 50.0;
    double maxCapacidad2 = 44.0;

    public Knapsack(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {

        numberOfVariables_ = 20;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 2;
        problemName_ = "Knapsack";

        lowerLimit_ = new double[numberOfVariables_];
        upperLimit_ = new double[numberOfVariables_];

        for (int var = 0; var < numberOfVariables_; var++) {
            lowerLimit_[var] = 0.0;
            upperLimit_[var] = 2.0;
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
            double t = variables[var].getValue();
            if(variables[var].getValue()==1){
                mochila[0] = mochila[0] + VarValor1[var];
            }else if(variables[var].getValue() == 2){
                mochila[1] = mochila[1] + VarValor2[var];
            }
        } // for
        solution.setObjective(0, (-1) * mochila[0]);
        solution.setObjective(1, (-1) * mochila[1]);
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();
        int number = 0;
        double total = 0.0;
        double[] pesos = new double[numberOfConstraints_];
        double[] constraint = new double[numberOfConstraints_];

        for (int var = 0; var < this.numberOfVariables_; var++) {

            if (variables[var].getValue() == 1) {
                pesos[0] = pesos[0] + VarPeso[var];
                if (pesos[0] > this.maxCapacidad1) {
                    constraint[0] = pesos[0] - 50;
                }
            }
        }
        for (int var = 0; var < this.numberOfVariables_; var++) {
            if (variables[var].getValue()==2) {
                pesos[1] = pesos[1] + VarPeso[var];
                if (pesos[1] > this.maxCapacidad2) {
                    constraint[1] = pesos[1] - 44;
                }
            }
        }
        for (int i = 0; i < this.numberOfConstraints_; i++) {
            if (constraint[i] > 0.0) {
                number++;
                total +=constraint[i];
            }
        }
        solution.setOverallConstraintViolation((-1)*total);
        solution.setNumberOfViolatedConstraint(number);
    }
}
