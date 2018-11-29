package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Telarania {

    private int tiempoDeVida;
    private double x;
    private double y;
    private static Image imgTelarania = Herramientas.cargarImagen("recursos/telarania.png");

    public Telarania(double x, double y) {
        this.x = x;
        this.y = y;
        this.tiempoDeVida = 1500;
    }

    public void dibujar(Entorno e) {
        e.dibujarImagen(imgTelarania, this.x, this.y, 0, 1);
    }

    public void restarTiempoDeVida() {
        this.tiempoDeVida -= 100;
    }

//********** Getters **********
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTiempoDeVida() {
        return tiempoDeVida;
    }

    public static int getAnchoImgTelarania() {
        return imgTelarania.getWidth(null);
    }

}
