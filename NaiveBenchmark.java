import java.io.File;
import java.io.FileNotFoundException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Scanner;
import java.util.function.Consumer;

public class NaiveBenchmark {

  // format duration to HH:MM:SS:ms:ns
  private static String formatDuration(Duration duration) {
    return String.format("%02d:%02d:%02d:%03d:%06d", duration.toHoursPart(), duration.toMinutesPart(),
        duration.toSecondsPart(), duration.toMillisPart(), duration.toNanosPart() - duration.toMillisPart() * 1000000);
  }

  private static Duration getTimedResults(int[] arr, Consumer<int[]> sortFunction) {
    long start = System.nanoTime();
    sortFunction.accept(Arrays.copyOf(arr, arr.length));
    long end = System.nanoTime();
    return Duration.ofNanos(end - start);
  }

  private static void printResults(String sortName, int[] arr, Consumer<int[]> sortFunction) {
    System.out.printf("%-20s ", sortName);
    System.err.printf("%-20s ", sortName);
    Duration totalDuration = Duration.ZERO;
    for (int i = 0; i < 20; i++) {
      Duration duration = getTimedResults(arr, sortFunction);
      System.out.printf("%-20s ", formatDuration(duration));
      System.err.printf("%-20s ", formatDuration(duration));
      totalDuration = totalDuration.plus(duration);
    }
    System.out.printf("%-20s\n", formatDuration(totalDuration.dividedBy(20)));
    System.err.printf("%-20s\n", formatDuration(totalDuration.dividedBy(20)));
  }

  public static void main(String[] args) throws FileNotFoundException {

    File file = new File("dataset.txt");
    Scanner sc = new Scanner(file);

    List<Integer> list = new ArrayList<Integer>();
    while (sc.hasNextLine()) {
      list.add(Integer.parseInt(sc.nextLine()));
    }

    sc.close();

    int[] arr = new int[list.size()];
    for (int i = 0; i < list.size(); i++) {
      arr[i] = list.get(i);
    }

    int[] arr1k = Arrays.copyOf(arr, 1000);
    int[] arr10k = Arrays.copyOf(arr, 10000);
    int[] arr50k = Arrays.copyOf(arr, 50000);
    int[] arr100k = Arrays.copyOf(arr, 100000);
    int[] arr500k = Arrays.copyOf(arr, 500000);

    System.out.println("Sorting 1000 elements");
    printResultsForArray(arr1k);

    System.out.println("Sorting 10000 elements");
    printResultsForArray(arr10k);

    System.out.println("Sorting 50000 elements");
    printResultsForArray(arr50k);

    System.out.println("Sorting 100000 elements");
    printResultsForArray(arr100k);

    System.out.println("Sorting 500000 elements");
    printResultsForArray(arr500k);

  }

  static void printResultsForArray(int[] arr) {
    // pretty print results in a table
    System.out.println("============================================================");
    System.err.println("============================================================");
    System.out.printf("%-20s ", "Sorting Algorithm");
    for (int i = 0; i < 20; i++)
      System.out.printf("%-20s ", "Iteration " + (i + 1));
    System.out.printf("%-20s\n", "Average Time (HH:MM:SS:ms:ns)");

    System.err.printf("%-20s ", "Sorting Algorithm");
    for (int i = 0; i < 20; i++)
      System.err.printf("%-20s ", "Iteration " + (i + 1));
    System.err.printf("%-20s\n", "Average Time (HH:MM:SS:ms:ns)");

    printResults("Bubble Sort", arr, SortingAlgorithm::bubbleSort);
    printResults("Selection Sort", arr, SortingAlgorithm::selectionSort);
    printResults("Insertion Sort", arr, SortingAlgorithm::insertionSort);
    printResults("Quick Sort", arr, SortingAlgorithm::quickSort);
    printResults("Arrays.sort", arr, Arrays::sort);

    System.out.println("============================================================");
    System.err.println("============================================================");
    System.out.println();
    System.err.println();
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
