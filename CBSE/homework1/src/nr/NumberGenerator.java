package nr;

import java.math.BigDecimal;
import java.util.Random;

public class NumberGenerator implements Number, GeneratorAttributes {

	private Random gen;
	private int limit;

	public NumberGenerator() {
		gen = new Random();
	}

	public Object[] getNumber() {
		int nr = gen.nextInt(limit);
		return new Object[] { new BigDecimal(nr), "" + nr };
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}
}