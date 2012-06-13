/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.pogofish.jadt.emitter;

import static com.pogofish.jadt.ast.PrimitiveType._IntType;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Primitive;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.pogofish.jadt.Version;
import com.pogofish.jadt.ast.*;
import com.pogofish.jadt.emitter.DocEmitter;
import com.pogofish.jadt.emitter.DummyDataTypeEmitter;
import com.pogofish.jadt.emitter.StandardDocEmitter;
import com.pogofish.jadt.target.StringTargetFactory;
import com.pogofish.jadt.util.Util;


/**
 * Test the StandardDocEmitter.  Only shallow testing is performed here, the pieces of the doc emitter
 * are tested more thoroughly elsewhere
 *
 * @author jiry
 */
public class DocEmitterTest {
	private static final String VERSION = new Version().getVersion();
	private static final String BOILERPLATE = 
    "This file was generated based on EmitterTest using JADT version " + VERSION + " (http://jamesiry.github.com/JADT/). Please do not modify directly.\n" +
    "\n" +
    "The source was parsed as: \n" +
    "\n";

	
    private static final String FULL_HEADER =
    "package some.package;\n" +
    "\n" +
    "import wow.man;\n" +
    "import flim.flam;\n" +
    "\n" +
    "/*\n" +
    BOILERPLATE +
    "package some.package\n" +
    "\n" +
    "import wow.man\n" +
    "import flim.flam\n" +
    "\n" +
    "FooBar =\n" +
    "    Foo(int yeah, String hmmm)\n" +
    "  | Bar\n" +
    "Whatever =\n" +
    "    Whatever\n" +
    "\n" +
    "*/\n";

    private static final String NO_PACKAGE_HEADER =
    "import wow.man;\n" +
    "import flim.flam;\n" +
    "\n" +
    "/*\n" +
    BOILERPLATE +
    "import wow.man\n" +
    "import flim.flam\n" +
    "\n" +
    "FooBar =\n" +
    "    Foo(int yeah, String hmmm)\n" +
    "  | Bar\n" +
    "Whatever =\n" +
    "    Whatever\n" +
    "\n" +
    "*/\n";

    
    private static final String NO_IMPORTS_HEADER =
    "package some.package;\n" +
    "\n" +
    "/*\n" +
    BOILERPLATE +
    "package some.package\n" +
    "\n" +
    "FooBar =\n" +
    "    Foo(int yeah, String hmmm)\n" +
    "  | Bar\n" +
    "Whatever =\n" +
    "    Whatever\n" +
    "\n" +
    "*/\n";
    
    private static final String FOOBAR = 
    "FooBar";
    
    private static final String WHATEVER =
    "Whatever";
    
    /**
     * Test a reasonably fully document with pacakge, imports, and datatypes
     */
    @Test
    public void testFull() {
        final Doc doc = new Doc("EmitterTest", "some.package", list("wow.man", "flim.flam"), list(
                new DataType("FooBar", Util.<String>list(), list(
                        new Constructor("Foo", list(
                                new Arg(_Primitive(_IntType()), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                )),
                new DataType("Whatever", Util.<String>list(), list(
                        new Constructor("Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringTargetFactory factory = new StringTargetFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("some.package.FooBar");
        assertEquals(FULL_HEADER+FOOBAR, foobar);
        assertEquals(FULL_HEADER+WHATEVER, results.get("some.package.Whatever"));
    }

    /**
     * Test a doc with no imports
     */
    @Test
    public void testNoImports() {
        final Doc doc = new Doc("EmitterTest", "some.package", Util.<String>list(), list(
                new DataType("FooBar", Util.<String>list(), list(
                        new Constructor("Foo", list(
                                new Arg(_Primitive(_IntType()), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                )),
                new DataType("Whatever", Util.<String>list(), list(
                        new Constructor("Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringTargetFactory factory = new StringTargetFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("some.package.FooBar");
        assertEquals(NO_IMPORTS_HEADER+FOOBAR, foobar);
        assertEquals(NO_IMPORTS_HEADER+WHATEVER, results.get("some.package.Whatever"));
    }
    
    /**
     * Test a doc with no package declaration
     */
    @Test
    public void testNoPackage() {
        final Doc doc = new Doc("EmitterTest", "", list("wow.man", "flim.flam"), list(
                new DataType("FooBar", Util.<String>list(), list(
                        new Constructor("Foo", list(
                                new Arg(_Primitive(_IntType()), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                )),
                new DataType("Whatever", Util.<String>list(), list(
                        new Constructor("Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringTargetFactory factory = new StringTargetFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("FooBar");
        assertEquals(NO_PACKAGE_HEADER+FOOBAR, foobar);
        assertEquals(NO_PACKAGE_HEADER+WHATEVER, results.get("Whatever"));
    }
}