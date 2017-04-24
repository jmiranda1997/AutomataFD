/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 *
 * @author jonathanmiranda
 */
public class Arrastrar extends JPanel implements MouseListener{
    private Point[] a, b, c;
    private int NCirculos=0;
    public Automata Automata;
    Color [] color;
    boolean[] estadosmarcador;
    int val=-1;
    Point f1,f2,f3;
    String Direccion;
    
    public Arrastrar(Point[] a1, Point[] b1, Point[] c1, Automata A1, Color[] col, String D){
       f1 = new Point();
       f2 = new Point();
       f3 = new Point();
       
       this.addMouseListener(this);
       this.Automata=A1;
       this.a=a1;
       this.b=b1;
       this.c=c1;
       f1.x=c[0].x-24;
       f1.y=c[0].y;
       f2.x=c[0].x-4;
       f2.y=c[0].y+25;
       f3.x=c[0].x-24;
       f3.y=c[0].y+50;
       this.color = col;
       this.NCirculos= c.length;
       this.Direccion = D;
       estadosmarcador = new boolean[NCirculos];
       estados();
 
    }
    /**
     * Metodo que se encarga de escribir la imagen generada en el metodo privado screenShot que genera la imagen
     * PNG en la direccion seleccionada
     * @param comp El componente de donde se saca el grafico de la imagen en este caso es un JPanel
     * @param Direccion Direccion donde sera almacenada la imagen
     * @throws Exception Indica si no se puede generar la imagen
     */
    public  void guardar(Component comp, String Direccion) throws Exception{
        BufferedImage imagen = screenShot(comp);/*Generamos una variable que almacena los graficos de la imagen obtenidos en le metodo screenShot*/
        ImageIO.write(imagen, "PNG", new File(Direccion));/*Se escribe la imagen en la de extencion PNG en la dirección solicitada*/
    }
    /**
     * Genera un BufferedImagen con el tamaño y los graficos del componente enviado (en este caso JPanel)
     * @param comp Componente del cual se genera la imagen
     * @return BuferredImagen con los graficos extraidos del componente
     */
    private  BufferedImage screenShot(Component comp){
        BufferedImage imagen = new BufferedImage(comp.getWidth(), comp.getHeight(),BufferedImage.TYPE_INT_RGB);/*Genera el grafico de 
        *la imagen con las dimenciones del JPanel donde esta almacenada con el formato RGB con un fondo negro*/
        comp.paint(imagen.getGraphics());
        return imagen;   
    }
    public void estados(){
        for (int i = 0; i < NCirculos; i++) {
            estadosmarcador[i] = false;
        }
        for (int i = 0; i < NCirculos; i++) {
            if (Automata.getEstados(i).esAceptable()) {
                estadosmarcador[i]=true;
            }
        }
    }
    public void paint(Graphics g){
        
       Graphics2D g2 = (Graphics2D) g;
       g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.white);
        g2.clearRect(0, 0, 800, 600);
        g2.drawLine(f1.x, f1.y, f2.x, f2.y);
        g2.drawLine(f2.x, f2.y, f3.x, f3.y);
        Circulos(g);
        dibujarTransiciones(g);
        val=-1;
        
    }
   
    public void Circulos (Graphics g){
        for (int i = 0; i < NCirculos; i++) {
            if (estadosmarcador[i]) {Final(g, i);}
            else{ noFinal(g, i);}
        }
    }
    private void Final(Graphics g, int i){
        g.setColor(Color.green);
        g.drawOval(c[i].x, c[i].y, 50, 50);
        g.drawOval(c[i].x-4, c[i].y-4, 58, 58);
        g.fillOval(a[i].x-2, a[i].y-2, 4, 4);
        g.drawOval(b[i].x-2, b[i].y-2, 4, 4);
        g.drawString(Automata.getEstados()[i].getSimbolo(), c[i].x+20, c[i].y+30);
    }
    
    private void noFinal(Graphics g, int i){
        g.setColor(Color.red);
        g.drawOval(c[i].x, c[i].y, 50, 50);
        g.fillOval(a[i].x-2, a[i].y-2, 4, 4);
        g.drawOval(b[i].x-2, b[i].y-2, 4, 4);
        g.drawString(Automata.getEstados()[i].getSimbolo(), c[i].x+20, c[i].y+30);
    }
    public void dibujarTransiciones(Graphics g){
        // Cuando se da el caso de que un estado apunta a sí mismo, se dibuja un círuculo que es tangente al punto del
        // circuloSuperior
        // Variable que servirá para determinar el tamaño de las secciones de óvalos que se dibujarán para las transiciones hacia el mismo estado
        int contLlegada = 0;
        for (int i = 0; i < NCirculos; i++) {
            for (int j = 0; j < Automata.getCantidadSimbolos(); j++) {
                Estado aux = Automata.getEstados(i).getTransicion(j);
                int numeroEstado= buscarEstado(aux);
                g.setColor(color[j]);
                
                if (i == numeroEstado) {    // Se dibuja un círculo
                    contLlegada++;
                    int radio = 10+4*contLlegada;
                    g.drawArc(a[i].x-radio/2, a[i].y-2*radio, radio, 2*radio, -85, 300);
                } else {
                    g.drawLine(a[i].x, a[i].y, b[numeroEstado].x, b[numeroEstado].y);
                }
                g.drawString(Automata.getAlfabeto()[j], 10+(j*10), 60);
            }
        }
    }
    private int buscarEstado(Estado aux){
        int j=0;
        for (int i=0; i < Automata.getEstados().length; i++) {
            if(aux == Automata.getEstados(i)) j=i;
        }
        return j;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        int x=e.getX(), y=e.getY();
        for (int i = 0; i < NCirculos; i++) {
            if ((x>c[i].x)&&(x<(c[i].x+50))&&(y>c[i].y)&&(y<(c[i].y+50))) {
                val=i;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (val!=-1) {
            c[val].x=e.getX()-25;
            c[val].y=e.getY()-25;
            a[val].x=e.getX();
            a[val].y=e.getY()-35;
            b[val].x=e.getX();
            b[val].y=e.getY()+31;
            if(val==0){
                f1.x=c[0].x-24;
                f1.y=c[0].y;
                f2.x=c[0].x-4;
                f2.y=c[0].y+25;
                f3.x=c[0].x-24;
                f3.y=c[0].y+50;            
            }
            paint(this.getGraphics());
             try {
                    guardar(this, Direccion);
                } catch (Exception ex) {
                    Logger.getLogger(Arrastrar.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}

