package juego;

public class FuncionesAuxiliares {

    public static double calcularDistancia(double x1, double y1, double x2, double y2) {
        double c1 = x1 - x2;
        double c2 = y1 - y2;
        double distancia = Math.hypot(c1, c2);
        return distancia;
    }
    
}
