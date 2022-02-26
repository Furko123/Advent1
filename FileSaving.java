import java.io.*;

public class FileSaving
{
    public static void save(String DateiName, Object[] objects) 
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream
        ((new File("Spielstaende").getAbsolutePath()) + "/" + (DateiName+".bin") ))) 
        {
            for(Object e : objects)
                out.writeObject(e);
        } catch (Exception e) { 
            System.out.println("Serialization failed");
            System.out.println(e);
        }
    }
    public static Object[] load(String DateiName, int laenge) {
        Object[] o = new Object[laenge];
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream
        ((new File("Spielstaende").getAbsolutePath()) + "/" + (DateiName+".bin") )))
        {
            for (int i = 0; i < o.length; i++) 
            {
                o[i] = (Object) in.readObject();
            }
            Spiel.konsole.printD("System",8);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Deserialization failed");
            System.out.println();
        }
        return o;
    }
    public static boolean WorldsaveExists(String DateiName)
    {
        File f = new File(new File("Spielstaende").getAbsolutePath() + "/" + (DateiName+".bin") );
        if(f.exists() && !f.isDirectory() )
        {
            return true;
        }
        return false;
    }
    public static boolean saveIsEmpty()
    {
        if(0 >= new File("Spielstaende").listFiles().length) 
        {
            return true;
        }
        return false;
    }
}
