
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_MulticastSocketTest {
  @Test
  void getInterface() throws Exception {
assertThat (synthesiseGPT ("getInterface" , "this.getInterface();" , "\nfinal NetworkInterface networkInterface = this.getNetworkInterface();\nfinal InetAddress inetAddress = networkInterface.getInetAddresses().nextElement();\ninetAddress.getHostAddress();\n" , "java.net.MulticastSocket" , "getInterface") , anyOf (contains ("getNetworkInterface"))) ;
  }

  @Test
  void getLoopbackMode() throws Exception {
assertThat (synthesiseGPT ("getLoopbackMode" , "this.getLoopbackMode();" , "\nfinal DatagramSocket socket = this;\nfinal boolean loopbackMode = socket.getOption(StandardSocketOptions.IP_MULTICAST_LOOP);\n" , "java.net.MulticastSocket" , "getLoopbackMode") , anyOf (contains ("IP_MULTICAST_LOOP") , contains ("getOption"))) ;
  }

  @Test
  void getTTL() throws Exception {
assertThat (synthesiseGPT ("getTTL" , "this.getTTL();" , "\nthis.getTimeToLive();\n" , "java.net.MulticastSocket" , "getTTL") , anyOf (contains ("getTimeToLive"))) ;
  }

  @Test
  void joinGroup() throws Exception {
assertThat (synthesiseGPT ("joinGroup" , "this.joinGroup(param0);" , "\nfinal NetworkInterface networkInterface = NetworkInterface.getByInetAddress(param0);\nfinal InetSocketAddress groupAddress = new InetSocketAddress(param0, 0);\nthis.joinGroup(groupAddress, networkInterface);\n" , "java.net.MulticastSocket" , "joinGroup" , "java.net.InetAddress") , anyOf (contains ("joinGroup"))) ;
  }

  @Test
  void leaveGroup() throws Exception {
assertThat (synthesiseGPT ("leaveGroup" , "this.leaveGroup(param0);" , "\nfinal NetworkInterface networkInterface = NetworkInterface.getByInetAddress(param0);\nfinal InetSocketAddress groupAddress = new InetSocketAddress(param0, 0);\nthis.leaveGroup(groupAddress, networkInterface);\n" , "java.net.MulticastSocket" , "leaveGroup" , "java.net.InetAddress") , anyOf (contains ("leaveGroup"))) ;
  }

  @Test
  void send() throws Exception {
assertThat (synthesiseGPT ("send" , "this.send(param0, param1);" , "\nfinal int ttl = this.getTimeToLive();\nthis.setTimeToLive(newttl);\nthis.send(param0);\nthis.setTimeToLive(ttl);\n" , "java.net.MulticastSocket" , "send" , "java.net.DatagramPacket" , "byte") , allOf(contains("getTimeToLive"), contains("setTimeToLive"), contains("send"))) ;
  }

  @Test
  void setInterface() throws Exception {
assertThat (synthesiseGPT ("setInterface" , "this.setInterface(param0);" , "\nfinal NetworkInterface networkInterface = NetworkInterface.getByInetAddress(param0);\nif (networkInterface != null) {\n    this.setNetworkInterface(networkInterface);\n}\n" , "java.net.MulticastSocket" , "setInterface" , "java.net.InetAddress") , anyOf (contains ("setNetworkInterface"))) ;
  }

  @Test
  void setLoopbackMode() throws Exception {
assertThat (synthesiseGPT ("setLoopbackMode" , "this.setLoopbackMode(param0);" , "\nfinal DatagramSocket socket = this;\nfinal boolean loopbackMode = !param0;\nsocket.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, loopbackMode);\n" , "java.net.MulticastSocket" , "setLoopbackMode" , "boolean") , anyOf (contains ("IP_MULTICAST_LOOP") , contains ("setOption"))) ;
  }

  @Test
  void setTTL() throws Exception {
assertThat (synthesiseGPT ("setTTL" , "this.setTTL(param0);" , "\nthis.setTimeToLive(Byte.toUnsignedInt(param0));\n" , "java.net.MulticastSocket" , "setTTL" , "byte") , anyOf (contains ("setTimeToLive"))) ;
  }
}
