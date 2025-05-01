package cinemaparadiso;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Comparator;
import java.util.TreeSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * @author Andreu Niso Sevilla
 */

public class MenuAdmin extends JFrame {

    private final int FILES = 8, COLUMNES = 15;
    private JTextArea taEntrades;
    private JTextField tfNom;
    private JComboBox cbDNI;
    private TreeSet<String> entradesSeleccionades;
    private Font fontText = new Font("Source Sans Pro", Font.PLAIN, 14);
    private int butaques[][] = new int[FILES][COLUMNES]; //0 indicara que s'ha comprat, 1 indicarà que està seleccionada i 2 que està disponible.
    
    private JButton[][] jbButaca = new JButton[FILES][COLUMNES];
    private ImageIcon butacaLliure = new ImageIcon("src/imatges/butacaLliure.png");
    private ImageIcon butacaSeleccionada = new ImageIcon("src/imatges/butacaSeleccionada.png");
    private ImageIcon butacaOcupada = new ImageIcon("src/imatges/butacaOcupada.png");
        
    public MenuAdmin() {
        setTitle("Cinema Paradiso");
        setLayout(new BorderLayout());
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener (new WindowAdapter() {
           public void windowClosing (WindowEvent e) {
               new CinemaParadiso().main(new String[0]); //Executa el main de nou per a reiniciar el programa
           } 
        });
        setResizable(true);
        setVisible(true);

        mostrarTitol();
        mostrarDades();
        mostrarButaques();
    }

    //Mostra l'apartat del títol
    private void mostrarTitol() {
        JPanel panelTitol = new JPanel();
        ImageIcon titol = new ImageIcon("src/imatges/titol.png");
        JLabel jlTitol = new JLabel(titol);
        panelTitol.setBackground(new Color(153, 102, 204));
        panelTitol.add(jlTitol);
        add(panelTitol, BorderLayout.NORTH);
    }

