import java.util.*;
import java.io.*;
import java.util.Random;

public class GameManager
{
    Map Ma;
    static Player P;
    Mob m;
    String worldName;
    private int ObjekteZuSpeichern = 3;
    private boolean neueWelt;
    private ItemList schmiedI = KONSTANTEN.schmiedI();
    private ItemList ladenI = KONSTANTEN.ladenI();
    public static int mobSP; // Mob Spawn Probability
    Random rand = new Random();
    private boolean exit = false;
    private int descriptionRand = 0;
    private int randPic = 1;
    
    public GameManager()
    {
        Spiel.konsole.clear();
        start();        
        spielen();
    }
    
    private void start()
    {
        while (true)
        {
            Spiel.konsole.printD("System",2);
            String answer = Spiel.konsole.getInput();
            if(FileSaving.saveIsEmpty() && Spiel.konsole.compareInput("AlteWelt", answer)) 
            {
               Spiel.konsole.println("Kein Speicherstand vorhanden. Du musst eine neue Welt erstellen.");
               answer = "neue Welt";
            }
            if(Spiel.konsole.compareInput("AlteWelt", answer))
            {   
                while(true) //Eingabe anfordern bis sie valide ist.
                {
                    Spiel.konsole.printD("System",4);
                    worldName = Spiel.konsole.getInput();
                    if(FileSaving.WorldsaveExists(worldName) ) 
                    { 
                        Spiel.konsole.printD("System",6);
                        loadWorld();
                        neueWelt = false;
                        break;
                    }
                    Spiel.konsole.printD("Fehler", 2);
                    Spiel.konsole.println("Namen der existierender Welten:");
                    Spiel.konsole.printWorldNames();
                }
                break;
            }   
            if(Spiel.konsole.compareInput("NeueWelt", answer))
            {
                while(true) //Eingabe anfordern bis sie valide ist.
                {
                    Spiel.konsole.printD("System",3);
                    worldName = Spiel.konsole.getInput();
                    if(worldName.trim().length() != 0 && isOnlyLetter(worldName))
                    {
                        break;
                    }
                    Spiel.konsole.printD("Fehler", 0);
                }
                Spiel.konsole.printD("System",5);
                //erstellt File
                Object o[] =  new Object[ObjekteZuSpeichern];
                FileSaving.save(worldName,o);
                //speichert start Zustand
                P = new Player();
                P.setInventar(KONSTANTEN.startInventar());
                saveWorld();
                neueWelt = true;
                break;
            }
            else
                Spiel.konsole.printD("Fehler", 1);
        }
    }
    
    private void spielen()
    {
        Spiel.konsole.clear();
        changeBackground();
        if(neueWelt)
        {
            //Einleitung
            if(!KONSTANTEN.testMode)
            {
                waitX(1300);
                for(int p = 0; p != 5; p++)
                {
                    Spiel.konsole.printD("Einleitung", p);
                    waitX(1000);
                }
                Spiel.konsole.println("Falls du Hilfe brauchst dich zurechtzufinden schreibe 'help'.");
            }
        }else
        {
            //zum Anfang kurz die Position des Spielers angeben
            Spiel.konsole.printD(P.getBigCoord() + "", 0);
            Spiel.konsole.printD(P.getCoord() + "", 0);
            Spiel.konsole.printD(P.getBigCoord() + "", rand.nextInt(10) + 1);
        }
        
        
        while(true)
        {
            //Input des Spielers umsetzen
            Instruction();
            missionAccomplished(P.missionNumber);
            saveWorld();
            if(exit)
            {
                Spiel.konsole.clear();
                break;
            }
        }
    }

