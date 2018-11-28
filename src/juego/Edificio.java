package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Edificio {

    private double x;
    private double y;
    private static Image imgEdificio = Herramientas.cargarImagen("recursos/techo.png");

    Edificio(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarImagen(imgEdificio, this.x, this.y, 0, 1);
    }

//********** Getters **********
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static int getAnchoImgEdificio() {
        return imgEdificio.getWidth(null);
    }

}
