package at.rovo.diff;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class to return the differences in a more convenient way.
 *
 * @param <E>
 *         The type of the objects that are compared
 *
 * @author Roman Vottner
 */
public class Result<E>
{
    /** Deleted tokens from document 1 **/
    private final List<E> deletedTokens = new ArrayList<>();
    /** Inserted tokens from document 2 **/
    private final List<E> insertedTokens = new ArrayList<>();
    /** Tokens that are equal in both documents **/
    private final List<E> sameTokens = new ArrayList<>();

    private boolean isForwardDirection = true;

    public Result(boolean isForwardDirection)
    {
        this.isForwardDirection = isForwardDirection;
    }

    public void addDeletedToken(E delToken)
    {
        this.deletedTokens.add(delToken);
    }

    public void addInsertedToken(E insToken)
    {
        this.insertedTokens.add(insToken);
    }

    public void addRegularToken(E regToken)
    {
        this.sameTokens.add(regToken);
    }

    public List<E> getDeletedTokens()
    {
        return this.deletedTokens;
    }

    public List<E> getInsertedTokens()
    {
        return this.insertedTokens;
    }

    public List<E> getRegularTokens()
    {
        return this.sameTokens;
    }

    public boolean isForwardDirection()
    {
        return this.isForwardDirection;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (this.isForwardDirection)
        {
            if (!this.deletedTokens.isEmpty())
            {
                sb.append("Deleted: ");
                sb.append(printHelp(this.deletedTokens));
                sb.append("\n");
            }
            if (!this.insertedTokens.isEmpty())
            {
                sb.append("Inserted: ");
                sb.append(printHelp(this.insertedTokens));
                sb.append("\n");
            }
        }
        if (!this.sameTokens.isEmpty())
        {
            sb.append("Same: ");
            sb.append(printHelp(this.sameTokens));
            sb.append("\n");
        }
        if (!this.isForwardDirection)
        {
            if (!this.deletedTokens.isEmpty())
            {
                sb.append("Deleted: ");
                sb.append(printHelp(this.deletedTokens));
                sb.append("\n");
            }
            if (!this.insertedTokens.isEmpty())
            {
                sb.append("Inserted: ");
                sb.append(printHelp(this.insertedTokens));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String printHelp(List<E> list)
    {
        StringBuilder sb = new StringBuilder();
        for (E item : list)
        {
            sb.append(item);
            sb.append(" ");
        }
        return sb.toString();
    }
}
