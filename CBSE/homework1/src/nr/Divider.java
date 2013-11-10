package nr;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public class Divider implements Number, BindingController {

	private Number a;
	private Number b;

	public Object[] getNumber() {
		Object[] left = a.getNumber();
		Object[] right = b.getNumber();
		BigDecimal leftNr = (BigDecimal) left[0];
		BigDecimal rightNr = (BigDecimal) right[0];
		String leftStr = (String) left[1];
		String rightStr = (String) right[1];
		return new Object[] { leftNr.divide(rightNr, 2, RoundingMode.HALF_UP),
				"(" + leftStr + "/" + rightStr + ")" };
	}

	public void bindFc(String arg0, Object arg1)
			throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (arg0.equals("a")) {
			a = (Number) arg1;
		} else if (arg0.equals("b")) {
			b = (Number) arg1;
		}
	}

	public String[] listFc() {
		return new String[] { "a", "b" };
	}

	public Object lookupFc(String arg0) throws NoSuchInterfaceException {
		if (arg0.equals("a")) {
			return a;
		} else if (arg0.equals("b")) {
			return b;
		}
		return null;
	}

	public void unbindFc(String arg0) throws NoSuchInterfaceException,
			IllegalBindingException, IllegalLifeCycleException {
		if (arg0.equals("a")) {
			a = null;
		} else if (arg0.equals("b")) {
			b = null;
		}
	}

}