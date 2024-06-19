//Bartosz Konarzewski s200482, MTR, proj.21 - Autobusy miejskie (P3)
package Rozklady_jazdy;

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * G³ówna klasa programu
 */
public class App {

    /**
     * Klasa wywo³ywana przy starcie programu, odpowiadajaca za stricte
     * wyswietlenie utworzonej ramki
     *
     * @param args podstawowy argument klasy main
     * @author Bartosz Konarzewski
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> { //umieszczenie w kolejce
            var frame = new Window(); //utworzenie obiektu Ramki
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        });
    }

    Object getGreeting() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
