package at.rovo.diff;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Roman Vottner
 * @see http://simplygenius.net/Article/DiffTutorial2
 * @see http://www.codeproject.com/Articles/42279/Investigating-Myers-diff-algorithm-Part-2-of-2
 */
public class LinearDiff
{
	/**
	 * <p>Compares two character sequences or strings with each other and 
	 * calculates the shortest edit sequence (SES) as well as the longest common
	 * subsequence (LCS) to transfer input <em>a</em> to input <em>b</em>. The
	 * SES are the necessary actions required to perform the transformation.</p>
	 * 
	 * @param a The first character sequence; usually the oldest string
	 * @param b The second character sequence; usually the newest string
	 * @return The result containing the snake that lead from input string a to
	 *         input string b
	 * @throws Exception
	 */
	public static Results<String> Compare( String a, String b ) throws Exception
	{
		String[] aa = StringToArray(a);
		String[] ab = StringToArray(b);

		return Compare( aa, ab );
	}
	
	/**
	 * <p>Transforms a string into an array of single string characters.</p>
	 * 
	 * @param s The string to turn into a string array
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
	 * <p>Compares two arrays of type <em>T</em> with each other and calculates
	 * the shortest edit sequence (SES) as well as the longest common 
	 * subsequence (LCS) to transfer input <em>a</em> to input <em>b</em>. The
	 * SES are the necessary actions required to perform the transformation.</p>
	 * 
	 * @param aa Usually the older object which should be compared
	 * @param ab Usually the newest object to be compared with <em>aa</em>
	 * @return The result containing the snake that lead from input <em>aa</em>
	 *         to input <em>ab</em>
	 * @throws Exception
	 */
	public static <T> Results<T> Compare( T[] aa, T[] ab ) throws Exception
	{
		V VForward = new V( aa.length, ab.length, true, true );
		V VReverse = new V( aa.length, ab.length, false, true );

		List<Snake<T>> snakes = new ArrayList<Snake<T>>();
		List<V> forwardVs = new ArrayList<V>();
		List<V> reverseVs = new ArrayList<V>();

		Compare( snakes, forwardVs, reverseVs, aa, aa.length, ab, ab.length, VForward, VReverse );

		return new Results<T>(snakes, forwardVs, reverseVs );
	}

	/**
	 * <p>Compares two arrays of type <em>T</em> with each other and calculates
	 * the shortest edit sequence (SES) as well as the longest common 
	 * subsequence (LCS) to transfer input <em>a</em> to input <em>b</em>. The
	 * SES are the necessary actions required to perform the transformation.</p>
	 * 
	 * @param snakes The possible solution paths for transforming object <em>pa
	 *               </em> to <em>pb</em>
	 * @param forwardVs All saved end points in forward direction indexed on 
	 *                  <em>d</em>
	 * @param reverseVs All saved end points in backward direction indexed on 
	 *                  <em>d</em>
	 * @param pa Usually the older object which should be compared
	 * @param N The length of <em>pa</em>
	 * @param pb Usually the newest object to be compared with <em>pa</em>
	 * @param M The length of <em>pb</em>
	 * @param VForward An array of end points for a given k-line in forward 
	 *                 direction
	 * @param VReverse An array of end points for a given k-line in backward 
	 *                 direction
	 * @param forward Indicates forward or backward comparison of both words
	 * @throws Exception
	 */
	static <T> void Compare( List<Snake<T>> snakes,
		List<V> forwardVs, List<V> reverseVs,
		T[] pa, int N, T[] pb, int M,
		V VForward, V VReverse ) throws Exception
	{
		Compare(0, snakes, forwardVs, reverseVs, pa, 0, N, pb, 0, M, VForward, VReverse );
	}

