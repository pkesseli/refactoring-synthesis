package uk.ac.ox.cs.refactoring.synthesis.cegis;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Generates instances of OperatingSystemMXBean using
 * {@link ManagementFactory#getOperatingSystemMXBean()}.
 */
public class OperatingSystemMXBeanGenerator extends Generator<OperatingSystemMXBean> {

  /** {@link Generator#Generator(java.lang.Class)} */
  public OperatingSystemMXBeanGenerator() {
    super(OperatingSystemMXBean.class);
  }

  @Override
  public OperatingSystemMXBean generate(SourceOfRandomness random, GenerationStatus status) {
    return (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  }
}
