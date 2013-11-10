package nradl;

import java.math.BigDecimal;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;

public class ExpressionsDynamic {
	private GenericFactory cf;
	private Component boot;
	private Component root;
	private ComponentType runnableType;
	private ComponentType generatorType;
	private ComponentType printerType;
	private ComponentType plusType;
	private ComponentType multiplyType;
	private ComponentType dividerType;
	private ComponentType minusType;
	private ComponentType incType;
	private ComponentType decType;
	private BigDecimal limit = new BigDecimal(40);

	private static class Reference {
		private Component comp;
		private String interfaceName;

		private Reference(Component c, String i) {
			comp = c;
			interfaceName = i;
		}
	}

	public ExpressionsDynamic() throws Exception {
		System.out.println("ExpressionsDynamic");
		createTypes();
		createAndBindComponents();
	}

	private void reconfigure() throws Exception {
		for (Component comp : Fractal.getContentController(root)
				.getFcSubComponents()) {
			reconfigure(comp);
		}
	}

	private void reconfigure(Component comp) throws Exception {
		if (has(comp, new String[] { "plusA", "plusB" })) {
			reconfigurePlus(comp);
		} else if (has(comp, new String[] { "minA", "minB" })) {
			reconfigureDiv10(comp);
		} else if (has(comp, new String[] { "incA" })) {
			reconfigureMin2(comp);
		} else if (has(comp, new String[] { "decA" })) {
			reconfigureDiv10(comp);
		} else if (has(comp, new String[] { "mulA", "mulB" })) {
			reconfigureMul(comp);
		} else if (has(comp, new String[] { "divA", "divB" })) {
			reconfigureDiv10(comp);
		}
		// for (String name : Fractal.getBindingController(comp).listFc()) {
		// System.out.println(name);
		// }
		// System.out.println(Fractal.getSuperController(comp)
		// .getFcSuperComponents().length);
	}

