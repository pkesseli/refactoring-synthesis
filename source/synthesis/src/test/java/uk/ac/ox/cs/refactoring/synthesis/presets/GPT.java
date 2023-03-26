package uk.ac.ox.cs.refactoring.synthesis.presets;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfigurations;
import uk.ac.ox.cs.refactoring.synthesis.cegis.AffineTransformGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.ClassGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.FontMetricsGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.MethodHandlesLookupGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.ObjectGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.OfflineMulticastSocketGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.OperatingSystemMXBeanGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.RuntimeVersionGenerator;
import uk.ac.ox.cs.refactoring.synthesis.cegis.TextAreaGenerator;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.CounterexampleGenerator;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;
import uk.ac.ox.cs.refactoring.synthesis.state.ClassLoaderClonerStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.StateFactory;
import uk.ac.ox.cs.refactoring.synthesis.verification.FuzzingVerification;

public final class GPT {
    public static boolean verify(SnippetCandidate candidate, final String benchmarkName,
        final String fullyQualifiedClassName, final String methodName, final String... fullyQualifiedParameterClassNames)
        throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException, NoSuchElementException, IOException {


        final MethodIdentifier methodToRefactor = new MethodIdentifier(fullyQualifiedClassName, methodName,
            Arrays.asList(fullyQualifiedParameterClassNames));


        final StateFactory stateFactory = new ClassLoaderClonerStateFactory();
        final SnippetCandidateExecutor executor = new SnippetCandidateExecutor(stateFactory);
        final Invoker invoker = new Invoker(methodToRefactor);

        GeneratorConfiguration generatorConfiguration = GeneratorConfigurations.experimentConfiguration(methodToRefactor);

        final SourceOfRandomness sourceOfRandomness = new SourceOfRandomness(new Random());
        final GeneratorRepository baseRepository = new GeneratorRepository(sourceOfRandomness)
            .register(new ObjectGenerator())
            .register(new ServiceLoaderGeneratorSource())
            .register(new ClassGenerator())
            .register(new MethodHandlesLookupGenerator())
            .register(new OperatingSystemMXBeanGenerator())
            .register(new AffineTransformGenerator())
            .register(new FontMetricsGenerator())
            .register(new OfflineMulticastSocketGenerator());
        baseRepository.register(new RuntimeVersionGenerator(baseRepository))
            .register(new TextAreaGenerator(baseRepository));
        final GeneratorRepository verificationRepository = new GeneratorRepository(sourceOfRandomness)
            .register(new ServiceLoaderGeneratorSource())
            .register(new CounterexampleGenerator(baseRepository, generatorConfiguration.InstanceType,
                generatorConfiguration.ParameterTypes));


        FuzzingVerification<SnippetCandidate> verification = new FuzzingVerification<>(generatorConfiguration, verificationRepository, executor, invoker,
            null);


        // TODO candidate?

        return verification.verify(null).isEmpty();

    }
}