package analysis;

import java.util.ArrayList;
import java.util.List;

public class MemberTable {

	public static enum MemberType {
		FIELD, METHOD;
	}

	public static class MemberReference implements MemberOwner {
		private String name;
		private String[] signature;
		private String returnType;
		private MemberType type;
		private FormalParamTable params;
		private MemberTable members;

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

		@Override
		public void addMember(MemberReference member) {
			if (members == null)
				members = new MemberTable();
			members.addMember(member);
		}
	}

	private List<MemberReference> m_members;

	public MemberTable() {
		m_members = new ArrayList<MemberReference>();
	}

	public void addMember(MemberReference member) {
		for (MemberReference current : m_members) {
			if (current.getName().equals(member.getName())) {
				verify(current, member);
			}
		}
		m_members.add(member);
	}

	private void verify(MemberReference m1, MemberReference m2) {
		if (m1.type == MemberType.FIELD && m2.type == MemberType.FIELD) {
			throw new RuntimeException("Defining field with the same name:"
					+ m2.name);
		}
		if (m1.type != m2.type) {
			return;
		}
		if (m1.params.sameAs(m2.params)) {
			throw new RuntimeException(
					"Defining method with the same signature:" + m2.name);
		}
	}

	public void print(String prefix) {
		System.out.println(prefix + "Printing members");
		int i = 0;
		for (MemberReference member : m_members) {
			System.out.println(prefix + i + " " + member.getName() + " "
					+ member.getSignature().length + " "
					+ member.getReturnType() + " " + member.getMemberType());
			if (member.params != null) {
				member.params.print(prefix + "    ");
			}
			if (member.members != null)
				member.members.print(prefix + "    ");
			i++;
		}
	}
}
