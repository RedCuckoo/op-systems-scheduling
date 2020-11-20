public class sProcess {
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;

//  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked) {
  public sProcess(int cputime, int delay, int cpudone, int numblocked){
  this.cputime = cputime;
   // this.ioblocking = ioblocking;
this.delay = delay;
    this.cpudone = cpudone;
   // this.ionext = ionext;
   
 this.numblocked = numblocked;
  } 	
}
