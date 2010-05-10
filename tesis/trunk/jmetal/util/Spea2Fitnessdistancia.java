/**
 * Spea2Fitness.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 *
 */
package jmetal.util;

import jmetal.base.*                    ;
import jmetal.base.operator.comparator.*;
import java.util.*                      ;

/**
 * This class implements some facilities for calculating the Spea2Fitness
 */
public class Spea2Fitnessdistancia {

    private double maxdistance;
private double [][] auxDistance = null;
  /**
   * Stores the distance between solutions
   */
  private double [][] distance = null;

  /**
   * Stores the solutionSet to assign the fitness
   */
  private SolutionSet solutionSet_ = null;

  /**
   * stores a <code>Distance</code> object
   */
  private static final Distance distance_ = new Distance();

  /**
   * stores a <code>Comparator</code> for distance between nodes checking
   */
  private static final Comparator distanceNodeComparator = new DistanceNodeComparator();

  /**
   * stores a <code>Comparator</code> for dominance checking
   */
  private static final Comparator dominance_ = new DominanceComparator();

  /**
   * Constructor.
   * Creates a new instance of Spea2Fitness for a given <code>SolutionSet</code>.
   * @param solutionSet The <code>SolutionSet</code>
   */
  public Spea2Fitnessdistancia(SolutionSet solutionSet) {
       //   maxdistance[0] = 0;
    distance     = distance_.distanceMatrix(solutionSet);
    auxDistance = distance_.distanceMatrix(solutionSet);
     for (int j = 0; j < distance.length; j++) {
      Arrays.sort(auxDistance[j]);
      if ( auxDistance[j][1] > maxdistance)
            maxdistance =  auxDistance[j][1]; // Calcule de D(i) distance
      //population.get(i).setFitness(rawFitness[i]);
     } // for
    solutionSet_ = solutionSet;
    solutionSet_.get(0).setCrowdingDistance(maxdistance);
    for (int i = 0; i < solutionSet_.size(); i++) {
      solutionSet_.get(i).setLocation(i);
    } // for
   // System.out.println(maxdistance + " fitness");
  } // Spea2Fitness


  /**
   * Assigns fitness for all the solutions.
   */
  public void fitnessAssign() {
    double [] strength    = new double[solutionSet_.size()];
    double [] rawFitness  = new double[solutionSet_.size()];
    double distanciaij;
    double kDistance                                          ;


    //Calculate the strength value
    // strength(i) = |{j | j <- SolutionSet and i dominate j}|
    for (int i = 0; i < solutionSet_.size(); i++) {
      for (int j = 0; j < solutionSet_.size();j++) {
          distanciaij = distance[i][j];
          if (distance[i][j]>= maxdistance)
            if (dominance_.compare(solutionSet_.get(i),solutionSet_.get(j))==-1) {
          strength[i] += 1.0;
        } // if
      } // for
    } // for


    //Calculate the raw fitness
    // rawFitness(i) = |{sum strenght(j) | j <- SolutionSet and j dominate i}|
    for (int i = 0;i < solutionSet_.size(); i++) {
      for (int j = 0; j < solutionSet_.size();j++) {
        if (distance[i][j]>= maxdistance)
        if (dominance_.compare(solutionSet_.get(i),solutionSet_.get(j))==1) {
          rawFitness[i] += strength[j];
        } // if
      } // for
    } // for


    // Add the distance to the k-th individual. In the reference paper of SPEA2,
    // k = sqrt(population.size()), but a value of k = 1 recommended. See
    // http://www.tik.ee.ethz.ch/pisa/selectors/spea2/spea2_documentation.txt
     int k = 1 ;

//    for (int i = 0; i < distance.length; i++) {
//         k[0] = 0 ;
//        for (int j=0; j< distance.length;j++){
//      if (distance[i][j]<=maxdistance[0]){
//        k[0] +=         distance[i][j];
//      }
//      else
//          j=distance.length;

  

    
      for (int i = 0; i < auxDistance.length; i++) {
       kDistance = 1.0 / (auxDistance[i][k] + 2.0); // Calcule de D(i) distance
      //population.get(i).setFitness(rawFitness[i]);
      solutionSet_.get(i).setFitness(rawFitness[i] + kDistance);
    } // for   // for
  }  // fitnessAsign


  /**
  *  Gets 'size' elements from a population of more than 'size' elements
  *  using for this de enviromentalSelection truncation
  *  @param size The number of elements to get.
  */
  public SolutionSet environmentalSelection(int size){

    if (solutionSet_.size() < size) {
      size = solutionSet_.size();
    }

    // Create a new auxiliar population for no alter the original population
    SolutionSet aux = new SolutionSet(solutionSet_.size());

    int i = 0;
    while (i < solutionSet_.size()){
      if (solutionSet_.get(i).getFitness()<1.0){
        aux.add(solutionSet_.get(i));
        solutionSet_.remove(i);
      } else {
        i++;
      } // if
    } // while

    if (aux.size() < size){
      Comparator comparator = new FitnessComparator();
      solutionSet_.sort(comparator);
      int remain = size - aux.size();
      for (i = 0; i < remain; i++){
        aux.add(solutionSet_.get(i));
      }
       aux.get(0).setCrowdingDistance(maxdistance);
      return aux;
    } else if (aux.size() == size) {
         aux.get(0).setCrowdingDistance(maxdistance);
      return aux;
    }

    double [][] distance = distance_.distanceMatrix(aux);
    List< List<DistanceNode> > distanceList = new LinkedList< List<DistanceNode> >();
    for (int pos = 0; pos < aux.size(); pos++) {
      aux.get(pos).setLocation(pos);
      List<DistanceNode> distanceNodeList = new ArrayList<DistanceNode>();
      for (int ref = 0; ref < aux.size(); ref++) {
        if (pos != ref) {
          distanceNodeList.add(new DistanceNode(distance[pos][ref],ref));
        } // if
      } // for
      distanceList.add(distanceNodeList);
    } // for


    for (int q = 0; q < distanceList.size(); q++){
      Collections.sort(distanceList.get(q),distanceNodeComparator);
    } // for

    while (aux.size() > size) {
      double minDistance = Double.MAX_VALUE;
      int toRemove = 0;
      i = 0;
      Iterator<List<DistanceNode>> iterator = distanceList.iterator();
      while (iterator.hasNext()){
        List<DistanceNode> dn = iterator.next();
        if (dn.get(0).getDistance() < minDistance){
          toRemove = i;
          minDistance = dn.get(0).getDistance();
          //i y toRemove have the same distance to the first solution
        } else if (dn.get(0).getDistance() == minDistance) {
          int k = 0;
          while ((dn.get(k).getDistance() ==
        	      distanceList.get(toRemove).get(k).getDistance()) &&
                  k < (distanceList.get(i).size()-1)) {
            k++;
          }

          if (dn.get(k).getDistance() <
              distanceList.get(toRemove).get(k).getDistance()) {
            toRemove = i;
          } // if
        } // if
        i++;
      } // while

      int tmp = aux.get(toRemove).getLocation();
      aux.remove(toRemove);
      distanceList.remove(toRemove);

      Iterator<List<DistanceNode>> externIterator = distanceList.iterator();
      while (externIterator.hasNext()){
        Iterator<DistanceNode> interIterator = externIterator.next().iterator();
        while (interIterator.hasNext()){
          if (interIterator.next().getReference() == tmp){
            interIterator.remove();
            continue;
          } // if
        } // while
      } // while
    } // while
    aux.get(0).setCrowdingDistance(maxdistance);
    return aux;
  } // environmentalSelection
} // Spea2Fitness