package gymlife.view.bankgame;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import gymlife.controller.api.Controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;

/**
 * This class groups all the panels and shows them on screen.
 */
public final class BankGameView extends JLayeredPane {
    @Serial
    private static final long serialVersionUID = -3972452455820596601L;

    private final TextLabelView numberLabel;
    private final TextLabelView moneyLabel;
    private boolean STARTED = false;
    private final JTextField boxMoney;
    private final Font myFont = new Font("PLAIN", Font.PLAIN, 20);
    private float moneyMultiplied;
    private float moneyStart;

    /**
     * This method sets the dimensions of the plane image and the sky image, add a
     * button,
     * shows the multiplier and the money multiplied,
     * moreover it sets the images' layering.
     * 
     * @param controller
     */
    public BankGameView(final Controller controller) {
        numberLabel = new MultiplierGameView();
        moneyLabel = new MoneyGameView();
        final ImageLabelView planeLayer = new ImageLabelView("gymlife/airplane/airplane.png");
        final ImageLabelView skyLayer = new ImageLabelView("gymlife/sky/sky.png");
        final JButton button = new JButton();
        final JButton restarButton = new JButton();
        boxMoney = new JTextField();

        this.add(skyLayer, JLayeredPane.DEFAULT_LAYER);
        this.add(planeLayer, JLayeredPane.PALETTE_LAYER);
        this.add(numberLabel, JLayeredPane.MODAL_LAYER);
        this.add(button, JLayeredPane.MODAL_LAYER);
        this.add(restarButton, JLayeredPane.MODAL_LAYER);
        this.add(boxMoney, JLayeredPane.MODAL_LAYER);
        this.add(moneyLabel, JLayeredPane.MODAL_LAYER);

        button.setText("Play");
        button.setBackground(Color.GREEN);
        restarButton.setText("Restart");
        button.setEnabled(false);
        restarButton.setEnabled(false);

        boxMoney.setFont(myFont);

        /*
         * boxMoney.addKeyListener(new KeyAdapter() {
         * 
         * @Override
         * public void keyTyped(final KeyEvent e) {
         * if (e.getKeyCode() == KeyEvent.VK_DELETE) {
         * e.consume();
         * }
         * }
         * });
         */

        boxMoney.addActionListener(new ActionListener() {
            @Override
            public final void actionPerformed(final ActionEvent e) {
                String temp = boxMoney.getText();
                moneyStart = Float.parseFloat(temp);
                ((MoneyGameView)moneyLabel).updateText(moneyStart);        
                moneyLabel.setVisible(true);
                button.setEnabled(true);
                restarButton.setEnabled(true);
            }
        });

        boxMoney.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_ENTER) {
                    e.consume();
                    moneyLabel.setText("Wrong format, only numbers");
                }
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                setLayersNewSize(skyLayer, planeLayer, numberLabel, button, restarButton, moneyLabel);
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public final void actionPerformed(final ActionEvent e) {
                if (!STARTED) {
                    updateMulti(controller);
                    showsMulti(controller);
                    STARTED = true;
                    numberLabel.setVisible(true);
                    boxMoney.setEditable(false);
                    restarButton.setEnabled(false);
                } else {
                    controller.controllerStopMultiplier();
                    restarButton.setEnabled(true);
                    STARTED = false;
                    button.setEnabled(false);
                }
            }
        });

        restarButton.addActionListener(new ActionListener() {
            @Override
            public final void actionPerformed(final ActionEvent e) {
                numberLabel.setVisible(false);
                controller.randomizeNewThreshold();
                restarButton.setEnabled(false);
                moneyLabel.setVisible(false);
                boxMoney.setEditable(true);
            }
        });
        this.setVisible(true);
    }

    public void showsMulti(Controller controller) {
        new Thread(() -> {
            float multiplier;
            while ((multiplier = controller.getMultiplier()) != controller.getTreshold()) {
                moneyMultiplied = controller.controllerGetMoney();
                if (numberLabel instanceof MultiplierGameView mpgv) {
                    mpgv.updateText(multiplier, moneyMultiplied);
                }
            }
        }).start();
    }

    /**
     * Starts a new thread to update the multiplier value by calling the
     * startMultiplier method
     * with the current money to play value.
     * 
     * @param controller The controller object responsible for managing the
     *                   multiplier.
     */
    public void updateMulti(final Controller controller) {
        new Thread(() -> {
            controller.startMultiplier(moneyStart);
        }).start();
    }

    private void setLayersNewSize(final ImageLabelView skyLabel, final ImageLabelView planeLabel,
            final TextLabelView numberLabel, final JButton button, final JButton restartButton,
            final TextLabelView moneyLabel) {
        final Dimension newSize = this.getSize();
        skyLabel.setBounds(0, 0, newSize.width, newSize.height);
        skyLabel.reload();
        button.setBounds(newSize.width / 45,
                newSize.height / 3, newSize.height / 9,
                newSize.height / 11);
        restartButton.setBounds(newSize.width / 8,
                newSize.height / 3, newSize.height / 9,
                newSize.height / 11);
        planeLabel.setBounds(newSize.width / 4,
                newSize.height / 4, newSize.height / 2,
                newSize.height / 2);
        planeLabel.reload();
        numberLabel.setBounds(newSize.width / 3,
                newSize.height / 3, newSize.height / 1,
                newSize.height / 1);
        numberLabel.reload();
        boxMoney.setBounds(newSize.width / 40,
                newSize.height / 2, newSize.height / 11,
                newSize.height / 17);
        moneyLabel.setBounds(newSize.width / 40,
                newSize.height / 3, newSize.height / 1,
                newSize.height / 1);
        moneyLabel.reload();
    }
}
