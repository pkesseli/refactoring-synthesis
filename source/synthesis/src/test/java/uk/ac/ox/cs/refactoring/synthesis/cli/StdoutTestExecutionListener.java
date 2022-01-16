package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.util.function.Supplier;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.listeners.LoggingListener;

class StdoutTestExecutionListener {

  static TestExecutionListener create() {
    return LoggingListener.forBiConsumer(StdoutTestExecutionListener::print);
  }

  private static void print(final Throwable throwable, final Supplier<String> message) {
    if (throwable != null) {
      throwable.printStackTrace(System.out);
    }
    System.out.println(message.get());
  }
}
