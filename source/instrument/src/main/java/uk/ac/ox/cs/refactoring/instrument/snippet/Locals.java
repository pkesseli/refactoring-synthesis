package uk.ac.ox.cs.refactoring.instrument.snippet;

import java.util.HashMap;
import java.util.Map;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.SignatureAttribute.Type;

/**
 * Helper to provide local variable information of methods.
 */
public final class Locals {
  private Locals() {
  }

  /**
   * Provides all local variables in the method body.
   * 
   * @param code Method body to inspect.
   * @return {@link Map} of local variable names to types.
   */
  public static Map<String, Type> getLocalVariables(final CodeAttribute code) throws BadBytecode {
    final Map<String, Type> result = new HashMap<>();
    final LocalVariableAttribute local = (LocalVariableAttribute) code.getAttribute(LocalVariableAttribute.tag);
    for (int i = 0; i < local.tableLength(); ++i) {
      final String name = local.variableName(i);
      final String signature = local.signature(i);
      final Type type = SignatureAttribute.toTypeSignature(signature);
      result.put(name, type);
    }
    return result;
  }
}
