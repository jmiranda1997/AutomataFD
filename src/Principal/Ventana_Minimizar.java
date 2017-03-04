/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import Clases.Automata;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author USUARIO
 */
public class Ventana_Minimizar {
    JDialog ventana;
    JPanel panel1;
    JLabel nombre;
    JTextField caja_nombre;
    JButton crear;
    Automata nuevo;

    public Ventana_Minimizar(Automata automata) {
//        nuevo = automata;
        ventana = new JDialog();
        panel1 = new JPanel(new GridLayout(1,2));
        nombre = new JLabel("Nombre: ");
        caja_nombre = new JTextField();
        crear = new JButton();
        ventana.setLayout(null);
        ventana.setModal(true);
        crear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String nombre = caja_nombre.getText();
                 nuevo = automata.minimizar();
                 nuevo.setNombre(nombre);
                 ventana.dispose();
            }
        });
        panel1.setBounds(10,10,280,30);
        panel1.add(nombre);
        panel1.add(caja_nombre);
        crear.setText("Minimizar");
        crear.setBounds(120, 60, 100, 30);
        ventana.add(panel1);
        ventana.add(crear);
        
        ventana.setLocation(300,150);
        ventana.setSize(300,150);
        
        
        ventana.setResizable(false);
        ventana.setVisible(true);
        
    }

    public Automata getNuevo() {
        return nuevo;
    }
    
    
}
