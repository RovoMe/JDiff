package at.rovo.diff;

import java.util.List;

/**
 * <p>Provides functions to calculate the longest common subsequence (LCS) for
 * forward, backward and in-between estimations.</p>
 * 
 * @author Roman Vottner
 */
public class LCS
{
	/**
	 * <p>Calculates the longest common subsequence (LCS) in a forward manner
	 * for two objects <em>pa</em> and <em>pb</em>.</p>
	 * 
	 * @param snakes The actions (deletion, insertion) required to transform 
	 *               <em>pa</em> to <em>pb</em>. Snakes also contain the 
	 *               diagonal counts when both elements equals.
	 * @param pa Usually the older object which should be compared
	 * @param N The length of <em>pa</em>
	 * @param pb Usually the newest object to be compared with <em>pa</em>
	 * @param M The length of <em>pb</em>
	 * @param V An array of end points for a given k-line
	 * @param d number of differences for the same trace
	 * @return
	 */
	static <T> Snake<T> Forward( List<Snake<T>> snakes, T[] pa, int N, T[] pb, int M, V V, int d )
	{
		// An important observation for the implementation is that end points 
		// for even d are on even k-lines only and vice-versa. That's why k+=2
		for ( int k = -d ; k <= d ; k += 2 )
		{
			// are we on the down track?
			boolean down = ( k == -d || ( k != d && V.getK(k - 1) < V.getK(k + 1) ) );

			// to get to a line k, we either must move down (k+1) or right (k-1)
			int xStart = down ? V.getK(k + 1) : V.getK(k - 1);
			// y can easily calculated by subtracting k from x --> y = x - k
			int yStart = xStart - ( down ? k + 1 : k - 1 );

			// calculate end points
			int xEnd = down ? xStart : xStart + 1;
			int yEnd = xEnd - k;

			int snake = 0;
			// follow diagonals
			while ( xEnd < N && yEnd < M && pa[ xEnd ].equals(pb[ yEnd ]) ) 
			{ 
				xEnd++; yEnd++; snake++; 
			}

			// save end points
			V.setK(k,xEnd);

			// check for solution
			if ( xEnd >= N && yEnd >= M )
				// solution has been found
				return new Snake<T>( 0, N, 0, M, true, xStart, yStart, down, snake );
		}

		return null;
	}

	/**
	 * <p>Calculates the longest common subsequence (LCS) in a backward manner
	 * for two objects <em>pa</em> and <em>pb</em>.</p>
	 * 
	 * @param snakes The actions (deletion, insertion) required to transform 
	 *               <em>pa</em> to <em>pb</em>. Snakes also contain the 
	 *               diagonal counts when both elements equals.
	 * @param pa Usually the older object which should be compared
	 * @param N The length of <em>pa</em>
	 * @param pb Usually the newest object to be compared with <em>pa</em>
	 * @param M The length of <em>pb</em>
	 * @param V An array of end points for a given k-line
	 * @param d number of differences for the same trace
	 * @return
	 */
	static <T> Snake<T> Reverse( List<Snake<T>> snakes, T[] pa, int N, T[] pb, int M, V V, int d )
	{
		// As the length of sequences pa and pb can be different, the k lines of 
		// the forward and reverse algorithms can be different. It is useful to
		// isolate this difference as a variable.
		int DELTA = N - M;

		// An important observation for the implementation is that end points 
		// for even d are on even k-lines only and vice-versa. That's why k+=2
		for ( int k = -d + DELTA ; k <= d + DELTA ; k += 2 )
		{
			// are we on the down up-track or on the left one?
			boolean up = ( k == d + DELTA || ( k != -d + DELTA && V.getK(k - 1) < V.getK(k + 1) ) );

			// to get to a line k, we either must move up (k-1) or left (k+1)
			int xStart = up ? V.getK(k - 1) : V.getK(k + 1);
			// y can easily calculated by subtracting k from x --> y = x - k
			int yStart = xStart - ( up ? k - 1 : k + 1 );

			// calculate end points
			int xEnd = up ? xStart : xStart - 1;
			int yEnd = xEnd - k;

			int snake = 0;
			// follow diagonals
			while ( xEnd > 0 && yEnd > 0 && pa[ xEnd - 1 ].equals(pb[ yEnd - 1 ]) ) { xEnd--; yEnd--; snake++; }

			// save end points
			V.setK(k,xEnd);
			
			// check for solution
			if ( xEnd <= 0 && yEnd <= 0 )
				// solution has been found
				return new Snake<T>( 0, N, 0, M, false, xStart, yStart, up, snake );
		}

		return null;
	}