    //Mostra l'apartat on s'introdueixen les dades
    private void mostrarDades() {
        JPanel panelDades = new JPanel();
        panelDades.setLayout(new BoxLayout(panelDades, BoxLayout.Y_AXIS));
        panelDades.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        JLabel labelNom = new JLabel("Nom del comprador:");
        tfNom = new JTextField(20);
        tfNom.setMaximumSize(new Dimension(400, 30));
        labelNom.setFont(fontText);
        tfNom.setFont(fontText);
        tfNom.setEnabled(false);
        
        JLabel labelDNI = new JLabel("DNI del comprador:");
        cbDNI = new JComboBox();
        carregarDNI();
        cbDNI.setMaximumSize(new Dimension(400, 30));
        labelDNI.setFont(fontText);
        cbDNI.setFont(fontText);
        cbDNI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualitzarVista();
            }
        });
        
        JLabel labelEntrades = new JLabel("Entrades Reservades:");
        taEntrades = new JTextArea(10, 20);
        taEntrades.setMaximumSize(taEntrades.getPreferredSize());
        labelEntrades.setFont(fontText);
        taEntrades.setFont(fontText);
        
        JLabel jlConsulta = new JLabel("Consulta per DNI la reserva");
        jlConsulta.setPreferredSize(new Dimension(55, 35));
        jlConsulta.setFont(new Font("Source Sans Pro", Font.BOLD, 16));
        jlConsulta.setForeground(Color.RED);
        
        //S'alinien els components al centre del panel
        labelNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        tfNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelDNI.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbDNI.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelEntrades.setAlignmentX(Component.CENTER_ALIGNMENT);
        taEntrades.setAlignmentX(Component.CENTER_ALIGNMENT);
        taEntrades.setEditable(false);
        jlConsulta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane spTaEntrades = new JScrollPane(taEntrades);

        //S'assigna l'espai que hi haurà entre els elements
        panelDades.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDades.add(labelNom);
        panelDades.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDades.add(tfNom);
        panelDades.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDades.add(labelDNI);
        panelDades.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDades.add(cbDNI);
        panelDades.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDades.add(labelEntrades);
        panelDades.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDades.add(spTaEntrades);
        panelDades.add(Box.createRigidArea(new Dimension(0, 30)));
        panelDades.add(jlConsulta);
        panelDades.add(Box.createRigidArea(new Dimension(0, 30)));
        
        panelDades.setBackground(new Color(102, 153, 204));
        add(panelDades, BorderLayout.EAST);
    }

    //Mostra l'apartat de les butaques
    private void mostrarButaques() {
        JPanel panelButaques = new JPanel();

        panelButaques.setLayout(new GridLayout(FILES, COLUMNES));

        //Carreguem les butaques
        for (int i = 0; i < FILES; i++) {
            for (int j = 0; j < COLUMNES; j++) {
                butaques[i][j] = 2;
                jbButaca[i][j] = new JButton();
                jbButaca[i][j].setIcon(butacaLliure);
                jbButaca[i][j].setPreferredSize(new Dimension(55, 60));
                jbButaca[i][j].setOpaque(false);
                jbButaca[i][j].setContentAreaFilled(false);
                jbButaca[i][j].setBorderPainted(false);

                panelButaques.add(jbButaca[i][j]);
            }
        }
        panelButaques.setBackground(Color.WHITE);
        add(panelButaques);
        
        entradesSeleccionades = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String entrada1, String entrada2) {
                //Obtenim la primera fila i columna de cada entrada
                int fila1 = Integer.parseInt(entrada1.split(" - ")[0].substring(5).trim());
                int columna1 = Integer.parseInt(entrada1.split(" - ")[1].substring(9).trim());
                int fila2 = Integer.parseInt(entrada2.split(" - ")[0].substring(5).trim());
                int columna2 = Integer.parseInt(entrada2.split(" - ")[1].substring(9).trim());

                //Comparem les files, si son iguals, comparem les columnes
                if (fila1 != fila2) {
                    return Integer.compare(fila1, fila2);
                } else {
                    return Integer.compare(columna1, columna2);
                }
            }
        });
    }
    
    //Actualizar el JTextArea taEntrades
    private void actualizarTaEntrades() {
        taEntrades.setText("");
        for (String entrada: entradesSeleccionades) {
            taEntrades.append(entrada + "\n");
        }
    }
    
    private void carregarDNI() {
        try {
            cbDNI.addItem("(Selecciona un DNI)");
            FileInputStream arxiuDades = new FileInputStream("dades.dat");
            ObjectInputStream oisDades = new ObjectInputStream(arxiuDades);
            
            try {
                Compra c;
                int voltes = 1;
                while ((c = (Compra) oisDades.readObject()) != null) {
                    /*He hagut d'afegir al ComboBox un identificador, ja que la meva intenció
                    era deixar introduir DNIs duplicats (ja que al comprar una entrada,
                    sempre pots tornar a comprar mes entrades, per exemple, si algún amic s'apunta
                    a última hora). Pensava que es podia agafar la posició del item seleccionat,
                    pero al seleccionar un valor repetit, getSelectedIndex() retornarà l'index del primer valor.*/ 
                    cbDNI.addItem(voltes + ". " + c.getDNI());
                    voltes++;
                }
            } catch (Exception e) {}
            
            oisDades.close();
            arxiuDades.close();
        } catch (Exception e) {
            System.out.println("Error al carregar els DNI! Más información: " + e.getMessage());
        }
    }
    
    private void actualitzarVista() {
        try {
            String entrada;
            FileInputStream arxiuDades = new FileInputStream("dades.dat");
            ObjectInputStream oisDades = new ObjectInputStream(arxiuDades);
            int voltes = 0;

            Compra c = null, ultim = null;
            boolean finalArxiu = false;
            while (!finalArxiu) {
                try {
                    c = (Compra) oisDades.readObject();
                    voltes++;
                    if (voltes == cbDNI.getSelectedIndex()) {
                        finalArxiu = true;
                        ultim = c; //Si c es NULL, dispararà abans una excepció i ultim no es modificarà amb NULL
                    } 
                } catch (EOFException e) {
                    finalArxiu = true;
                }
            }
            oisDades.close();
            arxiuDades.close();

            //Actualitzar butaques
            try {
                for (int i = 0; i < FILES; i++) {
                    for (int j = 0; j < COLUMNES; j++) {
                        if (ultim != null) {
                            if (ultim.getButaques()[i][j] == 1) {
                                butaques[i][j] = 0;
                            } else {
                                butaques[i][j] = 2;
                            }
                        } else {
                            butaques[i][j] = 2;
                        }
                        if (butaques[i][j] == 0) {
                            jbButaca[i][j].setIcon(butacaOcupada);
                        } else {
                            jbButaca[i][j].setIcon(butacaLliure);
                        }
                        
                        //Actualitzar TextArea
                        if (butaques[i][j] == 0) {
                            entrada = "Fila: " + (i + 1) + " - Columna: " + (j + 1);
                            entradesSeleccionades.add(entrada);
                            actualizarTaEntrades();
                        } else {
                            entrada = "Fila: " + (i + 1) + " - Columna: " + (j + 1);
                            entradesSeleccionades.remove(entrada);
                            actualizarTaEntrades();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al actualitzar les butaques! Més informació: " + e.getMessage());
            }
            
            //Actualitzar nom comprador
            if (cbDNI.getSelectedIndex() == 0) {
                tfNom.setText("");
            } else {
                tfNom.setText(ultim.getNom());
            }
        } catch (Exception e) {
            System.out.println("Error al actualitzar la vista! Més informació: " + e.getMessage());
        }   
    }
}
