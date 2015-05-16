package at.rovo.test;

import at.rovo.diff.DiffUtil;
import at.rovo.diff.Result;
import at.rovo.diff.Results;
import at.rovo.parser.ParseResult;
import at.rovo.parser.Parser;
import at.rovo.parser.Token;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class simple compares two HTML files and prints the differences in both files to the console where a line
 * starting with <em>D</em> marks a deletion of a token (HTML tag or word) from the first file and a line starting with
 * <em>I</em> marks an insertion of a token from the second file at this position.
 *
 * @author Roman Vottner
 */
public class DiffTest
{
    /** The logger of this class **/
    private static Logger LOG = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Reads in two files (<em>testPattern1.html</em> and <em>testPattern2.html</em>) from the resource directory the
     * test-classpath is pointing to and compares these two files with each other and calls {@link
     * DiffUtil#getDifferences(Results, List)} to return the differences of both files.
     *
     * @throws Exception
     *         Thrown if either an error is thrown while trying to read one of the files to compare or while comparing
     *         those files
     */
    @Test
    public void testDifferencesGreedyDiffForwardDirection() throws Exception
    {
        String[] pages = new String[] {"testPattern1.html", "testPattern2.html"};
        List<Token[]> patterns = new ArrayList<>();

        for (String pattern : pages)
        {
            URL url = DiffTest.class.getResource("/" + pattern);
            String html = DiffUtil.readFile(url.toURI().getPath());
            Parser parser = new Parser();
            ParseResult res = parser.tokenize(html, false);
            List<Token> p = res.getParsedTokens();

            Token[] tmp = new Token[0];
            patterns.add(p.toArray(tmp));
        }

        Results<Token> res;
        res = at.rovo.diff.GreedyDiff.Compare(patterns.get(0), patterns.get(1), true);

        if (res.getSnakes() != null)
        {
            //DiffUtil.printDifferences(res, patterns);
            List<Result<Token>> diffResults = DiffUtil.getDifferences(res, patterns);
            Assert.assertEquals("Unexpected number of diff results found", 5, diffResults.size());
            // compare the first result object which only should contain the
            // same tokens for both files
            Assert.assertTrue("Forward direction expected", diffResults.get(0).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 1. diff result object",
                              diffResults.get(0).getDeletedTokens().isEmpty());
            Assert.assertTrue("No inserted tokens expected for the 1. diff result object",
                              diffResults.get(0).getInsertedTokens().isEmpty());
            Assert.assertEquals("Expected 12 equal tokens for the 1. diff result object", 12,
                                diffResults.get(0).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found",
                                "Same: <html> <head> <title> Test Title </title> </head> <body> <p> Simple Text </p> \n",
                                diffResults.get(0).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(1).isForwardDirection());
            Assert.assertEquals("Expected 2 deleted tokens for the 2. diff result object", 2,
                                diffResults.get(1).getDeletedTokens().size());
            Assert.assertTrue("No inserted tokens expected for the 2. diff result object",
                              diffResults.get(1).getInsertedTokens().isEmpty());
            Assert.assertTrue("No equal tokens expected for the 2. diff result object",
                              diffResults.get(1).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 2. diff result object", "Deleted: <a> Sister \n",
                                diffResults.get(1).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(2).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 3. diff result object",
                              diffResults.get(2).getDeletedTokens().isEmpty());
            Assert.assertEquals("Expected 2 inserted tokens for the 3. diff result object", 2,
                                diffResults.get(2).getInsertedTokens().size());
            Assert.assertEquals("Expected 1 similar token for the 3. diff result object", 1,
                                diffResults.get(2).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found for the 3. diff result object",
                                "Inserted: <p> This \nSame: page \n", diffResults.get(2).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(3).isForwardDirection());
            Assert.assertEquals("Expected 1 deleted token for the 4. diff result object", 1,
                                diffResults.get(3).getDeletedTokens().size());
            Assert.assertTrue("No inserted tokens expected for the 4. diff result object",
                              diffResults.get(3).getInsertedTokens().isEmpty());
            Assert.assertTrue("No equal tokens expected for the 4. diff result object",
                              diffResults.get(3).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 4. diff result object", "Deleted: </a> \n",
                                diffResults.get(3).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(4).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 5. diff result object",
                              diffResults.get(4).getDeletedTokens().isEmpty());
            Assert.assertEquals("Expected 5 inserted tokens for the 5. diff result object", 5,
                                diffResults.get(4).getInsertedTokens().size());
            Assert.assertEquals("Expected 2 equal tokens for the 5. diff result object", 2,
                                diffResults.get(4).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found for the 5. diff result object",
                                "Inserted: has an additional paragraph </p> \nSame: </body> </html> \n",
                                diffResults.get(4).toString());
        }
        else
        {
            LOG.error("No snakes found!");
            Assert.fail("No snakes found!");
        }
    }

    @Test
    public void testDifferencesGreedyDiffBackwardDirection() throws Exception
    {
        String[] pages = new String[] {"testPattern1.html", "testPattern2.html"};
        List<Token[]> patterns = new ArrayList<>();

        for (String pattern : pages)
        {
            URL url = DiffTest.class.getResource("/" + pattern);
            String html = DiffUtil.readFile(url.toURI().getPath());
            Parser parser = new Parser();
            ParseResult res = parser.tokenize(html, false);
            List<Token> p = res.getParsedTokens();

            Token[] tmp = new Token[0];
            patterns.add(p.toArray(tmp));
        }

        Results<Token> res;
        res = at.rovo.diff.GreedyDiff.Compare(patterns.get(0), patterns.get(1), false);

        if (res.getSnakes() != null)
        {
            List<Result<Token>> diffResults = DiffUtil.getDifferences(res, patterns);
            Assert.assertEquals("Unexpected number of diff results found", 5, diffResults.size());
            // compare the first result object which only should contain the
            // same tokens for both files
            Assert.assertFalse("Backward direction expected", diffResults.get(0).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 1. diff result object",
                              diffResults.get(0).getDeletedTokens().isEmpty());
            Assert.assertEquals("Expected 2 equal tokens for the 1. diff result object", 2,
                                diffResults.get(0).getInsertedTokens().size());
            Assert.assertEquals("Expected 12 equal tokens for the 1. diff result object", 12,
                                diffResults.get(0).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found",
                                "Same: <html> <head> <title> Test Title </title> </head> <body> <p> Simple Text </p> \nInserted: <p> This \n",
                                diffResults.get(0).toString());

            Assert.assertFalse("Backward direction expected", diffResults.get(1).isForwardDirection());
            Assert.assertEquals("Expected 2 deleted tokens for the 2. diff result object", 2,
                                diffResults.get(1).getDeletedTokens().size());
            Assert.assertTrue("No inserted tokens expected for the 2. diff result object",
                              diffResults.get(1).getInsertedTokens().isEmpty());
            Assert.assertTrue("No equal tokens expected for the 2. diff result object",
                              diffResults.get(1).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 2. diff result object", "Deleted: <a> Sister \n",
                                diffResults.get(1).toString());

            Assert.assertFalse("Backward direction expected", diffResults.get(2).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 3. diff result object",
                              diffResults.get(2).getDeletedTokens().isEmpty());
            Assert.assertEquals("Expected 2 inserted tokens for the 3. diff result object", 5,
                                diffResults.get(2).getInsertedTokens().size());
            Assert.assertEquals("Expected 1 similar token for the 3. diff result object", 1,
                                diffResults.get(2).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found for the 3. diff result object",
                                "Same: page \nInserted: has an additional paragraph </p> \n",
                                diffResults.get(2).toString());

            Assert.assertFalse("Backward direction expected", diffResults.get(3).isForwardDirection());
            Assert.assertEquals("Expected 1 deleted token for the 4. diff result object", 1,
                                diffResults.get(3).getDeletedTokens().size());
            Assert.assertTrue("No inserted tokens expected for the 4. diff result object",
                              diffResults.get(3).getInsertedTokens().isEmpty());
            Assert.assertTrue("No equal tokens expected for the 4. diff result object",
                              diffResults.get(3).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 4. diff result object", "Deleted: </a> \n",
                                diffResults.get(3).toString());

            Assert.assertFalse("Backward direction expected", diffResults.get(4).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 5. diff result object",
                              diffResults.get(4).getDeletedTokens().isEmpty());
            Assert.assertTrue("No inserted tokens expected for the 5. diff result object",
                              diffResults.get(4).getInsertedTokens().isEmpty());
            Assert.assertEquals("Expected 2 equal tokens for the 5. diff result object", 2,
                                diffResults.get(4).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found for the 5. diff result object", "Same: </body> </html> \n",
                                diffResults.get(4).toString());
        }
        else
        {
            LOG.error("No snakes found!");
            Assert.fail("No snakes found!");
        }
    }

    @Test
    public void testDifferencesLinearDiff() throws Exception
    {
        String[] pages = new String[] {"testPattern1.html", "testPattern2.html"};
        List<Token[]> patterns = new ArrayList<>();

        for (String pattern : pages)
        {
            URL url = DiffTest.class.getResource("/" + pattern);
            String html = DiffUtil.readFile(url.toURI().getPath());
            Parser parser = new Parser();
            ParseResult res = parser.tokenize(html, false);
            List<Token> p = res.getParsedTokens();

            Token[] tmp = new Token[0];
            patterns.add(p.toArray(tmp));
        }

        Results<Token> res;
        res = at.rovo.diff.LinearDiff.Compare(patterns.get(0), patterns.get(1));

        if (res.getSnakes() != null)
        {
            List<Result<Token>> diffResults = DiffUtil.getDifferences(res, patterns);
            Assert.assertEquals("Unexpected number of diff results found", 7, diffResults.size());
            // compare the first result object which only should contain the
            // same tokens for both files
            Assert.assertTrue("Forward direction expected", diffResults.get(0).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 1. diff result object",
                              diffResults.get(0).getDeletedTokens().isEmpty());
            Assert.assertTrue("No inserted tokens expected for the 1. diff result object",
                              diffResults.get(0).getInsertedTokens().isEmpty());
            Assert.assertEquals("Expected 12 equal tokens for the 1. diff result object", 12,
                                diffResults.get(0).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found",
                                "Same: <html> <head> <title> Test Title </title> </head> <body> <p> Simple Text </p> \n",
                                diffResults.get(0).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(1).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 2. diff result object",
                              diffResults.get(1).getDeletedTokens().isEmpty());
            Assert.assertEquals("No inserted tokens expected for the 2. diff result object", 1,
                                diffResults.get(1).getInsertedTokens().size());
            Assert.assertTrue("No equal tokens expected for the 2. diff result object",
                              diffResults.get(1).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 2. diff result object", "Inserted: <p> \n",
                                diffResults.get(1).toString());

            Assert.assertFalse("Backward direction expected", diffResults.get(2).isForwardDirection());
            Assert.assertEquals("Expected 1 deleted token for the 3. diff result object", 1,
                                diffResults.get(2).getDeletedTokens().size());
            Assert.assertTrue("No inserted tokens expected for the 3. diff result object",
                              diffResults.get(2).getInsertedTokens().isEmpty());
            Assert.assertTrue("No equal tokens expected for the 3. diff result object",
                              diffResults.get(2).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 3. diff result object", "Deleted: <a> \n",
                                diffResults.get(2).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(3).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 4. diff result object",
                              diffResults.get(3).getDeletedTokens().isEmpty());
            Assert.assertEquals("Expected 1 deleted token for the 4. diff result object", 1,
                                diffResults.get(3).getInsertedTokens().size());
            Assert.assertTrue("No equal tokens expected for the 4. diff result object",
                              diffResults.get(3).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 4. diff result object", "Inserted: This \n",
                                diffResults.get(3).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(4).isForwardDirection());
            Assert.assertEquals("Expected 1 deleted token for the 5. diff result object", 1,
                                diffResults.get(4).getDeletedTokens().size());
            Assert.assertTrue("No inserted tokens expected for the 5. diff result object",
                              diffResults.get(4).getInsertedTokens().isEmpty());
            Assert.assertTrue("No equal tokens expected for the 5. diff result object",
                              diffResults.get(4).getRegularTokens().isEmpty());
            Assert.assertEquals("Unexpected text found for the 5. diff result object", "Deleted: Sister \n",
                                diffResults.get(4).toString());

            Assert.assertFalse("Backward direction expected", diffResults.get(5).isForwardDirection());
            Assert.assertTrue("No deleted tokens expected for the 6. diff result object",
                              diffResults.get(5).getDeletedTokens().isEmpty());
            Assert.assertEquals("Expected 5 inserted tokens for the 6. diff result object", 5,
                                diffResults.get(5).getInsertedTokens().size());
            Assert.assertEquals("Expected 1 equal token for the 6. diff result object", 1,
                                diffResults.get(5).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found for the 6. diff result object",
                                "Same: page \nInserted: has an additional paragraph </p> \n",
                                diffResults.get(5).toString());

            Assert.assertTrue("Forward direction expected", diffResults.get(6).isForwardDirection());
            Assert.assertEquals("Expected 1 deleted token for the 7. diff result object", 1,
                                diffResults.get(6).getDeletedTokens().size());
            Assert.assertTrue("No inserted tokens expected for the 7. diff result object",
                              diffResults.get(6).getInsertedTokens().isEmpty());
            Assert.assertEquals("Expected 2 equal token for the 7. diff result object", 2,
                                diffResults.get(6).getRegularTokens().size());
            Assert.assertEquals("Unexpected text found for the 7. diff result object",
                                "Deleted: </a> \nSame: </body> </html> \n", diffResults.get(6).toString());
        }
        else
        {
            LOG.error("No snakes found!");
            Assert.fail("No snakes found!");
        }
    }
}