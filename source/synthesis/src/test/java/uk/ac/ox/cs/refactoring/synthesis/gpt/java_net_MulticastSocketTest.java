
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
assertThat (synthesiseGPT ("this.getInterface();" , "" , "java.net.MulticastSocket" , "getInterface") , anyOf (contains ("getNetworkInterface"))) ;
  }

  @Test
  void getLoopbackMode() throws Exception {
assertThat (synthesiseGPT ("this.getLoopbackMode();" , "\nthis.getTimeToLive() == 0\n;" , "java.net.MulticastSocket" , "getLoopbackMode") , anyOf (contains ("IP_MULTICAST_LOOP") , contains ("getOption"))) ;
  }

  @Test
  void getTTL() throws Exception {
assertThat (synthesiseGPT ("this.getTTL();" , "\nthis.getOption(IP_MULTICAST_TTL)\n;" , "java.net.MulticastSocket" , "getTTL") , Matchers . anything ()) ;
  }

  @Test
  void joinGroup() throws Exception {
assertThat (synthesiseGPT ("this.joinGroup(param0);" , "\nInetAddress groupAddress = InetAddress.getByName(param0);\nNetworkInterface networkInterface = NetworkInterface.getByName(\"eth0\"); // replace with appropriate interface name\nSocketAddress socketAddress = new InetSocketAddress(groupAddress, 0);\nthis.joinGroup(socketAddress, networkInterface);\n;" , "java.net.MulticastSocket" , "joinGroup" , "java.net.InetAddress") , anyOf (contains ("joinGroup"))) ;
  }

  @Test
  void leaveGroup() throws Exception {
assertThat (synthesiseGPT ("this.leaveGroup(param0);" , "" , "java.net.MulticastSocket" , "leaveGroup" , "java.net.InetAddress") , anyOf (contains ("leaveGroup"))) ;
  }

  @Test
  void send() throws Exception {
assertThat (synthesiseGPT ("this.send(param0, param1);" , "\nNetworkInterface networkInterface = // get the desired network interface\nthis.setNetworkInterface(networkInterface);\nthis.send(param0, param1);\n;" , "java.net.MulticastSocket" , "send" , "java.net.DatagramPacket" , "byte") , Matchers . anything ()) ;
  }

  @Test
  void setInterface() throws Exception {
assertThat (synthesiseGPT ("this.setInterface(param0);" , "\nDatagramSocket socket = new DatagramSocket();\nsocket.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, false);\nNetworkInterface networkInterface = NetworkInterface.getByInetAddress(param0);\nsocket.setNetworkInterface(networkInterface);\n;" , "java.net.MulticastSocket" , "setInterface" , "java.net.InetAddress") , anyOf (contains ("setNetworkInterface"))) ;
  }

  @Test
  void setLoopbackMode() throws Exception {
assertThat (synthesiseGPT ("this.setLoopbackMode(param0);" , "this.setTimeToLive(param0 ? 0 : 1);" , "java.net.MulticastSocket" , "setLoopbackMode" , "boolean") , anyOf (contains ("IP_MULTICAST_LOOP") , contains ("setOption"))) ;
  }

  @Test
  void setTTL() throws Exception {
assertThat (synthesiseGPT ("this.setTTL(param0);" , "\nthis.setTimeToLive(param0);\n;" , "java.net.MulticastSocket" , "setTTL" , "byte") , Matchers . anything ()) ;
  }
}
