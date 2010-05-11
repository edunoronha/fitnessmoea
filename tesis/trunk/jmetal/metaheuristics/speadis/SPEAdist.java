/**
 * SPEA2.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.speadis;

import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.*;
import java.util.Comparator;
import jmetal.util.*;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

/** 
 * This class representing the SPEA2 algorithm
 */
public class SPEAdist extends Algorithm{
          
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
  public SPEAdist(Problem problem) {
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
     Spea2Fitnessdistancia spea = new Spea2Fitnessdistancia(union);

      //Spea2Fitness spea = new Spea2Fitness(union);
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
        } while (j < SPEAdist.TOURNAMENTS_ROUNDS); // do-while
        int k = 0;
        do{
          k++;                
          parents[1] = (Solution)selectionOperator.execute(archive);
        } while (k < SPEAdist.TOURNAMENTS_ROUNDS); // do-while
            
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
//      solutionSet.get(0).setCrowdingDistance(archive.get(0).getCrowdingDistance());
//      System.out.println(solutionSet.get(0).getCrowdingDistance()+ " spea2");
    } // while
  Ranking ranking = new Ranking(archive);
//    Ranking ranking = new Ranking(archive);
    return ranking.getSubfront(0);
      
      
  } // execute    
} // Spea2
