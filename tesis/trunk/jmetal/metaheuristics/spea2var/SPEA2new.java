/**
 * SPEA2.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.spea2var;

import jmetal.base.*;
import java.util.Comparator;
import jmetal.util.*;

/** 
 * This class representing the SPEA2 algorithm
 */
public class SPEA2new extends Algorithm{
          
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
  public SPEA2new(Problem problem) {
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
      Spea2FitnessNew spea = new Spea2FitnessNew(union);
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
        } while (j < SPEA2new.TOURNAMENTS_ROUNDS); // do-while
        int k = 0;
        do{
          k++;                
          parents[1] = (Solution)selectionOperator.execute(archive);
        } while (k < SPEA2new.TOURNAMENTS_ROUNDS); // do-while
            
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
    for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
      Variable[] minima = new Variable[problem_.getNumberOfVariables()];
      Variable[] maxima = new Variable[problem_.getNumberOfVariables()];
      for (int j = 0; j<problem_.getNumberOfVariables();j++)
      {
          minima[j].setValue(problem_.getLowerLimit(i));
          maxima[j].setValue(problem_.getUpperLimit(i));
      }
      Solution newSolutionMinima = new Solution(problem_);
      newSolutionMinima.setDecisionVariables(minima);
      Solution newSolutionMaxima = new Solution(problem_);
      newSolutionMaxima.setDecisionVariables(minima);
      problem_.evaluate(newSolutionMinima);
      problem_.evaluateConstraints(newSolutionMinima);

    }
    Ranking ranking = new Ranking(archive);
    return ranking.getSubfront(0);
  } // execute    
} // Spea2
