import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;
import java.util.function.Consumer;

public class Benchmark {

  public static void main(String[] args) throws Exception {

    File file = new File("dataset.txt");
    Scanner sc = new Scanner(file);

    ArrayList<Integer> list = new ArrayList<Integer>();
    while (sc.hasNextLine()) {
      list.add(Integer.parseInt(sc.nextLine()));
    }

    sc.close();

    int[] arr = new int[list.size()];
    for (int i = 0; i < list.size(); i++) {
      arr[i] = list.get(i);
    }

    boolean printInTimeFormat = true; // set to false to print in nanoseconds

    // set according to args
    if (args.length > 0) {
      if (args[0].equals("-ns"))
        printInTimeFormat = false;
      if (args[0].equals("-hms"))
        printInTimeFormat = true;
    }

    int[] arrCopy;
    for (int i = 1; i < 35 + 1; i++) {
      System.out.println("Sorting " + i * 100 + " elements");
      arrCopy = Arrays.copyOf(arr, i * 250);
      printResultsForArray(arrCopy, printInTimeFormat);
    }

    System.out.println("Sorting " + 1000000 + " elements");
    arrCopy = Arrays.copyOf(arr, 1000000);
    printResultsForArray(arrCopy, printInTimeFormat);
  }

  static void printResultsForArray(int[] arr, boolean printInTimeFormat) {
    // pretty print results in a table
    System.out.println(
        "===============================================================================================================================================================================================================================================================================================================================================================================================================================================================================================");
    System.out.printf("%-20s ", "Sorting Algorithm");
    for (int i = 0; i < 20; i++)
      System.out.printf("%-20s ", "Iteration " + (i + 1));
    System.out.printf("%-20s\n", printInTimeFormat ? "Average Time (HH:MM:SS:ms:ns)" : "Average Time (ns)");

    printResults("Bubble Sort", arr, SortingAlgorithm::bubbleSort, printInTimeFormat);
    printResults("Selection Sort", arr, SortingAlgorithm::selectionSort, printInTimeFormat);
    printResults("Insertion Sort", arr, SortingAlgorithm::insertionSort, printInTimeFormat);
    printResults("Quick Sort", arr, SortingAlgorithm::quickSort, printInTimeFormat);
    printResults("Arrays.sort", arr, Arrays::sort, printInTimeFormat);

    System.out.println(
        "===============================================================================================================================================================================================================================================================================================================================================================================================================================================================================================");
    System.out.println();
  }

  private static void printResults(String sortName, int[] arr, Consumer<int[]> sortFunction,
      boolean printInTimeFormat) {
    System.out.printf("%-20s ", sortName);
    long totalDuration = 0;
    for (int i = 0; i < 20; i++) {
      long duration = getTimedResults(arr, sortFunction);
      System.out.printf("%-20s ", printInTimeFormat ? formatDuration(duration) : duration);
      totalDuration += duration;
    }
    System.out.printf("%-20s\n",
        printInTimeFormat ? formatDuration(totalDuration / 20L) : totalDuration / 20L);
  }

  private static long getTimedResults(int[] arr, Consumer<int[]> sortFunction) {
    long start = System.nanoTime();
    sortFunction.accept(Arrays.copyOf(arr, arr.length));
    long end = System.nanoTime();
    return end - start;
  }

  // format duration to HH:MM:SS:ms:ns
  private static String formatDuration(long duration) {
    long hours = duration / 3600000000000L;
    duration -= hours * 3600000000000L;

    long minutes = duration / 60000000000L;
    duration -= minutes * 60000000000L;

    long seconds = duration / 1000000000L;
    duration -= seconds * 1000000000L;

    long milliseconds = duration / 1000000L;
    duration -= milliseconds * 1000000L;

    long nanoseconds = duration;

    return String.format("%02d:%02d:%02d:%03d:%06d", hours, minutes, seconds, milliseconds, nanoseconds);
  }

}

class SortingAlgorithm {

  public static void bubbleSort(int[] A) {
    int n = A.length;

    for (int i = 0; i < n; i++)
      for (int j = n - 1; j > i; j--)
        if (A[j] < A[j - 1]) {
          // swap(A[j], A[j - 1])
          int temp = A[j];
          A[j] = A[j - 1];
          A[j - 1] = temp;
        }
  }

  public static void insertionSort(int A[]) {
    int n = A.length;
    for (int i = 1; i < n; i++) {
      int temp = A[i];
      int j = i - 1;

      while (j >= 0 && temp < A[j]) {
        A[j + 1] = A[j];
        j--;
      }
      A[j + 1] = temp;
    }
  }

  // selection sort
  public static void selectionSort(int[] A) {
    int n = A.length;
    for (int i = 0; i < n - 1; i++) {
      int small = i;
      for (int j = i + 1; j < n; j++)
        if (A[j] < A[small])
          small = j;

      // swap(A[i], A[small])
      int temp = A[i];
      A[i] = A[small];
      A[small] = temp;
    }
  }

  public static void quickSort(int[] arr) {
    quickSort(arr, 0, arr.length - 1);
  }

  public static void quickSort(int[] arr, int left, int right) {
    int i = left, j = right;
    int pivot = arr[(left + right) / 2];

    while (i <= j) {
      while (arr[i] < pivot)
        i++;
      while (arr[j] > pivot)
        j--;
      if (i <= j) {
        // swap(A[i], A[j])
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        i++;
        j--;
      }
    }

    if (left < j)
      quickSort(arr, left, j);
    if (i < right)
      quickSort(arr, i, right);

  }
}
