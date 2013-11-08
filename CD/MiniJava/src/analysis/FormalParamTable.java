package analysis;

import java.util.ArrayList;
import java.util.List;

public class FormalParamTable {

	public static class FormalParamRecord {
		private String name;
		private String type;

		public FormalParamRecord(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}
	}

	private List<FormalParamRecord> m_params;

	public FormalParamTable() {
		m_params = new ArrayList<FormalParamRecord>();
	}

	public void addParam(FormalParamRecord param) {
		for (FormalParamRecord current : m_params) {
			if (current.name.equals(param.name)) {
				throw new RuntimeException(
						"Cannot add formal parameters with the same name:"
								+ param.name);
			}
		}
		m_params.add(param);
	}

	public int size() {
		return m_params.size();
	}

	public FormalParamRecord getParam(int i) {
		return m_params.get(i);
	}

	public void print(String prefix) {
		System.out.println(prefix + "FormatlParamTable:");
		int i = 0;
		for (FormalParamRecord param : m_params) {
			System.out.println(prefix + i + " " + param.getName() + " "
					+ param.getType());
			i++;
		}
	}

	public boolean sameAs(FormalParamTable table) {
		if (m_params.size() != table.size()) {
			return false;
		}
		for (int i = 0; i < m_params.size(); i++) {
			FormalParamRecord p1 = m_params.get(i);
			FormalParamRecord p2 = table.m_params.get(i);
			if (!p1.type.equals(p2.type)) {
				return false;
			}
		}
		return true;
	}
}
