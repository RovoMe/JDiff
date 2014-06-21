package at.rovo.diff;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.rovo.common.UrlReader;
import at.rovo.parser.Token;

/**
 * <p>
 * This util class provides some helper functions to deal with results returned
 * by the diff algorithm more easily.
 * </p>
 * 
 * @author Roman Vottner
 */
@SuppressWarnings("unused")
public class DiffUtil
{
	/** The logger of this class **/
	private final static Logger LOG = LogManager.getLogger(DiffUtil.class);

	/** prevent initializations of the util class **/
	private DiffUtil()
	{
		
	}
	
	/**
	 * <p>
	 * Returns the differences of two compared files as a {@link List} of 
	 * {@link Result} objects. A result object is created for every found 
	 * difference between the first and second file.
	 * </p>
	 * 
	 * @param res
	 *            The result returned by the comparison algorithm
	 * @param patterns
	 *            The source files which got compared with each other
	 */
	public static <E> List<Result<E>> getDifferences(Results<E> res,
			List<E[]> patterns)
	{
		List<Result<E>> results = new ArrayList<>();
		for (Snake<E> snake : res.getSnakes())
		{
			Result<E> result = new Result<>(snake.IsForward);
			if (LOG.isTraceEnabled())
				LOG.trace("Snake: {}", snake);
			
			if (snake.IsForward)
			{
				LOG.trace("Following forward snake");
				results.add(getForward(patterns, snake, result));
			}
			else
			{
				LOG.trace("Following backward snake");
				results.add(getBackward(patterns, snake, result));
			}
		}
		
		return results;
	}
	
