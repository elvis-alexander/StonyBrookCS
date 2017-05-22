
public class DownloadJob {
    private int downloadSize;
    // so you can easily keep track of how much has been downloaded
    private int downloadSizeRemaining;
    // timestamp of when the job was requested (so you can calculate average wait time for the download).
    private int timeRequested;
    // stores whether this is a regular or a premium download.
    private boolean isPremium;
    // starts at 1
    private int id;

    public DownloadJob(int id, int timeRequested, int downloadSize, int downloadSizeRemaining, boolean isPremium) {
        this.id = id;
        this.timeRequested = timeRequested;
        this.downloadSize = downloadSize;
        this.downloadSizeRemaining = downloadSizeRemaining;
        this.isPremium = isPremium;
    }
    public String toString() {
        return String.format("#%d: %dMb total, %dMb remaining, Request Time: %d, %s", id, downloadSize, downloadSizeRemaining, timeRequested, isPremium ? "Premium":"Regular");
    }

    public int getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(int downloadSize) {
        this.downloadSize = downloadSize;
    }

    public int getDownloadSizeRemaining() {
        return downloadSizeRemaining;
    }

    public void setDownloadSizeRemaining(int downloadSizeRemaining) {
        this.downloadSizeRemaining = downloadSizeRemaining;
    }

    public int getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(int timeRequested) {
        this.timeRequested = timeRequested;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}