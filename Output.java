import java.util.Random;
import javax.swing.JTextArea;
public class Output extends Thread
{
    static Random rand = new Random();
    static int baseSpeed = KONSTANTEN.baseSpeed;
    static int maxSpeed = KONSTANTEN.maxSpeed;
    static int randSpeed = KONSTANTEN.randSpeed;
    static int counter = 0;
    public static void println(String s, JTextArea TA)
    {
        for (int i = 0 ;i < s.length() ; i++)
        {
            int n = rand.nextInt(randSpeed) + baseSpeed;
            
            if(i >= 5 && baseSpeed >= maxSpeed)
            {
                baseSpeed -= 2;
                counter = 0;
            }
            else
            {    
                counter += 1;
            }
            
            TA.setText(TA.getText() + s.charAt(i));
            if(KONSTANTEN.testMode)
            {
                n = 0;
            }
            try
            {
                Thread.sleep(n);
            }
            catch(Exception ex)
            {
                System.out.println("Fehler");
            }
        }
        TA.setText(TA.getText() + "\n");
    }
}
