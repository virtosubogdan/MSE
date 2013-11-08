package analysis;

import java.io.File;
import java.io.FileInputStream;

import mainframe.MainFrame;
import dynamic.ASTProgram;
import dynamic.MiniJava;

public class MiniJavaCompiler {
	public static void main(String args[]) {
		try {
			new MiniJava(new FileInputStream(new File("./samples/test1.java")));
			ASTProgram root = MiniJava.Program();
			root.dump(">");

			SymbolTableVisitor visitor = new SymbolTableVisitor();
			root.jjtAccept(visitor, null);
			visitor.print();
			
			ClassTable classTable=visitor.getClassTable();
			TypeTable typeTable=visitor.getTypeTable();
			SymbolTableVerifier verifier = new SymbolTableVerifier(typeTable,classTable);
			root.jjtAccept(verifier, null);
			verifier.print();

			System.out.println("Thank you.");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void visualize(ASTProgram root) {
		MainFrame frame = new MainFrame(root);
		frame.setVisible(true);
	}
}
