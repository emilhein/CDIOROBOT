package pictureToMat;

import dist.CalcDist;
import dist.Punkt;

public class PickGoal {
	
	public Punkt pick(Punkt robotCoordi, int timeLeft)
	{
		CalcDist distCalculator = new CalcDist();
		Punkt goalA = new Punkt(0, 0); // ! initialize
		Punkt goalB = new Punkt(0, 0); // ! initialize
		int minTime = 0; // ! initialize
		
		if(timeLeft < minTime)
		{
			int distanceToA = distCalculator.Calcdist(goalA, robotCoordi);
			int distanceToB = distCalculator.Calcdist(goalB, robotCoordi);
			
			if(distanceToB < distanceToA)
			{
				return goalB;
			}
			else
			{
				return goalA;
			}
		}
		else
		{
			return goalA;
		}
	}

}
