package search;

import java.util.ArrayList;

public class ThreadedSearch<T> implements Runnable {

  private T target;
  private ArrayList<T> list;
  private int begin;
  private int end;
  private Answer answer;

  public ThreadedSearch() {
  }

  private ThreadedSearch(T target, ArrayList<T> list, int begin, int end, Answer answer) {
    this.target=target;
    this.list=list;
    this.begin=begin;
    this.end=end;
    this.answer=answer;
  }

  public boolean parSearch(int numThreads, T target, ArrayList<T> list) throws InterruptedException {
      Answer answer = new Answer();

      Thread[] threads = new Thread[numThreads];
      int range = list.size() // This is the amount of Threads
      int start = 0; // Starting at zero

      //Creates new threads
      for (int i=0; i < numThreads; ++i){
        threads[i] = new Thread(new ThreadedSearch(target, list, start, start + range, answer));
        start = start + range;
        threads[i].start();
      }
    //Hangs the threads and and then combines them.
    for(int i=0;i<numThreads;++i){
      threads[i].join();
    }
    return answer.getAnswer();
  }

  //Looking to see if its done
  public void run() {
    for(int i=0;i<list.size();i++){
      if(list.get(i).equals(target)){
        this.answer.setAnswer(true);
        break;
      }
    }
  }

  private class Answer {
    private boolean response = false;

    public boolean getAnswer() {
      return answer;
    }

    // This has to be synchronized to ensure that no two threads modify
    // this at the same time, possibly causing race conditions.
    public synchronized void setAnswer(boolean newAnswer) {
      answer = newAnswer;
    }
  }

}
