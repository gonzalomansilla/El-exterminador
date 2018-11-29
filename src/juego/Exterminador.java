package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.util.List;

public class Exterminador {

    private double x;
    private double y;
    private double direccion;
    private int vidas;
    private int araniasExterminadas;
    private double anguloImgExterminador;
    private static Image imgExterminador = Herramientas.cargarImagen("recursos/heroe.png");

    public Exterminador(Entorno e) {
        this.x = e.ancho() / 2;
        this.y = e.alto() / 2;
        this.direccion = 0;
        this.vidas = 3;
        this.araniasExterminadas = 0;
        this.anguloImgExterminador = Math.PI / 2;
    }

    //Modificado
    public Mina lanzarMina() {
        Mina mina = new Mina(this.x, this.y);
        return mina;
    }

    //Modificado
    public Disparo disparar() {
        Disparo disparo = new Disparo(this.x, this.y, this.direccion);
        return disparo;
    }

    public void dibujar(Entorno e) {
        e.dibujarImagen(imgExterminador, this.x, this.y, this.anguloImgExterminador, 0.6);
    }

    public void reiniciarPosicion(Entorno e) {
        this.x = e.ancho() / 2;
        this.y = e.alto() / 2;
    }

//********** Movimientos **********
    public void girarHaciaDerecha() {
        this.direccion += 0.03;
        this.anguloImgExterminador += 0.03;
    }

    public void girarHaciaIzquierda() {
        this.direccion -= 0.03;
        this.anguloImgExterminador -= 0.03;
    }

    public void moverse(Entorno e, List<Edificio> edificios, List<Telarania> telaraniasLanzadas) {
        if (pisoUnaTelarania(telaraniasLanzadas)) {
            this.x += Math.cos(direccion) * 0.1;
            this.y += Math.sin(direccion) * 0.1;
        } else {
            this.x += Math.cos(direccion) * 1;
            this.y += Math.sin(direccion) * 1;
        }
        if (chocaConEdificio(edificios)) {
            this.x -= Math.cos(direccion) * 1;
            this.y -= Math.sin(direccion) * 1;
        }
        chocaConVentana(e);
    }

//********** FuncionesAuxiliares **********
    private void chocaConVentana(Entorno e) {
        if (this.x >= (e.ancho() - imgExterminador.getWidth(null) / 3)) {
            this.x -= 1;
        }
        if (this.x <= imgExterminador.getWidth(null) / 3) {
            this.x += 1;
        }
        if (this.y >= (e.alto() - imgExterminador.getWidth(null) / 3)) {
            this.y -= 1;
        }
        if (this.y <= imgExterminador.getWidth(null) / 3) {
            this.y += 1;
        }
    }

    private boolean pisoUnaTelarania(List<Telarania> telaraniasLanzadas) {
        for (Telarania telarania : telaraniasLanzadas) {
            double distancia = FuncionesAuxiliares.calcularDistancia(
                    this.x, this.y, telarania.getX(), telarania.getY());
            if (distancia < Telarania.getAnchoImgTelarania() / 3) {
                return true;
            }
        }
        return false;
    }

    private boolean chocaConEdificio(List<Edificio> edificios) {
        for (Edificio edificio : edificios) {
            double distancia = FuncionesAuxiliares.calcularDistancia(
                    this.x,this.y,edificio.getX() ,edificio.getY());
            if (distancia <= (Edificio.getAnchoImgEdificio() / 2)) {
                return true;
            }
        }
        return false;
    }

    public void reducirVida() {
        this.vidas -= 1;
    }

    public void aumentarPuntos() {
        this.araniasExterminadas += 1;
    }

//********** Getters **********
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int vidas() {
        return vidas;
    }

    public int puntos() {
        return araniasExterminadas;
    }

    public void reiniciarVida() {
        this.vidas = 3;
    }

    public void reiniciarPuntos() {
        this.araniasExterminadas = 0;
    }

}
