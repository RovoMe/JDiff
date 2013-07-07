package at.rovo.diff;

/**
 * <p>
 * A snake is a segment along a path which converts an object A to object B by
 * either eliminating elements from object A or inserting elements from object
 * B.
 * </p>
 * 
 * @author Roman Vottner
 * @param <T>
 *            The type of the element the snake will hold
 */
public class Snake<T>
{
	/** The x-position of a starting point **/
	public int XStart;
	/** The y-position of a starting point **/
	public int YStart;
	/** Defines the number of deleted elements from the first object to match 
	 * the second object **/
	public int ADeleted;
	/** Defines the number of inserted elements from the second object to match 
	 * the first object**/
	public int BInserted;
	/** Defines the number of equal elements in both objects **/
	public int DiagonalLength;
	/** Defines the comparison direction of both objects **/
	public boolean IsForward = true;
	/** The difference in length between the first and second object to compare. 
	 * This value is used as an offset between the forward k lines to the 
	 * reverse ones **/
	public int DELTA = 0;
	/** Defines if this snake is a middle segment **/
	private boolean IsMiddle = false;
	/** A value of 0 or 1 indicate an edge, where 0 means both objects are equal
	 * while 1 means there is either one insertion or one deletion. A value of
	 * greater than needs to be checked in both directions **/
	private int D = -1;
		
	/**
	 * <p>
	 * Initializes a new snake. The comparison direction can be defined via the
	 * <em>isForward</em> parameter. If set to true, the comparison will be done
	 * from start till end, while a value of false will result in a backward
	 * comparison from end to start.
	 * </p>
	 * <p>
	 * <em>delta</em> defines the difference in length between the first and the
	 * second object to compare.
	 * </p>
	 * 
	 * @param isForward
	 *            If set to true a forward comparison will be done; else a
	 *            backward comparison
	 * @param delta
	 *            The difference in length between the first and the second
	 *            object to compare
	 */
	Snake( boolean isForward, int delta )
	{
		this.IsForward = isForward;

		if ( !this.IsForward ) 
			this.DELTA = delta;
	}
	
	/**
	 * <p>
	 * Initializes a new snake segment.
	 * </p>
	 * 
	 * @param a0
	 *            The starting position in the array of elements from the first
	 *            object to compare
	 * @param N
	 *            The index of the last element from the first object to compare
	 * @param b0
	 *            The starting position in the array of elements from the second
	 *            object to compare
	 * @param M
	 *            The index of the last element from the second object to
	 *            compare
	 * @param isForward
	 *            The comparison direction; true for a forward comparison, false
	 *            otherwise
	 * @param xStart
	 *            The x-position of the current node
	 * @param yStart
	 *            The y-position of the current node
	 * @param aDeleted
	 *            Defines the number of removed elements from the first object
	 *            (right movements in the graph)
	 * @param bInserted
	 *            Defines the number of inserted elements from the second object
	 *            (down movement in the graph)
	 * @param diagonal
	 *            Defines the number of equal elements in both objects for a
	 *            given segment
	 */
	Snake( int a0, int N, int b0, int M, boolean isForward, int xStart, int yStart, int aDeleted, int bInserted, int diagonal )
	{
		this.XStart = xStart;
		this.YStart = yStart;
		this.ADeleted = aDeleted;
		this.BInserted = bInserted;
		this.DiagonalLength = diagonal;
		this.IsForward = isForward;

		RemoveStubs( a0, N, b0, M );
	}

	/**
	 * <p>
	 * Initializes a new snake segment.
	 * </p>
	 * 
	 * @param a0
	 *            The starting position in the array of elements from the first
	 *            object to compare
	 * @param N
	 *            The index of the last element from the first object to compare
	 * @param b0
	 *            The starting position in the array of elements from the second
	 *            object to compare
	 * @param M
	 *            The index of the last element from the second object to
	 *            compare
	 * @param isForward
	 *            The comparison direction; true for a forward comparison, false
	 *            otherwise
	 * @param xStart
	 *            The x-position of the current node
	 * @param yStart
	 *            The y-position of the current node
	 * @param down
	 *            Defines if insertion (down movement; true) or a deletion
	 *            (right movement; false) should be done
	 * @param diagonal
	 *            Defines the number of equal elements in both objects for a
	 *            given segment
	 */
	Snake( int a0, int N, int b0, int M, boolean isForward, int xStart, int yStart, boolean down, int diagonal )
	{
		this.XStart = xStart;
		this.YStart = yStart;
		this.ADeleted = down ? 0 : 1;
		this.BInserted = down ? 1 : 0;
		this.DiagonalLength = diagonal;
		this.IsForward = isForward;

		RemoveStubs( a0, N, b0, M );
	}

