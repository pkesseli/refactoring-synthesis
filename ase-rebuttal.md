We thank reviewers for their insightful comments. Below, we address a few common points before responding to the specific questions from each reviewer. We will add all explanations to the paper.

# Description of Client Programs
Our techniques refactor the deprecated calls without any context, meaning that our refactorings work in any context. As an example, a valid replacement for `date.getHours()` produced by our engine must have equivalent behaviour for any nondeterministic instance of `java.util.Date`. Any deprecated call expression in any repository can thus be trustedly replaced with our generated refactorings. To this end, both our symbolic and neural engines accept a generic method call expression (for example, `date.getHours()`), and return (if successful) a sequence of Java statements involving the original program variables (e.g. `date`). Applying the generated transformation to a code repository is thus a series of straightforward substitutions.

# Artifact Availability
The link to our anonymous repository containing the reproduction bundle was submitted in the HotCRP site (in the section Information about Artifact Availability). The [link](https://anonymous.4open.science/r/refactoring-synthesis-3078/README.md) is still active.

# Novelty / Significance of Findings
To the best of our knowledge, we are the first to use the Java code hints from the deprecation comments for automatic refactoring. We will clarify this in the paper. 

Our research questions are centered around the benefits of deprecation comments. Whether those comments, while designed to be helpful to developers, are helpful to (symbolic or neural) automatic engines is an open question. Our work gives a positive answer to this question and we believe the implications are significant. On the one hand, they motivate the addition of more code hints to the Javadoc, and, on the other hand, they encourage the developers of future automatic refactoring tools to take them into consideration.

# Reviewer A
## Q1 && Q2 (Lacking description of client code)
Please see our discussions in __Description of Client Programs__ above. 

## Equivalence Checking
We intentionally leave aside methods with no semantically equivalent replacements. We will clarify this in the paper.

Our equivalence checking is unsound as discussed in Section 5.2. But this is due to the nature of fuzzing, which, however, is reliably and commonly used for testing real world software.

## Long Execution Time
Our aim was to build a tool that works without context (see __Description of Client Programs__) such that a refactoring generated by our engine works in any position. This impacted the execution time of verification phase. Furthermore, the execution time is configurable with respect to the degree of certainty desired for the current use case. A user can choose to only fuzz for a few seconds or for multiple minutes, changing the number of inputs explored until a solution is accepted.

# Reviewer B
## Comment 4 (Motivation for our approaches)
We formulate the automatic deprecation refactoring problem as a program synthesis problem. In this context, the specification is the original call expression and the related deprecation comments. Component-based synthesis methods, particularly those inspired by Counterexample-Guided Inductive Synthesis (CEGIS), have achieved great success. We believe that those approaches are particularly relevant to our problem of quantifying the usefulness of deprecation comments, since the code hints in those comments can be used to construct a component library.

On the other hand, LLMs have shown great advances in code generation and general software engineering tasks. Investigating the impact of including deprecation comments in the prompt is intriguing. We believe our exploration could provide insights into how these comments enhance or influence the performance of LLMs in generating appropriate code transformations.

## Comment 11 (Lacking detailed explanation for our experimental setup)
### Model selection and hyper parameters
We selected the Claude model family because these models are among the best performing models for code generation [1]. We set the model temperature to 0.2 following prior works on LLMs-based code generation [2]. We will provide more detailed descriptions of our model selection and hyperparameter choices in the final paper.

### Experiments regarding removing code hints
In our experiments, we did not remove code hints from the benchmarks. However, not all benchmarks were documented or contained usable code fragments.  As a result, we split our dataset into two groups: one containing benchmarks with code hints and the other without, as presented in Table 1.

# Reviewer C
## Q1 (Novelty of our approach)
Please see our discussions in __Novelty / Significance of Findings__ above. 

## Q2 (Reproducibility of our prompt)
While the paper reports on results from a single run of the experiments, we originally ran the experiments with Claude 2, Claude 3 multiple times and did not observe significant variance in the number of successful cases. We'll add all the results to the paper.

We logged all prompts and responses from LLMs, and we will enable our artifact to "replay" these prompts and responses.

## Q3 (Did you issue pull requests to see if the transformations are acceptable by the developers?)
No, we didn't, but that is a very good suggestion. Thank you!

## Q4 (Which client programs were used in the evaluation?)
Please see our discussions in __Description of Client Programs__ above. 



[1] Chen, Mark et al. “Evaluating Large Language Models Trained on Code.” ArXiv abs/2107.03374 (2021): n. pag.
[2] Deligiannis, Pantazis & Lal, Akash & Mehrotra, Nikita & Rastogi, Aseem. (2023). Fixing Rust Compilation Errors using LLMs. 

