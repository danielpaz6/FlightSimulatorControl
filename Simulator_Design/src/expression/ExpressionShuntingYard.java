package expression;

import java.util.Arrays;
import java.util.Stack;
 
public class ExpressionShuntingYard {
 
    public static void main(String[] args) {
        //String infix = "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3";
       // String infix = "x+2 > 5*7 && 3 == 7 || ( z != y*2 - 3 )";
    	String infix = "x+2 > 3 && y < 3";
        //System.out.printf("infix:   %s%n", infix);
        System.out.printf("postfix: %s%n", infixToPostfix(infix));
        
        String[] s = infix.split("((?<=[><()])|(?=[><()]))|((?<=&&)|(?=&&))|((?<===)|(?===))|((?<=!=)|(?=!=))|(((?<=\\|\\|)|(?=\\|\\|)))");
        System.out.println(Arrays.toString(s));
    }
 
    static String infixToPostfix(String infix) {
        /* To find out the precedence, we take the index of the
           token in the ops string and divide by 2 (rounding down). 
           This will give us: 0, 0, 1, 1, 2 */
        //final String ops = "-+/*^";
        final String ops = "abhhg";
        
        
 
        StringBuilder sb = new StringBuilder();
        Stack<Integer> s = new Stack<>();
 
        for (String token : infix.split(("((?<=[><()])|(?=[><()]))|((?<=&&)|(?=&&))|((?<===)|(?===))|((?<=!=)|(?=!=))|(((?<=\\|\\|)|(?=\\|\\|)))")))
        {
        	token = token.trim();
        	
            if (token.isEmpty())
                continue;
            
            if(token.equals("&&"))
            	token = "g";
            
            if(token.equals(">"))
            	token = "b";
            
            if(token.equals("<"))
            	token = "a";
            
            char c = token.charAt(0);
            int idx = ops.indexOf(c);
 
            // check for operator
            if (idx != -1)
            {
                if (s.isEmpty())
                    s.push(idx);
 
                else {
                    while (!s.isEmpty()) {
                        int prec2 = s.peek() / 2;
                        int prec1 = idx / 2;
                        if (prec2 > prec1 || (prec2 == prec1 && c != 'g'))
                            sb.append(ops.charAt(s.pop())).append(' ');
                        else break;
                    }
                    s.push(idx);
                }
            } 
            else if (c == '(') {
                s.push(-2); // -2 stands for '('
            } 
            else if (c == ')') {
                // until '(' on stack, pop operators.
                while (s.peek() != -2)
                    sb.append(ops.charAt(s.pop())).append(' ');
                s.pop();
            }
            else {
                sb.append(token).append(' ');
            }
        }
        while (!s.isEmpty())
            sb.append(ops.charAt(s.pop())).append(' ');
        
        return sb.toString();
    }
}