package at.rovo.diff;

/**
 * <p>Utility class that hold both directional calculations for the segment the
 * snake is used for.</p>
 * 
 * @author Roman Vottner
 * @param <T> The type of the element the snakes will hold
 */
class SnakePair<T>
{
	/** The number of differences for both segment calculations **/
	private int D;
	/** The segment calculated in forward direction **/
	private Snake<T> Forward;
	/** The segment calculated in backward direction **/
	private Snake<T> Reverse;

	/**
	 * <p>Initializes a new instance of this utility class.</p>
	 * 
	 * @param d
	 * @param forward The segment calculated in a forward direction
	 * @param reverse The segment calculated in a backward direction
	 */
	public SnakePair( int d, Snake<T> forward, Snake<T> reverse )
	{
		this.D = d;
		this.Forward = forward;
		this.Reverse = reverse;
	}
	
	/**
	 * <p>Sets the number of differences for both calculation directions.</p>
	 * 
	 * @param d The number of differences for both calculation directions
	 */
	public void setD(int d)
	{
		this.D = d;
	}
	
	/**
	 * <p>Returns the number of differences for both calculation directions.</p>
	 * <p>A value of 0 indicates that compared elements from the first and the
	 * second object are equal. A value of 1 indicates either an insertion from 
	 * the second object or a deletion from the first object.</p>
	 * <p>Moreover, a value of 0 must be a reverse segment, while a value of 1 
	 * results from a forward segment.</p>
	 * 
	 * @return The number of differences for both calculation directions
	 */
	public int getD()
	{
		return this.D;
	}
	
	/**
	 * <p>Sets the new segment calculated in a forward direction.</p>
	 * 
	 * @param forward The segment calculated in forward direction
	 */
	public void setForward(Snake<T> forward)
	{
		this.Forward = forward;
	}
	
	/**
	 * <p>Returns the segment which was calculated in forward direction.</p>
	 * 
	 * @return The segment calculated in forward direction
	 */
	public Snake<T> getForward()
	{
		return this.Forward;
	}
	
	/**
	 * <p>Sets the new segment calculated in a backward direction.</p>
	 * 
	 * @param forward The segment calculated in backward direction
	 */
	public void setReverse(Snake<T> reverse)
	{
		this.Reverse = reverse;
	}
	
	/**
	 * <p>Returns the segment which was calculated in backward direction.</p>
	 * 
	 * @return The segment calculated in backward direction
	 */
	public Snake<T> getReverse()
	{
		return this.Reverse;
	}
}