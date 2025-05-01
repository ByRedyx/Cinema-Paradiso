package cinemaparadiso;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * @author Andreu Niso Sevilla
 */

public class MenuUsuari extends JFrame {

    private static final int FILES = 8, COLUMNES = 15;
    private int numEntrades = 0;
    private JTextField tfNom, tfDNI;
    private JTextArea taEntrades;
    private TreeSet<String> entradesSeleccionades;
    private Font fontText = new Font("Source Sans Pro", Font.PLAIN, 14);
    private int butaques[][] = new int[FILES][COLUMNES]; //0 indicara que s'ha comprat, 1 indicarà que està seleccionada i 2 que està disponible.
    
    private JButton[][] jbButaca = new JButton[FILES][COLUMNES];
    private ImageIcon butacaLliure = new ImageIcon("src/imatges/butacaLliure.png");
    private ImageIcon butacaSeleccionada = new ImageIcon("src/imatges/butacaSeleccionada.png");
    private ImageIcon butacaOcupada = new ImageIcon("src/imatges/butacaOcupada.png");
    
    public MenuUsuari() {
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

        inicialitzarButaques();
        mostrarTitol();
        mostrarDades();
        actualitzarButaques();
    }
    
    private void inicialitzarButaques() {
        JPanel panelButaques = new JPanel();
        panelButaques.setLayout(new GridLayout(FILES, COLUMNES));
        for (int i = 0; i < FILES; i++) {
            for (int j = 0; j < COLUMNES; j++) {
                jbButaca[i][j] = new JButton(); 
                butaques[i][j] = 2;
                
                jbButaca[i][j].setPreferredSize(new Dimension(55, 60));
                jbButaca[i][j].setOpaque(false);
                jbButaca[i][j].setContentAreaFilled(false);
                jbButaca[i][j].setBorderPainted(false);

                panelButaques.add(jbButaca[i][j]);
                
                //Declarem les files i columnes com a "final" per a utilitzarles a l'ActionListener
                //Si no el declarem com a "final", dona l'error "local variables referenced from an inner class must be final or effectively final")
                final int fila = i;
                final int columna = j;
                
                jbButaca[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String entrada;
                        if (butaques[fila][columna] == 2) {
                            numEntrades++;
                            butaques[fila][columna] = 1;
                            jbButaca[fila][columna].setIcon(butacaSeleccionada);
                            entrada = "Fila: " + (fila + 1) + " - Columna: " + (columna + 1);
                            entradesSeleccionades.add(entrada);
                            actualizarTaEntrades();
                        } else if (butaques[fila][columna] == 1) {
                            numEntrades--;
                            butaques[fila][columna] = 2;
                            jbButaca[fila][columna].setIcon(butacaLliure);
                            entrada = "Fila: " + (fila + 1) + " - Columna: " + (columna + 1);
                        entradesSeleccionades.remove(entrada);
                        actualizarTaEntrades();
                        }
                    }
                });
            }
        }
        panelButaques.setBackground(Color.WHITE);
        add(panelButaques);
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
        
        JLabel labelDNI = new JLabel("DNI del comprador:");
        tfDNI = new JTextField(20);
        tfDNI.setMaximumSize(new Dimension(400, 30));
        labelDNI.setFont(fontText);
        tfDNI.setFont(fontText);
        
        JLabel labelEntrades = new JLabel("Entrades Reservades:");
        taEntrades = new JTextArea(10, 20);
        taEntrades.setMaximumSize(taEntrades.getPreferredSize());
        labelEntrades.setFont(fontText);
        taEntrades.setFont(fontText);
        
        JButton jbPagament = new JButton("Pagar Entrades");
        jbPagament.setPreferredSize(new Dimension(55, 35));
        jbPagament.setFont(fontText);
        
        //S'alinien els components al centre del panel
        labelNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        tfNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelDNI.setAlignmentX(Component.CENTER_ALIGNMENT);
        tfDNI.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelEntrades.setAlignmentX(Component.CENTER_ALIGNMENT);
        taEntrades.setAlignmentX(Component.CENTER_ALIGNMENT);
        taEntrades.setEditable(false);
        jbPagament.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane spTaEntrades = new JScrollPane(taEntrades);

        //S'assigna l'espai que hi haurà entre els elements
        panelDades.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDades.add(labelNom);
        panelDades.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDades.add(tfNom);
        panelDades.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDades.add(labelDNI);
        panelDades.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDades.add(tfDNI);
        panelDades.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDades.add(labelEntrades);
        panelDades.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDades.add(spTaEntrades);
        panelDades.add(Box.createRigidArea(new Dimension(0, 30)));
        panelDades.add(jbPagament);
        panelDades.add(Box.createRigidArea(new Dimension(0, 30)));
        
        jbPagament.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Si s'ha seleccionat alguna entrada
                if (numEntrades > 0) {
                    //Si el nom es superior a 2 caràcters i inferior a 60
                    if (tfNom.getText().length() > 2 && tfNom.getText().length() < 60) {
                        
                        boolean dniIncorrecte = false;
                        
                        //Comprovem si el DNI compleix els requisits reals d'un DNI
                        if (Pattern.matches("[0-9]{8}[a-zA-Z]", tfDNI.getText())) {
                            int numerosDNI = Integer.parseInt(tfDNI.getText().substring(0, 8));
                            
                            //La lletra del DNI es calcula dividint els numeros entre 23, i la resta de la divisió ha de pertanyer a un caràcter de la taula
                            if (obtenirLletraDNI(numerosDNI % 23) == (tfDNI.getText().toUpperCase().charAt(8))) {
                                Pagament pagWin = new Pagament(numEntrades, MenuUsuari.this);
                                pagWin.pack();
                                pagWin.setMinimumSize(pagWin.getSize()); //Fem que la pantalla no sigui mes petita del tamany necessari per veure els elements correctament
                                Utilitats.centrarPantalla(pagWin);
                            } else {
                                dniIncorrecte = true;
                            }
                        } else {
                            dniIncorrecte = true;
                        }
                        if (dniIncorrecte) {
                            JOptionPane.showMessageDialog(null, "El DNI no compleix els requisits!", "DNI invàlid", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El nom ha de superar els 2 caràcters i ser inferior a 60!", "Nom invàlid", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Has de seleccionar alguna butaca per a poder fer el pagament!", "Pagament invàlid", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        panelDades.setBackground(new Color(102, 153, 204));
        add(panelDades, BorderLayout.EAST);
    }
    
    private void actualitzarButaques() {
        Compra c, ultim = null;
        try {
            FileInputStream arxiuDades = new FileInputStream("dades.dat");
            ObjectInputStream oisDades = new ObjectInputStream(arxiuDades);

            boolean finalArxiu = false;
            while (!finalArxiu) {
                try {
                    c = (Compra) oisDades.readObject();
                    ultim = c;
                } catch (EOFException e) {
                    finalArxiu = true;
                }
            }
            oisDades.close();
            arxiuDades.close();
        } catch (Exception e) {
            System.out.println("Error al actualitzar les butaques! Més informació: " + e.getMessage());
        }
        
        for (int i = 0; i < FILES; i++) {
            for (int j = 0; j < COLUMNES; j++) {
                if (ultim != null) {
                    if (ultim.getButaques()[i][j] == 0 || ultim.getButaques()[i][j] == 1) {
                        butaques[i][j] = 0;
                        jbButaca[i][j].setIcon(butacaOcupada);
                    } else {
                        butaques[i][j] = 2;
                        jbButaca[i][j].setIcon(butacaLliure);
                    }
                } else {
                    butaques[i][j] = 2;
                    jbButaca[i][j].setIcon(butacaLliure);
                }
            }
        }
        
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
    
    //Calcula la lletra del DNI. Més informació: https://noticiastrabajo.huffingtonpost.es/wp-content/uploads/2021/01/calcular-dni-copia-1-595x675.jpg
    private char obtenirLletraDNI(int resta) {
        char lletresDNI[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        return lletresDNI[resta];
    }
    
    protected int[][] getButaques() {
        return butaques;
    }

    protected String getNom() {
        return tfNom.getText();
    }

    public String getDNI() {
        return tfDNI.getText();
    }
}
