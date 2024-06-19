package Rozklady_jazdy;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Klasa odpowiadajaca za ca³e GUI programu
 */
public class Window extends JFrame {

    /**
     * Stala wartoœæ szerokosci guzika
     */
    public static final int BUTTON_SZER = 75;
    /**
     * Stala wartoœæ wysokoœci guzika
     */
    public static final int BUTTON_WYS = 30;
    /**
     * Inicjacja klasy Przystanek
     */
    public Przystanek przystanek;
    /**
     * Uwtorzenie zmiennych przycisków o typie JButton
     */
    private JButton szukajPrzycisk, zakonczPrzycisk;
    /**
     * Uwtorzenie zmiennych labeli o typie JLabel
     */
    private JLabel wybierzPrzystanek, daneAutora;
    /**
     * Uwtorzenie zmiennych pól tekstowych o typie JTextArea
     */
    private JTextArea odjazdy;
    /**
     * Uwtorzenie zmiennej od menu wyboru o typie JComboBox
     */
    private JComboBox przystankiLista;
    /**
     * Uwtorzenie zmiennej panelu o typie JPanel
     */
    private JPanel panel1;
    /**
     * Uwtorzenie zmiennej od scrollowania o typie JScrollPane
     */
    private JScrollPane scrol;

    /**
     * Klasa odpowiadajaca za ca³e GUI programu
     */
    public Window() {
        przystanek = new Przystanek();
        List<String> przystanki = przystanek.pokazPrzystanki();

        //Wystylizowanie ogólnego okna aplikacji
        setSize(400, 600);
        setResizable(false);
        setTitle("Autobusy Miejskie - GDAÑSK ZTM");
        getContentPane().setBackground(Color.decode("#292643"));
        panel1 = new JPanel();
        panel1.setSize(400, 500);
        panel1.setBackground(Color.decode("#F1F1F2"));
        panel1.setLocation(0, 130);

        //Wystylizowanie poszczególnych elementow GUI, przycisków itp
        szukajPrzycisk = new JButton();
        szukajPrzycisk.setSize(BUTTON_SZER, BUTTON_WYS);
        szukajPrzycisk.setText("Szukaj");
        szukajPrzycisk.setBackground(Color.decode("#51ca83"));
        szukajPrzycisk.setFont(new Font("Helvetica", Font.BOLD, 12));
        szukajPrzycisk.setForeground(Color.WHITE);
        szukajPrzycisk.setLocation(300, 45);
        szukajPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                szukajPrzyciskActionPerformed(evt);
            }
        });

        wybierzPrzystanek = new JLabel();
        wybierzPrzystanek.setLocation(15, 20);
        wybierzPrzystanek.setSize(200, 30);
        wybierzPrzystanek.setText("Wybierz przystanek:");
        wybierzPrzystanek.setFont(new Font("Helvetica", Font.BOLD, 14));
        wybierzPrzystanek.setForeground(Color.WHITE);

        daneAutora = new JLabel();
        daneAutora.setLocation(40, 510);
        daneAutora.setSize(400, 30);
        daneAutora.setText("Bartosz Konarzewski proj. 21- Autobusy miejskie (P3)");
        daneAutora.setFont(new Font("Helvetica", Font.ITALIC, 12));
        daneAutora.setForeground(Color.WHITE);

        zakonczPrzycisk = new JButton();
        zakonczPrzycisk.setLocation(15, 480);
        zakonczPrzycisk.setSize(350, BUTTON_WYS);
        zakonczPrzycisk.setText("Zakoñcz");
        zakonczPrzycisk.setBackground(Color.decode("#C64A44"));
        zakonczPrzycisk.setFont(new Font("Helvetica", Font.BOLD, 12));
        zakonczPrzycisk.setForeground(Color.WHITE);
        zakonczPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                zakonczPrzyciskActionPerformed(evt);
            }
        });

        przystankiLista = new JComboBox();
        przystankiLista.setLocation(15, 45);
        przystankiLista.setSize(250, BUTTON_WYS);
        przystankiLista.setModel(new DefaultComboBoxModel<String>(przystanki.toArray(new String[0])));

        odjazdy = new JTextArea();
        odjazdy.setLocation(15, 120);
        odjazdy.setSize(350, 350);
        odjazdy.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        odjazdy.setText("");
        odjazdy.setEditable(false);
        odjazdy.setFont(new Font("Helvetica", Font.BOLD, 12));
        odjazdy.setForeground(Color.BLACK);
        odjazdy.setOpaque(true);
        odjazdy.setBackground(Color.decode("#F1F1F2"));

        scrol = new JScrollPane(odjazdy);
        scrol.setBounds(15, 120, 350, 350);
        scrol.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrol.getVerticalScrollBar().setValue(0);

        this.setLayout(null);
        this.add(daneAutora);
        this.add(zakonczPrzycisk);
        this.add(scrol);
        this.add(przystankiLista);
        this.add(szukajPrzycisk);
        this.add(wybierzPrzystanek);

    }

    /**
     * Obslu¿enie klikniêcia guzika Szukaj
     *
     * @param e argument od wywo³ania akcji klikniêcia guzika szukaj
     */
    public void szukajPrzyciskActionPerformed(ActionEvent e) {
        odjazdy.setText("");
        String wybrany = przystankiLista.getSelectedItem().toString();
        wybrany = wybrany.substring(wybrany.indexOf("[") + 1, wybrany.indexOf("]"));
        int przystanekID = Integer.valueOf(wybrany);

        List<String> infoOdjazdy = przystanek.pokazOdjazdy(przystanekID);
        for (int i = 0; i < infoOdjazdy.size(); i++) {
            odjazdy.setText(odjazdy.getText() + infoOdjazdy.get(i) + "\n");

        }
    }

    /**
     * Obslu¿enie klikniêcia guzika Zakoñcz
     *
     * @param e argument od wywo³ania akcji klikniêcia guzika zakoncz
     */
    public void zakonczPrzyciskActionPerformed(ActionEvent e) {
        System.exit(0);

    }
}
