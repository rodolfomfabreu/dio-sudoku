package br.com.dio.ui.custom.screen;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

import br.com.dio.service.BoardService;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.panel.MainPanel;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);
    private final BoardService boardService;
    private JButton finishGameButton;
    private JButton resetGameButton;
    private JButton checkGameStatusButton;

    public MainScreen(Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
    }

    public void buildMainScreen() {
        JPanel maiPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, maiPanel);
        addResetButton(maiPanel);
        addCheckGameStatusButton(maiPanel);
        addFinishGameButton(maiPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addFinishGameButton(JPanel maiPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameIsFinished()) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Parabéns, você concluiu o jogo!"
                );
                resetGameButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(
                    null, 
                    "Você ainda não concluiu o jogo!"
                );
            }
        });
        maiPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel maiPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch(gameStatus) {
                case COMPLETED -> "Jogo Concluído";
                case INCOMPLETED -> "Jogo não está Concluído";
                case NON_STARTED -> "Jogo não Iniciado";
            };
            message += hasErrors ? " e contém errors" : " e não contém errors";

            JOptionPane.showMessageDialog(null, message);
        });
        maiPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel maiPanel) {
        resetGameButton = new ResetButton(e -> {
            var dialogResult = JOptionPane.showConfirmDialog(
                null, 
                "Deseja realmente reiniciar o jogo?", 
                "Resetar o Jogo", 
                YES_NO_OPTION,
                QUESTION_MESSAGE
            );

            if (dialogResult == 0) {
                boardService.reset();
            }
        });
        maiPanel.add(resetGameButton);
    }
}
