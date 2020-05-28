package uk.ac.ox.cs.refactoring.benchmarks.resources;

import java.nio.file.Path;
import java.text.MessageFormat;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

/**
 * Marks files for deletion after a unit test {@link ExtensionContext} expires.
 * This is useful to mark methods for deletion after a JUnit test method or
 * class is complete.
 */
public class PathDeletionStore {
  /**
   * Pattern for keys used in the JUnit extension context store. We register our
   * directories in the extension store to trigger their automatic removal after
   * a test or test class finishes.
   */
  private static final String FILE_KEY_PATTERN = "file-to-delete.{0}";

  /**
   * Files to be deleted are registered with the JUnit extension context store.
   * Since files are deleted through {@link DeletePathResource#close()}, we
   * never actually look up the resources again by their key. Thus we just need
   * a unique counter to make sure all directories can be stored in the
   * associated map.
   */
  private int nextUniqueStoreId = 0;

  /**
   * The given {@link Path} to a file or directory will be automatically deleted
   * when the given {@link ExtensionContext} expires, i.e. the associated test
   * method or test class is complete.
   *
   * @param context
   *          {@link ExtensionContext} during the duration of which the file
   *          should exist.
   * @param path
   *          {@link Path} to a file or directory. Non-empty directories will be
   *          deleted recursively.
   */
  public void registerForDeletion(final ExtensionContext context,
      final Path path) {
    final Namespace namespace = Namespace.create(this);
    final Store store = context.getStore(namespace);
    final DeletePathResource tempFile = new DeletePathResource(path);
    final int id = nextUniqueStoreId++;
    final String key = MessageFormat.format(FILE_KEY_PATTERN, id);
    store.put(key, tempFile);
  }
}
