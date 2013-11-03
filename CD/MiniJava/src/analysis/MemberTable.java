package analysis;

import java.util.ArrayList;
import java.util.List;

public class MemberTable {

	public static enum MemberType {
		FIELD, METHOD;
	}

	public static class MemberReference {
		private String name;
		private String[] signature;
		private String returnType;
		private MemberType type;
		private FormalParamTable params;

		public MemberReference(String name, String[] signature,
				String returnType, MemberType type, FormalParamTable params) {
			this.name = name;
			this.signature = signature;
			this.returnType = returnType;
			this.type = type;
			this.params = params;
		}

		public String getName() {
			return name;
		}

		public String[] getSignature() {
			return signature;
		}

		public String getReturnType() {
			return returnType;
		}

		public MemberType getMemberType() {
			return type;
		}
	}

	private List<MemberReference> m_members;

	public MemberTable() {
		m_members = new ArrayList<MemberReference>();
	}

	public void addMember(MemberReference member) {
		m_members.add(member);
	}

	public void print() {
		System.out.println("Printing members");
		int i = 0;
		for (MemberReference member : m_members) {
			System.out.println(i + " " + member.getName() + " "
					+ member.getSignature().length + " "
					+ member.getReturnType() + " " + member.getMemberType());
			if (member.params != null) {
				member.params.print();
			}
			i++;
		}
	}
}
