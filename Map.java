import java.util.HashMap;
import java.io.Serializable;
public class Map implements Serializable
{
    private HashMap<Integer, Integer> fl = new HashMap<Integer, Integer>();
    
    public Map()
    {
        createMaps();
    }
    
    private void createMaps()
    {
        for(int i = 0; i <=15; i++)
        {
            if(!(i % 4 == 0 || i == 1 || i == 3))
            {
                fl.put(i, i);
            }
        }
    }
}
