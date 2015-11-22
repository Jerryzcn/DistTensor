import java.net.InetAddress;
import java.util.Map;

/**
 * Created by Jerry on 11/19/2015.
 * <p>
 * Stores part of the parameters of the model.
 * Supplies the parameters to the worker, and update the parameter as worker send the gradient back.
 */
public class ParamServer implements Runnable {

  private Map<InetAddress, Integer> workers;
  private WorkerUpdater[] workerUpdaters;
  private float[] parameters;
  private boolean isStopped;

  /**
   * Constructs a parameter server.
   *
   * @param workers a map of workers address to ports
   * @param lo      the lower bound of the feature index
   * @param hi      the upper bound of the feature index
   */
  public ParamServer(Map<InetAddress, Integer> workers, int lo, int hi) {
    this.workers = workers;
    workerUpdaters = new WorkerUpdater[workers.keySet().size()];
    parameters = new float[hi - lo];
    isStopped = false;
  }

  @Override public void run() {

  }

  /** Stops the parameter server. */
  public void stop() {
    isStopped = true;
  }
}
