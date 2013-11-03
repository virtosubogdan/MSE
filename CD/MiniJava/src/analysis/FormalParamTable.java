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
		m_params.add(param);
	}

	public int size() {
		return m_params.size();
	}

	public FormalParamRecord getParam(int i) {
		return m_params.get(i);
	}

	public void print() {
		System.out.println("FormatlParamTable:");
		int i = 0;
		for (FormalParamRecord param : m_params) {
			System.out.println(i + " " + param.getName() + " "
					+ param.getType());
			i++;
		}
	}
}
