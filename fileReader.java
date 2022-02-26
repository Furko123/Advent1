import java.io.*;

public class fileReader
{
    public fileReader()
    {
    }
  
    public static String readLine(String section, String number, int fileSize)
    {     
        int i = fileSize;
        String line = new String();
        String tempLine = new String();
        String sectionNumber = section + ":" + number + " ";
        
        try 
        {
            FileReader file = new FileReader("output.txt");
            BufferedReader buffer = new BufferedReader(file);

            for (int j = 1; j < i; j++) 
            { 
                tempLine = buffer.readLine();
                
                if(tempLine != null)
                {
                    if(tempLine.contains(sectionNumber))
                    {    
                        line = tempLine;
                        line = line.replace(sectionNumber,"");
                        line = line.stripLeading();
                        line = line.stripTrailing();
                        break;
                    }
                }
            }   
        }
        catch(IOException e) 
        {
            e.printStackTrace();
        }
        
        return line;
    }
    
    public static boolean containsString(String section, String s, int fileSize)
    {
        int i = fileSize;
        String tempLine = new String();
        String line = new String();
        
        try 
        {
            FileReader file = new FileReader("input.txt");
            BufferedReader buffer = new BufferedReader(file);
            section = section.toLowerCase();
            
            for (int j = 1; j < i; j++) 
            { 
                tempLine = buffer.readLine();
                if(tempLine != null)
                {
                    line = tempLine.toLowerCase();
                    if(line.contains(section))
                    {    
                        line = line.replace(section,"");
                        line = line.stripLeading();
                        line = line.stripTrailing();
                        line = line.toLowerCase();
                        if(line.equals(s.toLowerCase()))  
                            return true;
                    }
                }
            }   
        }
        catch(IOException e) 
        {
          e.printStackTrace();
        }
        return false;
    }
}


