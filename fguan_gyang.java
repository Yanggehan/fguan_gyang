package fguan_gyang;
import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * G - a robot by (your name here)
 */
public class fguan_gyang extends AdvancedRobot
{
		private double eDist; //distance from the emeny
		private double move; //moving distance
		private double radarMove = 45; //radar moving angle
		private double dFirePower; //fire
       public void run() 
       {
	  setBodyColor(new Color (0, 0, 0));
	  setGunColor(new Color (255, 255, 255));
       	  setRadarColor(new Color (0, 0, 0));
	  setBulletColor(new Color (255, 255, 255));
       	  setScanColor(new Color (0, 0, 0));
	  eDist = 300;
	  while(true){
       //Every single cycle, the movement of random distance
       double period = 4*((int)(eDist/80)); //Cycle; the closer the enemy, the shorter the cycle, the more frequent the move
       //Period begins, then move
       if(getTime()%period == 0){ 
       move = (Math.random()*2-1)*(period*8 - 25);
       setAhead(move + ((move >= 0) ? 25: -25));
       }
    
        //avoid wall
        double heading = getHeadingRadians(); //get the arc direction 
	double x = getX() + move*Math.sin(heading); //The x coordinate that will be reached after moving move
	double y = getY() + move*Math.cos(heading); //The y coordinate that will be reached after moving move
	double dWidth = getBattleFieldWidth(); //length
	double dHeight = getBattleFieldHeight(); //width
        //When (x, y) exceeds the specified range, move in reverse
        if(x < 30 || x > dWidth-30 || y < 30 || y > dHeight-30)
        {
		setBack(move);
	}
		turnRadarLeft(radarMove); //turn radar
	}
	}//end run()

	/**
	 * onScannedRobot: What to do when you see another robot
	 */

        /**
           * @param e
        */		
	public void onScannedRobot(ScannedRobotEvent e)
	{
                //adjust the angle between each other
		double previousEnergy = 100; //The initial state of each other energy is 100
		int movementDirection = 1; //moving direction 
		int gunDirection = 1; //barrel direction
		setTurnRight(e.getBearing()+90-30*movementDirection);
                //If the other side of the energy loss of a certain value, to avoid the action
		double changeInEnergy = previousEnergy - e.getEnergy();
		if (changeInEnergy>0 && changeInEnergy<=3) 
		{
                    //avoid
	            movementDirection = -movementDirection; //exchange the direction from the last one 
	            setAhead((e.getDistance()/4+25)*movementDirection);
                }
                //Point the barrel to the other side's current position
	        gunDirection = -gunDirection;
                setTurnGunRight(99999*gunDirection);
	        fire(1);
                //reset the energy
                previousEnergy = e.getEnergy();
       }
      
       //if angle angle less than 180 we don't need to rotate the angle that much
       public  double rectify (double angle)
       {
          if ( angle < -Math.PI )
              angle += 2*Math.PI;
          if ( angle > Math.PI )
              angle -= 2*Math.PI;
            return angle;
        } 
     
	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e)
	{
       	   double period = 4*((int)(eDist/80));
	   double eBearing = e.getBearingRadians();
	   if(getTime()%period == 0)
       	   {
			move = (Math.random()*2-1)*(period*8 - 25);
			setAhead(move + ((move >= 0) ? 25: -25));
	   }
  	   setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(
	   getHeadingRadians() + eBearing - getGunHeadingRadians()));
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e)
        {
	  double heading = getHeadingRadians();
          double x = getX() + move*Math.sin(heading);
       	  double y = getY() + move*Math.cos(heading);	
      	  double dWidth = getBattleFieldWidth();
      	  double dHeight = getBattleFieldHeight();
          if(x < 30 || x > dWidth-30 || y < 30 || y > dHeight-30)
          {
	         setBack(move);
          }
        }
}

