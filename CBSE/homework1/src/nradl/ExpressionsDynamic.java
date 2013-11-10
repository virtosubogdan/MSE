package nradl;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;

public class ExpressionsDynamic {
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

	public ExpressionsDynamic() throws Exception {
		System.out.println("ExpressionsDynamic");
		createTypes();
		createAndBindComponents();
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
				.createFcItfType("n", "nradl.Number", false, false, false) });
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
		((java.lang.Runnable) root.getFcInterface("r")).run();
	}

	private void createAndBindComponents() throws Exception {
		GenericFactory cf = Fractal.getGenericFactory(boot);
		root = cf.newFcInstance(runnableType, "composite", null);
		Component printer = cf.newFcInstance(printerType, "primitive",
				"nradl.NumberPrinter");
		Component generator = cf.newFcInstance(generatorType, "primitive",
				"nradl.NumberGenerator");

		Component plus1 = cf.newFcInstance(plusType, "primitive", "nradl.Plus");
		Component mul1 = cf.newFcInstance(multiplyType, "primitive",
				"nradl.Multiply");
		Component plus2 = cf.newFcInstance(plusType, "primitive", "nradl.Plus");
		Component div1 = cf.newFcInstance(dividerType, "primitive",
				"nradl.Divider");
		Component inc1 = cf.newFcInstance(incType, "primitive",
				"nradl.Increment");

		Fractal.getContentController(root).addFcSubComponent(printer);
		Fractal.getContentController(root).addFcSubComponent(generator);
		Fractal.getContentController(root).addFcSubComponent(plus1);
		Fractal.getContentController(root).addFcSubComponent(mul1);
		Fractal.getContentController(root).addFcSubComponent(plus2);
		Fractal.getContentController(root).addFcSubComponent(div1);
		Fractal.getContentController(root).addFcSubComponent(inc1);

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
				generator.getFcInterface("n"));

		Fractal.getBindingController(plus1).bindFc("plusA",
				generator.getFcInterface("n"));
		Fractal.getBindingController(plus1).bindFc("plusB",
				generator.getFcInterface("n"));

		Fractal.getBindingController(inc1).bindFc("incA",
				div1.getFcInterface("res"));

		Fractal.getBindingController(div1).bindFc("divA",
				generator.getFcInterface("n"));
		Fractal.getBindingController(div1).bindFc("divB",
				generator.getFcInterface("n"));

	}

	public static void main(String args[]) {
		try {
			ExpressionsDynamic app = new ExpressionsDynamic();
			app.start();
			app.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
