package nradl;

import java.math.BigDecimal;
import java.util.Random;

import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

@Component(provides = {
		@Interface(name = "res", signature = nradl.Number.class),
		@Interface(name = "r", signature = java.lang.Runnable.class) })
public class NumberGenerator implements Number, Generator {

	private Random gen;
	@Attribute(value = "100")
	private String limit = "100";
	private boolean isVerbose=false;
	private final Object NR_SYNC = new Object();
	private int currentNumber;

	public NumberGenerator() {
		gen = new Random();
	}

	public void changeNumber() {
		synchronized (NR_SYNC) {
			currentNumber = gen.nextInt(Integer.valueOf(limit));
			if (isVerbose)
				System.out.println("Generator " + Thread.currentThread().getName() + ": new number: "
						+ currentNumber);
		}
	}

	public Object[] getNumber() {
		synchronized (NR_SYNC) {
			return new Object[] { new BigDecimal(currentNumber),
					"" + currentNumber };
		}
	}

	public void setLimit(String limit) {
		this.limit = limit;
		changeNumber();
	}

	public String getLimit() {
		return limit;
	}

	public void setVerbose(boolean isVerbose) {
		this.isVerbose = isVerbose;
	}

	public boolean isVerbose() {
		return isVerbose;
	}

}