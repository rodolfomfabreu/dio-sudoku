package br.com.dio.ui.custom.panel;
import java.awt.Dimension;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
    public MainPanel(Dimension dimension) {
        this.setSize(dimension);
        this.setPreferredSize(dimension);
    }
}
