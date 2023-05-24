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
    // warm up to avoid JVM optimizations
    for (int i = 0; i < 10; i++)
      sortFunction.accept(Arrays.copyOf(arr, arr.length));

    long start = System.nanoTime();
    sortFunction.accept(Arrays.copyOf(arr, arr.length));
    long end = System.nanoTime();
    return Duration.ofNanos(end - start);
  }

  private static void printResults(String sortName, int[] arr, Consumer<int[]> sortFunction) {
    System.out.printf("%-20s ", sortName);
    Duration totalDuration = Duration.ZERO;
    for (int i = 0; i < 20; i++) {
      Duration duration = getTimedResults(arr, sortFunction);
      System.out.printf("%-20s ", formatDuration(duration));
      totalDuration = totalDuration.plus(duration);
    }
    System.out.printf("%-20s\n", formatDuration(totalDuration.dividedBy(20)));
  }

  public static void main(String[] args) throws FileNotFoundException {

    File file = new File("dataset.txt");
    Scanner sc = new Scanner(file);

    List<Integer> list = new ArrayList<Integer>();
    while (sc.hasNextLine()) {
      list.add(Integer.parseInt(sc.nextLine()));
    }

    int[] arr = new int[list.size()];
    for (int i = 0; i < list.size(); i++) {
      arr[i] = list.get(i);
    }

    // pretty print results in a table
    System.out.printf("%-20s ", "Sorting Algorithm");
    for (int i = 0; i < 20; i++)
      System.out.printf("%-20s ", "Iteration " + (i + 1));
    System.out.printf("%-20s\n", "Average Time (HH:MM:SS:ms:ns)");

    printResults("Arrays.sort", arr, Arrays::sort);
    printResults("Merge Sort", arr, SortingAlgorithm::mergeSort);
    printResults("Insertion Sort", arr, SortingAlgorithm::insertionSort);
    printResults("Selection Sort", arr, SortingAlgorithm::selectionSort);
    printResults("Bubble Sort", arr, SortingAlgorithm::bubbleSort);

    sc.close();
  }
}

class SortingAlgorithm {

  public static void bubbleSort(int[] arr) {
    int n = arr.length;
    int temp = 0;

    for (int i = 0; i < n; i++) {
      for (int j = 1; j < (n - i); j++) {

        if (arr[j - 1] > arr[j]) {
          // swap elements
          temp = arr[j - 1];
          arr[j - 1] = arr[j];
          arr[j] = temp;
        }

      }
    }
  }

  public static void insertionSort(int array[]) {
    int n = array.length;
    for (int j = 1; j < n; j++) {
      int key = array[j];
      int i = j - 1;
      while ((i > -1) && (array[i] > key)) {
        array[i + 1] = array[i];
        i--;
      }
      array[i + 1] = key;
    }
  }

  // selection sort
  public static void selectionSort(int[] arr) {
    for (int i = 0; i < arr.length - 1; i++) {
      int index = i;
      for (int j = i + 1; j < arr.length; j++) {
        if (arr[j] < arr[index]) {
          index = j;// searching for lowest index
        }
      }
      int smallerNumber = arr[index];
      arr[index] = arr[i];
      arr[i] = smallerNumber;
    }
  }

  public static void mergeSort(int[] array) {
    mergeSort(array, 0, array.length - 1);
  }

  public static void mergeSort(int[] array, int low, int high) {
    if (high <= low)
      return;

    int mid = (low + high) / 2;
    mergeSort(array, low, mid);
    mergeSort(array, mid + 1, high);
    merge(array, low, mid, high);
  }

  private static void merge(int[] array, int low, int mid, int high) {
    // Creating temporary subarrays
    int leftArray[] = new int[mid - low + 1];
    int rightArray[] = new int[high - mid];

    // Copying our subarrays into temporaries
    for (int i = 0; i < leftArray.length; i++)
      leftArray[i] = array[low + i];
    for (int i = 0; i < rightArray.length; i++)
      rightArray[i] = array[mid + i + 1];

    // Iterators containing current index of temp subarrays
    int leftIndex = 0;
    int rightIndex = 0;

    // Copying from leftArray and rightArray back into array
    for (int i = low; i < high + 1; i++) {
      // If there are still uncopied elements in R and L, copy minimum of the two
      if (leftIndex < leftArray.length && rightIndex < rightArray.length) {
        if (leftArray[leftIndex] < rightArray[rightIndex]) {
          array[i] = leftArray[leftIndex];
          leftIndex++;
        } else {
          array[i] = rightArray[rightIndex];
          rightIndex++;
        }
      } else if (leftIndex < leftArray.length) {
        // If all elements have been copied from rightArray, copy rest of leftArray
        array[i] = leftArray[leftIndex];
        leftIndex++;
      } else if (rightIndex < rightArray.length) {
        // If all elements have been copied from leftArray, copy rest of rightArray
        array[i] = rightArray[rightIndex];
        rightIndex++;
      }
    }
  }
}