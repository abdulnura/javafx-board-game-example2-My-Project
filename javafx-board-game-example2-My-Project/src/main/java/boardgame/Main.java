package boardgame;


import javax.swing.*;

public class Main {

    public static void main(String[] args)
    {
        Boardgame game = new Boardgame();
        game.setSize(600, 310);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setVisible(true);
    }



}