	static <T> SnakePair<T> MiddleSnake( T[] pa, int a0, int N, T[] pb, int b0, int M, V VForward, V VReverse, List<V> forwardVs, List<V> reverseVs ) throws Exception
	{
		// we only need to find a middle snake with a d which is half of the
		// d of the forward and reverse algorithms.
		int MAX = ( N + M + 1 ) / 2;
		// As the length of sequences pa and pb can be different, the k lines of 
		// the forward and reverse algorithms can be different. It is useful to
		// isolate this difference as a variable.
		int DELTA = N - M;

		VForward.InitStub( N, M, MAX );
		VReverse.InitStub( N, M, MAX );

		boolean DeltaIsEven = ( DELTA % 2 ) == 0;

		for ( int d = 0 ; d <= MAX ; d++ )
		{
			// forward
			// checks against reverse D-1
			try
			{
				// An important observation for the implementation is that end points 
				// for even d are on even k-lines only and vice-versa. That's why k+=2
				for ( int k = -d ; k <= d ; k += 2 )
				{
					// are we on the down track?
					boolean down = ( k == -d || ( k != d && VForward.getK(k - 1) < VForward.getK(k + 1) ) );

					// to get to a line k, we either must move down (k+1) or right (k-1)
					int xStart = down ? VForward.getK(k + 1) : VForward.getK(k - 1);
					// y can easily calculated by subtracting k from x --> y = x - k
					int yStart = xStart - ( down ? k + 1 : k - 1 );

					// calculate end points
					int xEnd = down ? xStart : xStart + 1;
					int yEnd = xEnd - k;

					int snake = 0;
					// follow diagonals
					while ( xEnd < N && yEnd < M && pa[ xEnd + a0 ].equals(pb[ yEnd + b0 ]) ) 
					{ 
						xEnd++; yEnd++; snake++; 
					}

					// save end points
					VForward.setK(k, xEnd);

					// for odd delta, we must look for overlap of forward paths 
					// with differences d and reverse paths with differences d-1
					
					// if Δ is odd and k ϵ [ Δ - ( D - 1 ), Δ + ( D - 1 ) ]
					if ( DeltaIsEven || k < DELTA - ( d - 1 ) || k > DELTA + ( d - 1 ) ) 
						continue;

					// if the path overlaps the farthest reaching reverse 
					// ( D - 1 )-path in diagonal k
					if ( VForward.getK(k) < VReverse.getK(k) ) 
						continue;

					// overlap :)
					Snake<T> forward = new Snake<T>( a0, N, b0, M, true, xStart + a0, yStart + b0, down, snake );
					forward.D = d;

					return new SnakePair<T>( ( 2 * d ) - 1, forward, null );
				}
			}
			finally
			{
				if ( forwardVs != null ) 
					forwardVs.add( VForward.CreateCopy( d, true, 0 ) );
			}

			// backward
			// checks against forward D
			try
			{
				// An important observation for the implementation is that end points 
				// for even d are on even k-lines only and vice-versa. That's why k+=2
				for ( int k = -d + DELTA ; k <= d + DELTA ; k += 2 )
				{
					// are we on the down up-track or on the left one?
					boolean up = ( k == d + DELTA || ( k != -d + DELTA && VReverse.getK(k - 1) < VReverse.getK(k + 1) ) );

					// to get to a line k, we either must move up (k-1) or left (k+1)
					int xStart = up ? VReverse.getK(k - 1) : VReverse.getK(k + 1);
					// y can easily calculated by subtracting k from x --> y = x - k
					int yStart = xStart - ( up ? k - 1 : k + 1 );

					// calculate end points
					int xEnd = up ? xStart : xStart - 1;
					int yEnd = xEnd - k;

					int snake = 0;
					// follow diagonals
					while ( xEnd > 0 && yEnd > 0 && pa[ xEnd + a0 - 1 ].equals(pb[ yEnd + b0 - 1 ]) ) 
					{ 
						xEnd--; yEnd--; snake++; 
					}

					// save end points
					VReverse.setK(k, xEnd);

					// remember: our k is actually k + Δ

					// if Δ is even and k + Δ ϵ [ -D, D ]
					if ( !DeltaIsEven || k < -d || k > d ) 
						continue;

					// if the path overlaps the farthest reaching forward D-path 
					// in diagonal k + Δ
					if ( VReverse.getK(k) > VForward.getK(k) ) 
						continue;

					// overlap :)
					Snake<T> reverse = new Snake<T>( a0, N, b0, M, false, xStart + a0, yStart + b0, up, snake );
					reverse.D = d;

					return new SnakePair<T>( 2 * d, null, reverse );
				}
			}
			finally
			{
				if ( reverseVs != null ) 
					reverseVs.add( VReverse.CreateCopy( d, false, DELTA ) );
			}
		}

		throw new Exception( "No middle snake" );
	}
}
