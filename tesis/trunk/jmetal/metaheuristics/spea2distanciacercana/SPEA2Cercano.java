/**
 * SPEA2.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.spea2distanciacercana;

import jmetal.base.*;
import java.util.Comparator;
import jmetal.base.variable.Int;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.util.*;

/** 
 * This class representing the SPEA2 algorithm
 */
public class SPEA2Cercano extends Algorithm{
          
  /**
   * Defines the number of tournaments for creating the mating pool
   */
  public static final int TOURNAMENTS_ROUNDS = 1;

  /**
   * Stores the problem to solve
   */
  private Problem problem_;    

  /**
  * Constructor.
  * Create a new SPEA2 instance
  * @param problem Problem to solve
  */
  public SPEA2Cercano(Problem problem) {
    this.problem_ = problem;        
  } // Spea2
   
  /**   
  * Runs of the Spea2 algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */  
  public SolutionSet execute() throws JMException, ClassNotFoundException {   
    int populationSize, archiveSize, maxEvaluations, evaluations;
    Operator crossoverOperator, mutationOperator, selectionOperator;
    SolutionSet solutionSet, archive, offSpringSolutionSet;    
    
    //Read the params
    populationSize = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize    = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations = ((Integer)getInputParameter("maxEvaluations")).intValue();
        
    //Read the operators
    crossoverOperator = operators_.get("crossover");
    mutationOperator  = operators_.get("mutation");
    selectionOperator = operators_.get("selection");        
        
    //Initialize the variables
    solutionSet  = new SolutionSet(populationSize);
    archive     = new SolutionSet(archiveSize);
    evaluations = 0;
        
    //-> Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution(problem_);
      problem_.evaluate(newSolution);            
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      solutionSet.add(newSolution);
    }                        
        
    while (evaluations < maxEvaluations){               
      SolutionSet union = ((SolutionSet)solutionSet).union(archive);
      Spea2FitnessCercano spea = new Spea2FitnessCercano(union);
      spea.fitnessAssign();
      archive = spea.environmentalSelection(archiveSize);                       
      // Create a new offspringPopulation
      offSpringSolutionSet= new SolutionSet(populationSize);    
      Solution  [] parents = new Solution[2];
      while (offSpringSolutionSet.size() < populationSize){           
        int j = 0;
        do{
          j++;                
          parents[0] = (Solution)selectionOperator.execute(archive);
        } while (j < SPEA2Cercano.TOURNAMENTS_ROUNDS); // do-while
        int k = 0;
        do{
          k++;                
          parents[1] = (Solution)selectionOperator.execute(archive);
        } while (k < SPEA2Cercano.TOURNAMENTS_ROUNDS); // do-while
            
        //make the crossover 
        Solution [] offSpring = (Solution [])crossoverOperator.execute(parents);            
        mutationOperator.execute(offSpring[0]);            
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);            
        offSpringSolutionSet.add(offSpring[0]);
        evaluations++;
      } // while
      // End Create a offSpring solutionSet
      solutionSet = offSpringSolutionSet;                   
    } // while
          double[][] minPareto = new double[problem_.getNumberOfObjectives()][problem_.getNumberOfObjectives()];
     
        Hypervolume toma = new Hypervolume();
        Ranking ranking = new Ranking(archive);
//        double[][] paretoFront = ranking.getSubfront(0).writeObjectivesToMatrix();
//        double HV = toma.hypervolume(paretoFront, minPareto, problem_.getNumberOfObjectives());
//       System.out.print("Este es el HV del problema tal: "+HV);

     Spea2FitnessCercano.max=null;
     Spea2FitnessCercano.inicio=true;
    return archive;
  } // execute    
} // Spea2
