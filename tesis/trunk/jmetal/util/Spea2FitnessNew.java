/**
 * Spea2Fitness.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 *
 */
package jmetal.util;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import jmetal.base.*;
import jmetal.base.operator.comparator.*;
import java.util.*;
import java.lang.Object;

/**
 * This class implements some facilities for calculating the Spea2Fitness
 */
public class Spea2FitnessNew {

    Graphics g;
    Frame f;
    double[][] distancia = null;
    double[][] value1 = null;
    double[][][] valorPlano = null;
    private Problem problem_;
    double dist[] = null;
    private int objetivos = 0;
    /**
     * Stores the distance between solutions
     */
    double cuadrante_[] = null;
    public Polygon poligono = new Polygon();
    /*
     * double con las mejores soluciones
     */
    public int planoX_[][] = null;
    public int planoY_[][] = null;
    private double[][] square_ = null;
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
    public Spea2FitnessNew(SolutionSet solutionSet, Problem problem) {
        this.problem_ = problem;
        objetivos = solutionSet.get(0).numberOfObjectives();
        double[] cuadrante = new double[objetivos];
        this.cuadrante_ = cuadrante;
        solutionSet_ = solutionSet;
        double[][] puntos = new double[2][objetivos];
        double[][] square = new double[objetivos][objetivos];
        for (int i = 0; i < solutionSet_.size(); i++) {
            solutionSet_.get(i).setLocation(i);
//              for (int j = 0; j < objetivos; j++) {
//
//                square[j][j] =solutionSet.get(i).getObjective(j);
//
//        }
        } // for
        for (int j = 0; j < solutionSet.size(); j++) {
            for (int i = 0; i < objetivos; i++) {
                 if ((solutionSet.get(j).getObjective(i) < square[i][i])) {
               
                    square[i][i] = solutionSet.get(j).getObjective(i);
                
                }
            }
        }
        int lados = nPlanos(objetivos);
        int cont;
        int[][][] planos = new int[lados][3][2];
        if (objetivos != 2) {
            for (int k = 0; k < objetivos; k++) {
                cont = 0;
                for (int i = 0; i < objetivos - 1; i++) {
                    for (int j = 1; j < objetivos - i; j++) {
                        planos[cont][k][0] = (int) square[k][i];
                        planos[cont][k][1] = (int) square[k][i + j];
                        cont++;
                    }
                }

            }
        } else {
            for (int k = 0; k < lados + 1; k++) {

                for (int i = 0; i < objetivos - 1; i++) {

                    for (int j = 1; j < objetivos - i; j++) {
//                        if (k!=2){
                        planos[0][k][0] = (int) square[k][i];
                        planos[0][k][1] = (int) square[k][i + j];
                    }
                }
            }
        }


        planoX_ = new int[planos.length][3];
        planoY_ = new int[planos.length][3];
        for (int k = 0; k < planos.length; k++) {

            for (int i = 0; i < 3; i++) {
                planoX_[k][i] = planos[k][i][0];
                planoY_[k][i] = planos[k][i][1];
            }
            if (objetivos == 2) {
                planoX_[k][2] = planos[k][0][0];
                planoY_[k][2] = planos[k][1][1];
            }
        }
        if (objetivos != 2) {
            cont = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j < 3 - i; j++) {

                    planoX_[cont][2 - cont] = planos[cont][i][0];
                    planoY_[cont][2 - cont] = planos[cont][i + j][1];
                    cont++;
                }
            }
        }


        square_ = square;
    }

    /*
     * calcula el numero de posibles planos, segun el numero de objetivos
     */
    public static int nPlanos(int x) {
        int plano = factorial(x) / (factorial(2) * factorial(x - 2));
        return plano;
    }
    /*
     * calcula el factorial de un numero
     */

    public static int factorial(int x) {
        if (x < 0) {
            return 0;
        }

        int factorial = 1;

        while (x > 1) {
            factorial = factorial * x;
            x = x - 1;
        }

        return factorial;
    }

    /**
     * Assigns fitness for all the solutions.
     */
    public void fitnessAssign() {
        double[] strength = new double[solutionSet_.size()];
        double[] rawFitness = new double[solutionSet_.size()];
        double[] fitness = new double[solutionSet_.size()];
        dist = new double[solutionSet_.size()];
        int cont = 0;

        for (int i = 0; i < cuadrante_.length; i++) {
            //   cuadrante_[i] = (distanceObj[i]) / 6;
        }

        rectaDistancia();
        for (int j = 0; j < solutionSet_.size(); j++) {

            fitness[j] = dist[j];

            solutionSet_.get(j).setFitness(fitness[j]);
        }
//        for (int j = 0; j < square_.length; j++) {
//            for (int i = 0; i < square_.length; i++) {
//                System.out.print(square_[j][i] + " ");
//
//
//            }
//
//        }
//
////        System.out.println("pertenece a fitness contraint: " + cont2);
//        System.out.println("");
//        System.out.println("");

        //System.out.println("gfmir");
    } // fitnessAsign

    public void rectaDistancia() {
        int cont2 = 0;
        int cont4 = 0;
        int contnoesta = 0;
        int cont5 = 0;
        value1 = new double[solutionSet_.size()][objetivos];
        int numeroPlanos = nPlanos(objetivos);
        double mayorDistancia = 0;
        valorPlano = new double[solutionSet_.size()][numeroPlanos][2];
        distancia = new double[solutionSet_.size()][numeroPlanos];
        int[] pertenencia = null;
        pertenencia = new int[solutionSet_.size()];
        for (int j = 0; j < solutionSet_.size(); j++) {
            for (int i = 0; i < objetivos; i++) {
                value1[j][i] = solutionSet_.get(j).getObjective(i);
            }
            int cont = 0;
            if (objetivos != 2) {
                for (int i = 0; i < objetivos - 1; i++) {
                    for (int k = 1; k < objetivos - i; k++) {
                        valorPlano[j][cont][0] = value1[j][i];
                        valorPlano[j][cont][1] = value1[j][i + k];
                        cont++;
                    }
                }
            } else {
                valorPlano[j][0][0] = value1[j][0];
                valorPlano[j][0][1] = value1[j][1];
            }
            pertenencia[j] = lugar(j);
            Line2D.Double[] rectas = new Line2D.Double[numeroPlanos];
            cont = 0;
           // if (pertenencia[j] == 2) {
            for (int i = 0; i < numeroPlanos; i++) {
                rectas[i] = new Line2D.Double();
                Point[] punto = new Point[3];
                for (int l = 0; l < 2; l++) {
                    punto[l] = new Point(planoX_[i][cont], planoY_[i][cont]);
                    cont++;
                    if (cont > 2) {
                        cont = 0;
                    }
                }
                rectas[i].setLine(punto[0], punto[1]);
                distancia[j][i] = rectas[i].ptLineDist(value1[j][0], value1[j][1]); //acomodar para multi
//            dibujar(Plano);
            }
         //   }
        }

        for (int j = 0; j < solutionSet_.size(); j++) {
            if (pertenencia[j] == 2) {
                for (int i = 0; i < numeroPlanos; i++) {
                    if (mayorDistancia == 0) {
                        mayorDistancia = distancia[j][i];
                    } else if (mayorDistancia < distancia[j][i]) {
                        mayorDistancia = distancia[j][i];
                        //       System.out.println(" ayordistancia" + j);
                    }
                }

            }
        }
//
        for (int j = 0; j < solutionSet_.size(); j++) {


            //            } else {
            if (distancia[j].length != 1) {
                for (int i = 0; i < distancia[j].length - 1; i++) {
                    if (distancia[j][i] > distancia[j][i + 1]) {
                        dist[j] = distancia[j][i];
                    } else {
                        dist[j] = distancia[j][i + 1];
                    }
                }
            } else {
                dist[j] = distancia[j][0];
            }
            if (pertenencia[j] == 1) {

                dist[j] += 9999;

//            }
                if (solutionSet_.get(j).getOverallConstraintViolation() != 0) {
                    dist[j] += Math.abs(solutionSet_.get(j).getOverallConstraintViolation());
                }
//            } else if (pertenencia == -1) {
//                cont2++;
//                fitness[j] = 1000;//Math.abs(solutionSet_.get(j).getOverallConstraintViolation()) + 100.33;
////                  for (int i = 0; i < solutionSet_.get(i).numberOfObjectives(); i++) {
//                    distancia[i] = Math.abs(solutionSet_.get(j).getObjective(i) - square_[i][i]);
                contnoesta++;

            } else if (pertenencia[j] == 2) {

                if (solutionSet_.get(j).getNumberOfViolatedConstraint() == 0) {
                    dist[j] = (mayorDistancia-dist[j]) / 100;

                    cont2++;
                } else {
                    dist[j] = Math.abs(solutionSet_.get(j).getOverallConstraintViolation());
                }

            } else {
                if (solutionSet_.get(j).getNumberOfViolatedConstraint() == 0) {
                    dist[j] = ((dist[j]) / 100) + 1;

                } else {
                    dist[j] = Math.abs(solutionSet_.get(j).getOverallConstraintViolation()) * 100;
                }
                cont4++;
            }

        }
//      System.out.println("dentro de al menos uno " + cont4);
//        System.out.println("dentro de almenos un triangulo " + cont3);
//        System.out.println("dentro todos SIN CONSTRAINT: " + cont2);
//
    }

    public int lugar(int object1) {
//        Solution solution1 = (Solution) object1;
        int obj = 0;
        int obj2 = 0;
        int flag = 0;
        int numeroPlanos = nPlanos(objetivos);
        for (int i = 0; i < numeroPlanos; i++) {
            Polygon Plano = new Polygon(planoX_[i], planoY_[i], 3);
//            dibujar(Plano);
            if (Plano.contains(valorPlano[object1][i][0], valorPlano[object1][i][1]) == true) {
                flag++;
            }
        }
        if ((flag == numeroPlanos) || (flag >= 3)) {
            return 2;
        }
        if (flag != 0) {
            return 0;
        }

        return 1;
    } // lugar del plano

    public void dibujar(Polygon p) {


        g.setColor(Color.yellow);
        g.fillPolygon(p);
        g.drawPolyline(p.xpoints, p.ypoints, p.npoints);

    }

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
        double[] auxdist = new double[solutionSet_.size()];

        int i = 0;
        Comparator comparator = new FitnessComparator();
        solutionSet_.sort(comparator);

        while (i < solutionSet_.size()) {
            if (solutionSet_.get(i).getFitness() < 1) {
                aux.add(solutionSet_.get(i));
                auxdist[i] = dist[i];

                solutionSet_.remove(i);
            } else {
                i++;
            } // if
        } // while

        if (aux.size() < size) {


            int remain = size - aux.size();
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
//        double[] distance = new double[objetivos];
//        double[] distance2 = new double[aux.size()];

//        for (int j = 0; j < aux.size(); j++) {
//
//
//            aux.get(j).setFitness(auxdist[j]);
//        }
//        Comparator comparator = new FitnessComparator();
//
//        SolutionSet aux2 = new SolutionSet(size);
//        aux.sort(comparator);
//        for (i = 0; i < size; i++) {
//            //  int a = (int) (Math.random() * aux.size());
//            aux.get(i).setFitness(0);
//            aux2.add(aux.get(i));
//        }
//        return aux2;
    } // while
} // Spea2Fitness

