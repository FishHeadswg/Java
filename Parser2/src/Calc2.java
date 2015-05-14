/*

Trevor Richardson
COP4020
Project 2
Calc2.java


 1.
-2*3+1 

<expr>(-5)
---------------------------------------------------------------------
  |                                                           |
<term>(-6)                                                <term_tail>[-6](-5)
-------------------------------                           ---------------------------------------------------
    |                   |                                  |    |                                     |
 <factor>(-2)     <factor_tail>[-2](-6)                    +  <term>(1)                           <term_tail>[-5](-5)
 ------------     -------------------------------             -------------------------           -----------
  |     |          |     |              |                        |               |                    |
  -  <factor>(2)   *  <factor>(3)  <factor_tail>[-6](-6)      <factor>(1)   <factor_tail>[1](1)     empty
     --------         --------     -------------              --------      -------------
        |                |              |                        |               |
        2                3            empty                      1             empty
  
    
2.
1/2; returns 0 because integer division is used, in which Java truncates 0.5 to 0. 
The program reaches this conclusion by following the grammar from expr() to term() 
to factor(), where 1 is read, and the corresponding factor_tail() where 2 is read 
and the program performs integer division to return 0.
    
                                                     
3.
<expr1>   -> 'let' identifier '=' <expr2> expr2.in           := expr1.in;
                                          expr1.value        := expr2.value;
                                          expr1.out          := expr2.out.put(identifier=expr2.value)
           | <term> <term_tail>           term.in            := expr1.in;
                                          term_tail.in       := term.out;
                                          term_tail.subtotal := term.value;
                                          expr1.value        := term_tail.value;
                                          expr1.out          := term_tail.out
<term1>         -> <factor> <factor_tail> factor.in            := term1.in;
                                          factor_tail.in       := factor.out;
                                          factor_tail.subtotal := factor.value;
                                          term1.value        := factor_tail.value;
                                          term1.out          := factor_tail.out
<term_tail1>   -> '+' <term> <term_tail2>      term.in := term_tail1.in;
                                               term_tail2.in := term.out;
                                               term_tail2.subtotal :=
                                                         term_tail1.subtotal+term.value;
                                               term_tail1.value := term_tail2.value
                                               term_tail1.out := term_tail2.out;
                | '-' <term> <term_tail2>      term.in := term_tail1.in;
                                               term_tail2.in := term.out;
                                               term_tail2.subtotal :=
                                                         term_tail1.subtotal-term.value;
                                               term_tail1.value := term_tail2.value;
                                               term_tail1.out := term_tail2.out;
                | empty                        term_tail1.value := term_tail1.subtotal;
                                               term_tail1.out := term_tail1.in;
<factor1> -> '(' <expr> ')'               expr.in            := factor1.in;
                                          factor1.value      := expr.value;
                                          factor1.out        := expr.out;
           | '-' <factor2>                factor2.in         := factor1.in;
                                          factor1.value      := -factor2.value;
                                          factor1.out        := factor2.out;
           | identifier                   factor1.value      := factor1.in.get(identifier);
                                          factor1.out        := factor1.in;
           | number                       factor1.value      := number;
                                          factor1.out        := factor1.in;
<factor_tail1> -> '*' <factor> <factor_tail2>  factor.in := factor_tail1.in;
                                               factor_tail2.in := factor.out;
                                               factor_tail2.subtotal :=
                                                         factor_tail1.subtotal*factor.value;
                                               factor_tail1.value := factor_tail2.value;
                                               factor_tail1.out := factor_tail2.out;
                | '/' <factor> <factor_tail2>  factor.in := factor_tail1.in;
                                               factor_tail2.in := factor.out;
                                               factor_tail2.subtotal :=
                                                         factor_tail1.subtotal/factor.value;
                                               factor_tail1.value := factor_tail2.value;
                                               factor_tail1.out := factor_tail2.out;
                | empty                        factor_tail1.value := factor_tail1.subtotal;
                                               factor_tail1.out := factor_tail1.in;
 
 
 4. 
 
  * Calc2.java Implementes a calculator for simple expressions Uses
 * java.io.StreamTokenizer and recursive descent parsing
 * 
 * Compile: javac Calc2.java
 * 
 * Execute: java Calc2 or: java Calc2 <filename>
 * 
 */

