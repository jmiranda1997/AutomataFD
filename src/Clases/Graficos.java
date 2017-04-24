/*
 * Jonathan Miranda Todos los derechos reservados
 */
package Clases;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.io.File;
import javax.swing.*;
import javax.imageio.ImageIO;


/**
 *@author Jonathan Miranda j.miranda1997@gmail.com
 * Esta clase se encarga de generar los graficos de los automatas Finitos Deterministas segun un automata y 
 * genera una imagen PNG para poder abrirla en cualquier momento.
 */
public final class Graficos {

    private IOGraficos Panel_Grafico; /*Instancia a la clase Grficos*/
    private Automata Automata;/*Crea un Automata que es usado en los pricedimientosinternos*/
    /**
     * Constructor vacío para la clase Grafivos
     */
    private Graficos() {
    }
    
    /**
     * Contructor general de la calase que maneja la Clase @IOGraficos la cual con la ayuda de el metodo guardar
     * genera una imagen con formato PNG con fondo negro.
     * @param A Objeto de tipo @Automata 
     * @param Direccion String con la direccion donde se almacenara la imagem 
     */
    public Graficos(Automata A, String Direccion) {
        this.Automata=A; /*Guarda el objeto de tipo @Automata en la variable publica*/
        //Inicia bloque que genera el Grafico
        Panel_Grafico  = new IOGraficos(Automata);/*Se instancia la clase @IOGraficos enviando el Automata que envia un JPanel*/
        JFrame f =new JFrame(Direccion);/*Se crea un JFrame para almacenar el panel con el titulo de la direccion donde se almacenara la imagen*/
        f.setSize(800,600);/*Tamaño del JFrame*/
        f.setContentPane(Panel_Grafico);/*Se añade el panel al JFrame*/
        f.setVisible(true);/*Se hace visible*/
        f.setResizable(false);/*Se quita la opción de poder cambiar el tamaño del JFrame*/
        f.dispose();/*Se cierra instantanea mente*/
        //Finalisa el bloque que genera los graficos
        
        /*Se intenta guardar la imagen del panel en la direccion seleccionada*/
        try{guardar(Panel_Grafico, Direccion)/*Se llama a metodo para guardar*/;}
        catch(Exception e){
            System.out.println(e.getMessage())/*Se imprime el mesaje de error si es que sucede*/;}
    }
    
    /**
     * Metodo que se encarga de generar un panel dinamico para poder reposicionar los estados dentro del panel
     * y luego guardar una imagen PNG en la direccion enviada
     * @param Direccion direccion donde se almacenara la imgen PNG
     */
    public void mover(String Direccion, Timer reloj){
        Arrastrar panel = new Arrastrar(Panel_Grafico.getcirculoSuperior(), 
                Panel_Grafico.getcirculoInferior(), Panel_Grafico.getcirculoEstado(), Automata, Panel_Grafico.getColores(), Direccion);/*Se insatancia la clase @Arrastrar la cual regresa un JFrame y 
                *se le envia un arreglo de puntos a,b,c los cuales contienen las coordenadas de los circulos que se utilizan para dibujar el automata*/
        JFrame f =new JFrame(Direccion);/*Se crea el JFrame que se mostrara par modificar el grafico del Automata*/
        f.setSize(800,600);/*Tamaño del JFrame*/
        f.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                reloj.start();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                reloj.stop();
            }

            @Override
            public void windowClosed(WindowEvent e) {
               
            }

            @Override
            public void windowIconified(WindowEvent e) {
                
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                
            }

            @Override
            public void windowActivated(WindowEvent e) {
           
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
               
            }
        });
        f.setResizable(false);/*Se quita la opción para poder cambir el tamaño del JFrame*/ 
        f.add(panel);/*Se Añade el panel que se obtuvo de la instancia de la clase Arrastrar*/
        f.setVisible(true);/*Se muestra el Panel*/
        /*NOTA: en este caso no se guarda la imagen ya que se maneja dentro de la clase @Arrastar cadavez que se 
        *mueve un estado*/
        
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
}

/**
 * Esta clase se encarga de generar un panel con graficos de los automatas Finitos Deterministas segun un automata 
 * @author jonathan Miranda j.miranda1997@gmail.com
 */
