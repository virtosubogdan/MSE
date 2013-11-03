package expression;

import dynamic.ASTExpression;
import dynamic.ASTFactor;
import dynamic.ASTParanthesis;
import dynamic.ASTStart;
import dynamic.ASTTerm;
import dynamic.ExpressionParser;
import dynamic.ExpressionParserVisitor;
import dynamic.SimpleNode;

public class PolishPrefixVisitor implements ExpressionParserVisitor {

	private void debug(SimpleNode node) {
		// System.out.println(node+":"+node.jjtGetNumChildren());
		// for(int i=0;i<node.jjtGetNumChildren();i++){
		// System.out.println(node.jjtGetChild(i));
		// }
		// System.out.println("t:"+node.jjtGetFirstToken());
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTStart node, Object data) {
		debug(node);
		System.out.println("PolishPrefixVisitor:");
		node.childrenAccept(this, data);
		System.out.println();
		return null;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		debug(node);
		if (node.jjtGetNumChildren() == 2) {
			System.out.print("+ ");
			node.jjtGetChild(0).jjtAccept(this, data);
			node.jjtGetChild(1).jjtAccept(this, data);
		}else{
			node.jjtGetChild(0).jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTTerm node, Object data) {
		debug(node);
		if (node.jjtGetNumChildren() == 2) {
			System.out.print("* ");
			node.jjtGetChild(0).jjtAccept(this, data);
			node.jjtGetChild(1).jjtAccept(this, data);
		}else{
			node.jjtGetChild(0).jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTFactor node, Object data) {
		debug(node);
		if (node.jjtGetNumChildren() == 0) {
			System.out.print(node.jjtGetFirstToken() + " ");
		} else {
			node.childrenAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTParanthesis node, Object data) {
		debug(node);
		node.childrenAccept(this, data);
		return null;
	}
	
	public static void main(){
		ExpressionParser.main(new String[]{});
	}

}
