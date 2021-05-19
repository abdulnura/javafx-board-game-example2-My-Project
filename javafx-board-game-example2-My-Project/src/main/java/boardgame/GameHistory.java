package boardgame;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class GameHistory {

        class HistoryNode
        {
            String name;
            int win;
            int loss;

        }

        private Map<String, HistoryNode> history;


        public GameHistory()
        {
            history = new TreeMap<>();
        }

        public void loadFile(String fname)
        {
            try
            {
                Scanner file = new Scanner(new File(fname));
                while (file.hasNextInt())
                {
                    HistoryNode n = new HistoryNode();
                    n. win = file.nextInt();
                    n. loss = file.nextInt();
                    n. name = file.nextLine().trim();
                    history.put(n.name, n);
                }
                file.close();
            }
            catch (IOException e)
            {

            }
        }

        public ArrayList<String> getNames()
        {

            return new ArrayList<String>(history.keySet());
        }

        public void saveFile(String fname)
        {
            try
            {
                PrintStream file = new PrintStream(fname);
                for (HistoryNode n : history.values())
                {
                    file.println( n.win + " " + n.loss + " " + n.name);
                }
                file.close();
            }
            catch (IOException e)
            {

            }
        }
        public String toString()
        {
            String result = "";
            for (HistoryNode n : history.values())
            {
                result += (n.name + ": " + n.win + " wins and "
                        + n.loss + " losses" );
                result += "\n";
            }
            return result;
        }


        public void update(String name, boolean win)
        {
            if (name.isEmpty())
                return;

            if (!history.containsKey(name))
            {
                HistoryNode node = new HistoryNode();
                node.name = name;
                node.loss = win ? 0 : 1;
                node.win = win ? 1 : 0;

                history.put(name, node);
            }
            else
            {
                if (win)
                    history.get(name).win++;
                else
                    history.get(name).loss++;


            }
        }
    }