    private void Instruction()
    {
            String input = Spiel.konsole.getInput();
            if(move(input)) //BEWEGUNG
            {    
                kampf();
                return; 
            }
            else if(speichern(input)) //SPEICHERN
            {
                return;
            }
            else if(stats(input)) //INVENTAR ANZEIGEN
            {
                return;
            }
            else if(use(input)) //ITEM BENUTZEN
            {
                specialEventUse();
                return;
            }
            else if(look(input)) //ZUSÄTZLICHE INFORMATIONEN ZU EINEM FELD
            {
                 return;
            }
            else if(interact(input)) //MIT EINEM FELD INTERAGIEREN
            {
                specialEventInteract();
                return;
            }
            else if(Spiel.konsole.compareInput("Aufheben", input)) //ETWAS AUFHEBEN
            {
                if(P.currentMission == null)
                {
                    Spiel.konsole.println("Hier gibt es nichts zum aufheben.");
                    return;
                }
                if(P.currentMission.type == 7 && P.currentMission.coords == P.getCoord())
                {
                    P.currentMission.done = true;
                    return;
                }
                Spiel.konsole.println("Hier gibt es nichts zum aufheben.");
                return;
            }
            else if(Spiel.konsole.compareInput("Mission", input)) // Missionsbeschreibung
            {
                if(P.currentMission!=null)
                    Spiel.konsole.printD("Mission", P.currentMission.number);
                else
                    Spiel.konsole.println("Du hast aktuell keine Mission");
                    return;
            }
            else if(Spiel.konsole.compareInput("Hilfe", input)) //BEFEHLE WERDEN AUFGELISTET
            {
                for(int i = 1; i <= 11; i += 1)
                {
                    Spiel.konsole.printD("Help", i);
                }
                return;
            }
            else if(Spiel.konsole.compareInput("Ende", input)) //SPIEL BEENDEN
            {
                exit = true;
                return;
            }
            else if(P.currentMission != null)
            {
                if(P.currentMission.type == 8 && input.equals(P.currentMission.input) && P.currentMission.coords == P.getCoord()) //Das muss natürlich in einen Dialog integriert werden
                {
                    P.currentMission.done = true;
                    return;
                }
            }
            Spiel.konsole.printD("Fehler",1); //FEHLER
    }
    
    private boolean move(String input)
    {
        if(Spiel.konsole.compareInput("gehenNord", input) )
        {
            if(P.moveUp() )
            {
                printPos();
            }   
            return true;
        }
        else if(Spiel.konsole.compareInput("gehenOst", input) )
        {
            if(P.moveRight() )
            {
                printPos();
            }
            return true;
        }
        else if(Spiel.konsole.compareInput("gehenSüd", input) )
        {
            if(P.moveDown() )
            {
                printPos();
            }   
            return true;
        }
        else if(Spiel.konsole.compareInput("gehenWest", input) )
        {
            if(P.moveLeft() )
            {
                printPos();
            }
            return true;
        }
        return false;
    }
    
