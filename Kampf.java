import java.util.*;
import java.io.*;
import java.util.Random;
public class Kampf
{
    private static boolean firstFight, firstMeeleFight, firstRangedFight;
    private static float start,end;
    private static Scanner sc=new Scanner(System.in);
    public static void kampf(Mob M)
    {
        Spiel.konsole.println("");
        while(true)
        {
            // Kampfanleitung
            if(!firstFight)
            {
                for(int i = 0; i < 5; i++)
                {
                    Spiel.konsole.printD("Kampf", i);
                }
                Spiel.konsole.println("Willst du "+ M.getName() + " bekämpfen?");
                firstFight = true;
            }
            else
            {
                Spiel.konsole.println( Spiel.konsole.getPrintDString("Kampf", 0) );
                Spiel.konsole.println("Willst du "+ M.getName() + " bekämpfen?");
            }
            // Prüfe, ob Spieler Kämpfen oder fliehen möchte
            String st = Spiel.konsole.getInput();
            if(Spiel.konsole.compareInput("Nein", st) || Spiel.konsole.compareInput("Fliehen", st))
            {
                Spiel.konsole.println("Na dann weg hier!");
                GameManager.mobSP = 50;
                break;
            }
            else 
            {
                Spiel.konsole.println("Die aktuelle HP von dir beträgt " + (int)GameManager.P.getHP());
                Spiel.konsole.println("Die aktuelle HP vom " + M.getName() + " beträgt " + (int)M.getHP());
                // Der Kampf endet erst,sobald der Spieler oder das Monster tot ist
                while(true)
                {
                    Spiel.konsole.printD("Kampf", 6);
                    Spiel.konsole.printD("Kampf", 7);
                    // Entscheide die Schwierigkeit des Angriffes
                    // Höherer Schwierigkeitsgrad = Damagebonus/Verteidigungsbonus
                    String s = Spiel.konsole.getInput();
                    if(Spiel.konsole.compareInput("Leicht", s))
                        M.getDamage(GameManager.P.getInventar().getEquipItem("weapon").getPower(), Kampfmechanik(1, "Leicht", GameManager.P.getInventar().getEquipItem("weapon").getType()));
                    else if(Spiel.konsole.compareInput("Mittel", s))
                        M.getDamage(GameManager.P.getInventar().getEquipItem("weapon").getPower(), Kampfmechanik(1, "Mittel", GameManager.P.getInventar().getEquipItem("weapon").getType()));
                    else if(Spiel.konsole.compareInput("Schwer", s))
                        M.getDamage(GameManager.P.getInventar().getEquipItem("weapon").getPower(), Kampfmechanik(1, "Schwer", GameManager.P.getInventar().getEquipItem("weapon").getType()));
                    else 
                        Spiel.konsole.printD("Fehler", 1);
                    if(GameManager.P.getHP() <= 1 || M.getHP() <= 1)
                        break;
                    Spiel.konsole.println(M.getName() +"  hat " + (int)M.getHP() + " HP.");
                    Spiel.konsole.getInput();
                    
                    // Entscheide Schwierigkeit der Verteidigung
                    Spiel.konsole.println(M.getName() + " greift dich an.");
                    Spiel.konsole.printD("Kampf", 9);
                    String s1 = Spiel.konsole.getInput();
                    if(Spiel.konsole.compareInput("Leicht", s1))
                         GameManager.P.getDamage(M.getWeaponPower(), Kampfmechanik(0, "Leicht", GameManager.P.getInventar().getEquipItem("armour").getType()));
                    else if(Spiel.konsole.compareInput("Mittel", s1))
                         GameManager.P.getDamage(M.getWeaponPower(), Kampfmechanik(0, "Mittel", GameManager.P.getInventar().getEquipItem("armour").getType()));
                    else if(Spiel.konsole.compareInput("Schwer", s1))
                         GameManager.P.getDamage(M.getWeaponPower(), Kampfmechanik(0, "Schwer", GameManager.P.getInventar().getEquipItem("armour").getType()));
                    else 
                         Spiel.konsole.printD("Fehler", 1);
                    if(GameManager.P.getHP() <= 1 || M.getHP() <= 1)
                        break;
                    Spiel.konsole.println("Die aktuelle HP von dir beträgt " + (int)GameManager.P.getHP());
                    Spiel.konsole.getInput();
                }
                // Kampfende
                if(M.getHP() <= 1)
                {
                    Spiel.konsole.println("Du hast " + M.getName() +" besiegt.");
                    GameManager.P.getInventar().addItem(M.getLoot());
                    int g = new Random().nextInt(10)+20;
                    GameManager.P.changeGoldMuenzen(g);
                    Spiel.konsole.println("Du erbeutest: " + M.getLoot().getName() + " und "+ g + " Goldmünzen.");
                }else
                {
                    Spiel.konsole.println(M.getName() + " hat dich besiegt.");
                    GameManager.P.respawn();
                }
            }
            break;
        }
    }
    // Die selbe Methode allerdings für Missionen. Gibt Boolean zurück, der entscheidet ob Kampf gewonnen wurde
    public static boolean missionKampf(Mob M)
    {
        Spiel.konsole.println("Die aktuelle Hp von dir beträgt " + (int)GameManager.P.getHP());
        Spiel.konsole.println("Die aktuelle Hp vom " + M.getName() + " beträgt " + (int)M.getHP());
        while(true)
        {
            Spiel.konsole.printD("Kampf", 6);
            Spiel.konsole.printD("Kampf", 7);
            String s = Spiel.konsole.getInput();
            if(Spiel.konsole.compareInput("Leicht", s))
                M.getDamage(GameManager.P.getInventar().getEquipItem("weapon").getPower(), Kampfmechanik(1, "Leicht", GameManager.P.getInventar().getEquipItem("weapon").getType()));
            else if(Spiel.konsole.compareInput("Mittel", s))
                M.getDamage(GameManager.P.getInventar().getEquipItem("weapon").getPower(), Kampfmechanik(1, "Mittel", GameManager.P.getInventar().getEquipItem("weapon").getType()));
            else if(Spiel.konsole.compareInput("Schwer", s))
                M.getDamage(GameManager.P.getInventar().getEquipItem("weapon").getPower(), Kampfmechanik(1, "Schwer", GameManager.P.getInventar().getEquipItem("weapon").getType()));
            else 
                Spiel.konsole.printD("Fehler", 1);
            if(GameManager.P.getHP() <= 1 || M.getHP() <= 1)
                break;
            Spiel.konsole.println(M.getName() +"  hat " + (int)M.getHP() + " HP.");
            Spiel.konsole.getInput();
            
            Spiel.konsole.println(M.getName() + " greift dich an.");
            Spiel.konsole.printD("Kampf", 9);
            String s1 = Spiel.konsole.getInput();
            if(Spiel.konsole.compareInput("Leicht", s1))
                 GameManager.P.getDamage(M.getWeaponPower(), Kampfmechanik(0, "Leicht", GameManager.P.getInventar().getEquipItem("armour").getType()));
            else if(Spiel.konsole.compareInput("Mittel", s1))
                 GameManager.P.getDamage(M.getWeaponPower(), Kampfmechanik(0, "Mittel", GameManager.P.getInventar().getEquipItem("armour").getType()));
            else if(Spiel.konsole.compareInput("Schwer", s1))
                 GameManager.P.getDamage(M.getWeaponPower(), Kampfmechanik(0, "Schwer", GameManager.P.getInventar().getEquipItem("armour").getType()));
            else 
                 Spiel.konsole.printD("Fehler", 1);
            if(GameManager.P.getHP() <= 1 || M.getHP() <= 1)
                break;
            Spiel.konsole.println("Die aktuelle HP von dir beträgt " + (int)GameManager.P.getHP());
            Spiel.konsole.getInput();
        }
        if(M.getHP() <= 1)
        {
            Spiel.konsole.println("Du hast " + M.getName() +" besiegt.");
            GameManager.P.getInventar().addItem(M.getLoot());
            int g = new Random().nextInt(10)+20;
            GameManager.P.changeGoldMuenzen(g);
            Spiel.konsole.println("Du erbeutest: " + M.getLoot().getName() + " und "+ g + " Goldmünzen.");
            return true;
        }else
        {
            Spiel.konsole.println(M.getName() + " hat dich besiegt.");
            GameManager.P.respawn();
            return false;
        }
    }
    
    
    public static double Kampfmechanik(int b, String dif, String type)
    {
        double km = 0;
        // Bei der Kampfmechanik kann man einen Damage- bzw. Verteidigungsbonus kriegen
        // Gibt man eine falsche Lösung in der richtigen Zeit, kriegt man keinen Bonus
        
        if(dif.equals("Leicht") || dif.equals("Mittel") || dif.equals("Schwer"))
        {
            if(type.equals("range"))
            {
                // Kampfanleitung
                if(!firstRangedFight)
                {
                    Spiel.konsole.printD("Kampf", 10);
                    Spiel.konsole.printD("Kampf", 11);
                    Spiel.konsole.printD("Kampf", 14);
                    firstRangedFight = true;
                }
                int rn = new Random().nextInt(5);
                Spiel.konsole.println("Schreibe: " + Spiel.konsole.getPrintDString(dif, rn));
                startStopWatch();
                String s = Spiel.konsole.getInput();
                // Wort muss in bestimmter Zeit abgeschrieben werden
                if(s.equals(Spiel.konsole.getPrintDString("Leicht", rn)) && stopStopWatch() < 5.0)
                {
                    Spiel.konsole.printD("Kampf", 19);
                    km = 0.3 + b;
                    return km;
                }    
                else if(s.equals(Spiel.konsole.getPrintDString("Mittel", rn)) && stopStopWatch() < 8.0)
                {
                    Spiel.konsole.printD("Kampf", 19);
                    km = 0.6 + b;
                    return km;
                }    
                else if(s.equals(Spiel.konsole.getPrintDString("Schwer", rn)) && stopStopWatch() < 10.0)
                {
                    if(rn ==4)
                        Spiel.konsole.printD("Kampf", 23);
                    else
                        Spiel.konsole.printD("Kampf", 19);
                    km = 0.9 + b;
                    return km;
                }
                else if((s.equals(Spiel.konsole.getPrintDString("Leicht", rn)) && stopStopWatch() > 5.0) || (s.equals(Spiel.konsole.getPrintDString("Mittel", rn))
                        && stopStopWatch() < 8.0) || (s.equals(Spiel.konsole.getPrintDString("Schwer", rn)) && stopStopWatch() < 10.0))
                {
                     // Ist man zu langsam, macht man gar keinen Schaden
                     Spiel.konsole.printD("Kampf", 16);
                     if(b==1)
                        return km=0;
                }
                else
                    Spiel.konsole.printD("Kampf", 17);
            }
            if(type.equals("meele") || type.equals("armour"))
            {
                // Kampfanleitung
                if(!firstMeeleFight)
                {
                    Spiel.konsole.printD("Kampf", 12);
                    Spiel.konsole.printD("Kampf", 13);
                    Spiel.konsole.printD("Kampf", 14);
                    firstMeeleFight = true;
                }
                // Eine Rechenaufgabe muss in einer bestimmten Zeit gelöst werden
                if(dif.equals("Leicht"))
                {
                    int rn1 = new Random().nextInt(500);
                    int rn2 = new Random().nextInt(500);
                    Spiel.konsole.println("" + rn1 + " + " + rn2 + " = ?");
                    startStopWatch();
                    if(convertInputStringToInt()==rn1+rn2  && stopStopWatch() < 10 )
                    {
                        Spiel.konsole.printD("Kampf", 18);
                        km = 0.3 + b;
                        return km;
                    }
                    else if(stopStopWatch()>10)
                        Spiel.konsole.printD("Kampf", 16);
                    else
                        Spiel.konsole.printD("Kampf", 15);
                }    
                else if(dif.equals("Mittel"))
                {
                    int rn1 = new Random().nextInt(1000);
                    int rn2 = new Random().nextInt(1000);
                    Spiel.konsole.println("" + rn1 + " - " + rn2 + " = ?");
                    startStopWatch();
                    if(convertInputStringToInt()==rn1-rn2 && stopStopWatch() < 15)
                    {
                        Spiel.konsole.printD("Kampf", 18);
                        km = 0.6 + b;
                        return km;
                    }
                    else if(stopStopWatch()>15)
                        Spiel.konsole.printD("Kampf", 16);
                    else
                        Spiel.konsole.printD("Kampf", 15);
                }    
                else if(dif.equals("Schwer"))
                {
                    int rn1 = new Random().nextInt(30)+2;
                    int rn2 = new Random().nextInt(30)+2;
                    Spiel.konsole.println("" + rn1 + " * " + rn2 + " = ?");
                    startStopWatch();
                    if(convertInputStringToInt()==rn1*rn2 && stopStopWatch() < 20)
                    {
                        Spiel.konsole.printD("Kampf", 18);
                        km = 0.9 + b;
                        return km;
                    }
                    else if(stopStopWatch()>20)
                       {
                        // Wenn man zu langsam ist, macht man gar kein Schaden
                        Spiel.konsole.printD("Kampf", 16);
                        if(b==1)
                            return km=0;
                        }
                    else
                        Spiel.konsole.printD("Kampf", 15);
                }  
            }            
        }
        else
        {
            Spiel.konsole.printD("Fehler",1);
        }
        if(b==0)
            return km = 0;
        return km = 1;    
    }
    
    // Es wird die Systemzeit zu Beginn und Anfang genommen. Anschließend nimmt man die Differenz
    // um die Zeit dazwischen zu errechnen
    public static void startStopWatch()
    {
        start = System.currentTimeMillis();
    }
    
    public static int stopStopWatch()
    {
        end = System.currentTimeMillis();
        float t = (end - start)/1000;
        return (int)t;
    }
    
    // Tut was es sagt
    private static int convertInputStringToInt()
    {
        int s;
        try {
                       s = Integer.parseInt(Spiel.konsole.getInput());
                    }
                    catch (NumberFormatException e) {
                       s = 0;
                    }
        return s;
    }
    
}
