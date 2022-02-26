import java.util.*;
import java.lang.Math;
import java.io.Serializable;
public class Mob implements Serializable
{
    private int weaponPower;
    private int armourPower;
    private double hp;
    private String name;
    private int mobSP;
    private Item loot;

    // Monster haben eine HP Anzahl, einen Namen, eine Weaponpower, eine Armourpower und Loot
    public Mob(double h, String n, int wp, int ap, Item l)
    {
        hp = h;
        name = n;
        weaponPower = wp;
        armourPower = ap;
        loot = l;
    }
    
    // Die folgenden Methoden sind analog zu den Methoden beim Player
    public double getHP()
    {
        return hp;
    }

    public static int roundNumber(int k)
    {
        int rNumber = (k + 50) / 100 * 100; //round the Number to the hundreds
        return rNumber;
    }

    // Eine Wurzelfunktion, so dass mehr Weaponpower später einen schwächeren Anstieg hat. Mit dem 
    // Der Schaden ist proportional zum DamageBoost
    public void getDamage(int wp, double db)
    {
        double damage = db * Math.sqrt(10*wp - (10*wp * armourPower/100));
        hp -= damage;
    }
    
    public int getWeaponPower()
    {
        return weaponPower;
    }
    
    public int getArmourPower()
    {
        return armourPower;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Item getLoot()
    {
        return loot;
    }
}