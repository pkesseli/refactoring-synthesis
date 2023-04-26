package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.function.Function;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

class CollectionsGenerator {

  private CollectionsGenerator() {
  }

  static Object createCollection(final SourceOfRandomness random, final TypeResolver typeResolver,
      final Function<ResolvedType, Object> generateElement, final ResolvedType resolvedType) {
    final Class<?> cls = resolvedType.getErasedType();
    if (List.class == cls || ArrayList.class == cls)
      return createCollection(random, typeResolver, generateElement, resolvedType, new ArrayList<>());
    else if (LinkedList.class == cls)
      return createCollection(random, typeResolver, generateElement, resolvedType, new LinkedList<>());
    else if (Set.class == cls || HashSet.class == cls)
      return createCollection(random, typeResolver, generateElement, resolvedType, new HashSet<>());
    else if (TreeSet.class == cls)
      return createCollection(random, typeResolver, generateElement, resolvedType, new TreeSet<>());
    else if (Vector.class == cls)
      return createCollection(random, typeResolver, generateElement, resolvedType, new Vector<>());
    else if (Map.class == cls || HashMap.class == cls)
      return createMap(random, typeResolver, generateElement, resolvedType, new HashMap<>());
    else if (TreeMap.class == cls)
      return createMap(random, typeResolver, generateElement, resolvedType, new TreeMap<>());

    return null;
  }

  private static Object createCollection(final SourceOfRandomness random, final TypeResolver typeResolver,
      final Function<ResolvedType, Object> generateElement, final ResolvedType resolvedType,
      final Collection<Object> createdObject) {
    final List<ResolvedType> typeParameters = resolvedType.getTypeParameters();
    final ResolvedType elementType;
    if (typeParameters.isEmpty())
      elementType = typeResolver.resolve(Object.class);
    else
      elementType = typeParameters.get(0);

    final int length = getRandomArrayLength(random);
    for (int i = 0; i < length; ++i)
      createdObject.add(generateElement.apply(elementType));
    return createdObject;
  }

  private static Object createMap(final SourceOfRandomness random, final TypeResolver typeResolver,
      final Function<ResolvedType, Object> generateElement, final ResolvedType resolvedType,
      final Map<Object, Object> createdObject) {
    final List<ResolvedType> typeParameters = resolvedType.getTypeParameters();
    final ResolvedType keyType;
    if (typeParameters.isEmpty())
      keyType = typeResolver.resolve(Object.class);
    else
      keyType = typeParameters.get(0);
    final ResolvedType elementType;
    if (typeParameters.size() < 2)
      elementType = typeResolver.resolve(Object.class);
    else
      elementType = typeParameters.get(1);

    final int length = getRandomArrayLength(random);
    for (int i = 0; i < length; ++i) {
      final Object key = generateElement.apply(keyType);
      final Object value = generateElement.apply(elementType);
      createdObject.put(key, value);
    }
    return createdObject;
  }

  /**
   * Provides a random length for a collection or array to use.
   * 
   * @param random Random source.
   * @return Length to use.
   */
  static int getRandomArrayLength(final SourceOfRandomness random) {
    return (int) random.nextByte(Byte.MIN_VALUE, Byte.MAX_VALUE) - (int) Byte.MIN_VALUE;
  }
}
