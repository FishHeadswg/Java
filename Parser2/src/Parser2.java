/*      

Trevor Richardson
COP4020
Project 2
Parser2.java

1.
<expr>
------------------------------
   |               |
<term>       <term_tail1>
------       ----------------------------
  |           |        |          |
  a           +     <term>    <term_tail>
                      -----     ----------------------------
                        |       |       |           |
                        b       +     <term>    <term_tail>
                                      ------    -----------
                                        |           |
                                        c         empty

2.
<expr>
------------------------------
   |          |           |
<term>        ^         <expr>
------                 -----------------------
  |                      |        |       |
  a                    <term>     ^     <expr>
                         |                |
                         b             --------
                                          |
                                        <term>
                                          |
                                          c
                                          
3.
"2*(1+3)/x;" ==> "Syntax ok"

<expr>
-----------------------------------------------------------------------------------------------------------------------------------------
  |                                                                                                                              |
<term>                                                                                                                      <term_tail1>
----------------------------                                                                                                -------------
    |               |                                                                                                            |
 <factor>     <factor_tail>                                                                                                    empty
    |         ------------------------------------------------------------------------------------------
    2            |         |                                                                    |
               <mult_op>  <factor>                                                         <factor_tail>
               ---------  ------------------------------------------------------------    ------------------------------------
                  |        |    |                                                    |         |          |          |
                  *        (  <expr>                                                 )     <mult_op>  <factor>  <factor_tail>
                              --------------------                                         ---------  --------  -------------
                                |          |                                                   |          |          |                    
                               <term>  <term_tail>                                             /          x        empty
                               ------  ------------------------------------------------
                                 |        |         |                            |
                                 1     <add_op>   <term>                    <term_tail>
                                       --------   -----------------------   -----------
                                          |         |           |                |
                                          +       <factor>  <factor_tail>      empty
                                                     |          |
                                                     3         empty
 
"2x+1;" ==> "Syntax error"

The parser fails when it tries to decipher 2x under the <factor> non-terminal, 
as "2x" is neither a number nor a valid identifier nor any other acceptable 
terminal. Therefore, 2 is processed as a number but x cannot be matched to 
anything in <factor_tail> and eventually control returns to main. A syntax 
error is returned upon discovering the ';' token was not reached.

4.
2*f(1+a); => "Syntax ok"
<expr>
-----------------------------------------------------------------------------------------------------------------------------------------
  |                                                                                                                              |
<term>                                                                                                                      <term_tail1>
----------------------------                                                                                                -------------
    |               |                                                                                                            |
 <factor>     <factor_tail>                                                                                                    empty
    |         ----------------------------------------------------------------------------------------------
    2            |         |                                                                        |
               <mult_op>  <factor>                                                             <factor_tail>
               ---------  ----------------------------------------------------------------    --------------
                  |        |   |    |                                                    |          |    
                  *        f   (  <expr>                                                 )         empty
                                  --------------------                                         
                                    |          |                                                
                                   <term>  <term_tail>                                        
                                   ------  ------------------------------------------------
                                     |        |         |                            |
                                     1     <add_op>   <term>                    <term_tail>
                                           --------   -----------------------   -----------
                                              |         |           |                |
                                              +       <factor>  <factor_tail>      empty
                                                         |          |
                                                         a         empty



5.
Calculator language grammar (**UPDATED**):

 <expr>          -> <term> <term_tail>
 <term>          -> <factor> <factor_tail>
 <term_tail>     -> <add_op> <term> <term_tail>
                 | empty
 <factor>        -> <neg_op>
                 | <power> <power_tail>
 <factor_tail>   -> <mult_op> <factor> <factor_tail>
                 | empty
<power>          -> '(' <expr> ')'
                 | identifier '(' <expr> ')'
                 | identifier
                 | number
 <power_tail>   -> <power_op> <power> <power_tail>
                 | empty
 <add_op>        -> '+' | '-'
 <mult_op>       -> '*' | '/'
 <neg_op>        -> '-'
 <power_op>	     -> '^'
 
 
 Parser2.java
        Implements a parser for a calculator language
        Uses java.io.StreamTokenizer and recursive descent parsing

        Compile:
        javac Parser2.java
 
 */

import java.io.*;


public class Parser2 {
	private static StreamTokenizer tokens;
	private static int token;

	public static void main(String argv[]) throws IOException {
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

		// check if expression:
		expr();

		// check if expression ends with ';'
		if (token == (int) ';')
			System.out.println("Syntax ok");
		else
			System.out.println("Syntax error");
	}

	// getToken - advance to the next token on the input
	private static void getToken() throws IOException {
		token = tokens.nextToken();
	}

	// expr - parse <expr> -> <term> <term_tail>
	private static void expr() throws IOException {
		term();
		term_tail();
	}

	// term - parse <term> -> <factor> <factor_tail>
	private static void term() throws IOException {
		factor();
		factor_tail();
	}

	// term_tail - parse <term_tail> -> <add_op> <term> <term_tail> | empty
	private static void term_tail() throws IOException {
		if (token == (int) '+' || token == (int) '-') {
			add_op();
			term();
			term_tail();
		}
	}

	// factor - parse <factor> -> <neg_op> | <power> <power_tail>
	private static void factor() throws IOException {
		neg_op();
		power();
		power_tail();
	}

	// factor_tail - parse <factor_tail> -> <mult_op> <factor> <factor_tail> |
	// empty
	private static void factor_tail() throws IOException {
		if (token == (int) '*' || token == (int) '/') {
			mult_op();
			factor();
			factor_tail();
		}
	}
	
	// power - parse <power> -> '(' <expr> ')' | <neg_op> <power> 
	// | identifier '(' <expr> ')' | identifier | number
	private static void power() throws IOException {
		if (token == (int) '(') {
			getToken();
			expr();
			if (token == (int) ')')
				getToken();
			else
				System.out.println("closing ')' expected");
		} else if (token == StreamTokenizer.TT_WORD) {
			getToken();
			if (token == (int) '(') {
				getToken();
				expr();
				if (token == (int) ')')
					getToken();
				else
					System.out.println("closing ')' expected");
			}

		} else if (token == StreamTokenizer.TT_NUMBER)
			getToken();
		else
			System.out.println("factor expected");
	}
	
	// power_tail - parse <power_tail> -> <power_op> <power> <power_tail> |
		// empty
	private static void power_tail() throws IOException {
		if (token == (int) '^') {
			power_op();
			power();
			power_tail();
		}
	}

	// add_op - parse <add_op> -> '+' | '-'
	private static void add_op() throws IOException {
		if (token == (int) '+' || token == (int) '-')
			getToken();
	}

	// mult_op - parse <mult_op> -> '*' | '/'
	private static void mult_op() throws IOException {
		if (token == (int) '*' || token == (int) '/')
			getToken();
	}
	
	// neg_op - parse <neg_op> -> '-'
	private static void neg_op() throws IOException {
		if (token == (int) '-')
			getToken();
	}
	// power_op - parse <power_op> -> '-'
	private static void power_op() throws IOException {
		if (token == (int) '^')
			getToken();
	}
}