	/**
	 * <p>
	 * Prints the differences of two files in forward direction.
	 * </p>
	 * 
	 * @param patterns
	 *            The source files which got compared with each other
	 * @param snake
	 *            The snake containing the differences between the last changed
	 *            tokens and the current token that has been changed
	 */
	private static <E> Result<E> getForward(List<E[]> patterns, Snake<E> snake, Result<E> result)
	{
		// X is the position in the first file
		int Xstart = snake.getStartPoint().X();
		int Xend = snake.getEndPoint().X();

		// Y is the position in the second file
		int Ystart = snake.getStartPoint().Y();
		int Yend = snake.getEndPoint().Y();
			
		// tokens that got deleted from the first file
		if (snake.ADeleted > 0) 
		{
			StringBuilder sb = new StringBuilder();
			sb.append("D: ");
			for (int pos = Xstart; pos < Xend - snake.DiagonalLength; pos++)
			{
				result.addDeletedToken(patterns.get(0)[pos]);
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that got inserted from the second file
		if (snake.BInserted > 0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("I: ");
			for (int pos = Ystart; pos < Yend - snake.DiagonalLength; pos++)
			{
				result.addInsertedToken(patterns.get(1)[pos]);
				sb.append(patterns.get(1)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that are equal in both files
		if (snake.DiagonalLength > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (int pos = Xstart + snake.ADeleted; pos < Xend; pos++)
			{
				result.addRegularToken(patterns.get(0)[pos]);
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		
		return result;
	}
	
	/**
	 * <p>Prints the differences of two files in backward direction.</p>
	 * 
	 * @param patterns
	 *            The source files which got compared with each other
	 * @param snake
	 *            The snake containing the differences between the last changed
	 *            tokens and the current token that has been changed
	 */
	private static <E> Result<E> getBackward(List<E[]> patterns, Snake<E> snake, Result<E> result)
	{		
		// X is the position in the first file
		int Xstart = snake.getEndPoint().X();
		int Xend = snake.getStartPoint().X();

		// Y is the position in the second file
		int Ystart = snake.getEndPoint().Y();
		int Yend = snake.getStartPoint().Y();

		// tokens that are equal in both files
		if (snake.DiagonalLength > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (int pos = Xstart; pos < Xend - snake.ADeleted; pos++)
			{
				result.addRegularToken(patterns.get(0)[pos]);
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that got deleted from the first file
		if (snake.ADeleted > 0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("D: ");
			for (int pos = Xstart + snake.DiagonalLength; pos < Xend; pos++)
			{
				result.addDeletedToken(patterns.get(0)[pos]);
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that got inserted from the second file
		if (snake.BInserted > 0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("I: ");
			for (int pos = Ystart + snake.DiagonalLength; pos < Yend; pos++)
			{
				result.addInsertedToken(patterns.get(1)[pos]);
				sb.append(patterns.get(1)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		
		return result;
	}
	
	/**
	 * <p>
	 * Prints the differences of two compared files to the console where lines
	 * starting with <em>D</em> mark a deletion of a token from the first file
	 * while lines starting with <em>I</em> indicate an insertion of a token
	 * from the second file at this position.
	 * </p>
	 * 
	 * @param res
	 *            The result returned by the comparison algorithm
	 * @param patterns
	 *            The source files which got compared with each other
	 */
	public static void printDifferences(Results<Token> res,
			List<Token[]> patterns)
	{
		for (Snake<Token> snake : res.getSnakes())
		{
			if (LOG.isTraceEnabled())
				LOG.trace("Snake: {}", snake);
			
			if (snake.IsForward)
			{
				LOG.trace("Following forward snake");
				printForward(patterns, snake);
			}
			else
			{
				LOG.trace("Following backward snake");
				printBackward(patterns, snake);
			}
		}
	}
	
	/**
	 * <p>
	 * Prints the differences of two files in forward direction.
	 * </p>
	 * 
	 * @param patterns
	 *            The source files which got compared with each other
	 * @param snake
	 *            The snake containing the differences between the last changed
	 *            tokens and the current token that has been changed
	 */
	private static void printForward(List<Token[]> patterns, Snake<Token> snake)
	{
		// X is the position in the first file
		int Xstart = snake.getStartPoint().X();
		int Xend = snake.getEndPoint().X();

		// Y is the position in the second file
		int Ystart = snake.getStartPoint().Y();
		int Yend = snake.getEndPoint().Y();
			
		// tokens that got deleted from the first file
		if (snake.ADeleted > 0) 
		{
			StringBuilder sb = new StringBuilder();
			sb.append("D: ");
			for (int pos = Xstart; pos < Xend - snake.DiagonalLength; pos++)
			{
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that got inserted from the second file
		if (snake.BInserted > 0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("I: ");
			for (int pos = Ystart; pos < Yend - snake.DiagonalLength; pos++)
			{
				sb.append(patterns.get(1)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that are equal in both files
		if (snake.DiagonalLength > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (int pos = Xstart + snake.ADeleted; pos < Xend; pos++)
			{
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
	}
	
	/**
	 * <p>Prints the differences of two files in backward direction.</p>
	 * 
	 * @param patterns
	 *            The source files which got compared with each other
	 * @param snake
	 *            The snake containing the differences between the last changed
	 *            tokens and the current token that has been changed
	 */
	private static void printBackward(List<Token[]> patterns, Snake<Token> snake)
	{		
		// X is the position in the first file
		int Xstart = snake.getEndPoint().X();
		int Xend = snake.getStartPoint().X();

		// Y is the position in the second file
		int Ystart = snake.getEndPoint().Y();
		int Yend = snake.getStartPoint().Y();

		// tokens that are equal in both files
		if (snake.DiagonalLength > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (int pos = Xstart; pos < Xend - snake.ADeleted; pos++)
			{
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that got deleted from the first file
		if (snake.ADeleted > 0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("D: ");
			for (int pos = Xstart + snake.DiagonalLength; pos < Xend; pos++)
			{
				sb.append(patterns.get(0)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
		// tokens that got inserted from the second file
		if (snake.BInserted > 0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("I: ");
			for (int pos = Ystart + snake.DiagonalLength; pos < Yend; pos++)
			{
				sb.append(patterns.get(1)[pos]);
				sb.append(" ");
			}
			LOG.debug(sb.toString());
		}
	}
	
	/**
	 * <p>
	 * Reads a file and stores its content in a {@link String} so it can be
	 * processed.
	 * </p>
	 * 
	 * @param file
	 *            The file to read
	 * @return The fully read file stored in a {@link String}
	 * @throws IOException
	 *             If an error occurs while reading the file
	 */
	public static String readFile(String file) throws IOException
	{
		LOG.trace("Reading file '{}'", file);
		
		// if the file is a remote web page, load the page and return its 
		// content
		if (file.startsWith("http://"))
			return new UrlReader().readPage(file);
		
		// else read the file from the local storage
		String result;
		try(BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			LOG.trace("File-Content: ");
			while (line != null)
			{
				sb.append(line);
				LOG.trace(line);
				sb.append("\n");
				line = br.readLine();
			}
			result = sb.toString();
		}

		LOG.trace("Reading file done");
		return result;
	}
}
