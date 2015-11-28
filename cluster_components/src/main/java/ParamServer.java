import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Stores part of the parameters of the model.
 * Supplies the parameters to the worker, and update the parameter as worker send the gradient back.
 */
public class ParamServer implements Runnable {
  // Connections needed: tcp from master to parameter servers
  //                     udp from workers to parameter servers

  private final ServerSocket masterSocket;

  private Map<InetAddress, Integer> workers;
  private WorkerUpdater[] workerUpdaters;
  private float[] parameters;
  private boolean isStopped;

  public static void main(String[] args) {
    if (args.length != 1) {
      printUsage();
      return;
    }

    int port = Integer.parseInt(args[0]);
    Worker worker = new Worker(port);
    worker.run();
  }

  private static void printUsage() {
    System.out.println("usage: <Server port> <Master address>:<Master port>");
  }

  /**
   * Constructs a parameter server.
   *
   * @param workers a map of workers address to ports
   * @param lo      the lower bound of the feature index
   * @param hi      the upper bound of the feature index
   */
  public ParamServer(Map<InetAddress, Integer> workers, int port, int lo, int hi)
      throws IOException {
    this.workers = workers;
    workerUpdaters = new WorkerUpdater[workers.keySet().size()];
    int i = 0;
    for (InetAddress workerAddress : workers.keySet()) {
      workerUpdaters[i] = new WorkerUpdater(parameters, workerAddress, workers.get(workerAddress));
      i++;
    }
    masterSocket = new ServerSocket(port);
    parameters = new float[hi - lo];
    isStopped = true;
  }

  @Override public void run() {
    isStopped = false;
    try (Socket masterConnection = masterSocket.accept();
        BufferedInputStream inBuf = new BufferedInputStream(masterConnection.getInputStream());
        BufferedOutputStream outBuf = new BufferedOutputStream(
            masterConnection.getOutputStream())) {
      initialize(inBuf, outBuf);
      outBuf.write(Message.INITIALIZED.getBytes("UTF-8"));
      outBuf.flush();
      while (!isStopped()) {
        // TODO: communicate with master.
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isStopped() {
    return isStopped;
  }


  /** Stops the parameter server. */
  public void stop() {
    isStopped = true;
    try {
      masterSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initialize(BufferedInputStream inBuf, BufferedOutputStream outBuf) {
    // TODO: setup connection with workers.
  }
}
