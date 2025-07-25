package br.com.dio.ui.custom.button;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class FinishGameButton extends JButton {
    public FinishGameButton(ActionListener actionListener) {
        this.setText("Finalizar Jogo");
        this.addActionListener(actionListener);
    }
}
