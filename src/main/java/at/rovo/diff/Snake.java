package at.rovo.diff;

public class Snake<T>
{
	public int XStart;
	public int YStart;
	public int ADeleted;
	public int BInserted;
	public int DiagonalLength;
	public boolean IsForward = true;
	public int DELTA = 0;

	public boolean IsMiddle = false;
	public int D = -1;
	
	Snake( boolean isForward, int delta )
	{
		IsForward = isForward;

		if ( !IsForward ) DELTA = delta;
	}
	
	Snake( int a0, int N, int b0, int M, boolean isForward, int xStart, int yStart, int aDeleted, int bInserted, int diagonal )
	{
		XStart = xStart;
		YStart = yStart;
		ADeleted = aDeleted;
		BInserted = bInserted;
		DiagonalLength = diagonal;
		IsForward = isForward;

		RemoveStubs( a0, N, b0, M );
	}

	Snake( int a0, int N, int b0, int M, boolean isForward, int xStart, int yStart, boolean down, int diagonal )
	{
		XStart = xStart;
		YStart = yStart;
		ADeleted = down ? 0 : 1;
		BInserted = down ? 1 : 0;
		DiagonalLength = diagonal;
		IsForward = isForward;

		RemoveStubs( a0, N, b0, M );
	}

	Snake( int a0, int N, int b0, int M, boolean forward, int delta, V V, int k, int d, T[] pa, T[] pb )
	{
		this.IsForward = forward;
		this.DELTA = delta;
		Calculate( V, k, d, pa, a0, N, pb, b0, M );
	}

	Snake<T> Calculate( V V, int k, int d, T[] pa, int a0, int N, T[] pb, int b0, int M )
	{
		if ( IsForward ) return CalculateForward( V, k, d, pa, a0, N, pb, b0, M );

		return CalculateBackward( V, k, d, pa, a0, N, pb, b0, M );
	}

	Snake<T> CalculateForward( V V, int k, int d, T[] pa, int a0, int N, T[] pb, int b0, int M )
	{
		boolean down = ( k == -d || ( k != d && V.getK(k - 1) < V.getK(k + 1) ) );

		int xStart = down ? V.getK(k + 1) : V.getK(k - 1);
		int yStart = xStart - ( down ? k + 1 : k - 1 );

		int xEnd = down ? xStart : xStart + 1;
		int yEnd = xEnd - k;

		int snake = 0;
		while ( xEnd < N && yEnd < M && pa[ xEnd + a0 ].equals(pb[ yEnd + b0 ]) ) { xEnd++; yEnd++; snake++; }

		XStart = xStart + a0;
		YStart = yStart + b0;
		ADeleted = down ? 0 : 1;
		BInserted = down ? 1 : 0;
		DiagonalLength = snake;

		RemoveStubs( a0, N, b0, M );

		return this;
	}

	Snake<T> CalculateBackward( V V, int k, int d, T[] pa, int a0, int N, T[] pb, int b0, int M )
	{
		boolean up = ( k == d + DELTA || ( k != -d + DELTA && V.getK(k - 1) < V.getK(k + 1) ) );

		int xStart = up ? V.getK(k - 1) : V.getK(k + 1);
		int yStart = xStart - ( up ? k - 1 : k + 1 );

		int xEnd = up ? xStart : xStart - 1;
		int yEnd = xEnd - k;

		int snake = 0;
		while ( xEnd > 0 && yEnd > 0 && pa[xEnd-1].equals(pb[yEnd-1]) ) { xEnd--; yEnd--; snake++; }

		XStart = xStart;
		YStart = yStart;
		ADeleted = up ? 0 : 1;
		BInserted = up ? 1 : 0;
		DiagonalLength = snake;

		RemoveStubs( a0, N, b0, M );

		return this;
	}
	
	public Pair<Integer> getStartPoint()
	{
		return new Pair<Integer>(XStart, YStart);
	}
	
	public Pair<Integer> getMidPoint()
	{
		return new Pair<Integer>(this.getXMid(), this.getYMid());
	}
	
	public Pair<Integer> getEndPoint()
	{
		return new Pair<Integer>(this.getXEnd(), this.getYEnd());
	}
	
	public int getXMid()
	{
		return (IsForward) ? XStart+ADeleted : XStart - ADeleted;
	}
	
	public int getYMid()
	{
		return (IsForward) ? YStart+BInserted : YStart-BInserted;
	}
	
	public int getXEnd()
	{
		return (IsForward) ? XStart+ADeleted+DiagonalLength : XStart-ADeleted-DiagonalLength;
	}
	
	public int getYEnd()
	{
		return (IsForward) ? YStart+BInserted+DiagonalLength : YStart-BInserted-DiagonalLength;
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

	void RemoveStubs( int a0, int N, int b0, int M )
	{
		if ( IsForward )
		{
			if ( XStart == a0 && YStart == b0 - 1 && BInserted == 1 )
			{
				YStart = b0;
				BInserted = 0;
			}
		}
		else
		{
			if ( XStart == a0 + N && YStart == b0 + M + 1 && BInserted == 1 )
			{
				YStart = b0 + M;
				BInserted = 0;
			}
		}
	}
}