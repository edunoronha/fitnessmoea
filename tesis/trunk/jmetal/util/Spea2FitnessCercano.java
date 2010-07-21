/**
 * Spea2Fitness.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 *
 */
package jmetal.util;

import jmetal.base.*;
import jmetal.base.operator.comparator.*;
import java.util.*;

/**
 * This class implements some facilities for calculating the Spea2Fitness
 */
public class Spea2FitnessCercano {

    private static final Comparator overallConstraintViolationComparator_ =
            new OverallConstraintViolationComparator();
    private double[] max = null;
    private double[][] valorFitness = null;
    /**
     * Stores the distance between solutions
     */
    private double[][] distance = null;
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
    private static final Comparator dominance_ = new AverageComparator();

    /**
     * Constructor.
     * Creates a new instance of Spea2Fitness for a given <code>SolutionSet</code>.
     * @param solutionSet The <code>SolutionSet</code>
     */
    public Spea2FitnessCercano(SolutionSet solutionSet) {
        distance = distance_.distanceMatrix(solutionSet);
        solutionSet_ = solutionSet;
        double[] averageObj = new double[solutionSet_.size()];
        int size = solutionSet_.size();
        int nPromedio = 0;

        int objetivos = solutionSet_.get(0).numberOfObjectives();
        max = new double[objetivos];
        for (int i = 0; i < size; i++) {
            //      if (solutionSet_.get(i).getNumberOfViolatedConstraint()==0){
            //        nPromedio++;

            for (int j = 0; j < objetivos; j++) {
//                (solutionSet.get(i).getOverallConstraintViolation()==0)&&
                if ((solutionSet.get(i).getObjective(j) < max[j])) {
                    max[j] = solutionSet.get(i).getObjective(j);

                }
            }
            // }
            solutionSet_.get(i).setLocation(i);

        } // for

    } // Spea2Fitness

    /**
     * Assigns fitness for all the solutions.
     */
    public void fitnessAssign() {
        double[] strength = new double[solutionSet_.size()];
        double[] rawFitness = new double[solutionSet_.size()];
        double kDistance;

        int cont = 0, cont2 = 0;
//         for (int i = 0; i < solutionSet_.size(); i++) {
//                if (solutionSet_.get(i).getNumberOfViolatedConstraint()==0)
//                   //   System.out.println("este no tiene consstraint: " +i+" total:"+ solutionSet_.get(i).getTotal());
//        }
        //Calculate the strength value
        // strength(i) = |{j | j <- SolutionSet and i dominate j}|
        valorFitness = new double[solutionSet_.size()][2];
        for (int i = 0; i < solutionSet_.size(); i++) {

            //  if ((solutionSet_.get(i).getTotal() < averageProb)) {
            cont++;

            valorFitness[i] = compare(solutionSet_.get(i));
            strength[i] = valorFitness[i][1];
            // if
        } // for

             for (int i = 0; i < solutionSet_.size(); i++) {

            //  if ((solutionSet_.get(i).getTotal() < averageProb)) {
            cont++;

            rawFitness[i] = compareSitio(solutionSet_.get(i));

            // if
        } // for






        // Add the distance to the k-th individual. In the reference paper of SPEA2,
        // k = sqrt(population.size()), but a value of k = 1 recommended. See
        // http://www.tik.ee.ethz.ch/pisa/selectors/spea2/spea2_documentation.txt
        int k = 1;
        for (int i = 0; i < distance.length; i++) {
//            Arrays.sort(distance[i]);
//            kDistance = 1.0 / (distance[i][k] + 2.0); // Calcule de D(i) distance
            //population.get(i).setFitness(rawFitness[i]);
            String numero = ""+strength[i];
            double divisor= Math.pow(10,numero.length());

            solutionSet_.get(i).setFitness(rawFitness[i]+(strength[i]/divisor));
        } // for
//        System.out.println(" ");
    } // fitnessAsign

