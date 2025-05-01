package cinemaparadiso;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author Andreu Niso Sevilla
 */

//Classe Menu que representa la finestra principal de l'aplicació.
public class Menu extends JFrame implements ActionListener {

    private JButton botoUsuari;
    private JButton botoAdmin;

    public Menu() {
        setTitle("Cinema Paradiso");
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        botoUsuari = crearBoto("Comprar Entrades", 112, 100, 176, 35);
        botoAdmin = crearBoto("Comprovar Entrades", 112, 165, 176, 35);

        add(botoUsuari);
        add(botoAdmin);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        Utilitats.centrarPantalla(this);
        setVisible(true);
    }

    //Mètode per crear un botó amb el text, les coordenades i les mides especificades
    private JButton crearBoto(String txtBoto, int x, int y, int width, int height) {
        JButton jb = new JButton(txtBoto);
        jb.setBounds(x, y, width, height);
        jb.addActionListener(this);
        return jb;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botoUsuari) {
            //Creació de la finestra MenuUsuari
            MenuUsuari mainWinUser = new MenuUsuari();
            mainWinUser.setMinimumSize(mainWinUser.getPreferredSize());
            Utilitats.centrarPantalla(mainWinUser);
            dispose();
        } else if (e.getSource() == botoAdmin) {
            //Creació de la finestra MenuAdmin
            MenuAdmin mainWinAdmin = new MenuAdmin();
            mainWinAdmin.setMinimumSize(mainWinAdmin.getPreferredSize());
            Utilitats.centrarPantalla(mainWinAdmin);
            dispose();
        }
    }
}