	/**
	 * <p>
	 * Initializes a new instance and calculates the segment based on the
	 * provided data.
	 * </p>
	 * 
	 * @param a0
	 *            The starting position in the array of elements from the first
	 *            object to compare
	 * @param N
	 *            The index of the last element from the first object to compare
	 * @param b0
	 *            The starting position in the array of elements from the second
	 *            object to compare
	 * @param M
	 *            The index of the last element from the second object to
	 *            compare
	 * @param forward
	 *            The comparison direction; true for a forward comparison, false
	 *            otherwise
	 * @param delta
	 *            The difference in length of both objects to compare
	 * @param V
	 *            An array of end points for a given k-line
	 * @param k
	 *            The k-line the snake should get calculated for
	 * @param d
	 *            Number of differences for the same trace
	 * @param pa
	 *            Elements of the first object. Usually the original object
	 * @param pb
	 *            Elements of the second object. Usually the current object
	 */
	Snake( int a0, int N, int b0, int M, boolean forward, int delta, V V, int k, int d, T[] pa, T[] pb )
	{
		this.IsForward = forward;
		this.DELTA = delta;
		Calculate( V, k, d, pa, a0, N, pb, b0, M );
	}

	/**
	 * <p>
	 * Calculates a new snake segment depending on the current comparison
	 * direction.
	 * </p>
	 * 
	 * @param V
	 *            An array of end points for a given k-line
	 * @param k
	 *            The k-line the snake should get calculated for
	 * @param d
	 *            Number of differences for the same trace
	 * @param pa
	 *            Elements of the first object. Usually the original object
	 * @param a0
	 *            The starting position in the array of elements from the first
	 *            object to compare
	 * @param N
	 *            The index of the last element from the first object to compare
	 * @param pb
	 *            Elements of the second object. Usually the current object
	 * @param b0
	 *            The starting position in the array of elements from the second
	 *            object to compare
	 * @param M
	 *            The index of the last element from the second object to
	 *            compare
	 * @return The calculated snake segment
	 */
	Snake<T> Calculate( V V, int k, int d, T[] pa, int a0, int N, T[] pb, int b0, int M )
	{
		if ( this.IsForward ) 
			return CalculateForward( V, k, d, pa, a0, N, pb, b0, M );
		return CalculateBackward( V, k, d, pa, a0, N, pb, b0, M );
	}

	/**
	 * <p>
	 * Calculates a new snake segment for a forward comparison direction.
	 * </p>
	 * 
	 * @param V
	 *            An array of end points for a given k-line
	 * @param k
	 *            The k-line the snake should get calculated for
	 * @param d
	 *            Number of differences for the same trace
	 * @param pa
	 *            Elements of the first object. Usually the original object
	 * @param a0
	 *            The starting position in the array of elements from the first
	 *            object to compare
	 * @param N
	 *            The index of the last element from the first object to compare
	 * @param pb
	 *            Elements of the second object. Usually the current object
	 * @param b0
	 *            The starting position in the array of elements from the second
	 *            object to compare
	 * @param M
	 *            The index of the last element from the second object to
	 *            compare
	 * @return The calculated snake segment
	 */
	private Snake<T> CalculateForward( V V, int k, int d, T[] pa, int a0, int N, T[] pb, int b0, int M )
	{
		// determine if a insertion (down) or a deletion (right) occurred
		boolean down = ( k == -d || ( k != d && V.getK(k - 1) < V.getK(k + 1) ) );

		// calculate the x and y position based on the movement direction
		int xStart = down ? V.getK(k + 1) : V.getK(k - 1);
		int yStart = xStart - ( down ? k + 1 : k - 1 );

		// calculate the x and y position of the end point
		int xEnd = down ? xStart : xStart + 1;
		int yEnd = xEnd - k;

		// calculate the number of equal elements in both objects for this 
		// segment
		int snake = 0;
		while ( xEnd < N && yEnd < M && pa[ xEnd + a0 ].equals(pb[ yEnd + b0 ]) ) 
		{ 
			xEnd++; 
			yEnd++; 
			snake++; 
		}

		// assign the calculated values to the fields of this instance
		this.XStart = xStart + a0;
		this.YStart = yStart + b0;
		this.ADeleted = down ? 0 : 1;
		this.BInserted = down ? 1 : 0;
		this.DiagonalLength = snake;

		RemoveStubs( a0, N, b0, M );

		return this;
	}

