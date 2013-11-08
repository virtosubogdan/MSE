package analysis;

import java.util.ArrayList;
import java.util.List;

import analysis.MemberTable.MemberReference;

public class ClassTable {

	private List<ClassRecord> m_table;
	private TypeTable m_types;

	public static class ClassRecord implements MemberOwner {
		private String name;
		private String superclass;
		private MemberTable members;

		public ClassRecord(String name, String superclass) {
			this.name = name;
			this.superclass = superclass;
			members = new MemberTable();
		}

		public String getName() {
			return name;
		}

		public String getSuperclass() {
			return superclass;
		}

		public void addMember(MemberReference member) {
			members.addMember(member);
		}
	}

	public ClassTable(TypeTable types) {
		m_types = types;
		m_table = new ArrayList<ClassRecord>();
		m_table.add(new ClassRecord("Object", ""));
		m_types.addType("Object", "-1");
	}

	public ClassRecord addClass(String className, String superclass) {
		ClassRecord created = new ClassRecord(className, superclass);
		m_table.add(created);
		m_types.addType(className, superclass);
		return created;
	}

	public void print(String prefix) {
		int i = 0;
		for (ClassRecord clasa : m_table) {
			System.out.println(prefix + i + " " + clasa.getName() + " "
					+ clasa.getSuperclass());
			clasa.members.print(prefix + "    ");
			System.out.println();
			i++;
		}
	}

	public ClassRecord getClass(String className) {
		for (ClassRecord current : m_table) {
			if (current.getName().equals(className)) {
				return current;
			}
		}
		return null;
	}

	public void checkClass(String className) {
		ClassRecord clasa = null;
		for (ClassRecord current : m_table) {
			if (current.getName().equals(className)) {
				if (clasa != null) {
					throw new RuntimeException("Class " + className
							+ " defined multiple times!");
				}
				clasa = current;
			}
		}
		do {
			clasa = getSuperClass(clasa);
		} while (clasa != null && !clasa.getName().equals("Object")
				&& !clasa.getName().equals(className));
		if (clasa == null) {
			throw new RuntimeException("Class hierarchy for " + className
					+ " does not exist!");
		}
		if (clasa.getName().equals(className)) {
			throw new RuntimeException("Class " + className
					+ "'s hierachy tree is cyclic!");
		}
	}

	private ClassRecord getSuperClass(ClassRecord clasa) {
		for (ClassRecord current : m_table) {
			if (current.getName().equals(clasa.getSuperclass())){
				return current;
			}
		}
		return null;
	}
}
