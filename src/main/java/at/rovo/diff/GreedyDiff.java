package at.rovo.diff;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This implementation is based on the C# implementation of Nicholas Butler at
 * SimplyGenius.NET
 * </p>
 * <p>
 * The basic principle behind the algorithm is to find the shortest path that
 * transforms an input string <em>A</em> to a second string <em>B</em> with the
 * least actions required.
 * </p>
 * <p>
 * The algorithm therefore calculates values for each <em>d contour</em>, which
 * can be thought of as the border of actions with the same quantity. An action
 * is here either a deletion (right movement in the graph) or an insertion (down
 * movement in the graph). The <em>d contour</em> is itself calculated by
 * following the <em>k-lines</em> which are just straight diagonals, whose value
 * increase towards the right and decrease towards the left (or bottom). A
 * <em>k-line</em> therefore is the number of shortest edit scripts (SES; or
 * actions) that are required to reach any point on that diagonal from the
 * start.
 * </p>
 * 
 * @author Roman Vottner
 * @see http://simplygenius.net/Article/DiffTutorial1
 * @see http://www.codeproject.com/Articles/42279/Investigating-Myers-diff-algorithm-Part-1-of-2
 */
public class GreedyDiff
{
	/**
	 * <p>
	 * Compares two character sequences or strings with each other and
	 * calculates the shortest edit sequence (SES) as well as the longest common
	 * subsequence (LCS) to transfer input <em>a</em> to input <em>b</em>. The
	 * SES are the necessary actions required to perform the transformation.
	 * </p>
	 * 
	 * @param a
	 *            The first character sequence; usually the oldest string
	 * @param b
	 *            The second character sequence; usually the newest string
	 * @param forward
	 *            Indicates forward or backward comparison of both words
	 * @return The result containing the snake that lead from input string a to
	 *         input string b
	 * @throws Exception
	 */
	public static Results<String> Compare( String a, String b, boolean forward ) 
			throws Exception
	{
		String[] aa = StringToArray(a);
		String[] ab = StringToArray(b);
		
		return Compare( aa, ab, forward );
	}
	
	/**
	 * <p>
	 * Transforms a string into an array of single string characters.
	 * </p>
	 * 
	 * @param s
	 *            The string to turn into a string array
	 * @return The input string transformed into an array of string characters
	 */
	private static String[] StringToArray(String s)
	{
		String[] as = new String[s.length()];
		int i = 0;
		for (char c : s.toCharArray())
			as[i++] = ""+c;
		return as;
	}

	/**
	 * <p>
	 * Compares two arrays of type <em>T</em> with each other and calculates the
	 * shortest edit sequence (SES) as well as the longest common subsequence
	 * (LCS) to transfer input <em>a</em> to input <em>b</em>. The SES are the
	 * necessary actions required to perform the transformation.
	 * </p>
	 * 
	 * @param aa
	 *            Usually the older object which should be compared
	 * @param ab
	 *            Usually the newest object to be compared with <em>aa</em>
	 * @param forward
	 *            Indicates forward or backward comparison of both words
	 * @return The result containing the snake that lead from input <em>aa</em>
	 *         to input <em>ab</em>
	 * @throws Exception
	 */
	public static <T> Results<T> Compare( T[] aa, T[] ab, boolean forward ) 
			throws Exception
	{		
		V V = new V( aa.length, ab.length, forward, false );

		List<Snake<T>> snakes = new ArrayList<Snake<T>>();
		List<V> vs = new ArrayList<V>();

		Compare( snakes, vs, aa, aa.length, ab, ab.length, V, forward );

		return new Results<T>(snakes, forward, vs );
	}

	/**
	 * <p>
	 * Compares two arrays of type <em>T</em> with each other and calculates the
	 * shortest edit sequence (SES) as well as the longest common subsequence
	 * (LCS) to transfer input <em>a</em> to input <em>b</em>. The SES are the
	 * necessary actions required to perform the transformation.
	 * </p>
	 * 
	 * @param snakes
	 *            The possible solution paths for transforming object <em>pa
	 *               </em> to <em>pb</em>
	 * @param vs
	 *            All saved end points indexed on <em>d</em>
	 * @param pa
	 *            Elements of the first object. Usually the original object
	 * @param N
	 *            The number of elements of the first object to compare
	 * @param pb
	 *            Elements of the second object. Usually the current object
	 * @param M
	 *            The number of elements of the second object to compare
	 * @param V
	 *            An array of end points for a given k-line
	 * @param forward
	 *            Indicates forward or backward comparison of both words
	 * @throws Exception
	 */
	static <T> void Compare( List<Snake<T>> snakes, List<V> vs, T[] pa, int N, T[] pb, int M, V V, boolean forward ) throws Exception
	{
		Snake<T> last = null;

		int MAX = N + M;
		int DELTA = N - M;

		// calculate the farthest reaching path on each k line for successive d
		// As the maximum value of d can only be between N (deletions) and -M
		// (insertion) restrict the area to this.
		// Note: deletions count as +1 for a k-line, while an insertions count 
		//       as -1!
		for (int d = 0 ; d <= MAX ; d++ )
		{
			// d = number of differences in that trace
			if ( forward )
				last = LCS.Forward( snakes, pa, N, pb, M, V, d );
			else
				last = LCS.Reverse( snakes, pa, N, pb, M, V, d );

			vs.add( V.CreateCopy( d, forward, ( forward ? 0 : DELTA ) ) );

			if ( last != null ) break;
		}

		if ( last == null ) 
			throw new Exception( "No solution was found!" );

		// find the solving snake
		if ( forward ) 
			SolveForward( snakes, vs, pa, pb, N, M );
		else 
			SolveReverse( snakes, vs, pa, pb, N, M );
	}