	/**
	 * <p>
	 * Calculates a new snake segment for a backward comparison direction.
	 * </p>
	 * 
	 * @param V
	 *            An array of end points for a given k-line
	 * @param k
	 *            The k-line the snake should get calculated for
	 * @param d
	 *            Number of differences for the same trace
	 * @param pa
	 *            Elements of the first object. Usually the original object
	 * @param a0
	 *            The starting position in the array of elements from the first
	 *            object to compare
	 * @param N
	 *            The index of the last element from the first object to compare
	 * @param pb
	 *            Elements of the second object. Usually the current object
	 * @param b0
	 *            The starting position in the array of elements from the second
	 *            object to compare
	 * @param M
	 *            The index of the last element from the second object to
	 *            compare
	 * @return The calculated snake segment
	 */
	private Snake<T> CalculateBackward( V V, int k, int d, T[] pa, int a0, int N, T[] pb, int b0, int M )
	{
		// determine if a insertion (up) or a deletion (left) occurred
		boolean up = ( k == d + this.DELTA || 
				( k != -d + this.DELTA && V.getK(k - 1) < V.getK(k + 1) ) );

		// calculate the x and y position based on the movement direction
		int xStart = up ? V.getK(k - 1) : V.getK(k + 1);
		int yStart = xStart - ( up ? k - 1 : k + 1 );

		// calculate the x and y position of the end point
		int xEnd = up ? xStart : xStart - 1;
		int yEnd = xEnd - k;

		// calculate the number of equal elements in both objects for this 
		// segment by following diagonals
		int snake = 0;
		while ( xEnd > 0 && yEnd > 0 && pa[xEnd-1].equals(pb[yEnd-1]) ) 
		{ 
			xEnd--; 
			yEnd--; 
			snake++; 
		}

		// assign the calculated values to the fields of this instance
		this.XStart = xStart;
		this.YStart = yStart;
		this.ADeleted = up ? 0 : 1;
		this.BInserted = up ? 1 : 0;
		this.DiagonalLength = snake;

		RemoveStubs( a0, N, b0, M );

		return this;
	}
	
	/**
	 * <p>
	 * Returns the start point of this snake segment.
	 * </p>
	 * 
	 * @return The start point of this snake segment
	 */
	public Pair<Integer> getStartPoint()
	{
		return new Pair<Integer>(this.XStart, this.YStart);
	}
	
	/**
	 * <p>
	 * Returns the mid point of this snake segment.
	 * </p>
	 * 
	 * @return The mid point of this snake segment
	 */
	public Pair<Integer> getMidPoint()
	{
		return new Pair<Integer>(this.getXMid(), this.getYMid());
	}
	
	/**
	 * <p>
	 * Returns the end point of this snake segment.
	 * </p>
	 * 
	 * @return The end point of this snake segment
	 */
	public Pair<Integer> getEndPoint()
	{
		return new Pair<Integer>(this.getXEnd(), this.getYEnd());
	}
	
	/**
	 * <p>
	 * Returns the x-position of the mid point for this snake segment.
	 * </p>
	 * 
	 * @return The x-position of the mid point
	 */
	public int getXMid()
	{
		if (this.IsForward)
			return this.XStart+this.ADeleted;
		return this.XStart - this.ADeleted;
	}
	
	/**
	 * <p>
	 * Returns the y-position of the mid point for this snake segment.
	 * </p>
	 * 
	 * @return The y-position of the mid point
	 */
	public int getYMid()
	{
		if (this.IsForward) 
			return this.YStart+this.BInserted;
		return this.YStart-this.BInserted;
	}
	
	/**
	 * <p>
	 * Returns the x-position of the end point for this snake segment.
	 * </p>
	 * 
	 * @return The x-position of the end point
	 */
	public int getXEnd()
	{
		if (this.IsForward) 
			return this.XStart+this.ADeleted+this.DiagonalLength;
		return this.XStart-this.ADeleted-this.DiagonalLength;
	}
	
	/**
	 * <p>
	 * Returns the y-position of the end point for this snake segment.
	 * </p>
	 * 
	 * @return The y-position of the end point
	 */
	public int getYEnd()
	{
		if (this.IsForward) 
			return this.YStart+this.BInserted+this.DiagonalLength;
		return this.YStart-this.BInserted-this.DiagonalLength;
	}
	