    private void kampf()
    {
        int c = P.getBigCoord()/100;
        Mob m;
        if(P.currentMission != null)
        {
            if(P.currentMission.type == 5 && P.getCoord() == P.currentMission.coords) //Wenn Kampf gewonnen, kann ich hier noch nicht implementieren
            {
                Spiel.konsole.setImage(P.currentMission.mob.getName() );
                Spiel.konsole.println("");
                for(int i = 0; i < 2; i++)
                {
                    Spiel.konsole.printD("Mission", P.missionNumber * 100 + i);
                }
                Spiel.konsole.println("");
                boolean won = Kampf.missionKampf(P.currentMission.mob);
                if(won)
                {
                    P.currentMission.done = true;
                    changeBackground();
                }
            }
        }
        if(c==6 || c==9 || c==11 || c==14)
        {
            mobSP += 10;
            if(rand.nextInt(101-mobSP)+1 == 1)
            {
                mobSP = 0;
                switch(c)
                {
                    case 6:
                        m=null;
                        int i = rand.nextInt(3);
                        if(i == 0)
                        {
                            m = new Mob(25, "Adler", 40, 0, new Item("Fleisch","consumable",10,1,5));
                            Spiel.konsole.setImage("adler");
                        }
                        if(i == 1)
                        { 
                            m = new Mob(75, "Bergtroll", 15, 30, new Item("Monsterfleisch","consumable",15,20,2));
                            Spiel.konsole.setImage("Bergtroll");
                        }
                        if(i == 2)
                        {
                            m = new Mob(100, "Bergriese", 20, 65, new Item("Monsterfleisch","consumable",15,20,5));
                            Spiel.konsole.setImage("Bergriese");
                        }
                        break;
                    case 9:
                        m=null;
                        i = rand.nextInt(3);
                        if(i == 0)
                        {
                            m = new Mob(50, "Wildschwein", 20, 30, new Item("Fleisch","consumable",10,1,5));
                            Spiel.konsole.setImage("Wildschwein");
                        }
                        if(i == 1)
                        {
                            m = new Mob(75, "Bär", 20, 50, new Item("Fleisch","consumable",10,1,10));
                            Spiel.konsole.setImage("Baer");
                        }
                        if(i == 2)
                        {
                            m = new Mob(50, "Waldelf", 40, 20, new Item("Goldbeutel","schrott",0,20,5));
                            Spiel.konsole.setImage("Elf");
                        }
                        break;
                    case 11:
                        m=null;
                        i = rand.nextInt(3);
                        if(i == 0)
                        {
                            m = new Mob(25, "Bienenschwarm", 700, -350, new Item("Honig","schrott",30,10,2));
                            Spiel.konsole.setImage("Bienenschwarm");
                        }
                        if(i == 1)
                        {
                            m = new Mob(50, "Killer-Hase", 800, -200, new Item("Fleisch","consumable",10,1,5));
                            Spiel.konsole.setImage("KillerHase");
                        }
                        if(i == 2)
                        {
                            m = new Mob(25, "Fee", 50, 20, new Item("Feenstaub","schrott",0,200,1));
                            Spiel.konsole.setImage("Fee");
                        }
                        break;
                    case 14:
                        m=null;
                        i = rand.nextInt(3);
                        if(i == 0)
                        {
                            m = new Mob(25, "Zebra", 20, 30, new Item("Fleisch","consumable",10,1,5));
                            Spiel.konsole.setImage("Zebra");
                        }
                        if(i == 1)
                        {
                            m = new Mob(50, "Krokodil", 35, 45, new Item("Fleisch","consumable",10,1,5));
                            Spiel.konsole.setImage("krokodil");
                        }
                        if(i == 2)
                        {
                            m = new Mob(50, "Löwe", 40, 55, new Item("Fell","schrott",0,50,2));
                            Spiel.konsole.setImage("Loewe");
                        }
                        break;
                    default:
                        m=null;
                        i = rand.nextInt(3);
                        m = m = new Mob(5, "das ist falsch", 5, 1, new Item(""));
                        break;
                }
                Kampf.kampf(m);
                changeBackground();
            }
        }
    }
    
    private boolean speichern(String input)
    {
        if(Spiel.konsole.compareInput("Speichern", input) )
        {
            saveWorld();
            Spiel.konsole.printD("System",7);
            return true;
        }
        return false;
    }
    
    private boolean stats(String input)
    {
        if(Spiel.konsole.compareInput("stats", input) )
        {
            Spiel.konsole.println("");
            Spiel.konsole.println("Du hast "+ (int)P.getHP() + " Lebenspunkte." );
            Spiel.konsole.println("Du hast " + P.getGoldMuenzen() + " Goldmünzen." );
            Spiel.konsole.println("");
            if(P.getInventar().getEquipment().getList().size() != 0)
            {
                Spiel.konsole.println("Das ist in deinem Equipment:");
                Spiel.konsole.printList(P.getInventar().getEquipment().getList() );
            }else
            {
                Spiel.konsole.println("Du hast nichts ausgerüstet.");
            }
            Spiel.konsole.println("");
            if(P.getInventar().getInventar().getList().size() != 0)
            {
                Spiel.konsole.println("Das ist in deinem Inventar:");
                Spiel.konsole.printList(P.getInventar().getInventar().getList() );
            }else
            {
                Spiel.konsole.println("Du hast nichts in dienem Inventar.");
            }
            return true;
        }
        return false;
    }
    
    private boolean use(String input)
    {
        if(Spiel.konsole.compareInput("Benutzen", input) )
        {
            return true;
        }
        return false;
    }
    
