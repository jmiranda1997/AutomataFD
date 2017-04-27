/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.beans.value.ChangeListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author USUARIO
 */
public class Ventana_crear_Automata  {
    JPanel panel1,descripcion;
    JLabel simbolos, estados, nombre, des,expresion;
    JTextField[] cajas;
    JTextArea caja_descripcion;
    JButton Crear;
    JDialog ventana;
    VentanaAutomata automata;
    JScrollPane Caja_Comentario;
    JRadioButton tipo[];
    int Can_simbolos, Can_estados;

    public Ventana_crear_Automata() {
          ventana = new JDialog();
        ventana.setLayout(null);
        tipo = new JRadioButton[3];
        tipo[0] = new JRadioButton("Determinista");
        tipo[0].setSelected(true);
        tipo[0].addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent ae) {
        
                      if(tipo[0].isSelected()){
                          tipo[1].setSelected(false);
                          tipo[2].setSelected(false);
                          cajas[3].setEnabled(false);
                      }else{
                          tipo[1].setSelected(true);
                          tipo[2].setSelected(false);
                      }
                  
            }
          });
        tipo[1] = new JRadioButton("No Determinista");
        tipo[1].setSelected(false);
        tipo[1].addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent ae) {
                  if(tipo[1].isSelected()){
                          tipo[0].setSelected(false);
                          tipo[2].setSelected(false);
                          cajas[3].setEnabled(false);
                      }else{
                          tipo[0].setSelected(true);
                          tipo[2].setSelected(false);
                      }
             }
          });
        tipo[2] = new JRadioButton("Expresion Regular");
        tipo[2].setSelected(false);
        tipo[2].addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent ae) {
                  if(tipo[2].isSelected()){
                          tipo[0].setSelected(false);
                          tipo[1].setSelected(false);
                          cajas[3].setEnabled(true);
                      }else{
                          tipo[0].setSelected(true);
                          tipo[1].setSelected(false);
                          cajas[3].setEnabled(false);
                      }
             }
          });
        
        ventana.setModal(true);
        automata = null;
        nombre = new JLabel("Nombre del Automata: ");
        simbolos = new JLabel("Cantidad de Simbolos: ");
        estados = new JLabel("Cantidad de Estados: ");
        des = new JLabel("Descripcion: ");
        expresion = new JLabel("Expresion Regular");
        cajas = new JTextField[4];
        Crear = new JButton("Crear");
        Crear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String C_simbolos = cajas[0].getText();
                String C_estados = cajas[1].getText();
                String nombre = cajas[2].getText();
                String desrip = caja_descripcion.getText();
                C_simbolos.replaceAll(" ", "");
                C_estados.replaceAll(" ", "");
                
                  if((C_estados.length()==0 || C_simbolos.length()==0)&& (tipo[0].isSelected() || tipo[1].isSelected())){
                          JOptionPane.showMessageDialog(ventana, "El campo de simbolos o estados esta vacio", "Error", JOptionPane.ERROR_MESSAGE, null);
                  }
                  else if(C_simbolos.length() == 0) JOptionPane.showMessageDialog(ventana, "El campo de simbolos esta vacio", "Error", JOptionPane.ERROR_MESSAGE, null);
                  else{
                      if(tipo[0].isSelected() || tipo[1].isSelected() )
                      Can_estados = Integer.parseInt(C_estados);
                      else Can_estados = 1;
                      Can_simbolos = Integer.parseInt(C_simbolos);
                      if(Can_estados==0 || Can_simbolos == 0){
                          JOptionPane.showMessageDialog(ventana, "No se puede crear un Automata con 0 estados o 0 simbolos", "Error", JOptionPane.ERROR_MESSAGE, null);
                      }
                      else {
                          String exp = cajas[3].getText().trim();
                          if(tipo[0].isSelected()){
                            automata = new VentanaAutomata(Can_estados, Can_simbolos,1,exp);
                            ventana.dispose();
                            if(automata.getNuevo()!=null ){
                            automata.getNuevo().setNombre(nombre);
                            automata.getNuevo().setDescripcion(desrip);
                            ventana.dispose();}
                          }
                          else if(tipo[1].isSelected()){
                              automata = new VentanaAutomata(Can_estados, Can_simbolos,2,exp);
                              ventana.dispose();
                              if(automata.getNuevo()!=null ){
                              automata.getNuevo().setNombre(nombre);
                              automata.getNuevo().setDescripcion(desrip);
                              ventana.dispose();}
                          }else{
                              automata = new VentanaAutomata(Can_estados, Can_simbolos,3,exp);
                              ventana.dispose();
                              if(automata.getNuevo()!=null ){
                              automata.getNuevo().setNombre(nombre);
                              automata.getNuevo().setDescripcion(desrip);
                              ventana.dispose();}
                          }
                          
                      }
                  }
            }
        });
        Crear();
        
        
        panel1.setBounds(10, 10, 300, 140);
        descripcion.setBounds(10,150,300,90);
        ventana.add(panel1);
        ventana.add(descripcion);
       // guardar.addActionListener(this);
        Crear.setBounds(110, 260, 100, 30);
       
        ventana.add(Crear);
        ventana.addWindowListener(new WindowAdapter() {
            
             public void windowClosing(WindowEvent e){
                
                ventana.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                ventana.dispose();
             }
        });
        
        ventana.setLocation(320,200);
        ventana.setTitle("Crear Automata");
        //vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(330, 330);
        ventana.setResizable(false);
        ventana.setVisible(true);
    }
    
    
    
    
    
        public String texto1="";
    private void Crear() {
        panel1 = new JPanel(new GridLayout(6,2));
        descripcion = new JPanel();

        caja_descripcion = new JTextArea(3,25);
        caja_descripcion.setPreferredSize(new Dimension(290,80));
        caja_descripcion.setLineWrap(true); // Propiedad para Multilinea
        caja_descripcion.setWrapStyleWord(true);
        caja_descripcion.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (caja_descripcion.getText().length()>255 && e.getKeyChar()!=127) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        Caja_Comentario = new JScrollPane(caja_descripcion, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        descripcion.add(Caja_Comentario);
        cajas[0] = new JTextField();
        cajas[1] = new JTextField();
        cajas[0].addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char letra =e.getKeyChar();
                if (letra>57 || letra<48) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
               
            }
        });
        cajas[1].addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char letra =e.getKeyChar();
                if (letra>57 || letra<48) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
               
            }
        });
        cajas[2] = new JTextField();
        cajas[3] = new JTextField();
        cajas[3].setEnabled(false);
        panel1.add(nombre);
        panel1.add(cajas[2]);
        panel1.add(simbolos);
        panel1.add(cajas[0]);
        panel1.add(estados);
        panel1.add(cajas[1]);
        panel1.add(expresion);
        panel1.add(cajas[3]);
        panel1.add(tipo[0]);
        panel1.add(tipo[1]);
        panel1.add(tipo[2]);
        panel1.add(des);
        
 
    }

    public VentanaAutomata getAutomata() {
        return automata;
    }
    
}
