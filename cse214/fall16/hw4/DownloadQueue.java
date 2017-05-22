
import java.util.LinkedList;
public class DownloadQueue extends LinkedList<DownloadJob> {

    // - adds d to the end of the queue. You may use either void or boolean for this method.
    public void enqueue(DownloadJob d) {
        addLast(d);
    }

    //  takes the DownloadJob that is at the front of the queue, saves the value, removes the DownloadJob from the queue,
    //  and returns the Value. If the queue was empty, throw an EmptyQueueException.
    public DownloadJob dequeue() {
        DownloadJob j = null;
        try {
            if(isEmpty())
                throw new EmptyQueueException();
            j = getFirst();
            removeFirst();
        } catch (EmptyQueueException e) {
            // System.out.println(e.getMessage());
        }
        return j;
    }

    // takes the DownloadJob that is at the front of the queue, and returns that value to the caller.
    // Does NOT remove that DownloadJob from the queue.   If the queue was empty, throw an EmptyQueueException.
    public DownloadJob peek() {
        DownloadJob j = null;
        try {
            if(isEmpty())
                throw new EmptyQueueException();
            j = getFirst();
        } catch (EmptyQueueException e) {
            // System.out.println(e.getMessage());
        }
        return j;
    }

    // returns true if queue is empty, false otherwise.
    public boolean isEmpty() {
        return super.isEmpty();
    }

}
