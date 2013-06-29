package at.rovo.test;

import at.rovo.diff.GreedyDiff;
import at.rovo.diff.LinearDiff;
import at.rovo.diff.Results;
import at.rovo.diff.Snake;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		int sel = 5;
		
		Results<String> res = null;
		
		switch (sel)
		{
			case 0:
				res = GreedyDiff.Compare("ABCABBA", "CBABAC", true);
				break;
			case 1:
				res = GreedyDiff.Compare("12345666789", "123450789", true);
				break;
			case 2:
				res = GreedyDiff.Compare("ABCABBA", "CBABAC", false);
				break;
			case 3:
				res = GreedyDiff.Compare("12345666789", "123450789", false);
				break;
			case 4:
				res = LinearDiff.Compare("ABCABBA", "CBABAC");
				break;
			case 5:
				res = LinearDiff.Compare("12345666789", "123450789");
				break;
			default:
				break;
		}
		if (res.getSnakes() != null)
		{
			for (Snake<String> snake : res.getSnakes())
			{
				System.out.println(snake);
			}
		}
		else
			System.out.println("No snakes found!");
	}
}
