package cycuice.sourceAnalyzer.stree;

import java.util.ArrayList;
import com.google.gson.Gson;

import cycuice.sourceAnalyzer.common.JSONStringifyable;
import cycuice.sourceAnalyzer.common.PrettyPrintable;
import cycuice.sourceAnalyzer.lexer.Token;

public class STree implements PrettyPrintable, JSONStringifyable {
    private SNode root;

    /**
     * 傳入通過our sheme文法檢查的token串列，建立對應S-Tree並回傳
     * 
     * @param grammarCheckedTokenList - 通過our sheme文法檢查的token串列
     * @return 對應S-Tree
     */
    public static STree createSTree(ArrayList<Token> grammarCheckedTokenList) {
        STreeBuilder builder = new STreeBuilder();
        return builder.setGrammarCheckedTokenList(grammarCheckedTokenList).build();
    } // createSTree()
    
    public STree(SNode root) {
        this.root = root;
    } // STree()
    
    public SNode getRoot() {
        return root;
    } // getRoot()

    @Override
    public String getPrettyPrintString() {
        return buildPrettyPrintStringWithIndent(this.root, 0);
    } // getPrettyPrintString()

    private String buildPrettyPrintStringWithIndent(SNode rootNode, int indent) {
        if (rootNode.getType() != SNodeType.INTERNAL_NODE) {
            return rootNode.getStandardValue() + "\n";
        }  // if
        else {
            String prettyPrintStr = "";
            prettyPrintStr += "( " + buildPrettyPrintStringWithIndent(rootNode.getLeftChild(), indent + 2) ;
            
            SNode walkNode = rootNode.getRightChild();
            while (walkNode.getType() == SNodeType.INTERNAL_NODE) {
                prettyPrintStr += buildSpaceString(indent + 2) + buildPrettyPrintStringWithIndent(walkNode.getLeftChild(), indent + 2);
                walkNode = walkNode.getRightChild();
            } // while

            if (walkNode.getType() != SNodeType.NIL_NODE) {
                prettyPrintStr += buildSpaceString(indent + 2) + ".\n";
                prettyPrintStr += buildSpaceString(indent + 2) + buildPrettyPrintStringWithIndent(walkNode, indent + 2);
            } // if

            prettyPrintStr += buildSpaceString(indent) + ")\n";
            return prettyPrintStr;
        } // else
    } // buildPrettyPrintStringWithIndent()

    private String buildSpaceString(int spaceCount) {
        String spaceStr = "";
        for (int i = 0; i < spaceCount; i++) {
            spaceStr += " ";
        } // for

        return spaceStr;
    } // buildSpaceString()

    /**
     * 用於生成JSON格式的樹節點類別
     */
    private static class JSONTreeNode {
        private String value;
        private JSONTreeNode[] children;

        public static JSONTreeNode createInternalNode(JSONTreeNode[] children) {
            return new JSONTreeNode("", children);
        } // createInternalNode()

        public static JSONTreeNode createExternalNode(String value) {
            return new JSONTreeNode(value, null);
        } // createExternalNode()

        private JSONTreeNode(String value, JSONTreeNode[] children) {
            this.value = value;
            this.children = children;
        } // JSONTreeNode()
    } // class JSONTreeNode

    @Override
    public String getJSONString() {
        JSONTreeNode rootOfJSONTree = buildJSONTreeNode(this.root);
        Gson gson = new Gson();
        return gson.toJson(rootOfJSONTree);
    } // getJSONString()

    private JSONTreeNode buildJSONTreeNode(SNode root) {
        if (root.getType() != SNodeType.INTERNAL_NODE) {
            return JSONTreeNode.createExternalNode(root.getStandardValue());
        } // if
        else {
            JSONTreeNode[] children = new JSONTreeNode[2];
            children[0] = buildJSONTreeNode(root.getLeftChild());
            children[1] = buildJSONTreeNode(root.getRightChild());
            return JSONTreeNode.createInternalNode(children);
        } // else
    } // buildJSONTreeNode()
} // class STree