	private boolean has(Component comp, String[] intf) {
		try {
			for (String interfata : intf)
				Fractal.getBindingController(comp).lookupFc(interfata);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private void reconfigurePlus(Component plus) throws Exception {
		BigDecimal rslt = (BigDecimal) ((Number) plus.getFcInterface("res"))
				.getNumber()[0];
		if (rslt.compareTo(limit) < 1) {
			return;
		}
		System.out.println("Reconfiguring: "
				+ Fractal.getNameController(plus).getFcName());
		Reference user = clientOf((Interface) plus.getFcInterface("res"));
		Reference plusAComp = serverOf(Fractal.getBindingController(plus)
				.lookupFc("plusA"));
		Reference plusBComp = serverOf(Fractal.getBindingController(plus)
				.lookupFc("plusB"));
		stop();
		Component minus = cf.newFcInstance(minusType, "primitive",
				"nradl.Minus");
		Fractal.getBindingController(user.comp).unbindFc(user.interfaceName);
		Fractal.getBindingController(plus).unbindFc("plusA");
		Fractal.getBindingController(plus).unbindFc("plusB");
		Fractal.getContentController(root).removeFcSubComponent(plus);
		Fractal.getContentController(root).addFcSubComponent(minus);
		Fractal.getBindingController(user.comp).bindFc(user.interfaceName,
				minus.getFcInterface("res"));
		Fractal.getNameController(minus).setFcName("newMinus");
		Fractal.getBindingController(minus).bindFc("minA",
				plusAComp.comp.getFcInterface("res"));
		Fractal.getBindingController(minus).bindFc("minB",
				plusBComp.comp.getFcInterface("res"));
		start();
	}

	private void reconfigureMul(Component mul) throws Exception {
		BigDecimal rslt = (BigDecimal) ((Number) mul.getFcInterface("res"))
				.getNumber()[0];
		if (rslt.compareTo(limit) < 1) {
			return;
		}
		System.out.println("Reconfiguring: "
				+ Fractal.getNameController(mul).getFcName());
		Reference user = clientOf((Interface) mul.getFcInterface("res"));
		Reference mulAComp = serverOf(Fractal.getBindingController(mul)
				.lookupFc("mulA"));
		Reference mulBComp = serverOf(Fractal.getBindingController(mul)
				.lookupFc("mulB"));
		stop();
		Component divider = cf.newFcInstance(dividerType, "primitive",
				"nradl.Divider");
		Component decrement = cf.newFcInstance(decType, "primitive",
				"nradl.Decrement");
		Fractal.getBindingController(user.comp).unbindFc(user.interfaceName);
		Fractal.getBindingController(mul).unbindFc("mulA");
		Fractal.getBindingController(mul).unbindFc("mulB");
		Fractal.getContentController(root).removeFcSubComponent(mul);
		Fractal.getContentController(root).addFcSubComponent(divider);
		Fractal.getContentController(root).addFcSubComponent(decrement);
		Fractal.getBindingController(user.comp).bindFc(user.interfaceName,
				divider.getFcInterface("res"));
		Fractal.getNameController(divider).setFcName("newDivider");
		Fractal.getBindingController(divider).bindFc("divA",
				decrement.getFcInterface("res"));
		Fractal.getNameController(decrement).setFcName("newDecrement");
		Fractal.getBindingController(decrement).bindFc("decA",
				mulAComp.comp.getFcInterface("res"));
		Fractal.getBindingController(divider).bindFc("divB",
				mulBComp.comp.getFcInterface("res"));
		start();
	}

	private void reconfigureDiv10(Component comp) throws Exception {
		BigDecimal rslt = (BigDecimal) ((Number) comp.getFcInterface("res"))
				.getNumber()[0];
		if (rslt.compareTo(limit) < 1) {
			return;
		}
		System.out.println("Reconfiguring: "
				+ Fractal.getNameController(comp).getFcName());
		Reference user = clientOf((Interface) comp.getFcInterface("res"));
		stop();
		Component g10 = cf.newFcInstance(generatorType, "primitive",
				"nradl.StaticNumber");
		Component divider = cf.newFcInstance(dividerType, "primitive",
				"nradl.Divider");
		Fractal.getBindingController(user.comp).unbindFc(user.interfaceName);
		Fractal.getContentController(root).addFcSubComponent(divider);
		Fractal.getContentController(root).addFcSubComponent(g10);
		Fractal.getBindingController(user.comp).bindFc(user.interfaceName,
				divider.getFcInterface("res"));
		Fractal.getNameController(divider).setFcName("newDivider");
		Fractal.getBindingController(divider).bindFc("divA",
				comp.getFcInterface("res"));
		Fractal.getBindingController(divider).bindFc("divB",
				g10.getFcInterface("res"));
		start();
	}

	private void reconfigureMin2(Component comp) throws Exception {
		BigDecimal rslt = (BigDecimal) ((Number) comp.getFcInterface("res"))
				.getNumber()[0];
		if (rslt.compareTo(limit) < 1) {
			return;
		}
		System.out.println("Reconfiguring: "
				+ Fractal.getNameController(comp).getFcName());
		Reference user = clientOf((Interface) comp.getFcInterface("res"));
		stop();
		Component g2 = cf.newFcInstance(generatorType, "primitive",
				"nradl.StaticNumber2");
		Component minus = cf.newFcInstance(minusType, "primitive",
				"nradl.Minus");
		Fractal.getBindingController(user.comp).unbindFc(user.interfaceName);
		Fractal.getContentController(root).addFcSubComponent(minus);
		Fractal.getContentController(root).addFcSubComponent(g2);
		Fractal.getBindingController(user.comp).bindFc(user.interfaceName,
				minus.getFcInterface("res"));
		Fractal.getNameController(minus).setFcName("newMinus");
		Fractal.getBindingController(minus).bindFc("minA",
				comp.getFcInterface("res"));
		Fractal.getBindingController(minus).bindFc("minB",
				g2.getFcInterface("res"));
		start();
	}

	private Reference clientOf(Interface interfata) throws Exception {
		for (Component current : Fractal.getContentController(root)
				.getFcSubComponents()) {
			BindingController bc = Fractal.getBindingController(current);
			for (String inter : bc.listFc()) {
				if (bc.lookupFc(inter) == interfata) {
					return new Reference(current, inter);
				}
			}
		}
		return null;
	}

	private Reference serverOf(Object interfata) throws Exception {
		for (Component current : Fractal.getContentController(root)
				.getFcSubComponents()) {
			try {
				if (current.getFcInterface("res") == interfata) {
					return new Reference(current, "res");
				}
			} catch (NoSuchInterfaceException ex) {
			}
		}
		return null;
	}

	private void createTypes() throws Exception {
		boot = Fractal.getBootstrapComponent();
		TypeFactory tf = Fractal.getTypeFactory(boot);
		runnableType = tf
				.createFcType(new InterfaceType[] { tf.createFcItfType("r",
						"java.lang.Runnable", false, false, false) });
		printerType = tf.createFcType(new InterfaceType[] {
				tf.createFcItfType("r", "java.lang.Runnable", false, false,
						false),
				tf.createFcItfType("n", "nradl.Number", true, false, false) });
		generatorType = tf.createFcType(new InterfaceType[] { tf
				.createFcItfType("res", "nradl.Number", false, false, false) });
		plusType = tf
				.createFcType(new InterfaceType[] {
						tf.createFcItfType("plusA", "nradl.Number", true,
								false, false),
						tf.createFcItfType("plusB", "nradl.Number", true,
								false, false),
						tf.createFcItfType("res", "nradl.Number", false, false,
								false) });
		minusType = tf
				.createFcType(new InterfaceType[] {
						tf.createFcItfType("minA", "nradl.Number", true, false,
								false),
						tf.createFcItfType("minB", "nradl.Number", true, false,
								false),
						tf.createFcItfType("res", "nradl.Number", false, false,
								false) });
		multiplyType = tf
				.createFcType(new InterfaceType[] {
						tf.createFcItfType("mulA", "nradl.Number", true, false,
								false),
						tf.createFcItfType("mulB", "nradl.Number", true, false,
								false),
						tf.createFcItfType("res", "nradl.Number", false, false,
								false) });
		dividerType = tf
				.createFcType(new InterfaceType[] {
						tf.createFcItfType("divA", "nradl.Number", true, false,
								false),
						tf.createFcItfType("divB", "nradl.Number", true, false,
								false),
						tf.createFcItfType("res", "nradl.Number", false, false,
								false) });
		incType = tf
				.createFcType(new InterfaceType[] {
						tf.createFcItfType("incA", "nradl.Number", true, false,
								false),
						tf.createFcItfType("res", "nradl.Number", false, false,
								false) });
		decType = tf
				.createFcType(new InterfaceType[] {
						tf.createFcItfType("decA", "nradl.Number", true, false,
								false),
						tf.createFcItfType("res", "nradl.Number", false, false,
								false) });
	}

	private void start() throws Exception {
		Fractal.getLifeCycleController(root).startFc();

	}

	private void run() throws Exception {
		((java.lang.Runnable) root.getFcInterface("r")).run();
	}

	private void stop() throws Exception {
		Fractal.getLifeCycleController(root).stopFc();
	}

	private void createAndBindComponents() throws Exception {
		cf = Fractal.getGenericFactory(boot);
		root = cf.newFcInstance(runnableType, "composite", null);
		Component printer = cf.newFcInstance(printerType, "primitive",
				"nradl.NumberPrinter");
		Component generator0 = cf.newFcInstance(generatorType, "primitive",
				"nradl.StaticNumberGenerator");
		Component generator1 = cf.newFcInstance(generatorType, "primitive",
				"nradl.StaticNumberGenerator");
		Component generator2 = cf.newFcInstance(generatorType, "primitive",
				"nradl.StaticNumberGenerator");
		Component generator3 = cf.newFcInstance(generatorType, "primitive",
				"nradl.StaticNumberGenerator");
		Component generator4 = cf.newFcInstance(generatorType, "primitive",
				"nradl.StaticNumberGenerator");
		Component plus1 = cf.newFcInstance(plusType, "primitive", "nradl.Plus");
		Component mul1 = cf.newFcInstance(multiplyType, "primitive",
				"nradl.Multiply");
		Component plus2 = cf.newFcInstance(plusType, "primitive", "nradl.Plus");
		Component div1 = cf.newFcInstance(dividerType, "primitive",
				"nradl.Divider");
		Component inc1 = cf.newFcInstance(incType, "primitive",
				"nradl.Increment");
		Fractal.getNameController(root).setFcName("ROOT");
		Fractal.getContentController(root).addFcSubComponent(printer);
		Fractal.getNameController(printer).setFcName("printer");
		Fractal.getContentController(root).addFcSubComponent(generator0);
		Fractal.getNameController(generator0).setFcName("generator0");
		Fractal.getContentController(root).addFcSubComponent(generator1);
		Fractal.getNameController(generator1).setFcName("generator1");
		Fractal.getContentController(root).addFcSubComponent(generator2);
		Fractal.getNameController(generator2).setFcName("generator2");
		Fractal.getContentController(root).addFcSubComponent(generator3);
		Fractal.getNameController(generator3).setFcName("generator3");
		Fractal.getContentController(root).addFcSubComponent(generator4);
		Fractal.getNameController(generator4).setFcName("generator4");
		Fractal.getContentController(root).addFcSubComponent(plus1);
		Fractal.getNameController(plus1).setFcName("plus1");
		Fractal.getContentController(root).addFcSubComponent(mul1);
		Fractal.getNameController(mul1).setFcName("mul1");
		Fractal.getContentController(root).addFcSubComponent(plus2);
		Fractal.getNameController(plus2).setFcName("plus2");
		Fractal.getContentController(root).addFcSubComponent(div1);
		Fractal.getNameController(div1).setFcName("div1");
		Fractal.getContentController(root).addFcSubComponent(inc1);
		Fractal.getNameController(inc1).setFcName("inc1");

		Fractal.getBindingController(root).bindFc("r",
				printer.getFcInterface("r"));

		Fractal.getBindingController(printer).bindFc("n",
				plus2.getFcInterface("res"));

		Fractal.getBindingController(plus2).bindFc("plusA",
				mul1.getFcInterface("res"));
		Fractal.getBindingController(plus2).bindFc("plusB",
				inc1.getFcInterface("res"));

		Fractal.getBindingController(mul1).bindFc("mulA",
				plus1.getFcInterface("res"));
		Fractal.getBindingController(mul1).bindFc("mulB",
				generator0.getFcInterface("res"));

		Fractal.getBindingController(plus1).bindFc("plusA",
				generator1.getFcInterface("res"));
		Fractal.getBindingController(plus1).bindFc("plusB",
				generator2.getFcInterface("res"));

		Fractal.getBindingController(inc1).bindFc("incA",
				div1.getFcInterface("res"));

		Fractal.getBindingController(div1).bindFc("divA",
				generator3.getFcInterface("res"));
		Fractal.getBindingController(div1).bindFc("divB",
				generator4.getFcInterface("res"));

	}

	public static void main(String args[]) {
		try {
			ExpressionsDynamic app = new ExpressionsDynamic();
			app.start();
			app.run();
			app.reconfigure();
			app.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
