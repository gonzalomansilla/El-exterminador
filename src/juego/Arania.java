package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.util.List;

public class Arania {

    private double x;
    private double y;
    private double direccion;
    private static Image imgArania = Herramientas.cargarImagen("recursos/arania.png");
    private double anguloImgArania;

    public Arania(double x, double y, double direccion, double anguloImgArania) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
        this.anguloImgArania = anguloImgArania;
    }

    //Modificado
    public void perseguirExterminador(Exterminador ext, Entorno e, List<Edificio> edificios) {
        double cos = this.x - ext.getX();
        double sen = this.y - ext.getY();

        direccion = Math.atan2(sen, cos);

        this.x -= Math.cos(direccion) * 0.5;
        this.y -= Math.sin(direccion) * 0.5;
        
        if (chocaConUnEdifico(edificios)) {
            direccion += 0.3;
            
            this.x -= Math.cos(direccion + Math.PI) * 0.5;
            this.y -= Math.sin(direccion + Math.PI) * 0.5;
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

    private boolean chocaConUnEdifico(List<Edificio> edificios) {
        for (Edificio edificio : edificios) {
            double distancia = FuncionesAuxiliares.calcularDistancia(this.x, this.y, edificio.getX(), edificio.getY());
            if (distancia <= (Edificio.getAnchoImgEdificio() / 2)) {
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
