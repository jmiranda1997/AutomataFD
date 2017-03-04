/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author Wilson Xicará
 */
public class Ventana_exportar {
    private JDialog ventana;
    private JButton boton;
    private int seleccionado = -1;  // Variable que me indica cu�l item del ComboBox se ha seleccionado
    public Ventana_exportar(String[] items) {
        ventana = new JDialog();
        ventana.setModal(true);
        ventana.setLayout(null);
        ventana.setTitle("Exportar Autómata");
        // Creaci�n e inserci�n de la etiqueta
        JLabel etiqueta = new JLabel("Seleccione el Aut�mata a Exportar:");
        etiqueta.setBounds(10, 10, 200, 40);
        ventana.add(etiqueta);
        // Insersi�n del ComboBox
        JComboBox combobox = new JComboBox();
        combobox.setBounds(10, 50, 350, 30);
        for (int i = 0; i < items.length; i++) {
            combobox.addItem(items[i]);
        }
        ventana.add(combobox);
        // Creaci�n e inserci�n del Bot�n
        boton = new JButton("Seleccionar");
        boton.setBounds(150, 150, 100, 40);
        if (combobox.getItemCount() == 0) {
            etiqueta.setText("No hay autómatas para exportar");
            boton.setEnabled(false);
        }
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {   // Definici�n de la acci�n del bot�n
                seleccionado = combobox.getSelectedIndex();
                ventana.dispose();
            }
        });
        ventana.add(boton);
        ventana.setSize(400, 250);
        ventana.setLocation(500,300);
        ventana.setVisible(true);
    }
    public int getSeleccionado() { return seleccionado; }
}
