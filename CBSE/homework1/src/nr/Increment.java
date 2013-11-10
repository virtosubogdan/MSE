package nr;

import java.math.BigDecimal;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public class Increment implements Number, BindingController {

	private Number a;

	public Object[] getNumber() {
		Object[] left = a.getNumber();
		BigDecimal leftNr = (BigDecimal) left[0];
		String leftStr = (String) left[1];
		return new Object[] { leftNr.add(BigDecimal.ONE), "(" + leftStr + "++)" };
	}

	public void bindFc(String arg0, Object arg1)
			throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (arg0.equals("a")) {
			a = (Number) arg1;
		}
	}

	public String[] listFc() {
		return new String[] { "a" };
	}

	public Object lookupFc(String arg0) throws NoSuchInterfaceException {
		if (arg0.equals("a")) {
			return a;
		}
		return null;
	}

	public void unbindFc(String arg0) throws NoSuchInterfaceException,
			IllegalBindingException, IllegalLifeCycleException {
		if (arg0.equals("a")) {
			a = null;
		}
	}

}