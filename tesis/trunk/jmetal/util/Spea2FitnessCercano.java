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
     * stores areaOptima <code>Distance</code> object
     */
    private static final Distance distance_ = new Distance();
    /**
     * stores areaOptima <code>Comparator</code> for distance between nodes checking
     */
    private static final Comparator distanceNodeComparator = new DistanceNodeComparator();

    public Spea2FitnessCercano(SolutionSet solutionSet) {
        distance = distance_.distanceMatrix(solutionSet);
        solutionSet_ = solutionSet;
        double[] averageObj = new double[solutionSet_.size()];
        int size = solutionSet_.size();
        int nPromedio = 0;

        int objetivos = solutionSet_.get(0).numberOfObjectives();
        max = new double[objetivos];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < objetivos; j++) {
                if ((solutionSet.get(i).getObjective(j) < 0)) {
                    max[j] = Math.min(solutionSet.get(i).getObjective(j), max[j]);
                } else {
                    if (max[j] == 0) {
                        max[j] = solutionSet.get(i).getObjective(j);
                    } else {
                        max[j] = Math.min(solutionSet.get(i).getObjective(j), max[j]);
                    }
                }
            }
            solutionSet_.get(i).setLocation(i);
        } // for
    } // Spea2Fitness

    public void fitnessAssign() {
        double[] strength = new double[solutionSet_.size()];
        double[] rawFitness = new double[solutionSet_.size()];
        double kDistance;
        int cont = 0, cont2 = 0;
        valorFitness = new double[solutionSet_.size()][2];

        for (int i = 0; i < solutionSet_.size(); i++) {
            valorFitness[i] = compare(solutionSet_.get(i));
            strength[i] = valorFitness[i][1];
        } // for
        for (int i = 0; i < solutionSet_.size(); i++) {
            rawFitness[i] = compareSitio(solutionSet_.get(i));
        } // for
        int k = 1;
        for (int i = 0; i < distance.length; i++) {
            String numero = "" + strength[i];
            double divisor = Math.pow(10, numero.length());
            solutionSet_.get(i).setFitness(rawFitness[i]);
        } // for
    } // fitnessAsign

    public SolutionSet environmentalSelection(int size) {
        if (solutionSet_.size() < size) {
            size = solutionSet_.size();
        }
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
        int dominate2; // is the complementary of dominate1.
        Solution solution1 = (Solution) object1;
        int cont = 0;
        double[] value1 = new double[solution1.numberOfObjectives()];
        double[][] valor = new double[1][2];
        int special = 0;
        int flag = 0;
        if (solution1.getOverallConstraintViolation() != 0) {
            return 3;
        }
        int contadorSpecial = 0;
        
        for (int i = 0; i < solution1.numberOfObjectives(); i++) {
            double areaOptima = 0;//*2;;

            double c = 0;
            if (max[0] > 0) {
                areaOptima = (max[i] * 1 / 2 + max[i]);//*2;
                c = (max[i] * 3 / 4);
            } else {
                areaOptima = (max[i] * 9 / 10);//*2;
                c = (max[i] * 9.5 / 10);
            }
            if (solution1.getObjective(i) <= areaOptima) {//(max[i]/2)){
                flag++;
            }
            if (solution1.getObjective(i) <= c) {
                contadorSpecial++;
            }
        }
        
            System.out.println(max[0]+ " " + max[1] + " " + max[2]);
        
        if ((flag == solution1.numberOfObjectives())) {
            return compararMejores(solution1, max);
        } else if ((flag == solution1.numberOfObjectives() * 2 / 3)) {
            return 1;
        } else if (flag >= solution1.numberOfObjectives() * 1 / 3) {
            return 2;
        } else {
            return 3;
        }
    }

    public double[] compare(Object object1) {
        Solution solution1 = (Solution) object1;
        double[] value1 = new double[solution1.numberOfObjectives()];
        double[][] valor = new double[1][2];
        for (int i = 0; i < solution1.numberOfObjectives(); i++) {
            value1[i] = Math.abs(max[i] - solution1.getObjective(i));
            if ((valor[0][1] == 0) || (value1[i] < valor[0][1])) {
                valor[0][0] = i;
                valor[0][1] = value1[i];
            }
        }
        return (valor[0]);
    }

    public int compararMejores(Solution solution, double[] max) {
        double areaOptimaMayor = 0;
        double contadorOptimo = 0;
        for (int i = 0; i < max.length; i++) {
            areaOptimaMayor = max[i] * 9.8 / 10;
            if (solution.getObjective(i) <= areaOptimaMayor) {
                contadorOptimo++;
            }
        }
        if (contadorOptimo >= 1) {
            return -1;
        } else {
            return 0;
        }
    }// compare
} // Spea2Fitness

