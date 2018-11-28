package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.util.List;

public class Mina {

    private double x;
    private double y;
    private boolean generoExplosion;
    private static Image imgMina = Herramientas.cargarImagen("recursos/mina.png");
    private static Image imgExplosion = Herramientas.cargarImagen("recursos/explosion.gif");

    public Mina(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean fuePisada(Arania arania, Entorno e) {
        double distancia = FuncionesAuxiliares.calcularDistancia(
                this.x, this.y, arania.getX(), arania.getY());
        if (distancia < imgMina.getWidth(null) / 3) {
            return true;
        }
        return false;
    }

    public void activarMinasCercanas(List<Mina> minas, Entorno e) {
        if (!minas.isEmpty()) {
            if (this.generoExplosion) {
                for (Mina mina : minas) {
                    double distancia = FuncionesAuxiliares.calcularDistancia(this.x, this.y, mina.getX(), mina.getY());
                    if (distancia < getRadioExplosion()) {
                        mina.generarExplosion();
                    }
                }
            }
        }
    }

    public void dibujarExplosion(Entorno e) {
        e.dibujarImagen(imgExplosion, this.x, this.y, 0, 1);
    }

    public void dibujar(Entorno e) {
        e.dibujarImagen(imgMina, this.x, this.y, 0, 0.3);
    }

    public void generarExplosion() {
        this.generoExplosion = true;
    }

//********** Getters **********
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadioExplosion() {
        return imgExplosion.getWidth(null) / 3;
    }

    public boolean generoExplosion() {
        return generoExplosion;
    }

}
