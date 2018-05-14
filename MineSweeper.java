/**
 * This class contains all aspects of the MineSweeper game. Enjoy!
 *
 * @author Richard Sun
 * @version 0.0.1
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MineSweeper //This class that contains everything including the other class, as it is nested.
{

    private JPanel panel;
    private JFrame frame;
   
    private int i;
    private int j;
   
    private JButton buttons[][];
    private int[] MinesX;
    private int[] MinesY;
   
    private AL Listener;
    
    
    private ImageIcon blanks = new ImageIcon ("img/blanks.png");
    private ImageIcon num0 = new ImageIcon ("img/num0.png");
    private ImageIcon num1 = new ImageIcon ("img/num1.png");
    private ImageIcon num2 = new ImageIcon ("img/num2.png");
    private ImageIcon num3 = new ImageIcon ("img/num3.png");
    private ImageIcon num4 = new ImageIcon ("img/num4.png");
    private ImageIcon num5 = new ImageIcon ("img/num5.png");
    private ImageIcon num6 = new ImageIcon ("img/num6.png");
    private ImageIcon num7 = new ImageIcon ("img/num7.png");
    private ImageIcon num8 = new ImageIcon ("img/num8.png");
    private ImageIcon calmMine = new ImageIcon ("img/calmMine.jpg");
    private ImageIcon detMine = new ImageIcon ("img/detMine.jpg");
        
    public static void main (String[] args)  //This method is the main method, and is executed first. 
    {
        MineSweeper game = new MineSweeper();
        game.createFrame();
        game.addButtons();
        game.createMines();
        game.startGame();
    }
   
    public void createMines() //This method is called to create 10 randomly generated mines. If a duplicate mine is created, it is destroyed and replace by another random mine.
    {
        MinesX = new int[10];
        MinesY = new int[10];
        int mines;
        int others = 0;
        double temp = 0;
       
        for(mines = 0; mines < 10; mines++)
        {
            temp = Math.random();
            temp = temp * 9;
            MinesX[mines] = (int)temp;
            temp = Math.random();
            temp = temp * 9;
            MinesY[mines] = (int)temp;
            for (others = mines; others != 0; others--) //This loop checks for duplicate mines.
            {
                if((MinesX[mines] == MinesX[others - 1]) && (MinesY[mines] == MinesY[others - 1]))
                {
                    mines--;
                }
            }
        }
    }
   
    public void startGame() //This method sets the frame to visible and starts the game loop.
    {
        frame.setVisible(true);
        gameLoop();
    }
    
    public void gameLoop() //This method is the game loop. It terminates when the game is over.
    {
        while (Listener.getGame() == true)
        {
            checkButtonHit();
            checkWin();
        }
        endPopUp(); //After the game ends, this method is called to pop up a victory window.
    }
   
    public void createFrame() //This method creates the game frame and configures it. Also, it creates a panel and attaches it to the frame.
    {
        frame = new JFrame("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,450);
        frame.setLocation(150,200);

        panel = new JPanel();
        frame.add(panel);
        frame.setResizable(false);
        
    }
   
    public void addButtons() //This method creates all the 81 buttons and sets them in a grid format. it also creates an instance of the class AL.
    {
        buttons = new JButton[9][9];
        GridLayout grid = new GridLayout(9,9);
        Listener = new AL();
        panel.setLayout(grid);
       
        for(i = 0; i < 9; i++)
        {
            for(j = 0; j < 9; j++)
            {       
                buttons[i][j] = new JButton(blanks); 
                panel.add(buttons[i][j]);
                buttons[i][j].addActionListener(Listener);
                buttons[i][j].setVisible(true);
            }
        }
       
    }
   
    public class AL implements ActionListener //This class is a nested clas that implements ActionListener. Mainly, this class is used to recognize when a buttons is hit, and also to contain valuable fields.
    {
       
       
        private int a = 0;
        private int b = 0;
        private int ret = 0;
        private boolean game = true;
        private boolean buttonHit = false;
        private int storedA = 0;
        private int storedB = 0;
        private int hitCount = 0;
       
        public void actionPerformed(ActionEvent e) //This method is called once a button is hit. It then calls many methods that check if the button is a mine, if its near a mine, and to reveal all the mines if it is a mine.
        {
            hitCount++;
            JButton source = (JButton)e.getSource();
            for(a = 0; a < 9; a++)
            {
                for(b = 0; b < 9; b++)
                {
                    if (buttons[a][b] == source)
                    {
                        storedA = a;
                        storedB = b;
                        game = isMine(a, b);
                        if(game == false)
                        {
                            revealMines();
                            break;
                        }
                        checkMines(a, b);
                        break;
                    }
                }
            }
            buttonHit = true;
            source.removeActionListener(Listener);
        }
       
        public void setHitFalse() //This method is used to set buttonHit to false. This is called from the other class that can not access the private field in this class.
        {
            buttonHit = false;
        }
        
        public void checkMines(int a, int b) //This method checks the nearby buttons for mines. It then sets the value of a variable to the amount of nearby mines.
        {
            ret = 0;
            int num = 0;
           
            for (num = 0; num < 10; num++)
            {
                if ((a - 1 == MinesX[num]) && (b == MinesY[num]))
                   ret++;
                if ((a - 1 == MinesX[num]) && (b - 1 == MinesY[num]))
                   ret++;
                if ((a - 1 == MinesX[num]) && (b + 1 == MinesY[num]))
                   ret++;
                if ((a + 1 == MinesX[num]) && (b == MinesY[num]))
                   ret++;
                if ((a + 1 == MinesX[num]) && (b - 1 == MinesY[num]))
                   ret++;
                if ((a + 1 == MinesX[num]) && (b + 1 == MinesY[num]))
                   ret++;
                if ((a == MinesX[num]) && (b + 1 == MinesY[num]))
                   ret++;
                if ((a == MinesX[num]) && (b - 1 == MinesY[num]))
                   ret++;
            }
        }
    
        public boolean isMine(int q, int w) //This method checks if the button pressed is a mine.
        {
            int t = 0;
           
            for(t = 0; t < 9; t++)
            {
                if ((q == MinesX[t]) && (w == MinesY[t]))
                {
                    return false;
                }
            }
            return true;
        }
       
        public void revealMines() //This method reveals all of the mines once a mine is hit.
        {
            int g = 0;
            int m = 0;
            int n = 0;
           
            for(g = 0; g < 10; g++)
            {
                for(m = 0; m < 9; m++)
                {
                    for(n = 0; n < 9; n++)
                    {
                        if ((m == MinesX[g]) && (n == MinesY[g]))
                        {
                            buttons[m][n].setIcon(resizeIcon(detMine, 75, 75));
                        }
                    }
                }
            }
           
        }
        
        public void goodRevealMines() //This method reveals all of the mines if you win the game.
        {
            int g = 0;
            int m = 0;
            int n = 0;
           
            for(g = 0; g < 10; g++)
            {
                for(m = 0; m < 9; m++)
                {
                    for(n = 0; n < 9; n++)
                    {
                        if ((m == MinesX[g]) && (n == MinesY[g]))
                        {
                            buttons[m][n].setIcon(resizeIcon(calmMine, 75, 75));
                        }
                    }
                }
            }
        } 
        
        public boolean getGame() //This method returns the value of the variable game.
        {
            return game;
        }
        
        public boolean getButtonHit()//This method returns the value of the variable buttonHit.
        {
            return buttonHit;
        }
        
        public int getRet()//This method returns the value of the variable ret.
        {
            return ret;
        }
        
        public int getA()//This method returns the value of the variable a.
        {
            return storedA;
        }
        
        public int getB()//This method returns the value of the variable b.
        {
            return storedB;
        }
        
        public int getHitCount()//This method returns the value of the variable hitCount.
        {
            return hitCount;
        }
    }
    
    public void checkButtonHit()//This method checks if a button is hit. If it is, then it calls a method that changes the image to the corresponding amount of nearby mines.
    {
        if (Listener.getButtonHit())
        {  
            setIcon(Listener.getRet());
            Listener.setHitFalse();
        }
    }

    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) //This method resizes an image before being added to a JButton.
    {
        Image img = icon.getImage();  
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
        return new ImageIcon(resizedImage);
    }
    
    public void setIcon(int x) //This method changes the pressed button the the number that corresponds to the corresponding amount of nearby mines.
    {
        int a = Listener.getA();
        int b = Listener.getB();
        if ( x == 0)
            buttons[a][b].setIcon(resizeIcon(num0, 75, 75));
        if ( x == 1)
            buttons[a][b].setIcon(resizeIcon(num1, 75, 75));
        if ( x == 2)
            buttons[a][b].setIcon(resizeIcon(num2, 75, 75));
        if ( x == 3)
            buttons[a][b].setIcon(resizeIcon(num3, 75, 75));
        if ( x == 4)
            buttons[a][b].setIcon(resizeIcon(num4, 75, 75));
        if ( x == 5)
            buttons[a][b].setIcon(resizeIcon(num5, 75, 75));
        if ( x == 6)
            buttons[a][b].setIcon(resizeIcon(num6, 75, 75));
        if ( x == 7)
            buttons[a][b].setIcon(resizeIcon(num7, 75, 75));
        if ( x == 8)
            buttons[a][b].setIcon(resizeIcon(num8, 75, 75));
    }
    
    public void endPopUp() //This method creates a pop up that is shown once you lose the game. It asks you if you would like to try again.
    {
        int loses = JOptionPane.showConfirmDialog
        (null, "You lose! Would you like to try again?", "GAME OVER", JOptionPane.YES_NO_OPTION);
        if (loses == JOptionPane.YES_OPTION)
        {
            frame.dispose();
            MineSweeper game = new MineSweeper();
            game.createFrame();
            game.addButtons();
            game.createMines();
            game.startGame();
        }
        else if (loses == JOptionPane.NO_OPTION)
        {
            System.exit(1);
        }
    }
    
    public void checkWin() //This method checks if you've won the game. If you have, it creates a pop up that tells you that you've won, and asks you if you would like to play again.
    {
        if((Listener.getHitCount() == 71) && (Listener.getGame() == true))
        {
            Listener.goodRevealMines();
            int wins = JOptionPane.showConfirmDialog
            (null, "You Win! Play again?", "Congratulations!", JOptionPane.YES_NO_OPTION);
            if(wins == JOptionPane.YES_OPTION)
            {
                frame.dispose();
                MineSweeper game = new MineSweeper();
                game.createFrame();
                game.addButtons();
                game.createMines();
                game.startGame();
            }
            else if(wins == JOptionPane.NO_OPTION)
            {
                System.exit(1);
            }
        }
    }
}