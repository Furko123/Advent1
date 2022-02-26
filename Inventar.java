import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventar implements Serializable
{
    private ItemList inventar = new ItemList();
    private ItemList equipment = new ItemList();
    
    public Inventar()
    { 
    }
    
    public void addItem(Item i) //muss Item mit ganzen Werten sein 
    {
        if(inventar.isInList(i))
        {
            inventar.getListItem(i).changeAmount(1);
            return;
        }
        inventar.add(i);
    }
    
    public void useItem(Item i) //Item mit Name reicht
    {
        if(!inventar.isInList(i))
        {
            return;
        }
        Item using = inventar.getListItem(i);
        if(using.getType().equals("mission"))
        {
            return;
        }
        if(GameManager.P.currentMission != null)
        {
            if(using.equals(GameManager.P.currentMission.item))
            {
                if(GameManager.P.currentMission.type == 3)
                    GameManager.P.currentMission.done = true; 
            }
        }
        if(using.getType().equals("meele") || using.getType().equals("range") || using.getType().equals("armour") || using.getType().equals("tool"))
        {
            Item IforE = new Item(using.getName(), using. getType(), using.getPower(), using.getWert());
            List<Item> eqiup = equipment.getList();
            for(Item e : eqiup)
            {
                if(IforE.getType().equals("meele")||IforE.getType().equals("range"))
                {
                    if(e.getType().equals("range")||e.getType().equals("meele"))
                    {
                        addItem(e);
                        equipment.remove(e);
                        equipment.add(IforE);                    
                        InvUseItem(using);
                        return;
                    }
                }else if(e.getType().equals(IforE.getType()) )
                {
                    //Item vom eqipment zum Inventar adden 
                    addItem(e);
                    equipment.remove(e);
                    equipment.add(IforE);                    
                    InvUseItem(using);
                    return;
                }
            }
            //wenn noch kein Item des Typs in Equipment, dann..
            equipment.add(IforE);
            InvUseItem(using);
            return;
        }
        //wenn Item Typ consumable hat
        InvUseItem(using);
    }
    
    public void sellItem(Item i) //Item nur mit Namen reicht
    {
        if(!inventar.isInList(i))
        {
            return;
        }
        InvUseItem(inventar.getListItem(i));
    }
    
    public void removeItem(Item i) //Item nur mit Namen reicht
    {
        if(!inventar.isInList(i))
        {
            return;
        }
        inventar.remove(inventar.getListItem(i));
    }
    
    public ItemList getInventar()
    {
        return inventar;
    }
    
    public ItemList getEquipment()
    {
        return equipment;
    }
    
    public Item getEquipItem(String type)
    {
        List<Item> equip = equipment.getList();
        for(Item e : equip)
        {
            if(type.equals("weapon"))
            {
                if(e.getType().equals("meele")||e.getType().equals("range"))
                {
                    return e;
                }
            }
            if(type.equals(e.getType()))
            {
                return e;
            }
        }
        return new Item("");
    }
    
    private void InvUseItem(Item i)
    {
        i.changeAmount(-1);
        if(i.getAmount() == 0)
        {
            inventar.remove(i);
        }
    }
}
