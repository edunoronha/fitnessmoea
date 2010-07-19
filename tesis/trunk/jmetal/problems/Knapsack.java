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

double[] VarValor1 = {17,	17,	21,	20,	11,	24,	16,
8,	22,	1,	10,	24,	10,	13,
7,	1,	17,	16,	3,	10,	2,
12,	14,	3,	13,	2,	19,	13,
12,	9,	15,	15,	2,	13,	14,
12,	15,	10,	22,	18,	12,	24,
1,	16,	21,	22,	8,	17,	19,
19,	7,	6,	2,	19,	20,	24,
2,	5,	17,	3,	17,	18,	8,
7,	16,	13,	25,	23,	12,	19
};

    double[] VarValor2 = {3,	12,	17,	8,	25,	3,	20,
20,	20,	1,	2,	20,	22,	9,
6,	2,	9,	22,	16,	8,	8,
9,	1,	3,	18,	23,	9,	2,
25,	14,	15,	16,	19,	4,	8,
2,	12,	17,	15,	10,	22,	4,
4,	21,	5,	18,	14,	18,	11,
2,	7,	8,	16,	1,	15,	15,
11,	7,	20,	14,	14,	15,	15,
5,	16,	3,	18,	18,	2,	25
};
        
    double[] VarPeso = {6,	7,	2,	14,	4,	21,	10,
14,	15,	22,	16,	24,	17,	12,
23,	5,	9,	11,	9,	9,	12,
21,	9,	7,	24,	21,	20,	22,
16,	6,	13,	5,	23,	10,	15,
25,	15,	15,	15,	11,	3,	10,
2,	20,	6,	6,	20,	20,	18,
2,	9,	13,	14,	24,	14,	21,
5,	14,	2,	6,	15,	2,	21,
9,	21,	9,	4,	18,	4,	4
};
    
    double maxCapacidad1 = 450.0;
    double maxCapacidad2 = 390.0;

    public Knapsack(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {

        numberOfVariables_ = 70;
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
