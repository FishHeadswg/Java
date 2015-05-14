
import java.io.*;

public class ca
{
  private static StreamTokenizer tokens;
  private static int token;
  public static void main(String argv[]) throws IOException
  {
    InputStreamReader reader;
    if (argv.length > 0)
      reader = new InputStreamReader(new FileInputStream(argv[0]));
    else
      reader = new InputStreamReader(System.in);

    // create the tokenizer:
    tokens = new StreamTokenizer(reader);
    tokens.ordinaryChar('.');
    tokens.ordinaryChar('-');
    tokens.ordinaryChar('/');

    // advance to the first token on the input:
    getToken();

    // parse expression and get calculated value:
    int value = expr();

    // check if expression ends with ';' and print value
    if (token == (int)';')
      System.out.println("Value = " + value);
    else
      System.out.println("Syntax error");
  }

  // getToken - advance to the next token on the input
  private static void getToken() throws IOException
  {
    token = tokens.nextToken();
  }

  // expr - parse <expr> -> <term> <term_tail>
  private static int expr() throws IOException
  {
    int subtotal = term();
    return term_tail(subtotal);
  }

  // term - parse <term> -> <factor> <factor_tail>
  private static int term() throws IOException
  {
    int subtotal = factor();
    return factor_tail(subtotal);
  }

  // term_tail - parse <term_tail> -> <add_op> <term> <term_tail> | empty
  private static int term_tail(int subtotal) throws IOException
  {
    if (token == (int)'+')
    {
      getToken();
      int termvalue = term();
      return term_tail(subtotal + termvalue);
    }
    else if (token == (int)'-')
    {
      getToken();
      int termvalue = term();
      return term_tail(subtotal - termvalue);
    }
    else
      return subtotal;
  }

  // factor - parse <factor> -> '(' <expr> ')' | '-' <expr> | ID | NUM
  private static int factor() throws IOException
  {
    if (token == (int)'(')
    {
      getToken();
      int value = expr();
      if (token == (int)')')
        getToken();
      else
        System.out.println("closing ')' expected");
      return value;
    }
    else if (token == (int)'-')
    {
      getToken();
      return -factor();
    }
    else if (token == StreamTokenizer.TT_WORD)
    {
      getToken();
      // ignore variable names
      return 0;
    }
    else if (token == StreamTokenizer.TT_NUMBER)
    {
      getToken();
      return (int)tokens.nval;
    }
    else
    {
      System.out.println("factor expected");
      return 0;
    }
  }

  // factor_tail - parse <factor_tail> -> <mult_op> <factor> <factor_tail> | empty
  private static int factor_tail(int subtotal) throws IOException
  {
    if (token == (int)'*')
    {
      getToken();
      int factorvalue = factor();
      return factor_tail(subtotal * factorvalue);
    }
    else if (token == (int)'/')
    {
      getToken();
      int factorvalue = factor();
      return factor_tail(subtotal / factorvalue);
    }
    else
      return subtotal;
  }
}

