package it.xsemantics.dsl.generator;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import it.xsemantics.dsl.generator.XsemanticsErrorSpecificationGenerator;
import it.xsemantics.dsl.generator.XsemanticsGeneratorExtensions;
import it.xsemantics.dsl.generator.XsemanticsRuleGenerator;
import it.xsemantics.dsl.xsemantics.CheckRule;
import it.xsemantics.dsl.xsemantics.ErrorSpecification;
import it.xsemantics.dsl.xsemantics.JudgmentDescription;
import it.xsemantics.dsl.xsemantics.Rule;
import it.xsemantics.dsl.xsemantics.XsemanticsSystem;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.compiler.output.TreeAppendable;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class XsemanticsSystemGenerator {
  @Inject
  private XsemanticsGeneratorExtensions _xsemanticsGeneratorExtensions;
  
  @Inject
  private XsemanticsRuleGenerator ruleGenerator;
  
  @Inject
  private XsemanticsErrorSpecificationGenerator errSpecGenerator;
  
  public CharSequence compile(final XsemanticsSystem ts) {
    CharSequence _xblockexpression = null;
    {
      JvmGenericType _createJvmType = this._xsemanticsGeneratorExtensions.createJvmType(ts);
      ImportManager _importManager = new ImportManager(true, _createJvmType);
      final ImportManager importManager = _importManager;
      CharSequence _compile = this.compile(ts, importManager);
      _xblockexpression = (_compile);
    }
    return _xblockexpression;
  }
  
  public CharSequence compile(final XsemanticsSystem ts, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    final String applyMethods = this.compileApplyMethods(ts, importManager);
    _builder.newLineIfNotEmpty();
    final String entryMethods = this.compileEntryPointMethods(ts, importManager);
    _builder.newLineIfNotEmpty();
    final String checkMethods = this.compileCheckRules(ts, importManager);
    _builder.newLineIfNotEmpty();
    JvmGenericType _createJvmTypeForTypeSystemBaseClass = this._xsemanticsGeneratorExtensions.createJvmTypeForTypeSystemBaseClass();
    final CharSequence superClass = importManager.serialize(_createJvmTypeForTypeSystemBaseClass);
    _builder.newLineIfNotEmpty();
    final String dispatchers = this.polymorphicDispatchers(ts, importManager);
    _builder.newLineIfNotEmpty();
    final String dispatchersInit = this.polymorphicDispatchersInit(ts, importManager);
    _builder.newLineIfNotEmpty();
    final String packageGen = this._xsemanticsGeneratorExtensions.toPackage(ts);
    _builder.newLineIfNotEmpty();
    {
      int _length = packageGen.length();
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("package ");
        _builder.append(packageGen, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<String> _imports = importManager.getImports();
      boolean _isEmpty = _imports.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.newLine();
        {
          List<String> _imports_1 = importManager.getImports();
          for(final String i : _imports_1) {
            _builder.append("import ");
            _builder.append(i, "");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("public class ");
    String _javaClassName = this._xsemanticsGeneratorExtensions.toJavaClassName(ts);
    _builder.append(_javaClassName, "");
    _builder.append(" extends ");
    _builder.append(superClass, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    EList<Rule> _rules = ts.getRules();
    final Function1<Rule,CharSequence> _function = new Function1<Rule,CharSequence>() {
        public CharSequence apply(final Rule it) {
          CharSequence _ruleIssueDeclaration = XsemanticsSystemGenerator.this._xsemanticsGeneratorExtensions.ruleIssueDeclaration(it, importManager);
          return _ruleIssueDeclaration;
        }
      };
    List<CharSequence> _map = ListExtensions.<Rule, CharSequence>map(_rules, _function);
    String _join = IterableExtensions.join(_map, "\n");
    _builder.append(_join, "	");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append(dispatchers, "	");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    String _javaClassName_1 = this._xsemanticsGeneratorExtensions.toJavaClassName(ts);
    _builder.append(_javaClassName_1, "	");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("init();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void init() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append(dispatchersInit, "		");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append(entryMethods, "	");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append(checkMethods, "	");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append(applyMethods, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String compileEntryPointMethods(final XsemanticsSystem ts, final ImportManager importManager) {
    EList<JudgmentDescription> _judgmentDescriptions = ts.getJudgmentDescriptions();
    final Function1<JudgmentDescription,CharSequence> _function = new Function1<JudgmentDescription,CharSequence>() {
        public CharSequence apply(final JudgmentDescription desc) {
          CharSequence _compileEntryPointMethod = XsemanticsSystemGenerator.this.compileEntryPointMethod(desc, importManager);
          return _compileEntryPointMethod;
        }
      };
    List<CharSequence> _map = ListExtensions.<JudgmentDescription, CharSequence>map(_judgmentDescriptions, _function);
    String _join = IterableExtensions.join(_map, "\n");
    return _join;
  }
  
  public CharSequence compileEntryPointMethod(final JudgmentDescription judgmentDescription, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    final String resultType = this._xsemanticsGeneratorExtensions.resultType(judgmentDescription, importManager);
    _builder.newLineIfNotEmpty();
    String _suffixStartingFrom2 = this._xsemanticsGeneratorExtensions.suffixStartingFrom2(judgmentDescription);
    final String resultForFailureMethod = ("resultForFailure" + _suffixStartingFrom2);
    _builder.newLineIfNotEmpty();
    final String inputParams = this._xsemanticsGeneratorExtensions.inputParameters(judgmentDescription, importManager);
    _builder.newLineIfNotEmpty();
    final String inputArgs = this._xsemanticsGeneratorExtensions.inputArgs(judgmentDescription);
    _builder.newLineIfNotEmpty();
    _builder.append("public ");
    _builder.append(resultType, "");
    _builder.append(" ");
    CharSequence _entryPointMethodName = this._xsemanticsGeneratorExtensions.entryPointMethodName(judgmentDescription);
    _builder.append(_entryPointMethodName, "");
    _builder.append("(");
    _builder.append(inputParams, "");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return ");
    CharSequence _entryPointMethodName_1 = this._xsemanticsGeneratorExtensions.entryPointMethodName(judgmentDescription);
    _builder.append(_entryPointMethodName_1, "	");
    _builder.append("(new ");
    CharSequence _environmentClass = this._xsemanticsGeneratorExtensions.environmentClass(importManager);
    _builder.append(_environmentClass, "	");
    _builder.append("(),");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("null, ");
    _builder.append(inputArgs, "		");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public ");
    _builder.append(resultType, "");
    _builder.append(" ");
    CharSequence _entryPointMethodName_2 = this._xsemanticsGeneratorExtensions.entryPointMethodName(judgmentDescription);
    _builder.append(_entryPointMethodName_2, "");
    _builder.append("(");
    CharSequence _environmentDeclaration = this._xsemanticsGeneratorExtensions.environmentDeclaration(importManager);
    CharSequence _javaFinalParam = this._xsemanticsGeneratorExtensions.javaFinalParam(_environmentDeclaration);
    _builder.append(_javaFinalParam, "");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append(inputParams, "		");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return ");
    CharSequence _entryPointMethodName_3 = this._xsemanticsGeneratorExtensions.entryPointMethodName(judgmentDescription);
    _builder.append(_entryPointMethodName_3, "	");
    _builder.append("(");
    CharSequence _environmentName = this._xsemanticsGeneratorExtensions.environmentName();
    _builder.append(_environmentName, "	");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("null, ");
    _builder.append(inputArgs, "		");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public ");
    _builder.append(resultType, "");
    _builder.append(" ");
    CharSequence _entryPointMethodName_4 = this._xsemanticsGeneratorExtensions.entryPointMethodName(judgmentDescription);
    _builder.append(_entryPointMethodName_4, "");
    _builder.append("(");
    CharSequence _additionalParamsDeclaration = this._xsemanticsGeneratorExtensions.additionalParamsDeclaration(importManager);
    _builder.append(_additionalParamsDeclaration, "");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append(inputParams, "		");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return ");
    CharSequence _entryPointInternalMethodName = this._xsemanticsGeneratorExtensions.entryPointInternalMethodName(judgmentDescription);
    _builder.append(_entryPointInternalMethodName, "		");
    _builder.append("(");
    CharSequence _additionalArgs = this._xsemanticsGeneratorExtensions.additionalArgs();
    _builder.append(_additionalArgs, "		");
    _builder.append(", ");
    _builder.append(inputArgs, "		");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("} catch (");
    CharSequence _exceptionClass = this._xsemanticsGeneratorExtensions.exceptionClass(importManager);
    _builder.append(_exceptionClass, "	");
    _builder.append(" ");
    CharSequence _exceptionVarName = this._xsemanticsGeneratorExtensions.exceptionVarName(judgmentDescription);
    _builder.append(_exceptionVarName, "	");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return ");
    _builder.append(resultForFailureMethod, "		");
    _builder.append("(");
    CharSequence _exceptionVarName_1 = this._xsemanticsGeneratorExtensions.exceptionVarName(judgmentDescription);
    _builder.append(_exceptionVarName_1, "		");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence compileInternalMethod(final JudgmentDescription judgmentDescription, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    final String resultType = this._xsemanticsGeneratorExtensions.resultType(judgmentDescription, importManager);
    _builder.newLineIfNotEmpty();
    final String inputParams = this._xsemanticsGeneratorExtensions.inputParameters(judgmentDescription, importManager);
    _builder.newLineIfNotEmpty();
    final String inputArgs = this._xsemanticsGeneratorExtensions.inputArgs(judgmentDescription);
    _builder.newLineIfNotEmpty();
    _builder.append("protected ");
    _builder.append(resultType, "");
    _builder.append(" ");
    CharSequence _entryPointInternalMethodName = this._xsemanticsGeneratorExtensions.entryPointInternalMethodName(judgmentDescription);
    _builder.append(_entryPointInternalMethodName, "");
    _builder.append("(");
    CharSequence _additionalParamsDeclaration = this._xsemanticsGeneratorExtensions.additionalParamsDeclaration(importManager);
    _builder.append(_additionalParamsDeclaration, "");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append(inputParams, "		");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("checkParamsNotNull(");
    _builder.append(inputArgs, "		");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return ");
    CharSequence _polymorphicDispatcherField = this._xsemanticsGeneratorExtensions.polymorphicDispatcherField(judgmentDescription);
    _builder.append(_polymorphicDispatcherField, "		");
    _builder.append(".invoke(");
    CharSequence _additionalArgs = this._xsemanticsGeneratorExtensions.additionalArgs();
    _builder.append(_additionalArgs, "		");
    _builder.append(", ");
    _builder.append(inputArgs, "		");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("} catch (");
    CharSequence _exceptionClass = this._xsemanticsGeneratorExtensions.exceptionClass(importManager);
    _builder.append(_exceptionClass, "	");
    _builder.append(" ");
    CharSequence _exceptionVarName = this._xsemanticsGeneratorExtensions.exceptionVarName(judgmentDescription);
    _builder.append(_exceptionVarName, "	");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("sneakyThrowRuleFailedException(");
    CharSequence _exceptionVarName_1 = this._xsemanticsGeneratorExtensions.exceptionVarName(judgmentDescription);
    _builder.append(_exceptionVarName_1, "		");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return null;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String compileCheckRules(final XsemanticsSystem ts, final ImportManager importManager) {
    EList<CheckRule> _checkrules = ts.getCheckrules();
    final Function1<CheckRule,CharSequence> _function = new Function1<CheckRule,CharSequence>() {
        public CharSequence apply(final CheckRule rule) {
          CharSequence _compileCheckRule = XsemanticsSystemGenerator.this.ruleGenerator.compileCheckRule(rule, importManager);
          return _compileCheckRule;
        }
      };
    List<CharSequence> _map = ListExtensions.<CheckRule, CharSequence>map(_checkrules, _function);
    String _join = IterableExtensions.join(_map, "\n");
    return _join;
  }
  
  public String compileApplyMethods(final XsemanticsSystem ts, final ImportManager importManager) {
    EList<JudgmentDescription> _judgmentDescriptions = ts.getJudgmentDescriptions();
    final Function1<JudgmentDescription,CharSequence> _function = new Function1<JudgmentDescription,CharSequence>() {
        public CharSequence apply(final JudgmentDescription desc) {
          CharSequence _compilationForJudgmentDescription = XsemanticsSystemGenerator.this.compilationForJudgmentDescription(desc, importManager);
          return _compilationForJudgmentDescription;
        }
      };
    List<CharSequence> _map = ListExtensions.<JudgmentDescription, CharSequence>map(_judgmentDescriptions, _function);
    String _join = IterableExtensions.join(_map, "\n");
    String _plus = (_join + 
      "\n");
    EList<Rule> _rules = ts.getRules();
    final Function1<Rule,String> _function_1 = new Function1<Rule,String>() {
        public String apply(final Rule rule) {
          CharSequence _compileDispatcherImplMethod = XsemanticsSystemGenerator.this.ruleGenerator.compileDispatcherImplMethod(rule, importManager);
          String _string = _compileDispatcherImplMethod.toString();
          CharSequence _compileApplyMethod = XsemanticsSystemGenerator.this.ruleGenerator.compileApplyMethod(rule, importManager);
          String _plus = (_string + _compileApplyMethod);
          return _plus;
        }
      };
    List<String> _map_1 = ListExtensions.<Rule, String>map(_rules, _function_1);
    String _join_1 = IterableExtensions.join(_map_1, "\n");
    String _plus_1 = (_plus + _join_1);
    return _plus_1;
  }
  
  public CharSequence compilationForJudgmentDescription(final JudgmentDescription desc, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _compileErrorSpecification = this.compileErrorSpecification(desc, importManager);
    _builder.append(_compileErrorSpecification, "");
    _builder.newLineIfNotEmpty();
    CharSequence _compileInternalMethod = this.compileInternalMethod(desc, importManager);
    _builder.append(_compileInternalMethod, "");
    return _builder;
  }
  
  public String polymorphicDispatchers(final XsemanticsSystem ts, final ImportManager importManager) {
    EList<JudgmentDescription> _judgmentDescriptions = ts.getJudgmentDescriptions();
    final Function1<JudgmentDescription,CharSequence> _function = new Function1<JudgmentDescription,CharSequence>() {
        public CharSequence apply(final JudgmentDescription desc) {
          CharSequence _polymorphicDispatcherFieldDecl = XsemanticsSystemGenerator.this.polymorphicDispatcherFieldDecl(desc, importManager);
          return _polymorphicDispatcherFieldDecl;
        }
      };
    List<CharSequence> _map = ListExtensions.<JudgmentDescription, CharSequence>map(_judgmentDescriptions, _function);
    String _join = IterableExtensions.join(_map, "\n");
    return _join;
  }
  
  public CharSequence polymorphicDispatcherFieldDecl(final JudgmentDescription judgmentDescription, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("protected ");
    CharSequence _polymorphicDispatcherClass = this._xsemanticsGeneratorExtensions.polymorphicDispatcherClass(importManager);
    _builder.append(_polymorphicDispatcherClass, "");
    _builder.append("<");
    String _resultType = this._xsemanticsGeneratorExtensions.resultType(judgmentDescription, importManager);
    _builder.append(_resultType, "");
    _builder.append("> ");
    CharSequence _polymorphicDispatcherField = this._xsemanticsGeneratorExtensions.polymorphicDispatcherField(judgmentDescription);
    _builder.append(_polymorphicDispatcherField, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public String polymorphicDispatchersInit(final XsemanticsSystem ts, final ImportManager importManager) {
    EList<JudgmentDescription> _judgmentDescriptions = ts.getJudgmentDescriptions();
    final Function1<JudgmentDescription,CharSequence> _function = new Function1<JudgmentDescription,CharSequence>() {
        public CharSequence apply(final JudgmentDescription desc) {
          CharSequence _polymorphicDispatcherInit = XsemanticsSystemGenerator.this.polymorphicDispatcherInit(desc);
          return _polymorphicDispatcherInit;
        }
      };
    List<CharSequence> _map = ListExtensions.<JudgmentDescription, CharSequence>map(_judgmentDescriptions, _function);
    String _join = IterableExtensions.join(_map, "\n");
    return _join;
  }
  
  public CharSequence polymorphicDispatcherInit(final JudgmentDescription judgmentDescription) {
    CharSequence _xblockexpression = null;
    {
      final String relationSymbols = this._xsemanticsGeneratorExtensions.relationSymbolsArgs(judgmentDescription);
      String _xifexpression = null;
      boolean _isEmpty = relationSymbols.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        String _plus = (", " + relationSymbols);
        _xifexpression = _plus;
      } else {
        _xifexpression = "";
      }
      final String relationSymbolArgs = _xifexpression;
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _polymorphicDispatcherField = this._xsemanticsGeneratorExtensions.polymorphicDispatcherField(judgmentDescription);
      _builder.append(_polymorphicDispatcherField, "");
      _builder.append(" = ");
      CharSequence _polymorphicDispatcherBuildMethod = this._xsemanticsGeneratorExtensions.polymorphicDispatcherBuildMethod(judgmentDescription);
      _builder.append(_polymorphicDispatcherBuildMethod, "");
      _builder.append("(");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("\"");
      CharSequence _polymorphicDispatcherImpl = this._xsemanticsGeneratorExtensions.polymorphicDispatcherImpl(judgmentDescription);
      _builder.append(_polymorphicDispatcherImpl, "	");
      _builder.append("\", ");
      CharSequence _polymorphicDispatcherNumOfArgs = this._xsemanticsGeneratorExtensions.polymorphicDispatcherNumOfArgs(judgmentDescription);
      _builder.append(_polymorphicDispatcherNumOfArgs, "	");
      _builder.append(", ");
      _builder.append("\"");
      String _judgmentSymbol = judgmentDescription.getJudgmentSymbol();
      _builder.append(_judgmentSymbol, "	");
      _builder.append("\"");
      _builder.append(relationSymbolArgs, "	");
      _builder.append(");");
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence compileErrorSpecification(final JudgmentDescription judgmentDescription, final ImportManager importManager) {
    CharSequence _xblockexpression = null;
    {
      final ErrorSpecification errorSpecification = judgmentDescription.getError();
      boolean _equals = Objects.equal(errorSpecification, null);
      if (_equals) {
        StringConcatenation _builder = new StringConcatenation();
        return _builder;
      }
      final TreeAppendable b = this._xsemanticsGeneratorExtensions.createAndConfigureAppendable(judgmentDescription, importManager);
      final String error = this.errSpecGenerator.compileErrorOfErrorSpecification(errorSpecification, b);
      final String source = this.errSpecGenerator.compileSourceOfErrorSpecification(errorSpecification, b);
      final String feature = this.errSpecGenerator.compileFeatureOfErrorSpecification(errorSpecification, b);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("protected void ");
      CharSequence _throwExceptionMethod = this._xsemanticsGeneratorExtensions.throwExceptionMethod(judgmentDescription);
      _builder_1.append(_throwExceptionMethod, "");
      _builder_1.append("(String _issue, ");
      CharSequence _exceptionClass = this._xsemanticsGeneratorExtensions.exceptionClass(importManager);
      _builder_1.append(_exceptionClass, "");
      _builder_1.append(" _ex, ");
      String _inputParameters = this._xsemanticsGeneratorExtensions.inputParameters(judgmentDescription, importManager);
      _builder_1.append(_inputParameters, "");
      _builder_1.append(") ");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t\t");
      _builder_1.append("throws ");
      CharSequence _ruleFailedExceptionClass = this._xsemanticsGeneratorExtensions.ruleFailedExceptionClass(importManager);
      _builder_1.append(_ruleFailedExceptionClass, "		");
      _builder_1.append(" {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append(b, "	");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      CharSequence _throwRuleFailedExceptionMethod = this._xsemanticsGeneratorExtensions.throwRuleFailedExceptionMethod();
      _builder_1.append(_throwRuleFailedExceptionMethod, "	");
      _builder_1.append("(");
      _builder_1.append(error, "	");
      _builder_1.append(",");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t\t\t");
      _builder_1.append("_issue, _ex,");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("new ");
      CharSequence _errorInformationClass = this._xsemanticsGeneratorExtensions.errorInformationClass(importManager);
      _builder_1.append(_errorInformationClass, "			");
      _builder_1.append("(");
      _builder_1.append(source, "			");
      _builder_1.append(", ");
      _builder_1.append(feature, "			");
      _builder_1.append("));");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.newLine();
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
  }
}
