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
package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.util.Util.list;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.target.StringTarget;
import pogofish.jadt.util.Util;

/**
 * Test the StandardDataTypeEmitter
 *
 * @author jiry
 */
public class DataTypeEmitterTest {
    private static final String HEADER = "/*header*/\n";
    private static final String MULTI_CONSTRUCTOR = 
    "public abstract class FooBar/* type arguments */ {\n" +
    "\n" +
    "   private FooBar() {\n" +
    "   }\n" +
    "\n" +
    "/* factory FooBar Foo */\n" +
    "/* factory FooBar Bar */\n" +
    "\n" +
    "   public static interface Visitor/* type arguments */ {\n" +
    "      ResultType visit(Foo/* type arguments */ x);\n" +
    "      ResultType visit(Bar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class VisitorWithDefault/* type arguments */ implements Visitor/* type arguments */ {\n" +
    "      @Override\n" +
    "      public ResultType visit(Foo/* type arguments */ x) { return getDefault(x); }\n" +
    "\n" +
    "      @Override\n" +
    "      public ResultType visit(Bar/* type arguments */ x) { return getDefault(x); }\n" +    
    "\n" +
    "      protected abstract ResultType getDefault(FooBar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static interface VoidVisitor/* type arguments */ {\n" +
    "      void visit(Foo/* type arguments */ x);\n" +
    "      void visit(Bar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class VoidVisitorWithDefault/* type arguments */ implements VoidVisitor/* type arguments */ {\n" +
    "      @Override\n" +
    "      public void visit(Foo/* type arguments */ x) { doDefault(x); }\n" +
    "\n" +
    "      @Override\n" +
    "      public void visit(Bar/* type arguments */ x) { doDefault(x); }\n" +    
    "\n" +
    "      protected abstract void doDefault(FooBar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +    
    "/* declaration FooBar Foo */\n" +
    "\n" +
    "/* declaration FooBar Bar */\n" +
    "\n" +
    "   public abstract <ResultType> ResultType accept(Visitor/* type arguments */ visitor);\n" +
    "\n" +
    "   public abstract void accept(VoidVisitor/* type arguments */ visitor);\n" +
    "\n" +
    "}";
    
    private static final String SINGLE_CONSTRUCTOR = 
    "public final class FooBar/* type arguments */ {\n" +
    "\n" +
    "/* constructor factory FooBar Foo FooBar*/\n" +
    "\n" +
    "/* constructor method FooBar*/\n" +
    "\n" +
    "/* hashCode method FooBar*/\n" +
    "\n" +
    "/* equals method FooBar*/\n" +
    "\n" +
    "/* toString method FooBar*/\n" +
    "\n" +
    "}";
    
    /**
     * Test that multiple constructors does its thing correctly
     */
    @Test
    public void testMultipleConstructors() {
        final DataType fooBar = new DataType("FooBar", Util.<String>list(), list(
                new Constructor("Foo", list(new Arg(_Ref(_ClassType("Integer", Util.<RefType> list())), "yeah"),
                        new Arg(_Ref(_ClassType("String", Util.<RefType> list())), "hmmm"))), new Constructor("Bar",
                        Util.<Arg> list())));

        final StringTarget target = new StringTarget();
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter(new DummyClassBodyEmitter(),
                    new DummyConstructorEmitter());

            emitter.emit(target, fooBar, HEADER);
        } finally {
            target.close();
        }
        assertEquals(HEADER + MULTI_CONSTRUCTOR, target.result());
    }
   
    /**
     * Test that single constructor does its thing properly
     */
    @Test
    public void testSingleConstructor() {
        final DataType fooBar = new DataType("FooBar", Util.<String>list(), list(new Constructor("Foo", list(
                new Arg(_Ref(_ClassType("Integer", Util.<RefType> list())), "yeah"),
                new Arg(_Ref(_ClassType("String", Util.<RefType> list())), "hmmm")))));

        final StringTarget target = new StringTarget();
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter(new DummyClassBodyEmitter(),
                    new DummyConstructorEmitter());

            emitter.emit(target, fooBar, HEADER);
        } finally {
            target.close();
        }
        assertEquals(HEADER + SINGLE_CONSTRUCTOR, target.result());
    }
   
}
