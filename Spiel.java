import java.util.*;
import java.io.*;
import java. awt.*;

public class Spiel
{
    static GameManager G;
    static Konsole konsole;
    public static void main(String[] args)
    {
        konsole = new Konsole();
        konsole.build();
        while(true)
        {
            konsole.printD("System",0);
            konsole.printD("System",1);
            konsole.getInput();
            G = new GameManager();
            konsole.setBackground(13,42, 40, 255);
            konsole.setImage("astart");
            konsole.println("Willst du nochmal Spielen ?");
            if(konsole.compareInput("Ja", konsole.getInput()) )
            {
                konsole.clear();
            }else
            {
                konsole.close();
                break;
            }
        }
    }
}
