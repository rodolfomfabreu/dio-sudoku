package br.com.dio.ui.custom.screen;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.EventEnum;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);
    private final NotifierService notifierService;
    private final BoardService boardService;
    private JButton finishGameButton;
    private JButton resetGameButton;
    private JButton checkGameStatusButton;

    public MainScreen(Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel maiPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, maiPanel);
        for (int r = 0; r < 9; r+=3) {
            Integer endRow = r + 2;

            for (int c = 0; c < 9; c+=3) {
                Integer endCol = c + 2;
                var spaces = getSpacesFromSector(
                    boardService.getSpaces(), 
                    c, endCol, r, endRow
                );
                maiPanel.add(generateSection(spaces));
            }

        }
        addResetButton(maiPanel);
        addCheckGameStatusButton(maiPanel);
        addFinishGameButton(maiPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(List<List<Space>> spaces, 
        int initCol, int endCol, int initRow, int endRow) {
        List<Space> spacesSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                spacesSector.add(spaces.get(c).get(r));
            }
        }

        return spacesSector;
    }

    private JPanel generateSection(List<Space> spaces) {
        List<NumberText> fields = new ArrayList<>(
            spaces.stream().map(NumberText::new).toList()
        );
        fields.forEach(f -> notifierService.subscriber(EventEnum.CLEAR_SPACE, f));
        return new SudokuSector(fields);
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
                notifierService.notify(EventEnum.CLEAR_SPACE);
            }
        });
        maiPanel.add(resetGameButton);
    }
}
