package jmetal.qualityIndicator;

import java.io.IOException;
import jmetal.base.*;
import jmetal.base.operator.crossover.*;
import jmetal.base.operator.mutation.*;
import jmetal.base.operator.selection.*;
import jmetal.problems.*;



import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.metaheuristics.spea2distanciacercana.SPEA2Cercano;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class PruebasVarias {

    public static Logger logger_;      // Logger object
    public static FileHandler fileHandler_; // FileHandler object
    public static Problem problem;         // The problem to solve
    public static Algorithm algorithm;         // The algorithm to use
    public static Operator crossover;         // Crossover operator
    public static Operator mutation;         // Mutation operator
    public static Operator selection;         // Selection operator
    public static SolutionSet population;

    /**
     * @param args Command line arguments. The first (optional) argument specifies
     *             the problem to solve.
     * @throws JMException
     * @throws IOException
     * @throws SecurityException
     * Usage: three options
     *      - jmetal.metaheuristics.mocell.MOCell_main
     *      - jmetal.metaheuristics.mocell.MOCell_main problemName
     *      - jmetal.metaheuristics.mocell.MOCell_main problemName ParetoFrontFile
     */
    public static void main(String[] args) throws JMException, IOException, ClassNotFoundException, WriteException, BiffException {

        try {
            parametros();
            int corrida = 30;

            long[] estimatedTimeCercano = new long[corrida];
            long[] estimatedTimeSpea = new long[corrida];
            long[] estimatedTimeNsga = new long[corrida];
            for (int i = 0; i < corrida; i++) {
                long initTime = System.currentTimeMillis();

                algoritmo(1);
                population = algorithm.execute();
                String a = algorithm.toString();
                population.printVariablesToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+ population.get(0).numberOfVariables()+ "/SPEACercano/VAR_" + i, "SPEA2Cercano" + i);
                population.printObjectivesToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+population.get(0).numberOfVariables()+ "/SPEACercano/FUNspeaCercano_" + i, "SPEA2Cercano" + i);

                estimatedTimeCercano[i] = (System.currentTimeMillis() - initTime) / 1000;
                System.out.println(estimatedTimeCercano[i] + " tiempo transcurrido CERCANO");
                initTime = System.currentTimeMillis();
                algoritmo(0);
                population = algorithm.execute();
                population.printVariablesToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+population.get(0).numberOfVariables()+ "/SPEA2/VAR_" + i, "SPEA2" + i);
                population.printObjectivesToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+ population.get(0).numberOfVariables()+"/SPEA2/FUNspea2_" + i, "SPEA2" + i);
                estimatedTimeSpea[i] = (System.currentTimeMillis() - initTime) / 1000;
                System.out.println(estimatedTimeSpea[i] + " tiempo transcurrido SPEA2");
                initTime = System.currentTimeMillis();
                algoritmo(2);
                population = algorithm.execute();
                population.printVariablesToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+ population.get(0).numberOfVariables()+"/NSGA2/VAR_" + i, "SPEA2" + i);
                population.printObjectivesToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+population.get(0).numberOfVariables()+ "/NSGA2/FUNnsga2_" + i, "nsga2" + i);
                estimatedTimeNsga[i] = (System.currentTimeMillis() - initTime) / 1000;
                System.out.println(estimatedTimeNsga[i] + " tiempo transcurrido NSGA2");

            }
            population.printTimeToFile(problem.getName() + " " + population.get(0).numberOfObjectives() + " "+population.get(0).numberOfVariables()+"/SPEACercano/tiempoCorridas", estimatedTimeCercano);
            population.printTimeToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+ population.get(0).numberOfVariables()+"/SPEA2/tiempoCorridas", estimatedTimeSpea);
            population.printTimeToFile(problem.getName() + " " + population.get(0).numberOfObjectives() +" "+population.get(0).numberOfVariables()+ "/NSGA2/tiempoCorridas", estimatedTimeNsga);
            String[] parametros = new String[2];
            parametros[0]=(problem.getName() + " " + population.get(0).numberOfObjectives()+" "+population.get(0).numberOfVariables());
            parametros[1]= corrida+"";
            SetCoveragevariasCorridas covertura = new SetCoveragevariasCorridas();
            covertura.main(parametros);
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Result messages


//      Hypervolume toma = new Hypervolume();
//     SetCoverage T = new SetCoverage();
//        @SuppressWarnings("static-access")
//     double[][] f = T.archivoAMatriz("FUNspea");
//    double HV = toma.hypervolume(population.writeObjectivesToMatrix(),f, problem.getNumberOfObjectives());
//   // System.out.print("Este es el HV del problema: "+HV);


    }//main

    public static void algoritmo(int a) {
        if (a == 0) {
            algorithm = new SPEA2(problem);
        } else if (a == 1) {
            algorithm = new SPEA2Cercano(problem);
        } else {
            algorithm = new NSGAII(problem);
        }

        // Algorithm parameters
        algorithm.setInputParameter("populationSize", 100);
        algorithm.setInputParameter("archiveSize", 100);
        algorithm.setInputParameter("maxEvaluations", 25000);
        algorithm.setInputParameter("indicators", null);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        // Execute the algorithm
        long initTime = System.currentTimeMillis();

    }

    public static void parametros() {
        try {

            QualityIndicator indicators; // Object to get quality indicators

            // Logger object and file to store log messages
            logger_ = Configuration.logger_;
            fileHandler_ = new FileHandler("SPEA2.log");
            logger_.addHandler(fileHandler_);
            indicators = null;
            // Default problem

//             problem = new Tanaka("Real");
            //problem = new ZDT1("ArrayReal", 1000);
            //problem = new ZDT4("BinaryReal");
            //problem = new WFG1("Real");
            //problem = new DTLZ1("Real");



//kursawe
//    problem = new Kursawe("Real", 3);
//    mutation = MutationFactory.getMutationOperator("PolynomialMutation");
//    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");
            // Mutation and crossover for real codification
////     TSP
//                problem = new TSP(0, 5);
//    crossover = CrossoverFactory.getCrossoverOperator("TwoPointsCrossover");
//    mutation = MutationFactory.getMutationOperator("SwapMutation");
//
////
//            problem = new KnapsackMuchos("Real", 6);
                         problem = new SetPacking(3) ;
            crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");
            mutation = MutationFactory.getMutationOperator("BitFlipMutation");

            crossover.setParameter("probability", 0.9);
            crossover.setParameter("distributionIndex", 20.0);
            //knapsack

            mutation.setParameter("probability", 1.0 / problem.getNumberOfVariables());
            mutation.setParameter("distributionIndex", 20.0);

            // Selection operator
            selection = SelectionFactory.getSelectionOperator("BinaryTournament");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} // SPEA2_main.java

