import java.util.stream.Stream;

import br.com.dio.ui.custom.screen.MainScreen;

import static java.util.stream.Collectors.toMap;

public class UIMain {
    public UIMain(String[] args) {
        var gameConfig = Stream.of(args)
                    .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                    ));
        MainScreen mainScreen = new MainScreen(gameConfig);
        mainScreen.buildMainScreen();
    }
    public static void main(String[] args) {
        var gameConfig = Stream.of(args)
                    .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                    ));
        MainScreen mainScreen = new MainScreen(gameConfig);
        mainScreen.buildMainScreen();
    }
}
