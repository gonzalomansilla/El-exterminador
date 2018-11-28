package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Juego extends InterfaceJuego {

    private Entorno entorno;
    private Exterminador exterminador;
    private List<Arania> aranias;
    private List<Edificio> edificios;
    private List<Telarania> telaraniasLanzadas;

    private List<Disparo> disparos;
    private List<Mina> minas;

    private String jugador;
    private int ticks;
    private int tiempoDeGeneradoDeAranias;
    private int nivel;
    private int objetivo_AraniasAexterminar;
    private int objetivo;
    private boolean seCambiaDeNivel;
    private boolean finalizoJuego;
    private static File records;
    private Scanner teclado;
    private static Image imgFondo = Herramientas.cargarImagen("recursos/fondo.jpg");
    private static Image imgGameOver = Herramientas.cargarImagen("recursos/gameover.png");

    Juego() {
        this.ticks = 0;
        this.entorno = new Entorno(this, "El Exterminador - Grupo Mansilla - Mercado - Schneider - V0.01", 800, 600);
        this.exterminador = new Exterminador(entorno);
        this.edificios = new ArrayList<>();
        this.aranias = new ArrayList<>();
        this.disparos = new ArrayList<>();
        this.minas = new ArrayList<>();
        this.telaraniasLanzadas = new ArrayList<>();
        this.tiempoDeGeneradoDeAranias = 600;
        this.objetivo_AraniasAexterminar = 5;
        this.objetivo = 5;
        this.nivel = 1;

        generarEdificios();
        this.teclado = new Scanner(System.in);
        System.out.print("Nombre del jugador: ");
        this.jugador = teclado.next();
        //Toma la ruta relativa del archivo(Es diferente en cada PC)
        String direccion = new File("src/recursos/records.txt").getAbsolutePath();
        this.records = new File(direccion);

        this.entorno.iniciar();
    }

    @Override
    public void tick() {
        if (finalizoJuego) {
            dibujarRecords();
            entorno.cambiarFont("Arial", 20, Color.WHITE);
            entorno.escribirTexto("Presiona Enter para volver a jugar ", 250, 525);
            if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                reiniciarJuego();
            }
        } else {
            this.ticks++;
            this.entorno.dibujarImagen(imgFondo, entorno.ancho() / 2, entorno.alto() / 2, 0, 0.9);
            dibujarEdificios();

            entorno.cambiarFont("Arial", 18, Color.GREEN);
            entorno.escribirTexto("Vidas: " + exterminador.vidas(), 25, 20);
            entorno.escribirTexto("Puntos: " + exterminador.puntos(), 700, 20);

            exterminador.dibujar(entorno);

            if (this.ticks % tiempoDeGeneradoDeAranias == 0) {
                generarAranias();
            }
            dibujarAranias();

            generarTelaranias();
            dibujarTelaranias();

            //Modificados
            verificarImpactoDeProyectiles(entorno, aranias, edificios);
            verificarExplosionesDeMinas();
            dibujarExplosiones();
            eliminarMinasExplotadas();

            //********** Acciones del exterminador **********
            //Modificados
            if (entorno.sePresiono('m')) {
                minas.add(exterminador.lanzarMina());

            }
            dibujarMinas();

            if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
                disparos.add(exterminador.disparar());
            }
            dibujarDisparos();

            //********** Moviminetos del exterminador **********
            if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
                exterminador.moverse(entorno, edificios, this.telaraniasLanzadas);
            }

            if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
                exterminador.girarHaciaDerecha();
            }

            if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
                exterminador.girarHaciaIzquierda();
            }

            //********** - **********
            entorno.escribirTexto("Nivel: " + nivel, 700, 40);
            if (exterminador.puntos() >= 1 && exterminador.puntos() / objetivo_AraniasAexterminar == 1) {
                seCambiaDeNivel = true;
                aumentarNivel();
            }

            if (araniaAlcanzoAlExterminador()) {
                finalizarJuego();
                return;
            }
        }

    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }

    //********** Metodos nuevos y corregidos **********
    public void verificarImpactoDeProyectiles(Entorno e, List<Arania> aranias, List<Edificio> edificios) {
        Iterator<Disparo> i_Disparos = this.disparos.iterator();
        while (i_Disparos.hasNext()) {
            Disparo disparo = i_Disparos.next();
            if (disparo.impactaConVentana(e)) {
                eliminarDisparo(disparo, i_Disparos);
            }
            if (disparo.impactaConArania(disparo, aranias, this.exterminador)) {
                exterminador.aumentarPuntos();
                eliminarDisparo(disparo, i_Disparos);
            }
            if (disparo.impactoConMina(minas, e)) {
                eliminarDisparo(disparo, i_Disparos);
            }
            if (disparo.impactaConEdificio(disparo, edificios)) {
                eliminarDisparo(disparo, i_Disparos);
            }
        }
    }

    private void eliminarDisparo(Disparo disparo, Iterator<Disparo> i_Disparos) {
        if (this.disparos.contains(disparo)) {
            i_Disparos.remove();
            return;
        }
    }

    private void verificarExplosionesDeMinas() {
        if (!minas.isEmpty()) {
            //Verifica si una mina fue pisada o si la misma activo otras minas
            for (Mina mina : minas) {
                Iterator<Arania> i_Aranias = aranias.iterator();
                while (i_Aranias.hasNext()) {
                    Arania arania = i_Aranias.next();
                    //Comprueba las que no explotaron
                    if (!mina.generoExplosion()) {
                        if (mina.fuePisada(arania, entorno)) {
                            mina.generarExplosion();
                            exterminador.aumentarPuntos();
                            if (aranias.contains(arania)) {
                                i_Aranias.remove();
                            }
                        }
                    }
                }
                mina.activarMinasCercanas(minas, entorno);
            }
        }
        if (explosionAlcanzoAlExterminador()) {
            finalizarJuego();
        }
        eliminarAraniasAlcanzadasPorExplosion();
    }

    private void eliminarMinasExplotadas() {
        Iterator<Mina> i_Minas = minas.iterator();
        while (i_Minas.hasNext()) {
            Mina mina = i_Minas.next();
            if (mina.generoExplosion()) {
                if (this.ticks % 10 == 0) {
                    if (minas.contains(mina)) {
                        i_Minas.remove();
                    }
                }
            }
        }
    }

    private void eliminarAraniasAlcanzadasPorExplosion() {
        if (!minas.isEmpty()) {
            for (Mina mina : minas) {
                Iterator<Arania> i_Aranias = aranias.iterator();
                while (i_Aranias.hasNext()) {
                    Arania arania = i_Aranias.next();
                    //Compara con las aranias con las minas que explotaron
                    //Soluciono el error: No eliminaba las minas tocadas por aranias
                    if (mina.generoExplosion()) {
                        double distancia = FuncionesAuxiliares.calcularDistancia(
                                mina.getX(), mina.getY(), arania.getX(), arania.getY());
                        if (distancia < mina.getRadioExplosion()) {
                            exterminador.aumentarPuntos();
                            if (aranias.contains(arania)) {
                                i_Aranias.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    private void dibujarExplosiones() {
        for (Mina mina : minas) {
            if (mina.generoExplosion()) {
                mina.dibujarExplosion(entorno);
            }
        }
    }

    //********** *********
    private void reiniciarJuego() {
        this.telaraniasLanzadas.clear();
        this.aranias.clear();
        this.minas.clear();
        this.disparos.clear();
        this.exterminador.reiniciarPosicion(entorno);
        this.exterminador.reiniciarVida();
        this.exterminador.reiniciarPuntos();
        this.tiempoDeGeneradoDeAranias = 600;
        this.seCambiaDeNivel = false;
        this.objetivo_AraniasAexterminar = 5;
        this.objetivo = 5;
        this.nivel = 1;
        this.finalizoJuego = false;
        reposicionarEdificios();

        tick();
    }

    private void aumentarNivel() {
        if (seCambiaDeNivel) {
            this.telaraniasLanzadas.clear();
            this.aranias.clear();
            this.minas.clear();
            this.disparos.clear();
            this.exterminador.reiniciarPosicion(entorno);
            reposicionarEdificios();
            this.nivel++;
            objetivo += 5;
            this.objetivo_AraniasAexterminar = exterminador.puntos() + objetivo;
            this.seCambiaDeNivel = false;
            if (this.tiempoDeGeneradoDeAranias > 250) {
                this.tiempoDeGeneradoDeAranias -= 50;
            }
        }
    }

    void reposicionarEdificios() {
        this.edificios.clear();
        generarEdificios();
    }

    private void finalizarJuego() {
        escrbirDatosDelJugador();
    }

    private void dibujarRecords() {
        entorno.dibujarImagen(imgGameOver, entorno.ancho() / 2, entorno.alto() / 2, 0, 1.1);
        entorno.cambiarFont("Arial", 20, Color.ORANGE);

        BufferedReader br = null;
        FileReader fr = null;
        String linea = "";
        int puntoY = 50;

        try {
            fr = new FileReader(records);
            br = new BufferedReader(fr);
            //mientras tenga texto para leer
            while ((linea = br.readLine()) != null) {
                entorno.escribirTexto(linea, 25, puntoY);
                puntoY += 20;
            }
            //Solo para captuar alguna excepcion
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void escrbirDatosDelJugador() {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            //Obtengo el archivo records para ser escrito. si es true escribe al final del txt
            fw = new FileWriter(records, true);
            bw = new BufferedWriter(fw);
            String lineaTexto = this.jugador + " - " + exterminador.puntos();
            //Escribo el String con un salto de linea
            bw.write(lineaTexto + "\r\n");

        } catch (IOException e) {
            System.out.println("Error : " + e.getMessage());
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }

            } catch (IOException e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
    }

    private boolean explosionAlcanzoAlExterminador() {
        for (Mina mina : minas) {
            if (mina.generoExplosion()) {
                double distancia = FuncionesAuxiliares.calcularDistancia(
                        exterminador.getX(), exterminador.getY(), mina.getX(), mina.getY());
                if (distancia < mina.getRadioExplosion()) {
                    if (ticks % 10 == 0) {
                        exterminador.reducirVida();
                    }
                    if (exterminador.vidas() == 0) {
                        finalizoJuego = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean araniaAlcanzoAlExterminador() {
        for (Arania arania : aranias) {
            double distancia = FuncionesAuxiliares.calcularDistancia(
                    arania.getX(), arania.getY(), exterminador.getX(), exterminador.getY());
            if (distancia < Arania.getAnchoImgArania() / 2) {
                if (ticks % 70 == 0) {
                    exterminador.reducirVida();
                }
                if (exterminador.vidas() == 0) {
                    finalizoJuego = true;
                    return true;
                }

            }
        }
        return false;
    }

//********** Generado de figuras **********
    public void generarEdificios() {
        int numEdificiosAgenerar = (int) (Math.random() * (8 - 4 + 1) + 4);
        int totalEdificios = 0;
        while (totalEdificios < numEdificiosAgenerar) {
            double x = Math.random() * entorno.ancho();
            double y = Math.random() * entorno.alto();
            if (x < 300 || x > 500 || y < 200 || y > 400) {
                Edificio e = new Edificio(x, y);
                edificios.add(e);
                totalEdificios++;
            }
        }
    }

    public void generarAranias() {
        // Superior
        double xRandom;
        xRandom = (int) (Math.random() * entorno.ancho());
        aranias.add(new Arania(xRandom, 0, Math.PI / 2, Math.PI));

        // Izquierda
        double yRandom;
        yRandom = (int) (Math.random() * entorno.alto());
        aranias.add(new Arania(0, yRandom, 0, Math.PI / 2));

        // Derecha
        yRandom = (int) (Math.random() * entorno.ancho());
        aranias.add(new Arania(entorno.ancho(), yRandom, Math.PI, 3 * Math.PI / 2));

        // Inferior
        xRandom = (int) (Math.random() * entorno.ancho());
        aranias.add(new Arania(xRandom, entorno.alto(), 3 * Math.PI / 2, 0));
    }

    public void generarTelaranias() {
        if (!aranias.isEmpty()) {
            if (this.ticks % 200 == 0) {
                int araniaElegida = (int) (Math.random() * aranias.size());
                double xArania = aranias.get(araniaElegida).getX();
                double yArania = aranias.get(araniaElegida).getY();
                telaraniasLanzadas.add(aranias.get(araniaElegida).lanzarTelarania(xArania, yArania));
            }
        }
    }

//********** Dibujado de figuras **********
    public void dibujarTelaranias() {
        if (!this.telaraniasLanzadas.isEmpty()) {
            Iterator<Telarania> i_telarania = this.telaraniasLanzadas.iterator();
            while (i_telarania.hasNext()) {
                Telarania t = i_telarania.next();
                t.dibujar(entorno);
                if (ticks % 100 == 0) {
                    t.restarTiempoDeVida();
                }
                if (t.getTiempoDeVida() <= 0) {
                    if (this.telaraniasLanzadas.contains(t)) {
                        i_telarania.remove();
                    }
                }
            }
        }
    }

    public void dibujarAranias() {
        for (Arania arania : aranias) {
            arania.dibujar(entorno);
            arania.perseguirExterminador(exterminador, entorno, edificios);
        }
    }

    //Modificado
    public void dibujarDisparos() {
        for (Disparo disparo : disparos) {
            disparo.dibujarDisparo(entorno);
            disparo.realizarTrayecto();
        }
    }

    public void dibujarEdificios() {
        for (Edificio e : edificios) {
            e.dibujar(entorno);
        }
    }

    //MOdificado
    public void dibujarMinas() {
        if (!minas.isEmpty()) {
            for (Mina mina : minas) {
                mina.dibujar(entorno);
            }
        }
    }

}
