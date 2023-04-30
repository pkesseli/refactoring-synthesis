
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_MulticastSocketTest {
  @Test
  void getInterface() throws Exception {
    assertThat(synthesiseGPT("MulticastSocket socket = new MulticastSocket(port);\nsocket.joinGroup(group);\nNetworkInterface networkInterface = socket.getInterface();\n\n", "MulticastSocket socket = new MulticastSocket(port);\nsocket.joinGroup(group);\nNetworkInterface networkInterface = socket.getNetworkInterface();\n", "java.net.MulticastSocket", "getInterface"), anyOf(contains("getNetworkInterface")));
  }

  @Test
  void getLoopbackMode() throws Exception {
    assertThat(synthesiseGPT("boolean loopbackMode = this.getLoopbackMode();\n\n", "boolean loopbackMode = this.getOption(StandardSocketOptions.IP_MULTICAST_LOOP);\n", "java.net.MulticastSocket", "getLoopbackMode"), anyOf(contains("IP_MULTICAST_LOOP"), contains("getOption")));
  }

  @Test
  void getTTL() throws Exception {
    assertThat(synthesiseGPT("int ttl = this.getTTL();\n\n", "int ttl = this.getTimeToLive();\n", "java.net.MulticastSocket", "getTTL"), Matchers.anything());
  }

  @Test
  void joinGroup() throws Exception {
    assertThat(synthesiseGPT("InetAddress group = InetAddress.getByName(\"224.0.0.1\");\nthis.joinGroup(group);\n\n", "InetAddress group = InetAddress.getByName(\"224.0.0.1\");\nNetworkInterface networkInterface = NetworkInterface.getByName(\"eth0\");\nSocketAddress socketAddress = new InetSocketAddress(group, 1234);\nthis.joinGroup(socketAddress, networkInterface);\n", "java.net.MulticastSocket", "joinGroup", "java.net.InetAddress"), anyOf(contains("joinGroup")));
  }

  @Test
  void leaveGroup() throws Exception {
    assertThat(synthesiseGPT("InetAddress group = InetAddress.getByName(\"224.0.0.1\");\nMulticastSocket socket = new MulticastSocket(1234);\nsocket.joinGroup(group);\nsocket.leaveGroup(group);\n\n", "InetAddress group = InetAddress.getByName(\"224.0.0.1\");\nMulticastSocket socket = new MulticastSocket(1234);\nsocket.joinGroup(group);\nNetworkInterface networkInterface = NetworkInterface.getByName(\"eth0\");\nSocketAddress socketAddress = new InetSocketAddress(group, 1234);\nsocket.leaveGroup(socketAddress, networkInterface);\n", "java.net.MulticastSocket", "leaveGroup", "java.net.InetAddress"), anyOf(contains("leaveGroup")));
  }

  @Test
  void send() throws Exception {
    assertThat(synthesiseGPT("this.send(a, b);\n", "int ttl = this.getTimeToLive();\nthis.setTimeToLive(newttl);\nthis.send(a);\nthis.setTimeToLive(ttl);\n", "java.net.MulticastSocket", "send", "java.net.DatagramPacket", "byte"), Matchers.anything());
  }

  @Test
  void setInterface() throws Exception {
    assertThat(synthesiseGPT("InetAddress a = InetAddress.getByName(\"224.0.0.1\");\nthis.setInterface(a);\n\n", "InetAddress a = InetAddress.getByName(\"224.0.0.1\");\nNetworkInterface ni = NetworkInterface.getByInetAddress(a);\nthis.setNetworkInterface(ni);\n", "java.net.MulticastSocket", "setInterface", "java.net.InetAddress"), anyOf(contains("setNetworkInterface")));
  }

  @Test
  void setLoopbackMode() throws Exception {
    assertThat(synthesiseGPT("this.setLoopbackMode(a);\n\n", "this.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, !a);\n", "java.net.MulticastSocket", "setLoopbackMode", "boolean"), anyOf(contains("IP_MULTICAST_LOOP"), contains("setOption")));
  }

  @Test
  void setTTL() throws Exception {
    assertThat(synthesiseGPT("this.setTTL(a);\n\n", "this.setTimeToLive((int) a);\n", "java.net.MulticastSocket", "setTTL", "byte"), Matchers.anything());
  }
}