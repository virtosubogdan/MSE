package analysis;

import java.io.File;
import java.io.FileInputStream;

import dynamic.ASTCall;
import dynamic.ASTClassDecl;
import dynamic.ASTCreation;
import dynamic.ASTExpression;
import dynamic.ASTExpressionBegin;
import dynamic.ASTExpressionList;
import dynamic.ASTFormalList;
import dynamic.ASTIdentifier;
import dynamic.ASTIntegerLiteral;
import dynamic.ASTMainClass;
import dynamic.ASTMethodDecl;
import dynamic.ASTOp;
import dynamic.ASTProgram;
import dynamic.ASTStatement;
import dynamic.ASTType;
import dynamic.ASTVarDecl;
import dynamic.MiniJava;
import dynamic.MiniJavaVisitor;
import dynamic.SimpleNode;

public class SymbolTableVisitor implements MiniJavaVisitor{

	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTProgram node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTMainClass node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTClassDecl node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTVarDecl node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTMethodDecl node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTFormalList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTType node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTExpressionList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String args[]) 
	{
	    System.err.println("Reading from standard input...");
	    try 
	    {
	      	MiniJava p = new MiniJava(new FileInputStream(new File("./samples/test1.java")));
	    	ASTProgram root = p.Program();
	    	root.dump(">");
			System.out.println("Thank you.");
	    } 
	    catch (Exception e) 
	    {									      
			System.err.println("Oops.");
			System.err.println(e.getMessage());
			e.printStackTrace();
	    }
	}

	@Override
	public Object visit(ASTExpressionBegin node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTOp node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCreation node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCall node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTIntegerLiteral node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

}
