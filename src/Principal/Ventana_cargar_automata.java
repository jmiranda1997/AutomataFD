/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;
import Clases.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author pc
 */
public class Ventana_cargar_automata {
    private JComboBox listado;
    private JTextField descripcion;
    /*Automata que se crea*/
    private Automata automata;
    /* DefinicÛn de la ventana principal */
    JDialog ventana;
    /* DefiniciÛn de los botones */
    JButton cargar_automata;
    
    public Ventana_cargar_automata() {
        /* CreaciÛn de los elementos principales */
        ventana = new JDialog();
        ventana.setModal(true);
        ventana.setLayout(null);
        ventana.setTitle("Cargar Autómata");
        automata = null;
        
        /* CreaciÛn del archivo y obtenciÛn de referencias */
        ManejoArchivo archivo = new ManejoArchivo();
        IndiceAutomatas[] referencias = archivo.obtenerReferencias();
        
        /* CreaciÛn de los componentes y agregado de los mismos a la ventana */
        // CreaciÛn de la etiqueta
        
        // CreaciÛn del JTextFiel que tendr· la DescripciÛn de los AutÛmatas
        descripcion = new JTextField();
        descripcion.setBounds(25, 100, 300, 100);
        ventana.add(descripcion);
        // CraciÛn del ComboBox
        listado = new JComboBox();
        listado.setBounds(25, 50, 300, 40); // listado.setBounds(posX, posY, ancho, alto);
        listado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {   // DefiniciÛn de la acciÛn del ComboBox
                descripcion.setText(referencias[listado.getSelectedIndex()].getDescripcion());
            }
        });
        if (referencias != null) {  // DefiniciÛn de si el archivo tiene 0 registros
            for(int i=0; i<referencias.length; i++)
                listado.addItem(referencias[i].getNombreAutomata());    // Carga de los Nombres de los AutÛmatas encontrados
            descripcion.setText(referencias[listado.getSelectedIndex()].getDescripcion());
        }   
        ventana.add(listado);
        // CreaciÛn del BotÛn para confirmar la carga de los datos del autómata
        JLabel etiqueta = new JLabel("Seleccione un autÛmata de la lista:");
        etiqueta.setBounds(10,10, 250, 40); // new Font("tipo_letra", estilo_letra, tamaÒo)
        ventana.add(etiqueta);
        cargar_automata = new JButton("Cargar");
        cargar_automata.setBounds(100, 200, 150, 40);
        cargar_automata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {   // DefiniciÛn de la acciÛn del botÛn
                if(referencias!=null){
                    automata = archivo.cargarAutomata(referencias[listado.getSelectedIndex()]);
                }
                ventana.dispose();
            }
        });
        if (referencias==null) {
            etiqueta.setText("No hay Automatas Guardados");
                    cargar_automata.setEnabled(false);
        }
        ventana.add(cargar_automata);
        
        // DefiniciÛn de otros atributos de la ventana
        ventana.setSize(350, 300);
        ventana.setLocation(500,300);
        ventana.setResizable(false);
        ventana.setVisible(true);
    }
    public Automata getAutomata() { return automata; }
}