import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemList implements Serializable
{
    private List<Item> itemList;
    
    public ItemList()
    {
        itemList = new ArrayList<Item>();
    }
    
    public void add(Item i)
    {
        itemList.add(i);
    }
    
    public void remove(Item i)
    {
        itemList.remove(i);
    }
    
    public boolean isInList(Item i) 
    {
        for(Item e : itemList)
        {
            if(i.equals(e))
            {
                return true;
            }
        }
        return false;
    }
    
    public Item getListItem(Item i)
    {
        for(Item e : itemList)
        {
            if(i.equals(e))
            {
                return e; 
            }
        }
        return null;
    }
    
    public List getList()
    {
        return itemList;
    }
}
