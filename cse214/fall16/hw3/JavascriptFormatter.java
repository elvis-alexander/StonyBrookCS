package hw3;

public class JavascriptFormatter {
    private String errorMsg;

    // instance vars
    private JSStack stack;
    private int indentLevel;
    // utility flush to file
    private final char TAB = '\t';
    private final char NEW_LINE = '\n';
    // bracket delimiters
    private final char OPEN_BRACE = '{';
    private final char CLOSE_BRACE = '}';
    // parenthesis delimiters
    private final char OPEN_PAREN = '(';
    private final char CLOSE_PAREN = ')';
    private final char SEMI = ';';
    // for lopp delimiter
    private final char F = 'f';
    private final char O = 'o';
    private final char R = 'r';
    // for loop statement
    private final String FOR_LOOP = "for(";
    // error msg's extra delimiters
    private final String EXTRA_BRACE = "//extra brace found.";
    private final String EXTRA_PAREN = "//extra closing parenthesis found.";
    // error msg's missing
    private final String MISSING_BRACE = "\n//missing closing brace found.";
    private final String MISSING_PAREN = "\n//missing parenthesis";


    public JavascriptFormatter() {
        this.stack = new JSStack();
        this.errorMsg = null;
    }

    public static boolean matches(BlockType c1, char c2) {
        if(c1 == BlockType.PAREN && c2 == ')') {
            return true;
        } else if(c1 == BlockType.BRACE && c2 == '}') {
            return true;
        } else if(c1 == BlockType.FOR && c2 == ')') {
            return true;
        }
        return false;
    }

    public static void appendTabs(StringBuffer b, int n) {
        char TAB = '\t';
        while(n-- > 0) {
            b.append(TAB);
        }
    }

    public String format(String input) {
        // input
        char[] in = input.toCharArray();
        // output buffer
        StringBuffer out = new StringBuffer("");
        // char index
        int i = 0;
        // read every character
        while (i < in.length) {
            // current character
            char c = in[i++];
            // detect "for loop"
            if(c == F)
            {
                // detect "for(" input
                if(in[i] == O && in[i+1] == R && in[i+2] == OPEN_PAREN)
                {
                    // push For BlockType
                    stack.push(BlockType.FOR);
                    // output "for(" input
                    out.append(FOR_LOOP);
                    // advance input after "for(" input
                    i += 3;
                } else {
                    // for loop not detected
                    out.append(c);
                }
            } else if(c == OPEN_PAREN)
            {
                // push PAREN BlockType
                stack.push(BlockType.PAREN);
                // output open parenthesis
                out.append(OPEN_PAREN);
            } else if(c == CLOSE_PAREN)
            {
                // output ")"
                out.append(CLOSE_PAREN);
                // extra right parenthesis detected
                if(stack.isEmpty() || (!stack.isEmpty() && !matches(stack.pop(), c)))
                {
                    // new line
                    out.append(NEW_LINE);
                    errorMsg = EXTRA_PAREN;
                    // error msg
                    out.append(EXTRA_PAREN);
                    // terminate process
                    return out.toString();
                }
            } else if(c == OPEN_BRACE)
            {
                // increase indent level for formatting
                ++indentLevel;
                // push brace to stack
                stack.push(BlockType.BRACE);
                // output brace
                out.append(OPEN_BRACE);
                // output new line for formatting
                out.append(NEW_LINE);
                // append tabs if next char is not a close brace
                if(i != in.length && in[i] != CLOSE_BRACE) {
                    JavascriptFormatter.appendTabs(out, indentLevel);
                }
            } else if(c == CLOSE_BRACE)
            {
                // decrement indent level
                --indentLevel;
                // detect extra close brace
                if(stack.isEmpty() || (!stack.isEmpty() && !matches(stack.pop(), c))) {
                    JavascriptFormatter.appendTabs(out, indentLevel);
                    out.append(CLOSE_BRACE);
                    out.append(NEW_LINE);
                    errorMsg = EXTRA_BRACE;
                    return out.toString() + EXTRA_BRACE;
                }
                /*if(!matches(stack.pop(), c)) {
                    // terminate program - mismatch delimiters
                    return out.toString();
                }*/
                // output tabs for formatting
                JavascriptFormatter.appendTabs(out, indentLevel);
                // output close brace
                out.append(CLOSE_BRACE);
                // output new line if end of file not reached
                if(i != in.length) {
                    out.append(NEW_LINE);
                }
            }  else if(c == SEMI)
            {

                if(!stack.isEmpty() && stack.peek() == BlockType.PAREN) {

                    errorMsg = MISSING_PAREN;
                    return out.toString() + MISSING_PAREN;
                }

                // output ';'
                out.append(SEMI);
                // check for last statement and last line
                if(!stack.isEmpty() && stack.peek() == BlockType.FOR) {
                    // don't print new line
                } else {
                    // output new line
                    out.append(NEW_LINE);
                    // append tabs if next char is not a close brace
                    if(i < in.length && i != in.length - 1 && in[i] != CLOSE_BRACE) {
                        JavascriptFormatter.appendTabs(out, indentLevel);
                    }
                }
            } else
            {
                // append normal character
                out.append(c);
            }
        }

        // detect missing closing delimiter
        if(!stack.isEmpty()) {
            if(stack.peek() == BlockType.BRACE) {
                errorMsg = MISSING_BRACE;
                out.append(MISSING_BRACE);
            } else if(stack.peek() == BlockType.PAREN) {
                errorMsg = MISSING_PAREN;
                out.append(MISSING_PAREN);
            }
        }
        return out.toString();
    }

    public String getErrorMsg() { return errorMsg; }

}