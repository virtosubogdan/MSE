package analysis;

import java.util.ArrayList;
import java.util.List;

public class TypeTable {

	private List<TypeRecord> m_types;

	public static class TypeRecord {
		private String name;
		private int generatingClassIndex;

		public TypeRecord(String name, int index) {
			this.name = name;
			this.generatingClassIndex = index;
		}

		public String getName() {
			return name;
		}

		public int getGeneratingClassIndex() {
			return generatingClassIndex;
		}
	}

	public TypeTable() {
		m_types = new ArrayList<TypeRecord>();
		m_types.add(new TypeRecord("void", -1));
		m_types.add(new TypeRecord("boolean", -1));
		m_types.add(new TypeRecord("int", -1));
		m_types.add(new TypeRecord("int[]", -1));
	}

	public void addType(String name, int index) {
		m_types.add(new TypeRecord(name, index));
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
					+ type.getGeneratingClassIndex());
		}
	}
}
