import java.io.Serializable;
public class Mission implements Serializable
{
    int type;
    int coords;
    public Item item = new Item("");
    public Item receiveItem = new Item("");
    public boolean done;
    public int number;
    int gold;
    public boolean useIt;
    public boolean sellIt;
    public boolean autoNext;
    public String input;
    public Mob mob;
    
    public Mission(int n)
    {
        done = false;
        useIt = false;
        autoNext = true;
        number = n;
        
        switch(number){
            case 1: 
                acquireItem();
                gold = 10;
                autoNext = false;
                break;
            case 2: 
                useItem();
                gold = 15;
                autoNext = false;
                break;
            case 3: 
                sellItem();
                gold = 40;
                autoNext = false;
                break;
            case 4: 
                findPlace(1009);
                gold = 20;
                break;
            case 5:
                findPlace(1311);
                gold = 20;
                break;
            case 6:
                findPlaceUseItem(514); //Schatzkiste finden, Schaufel wird benötigt
                gold = 100;
                break;
            case 7:
                winFight(1009); //In Taverne gehen, gegen Mann kämpfen
                gold = 50;
                break;
            case 8:
                findPlace(1013); //Kirche betreten
                break;
            case 9:
                findPlace(1509); //Häuser in Wüste finden
                break;
            case 10:
                winFight(1511); //Grabmal finden + Monster bekämpfen
                receiveItem = new Item("Schlüssel","tool",5,10);
                break;
            case 11:
                findPlaceUseItem(1510); //Pyramide mit Schlüssel finden
                autoNext = false;
                break;
            case 12: 
                findPlace(1309); //Panda finden
                break;
            case 13:
                winFight(1315); //Tiger finden + bekämpfen
                autoNext = false;
                break;
            case 14:
                findPlace(1006); //Zum Schmied gehen + Spitzhacke erhalten
                receiveItem = new Item("Stahlspitzhacke","tool",0,0);
                break;
            case 15:
                findPlaceUseItem(711); //Obsidianhügel finden + Spitzhacke benutzen
                receiveItem = new Item("Obsidian-Fragmente","mission",0,0); 
                break;
            case 16:
                findPlace(1006); //Fragmente zu Schmied bringen + Schwert erhalten
                receiveItem = new Item("Obsidian-Schwert","melee",20,100);
                autoNext = false;
                break;
            case 17:
                findPlace(1011); //Laden betreten
                break;
            case 18:
                findPlace(505); //Hexenhüte betreten
                break;
            case 19:
                winFight(707); //Gegen Drachen kämpfen + Drachenschuppe erhalten
                receiveItem = new Item("Drachenschuppe","mission",0,0);
                break;
            case 20:
                pickUpItem(713); //Glas aufheben
                receiveItem = new Item("Glasklumpen","mission",0,0);
                break;
            case 21:
                pickUpItem(715); //Pflanze aufheben
                receiveItem = new Item("Pflanze des Lebens","mission",0,0);
                break;
            case 22:
                pickUpItem(506); //Pilze aufheben
                receiveItem = new Item("Silberne Pilze","mission",0,0);
                break;
            case 23:
                findPlace(505); //Hexe aufsuchen und Zaubertrank erhalten
                receiveItem = new Item("Zaubertrank der Heilung","consumable",50,200); //Items müssen natürlich aus Inventar entfernt werden
                break;
            case 24:
                findPlace(515); //Verlassen Hütte finden
                break;
            case 25:
                findPlace(1505); //Oase finden
                break;
            case 26:
                winFight(1506); //Sandwurm bekämpfen
                receiveItem = new Item("Sandwurm-Perle","mission",0,0);
                break;
            case 27:
                findPlace(1006); //Schmied aufsuchen + Rüstung erhalten
                receiveItem = new Item("Goldenes Kettenhemd","armour",50,200);
                autoNext = false;
                break;
            case 28:
                pickUpItem(509); //Leiche finden und aufheben
                receiveItem = new Item("Leiche","tool",0,0);
                break;
            case 29:
                winFight(510); //Seemonster besiegen
                break;
            case 30:
                winFight(511); //Schleimmonster bekämpfen
                break;
            case 31:
                findPlaceUseItem(1007); //Friedhof finden und Leiche begraben
                autoNext = false;
                break;
            case 32:
                requiresInput(1507); //Sphinx finden und Rätsel lösen
                input = "Das Herz";
                break;
            case 33:
                winFight(1310); //Aztekentempel finden und Leute dort bekämpfen
                receiveItem = new Item("Schwarzpulver-Sack","tool",0,0);
                break;
            case 34:
                findPlaceUseItem(710); //Vulkan finden und zum Explodieren bringen
                break;
            case 35:
                findPlace(2); //Arktis betreten
                break; //Sonderfall, nur das Arktis-Biom an sich ist wichtig
            case 36:
                findPlace(206); //Raumschiffreste finden
                break;
            case 37:
                pickUpItemWithItem(513); //Leuchtende Kugel im Schleimsumpf aufheben, Schaufel benötigt
                receiveItem = new Item("Energiequelle");
                break;
            case 38:
                pickUpItemWithItem(714); //Schwere Metallplatte im Vulkanbiom aufheben, Trank der Stärke benötigt
                receiveItem = new Item("Hitzeschild");
                break;
            case 39:
                pickUpItemWithItem(1305); //Metallklumpen im Urwald aufheben, Goldenes Kettenhemd zum Schutz benötigt
                receiveItem = new Item("Anti-Gravitations-Antrieb");
                break;
            case 40:
                pickUpItemWithItem(1515); //Beschriebene Bruchstücke aufheben, Schutzhandschuhe benötigt
                receiveItem = new Item("Koordinatensysteme");
                break;
            case 41:
                winFight(210); //Monster in Arktis besiegen
                break; //Sonderfall, nur das Arktisbiom an sich ist wichtig
            case 42:
                findPlace(206); //Raumschiff wieder betreten
                break;
            default:
                break;
        }
        
    }

