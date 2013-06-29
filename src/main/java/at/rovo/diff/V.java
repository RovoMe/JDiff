package at.rovo.diff;

/**
 * <p>This class is a helper class to store the actual x-positions of end-points
 * on a k-line.</p>
 * <p>It further provides a method to calculate the y-position for end-points
 * based on the x-position and the k-line the end point is lying on.</p>
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
	private int _Max;
	/** As the length of A and B can be different, the k lines of the forward 
	 * and reverse algorithms can be different. It is useful to isolate this as
	 * a variable **/
	private int _Delta;
	/** Stores the actual x-position **/
	private int[] _Array;
	
	/**
	 * <p>Stores the x-position of an end point for a given k-line.</p>
	 * 
	 * @param k The k-line to store the position for
	 * @param value The x-position of the end point
	 */
	public void setK(int k, int value)
	{
		this._Array[k-this._Delta+this._Max] = value;
	}
	
	/**
	 * <p>Returns the x-position for an end point for a given k-line</p>
	 * 
	 * @param k The k-line to recall the x-position for
	 * @return The x-position of an end point
	 */
	public int getK(int k)
	{
		return _Array[k-this._Delta+this._Max];
	}
	
	/**
	 * <p>Calculates the y-position of an end point based on the x-position and
	 * the k-line.</p>
	 * 
	 * @param k The k-line the end point is on
	 * @return The y-position of the end point
	 */
	public int Y( int k ) { return this.getK(k) - k; }

	/**
	 * <p>Package private constructor</p>
	 */
	V() { }

	/**
	 * <p>Initializes a new instance of this helper class.</p>
	 * 
	 * @param n The length of the first object which gets compared to the second
	 * @param m The length of the second object which gets compared to the first
	 * @param forward The comparison direction; True if forward, false otherwise
	 * @param linear True if a linear comparison should be used for comparing
	 *               two objects or the greedy method (false)
	 */
	V( int n, int m, boolean forward, boolean linear )
	{
//		Debug.Assert( n >= 0 && m >= 0, "V.ctor N:" + n + " M:" + m );

		IsForward = forward;
		N = n;
		M = m;

		// calculate the maximum number of end points to store
		_Max = ( linear ? ( n + m ) / 2 + 1 : n + m );
		if ( _Max <= 0 ) _Max = 1;

		// as each point on a k-line can either come from a down or right move
		// there can only be two successor points for each end-point
		_Array = new int[ 2 * _Max + 1 ];

		InitStub( n, m, _Max );
	}
	
	/**
	 * <p>Returns the comparison direction.</p>
	 * 
	 * @return True if the comparison direction is forward, false otherwise
	 */
	public boolean isForward()
	{
		return this.IsForward;
	}
	
	/**
	 * <p>Returns the length of the second object which gets compared to the
	 * first.</p>
	 * 
	 * @return The length of the second object
	 */
	public int getM()
	{
		return this.M;
	}
	
	/**
	 * <p>Returns the length of the first object which gets compared to the
	 * second.</p>
	 * 
	 * @return The length of the first object
	 */
	public int getN()
	{
		return this.N;
	}

	public void InitStub( int n, int m, int max )
	{
		if ( IsForward ) this.setK(1, 0); // stub for forward
		else
		{
			_Delta = n - m;
			this.setK(n - m - 1, n); // stub for backward
		}
	}


	public V CreateCopy( int d, boolean isForward, int delta ) throws Exception
	{
//		Debug.Assert( !( isForward && delta != 0 ) );

		if ( d == 0 ) d++;

		V o = new V();

		o.IsForward = isForward;
		o._Max = d;
		if ( !isForward ) o._Delta = delta;
		o._Array = new int[ 2 * d + 1 ];

		if ( d <= _Max )
		{
			System.arraycopy(this._Array, (this._Max-this._Delta)-(o._Max-o._Delta), o._Array, 0, o._Array.length);
		}
		else
		{
			throw new Exception( "V.CreateCopy" );
		}

		return o;
	}

	@Override
	public String toString()
	{
		return "V " + _Array.length + " [ " + ( _Delta - _Max ) + " .. " + _Delta + " .. " + ( _Delta + _Max ) + " ]";
	}
}