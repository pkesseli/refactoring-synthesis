package uk.ac.ox.cs.refactoring.benchmarks.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

/**
 * Implements {@link CloseableResource} by registering a {@link Path} to be
 * deleted on {@link CloseableResource#close()};
 */
public class DeletePathResource implements CloseableResource {
  /**
   * File or directory to be (recursively) deleted.
   */
  private final Path path;

  /**
   * @param path
   *          {@link #file}
   */
  public DeletePathResource(final Path path) {
    this.path = path;
  }

  @Override
  public void close() throws IOException {
    try(final Stream<Path> nodes = Files.walk(path)
        .sorted(Comparator.reverseOrder())) {
      for(final Path node : (Iterable<Path>) nodes::iterator)
        Files.delete(node);
    }
  }
}
