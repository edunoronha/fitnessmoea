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
import jmetal.base.variable.Real;
import jmetal.util.JMException;

public class Knapsack extends Problem {

    double[] VarValor = {2.0, 8.0, 6.0, 12.0, 4.0, 7.0, 1.0, 3.0, 5.0, 14.0, 15.0, 8.0};
    double[] VarPeso = {4.0, 3.0, 6.0, 11.0, 2.0, 10.0, 1.0, 7.0, 9.0, 12.0, 5.0, 13.0};
    double maxCapacidad = 30.0;

    public Knapsack(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {

        numberOfVariables_ = 12;
        numberOfObjectives_ = 1;
        numberOfConstraints_ = 0;
        problemName_ = "Knapsack";
//        length_       = new int[numberOfVariables_];

        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];
//
//        lowerLimit_[0] = 1.0;
//        lowerLimit_[1] = 4.0;
//        lowerLimit_[2] = 7.0;
//        lowerLimit_[3] = 2.0;
//        lowerLimit_[4] = 3.0;
//        lowerLimit_[5] = 9.0;
//
//        upperLimit_[0] = 1.0;
//        upperLimit_[1] = 4.0;
//        upperLimit_[2] = 7.0;
//        upperLimit_[3] = 2.0;
//        upperLimit_[4] = 3.0;
//        upperLimit_[5] = 9.0;
//        length_      [0] = 12 ;
        for (int i = 0; i < numberOfVariables_; i++) {
            lowerLimit_[i] = 0.0;
            upperLimit_[i] = 1.0;
        }

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
        for (int var = 0; var < numberOfVariables_ - 1; var++) {
                    bits = variables[var].toString();
                        fx[0] = fx[0] + VarValor[var]*Integer.valueOf(bits);
        } // for
        solution.setObjective(0, fx[0]);
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
    }
}