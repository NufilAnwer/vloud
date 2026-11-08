# AFCAWS CloudSim Demo

This project runs the supplied AFCAWS scheduling example with CloudSim 3.0.3. It creates one datacenter, 50 VMs, and 250 synthetic cloudlets.

The scoring metrics in `AFCAWSBroker` are placeholders, and the RL agent uses a rule-based profile selector rather than a trained Q-learning model. This demo does not reproduce the experimental results or WfCommons workflow evaluation described in the accompanying paper.

## Findings and Results

The project records a successful local functional run: all 250 synthetic cloudlets reported `SUCCESS` across 50 VMs in one datacenter. This is a demo result, not a result from the paper or a reproduced experiment.

| Aspect | Paper evaluation described for this project | This repository |
|---|---|---|
| Workload | WfCommons workflow DAG traces | 250 synthetic cloudlets |
| Scheduler comparison | Five baseline schedulers | No baseline comparison |
| Capacity scenarios | Dynamic capacity drops | Not modeled |
| Repetitions | 30 independent runs | One previously recorded functional run |
| Result | Paper's quantitative results are not included here | 250/250 cloudlets completed successfully in the recorded run |

The results are not directly comparable. This demo does not reproduce the paper's workload, baselines, capacity scenarios, or repeated-run protocol. Utilization, fairness, and termination inputs are placeholders, and scheduling scores include random values. Consequently, the successful cloudlet count only confirms the recorded demo run; it does not establish a performance improvement over the paper's baselines.

The paper itself and its citation are not included in this repository, so its reported numerical metrics cannot be independently verified or reproduced from the available project files.

## Requirements

- JDK 25 (the latest LTS release) or newer
- Maven 3.6 or newer

## Run

From the project directory:

```shell
mvn compile exec:java
```