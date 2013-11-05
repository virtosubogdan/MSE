package analysis;

import java.io.File;
import java.io.FileInputStream;

import mainframe.MainFrame;
import analysis.ClassTable.ClassRecord;
import analysis.FormalParamTable.FormalParamRecord;
import analysis.MemberTable.MemberReference;
import analysis.MemberTable.MemberType;
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

public class SymbolTableVisitor implements MiniJavaVisitor {

	private ClassTable m_classTable;
	private TypeTable m_types;

	private class IdentifierData {
		private String value;
	}

	private class TypeData {
		private String value;
	}

	public SymbolTableVisitor() {
		m_types = new TypeTable();
		m_classTable = new ClassTable(m_types);
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTProgram node, Object data) {
		System.out.println("visitProgram");
		node.jjtGetChild(0).jjtAccept(this, data);
		for (int i = 1; i < node.jjtGetNumChildren(); i++)
			node.jjtGetChild(i).jjtAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTMainClass node, Object data) {
		System.out.println("visitMainClass");
		IdentifierData nameIden = new IdentifierData();
		node.jjtGetChild(0).jjtAccept(this, nameIden);
		IdentifierData argumentsIdentifier = new IdentifierData();
		node.jjtGetChild(1).jjtAccept(this, argumentsIdentifier);
		node.jjtGetChild(2).jjtAccept(this, data);
		ClassRecord mainClass = m_classTable.addClass(nameIden.value, "Object");
		FormalParamTable params = new FormalParamTable();
		params.addParam(new FormalParamRecord(argumentsIdentifier.value,
				"int[]"));
		MemberReference mainFunction = new MemberReference("main",
				new String[] { "main", "int[]" }, "void", MemberType.METHOD,
				params);
		mainClass.addMember(mainFunction);
		return null;
	}

	@Override
	public Object visit(ASTClassDecl node, Object data) {
		System.out.println("visitClassDecl");
		int startDeclaration = 1;
		IdentifierData nameIden = new IdentifierData();
		node.jjtGetChild(0).jjtAccept(this, nameIden);
		ClassRecord clasa;
		if (node.jjtGetChild(1) instanceof ASTIdentifier) {
			IdentifierData argumentsIdentifier = new IdentifierData();
			node.jjtGetChild(1).jjtAccept(this, argumentsIdentifier);
			startDeclaration = 2;
			clasa = m_classTable.addClass(nameIden.value,
					argumentsIdentifier.value);
		} else {
			clasa = m_classTable.addClass(nameIden.value, "Object");
		}
		for (int i = startDeclaration; i < node.jjtGetNumChildren(); i++)
			node.jjtGetChild(i).jjtAccept(this, clasa);
		return null;
	}

	@Override
	public Object visit(ASTVarDecl node, Object data) {
		System.out.println("visitVarDecl");
		MemberOwner owner = (MemberOwner) data;
		ASTType tip = (ASTType) node.jjtGetChild(0);
		TypeData tipData = new TypeData();
		tip.jjtAccept(this, tipData);
		IdentifierData idData = new IdentifierData();
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		identifier.jjtAccept(this, idData);
		MemberReference defined = new MemberReference(idData.value,
				new String[] { idData.value }, tipData.value, MemberType.FIELD,
				null);
		owner.addMember(defined);
		return null;
	}

	@Override
	public Object visit(ASTMethodDecl node, Object data) {
		System.out.println("visitMethodDecl");
		MemberOwner clasa = (MemberOwner) data;
		ASTType tip = (ASTType) node.jjtGetChild(0);
		TypeData tipData = new TypeData();
		tip.jjtAccept(this, tipData);
		IdentifierData idData = new IdentifierData();
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		identifier.jjtAccept(this, idData);
		String[] formalList;
		int start;
		FormalParamTable params = new FormalParamTable();
		if (node.jjtGetChild(2) instanceof ASTFormalList) {
			start = 3;
			((ASTFormalList) node.jjtGetChild(2)).jjtAccept(this, params);
			formalList = new String[params.size() + 1];
			formalList[0] = idData.value;
			for (int i = 0; i < params.size(); i++) {
				formalList[i + 1] = params.getParam(i).getType();
			}
		} else {
			start = 2;
			formalList = new String[] { idData.value };
		}
		MemberReference defined = new MemberReference(idData.value, formalList,
				tipData.value, MemberType.METHOD, params);
		clasa.addMember(defined);
		for (int i = start; i < node.jjtGetNumChildren(); i++)
			node.jjtGetChild(i).jjtAccept(this, defined);
		return null;
	}

	@Override
	public Object visit(ASTFormalList node, Object data) {
		FormalParamTable params = (FormalParamTable) data;
		for (int i = 0; i < node.jjtGetNumChildren(); i += 2) {
			ASTType tip = (ASTType) node.jjtGetChild(0);
			TypeData tipData = new TypeData();
			tip.jjtAccept(this, tipData);
			IdentifierData idData = new IdentifierData();
			ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
			identifier.jjtAccept(this, idData);
			params.addParam(new FormalParamRecord(idData.value, tipData.value));
		}
		return null;
	}

	@Override
	public Object visit(ASTType node, Object data) {
		TypeData tipData = (TypeData) data;
		if (node.jjtGetNumChildren() == 0) {
			if (node.jjtGetFirstToken().toString() == "boolean") {
				tipData.value = "boolean";
			} else if (node.jjtGetLastToken().toString() == "]") {
				tipData.value = "int[]";
			} else {
				tipData.value = "int";
			}
		} else {
			IdentifierData typeId = new IdentifierData();
			node.jjtGetChild(0).jjtAccept(this, typeId);
			tipData.value = typeId.value;
		}
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
		System.out.println("identifier");
		IdentifierData iden = (IdentifierData) data;
		iden.value = node.jjtGetFirstToken().toString();
		System.out.println(node.jjtGetFirstToken().toString());
		return null;
	}

	public static void main(String args[]) {
		try {
			new MiniJava(new FileInputStream(new File("./samples/test1.java")));
			ASTProgram root = MiniJava.Program();
			root.dump(">");
			MainFrame frame = new MainFrame(root);
			// frame.setVisible(true);
			SymbolTableVisitor visitor = new SymbolTableVisitor();
			root.jjtAccept(visitor, null);
			visitor.m_classTable.print("");
			visitor.m_types.print();
			System.out.println("Thank you.");
		} catch (Exception e) {
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
