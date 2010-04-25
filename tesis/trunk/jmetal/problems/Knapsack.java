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

    double[] VarValor = {2.0, 8.0, 6.0, 12.0, 4.0, 7.0, 1.0, 3.0, 5.0, 14.0, 15.0, 8.0};
    double[] VarPeso = {4.0, 3.0, 6.0, 11.0, 2.0, 10.0, 1.0, 7.0, 9.0, 12.0, 5.0, 13.0};
    double maxCapacidad = 30.0;

    public Knapsack(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {

        numberOfVariables_ = 12;
        numberOfObjectives_ = 1;
        numberOfConstraints_ = 1;
        problemName_ = "Knapsack";
//        length_       = new int[numberOfVariables_];


        if (solutionType.compareTo("Real") == 0) {
            solutionType_ = new BinarySolutionType(this);
        } else {
            System.out.println("Error: solution type " + solutionType + " invalid");
            System.exit(-1);
        }
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();

        String bits;
        double[] fx = new double[1]; // function values
        for (int var = 0; var < numberOfVariables_; var++) {
            Binary p1 = (Binary)solution.getDecisionVariables()[var];
            bits = variables[var].toString();
            fx[0] = -(fx[0] + VarValor[var] * Integer.valueOf(bits));
        } // for
        solution.setObjective(0,-1*fx[0]);
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();
        String bits;
        int number = 0;
        double total = 0.0;
        double xPeso = 0.0;
        double constraint = 0.0;

        for (int var = 0; var < this.numberOfVariables_; var++) {
            bits = variables[var].toString();
            if (bits.equals("1")) {
                xPeso = xPeso + VarPeso[var];
                if (xPeso > this.maxCapacidad) {
                    constraint = xPeso -30 + VarPeso[var];
                }
            }

        }
        for (int i = 0; i < this.numberOfConstraints_; i++) {
            if (constraint > 0.0) {
                number++;
                total+=-(constraint);
            }
        }
        solution.setOverallConstraintViolation(-1*(total));
        solution.setNumberOfViolatedConstraint(number);
    }
}
