package interpreter;

import java.util.Scanner;

public class PeekableScanner
{
    private Scanner scan;
    private String next;

    public PeekableScanner( String source )
    {
        scan = new Scanner( source );
        next = (scan.hasNext() ? scan.next() : null);
    }
    
    public void useDelimiter(String regex) {
    	scan.useDelimiter(regex);
    }

    public boolean hasNext()
    {
        return (next != null);
    }

    public String next()
    {
        String current = next;
        next = (scan.hasNext() ? scan.next() : null);
        return current;
    }

    public String peek()
    {
        return next;
    }
}