    /**
     *  Gets 'size' elements from a population of more than 'size' elements
     *  using for this de enviromentalSelection truncation
     *  @param size The number of elements to get.
     */
    public SolutionSet environmentalSelection(int size) {

        if (solutionSet_.size() < size) {
            size = solutionSet_.size();
        }

        // Create a new auxiliar population for no alter the original population
        SolutionSet aux = new SolutionSet(solutionSet_.size());
      
        int i = 0;
        while (i < solutionSet_.size()) {
        double a = 0;
            if (solutionSet_.get(i).getFitness() < 1) {

                aux.add(solutionSet_.get(i));
                solutionSet_.remove(i);
            } else {
                i++;
            }
        } // while

        if (aux.size() < size) {
            int remain = size - aux.size();
        Comparator comparator = new FitnessComparator();
        solutionSet_.sort(comparator);
            for (i = 0; i < remain; i++) {
                aux.add(solutionSet_.get(i));
            }
            return aux;
        } else if (aux.size() == size) {
            return aux;
        }

        double[][] distance = distance_.distanceMatrix(aux);
        List<List<DistanceNode>> distanceList = new LinkedList<List<DistanceNode>>();
        for (int pos = 0; pos < aux.size(); pos++) {
            aux.get(pos).setLocation(pos);
            List<DistanceNode> distanceNodeList = new ArrayList<DistanceNode>();
            for (int ref = 0; ref < aux.size(); ref++) {
                if (pos != ref) {
                    distanceNodeList.add(new DistanceNode(distance[pos][ref], ref));
                } // if
            } // for
            distanceList.add(distanceNodeList);
        } // for


        for (int q = 0; q < distanceList.size(); q++) {
            Collections.sort(distanceList.get(q), distanceNodeComparator);
        } // for

        while (aux.size() > size) {
            double minDistance = Double.MAX_VALUE;
            int toRemove = 0;
            i = 0;
            Iterator<List<DistanceNode>> iterator = distanceList.iterator();
            while (iterator.hasNext()) {
                List<DistanceNode> dn = iterator.next();
                if (dn.get(0).getDistance() < minDistance) {
                    toRemove = i;
                    minDistance = dn.get(0).getDistance();
                    //i y toRemove have the same distance to the first solution
                } else if (dn.get(0).getDistance() == minDistance) {
                    int k = 0;
                    while ((dn.get(k).getDistance()
                            == distanceList.get(toRemove).get(k).getDistance())
                            && k < (distanceList.get(i).size() - 1)) {
                        k++;
                    }

                    if (dn.get(k).getDistance()
                            < distanceList.get(toRemove).get(k).getDistance()) {
                        toRemove = i;
                    } // if
                } // if
                i++;
            } // while

            int tmp = aux.get(toRemove).getLocation();
            aux.remove(toRemove);
            distanceList.remove(toRemove);

            Iterator<List<DistanceNode>> externIterator = distanceList.iterator();
            while (externIterator.hasNext()) {
                Iterator<DistanceNode> interIterator = externIterator.next().iterator();
                while (interIterator.hasNext()) {
                    if (interIterator.next().getReference() == tmp) {
                        interIterator.remove();
                        continue;
                    } // if
                } // while
            } // while
        } // while
        return aux;
    } // environmentalSelection
  public int compareSitio(Object object1) {
        int dominate1; // dominate1 indicates if some objective of solution1
        // dominates the same objective in solution2. dominate2
        int dominate2; // is the complementary of dominate1.
        Solution solution1 = (Solution) object1;
        int cont = 0;
        // Equal number of violated constraint. Apply a dominance Test
        double[] value1 = new double[solution1.numberOfObjectives()];
        double[][] valor = new double[1][2];
        int flag=0;
         if (solution1.getOverallConstraintViolation() != 0) {
            return 3;

        }
        for (int i = 0; i < solution1.numberOfObjectives(); i++) {
            double a = (max[i]/2);//*2;
            double b= solution1.getObjective(i);
            if (solution1.getObjective(i) <=a){//(max[i]/2)){
                flag++;
            }
      }
        if (flag == solution1.numberOfObjectives()){
            return 0;
        }else if ((flag>=solution1.numberOfObjectives()*2/3)){
            return 1;
        }else if (flag>=solution1.numberOfObjectives()*1/3){
            return 2;
        }
        else
        return 3;
    }
    public double[] compare(Object object1) {


        int dominate1; // dominate1 indicates if some objective of solution1
        // dominates the same objective in solution2. dominate2
        int dominate2; // is the complementary of dominate1.
        Solution solution1 = (Solution) object1;
        int cont = 0;
        // Equal number of violated constraint. Apply a dominance Test
        double[] value1 = new double[solution1.numberOfObjectives()];
        double[][] valor = new double[1][2];
        int flag=0;
        for (int i = 0; i < solution1.numberOfObjectives(); i++) {
            if (solution1.getObjective(i) <= (max[i]/4)){
                flag++;
            }

            value1[i] = Math.abs(max[i] - solution1.getObjective(i));
            if ((valor[0][1] == 0) || (value1[i] < valor[0][1])) {
                valor[0][0] = i;
                valor[0][1] = value1[i];
            }

        }

       
        return (valor[0]);


    } // compare
} // Spea2Fitness

