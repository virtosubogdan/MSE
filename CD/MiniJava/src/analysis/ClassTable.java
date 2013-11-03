package analysis;

import java.util.ArrayList;
import java.util.List;

import analysis.MemberTable.MemberReference;

public class ClassTable {

	private List<ClassRecord> m_table;
	private TypeTable m_types;

	
	
	public static class ClassRecord {
		private String name;
		private int parentIndex;
		private MemberTable members;

		public ClassRecord(String name, int parentIndex) {
			this.name = name;
			this.parentIndex = parentIndex;
			members = new MemberTable();
		}

		public String getName() {
			return name;
		}

		public int getParentIndex() {
			return parentIndex;
		}
		public void addMember(MemberReference member){
			members.addMember(member);
		}
	}

	public ClassTable(TypeTable types) {
		m_types = types;
		m_table = new ArrayList<ClassRecord>();
		m_table.add(new ClassRecord("Object", -1));
		m_types.addType("Object", -1);
	}

	public ClassRecord addClass(String className, String superclass) {
		for (int i = 0; i < m_table.size(); i++) {
			ClassRecord definedClass = m_table.get(i);
			if (definedClass.getName().equals(superclass)) {
				ClassRecord created = new ClassRecord(className, i);
				m_table.add(created);
				m_types.addType(className, i);
				return created;
			}
		}
		throw new RuntimeException("Class " + superclass
				+ " does not exist, so " + className + " cannot be defined");
	}

	public void print() {
		int i = 0;
		for (ClassRecord clasa : m_table) {
			System.out.println(i + " " + clasa.getName() + " "
					+ clasa.getParentIndex());
			clasa.members.print();
			System.out.println();
			i++;
		}
		
	}
}
