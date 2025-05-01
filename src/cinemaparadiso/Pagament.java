package cinemaparadiso;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andreu Niso Sevilla
 */

public class Pagament extends JFrame {
    
    private MenuUsuari mainWinUser;
    
    public Pagament(int totalEntrades, MenuUsuari menu) {
        this.mainWinUser = menu;
        setTitle("Cinema Paradiso");
        setLayout(new BorderLayout());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        
        seleccionarEntrades(totalEntrades);
    }
    
    //Mostra la pantalla de seleccionament de tipus d'entrades
    private void seleccionarEntrades(int entrades) {
        JPanel panelTitol = new JPanel();
        JPanel panelEntrades = new JPanel();
        JPanel panelPagar = new JPanel();
        
        SpinnerNumberModel modelGeneral = new SpinnerNumberModel(0, 0, entrades, 1);
        SpinnerNumberModel modelJove = new SpinnerNumberModel(0, 0, entrades, 1);
        SpinnerNumberModel modelJubilat = new SpinnerNumberModel(0, 0, entrades, 1);
    
        panelEntrades.setLayout(new GridLayout(4, 2, 35, 15));
        
        Font fontTitol = new Font("Open Sans", Font.BOLD, 30);
        Font fontText = new Font("Source Sans Pro", Font.PLAIN, 16);
        
        JLabel jlTitol = new JLabel("TOTAL ENTRADES: " + entrades);
        jlTitol.setFont(fontTitol);
        
        JLabel jlGeneral = new JLabel("Entrades Generals: ");
        JSpinner jsGeneral = new JSpinner();
        
        JLabel jlJove = new JLabel("Entrades Joves: ");
        JSpinner jsJove = new JSpinner();
        JLabel jlJubilat = new JLabel("Entrades Jubilats: ");
        JSpinner jsJubilat = new JSpinner();
        JLabel jlTotal = new JLabel("PREU TOTAL: ");
        JTextField tfTotal = new JTextField();
        tfTotal.setEditable(false);
        tfTotal.setHorizontalAlignment(JTextField.RIGHT);
        JButton jbPagar = new JButton("PAGAR");
        
        jlGeneral.setFont(fontText);
        jsGeneral.setFont(fontText);
        jlJove.setFont(fontText);
        jsJove.setFont(fontText);
        jlJubilat.setFont(fontText);
        jsJubilat.setFont(fontText);
        jlTotal.setFont(fontText);
        tfTotal.setFont(fontText);
        jbPagar.setFont(fontText);
        
        panelTitol.add(jlTitol);
        
        panelEntrades.add(jlGeneral);
        panelEntrades.add(jsGeneral);
        panelEntrades.add(jlJove);
        panelEntrades.add(jsJove);
        panelEntrades.add(jlJubilat);
        panelEntrades.add(jsJubilat);
        panelEntrades.add(jlTotal);
        panelEntrades.add(tfTotal);
        
        panelPagar.add(jbPagar);
        
        panelTitol.setBorder(BorderFactory.createEmptyBorder(10, 40, 0, 40));
        panelEntrades.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        panelPagar.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));
        
        ChangeListener jsChangeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int general = (int) jsGeneral.getValue();
                int jove = (int) jsJove.getValue();
                int jubilat = (int) jsJubilat.getValue();
                
                int maxGeneral = entrades - jove - jubilat;
                int maxJove = entrades - general - jubilat;
                int maxJubilat = entrades - general - jove;
                
                //Fem que cada vegada que es modifica un dels spinners, es modifiqui el màxim dels altres
                modelGeneral.setMaximum(maxGeneral);
                modelJove.setMaximum(maxJove);
                modelJubilat.setMaximum(maxJubilat);

                tfTotal.setText(((general * 10) + (jove * 6) + (jubilat * 7)) + "€");
            }
        };
        
        jsGeneral.setModel(modelGeneral);
        jsGeneral.addChangeListener(jsChangeListener);
        jsJove.setModel(modelJove);
        jsJove.addChangeListener(jsChangeListener);
        jsJubilat.setModel(modelJubilat);
        jsJubilat.addChangeListener(jsChangeListener);
        
        jbPagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (((int)jsGeneral.getValue() + (int)jsJove.getValue() + (int)jsJubilat.getValue()) == entrades) {
                    JOptionPane.showMessageDialog(null, "Pagament realitzat correctament!", "Compra completada", JOptionPane.INFORMATION_MESSAGE);
                    guardarDades();
                    dispose();
                    mainWinUser.dispose();
                    mainWinUser = new MenuUsuari(); //Executa el menu de nou per reiniciar les butaques
                    mainWinUser.pack();
                    mainWinUser.setMinimumSize(mainWinUser.getSize()); //Fem que la pantalla no sigui mes petita del tamany necessari per veure els elements correctament

                    Utilitats.centrarPantalla(mainWinUser);
                } else {
                    JOptionPane.showMessageDialog(null, "Assigna totes les entrades segons el seu tipus!", "Error d'assignació", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        add(panelTitol, BorderLayout.NORTH);
        add(panelEntrades, BorderLayout.CENTER);
        add(panelPagar, BorderLayout.SOUTH);
    }
    
    private void guardarDades() {
        try {
            ArrayList<Compra> arrayCompres = new ArrayList<>();

            try {
                FileInputStream arxiuDadesIn = new FileInputStream("dades.dat");
                ObjectInputStream oisDadesIn = new ObjectInputStream(arxiuDadesIn);
                
                //He de llegir l'arxiu i guardar-lo a un ArrayList ja que sino es sobreescriu
                try {
                    Compra c;
                    while ((c = (Compra) oisDadesIn.readObject()) != null) {
                        arrayCompres.add(c);
                    }
                } catch (EOFException e) {
                    arxiuDadesIn.close();
                    oisDadesIn.close();
                }
            } catch (Exception e) {
                System.out.println("Error al carregar els arxius! Més informació: " + e.getMessage());
            }
            
            int[][] butaques = mainWinUser.getButaques();
            String nom = mainWinUser.getNom();
            String dni = mainWinUser.getDNI().toUpperCase();
            arrayCompres.add(new Compra(butaques, nom, dni));

            FileOutputStream arxiuDadesOut = new FileOutputStream("dades.dat");
            ObjectOutputStream oosDadesOut = new ObjectOutputStream(arxiuDadesOut);
            
            for (Compra c: arrayCompres) {
                oosDadesOut.writeObject(c);
            }
            oosDadesOut.close();
            arxiuDadesOut.close();
        } catch (Exception e) {
            System.out.println("Error al guardar les dades! Més informació: " + e.getMessage());
        }
    }

}