	/**
	 * <p>Compares two arrays of type <em>T</em> with each other and calculates
	 * the shortest edit sequence (SES) as well as the longest common 
	 * subsequence (LCS) to transfer input <em>a</em> to input <em>b</em>. The
	 * SES are the necessary actions required to perform the transformation.</p>
	 * 
	 * @param recursion The number of the current recursive step
	 * @param snakes The possible solution paths for transforming object <em>pa
	 *               </em> to <em>pb</em>
	 * @param forwardVs All saved end points in forward direction indexed on 
	 *                  <em>d</em>
	 * @param reverseVs All saved end points in backward direction indexed on 
	 *                  <em>d</em>
	 * @param pa Usually the older object which should be compared
	 * @param a0
	 * @param N The length of <em>pa</em>
	 * @param pb Usually the newest object to be compared with <em>pa</em>
	 * @param b0
	 * @param M The length of <em>pb</em>
	 * @param VForward An array of end points for a given k-line in forward 
	 *                 direction
	 * @param VReverse An array of end points for a given k-line in backward 
	 *                 direction
	 * @param forward Indicates forward or backward comparison of both words
	 * @throws Exception
	 */
	static <T> void Compare( int recursion, List<Snake<T>> snakes,
		List<V> forwardVs, List<V> reverseVs,
		T[] pa, int a0, int N, T[] pb, int b0, int M,
		V VForward, V VReverse ) throws Exception
	{
		if ( M == 0 && N > 0 )
		{
			// add N deletions to SES
			Snake<T> right = new Snake<T>( a0, N, b0, M, true, a0, b0, N, 0, 0 );
			snakes.add( right );
		}

		if ( N == 0 && M > 0 )
		{
			// add M insertions to SES
			Snake<T> down = new Snake<T>( a0, N, b0, M, true, a0, b0, 0, M, 0 );
			snakes.add( down );
		}

		if ( N <= 0 || M <= 0 ) 
			return;

		//calculate middle snake
		SnakePair<T> m = LCS.MiddleSnake( pa, a0, N, pb, b0, M, VForward, VReverse, forwardVs, reverseVs );

		// Initial setup for recursion
		if ( recursion == 0 )
		{
			if ( m.Forward != null ) { m.Forward.IsMiddle = true; }
			if ( m.Reverse != null ) { m.Reverse.IsMiddle = true; }
		}

		if ( m.D > 1 )
		{
			Pair<Integer> xy = ( m.Forward != null ? m.Forward.getStartPoint() : m.Reverse.getEndPoint() );
			Pair<Integer> uv = ( m.Reverse != null ? m.Reverse.getStartPoint() : m.Forward.getEndPoint() );

			// top left
			Compare( recursion + 1, snakes, null, null, pa, a0, xy.X() - a0, pb, b0, xy.Y() - b0, VForward, VReverse );

			// add middle snake to results
			if ( m.Forward != null ) 
				snakes.add( m.Forward );
			if ( m.Reverse != null ) 
				snakes.add( m.Reverse );

			// bottom right
			Compare( recursion + 1, snakes, null, null, pa, uv.X(), a0 + N - uv.X(), pb, uv.Y(), b0 + M - uv.Y(), VForward, VReverse );

		}
		else
		{
			if ( m.Forward != null )
			{
				// add d = 0 diagonal to results
				if ( m.Forward.XStart > a0 )
				{
					if ( m.Forward.XStart - a0 != m.Forward.YStart - b0 ) 
						throw new Exception( "Missed D0 forward" );
					snakes.add( new Snake<T>( a0, N, b0, M, true, a0, b0, 0, 0, m.Forward.XStart - a0 ) );
				}

				// add middle snake to results
				snakes.add( m.Forward );
			}

			if ( m.Reverse != null )
			{
				// add middle snake to results
				snakes.add( m.Reverse );

				// D0
				if ( m.Reverse.XStart < a0 + N )
				{
					if ( a0 + N - m.Reverse.XStart != b0 + M - m.Reverse.YStart ) 
						throw new Exception( "Missed D0 reverse" );
					snakes.add( new Snake<T>( a0, N, b0, M, true, m.Reverse.XStart, m.Reverse.YStart, 0, 0, a0 + N - m.Reverse.XStart ) );
				}
			}
		}
	}
}