    private void specialEventUse()
    {
        if(P.getInventar().getInventar().getList().size() == 0)
        {
            Spiel.konsole.println("Dein Inventar ist leer. Du hast nichts, was du benutzen kannst.");
            return;
        }
        while(true)
        {
            Spiel.konsole.println("Welches Item willst du benutzen?");
            String input = Spiel.konsole.getInput();
            if(Spiel.konsole.compareInput("Nichts", input) )
            {
                Spiel.konsole.println("Ok du willst also keines benutzen.");
                return;
            }else if(stats(input))
            {
                Spiel.konsole.println("Also nochmal.");
            }else if(P.getInventar().getInventar().isInList(new Item(input)) )
            {
                Item using = P.getInventar().getInventar().getListItem(new Item(input));
                if(using.getType().equals("mission") )
                {
                    Spiel.konsole.println("Das Item brauchst du noch für später.");
                    return;
                }
                if(using.getType().equals("schrott") )
                {
                    Spiel.konsole.println("Das Item solltest du lieber Verkaufen.");
                    return;
                }
                if(using.getType().equals("consumable") )
                {
                    P.changeHP(using.getPower() );
                }
                P.getInventar().useItem(new Item(input) );
                Spiel.konsole.println("Item erfolgreich benutzt.");
                return;
            }else
            {
                Spiel.konsole.println("Sorry ich verstehe nicht was du meinst.");
            }
        }
    }
    
    private void mission()
    {
        if(!P.hasMission)
        {
            Spiel.konsole.println("Willst du eine neue Mission annehmen?");
            while(true)
            {
                String input = Spiel.konsole.getInput();
                if(Spiel.konsole.compareInput("Ja", input))
                {
                    P.missionNumber += 1;
                    P.hasMission = true;
                    for(int i = -4; i < 0; i++)
                    {
                        Spiel.konsole.printD("Mission", P.missionNumber * 100 + i);
                    }
                    Spiel.konsole.printDMission("DialogM", "Mission", P.missionNumber);
                    P.currentMission = new Mission(P.missionNumber);
                    break;
                }else if(Spiel.konsole.compareInput("Nein", input))
                {
                    Spiel.konsole.println("ok dann nicht.");
                    break;
                }
            }
        }
        else if(P.hasMission && P.getCoord() == 1014)
        {
            Spiel.konsole.println("Du kannst gerade keine neue Mission annehmen.");
            Spiel.konsole.printDVoranSchreiben("Mission", "Beende zuerst laufende Missionen: ", P.missionNumber);
        }
    }
    