	/**
	 * <p>
	 * Finds all snakes that lead to the solution by taking a snapshot of each
	 * end point after each iteration of d and then working backwards from
	 * d<sub>solution</sub>
	 * </p>
	 * to 0.
	 * 
	 * @param snakes
	 *            All found snakes that lead to the solution
	 * @param vs
	 *            All saved end points indexed on <em>d</em>
	 * @param pa
	 *            Elements of the first object. Usually the original object
	 * @param pb
	 *            Elements of the second object. Usually the current object
	 * @param N
	 *            The number of elements of the first object to compare
	 * @param M
	 *            The number of elements of the second object to compare
	 * @throws Exception
	 */
	static <T> void SolveForward( List<Snake<T>> snakes, List<V> vs, T[] pa, T[] pb, int N, int M ) throws Exception
	{
		Pair<Integer> p = new Pair<Integer>( N, M );

		for ( int d = vs.size() - 1 ; p.X() > 0 || p.Y() > 0 ; d-- )
		{
			V V = vs.get(d);

			int k = p.X() - p.Y();

			int xEnd = V.getK(k);
			int yEnd = xEnd - k;

			if ( xEnd != p.X() || yEnd != p.Y() )
				throw new Exception( "No solution for " 
					+ "d:" + d + " k:" + k 
					+ " p:( " + p.X() + ", " + p.Y() + " )"
					+ " V:( " + xEnd + ", " + yEnd + " )" );

			Snake<T> solution = new Snake<T>( 0, p.X(), 0, p.Y(), true, 0, V, k, d, pa, pb );

			if ( solution.getXEnd() != p.X() || solution.getYEnd() != p.Y() )
				throw new Exception( "Missed solution for " 
					+ "d:" + d + " k:" + k 
					+ " p:( " + p.X() + ", " + p.Y() + " )"
					+ " V:( " + xEnd + ", " + yEnd + " )" );

			if (snakes.size() > 0)
			{
				Snake<T> snake = snakes.get(0);
				// combine snakes of the same kind
				if (!snake.append(solution))
					snakes.add(0, solution );
			}
			else
				snakes.add(0, solution);

			p.X(solution.XStart);
			p.Y(solution.YStart);
		}
	}

	/**
	 * <p>
	 * Finds all snakes that lead to the solution by taking a snapshot of each
	 * end point after each iteration of d and then working forward from 0 to
	 * d<sub>solution</sub>
	 * </p>
	 * .
	 * 
	 * @param snakes
	 *            All found snakes that lead to the solution
	 * @param vs
	 *            All saved end points indexed on <em>d</em>
	 * @param pa
	 *            Elements of the first object. Usually the original object
	 * @param pb
	 *            Elements of the second object. Usually the current object
	 * @param N
	 *            The number of elements of the first object to compare
	 * @param M
	 *            The number of elements of the second object to compare
	 * @throws Exception
	 */
	static <T> void SolveReverse( List<Snake<T>> snakes, List<V> vs, T[] pa, T[] pb, int N, int M ) throws Exception
	{
		Pair<Integer> p = new Pair<Integer>( 0, 0 );

		for ( int d = vs.size() - 1 ; p.X() < N || p.Y() < M ; d-- )
		{
			V v = vs.get(d);

			int k = p.X() - p.Y();

			int xEnd = v.getK(k);
			int yEnd = xEnd - k;

			if ( xEnd != p.X() || yEnd != p.Y() )
				throw new Exception( "No solution for " 
					+ "d:" + d + " k:" + k 
					+ " p:( " + p.X() + ", " + p.Y() + " )"
					+ " V:( " + xEnd + ", " + yEnd + " )" );

			Snake<T> solution = new Snake<T>( p.X(), N - p.X(), p.Y(), M - p.Y(), false, N - M, v, k, d, pa, pb );

			if ( solution.getXEnd() != p.X() || solution.getYEnd() != p.Y() )
				throw new Exception( "Missed solution for " 
					+ "d:" + d + " k:" + k 
					+ " p:( " + p.X() + ", " + p.Y() + " )"
					+ " V:( " + xEnd + ", " + yEnd + " )" );

			if (snakes.size() > 0)
			{
				Snake<T> snake = snakes.get(snakes.size()-1);
				// combine snakes of the same kind
				if (snake.append(solution))
					solution = snake;
				else
					snakes.add(solution );
			}
			else
				snakes.add(solution);

			p.X(solution.XStart);
			p.Y(solution.YStart);
		}
	}
}