    private void findPlace(int c) //Mission: Bestimmtes Feld finden
    {
        type = 1;
        coords = c;
    }
    
    private void acquireItem() //Mission: Item kaufen oder bekommen
    {
        type = 2;
        if(number == 1)
            item = new Item("Holzschwert");
    }
    
    private void useItem() //Mission: Benutze Item
    {
        type = 3;
        useIt = true;
        if(number == 2)
            item = new Item("Holzschwert");
    }
    
    private void sellItem() //Mission: Verkaufe Item
    {
        type = 4;
        sellIt = true;
        if(number == 3)
            item = new Item("Stock");
    }
    
    private void winFight(int c) //Mission: Kampf gewinnen
    {
        type = 5;
        coords = c;
        switch(number)
        {
            case 7:
                mob = new Mob(50,"Mann",15,35,new Item("Goldbeutel","schrott",0,30));
                break;
            case 10:
                mob = new Mob(75,"Mumie",50,50,new Item("Knochen","schrott",0,5,15));
                break;
            case 13:
                mob = new Mob(75,"Tiger",60,65,new Item("Fell","schrott",0,75));
                break;
            case 19:
                mob = new Mob(175,"Drache",55,65,new Item("Drachenatem","range",80,50));
                break;
            case 26:
                mob = new Mob(150,"Sandwurm",40,85,new Item("Sandwurmzahn","schrott",15,5));
                break;
            case 29:
                mob = new Mob(150,"Seemonster",65,40,new Item("Tentakel","schrott",10,8));
                break;
            case 30:
                mob = new Mob(100,"Schleimmonster",25,90,new Item("Schleim","armour",80,10));
                break;
            case 33:
                mob = new Mob(75,"Azteken",80,35,new Item("Goldketten","schrott",50,2));
                break;
            case 41:
                mob = new Mob(200,"Yeti",75,90,new Item("Yeti-Fell","armour",90,200));
                break;
        }
    }
    
    private void findPlaceUseItem(int c) //Mission: Bestimmtes Feld finden und dort bestimmten Gegenstand benutzen
    {
        type = 6;
        coords = c;
        switch(number)
        {
            case 6:
                item = new Item("Schaufel");
                break;
            case 11:
                item = new Item("Schlüssel");
                break;
            case 15:
                item = new Item("Stahlspitzhacke");
                break;
            case 31:
                item = new Item("Leiche");
                break;
            case 41:
                item = new Item("Schwarzpulver-Sack");
                break;
        }
    }
    
    private void pickUpItem(int c) //Mission: Bestimmtes Feld finden und den Befehl "Aufheben" dort benutzen 
    {
        type = 7;
        coords = c;
    }
    
    private void requiresInput(int c)
    {
        type = 8;
        coords = c;
    }

    private void pickUpItemWithItem(int c)
    {
        type = 9;
        coords = c;
        switch(number)
        {
            case 37:
                item = new Item("Schaufel");
                break;
            case 38:
                item = new Item("Trank der Schwerelosigkeit");
                break;
            case 39: 
                item = new Item("Goldenes Kettenhemd");
                break;
            case 40:
                item = new Item("Handschuhe");
        }
    }
    
    public Item getItem()
    {
        return item;
    }
}