	/**
	 * <p>
	 * Returns if this snake segment is a middle point.
	 * </p>
	 * 
	 * @return true indicates that this segment is a middle point; false that it
	 *         is not a middle point
	 */
	public boolean isMiddlePoint()
	{
		return this.IsMiddle;
	}
	
	/**
	 * <p>
	 * Defines if this snake segment is a middle point.
	 * </p>
	 * 
	 * @param isMiddle
	 *            true indicates that this segment is a middle point, false that
	 *            it is not a middle point
	 */
	public void setMiddlePoint(boolean isMiddle)
	{
		this.IsMiddle = isMiddle;
	}
	
	/**
	 * <p>
	 * Returns the number of differences between the first and the second object
	 * in that trace and for that segment.
	 * </p>
	 * 
	 * @return The number of differences in that trace
	 */
	public int getD()
	{
		return this.D;
	}
	
	/**
	 * <p>
	 * Sets the d contours for this segment which correspond to the number of
	 * differences in that trace, irrespective of the number of equal elements.
	 * </p>
	 * 
	 * @param d
	 *            The number of differences in that trace
	 */
	public void setD(int d)
	{
		this.D = d;
	}
		
	@Override
	public String toString()
	{
		return "Snake " + ( IsForward ? "F" : "R" ) + ": ( " + XStart + ", " + YStart + " ) + " +
			( ADeleted > 0 ? "D( " : (BInserted > 0 ? "I( " : "( ") ) +
			+ ADeleted + ", " + BInserted +
			" ) + " + DiagonalLength + " -> ( " + this.getXEnd() + ", " + this.getYEnd() + " )" +
			" k=" + ( this.getXMid() - this.getYMid() );
	}

	/**
	 * <p>
	 * Removes the effects of a single insertion (down or up movement in the
	 * graph) if the x-position of the starting vertex equals <em>a0</em> and
	 * the y-position of the starting vertex equals the y-position of
	 * <em>b0</em> before the insertion.
	 * </p>
	 * 
	 * @param a0
	 *            The starting position in the array of elements from the first
	 *            object to compare
	 * @param N
	 *            The index of the last element from the first object to compare
	 * @param b0
	 *            The starting position in the array of elements from the second
	 *            object to compare
	 * @param M
	 *            The index of the last element from the second object to
	 *            compare
	 */
	private void RemoveStubs( int a0, int N, int b0, int M )
	{
		if ( this.IsForward )
		{
			if ( this.XStart == a0 && this.YStart == b0 - 1 && this.BInserted == 1 )
			{
				this.YStart = b0;
				this.BInserted = 0;
			}
		}
		else
		{
			if ( this.XStart == a0 + N && this.YStart == b0 + M + 1 && this.BInserted == 1 )
			{
				this.YStart = b0 + M;
				this.BInserted = 0;
			}
		}
	}
	
	/**
	 * <p>
	 * Combines two snakes of the same kind to reduce the number of returned
	 * snakes.
	 * </p>
	 * <p>
	 * A snake is of the same kind if both are in the same direction and if both
	 * have either a positive ADeleted field or a positive BInserted field, but
	 * not either a positive ADeleted and the other a positive BInserted field!
	 * Moreover, if the snake to append has a DiagonalLength > 0 it is not meant
	 * to be of the same kind and therefore should not be appended to this
	 * snake.
	 * </p>
	 * 
	 * @param snake
	 *            The snake to append to the current snake
	 * @return true if the snake could be appended to this snake; false
	 *         otherwise
	 */
	boolean append(Snake<T> snake)
	{
		if (((this.IsForward && this.DiagonalLength >= 0) 
				|| (!this.IsForward && snake.DiagonalLength >= 0)) && 
				(this.ADeleted > 0 && snake.ADeleted > 0 
						|| this.BInserted > 0 && snake.BInserted > 0))
		{
			this.ADeleted += snake.ADeleted;
			this.BInserted += snake.BInserted;
			
			this.DiagonalLength += snake.DiagonalLength;
			
			if (this.IsForward)
			{
				this.XStart = Math.min(this.XStart, snake.XStart);
				this.YStart = Math.min(this.YStart, snake.YStart);
			}
			else
			{
				this.XStart = Math.max(this.XStart, snake.XStart);
				this.YStart = Math.max(this.YStart, snake.YStart);
			}
			return true;
		}
		return false;
	}
}