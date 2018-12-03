package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.util.List;

public class Arania {

    private double x;
    private double y;
<<<<<<< HEAD
    private double angulo;
=======
    private double direccion;
>>>>>>> parent of 6580e1a... Limpieza final de codigo
    private double anguloImgArania;
    private static Image imgArania = Herramientas.cargarImagen("recursos/arania.png");

    public Arania(double x, double y, double direccion, double anguloImgArania) {
        this.x = x;
        this.y = y;
<<<<<<< HEAD
        this.angulo = direccion;
=======
        this.direccion = direccion;
>>>>>>> parent of 6580e1a... Limpieza final de codigo
        this.anguloImgArania = anguloImgArania;
    }

    //Modificado
    public void perseguirExterminador(Exterminador ext, Entorno e, List<Edificio> edificios) {
        if (chocaConUnEdifico(edificios)) {
<<<<<<< HEAD
            angulo += 1;
            this.x -= Math.cos(angulo) * 1;
            this.y -= Math.sin(angulo) * 1;
        } else {
            double cos = this.x - ext.getX();
            double sen = this.y - ext.getY();
            angulo = Math.atan2(sen, cos);
            this.x -= Math.cos(angulo) * 0.5;
            this.y -= Math.sin(angulo) * 0.5;
=======
            direccion += 1;
            this.x -= Math.cos(direccion) * 1;
            this.y -= Math.sin(direccion) * 1;
        } else {
            double cos = this.x - ext.getX();
            double sen = this.y - ext.getY();
            direccion = Math.atan2(sen, cos);
            this.x -= Math.cos(direccion) * 0.5;
            this.y -= Math.sin(direccion) * 0.5;
>>>>>>> parent of 6580e1a... Limpieza final de codigo
        }
        chocaConVentana(e);
    }

    //Modificado
    public Telarania lanzarTelarania(double x, double y) {
        Telarania t = new Telarania(this.x, this.y);
        return t;
    }

    public void dibujar(Entorno e) {
        e.dibujarImagen(imgArania, this.x, this.y, this.anguloImgArania, 0.4);
    }

    private void chocaConVentana(Entorno e) {
        if (this.x > e.ancho() || this.x < 0) {
            this.x -= 1;
        }
        if (this.y > e.alto() || this.y < 0) {
            this.y -= 1;
        }
    }

<<<<<<< HEAD
    //Modificado
    private boolean chocaConUnEdifico(List<Edificio> edificios) {
        for (Edificio edificio : edificios) {
            double distancia = FuncionesAuxiliares.calcularDistancia(
                    this.x,this.y,edificio.getX(),edificio.getY());
=======
    private boolean chocaConUnEdifico(List<Edificio> edificios) {
        for (Edificio edificio : edificios) {
            double distancia = FuncionesAuxiliares.calcularDistancia(
                    this.x,
                    this.y,
                    edificio.getX(),
                    edificio.getY());
>>>>>>> parent of 6580e1a... Limpieza final de codigo
            if (distancia <= (Arania.getAnchoImgArania() / 3 + Edificio.getAnchoImgEdificio() / 3)) {
                return true;
            }
        }
        return false;
    }

//********** Getters  **********
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static int getAnchoImgArania() {
        return imgArania.getWidth(null);
    }

}