import java.util.*;
import java.io.*;

public class Calc2 {
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
		Hashtable<String, Integer> exprin = new Hashtable<String, Integer>();
		Hashtable<String, Integer> exprout = exprin;

		// advance to the first token on the input:
		getToken();
		int value = expr(exprin, exprout);

		// check if expression ends with ';' and print value
		if (token == (int) ';')
			System.out.println("Value = " + value);
		else
			System.out.println("Syntax error");
	}

	// getToken - advance to the next token on the input
	private static void getToken() throws IOException {
		token = tokens.nextToken();
	}

	// expr - parse <expr> -> 'let' identifier '=' <expr2> | <term> <term_tail>
	private static int expr(Hashtable<String, Integer> exprin,
			Hashtable<String, Integer> exprout) throws IOException {
		if (token == StreamTokenizer.TT_WORD && tokens.sval.equals("let")) {
			getToken(); // advance to identifier
			String id = tokens.sval;
			getToken(); // advance to '='
			getToken(); // advance to <expr>
			int value = expr(exprin, exprout);
			exprout.put(id, new Integer(value));
			return value;
		} else {
			Hashtable<String, Integer> x = exprin; // Java likes references to
													// be initialized
			int subtotal = term(exprin, x);
			return term_tail(subtotal, x, exprout);
		}
	}

	// term - parse <term> -> <factor> <factor_tail>
	private static int term(Hashtable<String, Integer> termin,
			Hashtable<String, Integer> termout) throws IOException {
		Hashtable<String, Integer> x = termin;
		int subtotal = factor(termin, termout);
		return factor_tail(subtotal, x, termout);
	}

	// term_tail - parse <term_tail> -> <add_op> <term> <term_tail> | empty
	private static int term_tail(int subtotal,
			Hashtable<String, Integer> termtailin,
			Hashtable<String, Integer> termtailout) throws IOException {
		Hashtable<String, Integer> x = termtailin;
		if (token == (int) '+') {
			getToken();
			int termvalue = term(termtailin, x);
			return term_tail(subtotal + termvalue, x, termtailout);
		} else if (token == (int) '-') {
			getToken();
			int termvalue = term(termtailin, x);
			return term_tail(subtotal - termvalue, x, termtailout);
		} else
			return subtotal;
	}

	// factor - parse <factor> -> '(' <expr> ')' | '-' <expr> | ID | NUM
	private static int factor(Hashtable<String, Integer> factorin,
			Hashtable<String, Integer> factorout) throws IOException {
		if (token == (int) '(') {
			Hashtable<String, Integer> x = factorin;
			getToken();
			int value = expr(x, factorout);
			if (token == (int) ')')
				getToken();
			else
				System.out.println("closing ')' expected");
			factorout = factorin;
			return value;
		} else if (token == (int) '-') {
			Hashtable<String, Integer> x = factorin;
			getToken();
			factorout = factorin;
			return -factor(x, factorout);
		} else if (token == StreamTokenizer.TT_WORD) {
			String id = tokens.sval;
			getToken();
			factorout = factorin;
			return ((Integer) factorin.get(id)).intValue();
		} else if (token == StreamTokenizer.TT_NUMBER) {
			getToken();
			factorout = factorin;
			return (int) tokens.nval;
		} else {
			System.out.println("factor expected");
			return 0;
		}
	}

	// factor_tail - parse <factor_tail> -> <mult_op> <factor> <factor_tail> |
	// empty
	private static int factor_tail(int subtotal,
			Hashtable<String, Integer> factortailin,
			Hashtable<String, Integer> factortailout) throws IOException {
		Hashtable<String, Integer> x = factortailin;
		if (token == (int) '*') {
			getToken();
			int factorvalue = factor(factortailin, x);
			return factor_tail(subtotal * factorvalue, x, factortailout);
		} else if (token == (int) '/') {
			getToken();
			int factorvalue = factor(factortailin, x);
			return factor_tail(subtotal / factorvalue, x, factortailout);
		} else
			return subtotal;
	}

}
