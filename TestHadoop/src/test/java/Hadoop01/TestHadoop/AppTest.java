package Hadoop01.TestHadoop;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {


        String str = "<artifactId>hadoop-yarn-server-resourcemanager</artifactId>";
        //String regex = "<artifactId>.*?</artifactId>";
        String regex = "[^abc]";
        //String[] s = str.split(regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        String ss = null;
        while (matcher.find()){
            ss = matcher.group();

            System.out.print(ss);
            //System.out.println(ss  + "   "+ss.substring(ss.indexOf(">")+1,ss.indexOf("/")-1));

        }








    }
}
