package at.rovo.diff;

class SnakePair<T>
{
	public int D;
	public Snake<T> Forward;
	public Snake<T> Reverse;

	public SnakePair( int d, Snake<T> forward, Snake<T> reverse )
	{
		D = d;
		Forward = forward;
		Reverse = reverse;
	}
	
	public void setD(int d)
	{
		this.D = d;
	}
	
	public int getD()
	{
		return this.D;
	}
	
	public void setForward(Snake<T> forward)
	{
		this.Forward = forward;
	}
	
	public Snake<T> getForward()
	{
		return this.Forward;
	}
	
	public void setReverse(Snake<T> reverse)
	{
		this.Reverse = reverse;
	}
	
	public Snake<T> getReverse()
	{
		return this.Reverse;
	}
}