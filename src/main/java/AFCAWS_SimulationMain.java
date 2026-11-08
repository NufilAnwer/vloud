import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AFCAWS_SimulationMain {
    private static final int HOST_COUNT = 13;
    private static final int PES_PER_HOST = 4;
    private static final int VM_COUNT = 50;
    private static final int CLOUDLET_COUNT = 250;

    public static void main(String[] args) {
        try {
            CloudSim.init(3, Calendar.getInstance(), false);

            createDatacenter("Datacenter_0");
            AFCAWSBroker broker = new AFCAWSBroker("AFCAWS_Broker");
            int brokerId = broker.getId();

            broker.submitVmList(createVMs(brokerId, VM_COUNT));
            broker.submitCloudletList(createCloudlets(brokerId, CLOUDLET_COUNT));

            CloudSim.startSimulation();
            List<Cloudlet> finishedCloudlets = broker.getCloudletReceivedList();
            CloudSim.stopSimulation();

            printCloudletList(finishedCloudlets);
            System.out.println("AFCAWS Simulation completed successfully!");
        } catch (Exception exception) {
            exception.printStackTrace();
            System.err.println("The simulation has been terminated due to an unexpected error.");
        }
    }

    private static Datacenter createDatacenter(String name) throws Exception {
        List<Host> hostList = new ArrayList<>();
        for (int hostId = 0; hostId < HOST_COUNT; hostId++) {
            List<Pe> peList = new ArrayList<>();
            for (int peId = 0; peId < PES_PER_HOST; peId++) {
                peList.add(new Pe(peId, new PeProvisionerSimple(1000)));
            }

            hostList.add(new Host(
                    hostId,
                    new RamProvisionerSimple(16384),
                    new BwProvisionerSimple(1000000),
                    1000000,
                    peList,
                    new VmSchedulerTimeShared(peList)));
        }

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                "x86", "Linux", "Xen", hostList, 10.0, 3.0, 0.05, 0.001, 0.0);
        return new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList),
                new LinkedList<Storage>(), 0);
    }

    private static List<Vm> createVMs(int brokerId, int count) {
        List<Vm> vmList = new ArrayList<>();
        for (int vmId = 0; vmId < count; vmId++) {
            vmList.add(new Vm(vmId, brokerId, 1000, 1, 1024, 1000, 10000,
                    "Xen", new CloudletSchedulerTimeShared()));
        }
        return vmList;
    }

    private static List<Cloudlet> createCloudlets(int brokerId, int count) {
        List<Cloudlet> cloudletList = new ArrayList<>();
        Random random = new Random(42);
        for (int cloudletId = 0; cloudletId < count; cloudletId++) {
            Cloudlet cloudlet = new Cloudlet(
                    cloudletId,
                    10000 + random.nextInt(40001),
                    1,
                    300,
                    300,
                    new UtilizationModelFull(),
                    new UtilizationModelFull(),
                    new UtilizationModelFull());
            cloudlet.setUserId(brokerId);
            cloudletList.add(cloudlet);
        }
        return cloudletList;
    }

    private static void printCloudletList(List<Cloudlet> cloudletList) {
        String format = "%-12s %-12s %-12s %-12s %-14s %-12s %-12s%n";
        System.out.printf(format, "Cloudlet ID", "Status", "Datacenter", "VM ID",
                "CPU Time", "Start Time", "Finish Time");

        for (Cloudlet cloudlet : cloudletList) {
            String status = cloudlet.getCloudletStatus() == Cloudlet.SUCCESS ? "SUCCESS" : "FAILED";
            System.out.printf(format,
                    cloudlet.getCloudletId(),
                    status,
                    cloudlet.getResourceId(),
                    cloudlet.getVmId(),
                    cloudlet.getActualCPUTime(),
                    cloudlet.getExecStartTime(),
                    cloudlet.getFinishTime());
        }
    }
}