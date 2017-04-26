/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExpresionRegular;

import AFN.*;
import Excepciones.ExcepcionAutomataIncorrecto;
import Excepciones.ExcepcionCadenaNoValida;
import Excepciones.ExcepcionDatosIncorrectos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class Ventana extends javax.swing.JFrame {
    private ExpresionRegular validador;
    private AFN AutomataAFN;
    /**
     * Creates new form Ventana
     */
    public Ventana() {
        initComponents();
        validador = new ExpresionRegular();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        evaluar_expresion_regular = new javax.swing.JButton();
        campo_expresion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        evaluar_cadena = new javax.swing.JButton();
        campo_cadena = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Expresión regular:");

        evaluar_expresion_regular.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        evaluar_expresion_regular.setText("Evaluar");
        evaluar_expresion_regular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evaluar_expresion_regularActionPerformed(evt);
            }
        });

        campo_expresion.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Cadena:");

        evaluar_cadena.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        evaluar_cadena.setText("Evaluar");
        evaluar_cadena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evaluar_cadenaActionPerformed(evt);
            }
        });

        campo_cadena.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campo_expresion, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(evaluar_expresion_regular))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campo_cadena, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addComponent(evaluar_cadena)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(campo_expresion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(evaluar_expresion_regular)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(campo_cadena, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(evaluar_cadena)
                .addContainerGap(120, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void evaluar_expresion_regularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evaluar_expresion_regularActionPerformed
       try {
           validador.setAlfabeto(new String[]{"a","b","c","d"});
            validador.validarER(campo_expresion.getText());
            System.out.println("============== SE IMPRIMIRÁ UNA EXPRESIÓN REGULAR COMPLETA");
            validador.generarGrupos();
            System.out.println("Resultado = "+validador.toString());
            System.out.println("***************************************************** FIN DE LA IMPRESIÓN");
            JOptionPane.showMessageDialog(this, "La cadena '"+campo_expresion.getText()+"' es válida", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            AutomataAFN = new AFN(validador.getGrupo(), validador.getAlfabeto());
        } catch (ExcepcionDatosIncorrectos ex) {
            Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "La cadena '"+campo_expresion.getText()+"' NO es válida", "Resultado", JOptionPane.ERROR_MESSAGE);
        }
            
        
        /*AutomataAFN = new AFN(new String[]{"a","b"});
        AutomataAFN = AutomataAFN.crearAutomata(validador.getGrupo());
        
        AFNTOAFD transformador = new AFNTOAFD(AutomataAFN);
        
        Automata AutomataAFD = transformador.getAutomata();
        
        AutomataAFD.setNombre("AFNTOAFD");
        
        new Graficos(AutomataAFD, ManejoArchivo.CARPETA_IMAGENES + ManejoArchivo.SEPARADOR + AutomataAFD.getNombre()+ ".png");*/
    }//GEN-LAST:event_evaluar_expresion_regularActionPerformed

    private void evaluar_cadenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evaluar_cadenaActionPerformed
        try {
            String cadena = campo_cadena.getText();
            AutomataAFN.probarCadena(cadena);
            JOptionPane.showMessageDialog(this, "La cadena SI pertenece al lenguaje", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExcepcionCadenaNoValida | ExcepcionAutomataIncorrecto ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Resultado", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_evaluar_cadenaActionPerformed
    private boolean validar() {
        boolean valido = false;
        String cadena = evaluar_expresion_regular.getText();
        // Determinación de paridad entre paréntesis abierto y paréntesis cerrado
        int contAb = 0, contCe = 0;
        for(int i=0; i<cadena.length(); i++) {
            String caracter = ""+cadena.charAt(i);
            if ("(".equals(caracter)) contAb++;
            if (")".equals(caracter)) contCe++;
        }
        return (contAb == contCe);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Lookcampo_expresionetting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField campo_cadena;
    private javax.swing.JTextField campo_expresion;
    private javax.swing.JButton evaluar_cadena;
    private javax.swing.JButton evaluar_expresion_regular;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
