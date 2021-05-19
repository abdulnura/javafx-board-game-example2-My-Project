package boardgame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

public class BoardGameApplication extends JDialog implements ActionListener {

        private boolean isAdded;

        private JButton btnAdd = new JButton("Add");
        private JButton btnCancel = new JButton("Cancel");
        private JTextField txtUserName = new JTextField(10);

        public BoardGameApplication() {
            setTitle("Add a New User");
            setLayout(new BorderLayout());
            JPanel pnTop = new JPanel(new GridLayout(2, 1));

            JPanel pn1 = new JPanel();
            pn1.add(new JLabel("New User :"));
            pn1.add(txtUserName);
            pnTop.add(pn1);

            JPanel pn2 = new JPanel();
            pn2.add(btnAdd);
            pn2.add(btnCancel);

            btnAdd.addActionListener(this);
            btnCancel.addActionListener(this);
            pnTop.add(pn2);

            add(pnTop, BorderLayout.NORTH);
        }

        public void reset(){
            this.isAdded = false;
            this.txtUserName.setText("");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnCancel){
                isAdded = false;
            }else if(e.getSource() == btnAdd){
                isAdded = true;
            }
            this.setVisible(false);
        }

        /**
         * @return the isAdded
         */
        public boolean isAdded() {
            return isAdded;
        }

        /**
         * @return the txtUserName
         */
        public JTextField getTxtUserName() {
            return txtUserName;
        }

    }

