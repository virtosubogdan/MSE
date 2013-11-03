package expression;

import dynamic.ASTExpression;
import dynamic.ASTFactor;
import dynamic.ASTParanthesis;
import dynamic.ASTStart;
import dynamic.ASTTerm;
import dynamic.ExpressionParser;
import dynamic.ExpressionParserVisitor;
import dynamic.SimpleNode;

public class EvaluateVisitor implements ExpressionParserVisitor {

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

	private class Data{
		private int value;
		private Data(int value){
			this.value=value;
		}
	}
	
	@Override
	public Object visit(ASTStart node, Object data) {
		debug(node);
		System.out.println("Evaluate visitor:");
		Data result=new Data(0);
		node.jjtGetChild(0).jjtAccept(this, result);
		System.out.println(result.value);
		return null;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		debug(node);
		if (node.jjtGetNumChildren() == 2) {
			Data left=new Data(0);
			node.jjtGetChild(0).jjtAccept(this, left);
			Data right=new Data(0);
			node.jjtGetChild(1).jjtAccept(this, right);
//			System.out.println("adding "+left.value+" "+right.value);
			((Data)data).value=left.value+right.value;
		}else{
			node.jjtGetChild(0).jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTTerm node, Object data) {
		debug(node);
		if (node.jjtGetNumChildren() == 2) {
			Data left=new Data(0);
			node.jjtGetChild(0).jjtAccept(this, left);
			Data right=new Data(0);
			node.jjtGetChild(1).jjtAccept(this, right);
//			System.out.println("multiplying "+left.value+" "+right.value);
			((Data)data).value=left.value*right.value;
		}else{
			node.jjtGetChild(0).jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTFactor node, Object data) {
		debug(node);
		if (node.jjtGetNumChildren() == 0) {
			((Data)data).value=Integer.valueOf(node.jjtGetFirstToken().toString());
		} else {
			node.childrenAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTParanthesis node, Object data) {
		debug(node);
		node.jjtGetChild(0).jjtAccept(this, data);
		return null;
	}
	
	public static void main(){
		ExpressionParser.main(new String[]{});
	}

}
