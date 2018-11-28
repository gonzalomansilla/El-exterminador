package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.util.Iterator;
import java.util.List;

public class Disparo {

    private double x;
    private double y;
    private double direccion;
    private static Image imgDisparo = Herramientas.cargarImagen("recursos/disparo.png");

    public Disparo(double x, double y, double direccion) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
    }

    public void realizarTrayecto() {
        this.x += Math.cos(this.direccion) * 3;
        this.y += Math.sin(this.direccion) * 3;
    }

    public boolean impactaConVentana(Entorno e) {
        return this.x >= e.ancho() || this.x <= 0 || this.y >= e.alto() || this.y <= 0;
    }

    public boolean impactaConArania(Disparo disparo, List<Arania> aranias, Exterminador exterminador) {
        Iterator<Arania> iAranias = aranias.iterator();
        while (iAranias.hasNext()) {
            Arania arania = iAranias.next();
            double distancia = FuncionesAuxiliares.calcularDistancia(
                    disparo.getX(), disparo.getY(), arania.getX(), arania.getY());
            if (distancia < Arania.getAnchoImgArania() / 3) {
                if (aranias.contains(arania)) {
                    iAranias.remove();
                }
                return true;
            }
        }
        return false;
    }

    public boolean impactoConMina(List<Mina> minas, Entorno e) {
        Iterator<Mina> i_Minas = minas.iterator();
        while (i_Minas.hasNext()) {
            Mina mina = i_Minas.next();
            double distancia = FuncionesAuxiliares.calcularDistancia(this.x, this.y, mina.getX(), mina.getY());
            if (distancia < imgDisparo.getWidth(null) / 2) {
                mina.generarExplosion();   
                mina.activarMinasCercanas(minas, e);
                return true;
            }
        }
        return false;
    }

    public boolean impactaConEdificio(Disparo disparo, List<Edificio> edificios) {
        for (Edificio edificio : edificios) {
            double distancia = FuncionesAuxiliares.calcularDistancia(
                    edificio.getX(), edificio.getY(), disparo.getX(), disparo.getY());
            if (distancia <= Edificio.getAnchoImgEdificio() / 2) {
                return true;
            }
        }
        return false;
    }

    public void dibujarDisparo(Entorno e) {
        e.dibujarImagen(imgDisparo, this.x, this.y, this.direccion, 0.5);
    }

//********** Getters **********
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
