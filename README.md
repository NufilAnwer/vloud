# Adaptive Fair and Capacity-Aware Workflow Scheduler (AFCAWS)

AFCAWS is a workflow scheduling approach for cloud datacenters that combines workflow urgency, resource fit, dominant-resource fairness, and expected machine availability. A reinforcement-learning agent selects a scheduling weight profile based on observed cluster conditions, while a safety validator is intended to enforce resource and workflow-dependency constraints.

This repository contains a small CloudSim demonstration inspired by AFCAWS. It is not the full experimental implementation described in the accompanying paper text. The paper’s results below are reported results, not results reproduced by this repository.

## How AFCAWS is designed

The proposed architecture has three layers:

1. **Workload and event generator** — supplies scientific workflow DAGs and capacity-event traces.
2. **AFCAWS core engine** — monitors cluster state, selects a weight profile, scores scheduling choices, and applies fallback and safety checks.
3. **Simulation environment** — models the datacenter, hosts, and virtual machines in CloudSim.

The scheduling score combines four factors:

```text
Score = W1(Urgency) + W2(Resource fit) + W3(Fairness) + W4(Availability)
```

The paper describes a Q-learning agent whose state includes ready-queue length, average utilization, variation in dominant resource shares, and recent task termination rate. It selects among five predefined profiles favoring urgency, fairness, availability, resource fit, or a balanced combination. Its reward is designed to favor utilization while penalizing terminations, makespan, and unfairness.

## Reported evaluation

The supplied paper text describes experiments with WfCommons Pegasus workflows (Epigenomics, Montage, and SIPHT), five scheduling baselines, capacity events, and 30 independent runs per configuration. It reports the following results:

| Scheduler | Average completion time (s) | Makespan (s) | Deadline misses | Jain fairness | CPU utilization |
|---|---:|---:|---:|---:|---:|
| FCFS | 4,250 | 18,400 | 14.2% | 0.45 | 65.2% |
| DRF | 3,890 | 16,100 | 8.5% | 0.98 | 72.4% |
| Workflow-Aware (WA) | 2,950 | 14,200 | 5.1% | 0.62 | 81.5% |
| Fixed-Weight AFCAWS | 2,820 | 13,900 | 4.4% | 0.85 | 86.3% |
| AFCAWS | 2,522 | 12,850 | 2.1% | 0.89 | 89.1% |

For stochastic capacity events, the paper reports 116 task terminations and 315 CPU-hours of repeated work for AFCAWS, compared with 150 terminations and 420 CPU-hours for the Capacity-Aware baseline. It also reports an average scheduling decision overhead of 12 ms for AFCAWS.

These figures summarize the supplied paper text; they have not been independently verified or reproduced from this repository. The paper text describes CloudSim 5.0 with custom extensions, while this project currently depends on CloudSim 3.0.3.

## What this repository implements

The current demo creates one datacenter with 13 hosts, 50 VMs, and 250 synthetic cloudlets. Its broker uses a simplified profile selector, but the scoring inputs are placeholders and several score components return random values. It does not currently implement the paper’s WfCommons DAG workloads, baseline comparisons, dynamic capacity events, trained Q-learning, or 30-run evaluation. A successful demo run therefore does not validate or reproduce the reported paper results.

## Requirements

- JDK 25 or newer
- Maven 3.6 or newer

## Run the demo

From the project directory:

```shell
mvn compile exec:java
```

The program prints the status and timing information for each simulated cloudlet.

## References

The supplied paper text cites the following background work:

1. Calheiros, R. N., et al. (2011). “CloudSim: a toolkit for modeling and simulation of cloud computing environments and evaluation of resource provisioning algorithms.” *Software: Practice and Experience*, 41(1), 23–50.
2. Ghodsi, A., et al. (2011). “Dominant Resource Fairness: Fair Allocation of Multiple Resource Types.” *NSDI*.
3. Ferreira da Silva, R., et al. (2020). “WfCommons: A framework for enabling scientific workflow research and development.” *Future Generation Computer Systems*, 112, 1–13.
4. Mao, M., & Humphrey, M. (2012). “Auto-scaling to minimize cost and meet application deadlines in cloud workflows.” *SC*.
5. Cheng, M., Li, J., & Nazarian, S. (2018). “DRL-cloud: Deep reinforcement learning based resource provisioning and task scheduling for cloud environments.” *GLSVLSI*.
6. Topcuoglu, H., Hariri, S., & Wu, M. Y. (2002). “Performance-effective and low-complexity task scheduling for heterogeneous computing.” *IEEE Transactions on Parallel and Distributed Systems*, 13(3), 260–274.