    private boolean missionAccomplished(int missionNumber)
    {
        if(P.currentMission != null)
        {
            if(P.currentMission.type == 1 && P.currentMission.coords == P.getCoord())
                P.currentMission.done = true;
            else if(P.currentMission.type == 2 && P.getInventar().getInventar().isInList(P.currentMission.item))
                P.currentMission.done = true;
            else if(P.currentMission.type == 3 && P.getInventar().getEquipment().isInList(P.currentMission.item))
                P.currentMission.done = true;
            if(P.currentMission.number == 35 && P.getBigCoord()/100 == 2)
                P.currentMission.done = true;
            if(P.currentMission.done)
            {
                switch(P.currentMission.number)
                {
                    case 16:
                        P.getInventar().getInventar().remove(new Item("Obsidian-Fragmente"));
                        break;
                    case 23:
                        P.getInventar().getInventar().remove(new Item("Drachenschuppe"));
                        P.getInventar().getInventar().remove(new Item("Glasklumpen"));
                        P.getInventar().getInventar().remove(new Item("Pflanze des Lebens"));
                        P.getInventar().getInventar().remove(new Item("Silberne Pilze"));
                        break;
                    case 2:
                        P.getInventar().getInventar().remove(new Item("Sandwurm-Perle"));
                        break;
                    case 34:
                        P.getInventar().getInventar().remove(new Item("Schwarzpulver-Sack"));
                        break;
                    case 42:
                        P.getInventar().getInventar().remove(new Item("Energiequelle"));
                        P.getInventar().getInventar().remove(new Item("Hitzeschild"));
                        P.getInventar().getInventar().remove(new Item("Anti-Gravitations-Antrieb"));
                        P.getInventar().getInventar().remove(new Item("Koordinatensysteme"));
                        break;
                    default:
                        break;
                }
            }
            
            if(P.currentMission.done && P.currentMission.number == 42)
            {   
                outro();
                return false;
            }
            
            if(P.currentMission.done)
            {
                Spiel.konsole.println("");
                int p = 0;
                if(P.currentMission.type == 5 || P.currentMission.type == 8)
                    p = 2;
                for(int i = p; i < 8; i++)
                    Spiel.konsole.printD("Mission", P.missionNumber * 100 + i);
                Spiel.konsole.println("");
                Spiel.konsole.println("Du hast die Mission erfüllt!");
                P.changeGoldMuenzen(P.currentMission.gold);
                if(!P.currentMission.receiveItem.equals(new Item("")))
                {
                    P.getInventar().addItem(P.currentMission.receiveItem);
                    Spiel.konsole.println("Du erhälst folgendes Item: " + P.currentMission.receiveItem.getName());
                    P.currentMission.receiveItem = null;
                }
                if(P.currentMission.gold != 0)
                    Spiel.konsole.println("Du erhälst " + P.currentMission.gold + " Goldmünzen!");
                if(P.currentMission.autoNext)
                {
                    P.missionNumber += 1;
                    Spiel.konsole.printDVoranSchreiben("Mission", "Dies ist deine neue Mission: ", P.missionNumber);
                    P.currentMission = new Mission(P.missionNumber);
                    return false;
                }
                else
                {
                    P.currentMission = null;
                    P.hasMission = false;
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    private boolean look(String input)
    {
        if(Spiel.konsole.compareInput("umschauen", input))
        {
            for(int p = 1; p <= 4; p++)
            {
                Spiel.konsole.printD(P.getCoord() + "", p);
                waitX(200);
            }
            return true;
        }
        return false;
    }
    
    private boolean interact(String input)
    {
        if(Spiel.konsole.compareInput("Aktion", input))
        {
            return true;
        }
        return false;
    }
    
    private void specialEventInteract()
    {
        if(P.getCoord() == 1006)
        {
            while(true)
            {
                String coord = P.getCoord() + "";
                Spiel.konsole.printD(coord, 5);
                String  answer = Spiel.konsole.getInput();
                if(Spiel.konsole.compareInput("Ja", answer))
                {
                    shop(schmiedI);
                    return;
                }else if(Spiel.konsole.compareInput("Nein", answer))
                {
                    Spiel.konsole.printD(coord, 7);
                    return;
                }else
                {   
                    Spiel.konsole.printD(coord, 6);
                }
            }
        }
        if(P.getCoord() == 1011)
        {
            //frage ob willst du was verkaufen oder kaufen
            while(true)
            {
                Spiel.konsole.println("Ladenbesitzer: Willst du was Kaufen oder Verkaufen?");
                String answer = Spiel.konsole.getInput();
                if(Spiel.konsole.compareInput("Verkaufen", answer))
                {
                    sell();
                    return;
                }
                else if(Spiel.konsole.compareInput("Kaufen", answer))
                {
                    shop(ladenI);
                    return;
                }
                else if(Spiel.konsole.compareInput("Nichts", answer))
                {
                    Spiel.konsole.printD(P.getCoord() + "",15);
                    return;
                }
                else
                    Spiel.konsole.printD(P.getCoord() + "",14);
            }
        }
        if(P.getCoord() == 1014)
        {    
            mission();
            return;
        }
        if(P.currentMission == null)
        {
            Spiel.konsole.println("Hier ist nichts zum Interagieren!");
            return;
        }
        if(P.currentMission.type == 6 && P.currentMission.coords == P.getCoord())
        {
            if(!P.getInventar().getEquipment().isInList(P.currentMission.item))
            {
                Spiel.konsole.println("Um mit diesem Feld interagieren zu können benötigst du dieses Item: " + P.currentMission.item.getName());
                return;
            }
            P.getInventar().getEquipment().remove(P.currentMission.item);
            P.getInventar().getInventar().remove(P.currentMission.item);
            P.currentMission.done = true;
            return;
        }
        if(P.currentMission.number == 32 && P.currentMission.coords == 1507)
        {
            Spiel.konsole.println("");
            Spiel.konsole.printD("Mission", 3200);
            Spiel.konsole.printD("Mission", 3201);
            return;
        }
        if(P.currentMission.type == 9 && P.currentMission.coords == P.getCoord())
        {
            if(P.getInventar().getEquipment().isInList(P.currentMission.item))
                P.currentMission.done = true;
            else
                Spiel.konsole.println("Um mit diesem Feld interagieren zu können benötigst du dieses Item: " + P.currentMission.item.getName());
            return;
        }
        Spiel.konsole.println("Hier ist nichts zum Interagieren!");
    }
    
    private void shop(ItemList shop)
    {
        String coord = P.getCoord() + "";
        Spiel.konsole.println("Du hast so viele Goldmünzen: " + P.getMoney());
        Spiel.konsole.printD(coord, 11);
        Spiel.konsole.printListShop(shop.getList());
        while(true)
        {
            Spiel.konsole.printD(coord, 12);
            String answer = Spiel.konsole.getInput();
            if(Spiel.konsole.compareInput("Nichts", answer))
            {
                Spiel.konsole.printD(coord, 7);
                return;
            }else if(shop.isInList(new Item(answer)) ) //überprüft ob das eingegeben Item zunm verkauf steht
            {
                Item buy = shop.getListItem(new Item(answer));
                if(P.getMoney() >= buy.getWert() ) //überprüft ob man genug Geld hat
                {
                    Spiel.konsole.printD(coord, 10);
                    P.changeGoldMuenzen(-1 * buy.getWert() );
                    P.getInventar().addItem(new Item(buy.getName(), buy.getType(), buy.getPower(), buy.getWert()/2) );
                    return;
                }
                Spiel.konsole.printD(coord, 9);
                Spiel.konsole.printD(coord, 7);
                return;
            }else{
                Spiel.konsole.printD(coord, 6);
            }
        }
    }
    
    private void sell()
    {
        while(true)
        {
            String coord = P.getCoord() + "";
            if(P.getInventar().getInventar().getList().size() == 0)
            {
                Spiel.konsole.println("Ladenbesitzer: Du hast nichts in deinem Inventar.");
                Spiel.konsole.printD(coord, 15);
                return;
            }
            Spiel.konsole.println("Das ist in deinem Inventar:");
            Spiel.konsole.printListM(P.getInventar().getInventar().getList() );
            while(true)
            {
                Spiel.konsole.printD(coord, 16);
                String answer = Spiel.konsole.getInput();
                if(Spiel.konsole.compareInput("Nichts", answer))
                {
                    Spiel.konsole.printD(coord, 15);
                    return;
                }else if(P.getInventar().getInventar().isInList(new Item(answer)) ) //überprüft ob Spieler eingegeben Item hat
                {
                    Item sell = P.getInventar().getInventar().getListItem(new Item(answer));
                    if(sell.getType().equals("tool")||sell.getType().equals("mission"))
                    {
                        Spiel.konsole.println("Ladenbesitze: Ich empfehle dir das nicht zu verkaufen.");
                        return;
                    }
                    if(P.currentMission.item == null)
                    {
                    }
                    else if(sell.equals(P.currentMission.item) && P.currentMission.sellIt) //Missionstyp 4
                    {
                       P.currentMission.done = true;
                    }
                    if(sell.getAmount() > 1)
                    {
                        while(true) 
                        {
                            Spiel.konsole.println("Ladenbesitzer: Willst du all dein(e)" + sell.getName() + " verkaufen?" );
                            String input = Spiel.konsole.getInput();
                            if(Spiel.konsole.compareInput("Ja", input) )
                            {
                                P.changeGoldMuenzen(sell.getWert() * sell.getAmount());
                                P.getInventar().removeItem(new Item(answer));
                                Spiel.konsole.printD(coord, 17);
                                return;
                            }else if(Spiel.konsole.compareInput("Nein", input) )
                            {
                                Spiel.konsole.println("Ladenbesitzer: Ok, also nur das eine.");
                                break;
                            }else
                                Spiel.konsole.printD(coord, 14);
                        }
                    }
                    P.changeGoldMuenzen(sell.getWert());
                    P.getInventar().sellItem(new Item(answer));
                    Spiel.konsole.printD(coord, 17);
                    return;
                }else{
                    Spiel.konsole.printD(coord, 14);
                }
            }
        }
    }
    
    private void loadWorld()
    {
        Object o[] = FileSaving.load(worldName, ObjekteZuSpeichern);
        Ma = (Map)o[0];
        P = (Player)o[1];
    }
    
    private void saveWorld()
    {
        Object o[] =  new Object[ObjekteZuSpeichern];
        o[0] = Ma;
        o[1] = P;
        FileSaving.save(worldName,o);
    }

    
    private boolean isOnlyLetter(String name) {
        char[] chars = name.toCharArray();
        for(char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
    
    public void changeBackground()
    {
        int r = randPic;
        while(randPic == r)
            randPic = rand.nextInt(5) + 1;
        switch(P.getBigCoord()/100){
            case 2:
                //name plus random zahl von 1-5
                Spiel.konsole.setImage("arktis");// + new Random().nextInt(5)+1);
                Spiel.konsole.setBackground(59,96,140,255);
                break;
            case 5:
                Spiel.konsole.setImage("schleim");
                Spiel.konsole.setBackground(71,48,48,255);
                break;
            case 6:
                Spiel.konsole.setImage("berge" + randPic );
                Spiel.konsole.setBackground(15,48,49,255);
                break;
            case 7:
                Spiel.konsole.setImage("vulkan");
                Spiel.konsole.setBackground(68,17,17,255);
                break;
            case 9:
                Spiel.konsole.setImage("wald" + randPic );
                Spiel.konsole.setBackground(39,76,45,255);
                break;
            case 11:
                Spiel.konsole.setImage("wiese" + randPic );
                Spiel.konsole.setBackground(152,157,94,255);
                break;
            case 13:
                Spiel.konsole.setImage("urwald");
                Spiel.konsole.setBackground(243,214,216,255);
                break;
            case 14:
                Spiel.konsole.setImage("savanne" + randPic ); //+ new Random().nextInt(5)+1);
                Spiel.konsole.setBackground(217,169,120,255);
                break;
            case 15:
                Spiel.konsole.setImage("wüste");
                Spiel.konsole.setBackground(253,219,164,255);
                break;
        }
        switch(P.getCoord()){
            case 1005:
                Spiel.konsole.setImage("weizenfeld");
                Spiel.konsole.setBackground(217,166,112,255);
                break;
            case 1006:
                Spiel.konsole.setImage("schmied");
                Spiel.konsole.setBackground(64,49,40,255);
                break;
            case 1007:
                Spiel.konsole.setImage("friedhof");
                Spiel.konsole.setBackground(7,18,37,255);
                break;
            case 1009:
                Spiel.konsole.setImage("taverne");
                Spiel.konsole.setBackground(83,49,34,255);
                break;
            case 1010:
                Spiel.konsole.setImage("markt");
                Spiel.konsole.setBackground(228,187,115,255);
                break;
            case 1011:
                Spiel.konsole.setImage("laden");
                Spiel.konsole.setBackground(164,110,84,255);
                break;
            case 1013:
                Spiel.konsole.setImage("kirche");
                Spiel.konsole.setBackground(34,73,81,255);
                break;
            case 1014:
                Spiel.konsole.setImage("rathaus");
                Spiel.konsole.setBackground(117,119,125,255);
                break;
            case 1015:
                Spiel.konsole.setImage("blumenfeld");
                Spiel.konsole.setBackground(173,111,97,255);
                break;
        }
    }
    
    private void printPos()
    {
        int r = descriptionRand;
        while(descriptionRand == r)
            descriptionRand = rand.nextInt(10) + 1;
        changeBackground();
        if(P.getBigVorCoord() != P.getBigCoord())
        {
            String t = P.getBigCoord() + "";
            Spiel.konsole.println("");
            Spiel.konsole.printD(t, 0);
        }
        Spiel.konsole.printD(P.getCoord() + "", 0);
        Spiel.konsole.printD(P.getBigCoord() + "", descriptionRand);
    }
    
    private void outro()
    {
        Spiel.konsole.clear();
        Spiel.konsole.setImage("ende");
        Spiel.konsole.setBackground(41,56,74,255);
        for(int i = 0; i < 12; i++)
        {
            Spiel.konsole.printD("Outro", i);
        }
        waitX(5000);
        exit = true;
    }
    
    private void waitX(int x)
    {
        try
        {
            Thread.sleep(x);
        }
        catch(Exception ex)
        {
            System.out.println("Fehler bei waitX-Methode");
        }
    }
}