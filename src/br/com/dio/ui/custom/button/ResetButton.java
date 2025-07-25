package br.com.dio.ui.custom.button;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ResetButton extends JButton {
    public ResetButton(ActionListener actionListener) {
        this.setText("Resetar Jogo");
        this.addActionListener(actionListener);
    }
}
