
public class DownloadScheduler {
    // this is where you store the time elapsed since the start of the simulation
    private int currentTime;
    // this is where your store the time when the simulation should end
    private int simulationEndTime;
    // queue where you will store the regular download jobs
    private DownloadQueue regularQ;
    // queue where you will store the premium download jobs.
    // connected with the regularQ, this forms a two-level priority queue.
    // you will always dequeue from this queue until it is empty before dequeueing from the regularQ.
    private DownloadQueue premiumQ;
    // this will generate jobs at each timestep.
    private DownloadRandomizer random;
    // this is where you store the jobs that are currently downloading.
    // Once a job is done downloading, you can remove it from the array (at the end of the timestep),
    // update the statistics and replace it with a new job.
    private DownloadJob[] currentJobs;
    // this stores the speed of the download in megabytes per second
    private int downloadSpeed;


    // Precondition: the CurrentJobs array, download speed, probabilities of various jobs, and queues have been initialized.
    public DownloadScheduler(int simulationEndTime, int numServers, int downloadSpeed, double premiumProbability, double regularProbability) {
        this.currentTime = 0;
        // the time simulation ends
        this.simulationEndTime = simulationEndTime;
        // the array of DownloadJobs that are currently on the server
        this.currentJobs = new DownloadJob[numServers];
        // download speed number of megabytes server can load up on a spin
        this.downloadSpeed = downloadSpeed;
        // randomize to get file size per time stamp of premium and regular jobs
        this.random = new DownloadRandomizer(premiumProbability, regularProbability);
        // queues to store jobs that are waiting
        this.regularQ = new DownloadQueue();
        this.premiumQ = new DownloadQueue();
    }

