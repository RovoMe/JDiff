package at.rovo.diff;

import java.util.List;

public class Results<T>
{
	public List<Snake<T>> Snakes;
	public List<V> ForwardVs;
	public List<V> ReverseVs;

	public Results(List<Snake<T>> snakes, List<V> forwardVs, List<V> reverseVs )
	{
		this.Snakes = snakes;
		this.ForwardVs = forwardVs;
		this.ReverseVs = reverseVs;
	}

	public Results( List<Snake<T>> snakes, boolean forward, List<V> Vs )
	{
		Snakes = snakes;

		if ( forward ) 
			this.ForwardVs = Vs; 
		else 
			this.ReverseVs = Vs;
	}
	
	public List<Snake<T>> getSnakes()
	{
		return this.Snakes;
	}
	
	protected void setSnakes(List<Snake<T>> Snakes)
	{
		this.Snakes = Snakes;
	}
	
	public List<V> getForwardVs()
	{
		return this.ForwardVs;
	}
	
	protected void setForwardVs(List<V> ForwardVs)
	{
		this.ForwardVs = ForwardVs;
	}
	
	public List<V> getReverseVs()
	{
		return this.ReverseVs;
	}
	
	protected void setReverseVs(List<V> ReverseVs)
	{
		this.ReverseVs = ReverseVs;
	}
}