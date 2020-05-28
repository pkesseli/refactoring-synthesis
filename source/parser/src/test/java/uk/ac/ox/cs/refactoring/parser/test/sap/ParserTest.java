package uk.ac.ox.cs.refactoring.parser.test.sap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

import uk.ac.ox.cs.refactoring.benchmarks.resources.Benchmark;
import uk.ac.ox.cs.refactoring.benchmarks.resources.CopyPackage;

@ExtendWith(CopyPackage.class)
public class ParserTest {
  @Test
  public void testParseCompilationUnit(
      @Benchmark("uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter") final Path benchmark)
      throws IOException {
    final SourceRoot source = new SourceRoot(benchmark);
    source.tryToParse();
    final List<CompilationUnit> compilationUnits = source.getCompilationUnits();
    assertEquals(2, compilationUnits.size());
    assertEquals("Original",
        compilationUnits.get(0).getType(0).getName().toString());
    assertEquals("Refactored",
        compilationUnits.get(1).getType(0).getName().toString());
  }
}
