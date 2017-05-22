package hw3;

import java.util.Stack;

public class JSStack extends Stack {

    public JSStack() {
        super();
    }

    public void push(BlockType b) {
        super.push(b);
    }

    public BlockType pop() {
        return (BlockType) super.pop();
    }

    public BlockType peek() {
        return (BlockType) super.peek();
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

}