class IOGraficos  extends JPanel{
    private Estado[] estados;/*Arreglo de estados donde se almacenaran todos los estados del automata*/
    private int cantidadEstados;/*El numero de estados dentro del automata*/
    private int x=10, y=100;/*Posicion Inicial para todos los graficos*/
    private Point[] circuloSuperior;/*Arreglo de puntos que contiene las cordenadas para cada circulo superior de salida para las transiciones*/
    private Point[] circuloInferior;/*Arreglo de puntos que contiene las cordenadas para cada circulo inferior de entrada para las transiciones*/
    private Point[] circuloEstado;/*Arreglo de puntos que contiene las cordenadas para cada circulo del estado*/
    private String[] alfabeto;/*Guarda el alfabeto del Automata*/
    private Color[] colores;/*Guarda colores para poder asignarcelo a cada simbolo del alfabeto*/
    private Automata Automata;/*Guarda el automata usado para obtener los datos antes mencionado*/

    /**
     * Constructor vacío de la clase
     */
    public IOGraficos() {
    }

    /**
     * Constructor de la clase que almacena el Automata, almacena e iniciliza las variables y genera el Jpanel
     * con el grafico del Automata 
     * @param A Automata del cual se contruye el Grafico
     */
    public IOGraficos(Automata A){
        this.Automata=A;/*Guardamos el Automata recivido*/
         
        estados= Automata.getEstados();/*Guardamos los estados del automata*/
        cantidadEstados = estados.length;/*Guarda la cantidad de estados en el Automata*/
        circuloSuperior = new Point[cantidadEstados];/*Inicializa el arreglo*/
        circuloInferior = new Point[cantidadEstados];/*Inicializa el arreglo*/
        circuloEstado = new Point[cantidadEstados];/*Inicializa el arreglo*/
        alfabeto = Automata.getAlfabeto();/*Guarda el alfabeto del Automata*/
    }
    
    /**
     * Metodo para obtener el arreglo de puntos que forman los circulos superiores
     * @return arreglo de puntos de los circulos superiores
     */
    public Point[] getcirculoSuperior() {
        return circuloSuperior;
    }
    
    /**
     * Metodo para obtener el arreglo de puntos que forman los circulos inferiores
     * @return arreglo de puntos de los circulos inferiores
     */
    public Point[] getcirculoInferior() {
        return circuloInferior;
    }
    
    /**
     * Metodo para obtener el arreglo de puntos que forman los circulos de estados
     * @return arreglo de puntos de los circulos de estados
     */
    public Point[] getcirculoEstado() {
        return circuloEstado;
    }
    
    /**
     * Metodo para obtener el arreglo de colores que se usan para el alfabeto
     * @return arreglo con los colores del tamaño de el numero de estados 
     */
    public Color[] getColores() {
        return colores;
    }
    
