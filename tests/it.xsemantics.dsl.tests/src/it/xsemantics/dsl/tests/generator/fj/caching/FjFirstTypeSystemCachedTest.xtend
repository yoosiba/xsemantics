package it.xsemantics.dsl.tests.generator.fj.caching

import com.google.inject.Inject
import com.google.inject.Provider
import it.xsemantics.example.fj.FJInjectorProvider
import it.xsemantics.example.fj.fj.Program
import it.xsemantics.test.fj.caching.FjFirstCachedTypeSystem
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

@InjectWith(typeof(FJInjectorProvider))
@RunWith(typeof(XtextRunner))
class FjFirstTypeSystemCachedTest extends FjFirstTypeSystemManualCachedTest {
	
	@Inject Provider<FjFirstCachedTypeSystem> cachedTypeSystemProvider
	
	@Inject extension ParseHelper<Program>
	
	override createCachedTypeSystem() {
		cachedTypeSystemProvider.get()
	}
	
	@Test
	override void testCachedSuperclassesUsedInTypecheck() {
		'''
		class A {}
		class B extends A {}
		class C extends B {}
		class D extends A {}
		'''
		.parse => [
			assertSuperclassesCachedInTypecheck("C",
'''
CheckClass: [] |- class C extends B {}
 superclasses(class C extends B {}) = [class B extends A {}, class A {}]
 Fields: [] ||- class B extends A {} >> []
  superclasses(class B extends A {}) = [class A {}]
 Methods: [] ||~ class B extends A {} >> []
'''
			)
		
			// we reuse the superclasses computation
			// implicitly also when computing fields
			assertSuperclassesCachedInTypecheck("C",
'''
CheckClass: [] |- class C extends B {}
 cached:
  superclasses(class C extends B {}) = [class B extends A {}, class A {}]
 cached:
  Fields: [] ||- class B extends A {} >> []
 cached:
  Methods: [] ||~ class B extends A {} >> []
'''
			)
		]
	}

	@Test
	override void testXsemanticsCacheListener() {
		'''
		class A {}
		class B extends A {}
		class C extends B {}
		class D extends A {}
		'''
		.parse => [
			assertCacheStatisticsInTypecheck(traceLogger,"C",
'''
''',
'''
superclasses(class C extends B {}) = [class B extends A {}, class A {}]
superclasses(class B extends A {}) = [class A {}]
Fields: [] ||- class B extends A {} >> []
Methods: [] ||~ class B extends A {} >> []
'''
			)
			
			// the second time, superclasses(class B extends A {}) = [class A {}]
			// is not used at all
			assertCacheStatisticsInTypecheck(traceLogger,"C",
'''
superclasses(class C extends B {}) = [class B extends A {}, class A {}]
Fields: [] ||- class B extends A {} >> []
Methods: [] ||~ class B extends A {} >> []
''',
'''
superclasses(class C extends B {}) = [class B extends A {}, class A {}]
superclasses(class B extends A {}) = [class A {}]
Fields: [] ||- class B extends A {} >> []
Methods: [] ||~ class B extends A {} >> []
'''
			)
		]
		cachedTypeSystem.cache.removeListener(traceLogger)
	}

	@Test
	def void testFailureCached() {
		'''
		class A {}
		class B extends A {}
		class C extends B {}
		class D extends A {}
		'''
		.parse => [
			assertSubtypingCachedFailedResult("D", "C", "")

			// also failures are cached because entry point methods are cached
			assertSubtypingCachedFailedResult("D", "C", "subclass: Result failed")
		]
	}
}