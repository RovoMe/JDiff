package at.rovo.diff;

/**
 * This class is a helper class to store the actual x-positions of end-points on a k-line.
 * <p>
 * It further provides a method to calculate the y-position for end-points based on the x-position and the k-line the
 * end point is lying on.
 *
 * @author Roman Vottner
 */
class V
{
	/** Comparison direction flag **/
	private boolean IsForward;
	/** Length of the first input string **/
	private int N;
	/** Length of the second input string **/
	private int M;

	/** The maximum number of end points to store **/
	private int max;
	/**
	 * As the length of A and B can be different, the k lines of the forward and reverse algorithms can be different. It
	 * is useful to isolate this as a variable
	 **/
	private int delta;
	/** Stores the actual x-position **/
	private int[] array;

	/**
	 * Stores the x-position of an end point for a given k-line.
	 *
	 * @param k
	 * 		The k-line to store the position for
	 * @param value
	 * 		The x-position of the end point
	 */
	public void setK(int k, int value)
	{
		this.array[k - this.delta + this.max] = value;
	}

	/**
	 * Returns the x-position for an end point for a given k-line
	 *
	 * @param k
	 * 		The k-line to recall the x-position for
	 *
	 * @return The x-position of an end point
	 */
	public int getK(int k)
	{
		return array[k - this.delta + this.max];
	}

	/**
	 * Calculates the y-position of an end point based on the x-position and the k-line.
	 *
	 * @param k
	 * 		The k-line the end point is on
	 *
	 * @return The y-position of the end point
	 */
	public int Y(int k)
	{
		return this.getK(k) - k;
	}

	/**
	 * Package private constructor
	 */
	V()
	{
	}

	/**
	 * Initializes a new instance of this helper class.
	 *
	 * @param n
	 * 		The length of the first object which gets compared to the second
	 * @param m
	 * 		The length of the second object which gets compared to the first
	 * @param forward
	 * 		The comparison direction; True if forward, false otherwise
	 * @param linear
	 * 		True if a linear comparison should be used for comparing two objects or the greedy method (false)
	 */
	V(int n, int m, boolean forward, boolean linear)
	{
		this.IsForward = forward;
		this.N = n;
		this.M = m;

		// calculate the maximum number of end points to store
		this.max = (linear ? (n + m) / 2 + 1 : n + m);
		if (this.max <= 0)
		{
			this.max = 1;
		}

		// as each point on a k-line can either come from a down or right move
		// there can only be two successor points for each end-point
		this.array = new int[2 * this.max + 1];

		InitStub(n, m);
	}

	/**
	 * Returns the comparison direction.
	 *
	 * @return True if the comparison direction is forward, false otherwise
	 */
	public boolean isForward()
	{
		return this.IsForward;
	}

	/**
	 * Returns the length of the second object which gets compared to the first.
	 *
	 * @return The length of the second object
	 */
	public int getM()
	{
		return this.M;
	}

	/**
	 * Returns the length of the first object which gets compared to the second.
	 *
	 * @return The length of the first object
	 */
	public int getN()
	{
		return this.N;
	}

	/**
	 * Initializes the k-line based on the comparison direction.
	 *
	 * @param n
	 * 		The length of the first object to compare
	 * @param m
	 * 		The length of the second object to compare
	 */
	public void InitStub(int n, int m)
	{
		if (this.IsForward)
		{
			this.setK(1, 0); // stub for forward
		}
		else
		{
			this.delta = n - m;
			this.setK(n - m - 1, n); // stub for backward
		}
	}

	/**
	 * Creates a new deep copy of this object.
	 *
	 * @param d
	 * 		Number of differences for the same trace
	 * @param isForward
	 * 		The comparison direction; True if forward, false otherwise
	 * @param delta
	 * 		Keeps track of the differences between the first and the second object to compare as they may differ in length
	 *
	 * @return The deep copy of this object
	 *
	 * @throws Exception
	 * 		If d > the maximum number of end points to store
	 */
	public V CreateCopy(int d, boolean isForward, int delta) throws Exception
	{
		if (d == 0)
		{
			d++;
		}

		V o = new V();

		o.IsForward = isForward;
		o.max = d;
		if (!isForward)
		{
			o.delta = delta;
		}
		o.array = new int[2 * d + 1];

		if (d <= this.max)
		{
			System.arraycopy(this.array, (this.max - this.delta) - (o.max - o.delta), o.array, 0, o.array.length);
		}
		else
		{
			throw new Exception("V.CreateCopy");
		}

		return o;
	}

	@Override
	public String toString()
	{
		return "V " + this.array.length + " [ " + (this.delta - this.max) + " .. " + this.delta + " .. " +
			   (this.delta + this.max) + " ]";
	}
}