package analysis;

import java.util.ArrayList;
import java.util.List;

public class TypeTable {

	private List<TypeRecord> m_types;

	public static class TypeRecord {
		private String name;
		private String parent;

		public TypeRecord(String name, String parent) {
			this.name = name;
			this.parent = parent;
		}

		public String getName() {
			return name;
		}

		public String getParent() {
			return parent;
		}
	}

	public TypeTable() {
		m_types = new ArrayList<TypeRecord>();
		m_types.add(new TypeRecord("void", "-1"));
		m_types.add(new TypeRecord("boolean", "-1"));
		m_types.add(new TypeRecord("int", "-1"));
		m_types.add(new TypeRecord("int[]", "-1"));
	}

	public void addType(String name, String parent) {
		m_types.add(new TypeRecord(name, parent));
	}

	public int getType(String name) {
		for (int i = 0; i < m_types.size(); i++) {
			TypeRecord type = m_types.get(i);
			if (type.getName().equals(name)) {
				return i;
			}
		}
		throw new RuntimeException("Cannot find type " + name);
	}

	public void print() {
		System.out.println("Type table");
		for (int i = 0; i < m_types.size(); i++) {
			TypeRecord type = m_types.get(i);
			System.out.println(i + " " + type.getName() + " "
					+ type.getParent());
		}
	}
}
