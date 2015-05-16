package at.rovo.diff;

@SuppressWarnings("unused")
public class Pair<T>
{
    private T t1 = null;
    private T t2 = null;

    public Pair(T t1, T t2)
    {
        this.t1 = t1;
        this.t2 = t2;
    }

    public void setFirst(T t1)
    {
        this.t1 = t1;
    }

    public void setLast(T t2)
    {
        this.t2 = t2;
    }

    public T getFirst()
    {
        return this.t1;
    }

    public T getLast()
    {
        return this.t2;
    }

    public void X(T t1)
    {
        this.t1 = t1;
    }

    public void Y(T t2)
    {
        this.t2 = t2;
    }

    public T X()
    {
        return this.t1;
    }

    public T Y()
    {
        return this.t2;
    }
}
