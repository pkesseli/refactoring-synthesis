package uk.ac.ox.cs.refactoring.rules.concurrency;

public class ExtractLocal {

  String message;

  int numberOfEvents;

  public void processEvents() {
    System.out.println("Starting event processing...");
    synchronized (this) {
      if (numberOfEvents > 0) {
        assert message != null;
        System.out.printf("%i events pending with message `%s`.", numberOfEvents, message);
      }
      // Submit as many events as possible...
    }

    // ...

    synchronized (this) {
      if (numberOfEvents > 0) {
        System.out.printf("%d events still pending...", numberOfEvents);
      }
    }
    // ...
  }

  public synchronized void setEvents(String message, int numberOfEvents) {
    this.message = message;
    this.numberOfEvents = numberOfEvents;
  }

  public void clearEvents() {
    setEvents(null, 0);
  }
}
