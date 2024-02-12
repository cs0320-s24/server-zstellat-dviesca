package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csvOperations.Exceptions.RestartLoopException;
import java.util.Scanner;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private Main(String[] args) {}

  private void run() {

    Scanner scan = new Scanner(System.in);

    while (true) {
      try {
        System.out.println("Welcome to the CSV parser and searcher!");
        System.out.println("Would you like to parse and search a CSV file? (Yes/No): ");
        String proceedArg = scan.nextLine().trim().toLowerCase();
        if (proceedArg.equals("no")) {
          break;
        }

        System.out.println("Enter the name of the CSV file: ");
        String csvFile = scan.nextLine().trim();

        System.out.println("Does the CSV file contain a row of headers? (Yes/No): ");
        String headerArg = scan.nextLine().trim().toLowerCase();
        boolean containsHeaders = headerArg.equals("yes");

        System.out.println("Enter the value you would like to search for: ");
        String searchValue = scan.nextLine().trim();

        if (containsHeaders) {
          System.out.println("Do you want to specify a column header to search by? (Yes/No): ");
          String columnArg = scan.nextLine().trim().toLowerCase();
          if (columnArg.equals("yes")) {
            System.out.println("Enter the name of the column you would like to search in: ");
            String searchColumn = scan.nextLine().trim();

            // Calls csvFileUtility to perform parse and search
            System.out.println("Parsing and Searching CSV...");
            new csvFileUtility<>(csvFile, containsHeaders, searchValue, searchColumn);
          } else {
            // Calls csvFileUtility to perform parse and search
            System.out.println("Parsing and Searching CSV...");
            new csvFileUtility<>(csvFile, containsHeaders, searchValue);
          }
        } else {
          // Calls csvFileUtility to perform parse and search
          System.out.println("Parsing and Searching CSV...");
          new csvFileUtility<>(csvFile, containsHeaders, searchValue);
        }

      } catch (RestartLoopException e) {
        // BUG: If I make this an err print statement, it prints in a weird order to the console.
        System.out.println("\n+++++++ " + e.getMessage() + " +++++++");
        System.out.println("\nRestarting...");
      }
      System.out.println("------------------------------------------------ \n");
    }
    System.out.println("----- Program exiting -----");
  }
}
