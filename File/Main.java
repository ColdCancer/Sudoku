import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
	// write your code here
        Demo MyFrame = new Demo();
    }
}

class Information {
    // Layout component
    static JButton[][] gridButton = new JButton[9][9];
    static JRadioButton[] degree = new JRadioButton[3];
    static JLabel step, accomplish, time;
    static Timer timer;
    static JButton start;

    // Game data
    static boolean[][] visit = new boolean[9][9];
    static int[] timeValue = new int[2];
    static int accomplishValue, stepValue, total;
    static Sudoku game;
    
    // Update data: update the record data for each step (fill in the number)
    static void UpDate() {
        double rate = (double)accomplishValue / total * 10000;
        step.setText("Step: " + stepValue);
        accomplish.setText("Ac: " + (int)rate / 100.0 + " %"); // Keep two decimal places
        time.setText("Time: " + timeValue[0] + " : " + timeValue[1]);
    }

    // Game initialization: generate legal data and initialize record data
    private static void init() {
        game = new Sudoku();
        timeValue[0] = timeValue[1] = 0;
        accomplishValue = stepValue = 0;
        for(int i = 0; i < 3; i++) {
            if(degree[i].isSelected()) {
                total = 9 + 18 * i;
            }
        }
    }

    /**
     *     Game start: initialize the data first, then set the components available,
     *     generate the game layout, and the timer starts to operate
      */
    static void run() {
        init();
        for(int i = 0; i < 3; i++) {
            degree[i].setEnabled(false);
        }
        UpDate();
        for(int i = 0; i < 81; i++) {
            int x = i / 9, y = i % 9;
            String content = Integer.toString(game.getNumber(x, y));
            gridButton[x][y].setText(content);
            gridButton[x][y].setBackground(Color.white);
            gridButton[x][y].setEnabled(false);
        }
        randomlyGeneratedLayout();
        timer.start();
    }

    // Game stop: stop timing and component unavailable
    static void stop() {
        timer.stop();
        for(int i = 0; i < 3; i++) {
            degree[i].setEnabled(true);
        }
        for(int i = 0; i < 81; i++) {
            int x = i / 9, y = i % 9;
            gridButton[x][y].setEnabled(false);
        }
    }

    // Randomly generate game layout: Based on the generated game data
    private static void randomlyGeneratedLayout() {
        Random rand = new Random();
        int count = total;
        while(count != 0) {
            int i = rand.nextInt(80);
            int x = i / 9, y = i % 9;
            if(!"".equals(gridButton[x][y].getText())) {
                visit[x][y] = false;
                game.setNumber(x, y, -1);
                gridButton[x][y].setBackground(Color.LIGHT_GRAY);
                gridButton[x][y].setText("");
                gridButton[x][y].setEnabled(true);
                count--;
            }
        }
    }

}

// Window layout and function class
class Demo extends JFrame {

