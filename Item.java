import java.io.Serializable;

public class Item implements Serializable
{
    //types: mission, consumable, meele, range, armour, tool, schrott
    private String name;
    private String type;
    private int power;
    private int Goldwert;
    private int amount=1;
    
    public Item(String n, String t, int p, int w)
    {
        name = n;
        type = t;
        power = p;
        Goldwert = w;
    }
    
    public Item(String n)
    {
        name=n;
    }
    
    public Item(String n, String t, int p, int w, int a)
    {
        name = n;
        type = t;
        power = p;
        Goldwert = w;
        amount = a;
    }
    
    public void changeAmount(int a)
    {
        amount = amount+a;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getType()
    {
        return type;
    }
    
    public int getPower()
    {
        return power;
    }
    
    public int getAmount()
    {
        return amount;
    }
    
    public int getWert()
    {
        return Goldwert;
    }
    
    public boolean equals(Object obj){
        if(obj instanceof Item){
            Item i = (Item) obj;
            return (name.equals(i.getName()) );
        }
        else{
            return false;
        }
    }
}
