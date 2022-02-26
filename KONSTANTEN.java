import java.awt.*;
import java.io.File;
public class KONSTANTEN
{
    static boolean testMode = false;
    
    static int baseSpeed = 35;
    static int maxSpeed = 40;
    static int randSpeed = 45;

    static int startCoord = 1010;
    static double startHp = 100;
    static int startGold = 100;
    
    static Inventar startInventar()
    {
        Inventar i = new Inventar();
        i.addItem(new Item("normale Kleidung","armour",10,1) );
        i.addItem(new Item("Stock","meele",15,1) );
        i.addItem(new Item("alter Brotlaib","consumable",5,1) );
        i.useItem(new Item("Stock") );
        i.useItem(new Item("normale Kleidung") );
        return i;
    }
    
    static ItemList schmiedI()
    {
        ItemList schmiedI = new ItemList();
        schmiedI.add(new Item("Holzschwert","meele",30,100));
        schmiedI.add(new Item("Lederrüstung","armour",20,100));  
        schmiedI.add(new Item("Eisenschwert","meele",40,250));
        schmiedI.add(new Item("Jagdbogen","range",40,300));
        schmiedI.add(new Item("Eisenrüstung","armour",60,250));
        schmiedI.add(new Item("Langschwert","meele",60,500));
        schmiedI.add(new Item("Kompositbogen","range",60,600));
        schmiedI.add(new Item("Schaufel","tool",5,50));
        return schmiedI;
    }
    static ItemList ladenI()
    {
        ItemList ladenI = new ItemList();
        ladenI.add(new Item("kleiner Heiltrank","consumable",30,50));
        ladenI.add(new Item("großer Heiltrank","consumable",60,100));
        ladenI.add(new Item("Trank der Schwerelosigkeit","tool",50,100));
        ladenI.add(new Item("Handschuhe","armour",25,75));
        ladenI.add(new Item("Excalibur","meele",110,1150));
        ladenI.add(new Item("Caiperubogen", "range",110,1150));
        return ladenI;
    }
}