    Demo() {
        setTitle("Sudoku");
        // Set window center (relative to screen)
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int frameWidth = 430, frameHeight = 300;
        int frameX = (screenWidth - frameWidth) / 2;
        int frameY = (screenHeight - frameHeight) / 2;
        setBounds(frameX, frameY, frameWidth, frameHeight);

        setLayout(null);
        setResizable(false);

        // Left side component layout (step,accomplish,time,start)
        JPanel menu = new JPanel(new GridLayout(4, 1, 15, 10));
        // 1.游戏难度等级
        JPanel grade = new JPanel(new GridLayout(3, 1, 5, 5));
        String[] degreenName = {"Easy", "Medium", "Hard"};
        ButtonGroup grp = new ButtonGroup();
        for(int i = 0; i < 3; i++) {
            grade.add(Information.degree[i] = new JRadioButton(degreenName[i]));
            grp.add(Information.degree[i]);
        }
        Information.degree[0].setSelected(true);
        grade.setBounds(50,18,80,70);
        add(grade);
        // 2.Step count
        Information.step = new JLabel("step: - ", JLabel.CENTER);
        menu.add(Information.step);
        // 3.Completion rate record
        Information.accomplish = new JLabel("Accomplish: - ", JLabel.CENTER);
        menu.add(Information.accomplish);
        // 4.Time record
        Information.time = new JLabel("Time: - ", JLabel.CENTER);
        menu.add(Information.time);
        // 5.Start button
        Information.start = new JButton("Start");
        menu.add(Information.start);
        // Left component joins window and sets position
        add(menu);
        menu.setBounds(35, 102, 80, 140);

        // Right component layout (3 * 3 small cells in 3 * 3 large cells)
        // 1.Set the layout style of small palace panels (9 panels)
        JPanel[] pan = new JPanel[9];
        for(int i = 0; i < 9; i++) {
            pan[i] = new JPanel(new GridLayout(3, 3, 2, 2));
        }
        // 2.Set the layout style of the large grid panel
        JPanel gridPan = new JPanel(new GridLayout(3, 3, 4, 4));
        gridPan.setBounds(150, 5, 250, 250);
        // 3.All the small palaces are added to the big one
        for(int i = 0; i < 9; i++) {
            gridPan.add(pan[i]);
        }
        // 4.Set 9 * 9 cells and add panel
        for(int i = 0; i < 81; i++) {
            int x = i / 9, y = i % 9;
            Information.gridButton[x][y] = new JButton();
            Information.gridButton[x][y].setEnabled(false);
            Information.gridButton[x][y].setFont(new Font("Dialog", Font.BOLD, 17));
            Information.gridButton[x][y].setHorizontalAlignment(JButton.CENTER);
            Information.gridButton[x][y].setBorder(BorderFactory.createLineBorder(Color.black));
            pan[x / 3 * 3 + y / 3].add(Information.gridButton[x][y]);
        }
        add(gridPan);

        // Add a monitor to each cell to fill in the number
        for(int i = 0; i < 81; i++) {
            int x = i / 9, y = i % 9;
            Information.gridButton[x][y].addKeyListener(new MyKey(x, y));
        }

        // Monitor game progress, set game start or end
        Information.start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if("Start".equals(Information.start.getText())) {
                    Information.start.setText("Stop");
                    Information.run();
                } else {
                    Information.start.setText("Start");
                    Information.stop();
                }
            }
        });

        // Timer monitoring, recording game time
        Information.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Information.timeValue[1]++;
                if(Information.timeValue[1] == 60) {
                    Information.timeValue[1] = 0;
                    Information.timeValue[0]++;
                }
                String content = "Time: " + Information.timeValue[0] + " : " + Information.timeValue[1];
                Information.time.setText(content);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

}

// Key Listener class
class MyKey extends KeyAdapter {
    // x, y is the position of the button monitored by the corresponding keyboard in the Palace (x, y)
    private int x, y;

    MyKey(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() < '1' || '9' < e.getKeyChar()) return;
        int key = e.getKeyChar() - '0';
        JButton bt = (JButton)e.getSource();
        bt.setText(Integer.toString(key));
        Information.game.setNumber(x, y, key);
        Information.stepValue++;
        System.out.println(Information.stepValue + " : ( " + x + " , " + y + " ) -> " + key);
        if(Information.game.check(x, y, key)) {
            if(!Information.visit[x][y]) {
                Information.visit[x][y] = true;
                Information.accomplishValue++;
            }
            bt.setBackground(Color.GREEN);
            Information.UpDate();
            if(Information.total == Information.accomplishValue) {
                Information.stop();
                Information.start.setText("Start");
                String content = "Win the game!";
                JOptionPane.showMessageDialog(null, content);
                System.out.println("Success!");
            }
        } else {
            if(Information.visit[x][y]) {
                Information.visit[x][y] = false;
                Information.accomplishValue--;
            }
            bt.setBackground(Color.red);
            Information.UpDate();
        }
    }
}

