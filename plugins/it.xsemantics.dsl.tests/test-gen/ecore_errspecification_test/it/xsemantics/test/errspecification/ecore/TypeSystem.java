package it.xsemantics.test.errspecification.ecore;

import it.xsemantics.runtime.ErrorInformation;
import it.xsemantics.runtime.Result;
import it.xsemantics.runtime.RuleApplicationTrace;
import it.xsemantics.runtime.RuleEnvironment;
import it.xsemantics.runtime.RuleFailedException;
import it.xsemantics.runtime.XsemanticsRuntimeSystem;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.util.PolymorphicDispatcher;
import org.eclipse.xtext.xbase.lib.StringExtensions;

public class TypeSystem extends XsemanticsRuntimeSystem {
	public final static String EOBJECTECLASS = "it.xsemantics.test.errspecification.ecore.rules.EObjectEClass";
	public final static String EOBJECTECLASSWITHERRORSPECIFICATION = "it.xsemantics.test.errspecification.ecore.rules.EObjectEClassWithErrorSpecification";
	public final static String ECLASSSUBTYPING = "it.xsemantics.test.errspecification.ecore.rules.EClassSubtyping";

	protected PolymorphicDispatcher<Result<EClass>> typeDispatcher;
	
	protected PolymorphicDispatcher<Result<Boolean>> subtypeDispatcher;

	public TypeSystem() {
		init();
	}

	public void init() {
		typeDispatcher = buildPolymorphicDispatcher1(
			"typeImpl", 3, "|-", ":");
		subtypeDispatcher = buildPolymorphicDispatcher1(
			"subtypeImpl", 4, "|-", "<:");
	}

	public Result<EClass> type(final EObject c) {
		return type(new RuleEnvironment(),
			null, c);
	}
	
	public Result<EClass> type(final RuleEnvironment _environment_,
			final EObject c) {
		return type(_environment_,
			null, c);
	}
	
	public Result<EClass> type(final RuleEnvironment _environment_, final RuleApplicationTrace _trace_,
			final EObject c) {
		try {
			return typeInternal(_environment_, _trace_, c);
		} catch (Exception _e_type) {
			return resultForFailure(_e_type);
		}
	}
	
	public Result<Boolean> subtype(final EClass left, final EClass right) {
		return subtype(new RuleEnvironment(),
			null, left, right);
	}
	
	public Result<Boolean> subtype(final RuleEnvironment _environment_,
			final EClass left, final EClass right) {
		return subtype(_environment_,
			null, left, right);
	}
	
	public Result<Boolean> subtype(final RuleEnvironment _environment_, final RuleApplicationTrace _trace_,
			final EClass left, final EClass right) {
		try {
			return subtypeInternal(_environment_, _trace_, left, right);
		} catch (Exception _e_subtype) {
			return resultForFailure(_e_subtype);
		}
	}


