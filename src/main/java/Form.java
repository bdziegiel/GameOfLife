import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

public class Form extends JPanel implements ActionListener {

    private JButton resetButton;
    private JButton ustawPlansze;
    private JButton niezmienneButton;
    private JButton gliderButton;
    private JButton recznieButton;
    private JButton oscylatorButton;
    private JButton losowoButton;
    private JSpinner iteracje;
    private JSpinner X;
    private JSpinner Y;

    private JFrame myFrame;
    private JPanel graphicPanel;
    private JButton[][] board;

    private int boardHeight;
    private int boardWidth;
    private int iterations;
    private int[][] area;
    private int[][]nextGen;

    private Graphic graphic = new Graphic();
    private int timerCounter;

    void setAndShowGUI() {
        myFrame = new JFrame("Game of Life");
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(1100, 900);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(200, myFrame.getHeight()));

        //all the buttons
        ustawPlansze = new JButton("Ustaw planszę");
        ustawPlansze.setPreferredSize(new Dimension(150, 50));
        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(150, 50));
        niezmienneButton = new JButton("Niezmienne");
        niezmienneButton.setPreferredSize(new Dimension(150, 30));
        gliderButton = new JButton("Glider");
        gliderButton.setPreferredSize(new Dimension(150, 30));
        recznieButton = new JButton("Ręczna definicja");
        recznieButton.setPreferredSize(new Dimension(150, 30));
        oscylatorButton = new JButton("Oscylator");
        oscylatorButton.setPreferredSize(new Dimension(150, 30));
        losowoButton = new JButton("Losowo");
        losowoButton.setPreferredSize(new Dimension(150, 30));

        //all the spinners
        SpinnerModel model = new SpinnerNumberModel(10, 10, 100, 1);
        iteracje = new JSpinner(model);
        iteracje.setPreferredSize(new Dimension(150, 30));
        SpinnerModel model1 = new SpinnerNumberModel(20, 20, 100, 10);
        //SpinnerModel model2 = new SpinnerNumberModel(20, 20, 100, 10);
        X = new JSpinner(model1);
        Y = new JSpinner(model1);
        X.setPreferredSize(new Dimension(150, 30));
        Y.setPreferredSize(new Dimension(150, 30));

        //add all components to panel
        buttonPanel.add(new JLabel("Liczba iteracji:"));
        buttonPanel.add(iteracje);
        buttonPanel.add(new JLabel("Wymiar siatki X,Y:"));
        buttonPanel.add(X);
        buttonPanel.add(Y);
        buttonPanel.add(ustawPlansze);
        buttonPanel.add(new JLabel("Stany początkowe"));
        buttonPanel.add(niezmienneButton);
        buttonPanel.add(gliderButton);
        buttonPanel.add(recznieButton);
        buttonPanel.add(oscylatorButton);
        buttonPanel.add(losowoButton);
        buttonPanel.add(resetButton);

        graphicPanel = new JPanel();

        myFrame.add(graphicPanel, BorderLayout.CENTER);
        myFrame.add(buttonPanel, BorderLayout.EAST);
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ustawPlansze.addActionListener(setSizeListener);
        resetButton.addActionListener(setSizeListener);
        niezmienneButton.addActionListener(this);
        oscylatorButton.addActionListener(this);
        losowoButton.addActionListener(this);
        gliderButton.addActionListener(this);
        recznieButton.addActionListener(this);
    }
    //board buttons listener (on click)
        private ActionListener boardListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JButton) {
                    if (((JButton) e.getSource()).getBackground() == Color.WHITE)
                        ((JButton) e.getSource()).setBackground(Color.BLACK);
                    else if (((JButton) e.getSource()).getBackground() == Color.BLACK)
                        ((JButton) e.getSource()).setBackground(Color.WHITE);
                }
            }
        };

        //set board size
        private ActionListener setSizeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == ustawPlansze) {
                    iteracje.setEnabled(false);
                    X.setEnabled(false);
                    Y.setEnabled(false);

                    setBoardWidth(Integer.parseInt(X.getValue().toString()));
                    setBoardHeight(Integer.parseInt(Y.getValue().toString()));
                    setIterations(Integer.parseInt(iteracje.getValue().toString()));

                    for (int i = 0; i < graphicPanel.getComponentCount(); i++) graphicPanel.remove(i);
                    graphicPanel.repaint();
                    graphicPanel.setLayout(new GridLayout(getBoardHeight(), getBoardWidth()));

                    board = new JButton[getBoardWidth()][getBoardHeight()];
                    for (int i = 0; i < getBoardWidth(); i++) {
                        for (int j = 0; j < getBoardHeight(); j++) {
                            board[i][j] = new JButton();
                            board[i][j].setOpaque(true);
                            board[i][j].setBackground(Color.WHITE);
                            board[i][j].setPreferredSize(new Dimension(25, 25));
                            board[i][j].addActionListener(boardListener);
                            graphicPanel.add(board[i][j]);
                        }
                    }

                    myFrame.setVisible(true);
                }
                if (e.getSource() == resetButton) {
                    graphicPanel.removeAll();
                    graphicPanel.repaint();
                    iteracje.setEnabled(true);
                    X.setEnabled(true);
                    Y.setEnabled(true);
                }
            }
        };

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == niezmienneButton) area = graphic.setNiezmienne(getBoardWidth(), getBoardHeight()).clone();
        if (source == losowoButton) area = graphic.setLosowo(getBoardWidth(), getBoardHeight()).clone();
        if (source == oscylatorButton) area = graphic.setOscylator(getBoardWidth(), getBoardHeight()).clone();
        if (source == gliderButton) area = graphic.setGlider(getBoardWidth(),getBoardHeight()).clone();
        if (source == recznieButton) area = graphic.setRecznie(getBoardWidth(), getBoardHeight(), board).clone();
        timerCounter =0;

        //timer handler
        final Timer myTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                nextGen = (graphic.setNextGen(area)).clone();
                graphic.setBoard(nextGen, board);
                area = nextGen.clone();
                System.out.println(timerCounter);
                timerCounter++;
                if(timerCounter ==getIterations()) myTimer.cancel();
            }
        };

        graphic.setBoard(area, board);
        myTimer.schedule(task, 500, 500);
    }

    private void setIterations(int iterations) {
        this.iterations = iterations;
    }

    private int getIterations() {
        return iterations;
    }

    private void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    private void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    private int getBoardHeight() {
        return boardHeight;
    }

    private int getBoardWidth() {
        return boardWidth;
    }
}

