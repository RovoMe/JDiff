package at.rovo.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import at.rovo.UrlReader;
import at.rovo.parser.ParseResult;
import at.rovo.parser.Parser;
import at.rovo.parser.Token;
import at.rovo.diff.Results;
import at.rovo.diff.Snake;

/**
 * <p>
 * This class simple compares two HTML files and prints the differences in both
 * files to the console where a line starting with <em>D</em> marks a deletion
 * of a token (HTML tag or word) from the first file and a line starting with
 * <em>I</em> marks an insertion of a token from the second file at this
 * position.
 * </p>
 * 
 * @author Roman Vottner
 */
public class DiffTest
{
	/**
	 * <p>
	 * Entrance point to the application. Reads in two files (
	 * <em>testPattern1.html</em> and <em>testPattern2.html</em>) from the
	 * resource directory the test-classpath is pointing to and compares these
	 * two files with each other and calls
	 * {@link #printDifferences(Results, List)} to print the differences of both
	 * files to the console.
	 * </p>
	 * 
	 * @param args
	 *            Some arguments passed to the application. Note that this
	 *            example application does not check for any passed parameters
	 * @throws Exception
	 *             Thrown if either an error is thrown while trying to read one
	 *             of the files to compare or while comparing those files
	 */
	public static void main(String[] args) throws Exception
	{
		String[] pages = new String[] 
		{
				"testPattern1.html",
				"testPattern2.html" 
		};
		List<Token[]> patterns = new ArrayList<Token[]>();

		for (String pattern : pages)
		{
			URL url = DiffTest.class.getResource("/" + pattern);
			String html = readFile(url.toURI().getPath());
			Parser parser = new Parser();
			ParseResult res = parser.tokenize(html, false);
			List<Token> p = res.getParsedTokens();

			Token[] tmp = new Token[0];
			patterns.add(p.toArray(tmp));
		}

		Results<Token> res;
//		res = at.rovo.diff.GreedyDiff.Compare(patterns.get(0),patterns.get(1), false);
		res = at.rovo.diff.LinearDiff.Compare(patterns.get(0),patterns.get(1));
		
		if (res.getSnakes() != null)
		{
			printDifferences(res, patterns);
		}
		else
			System.out.println("No snakes found!");
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
			// System.out.println(
			//   "X.start "+snake.getStartPoint().X()
			// +" Y.start "+snake.getStartPoint().Y()
			// +" X.end "+snake.getEndPoint().X()
			// +" Y.end "+snake.getEndPoint().Y()
			// +" X.mid "+snake.getMidPoint().X()
			// +" Y.mid "+snake.getMidPoint().Y());
			// System.out.println(snake);

			if (snake.IsForward)
			{
				printForward(patterns, snake);
			}
			else
			{
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
			for (int pos = Xstart; pos < Xend - snake.DiagonalLength; pos++)
			{
				System.out.println("D: " + patterns.get(0)[pos]);
			}
		}
		// tokens that got inserted from the second file
		if (snake.BInserted > 0)
		{
			for (int pos = Ystart; pos < Yend - snake.DiagonalLength; pos++)
			{
				System.out.println("I: " + patterns.get(1)[pos]);
			}
		}
		// tokens that are equal in both files
		if (snake.DiagonalLength > 0)
		{
			for (int pos = Xstart + snake.ADeleted; pos < Xend; pos++)
			{
				System.out.print(patterns.get(0)[pos] + " ");
			}
			System.out.println();
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
			for (int pos = Xstart; pos < Xend - snake.ADeleted; pos++)
			{
				System.out.print(patterns.get(0)[pos] + " ");
			}
			System.out.println();
		}
		// tokens that got deleted from the first file
		if (snake.ADeleted > 0)
		{
			for (int pos = Xstart + snake.DiagonalLength; pos < Xend; pos++)
			{
				System.out.println("D: " + patterns.get(0)[pos]);
			}
		}
		// tokens that got inserted from the second file
		if (snake.BInserted > 0)
		{
			for (int pos = Ystart + snake.DiagonalLength; pos < Yend; pos++)
			{
				System.out.println("I: " + patterns.get(1)[pos]);
			}
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
	private static String readFile(String file) throws IOException
	{
		System.out.print("Reading file '" + file + "'");
		if (file.startsWith("http://"))
			return new UrlReader().readPage(file);
		String result = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		try
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null)
			{
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			result = sb.toString();
		}
		finally
		{
			br.close();
		}
		System.out.println(" ... DONE");
		return result;
	}
}