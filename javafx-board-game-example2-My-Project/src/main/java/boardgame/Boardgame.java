package boardgame;


import boardgame.model.GameBoard;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Boardgame extends JFrame implements ActionListener {


        public static final String HISTORY_FILE = "history.txt";

        private JButton[][] btnGrid = new JButton[5][5];
        private JButton btnNewGame = new JButton("Start");
        private JButton btnHistory = new JButton("History");
        private JButton btnExit = new JButton("Exit");
        private JButton btnAddPlayer = new JButton("Add Name");

        private JComboBox<String> combName1 = new JComboBox<String>(new String[]{"Player"});
        private JComboBox<String> combName2 = new JComboBox<String>(new String[]{"Player"});

        private GameHistory history = new GameHistory();
        private GameBoard board;

        private BoardGameApplication addUserDlg = new BoardGameApplication();
        private GameBoard.GameBoardToPlay gameBoardToPlay = null;


        private String playerName1;
        private String playerName2;
        private int currentCoin;
        private int playerCoin1;
        private int playerCoin2;


    public Boardgame()
        {
            setTitle("Board Game");
            setLayout(new BorderLayout());
            setLocation(600,400);

            JPanel pnAddPlayer = new JPanel();
            JPanel pnCommands = new JPanel();

            JPanel pn2 = new JPanel();
            JPanel pnCenter = new JPanel(new GridLayout(2, 1));

            JPanel pnEnd = new JPanel(new GridLayout(2, 1));

            CommonUtil.setBtn(btnNewGame,120);
            btnNewGame.addActionListener(this);
            pnCommands.add(btnNewGame);

            CommonUtil.setBtn(btnHistory,150);
            btnHistory.addActionListener(this);
            pnCommands.add(btnHistory);

            CommonUtil.setBtn(btnExit,120);
            btnExit.addActionListener(this);
            pnCommands.add(btnExit);

            CommonUtil.setBtn(btnAddPlayer,150);
            pnAddPlayer.add(btnAddPlayer);

            pn2.add(CommonUtil.createLabel("Player1 : "));
            pn2.add(combName1);
            pn2.add(CommonUtil.createLabel("      VS       Player2 : "));
            pn2.add(combName2);

            btnAddPlayer.addActionListener(this);

            history.loadFile(HISTORY_FILE);
            combName1.setEditable(false);
            combName2.setEditable(false);
            resetPlayerNames();
            combName1.setSelectedIndex(0);
            combName2.setSelectedIndex(0);

            pnCommands.setBackground(CommonUtil.BACKGROUND_COLOR);
            pnAddPlayer.setBackground(CommonUtil.BACKGROUND_COLOR);
            pn2.setBackground(CommonUtil.BACKGROUND_COLOR);

            add(pnCommands,BorderLayout.PAGE_START);
            pnCenter.add(pnAddPlayer);
            pnCenter.add(pn2);
            add(pnCenter,BorderLayout.CENTER);
            add(pnEnd,BorderLayout.PAGE_END);


            gameBoardToPlay = new GameBoard.GameBoardToPlay(this);
        }


        private void resetPlayerNames()
        {
            combName1.removeAllItems();
            combName1.addItem("Player");
            combName2.removeAllItems();
            combName2.addItem("Player");

            for (String name : history.getNames()){
                combName1.addItem(name);
                combName2.addItem(name);
            }
        }
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == btnExit)
            {
                history.saveFile(HISTORY_FILE);
                System.exit(0);
            }
            else if (e.getSource() == btnHistory)
            {
                String result = history.toString();
                if (result.isEmpty())
                    result = "Empty history";
                JOptionPane.showMessageDialog(this, result);
            }
            else if (e.getSource() == btnNewGame)
            {
                createNewGame();

            }else if(e.getSource() == btnAddPlayer){
                addUserDlg.setLocationRelativeTo(this);
                addUserDlg.setModal(true);
                addUserDlg.reset();
                addUserDlg.pack();
                addUserDlg.setVisible(true);

                if(addUserDlg.isAdded()){
                    String newUser = addUserDlg.getTxtUserName().getText().trim();
                    boolean isDuplicate = false;
                    if(newUser.length()>0){

                        for(int i=0;i<combName1.getItemCount();i++){
                            if(((String)combName1.getItemAt(i)).equals(newUser)){
                                isDuplicate = true;
                                break;
                            }
                        }
                        if(!isDuplicate) combName1.addItem(newUser);
                    }
                }
            }
        }

        public void createNewGame()
        {

            String title = null;
            playerName1 = combName1.getSelectedItem().toString().trim();
            playerName2 = combName2.getSelectedItem().toString().trim();
            gameBoardToPlay.setVisible(true);

            if (playerName1.isEmpty() || playerName2.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please choose the player name.");
                return;
            }

            for (int i = 0; i < btnGrid.length; i++)
            {
                for (int j = 0; j < btnGrid.length; j++)
                {
                    btnGrid[i][j].setEnabled(true);
                    btnGrid[i][j].setText("");
                }
            }

            board = new GameBoard();

            {
                playerCoin1 = GameBoard.PLAYER1;
                playerCoin2 = GameBoard.PLAYER2;
                currentCoin = GameBoard.PLAYER1;
                title = playerName1 + " VS " + playerName2;

            }
            gameBoardToPlay.setTitle(title);
            displayBoard();


        }

        public void displayBoard()
        {
            for (int i = 0; i < btnGrid.length; i++)
            {
                for (int j = 0; j < btnGrid.length; j++)
                {
                    if (board.getBoard()[i][j] == GameBoard.PLAYER1)
                    {
                        btnGrid[i][j].setText("X");
                        btnGrid[i][j].setForeground(CommonUtil.BACKGROUND_COLOR);
                    }
                    else if (board.getBoard()[i][j] == GameBoard.PLAYER2)
                    {
                        btnGrid[i][j].setText("O");
                        btnGrid[i][j].setForeground(Color.RED);
                    }
                }
            }
            gameBoardToPlay.setSize(800,800);
            gameBoardToPlay.setVisible(true);
            this.setVisible(false);
        }

        public void playerClick(int row, int col)
        {
            if (board == null)
                return;

            if (board.getBoard()[row][col] != 0)
            {
                JOptionPane.showMessageDialog(this, "Sorry cannot place here.");

                return;

            }

            board.placeStone(row, col, currentCoin);
            displayBoard();
            if (board.findWinner() == currentCoin)
            {
                gameOver(currentCoin);
            }
            else if (board.isFull())
            {
                tieGame();
            }
            else
            {
                if (currentCoin == GameBoard.PLAYER1)
                    currentCoin = GameBoard.PLAYER2;
                else
                    currentCoin = GameBoard.PLAYER1;
            }
        }


        private void gameOver(int winner)
        {
            for (int i = 0; i < btnGrid.length; i++)
                for (int j = 0; j < btnGrid.length; j++)
                    btnGrid[i][j].setEnabled(false);

            {
                if (winner != playerCoin1)
                    JOptionPane.showMessageDialog(this, "Perfect " +
                            playerName1 + " won.");
                else
                    JOptionPane.showMessageDialog(this, "Perfect " +
                            playerName2 + " won.");
            }

            if (!playerName1.equalsIgnoreCase("Guest"))
            {
                    history.update(playerName1, winner == playerCoin1);


                history.saveFile(HISTORY_FILE);
                resetPlayerNames();
            }
            combName1.setSelectedItem(playerName1);
            combName2.setSelectedItem(playerName2);

            this.setVisible(true);
            gameBoardToPlay.setVisible(false);
        }

        private void tieGame()
        {
            for (int i = 0; i < btnGrid.length; i++)
                for (int j = 0; j < btnGrid.length; j++)
                    btnGrid[i][j].setEnabled(false);
            JOptionPane.showMessageDialog(this, "TIE GAME!");
            this.setVisible(true);
            gameBoardToPlay.setVisible(false);
        }

        public JButton[][] getBtnGrid() {
            return btnGrid;
        }
}
    class CommonUtil {

    public static final Color BACKGROUND_COLOR= Color.GREEN.brighter();
    public static final Color BUTTON_BACKGROUND_COLOR = Color.BLACK.darker();
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    public static void setBtn(JButton btn,int btnWidth){
        btn.setPreferredSize(new Dimension(btnWidth,60));
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(BUTTON_BACKGROUND_COLOR);
        btn.setForeground(BUTTON_TEXT_COLOR);
    }

    public static JLabel createLabel(String caption){
        JLabel label = new JLabel();
        label.setForeground(BUTTON_TEXT_COLOR);
        label.setText(caption);
        return label;
    }

}