	protected void typeThrowException(String _issue, Exception _ex, final EObject c) 
			throws RuleFailedException {
		
		String _operator_plus = StringExtensions.operator_plus("cannot find ", c);
		String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, "\'s EClass");
		String error = _operator_plus_1;
		throwRuleFailedException(error,
				_issue, _ex,
				new ErrorInformation(null, null));
	}
	
	protected Result<EClass> typeInternal(final RuleEnvironment _environment_, final RuleApplicationTrace _trace_,
			final EObject c) {
		try {
			checkParamsNotNull(c);
			return typeDispatcher.invoke(_environment_, _trace_, c);
		} catch (Exception _e_type) {
			sneakyThrowRuleFailedException(_e_type);
			return null;
		}
	}
	
	protected void subtypeThrowException(String _issue, Exception _ex, final EClass left, final EClass right) 
			throws RuleFailedException {
		
		String _name = left.getName();
		String _operator_plus = StringExtensions.operator_plus(_name, " is not a subtype of ");
		String _name_1 = right.getName();
		String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _name_1);
		String error = _operator_plus_1;
		EObject source = left;
		EStructuralFeature _eStructuralFeature = left.getEStructuralFeature("name");
		EStructuralFeature feature = _eStructuralFeature;
		throwRuleFailedException(error,
				_issue, _ex,
				new ErrorInformation(source, feature));
	}
	
	protected Result<Boolean> subtypeInternal(final RuleEnvironment _environment_, final RuleApplicationTrace _trace_,
			final EClass left, final EClass right) {
		try {
			checkParamsNotNull(left, right);
			return subtypeDispatcher.invoke(_environment_, _trace_, left, right);
		} catch (Exception _e_subtype) {
			sneakyThrowRuleFailedException(_e_subtype);
			return null;
		}
	}
	
	protected Result<EClass> typeImpl(final RuleEnvironment G, final RuleApplicationTrace _trace_,
			final EObject obj) 
			throws RuleFailedException {
		try {
			RuleApplicationTrace _subtrace_ = newTrace(_trace_);
			Result<EClass> _result_ = applyRuleEObjectEClass(G, _subtrace_, obj);
			addToTrace(_trace_, ruleName("EObjectEClass") + stringRepForEnv(G) + " |- " + stringRep(obj) + " : " + stringRep(_result_.getFirst()));
			addAsSubtrace(_trace_, _subtrace_);
			return _result_;
		} catch (Exception e_applyRuleEObjectEClass) {
			typeThrowException(EOBJECTECLASS,
				e_applyRuleEObjectEClass, obj);
			return null;
		}
	}
	
	protected Result<EClass> applyRuleEObjectEClass(final RuleEnvironment G, final RuleApplicationTrace _trace_,
			final EObject obj) 
			throws RuleFailedException {
		EClass eClass = null;
		
		EClass _eClass = obj.eClass();
		eClass = _eClass;
		return new Result<EClass>(eClass);
	}
	
	protected Result<EClass> typeImpl(final RuleEnvironment G, final RuleApplicationTrace _trace_,
			final EClass obj) 
			throws RuleFailedException {
		try {
			RuleApplicationTrace _subtrace_ = newTrace(_trace_);
			Result<EClass> _result_ = applyRuleEObjectEClassWithErrorSpecification(G, _subtrace_, obj);
			addToTrace(_trace_, ruleName("EObjectEClassWithErrorSpecification") + stringRepForEnv(G) + " |- " + stringRep(obj) + " : " + stringRep(_result_.getFirst()));
			addAsSubtrace(_trace_, _subtrace_);
			return _result_;
		} catch (Exception e_applyRuleEObjectEClassWithErrorSpecification) {
			
			String _stringRep = this.stringRep(obj);
			String _operator_plus = StringExtensions.operator_plus("cannot find EClass for EClass ", _stringRep);
			String error = _operator_plus;
			EObject source = obj;
			EStructuralFeature _eContainingFeature = obj.eContainingFeature();
			EStructuralFeature feature = _eContainingFeature;
			throwRuleFailedException(error,
				EOBJECTECLASSWITHERRORSPECIFICATION, e_applyRuleEObjectEClassWithErrorSpecification,
				new ErrorInformation(source, feature));
			return null;
		}
	}
	
	protected Result<EClass> applyRuleEObjectEClassWithErrorSpecification(final RuleEnvironment G, final RuleApplicationTrace _trace_,
			final EClass obj) 
			throws RuleFailedException {
		EClass eClass = null;
		
		EClass _eClass = obj.eClass();
		eClass = _eClass;
		return new Result<EClass>(eClass);
	}
	
	protected Result<Boolean> subtypeImpl(final RuleEnvironment G, final RuleApplicationTrace _trace_,
			final EClass candidate, final EClass superClass) 
			throws RuleFailedException {
		try {
			RuleApplicationTrace _subtrace_ = newTrace(_trace_);
			Result<Boolean> _result_ = applyRuleEClassSubtyping(G, _subtrace_, candidate, superClass);
			addToTrace(_trace_, ruleName("EClassSubtyping") + stringRepForEnv(G) + " |- " + stringRep(candidate) + " <: " + stringRep(superClass));
			addAsSubtrace(_trace_, _subtrace_);
			return _result_;
		} catch (Exception e_applyRuleEClassSubtyping) {
			subtypeThrowException(ECLASSSUBTYPING,
				e_applyRuleEClassSubtyping, candidate, superClass);
			return null;
		}
	}
	
	protected Result<Boolean> applyRuleEClassSubtyping(final RuleEnvironment G, final RuleApplicationTrace _trace_,
			final EClass candidate, final EClass superClass) 
			throws RuleFailedException {
		
		boolean _isSuperTypeOf = superClass.isSuperTypeOf(candidate);
		/* superClass.isSuperTypeOf(candidate) */
		if (!_isSuperTypeOf) {
		  sneakyThrowRuleFailedException("superClass.isSuperTypeOf(candidate)");
		}
		return new Result<Boolean>(true);
	}
}
