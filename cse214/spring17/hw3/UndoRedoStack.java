import java.util.EmptyStackException;
import java.util.Stack;

public class UndoRedoStack extends Stack<ActionCommand> {

    /**
     * pushes command onto stack
     * @param a
     * @return
     */
    public ActionCommand push(ActionCommand a) {
        super.push(a);
        return a;
    }

    /**
     * pops command from stack
     * @return
     * @throws EmptyStackException
     */
    public ActionCommand pop() throws EmptyStackException {
        if(isEmpty())
            throw new EmptyStackException();
        return super.pop();
    }

    /**
     * returns top element without removing
     * @return
     */
    public ActionCommand peek() {
        if(isEmpty())
            throw new EmptyStackException();
        return super.peek();
    }

    /**
     * is the stack empty
     * @return
     */
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * toString for stack
     * @return
     */
    public String toString() {
        if(this.isEmpty())
            return "[empty]\n";
        StringBuffer buffer = new StringBuffer();
        for(int i = this.size() - 1; i >= 0; --i)
            buffer.append(this.get(i));
        return buffer.toString();
    }
}
