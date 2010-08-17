package jmetal.qualityIndicator;

import jmetal.metaheuristics.spea2distanciacercana.SPEA2_mainCercano;
import jmetal.metaheuristics.spea2.SPEA2_main;

public class Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
        SPEA2_main sp2 = new SPEA2_main();
        SPEA2_mainCercano sp2Cercano = new SPEA2_mainCercano();
        sp2.main(args);
        sp2Cercano.main(args);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
