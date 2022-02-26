import java.util.*;
import java.lang.Math;
import java.io.Serializable;

public class Player implements Serializable
{
    private  Inventar inventar;
    Map M;
    private int Coord; 
    private double hp;
    private int vorCoord;
    private int goldMuenzen;
    public boolean hasMission;
    public int missionNumber; //immer eine Zahl kleiner als gewünschte Startmission
    public Mission currentMission;
    
    public Player()
    {
        M = new Map();
        Coord = KONSTANTEN.startCoord;
        hp = KONSTANTEN.startHp;
        missionNumber = 0;
        hasMission = false;
        goldMuenzen = KONSTANTEN.startGold;
    }
    
    public Inventar getInventar()
    {
        return inventar;
    }
    
    public void setInventar(Inventar in)
    {
        inventar = in;
    }

    public int getCoord()
    {
        return Coord;
    }
    
    // Erhöht die Hp des Spielers bis maximal 100 HP
    public void changeHP(int change)
    {
        if(hp+change > 100)
            hp = 100;
        else
            hp = hp + change;
        Spiel.konsole.println("Dieser kulinarischer Genuss belebt deinen Körper. Deine aktuelle Hp beträgt: " + hp);
    }
    
    public int getGoldMuenzen()
    {
        return goldMuenzen;
    }
    
    public void changeGoldMuenzen(int betrag)
    {
        goldMuenzen = goldMuenzen + betrag;
    }
        
    
    // Es gibt 9 Biome. Jedes der Biome sind in sich selbst ein 3x3 oder ein 6x6 Feld. Die 3x3 Biome sind
    // in den Ecken und in der Mitte. Die x-y Koordinate innerhalb eines Bioms werden mit x = 1 und y = 7
    // zusammengerechnet, so dass jedes Feld eine eigene individuelle Zahl hat. Das gleiche macht man für
    // die Koordinaten der Biome und rechnet anschließend dieses mal 100. Beide Koordinaten rechnet man nun
    // zusammen. In Coord stecken also 4 Koordinaten drinnen. 
    public boolean moveRight()
    {
        vorCoord = Coord;
        int i = getSmallCoord();
        // Überprüf ob wir uns in einem großen Biom bewegen
        if(checkGreatMap())
        {
              if(i % 7 == 6)
              {  
                  // Überprüfe ob man sich am Rand der Welt befindet oder eines anderen Biomes.
                  // Da zwei Felder eines 6x6 Biomes an ein Feld eines 3x3 Biomes angrenzen. Wird in einer Art
                  // "if-Statement" durch eine Modulo Rechnung dafür gesorgt, dass man auf das richtige Feld gelangt
                  // von beiden angrenzenden Feldern aus
                  int t = +92 - (((getSmallCoord()-13-(1-(getSmallCoord()%2)*7))/14)*10)-((1-getSmallCoord()%2)*7);
                  if(checkBigEdge(Coord + t))
                  {
                      Spiel.konsole.printD("Grenze", getBigCoord()/100);
                      return false;
                  }
                  else
                  {
                    Coord = Coord + t;
                    return true;
                  }    
              }
              else
              {    
                  Coord = Coord + 1;
                  return true;
              }
        }
        else
        {
            if(i % 4 == 3)
            {    
                // Geht man von einem 3x3 Biom in ein 6x6 Biom über, gelangt man von den zwei 
                // angrenzenden Feldern, auf das Feld mit der kleineren Koordinate
                int t = +101 + (((getSmallCoord()-7)/4)*10);
                if(checkBigEdge(Coord + t))
                {
                    Spiel.konsole.printD("Grenze", getBigCoord()/100);
                    return false;
                }
                else
                {
                    Coord = Coord + t;
                    return true;
                }
            }
            else
            {
                Coord = Coord + 1;
                return true;
            }
        }
    }
    
    // Analog zu moveRight, aber andere Rechnung
    public boolean moveLeft()
    { 
        vorCoord = Coord;
        int i = getSmallCoord();
        if(checkGreatMap())
        {
              if(i % 7 == 1)
              {  
                  int t = - 101 - ((((getSmallCoord()-8)-(7*(getSmallCoord()%2)))/14)*10)-((getSmallCoord()%2)*7);
                  if(checkBigEdge(Coord + t))
                  {
                      Spiel.konsole.printD("Grenze", getBigCoord()/100);
                      return false;
                  }
                  else
                  {
                    Coord = Coord + t;
                    return true;
                  }    
              }
              else
              {    
                  Coord = Coord - 1;
                  return true;
              }
        }
        else
        {
            if(i % 4 == 1)
            {    
                int t = -92 + (((getSmallCoord()-5)/4)*10);
                if(checkBigEdge(Coord + t))
                {
                    Spiel.konsole.printD("Grenze", getBigCoord()/100);
                    return false;
                }
                else
                {
                    Coord = Coord + t;
                    return true;
                }
            }
            else
            {
                Coord = Coord - 1;
                return true;
            }
        }
    }
    
