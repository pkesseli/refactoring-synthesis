package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.github.javaparser.ast.type.PrimitiveType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;

public class Cast {

  private static class ByteToInt extends CastExpression<Byte> {

    private ByteToInt(final IExpression operand) {
      super(operand, PrimitiveType.intType());
    }

    @Override
    public Object evaluate(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
        InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return evaluateOperand(context).intValue();
    }
  }

  private static class IntToByte extends CastExpression<java.lang.Integer> {

    private IntToByte(final IExpression operand) {
      super(operand, PrimitiveType.byteType());
    }

    @Override
    public Object evaluate(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
        InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return evaluateOperand(context).byteValue();
    }
  }

  /**
   * Helper to register cast expressions in Java candidate builders.
   * 
   * @param components Directory in which to register all int expressions.
   */
  public static void register(final JavaComponents components) {
    final PrimitiveType byteType = PrimitiveType.byteType();
    final PrimitiveType intType = PrimitiveType.intType();
    final JavaLanguageKey byteExpression = JavaLanguageKeys.expression(byteType);
    final JavaLanguageKey intExpression = JavaLanguageKeys.expression(intType);
    components.nonnull(intType, new ConstructorComponent<>(Arrays.asList(byteExpression), ByteToInt.class));
    components.nonnull(byteType, new ConstructorComponent<>(Arrays.asList(intExpression), IntToByte.class));
  }
}