    /**
     * Metodo con el que se generan los graficos utilizando una variable de tipo graficos 
     * @param g Variable de tipo Graficos
     */
     @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g; /*Se le dan las propiedades de Graphics2D a ka variable de graficos*/
        g2.setStroke(new BasicStroke(2));/*Se define el ancho del pincel para las imagenes*/
        g2.setColor(Color.white);/*Cambio de color para señalar el estado inicial*/
        g2.drawLine(x+26, y, x+46, y+25);/*Se dibuja la primer linea para la felcha que indica la posicion inicial*/
        g2.drawLine(x+46, y+25, x+26, y+50);/*se dibuja la degunda linea para la flecha que indica la posicion inical*/
        dibujarEstado1_2(g);/*Se llama al metodo que genera los circulos*/
        dibujarTransiciones(g);/*Se llama al metodo que realiza las transiciones del automata*/
        
    }   
    
    /**
     * Metodo que fibuja los circulos de los estados con un circulo superior que indica que sale una transicion
     * y el circulo inferiror que indica que recive una transicion 
     * @param g Variable Grafica que se usa durante todo el programa obtenida de @paintComponent
     */
    private void dibujarEstado1_2(Graphics g){
        for (int i = 0; i < cantidadEstados; i++) {
             if (estados[i].esAceptable()) {
                 circuloSuperior[i]= new Point();circuloInferior[i]= new Point();circuloEstado[i]= new Point();
                 g.setColor(Color.GREEN);
                 circuloEstado[i].x=x+50;
                 circuloEstado[i].y=y;
                 g.drawOval(x+50, y, 50, 50);
                 g.drawOval(x+46, y-4, 58, 58);
                 g.drawString(Automata.getEstados()[i].getSimbolo(), x+70, y+30);
                 circuloSuperior[i] = new Point(x+75,y-12);
                 g.fillOval(circuloSuperior[i].x, circuloSuperior[i].y, 4, 4);
                 circuloInferior[i] = new Point(x+75,y+58);
                 g.drawOval(circuloInferior[i].x, circuloInferior[i].y, 4, 4);
                 circuloSuperior[i].x= circuloSuperior[i].x+2;
                 circuloSuperior[i].y= circuloSuperior[i].y+2;
                 circuloInferior[i].x= circuloInferior[i].x+2;
                 circuloInferior[i].y= circuloInferior[i].y+2;
              
                 x+=70;
             }else{
                 circuloSuperior[i]= new Point();circuloInferior[i]= new Point();circuloEstado[i]= new Point();
                 circuloEstado[i].x=x+50;
                 circuloEstado[i].y=y;
                 g.setColor(Color.RED);
                 g.drawOval(x+50, y, 50, 50);
                 g.drawString(Automata.getEstados()[i].getSimbolo(), x+70, y+30);
                 circuloSuperior[i] = new Point(x+75,y-12);
                 g.fillOval(circuloSuperior[i].x, circuloSuperior[i].y, 4, 4);
                 circuloInferior[i] = new Point(x+75,y+58);
                 g.drawOval(circuloInferior[i].x, circuloInferior[i].y, 4, 4);
                 circuloSuperior[i].x= circuloSuperior[i].x+2;
                 circuloSuperior[i].y= circuloSuperior[i].y+2;
                 circuloInferior[i].x= circuloInferior[i].x+2;
                 circuloInferior[i].y= circuloInferior[i].y+2;
                 
                 x+=70;
             }

         }
    }
    /**
     * 
     * @param g 
     */
    private void dibujarTransiciones(Graphics g){
        definirColores();
        // Cuando se da el caso de que un estado apunta a sí mismo, se dibuja un círuculo que es tangente al punto del
        // circuloSuperior
        // Variable que servirá para determinar el tamaño de las secciones de óvalos que se dibujarán para las transiciones hacia el mismo estado
        int contLlegada = 0;
        for (int i = 0; i < cantidadEstados; i++) {
            for (int j = 0; j < alfabeto.length; j++) {
                Estado aux = estados[i].getTransicion(j);
                int numeroEstado= buscarEstado(aux);
                g.setColor(colores[j]);
                if (i == numeroEstado) {    // Se dibuja un círculo
                    contLlegada++;
                    int radio = 10+4*contLlegada;
//                    g.drawOval(circuloSuperior[i].x-radio, circuloSuperior[i].y-2*radio, 2*radio, 2*radio);
                    g.drawArc(circuloSuperior[i].x-radio/2, circuloSuperior[i].y-2*radio, radio, 2*radio, -85, 300);
                } else {
                    g.drawLine(circuloSuperior[i].x, circuloSuperior[i].y, circuloInferior[numeroEstado].x, circuloInferior[numeroEstado].y);
                }
                g.drawString(alfabeto[j], 10+(j*10), y-40);
            }
        }
    }
    /**
     * 
     * @param aux
     * @return 
     */
    private int buscarEstado(Estado aux){
        int j=0;
        for (int i=0; i < cantidadEstados; i++) {
            if(aux == estados[i]) j=i;
        }
        return j;
    }
    /**
     * 
     */
    private void definirColores(){
        Random R= new Random(),G= new Random(),B= new Random();
       
        colores= new Color[alfabeto.length];
        for (int i = 0; i < alfabeto.length; i++) {
           int r= R.nextInt(255);
           int g= G.nextInt(255);
           int  bl= B.nextInt(255);
            B.ints(0, 255);
            colores[i] = new Color(r, g,  bl);
        }
    }
}
