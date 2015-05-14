package at.rovo.diff;

/**
 * Utility class that hold both directional calculations for the segment the snake is used for.
 *
 * @param <T>
 * 		The type of the element the snakes will hold
 *
 * @author Roman Vottner
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
	 * Initializes a new instance of this utility class.
	 *
	 * @param d
	 * 		The number of differences for both segment calculations
	 * @param forward
	 * 		The segment calculated in a forward direction
	 * @param reverse
	 * 		The segment calculated in a backward direction
	 */
	public SnakePair(int d, Snake<T> forward, Snake<T> reverse)
	{
		this.D = d;
		this.Forward = forward;
		this.Reverse = reverse;
	}

	/**
	 * Sets the number of differences for both calculation directions.
	 *
	 * @param d
	 * 		The number of differences for both calculation directions
	 */
	public void setD(int d)
	{
		this.D = d;
	}

	/**
	 * Returns the number of differences for both calculation directions.
	 * <p>
	 * A value of 0 indicates that compared elements from the first and the second object are equal. A value of 1
	 * indicates either an insertion from the second object or a deletion from the first object.
	 * <p>
	 * Moreover, a value of 0 must be a reverse segment, while a value of 1 results from a forward segment.
	 *
	 * @return The number of differences for both calculation directions
	 */
	public int getD()
	{
		return this.D;
	}

	/**
	 * Sets the new segment calculated in a forward direction.
	 *
	 * @param forward
	 * 		The segment calculated in forward direction
	 */
	public void setForward(Snake<T> forward)
	{
		this.Forward = forward;
	}

	/**
	 * Returns the segment which was calculated in forward direction.
	 *
	 * @return The segment calculated in forward direction
	 */
	public Snake<T> getForward()
	{
		return this.Forward;
	}

	/**
	 * Sets the new segment calculated in a backward direction.
	 *
	 * @param reverse
	 * 		The segment calculated in backward direction
	 */
	public void setReverse(Snake<T> reverse)
	{
		this.Reverse = reverse;
	}

	/**
	 * Returns the segment which was calculated in backward direction.
	 *
	 * @return The segment calculated in backward direction
	 */
	public Snake<T> getReverse()
	{
		return this.Reverse;
	}
}