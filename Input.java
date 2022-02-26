import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Input 
{
    static String input = "";
    public static void getInput(JTextField jt, Konsole k)
    {
        jt.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { //wenn Enter getippt dann...
                input = jt.getText();
                jt.removeActionListener(this);
                synchronized (k) {
                    k.notify();
                }
            }
        } );
    }
    
    public static String returnInput()
    {
        return input.trim();
    }
}
