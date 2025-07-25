import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.dio.model.Board;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

public class App {
    private final static Scanner scanner = new Scanner(System.in);
    private final static int BOARD_LIMIT = 9;
    private static Board board;
    public static void main(String[] args) throws Exception {
        final var positions = Stream.of(args)
                    .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                    ));

        Integer options = -1;
        while(true) {
            System.out.println("1 Iniciar um novo jogo");
            System.out.println("2 Colocar um novo numero");
            System.out.println("3 Remover um novo numero");
            System.out.println("4 Visualizar jogo atual");
            System.out.println("5 Verificar status do jogo atual");
            System.out.println("6 Limpar jogo");
            System.out.println("7 Finalizar jogo");
            System.out.println("8 Sair do jogo");

            options = scanner.nextInt();

            switch(options) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
            }
        }
    }
    private static void finishGame() {
        if (isNotStarted()) {
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabéns!! Você venceu");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros, tente novamente");
            showCurrentGame();
        } else {
            System.out.println("Está faltando números para ser preenchidos");
            showCurrentGame();
        }
    }
    private static void clearGame() {
        if (isNotStarted()) {
            return;
        }

        System.out.println("Você perderá seu progresso, deseja continuar?");
        var confirm = scanner.next();
        while(!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("nao")) {
            System.out.println("Informe sim ou nao");
            confirm = scanner.next();
        }

        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
        }
    }
    private static void showGameStatus() {
        if (isNotStarted()) {
            return;
        }

        System.out.printf("Status do jogo: %s", board.getStatus().getLabel());
        if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros");
        } else {
            System.out.println("Seu jogo não contém erros");
        }
    }
    private static void showCurrentGame() {
        if (isNotStarted()) {
            return;
        }

        Object[] args = new Object[81];
        Integer varPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col: board.getSpaces()) {
                args[varPos++] = " " + (isNull(col.get(i).getActual()) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Seu jogo:");
        System.out.printf((BoardTemplate.BOARD_TEMPLATE)+"\n", args);
    }
    private static void removeNumber() {
        if (isNotStarted()) {
            return;
        }

        System.out.println("Informe a coluna que o número será inserido");
        Integer col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que o número será inserido");
        Integer row = runUntilGetValidNumber(0, 8);

        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }
    }
    private static void inputNumber() {
        if (isNotStarted()) {
            return;
        }

        System.out.println("Informe a coluna que o número será inserido");
        Integer col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que o número será inserido");
        Integer row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número da posição [%s,%s]\n", col, row);
        Integer value = runUntilGetValidNumber(1, 9);

        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }
    }
    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado!");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>())
            for (int j = 0; j < BOARD_LIMIT; j++) {
                String positionConfig = positions.get("%s,%s".formatted(i, j));
                Integer expected = Integer.parseInt(positionConfig.split(",")[0]);
                boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                Space currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }

        board = new Board(spaces);
        System.out.println("Jogo foi iniciado!");
    }

    public static int runUntilGetValidNumber(int min, int max) {
        Integer current = scanner.nextInt();

        while(current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }

        return current;
    }

    public static boolean isNotStarted() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado!");
            return true;
        }

        return false;
    }
}