    // this method creates a simulation based on the user input.
    public String simulate() {
        // output of simulation
        StringBuffer output = new StringBuffer("--------------------------Simulation Starting--------------------------\n");
        // new jobs size
        int newPremJobSize;
        int newRegJobSize;
        // new jobs
        DownloadJob newPremJob;
        DownloadJob newRegJob;
        // number of jobs
        int count = 1;        // id's indexed by 1 (this is total jobs)
        // keep track of number of jobs currently being served by servers
        int countJobs = 0;

        // stats
        // num of completed jobs
        int totalJobsServed = 0;
        int totalPremJobsServed = 0;
        int totalRegJobsServed = 0;
        // size of completed jobs
        int totalJobsSize = 0;
        int totalPremJobsSize = 0;
        int totalRegJobsSize = 0;
        // wait times
        int totalWaitTimePrem = 0;
        int totalWaitTimeReg = 0;

        // main execution for simulation
        for(currentTime = 0; currentTime <= simulationEndTime; ++currentTime) {
            output.append(String.format("Timestep %d:\n", currentTime) );
            newRegJobSize = random.getRegular();
            newPremJobSize = random.getPremium();
            // generate new reg jobs
            if(newRegJobSize != -1) {
                // int id, int timeRequested, int downloadSize, int downloadSizeRemaining, boolean isPremium
                newRegJob = new DownloadJob(count++, currentTime, newRegJobSize, newRegJobSize, false);
                regularQ.enqueue(newRegJob);
                output.append(String.format("\tNew Regular Job: Job#%d: Size: %dMb\n", newRegJob.getId(), newRegJob.getDownloadSize()));
            } else {
                output.append("\tNew Regular Job: n/a\n");
            }
            // generate new pre jobs
            if(newPremJobSize != -1) {
                // int id, int timeRequested, int downloadSize, int downloadSizeRemaining, boolean isPremium
                newPremJob = new DownloadJob(count++, currentTime, newPremJobSize, newPremJobSize, true);
                premiumQ.enqueue(newPremJob);
                output.append(String.format("\tNew Premium Job: Job#%d: Size: %dMb\n", newPremJob.getId(), newPremJob.getDownloadSize()));
            } else {
                output.append("\tNew Premium Job: n/a\n");
            }

            // some logic to see if anything was completed
            // there should be some check to decrement the download size remaining iff timeStamp != 0
            StringBuffer jobsCompletedMsg = new StringBuffer("");    // provides status for/if any jobs have been completed
            if(currentTime != 0) {
                for(int i = 0; i < currentJobs.length; ++i) {//countJobs; ++i) {
                    if(currentJobs[i] != null) {
                        // keep stats
                        // decrement DownloadJob
                        int timeRemain = currentJobs[i].getDownloadSizeRemaining();
                        currentJobs[i].setDownloadSizeRemaining(timeRemain - downloadSpeed);
                        // account for jobs completed
                        if(currentJobs[i].getDownloadSizeRemaining() <= 0) {
                            // Job  3 finished, Premium job. 140Mb served, Total wait time: 6
                            jobsCompletedMsg.append(String.format("Job %d finished, %s job. %dMb served, Total wait time: %d\n",
                                    currentJobs[i].getId(),
                                    currentJobs[i].isPremium() ? "Premium":"Regular",
                                    currentJobs[i].getDownloadSize(),
                                    currentTime - currentJobs[i].getTimeRequested()
                            ));
                            // STATS
                            ++totalJobsServed;
                            totalJobsSize += currentJobs[i].getDownloadSize();
                            if(currentJobs[i].isPremium()) {
                                ++totalPremJobsServed;
                                totalPremJobsSize += currentJobs[i].getDownloadSize();
                                totalWaitTimePrem += currentTime - currentJobs[i].getTimeRequested();
                            } else {
                                ++totalRegJobsServed;
                                totalRegJobsSize += currentJobs[i].getDownloadSize();
                                totalWaitTimeReg += currentTime - currentJobs[i].getTimeRequested();
                            }
                            currentJobs[i] = null;
                            --countJobs;
                        }
                    }
                }
            }

            // if checks if there are even jobs available to be placed
            // some logic that puts the premium queues into free servers
            while (countJobs < currentJobs.length) {
                // there is a free spot insert all premium jobs
                // if there are no more elements break
                if(premiumQ.isEmpty())
                    break;
                // search for open spot and dequeue
                for(int i = 0; i < currentJobs.length; ++i) {
                    if(currentJobs[i] == null) {
                        // this is an open spot insert!
                        currentJobs[i] = premiumQ.dequeue();
                        ++countJobs;
                        break;
                    }
                }
            }

            // insert regular jobs if available
            while(countJobs < currentJobs.length) {
                // if there are regular jobs in the queue
                if(regularQ.isEmpty())
                    break;
                for(int i = 0; i < currentJobs.length; ++i) {
                    if(currentJobs[i] == null) {
                        // there is an open spot insert! (regular)
                        currentJobs[i] = regularQ.dequeue();
                        ++countJobs;
                        break;
                    }
                }
            }

            /* RegularQueue:[#6:115Mb][#7:135Mb][#9:125Mb][#10:145Mb]
               PremiumQueue:empty */
            if(!regularQ.isEmpty()) {
                DownloadQueue temp = new DownloadQueue();
                while (!regularQ.isEmpty()) {
                    temp.enqueue(regularQ.dequeue());
                }
                output.append("\tRegular Queue:");
                while (!temp.isEmpty()) {
                    DownloadJob j = temp.dequeue();
                    output.append(String.format("[#%d:%dMb]", j.getId(), j.getDownloadSize()));
                    regularQ.enqueue(j);
                }
                output.append("\n");
            } else {
                output.append("\tRegular Queue: empty\n");
            }

            if(!premiumQ.isEmpty()) {
                DownloadQueue temp = new DownloadQueue();
                while (!premiumQ.isEmpty()) {
                    temp.enqueue(premiumQ.dequeue());
                }
                while (!temp.isEmpty()) {
                    output.append("\tPremiumQueue:");
                    DownloadJob j = temp.dequeue();
                    output.append(String.format("[#%d:%dMb]", j.getId(), j.getDownloadSize()));
                    premiumQ.enqueue(j);
                }
                output.append("\n");
            } else {
                output.append("\tPremiumQueue:empty\n");
            }

            for(int i = 0; i < currentJobs.length; ++i) {
                DownloadJob job = currentJobs[i];
                if(job != null) {
                    output.append(String.format("\tServer %d:[%s]\n", i+1, job.toString()));
                } else {
                    output.append(String.format("\tServer %d:idle\n", i+1));
                }
            }
            output.append("\n");
            output.append(jobsCompletedMsg.toString());
            output.append("-----------------------------------------------------------------------");
            output.append("\n");

        }

        output.append("Simulation Ended:\n");
        output.append(String.format("Total Jobs served: %d\n", totalJobsServed));
        output.append(String.format("Total Premium Jobs Served: %d\n", totalPremJobsServed));
        output.append(String.format("Total Regular Jobs Served: %d\n", totalRegJobsServed));
        output.append(String.format("Total Data Served: %dMb\n", totalJobsSize));
        output.append(String.format("Total Premium Data Served: %dMb\n", totalPremJobsSize));
        output.append(String.format("Total Regular Data Served: %dMb\n", totalRegJobsSize));

        if(totalPremJobsServed != 0) {
            output.append(String.format("Average Premium Wait Time: %d\n", totalWaitTimePrem/totalPremJobsServed));
        } else {
            output.append(String.format("Average Premium Wait Time: 0\n"));
        }

        if(totalRegJobsServed != 0) {
            output.append(String.format("Average Regular Wait Time: %d\n", totalWaitTimeReg/totalRegJobsServed));
        } else {
            output.append(String.format("Average Regular Wait Time: 0\n"));
        }
        return output.toString();
    }

    // id,time_requested, total, remain, isPremium

}