    // Analog zu moveRight, aber andere Rechnung
    public boolean moveUp()
    {
        vorCoord = Coord;
        int i = getSmallCoord();
        if(missionNumber<35)
        {
            if(GameManager.P.getCoord() - GameManager.P.getBigCoord() <= 13 && GameManager.P.getBigCoord() == 600)
            {   
                Spiel.konsole.printD("Grenze", 6);
                return false;
            }
        }
        if(checkGreatMap())
        {
              if(i >= 8 && i <= 13)
              {  
                  int t = -395 + (4-(getSmallCoord()/2)-getSmallCoord()%2);
                  if(checkBigEdge(Coord + t))
                  {
                      Spiel.konsole.printD("Grenze", getBigCoord()/100);
                      return false;
                  }
                  else
                  {
                    Coord = Coord + t;
                    return true;
                  }    
              }
              else
              {    
                  Coord = Coord - 7;
                  return true;
              }
        }
        else
        {
            if(i >=5 && i <= 7)
            {    
                int t = -360 - 7 + getSmallCoord();
                if(checkBigEdge(Coord + t))
                {
                    Spiel.konsole.printD("Grenze", getBigCoord()/100);
                    return false;
                }
                else
                {
                    Coord = Coord + t;
                    return true;
                }
            }
            else
            {
                Coord = Coord - 4;
                return true;
            }
        }
    }
    
    // Analog zu moveRight, aber andere Rechnung
    public boolean moveDown()
    {
        vorCoord = Coord;
        int i = getSmallCoord();
        if(checkGreatMap())
        {
              if(i >= 43 && i <= 48)
              {  
                  int t = +362 + (21-((getSmallCoord()-2)+(getSmallCoord()%2))/2)-(1-(getSmallCoord()%2));
                  if(checkBigEdge(Coord + t))
                  {
                      Spiel.konsole.printD("Grenze", getBigCoord()/100);
                      return false;
                  }
                  else
                  {
                    Coord = Coord + t;
                    return true;
                  }    
              }
              else
              {    
                  Coord = Coord + 7;
                  return true;
              }
        }
        else
        {
            if(i >=13 && i <=15)
            {    
                int t = +395 - 13 + getSmallCoord();
                if(checkBigEdge(Coord + t))
                {
                    Spiel.konsole.printD("Grenze", getBigCoord()/100);
                    return false;
                }
                else
                {
                    Coord = Coord + t;
                    return true;
                }
            }
            else
            {
                Coord = Coord + 4;
                return true;
            }
        }
    }
    
    public int getBigCoord()
    {
        int bigCoord = roundNumber(Coord);
        return bigCoord;
    }
    
    public int getSmallCoord()
    {
        int smallCoord = Coord - getBigCoord();
        return smallCoord;
    }
    
    public int getBigVorCoord()
    {
        int bigVorCoord = roundNumber(vorCoord);
        return bigVorCoord;
    }
    
    // Wenn die allgemeine Biomkoordinate eine der folgenden Zahlen hat, dann gehört sie nicht mehr zur Map
    // Man ist also dann am Rand.
    public boolean checkBigEdge(int k)
    {
        int i = roundNumber(k)/100;
        if(i % 4 == 0 || i >= 17 || i == 1 || i == 3 || i < 0)
            return true;
        else 
            return false;
    }
    
    // Die großen Maps haben jeweils die allgemeine Biomkoordinate 600, 900, 1100, 1400
    public boolean checkGreatMap()
    {
        int i = getBigCoord()/100;
        if(i == 6 || i == 9 || i == 11 || i == 14)
            return true;
        else
            return false;
    }
    
    // Runde Zahlen zu den Hunderten    
    public static int roundNumber(int k)
    {
        int rNumber = (k + 50) / 100 * 100; 
        return rNumber;
    }
    
    public int getMoney()
    {
        return goldMuenzen;
    }
    
    // Eine Wurzelfunktion für den Schaden. Je höher die WeaponPower desto schwächer ist der Anstieg
    public void getDamage(int wp, double dr)
    {
        double damage = Math.sqrt((7*wp - (7*wp * getInventar().getEquipItem("armour").getPower()/100))*(1-dr));
        hp -= damage;
    }
    
    public double getHP()
    {
        return hp;
    }
    // Verlier die Hälfte deiner Münzen und komm zurück zur Mitte nach dem Tod
    public void respawn()
    {
        hp = KONSTANTEN.startHp;
        Coord = 1007;
        Spiel.konsole.setImage("friedhof");
        Spiel.konsole.setBackground(7,18,37,255);
        goldMuenzen = (int)goldMuenzen/2;
        Spiel.konsole.println("Du wachst auf einem einsamen Friedhof auf.");
        Spiel.konsole.printD("Mission", currentMission.number);
    }
}