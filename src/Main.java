import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        fifteenPuzzle game = new fifteenPuzzle(550,30);
        JFrame frame =new JFrame();
        SwingUtilities.invokeLater(()->{
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Pinky-FifteenPuzzle");
            frame.setResizable(false);
            frame.add(game, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        Scanner sc = new Scanner(System.in);
        System.out.print(">>> ");
        String str;
        str = sc.nextLine();
        while(!str.equals("QUIT")){

            if(str.equals("SOLUTION")){
                game.solution();
            }
            else if (str.equals("INPUTTEST")){
                System.out.print("masukkan nama file: ");
                String file = sc.nextLine();
                game.readFile(file);
            }
            else if(str.equals("RANDOM")){
                game.newGame();
            }
            else if(str.equals("HELP")){
                System.out.println("COMMAND LIST");
                System.out.println("===============================================");
                System.out.println("RANDOM      : Melakukan pengacakan pada puzzle");
                System.out.println("INPUTTEST   : Melakukan input puzzle dari file di dalam folder test");
                System.out.println("SOLUTION    : Menjalankan Solusi dari puzle tersebut");
                System.out.println("QUIT        : Keluar dari program");
                System.out.println("PS: Program yang dibuat merupakan separuh GUI dan CLI, dimana user akan melakukan input melalui GUI " +
                        "dan Hasil dari input tersebut akan ditampilkan pada GUI fifteenPuzzle");

            }
            else{
                System.out.println("INVALID COMMAND");
            }
            System.out.print(">>> ");
            str = sc.nextLine();
        }
        System.exit(0);
    }
}
