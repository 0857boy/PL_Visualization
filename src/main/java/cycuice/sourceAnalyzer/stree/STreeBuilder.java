package cycuice.sourceAnalyzer.stree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import cycuice.sourceAnalyzer.lexer.Token;
import cycuice.sourceAnalyzer.lexer.TokenType;

public class STreeBuilder {
    private Queue<Token> grammarCheckedTokenQueue;

    public STreeBuilder setGrammarCheckedTokenList(ArrayList<Token> grammarCheckedTokenList) {
        this.grammarCheckedTokenQueue = new LinkedList<Token>();
        for (Token token : grammarCheckedTokenList) {  // 深度複製token list至queue
            this.grammarCheckedTokenQueue.add(token);
        } // for

        return this;
    } // setGrammarCheckedTokenList()
    
    public STree build() {
        return buildSTreeAndCleanQueue();
    } // build()

    /**
     * 遞迴建立S-Tree，過程中不斷取出queue中的token，直到最後清空整條queue，並建立對應的S-Tree
     * 
     * @return 對應S-Tree
     */
    private STree buildSTreeAndCleanQueue() {
        Token frontToken = this.grammarCheckedTokenQueue.remove();
        if (frontToken.getType() == TokenType.INT) {
            return new STree(SNode.createIntNode(Integer.parseInt(frontToken.getValue())));
        } // if
        else if (frontToken.getType() == TokenType.FLOAT) {
            return new STree(SNode.createFloatNode(Double.parseDouble(frontToken.getValue())));
        } // else if
        else if (frontToken.getType() == TokenType.STRING) {
            return new STree(SNode.createStringNode(frontToken.getValue()));
        } // else if        
        else if (frontToken.getType() == TokenType.SYMBOL) {
            return new STree(SNode.createSymbolNode(frontToken.getValue()));
        } // else if
        else if (frontToken.getType() == TokenType.T) {
            return new STree(SNode.createTNode());
        } // else if
        else if (frontToken.getType() == TokenType.NIL) {
            return new STree(SNode.createNilNode());
        } // else if
        else if (frontToken.getType() == TokenType.QUOTE) {
            STree leftSubTree = new STree(SNode.createQuoteNode());
            STree rightSubTree = new STree(SNode.createInternalNode(buildSTreeAndCleanQueue().getRoot(), SNode.createNilNode()));
            return new STree(SNode.createInternalNode(leftSubTree.getRoot(), rightSubTree.getRoot()));
        } // else if
        else {
            Stack<STree> subTreeStack = new Stack<STree>();
            boolean hasDot = false;
            STree rightmostTree = null;
            frontToken = this.grammarCheckedTokenQueue.peek();
            while (frontToken.getType() != TokenType.RIGHT_PAREN) {
                if (frontToken.getType() == TokenType.DOT) {
                    this.grammarCheckedTokenQueue.remove();  // 拿掉dot token
                    hasDot = true;
                    rightmostTree = buildSTreeAndCleanQueue();
                } // if
                else {
                    subTreeStack.push(buildSTreeAndCleanQueue());
                } // else

                frontToken = this.grammarCheckedTokenQueue.peek();
            } // while

            this.grammarCheckedTokenQueue.remove();  // 拿掉right-paren token

            if (!hasDot) {
                rightmostTree = new STree(SNode.createNilNode());
            } // if

            STree resultTree = new STree(rightmostTree.getRoot());
            while (!subTreeStack.empty()) {
                resultTree = new STree(SNode.createInternalNode(subTreeStack.pop().getRoot(), resultTree.getRoot()));
            } // while

            return resultTree;
        } // else
    } // buildSTreeAndCleanQueue()
} // class STreeBuilder
