public class sProcess {
    public int cputime;
    public int delay;
    public int cpudone;
    public int numblocked;

    public sProcess(int cputime, int delay, int cpudone, int numblocked) {
        this.cputime = cputime;
        this.delay = delay;
        this.cpudone = cpudone;
        this.numblocked = numblocked;
    }
}
