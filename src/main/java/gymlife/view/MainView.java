package gymlife.view;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gymlife.utility.Constants;

//import java.util.HashMap;
//import java.util.Map;
import gymlife.controller.api.Controller;
import gymlife.controller.ControllerImpl;
import gymlife.utility.GameDifficulty;
import gymlife.view.stats.SideStatsView;
//import gymlife.utility.ScenariosType;

/**
 * The MainView class represents the main view of the application.
 * It extends the JFrame class and provides methods to start and display the main view.
 */
public class MainView extends JFrame {
    public static final long serialVersionUID = 4328743;
    private final transient  Controller controller = new ControllerImpl(GameDifficulty.EASY);
    private final JPanel mainPanel = new JPanel();
    private final JPanel scenariosContainer = new JPanel();
    private final JPanel sideContainer = new JPanel();
    private final JPanel statsView = new SideStatsView(controller);
    private final JPanel gameMapView = new GameMapView(controller);
    private final DimensionGetter dimensionGetter = new DimensionGetter();

//    private final CharacterView charView = new CharacterView(controller);
//    private final Map<ScenariosType,JPanel> scenariosMap = new HashMap<>();

    /**
     * Starts the main view of the application.
     * Sets the size, layout, and default close operation of the frame.
     * Sets the size, layout, and visibility of the character view panel.
     * Adds the character view panel to the main frame and makes it visible.
     */
    public void start() {
        this.setSize(dimensionGetter.getFrameDimension());
        System.out.println("Screen width: " + dimensionGetter.getFrameDimension().width + " Screen height: " + dimensionGetter.getFrameDimension().height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel.setPreferredSize(dimensionGetter.getFrameDimension());
        mainPanel.setLayout(new BorderLayout());

        scenariosContainer.setPreferredSize(dimensionGetter.getScenarioDimension());
        scenariosContainer.setLayout(new CardLayout());
        scenariosContainer.setBackground(Color.RED);

        sideContainer.setPreferredSize(dimensionGetter.getSideDimension());
        sideContainer.setLayout(new CardLayout());
        sideContainer.setBackground(Color.BLUE);

        mainPanel.add(scenariosContainer, BorderLayout.WEST);
        mainPanel.add(sideContainer, BorderLayout.CENTER);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '+') {
                    dimensionGetter.incScreenDimension();
                    resizeComponents();
                }
                if (e.getKeyChar() == '-') {
                    dimensionGetter.decScreenDimension();
                    resizeComponents();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        sideContainer.add(statsView, BorderLayout.CENTER);
        statsView.setVisible(true);
        scenariosContainer.add(gameMapView, SwingConstants.CENTER);

        gameMapView.setVisible(true);
        sideContainer.setVisible(true);
        this.setUndecorated(true);
        this.add(mainPanel);
        this.setLocationRelativeTo(null); // Posiziona il frame al centro dello schermo
        this.setResizable(false);
        this.setVisible(true);
    }

    // Metodo per ridimensionare i pannelli proporzionalmente
    private void resizeComponents() {
        // Calcola le nuove dimensioni proporzionali per i pannelli
        scenariosContainer.setPreferredSize(dimensionGetter.getScenarioDimension());
        sideContainer.setPreferredSize(dimensionGetter.getSideDimension());
        mainPanel.setPreferredSize(dimensionGetter.getFrameDimension());
        this.setPreferredSize(dimensionGetter.getFrameDimension());

        System.out.println("Main view " + this.getSize().width + " " + this.getSize().height);
        System.out.println("Main Panel " + mainPanel.getSize().width + " " + mainPanel.getSize().height);
        System.out.println("Screen width: " + dimensionGetter.getFrameDimension().width + " Screen height: " + dimensionGetter.getFrameDimension().height);


        // Aggiorna il layout
        sideContainer.revalidate();
        sideContainer.repaint();
        scenariosContainer.revalidate();
        scenariosContainer.repaint();

        mainPanel.repaint();;
        this.pack();
        this.repaint();
    }
}
