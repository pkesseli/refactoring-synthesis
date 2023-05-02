
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_MulticastSocketTest {
  @Test
  void getInterface() throws Exception {
    assertThat(synthesiseGPT("MulticastSocket socket = new MulticastSocket(port);\nNetworkInterface networkInterface = socket.getInterface();\n\n", "MulticastSocket socket = new MulticastSocket(port);\nNetworkInterface networkInterface = NetworkInterface.getByInetAddress(socket.getLocalAddress());\nsocket.setNetworkInterface(networkInterface);\n", "java.net.MulticastSocket", "getInterface"), anyOf(contains("getNetworkInterface")));
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
    assertThat(synthesiseGPT("this.joinGroup(a);\n\n", "InetAddress group = a;\nNetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());\nInetSocketAddress groupAddress = new InetSocketAddress(group, 0);\nthis.joinGroup(groupAddress, networkInterface);\n", "java.net.MulticastSocket", "joinGroup", "java.net.InetAddress"), anyOf(contains("joinGroup")));
  }

  @Test
  void leaveGroup() throws Exception {
    assertThat(synthesiseGPT("this.leaveGroup(a);\n\n", "this.leaveGroup(new InetSocketAddress(a, 0), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));\n", "java.net.MulticastSocket", "leaveGroup", "java.net.InetAddress"), anyOf(contains("leaveGroup")));
  }

  @Test
  void send() throws Exception {
    assertThat(synthesiseGPT("this.send(a, b);\n\n", "InetAddress group = a.getAddress();\nint port = a.getPort();\nbyte[] data = a.getData();\nDatagramPacket packet = new DatagramPacket(data, data.length, group, port);\nthis.send(packet);\n", "java.net.MulticastSocket", "send", "java.net.DatagramPacket", "byte"), Matchers.anything());
  }

  @Test
  void setInterface() throws Exception {
    assertThat(synthesiseGPT("this.setInterface(a);\n\n", "this.setNetworkInterface(NetworkInterface.getByInetAddress(a));\n", "java.net.MulticastSocket", "setInterface", "java.net.InetAddress"), anyOf(contains("setNetworkInterface")));
  }

  @Test
  void setLoopbackMode() throws Exception {
    assertThat(synthesiseGPT("this.setLoopbackMode(a);\n\n", "this.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, a);\n", "java.net.MulticastSocket", "setLoopbackMode", "boolean"), anyOf(contains("IP_MULTICAST_LOOP"), contains("setOption")));
  }

  @Test
  void setTTL() throws Exception {
    assertThat(synthesiseGPT("this.setTTL(a);\n\n", "this.setTimeToLive(Byte.toUnsignedInt(a));\n", "java.net.MulticastSocket", "setTTL", "byte"), Matchers.anything());
  }
}