import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;
import java.util.List;
import java.util.*;
import java.lang.*;
import javax.imageio.ImageIO;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Konsole implements ComponentListener
{
    JFrame f;
    private JPanel jp;
    protected JTextArea textArea;
    Font font = initializeFont();
    static int fileSizeOut = getNumberOfLinesOut();
    static int fileSizeIn = getNumberOfLinesIn();
    JTextField JT = new JTextField();
    JLabel Image = null;
    GroupLayout gbc;
    
    public void build()
    {
        f = new JFrame("Textadventure");
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        jp = new JPanel(); 
        jp.setBackground(new Color(13,42, 40, 255));
        jp.setFocusable(true);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane (textArea, 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        new SmartScroller(scrollPane, SmartScroller.VERTICAL, SmartScroller.END);
        scrollPane.setPreferredSize(new Dimension(900, 1000));

        JT.setPreferredSize(new Dimension(900, 30));
        JT.setVisible(false);

        try
        {
            Image image = ImageIO.read(new File(new File("Bilder").getAbsolutePath() + "/" + ("astart"+".png")) );
            Image = new JLabel()
            {
                public void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null), null);
                }
            };
            Image.setPreferredSize(new Dimension(450,718));
        }catch(IOException e) 
        {
            e.printStackTrace();
        }
        
        gbc = new GroupLayout(jp);
        gbc.setHonorsVisibility(false);
        gbc.setAutoCreateGaps(true);
        gbc.setAutoCreateContainerGaps(true);
        gbc.setHorizontalGroup(gbc.createSequentialGroup()
            .addGroup(gbc.createParallelGroup().addComponent(scrollPane).addComponent(JT))
            .addComponent(Image,0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE) );
        gbc.setVerticalGroup(gbc.createParallelGroup()
            .addGroup(gbc.createSequentialGroup().addComponent(scrollPane).addComponent(JT))
            .addComponent(Image,0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE) );

        jp.setLayout(gbc);

        f.getContentPane().add(jp);
        f.addComponentListener(this);
        f.pack();
        f.setVisible(true);
    }
    
    public void println(String s)
    {
        //write with output 
        if(s.length() >= 96)
        {
            char[] ch = new char[s.length()];
            for(int i = 0; i < s.length(); i++)
            {
                ch[i] = s.charAt(i);
            }
            int pos = 95;
            while(!Character.isSpaceChar(ch[pos]) || !Character.isWhitespace(ch[pos]))
            {
                pos--;
            }
            pos++;
            char[] part1 = new char[pos];
            char[] part2 = new char[s.length() - pos];
            System.arraycopy(ch, 0           , part1, 0     , part1.length);
            System.arraycopy(ch, part1.length, part2, 0     , part2.length);
            
            Output.println(String.valueOf(part1)+ "", textArea);
            Output.println("  " + String.valueOf(part2),textArea);
        }else
        {
            Output.println(s, textArea);
        }
    }
    
    public void printD(String string, int n)
    {
        String number = n + "";
        String text = fileReader.readLine(string, number, fileSizeOut);
        if(text.trim().length() != 0)
        {
            println(text);
        }
    }
    
    public String getPrintDString(String string, int n)
    {
        String number = n + "";
        String text = fileReader.readLine(string, number, fileSizeOut);
        if(text.trim().length() != 0)
        {
            return text;
        }
        else
            return null;
    }
    
    public void printDAnhaengen(String string, String anhaengen, int n)
    {
        String number = n + "";
        println(fileReader.readLine(string, number, fileSizeOut)+ anhaengen);
    }
    
    public void printDVoranSchreiben(String string, String voranSchreiben, int n)
    {
        String number = n + "";
        println(voranSchreiben + fileReader.readLine(string, number, fileSizeOut));
    }
       
    public void printDMission(String dialog, String mission, int n)
    {
        String number = n + "";
        String randNumber = new Random().nextInt(5) + 1 + "";
        println(fileReader.readLine(dialog, randNumber, fileSizeOut) + " " + fileReader.readLine(mission, number, fileSizeOut));
    }
    
    public void printList(List<Item> l)
    {
        l.forEach(item -> 
            {
                if(item.getPower() != 0)
                    println(item.getAmount() + " x " + item.getName() + " mit der Stärke " + item.getPower());
                else 
                    println(item.getAmount() + " x " + item.getName());
            });
    }
    
    public void printListShop(List<Item> l)
    {
        l.forEach(item -> println(item.getAmount() + " x " + item.getName() 
            + " mit der Stärke " + item.getPower() + " und einem Preis von " + item.getWert() + " Goldmünzen.") );
    }
    public void printListM(List<Item> l)
    {
        l.forEach(item -> println(item.getAmount() + " x " + item.getName() 
                  + " mit der Stärke " + item.getPower() + " mit einem Verkaufpreis von " + item.getWert() + " Goldmünzen.") );
    }
    public void printWorldNames()
    {
        File folder = new File("Spielstaende");
        //array zu liste kann eigentlich nicht removen deshalb LinkedList
        List <File> listOfFiles = new LinkedList<File>(Arrays.asList(folder.listFiles()) ); 
        for (File file : listOfFiles) {
            if (file.isFile()) {
                println(file.getName().replace(".bin","")); 
            }
        }
    }
    public String getInput()
    {
        JT.setText("");
        JT.setVisible(true);
        jp.revalidate();
        jp.repaint();
        JT.requestFocus();
        
        Input.getInput(JT, this);
        try{
            synchronized (this) 
                {
                       this.wait();
                }
        }catch(Exception ex)
        {
            System.err.println(ex);
        }
        
        JT.setVisible(false);
        jp.revalidate();
        jp.repaint();
        return Input.returnInput();
    }
    
    public void clear()
    {
        textArea.setText("");
    }
    
    public void clearLine()
    {
        try{
            int start = textArea.getLineStartOffset(textArea.getLineCount()-2);
            int end = textArea.getLineEndOffset(textArea.getLineCount()-1);
            textArea.replaceRange("", start, end);
        }catch(Exception ex)
        {
            System.err.println(ex);
        }       
    }
    
    public boolean compareInput(String meaning, String answer)
    {
        return fileReader.containsString(meaning + ":", answer, fileSizeIn);
    }
    
    public void setBackground(int r, int g, int b, int a)
    {
        jp.setBackground(new Color(r,g,b,a));
        jp.revalidate();
        jp.repaint();
    }
    
    public void setImage(String FileName)
    {
        try
        {
            Image image = ImageIO.read( new File(
                    new File("Bilder").getAbsolutePath() + "/" + (FileName+".png")) );
            JLabel newImage = new JLabel()
            {
                public void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null), null);
                }
            };
            newImage.setPreferredSize(new Dimension(450,718));
            gbc.replace(Image, newImage);
            Image = newImage;
        }catch(IOException e) 
        {
            e.printStackTrace();
        }
        jp.revalidate();
        jp.repaint();
    }
    
    public static int getNumberOfLinesIn()
    {
        int lines = 0;
        try
        {
            FileReader file = new FileReader("input.txt");
            BufferedReader reader = new BufferedReader(file);
            while (reader.readLine() != null) 
                lines++;
            reader.close();
        }
        catch(IOException e) 
        {
          e.printStackTrace();
        }
        return lines;
    }
    public static int getNumberOfLinesOut()
    {
        int lines = 0;
        try
        {
            FileReader file = new FileReader("output.txt");
            BufferedReader reader = new BufferedReader(file);
            while (reader.readLine() != null) 
                lines++;
            reader.close();
        }
        catch(IOException e) 
        {
          e.printStackTrace();
        }
        return lines;
    }
    public void close()
    {
        f.dispose();
        f.setVisible(false);
    }
    
    private Font initializeFont()
    {
        try
        {
            Font f = Font.createFont(Font.TRUETYPE_FONT,
            new File(new File("Font").getAbsolutePath() + "/Lora.ttf"));
            return f;
        }catch(IOException|FontFormatException e) {
            System.err.println(e);
        }   
        return null;
    }
        
    @Override
    public void componentResized(ComponentEvent e) 
    {
            float size = f.getWidth()/80;
            font = font.deriveFont(size);
            textArea.setFont(font);
            JT.setFont(font);
            jp.revalidate();
            jp.repaint();
    }
    
    @Override
    public void componentHidden(ComponentEvent e) {}
    
    @Override
    public void componentMoved(ComponentEvent e) {}
    
    @Override
    public void componentShown(ComponentEvent e) {}
}
