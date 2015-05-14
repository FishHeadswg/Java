/*

Trevor Richardson
COP4020
Project 2
UCalc.java


<expr1>   -> 'let' identifier '=' <expr2> expr2.in           := expr1.in;
                                          expr1.value        := expr2.value;
                                          expr1.out          := expr2.out.put(identifier=expr2.value)
           | <term> <term_tail>           term.in            := expr1.in;
                                          term_tail.in       := term.out;
                                          term_tail.subtotal := term.value;
                                          expr1.value        := term_tail.value;
                                          expr1.out          := term_tail.out
<term>         -> <factor> <factor_tail>  factor.in            := term.in;
                                          factor_tail.in       := factor.out;
                                          factor_tail.subtotal := factor.value;
                                          term.value        := factor_tail.value;
                                          term.out          := factor_tail.out
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
<factor1> -> '-' <factor2>                factor2.in         := factor1.in;
                                          factor1.value      := -factor2.value;
                                          factor1.out        := factor2.out;
           | <power> <power_tail>         power.in            := factor1.in;
                                          power_tail.in       := power.out;
                                          power_tail.subtotal := power.value;
                                          factor1.value        := power_tail.value;
                                          factor1.out          := power_tail.out
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
<power1> -> '(' <expr> ')'                expr.in           := power1.in;
                                          power1.value      := expr.value;
                                          power1.out        := expr.out;
           | identifier                   power1.value      := power1.in.get(identifier);
                                          power1.out        := power1.in;
           | number                       power1.value      := number;
                                          power1.out        := power1.in;
<power_tail1> -> '^' <power> <power_tail2>     power.in := power_tail1.in;
                                               power_tail2.in := power.out;
                                               power_tail2.subtotal :=
                                                         power_tail1.subtotal^power.value;
                                               power_tail1.value := power_tail2.value;
                                               power_tail1.out := power_tail2.out;
                | empty                        power_tail1.value := power_tail1.subtotal;
                                               power_tail1.out := power_tail1.in;
                                               
 * UCalc.java Implementes a calculator for simple expressions Uses
 * java.io.StreamTokenizer and recursive descent parsing
 * 
 * Compile: javac UCalc.java
 * 
 * Execute: java UCalc or: java UCalc <filename>
 * 
 * Custom exceptions:
 * 
 * SyntaxError: illegal character is found, a closing ) is not found, 
 * or a = is not used in a let expression
 * 
 * RuntimeError: an identifier is encountered for which no value can be found
 * 
 */

import java.util.*;
import java.io.*;

public class UCalc {
	
	// illegal character is found, a closing ) is not found, or a = is not used in a let expression
	public static class SyntaxError extends Exception {
		private static final long serialVersionUID = 3924831457233316711L;

		public SyntaxError(String message) {
	        super(message);
	    }
	}
	
	// an identifier is encountered for which no value can be found
	public static class RuntimeError extends Exception {
		private static final long serialVersionUID = -5829333073907682516L;

		public RuntimeError(String message) {
	        super(message);
	    }
	}
	
	private static StreamTokenizer tokens;
	private static int token;

	public static void main(String argv[]) throws IOException, SyntaxError, RuntimeError {
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
		int value = 0;

		try {
		// advance to the first token on the input:
		getToken();
		value = expr(exprin, exprout);
		// check if expression ends with ';' and print value
		if (token == (int) ';')
			System.out.println("Value = " + value);
		else
			throw new SyntaxError("operator expected");
		}
		catch(SyntaxError e) {
			System.out.println("syntax error: " + e.getMessage());
			System.exit(0);
		}
		catch(RuntimeError e) {
			System.out.println("runtime error: '" + e.getMessage() + "' undefined");
			System.exit(0);
		}
	}

	// getToken - advance to the next token on the input
	private static void getToken() throws IOException {
		token = tokens.nextToken();
	}

	// expr - parse <expr> -> 'let' identifier '=' <expr2> | <term> <term_tail>
	private static int expr(Hashtable<String, Integer> exprin,
			Hashtable<String, Integer> exprout) throws IOException, SyntaxError, RuntimeError {
		if (token == StreamTokenizer.TT_WORD && tokens.sval.equals("let")) {
			getToken(); // advance to identifier
			String id = tokens.sval;
			getToken(); // advance to '='
			if (token == (int) '=')
				getToken(); // advance to <expr>
			else
				throw new SyntaxError("'=' expected");
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
			Hashtable<String, Integer> termout) throws IOException, SyntaxError, RuntimeError {
		Hashtable<String, Integer> x = termin;
		int subtotal = factor(termin, termout);
		return factor_tail(subtotal, x, termout);
	}

	// term_tail - parse <term_tail> -> <add_op> <term> <term_tail> | empty
	private static int term_tail(int subtotal,
			Hashtable<String, Integer> termtailin,
			Hashtable<String, Integer> termtailout) throws IOException, SyntaxError, RuntimeError {
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

	// factor - parse <factor1> -> -<factor2> | <power> <power_tail>
	private static int factor(Hashtable<String, Integer> factorin,
			Hashtable<String, Integer> factorout) throws IOException, SyntaxError, RuntimeError {
		if (token == (int) '-') {
			Hashtable<String, Integer> x = factorin;
			getToken();
			factorout = factorin;
			return -factor(x, factorout);
		} else {
			Hashtable<String, Integer> x = factorin;
			int subtotal = power(factorin, factorout);
			return power_tail(subtotal, x, factorout);
		}
	}

	// factor_tail - parse <factor_tail> -> <mult_op> <factor> <factor_tail> |
	// empty
	private static int factor_tail(int subtotal,
			Hashtable<String, Integer> factortailin,
			Hashtable<String, Integer> factortailout) throws IOException, SyntaxError, RuntimeError {
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
	// power - parse <power> -> '(' <expr> ')' | ID | NUM
		private static int power(Hashtable<String, Integer> powerin,
				Hashtable<String, Integer> powerout) throws IOException, SyntaxError, RuntimeError {
			if (token == (int) '(') {
				Hashtable<String, Integer> x = powerin;
				getToken();
				int value = expr(x, powerout);
				if (token == (int) ')')
					getToken();
				else
					throw new SyntaxError("')' expected");
				powerout = powerin;
				return value;
			} else if (token == StreamTokenizer.TT_WORD) {
				String id = tokens.sval;
				getToken();
				powerout = powerin;
				Integer lookup = 0;
				try {
				lookup = ((Integer) powerin.get(id)).intValue();
				}
				catch(NullPointerException e) {
					throw new RuntimeError(id);
				}
				return lookup;
			} else if (token == StreamTokenizer.TT_NUMBER) {
				getToken();
				powerout = powerin;
				return (int) tokens.nval;
			} else {
				System.out.println("power expected");
				return 0;
			}
		}
		// power_tail - parse <power_tail> -> '^' <power> <power_tail> | empty
		private static int power_tail(int subtotal,
				Hashtable<String, Integer> powertailin,
				Hashtable<String, Integer> powertailout) throws IOException, SyntaxError, RuntimeError {
			Hashtable<String, Integer> x = powertailin;
			if (token == (int) '^') {
				getToken();
				int powervalue = power(powertailin, x);
				return power_tail((int)Math.pow(subtotal, powervalue), x, powertailout);
			} else
				return subtotal;
		}
}

