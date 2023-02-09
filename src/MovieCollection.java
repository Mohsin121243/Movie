import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;


public class MovieCollection {
  private ArrayList<Movie> movies;
  private Scanner scanner;

  public MovieCollection(String fileName) {
    importMovieList(fileName);
    scanner = new Scanner(System.in);
  }

  public ArrayList<Movie> getMovies() {
    return movies;
  }

  public void menu() {
    String menuOption = "";

    System.out.println("Welcome to the movie collection!");
    System.out.println("Total: " + movies.size() + " movies");

    while (!menuOption.equals("q")) {
      System.out.println("------------ Main Menu ----------");
      System.out.println("- search (t)itles");
      System.out.println("- search (k)eywords");
      System.out.println("- search (c)ast");
      System.out.println("- see all movies of a (g)enre");
      System.out.println("- list top 50 (r)ated movies");
      System.out.println("- list top 50 (h)igest revenue movies");
      System.out.println("- (q)uit");
      System.out.print("Enter choice: ");
      menuOption = scanner.nextLine();

      if (!menuOption.equals("q")) {
        processOption(menuOption);
      }
    }
  }

  private void processOption(String option) {
    if (option.equals("t")) {
      searchTitles();
    } else if (option.equals("c")) {
      searchCast();
    } else if (option.equals("k")) {
      searchKeywords();
    } else if (option.equals("g")) {
      listGenres();
    } else if (option.equals("r")) {
      listHighestRated();
    } else if (option.equals("h")) {
      listHighestRevenue();
    } else {
      System.out.println("Invalid choice!");
    }
  }

  private void searchTitles() {
    System.out.print("Enter a title search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String movieTitle = movies.get(i).getTitle();
      movieTitle = movieTitle.toLowerCase();

      if (movieTitle.indexOf(searchTerm) != -1) {
        //add the Movie objest to the results list
        results.add(movies.get(i));
      }
    }


    // sort the results by title
    sortResults(results);

    // now, display them all to the user    
    for (int i = 0; i < results.size(); i++) {
      String title = results.get(i).getTitle();

      // this will print index 0 as choice 1 in the results list; better for user!
      int choiceNum = i + 1;

      System.out.println("" + choiceNum + ". " + title);
    }


    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    int choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = results.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }


  private void sortResults(ArrayList<Movie> listToSort) {
    for (int j = 1; j < listToSort.size(); j++) {
      Movie temp = listToSort.get(j);
      String tempTitle = temp.getTitle();

      int possibleIndex = j;
      while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
  }

  private void displayMovieInfo(Movie movie) {
    System.out.println();
    System.out.println("Title: " + movie.getTitle());
    System.out.println("Tagline: " + movie.getTagline());
    System.out.println("Runtime: " + movie.getRuntime() + " minutes");
    System.out.println("Year: " + movie.getYear());
    System.out.println("Directed by: " + movie.getDirector());
    System.out.println("Cast: " + movie.getCast());
    System.out.println("Overview: " + movie.getOverview());
    System.out.println("User rating: " + movie.getUserRating());
    System.out.println("Box office revenue: " + movie.getRevenue());
  }

  private void searchCast() {
    System.out.println("Enter search term: ");
    String castSearch = scanner.nextLine();

    ArrayList<String> castMembers = new ArrayList<>();
    for (int i = 0; i < movies.size(); i++) {
      String[] temp = movies.get(i).getCast().split("\\|");
      for (int j = 0; j < temp.length; j++) {
        String castMember = temp[j].toLowerCase();
        if (castMember.contains(castSearch.toLowerCase()) && !castMembers.contains(castMember)) {
          castMembers.add(temp[j]);
        }
      }
    }

    Collections.sort(castMembers);
    for (int i = 0; i < castMembers.size(); i++) {
      for (int j = i + 1; j < castMembers.size(); j++) {
        if (castMembers.get(i).equals(castMembers.get(j))) {
          castMembers.remove(j);
          j--;
        }
      }
    }
    for (int i = 0; i < castMembers.size(); i++) {

      System.out.println((i + 1) + ". " + castMembers.get(i));
    }

    System.out.println("Enter the number of a cast member to see the movies they appear in: ");
    int selectedCastMember = scanner.nextInt();
    scanner.nextLine();
    selectedCastMember--;

    if (selectedCastMember < 0 || selectedCastMember >= castMembers.size()) {
      System.out.println("Invalid selection");
      return;
    }

    ArrayList<String> moviesByCastMember = new ArrayList<>();
    for (int i = 0; i < movies.size(); i++) {
      String[] temp = movies.get(i).getCast().split("\\|");
      for (int j = 0; j < temp.length; j++) {
        if (temp[j].equals(castMembers.get(selectedCastMember))) {
          moviesByCastMember.add(movies.get(i).getTitle());
          break;
        }
      }
    }

    Collections.sort(moviesByCastMember);
    System.out.println("Movies:");
    for (int i = 0; i < moviesByCastMember.size(); i++) {
      System.out.println((i + 1) + ". " + moviesByCastMember.get(i));
    }

    System.out.println("Enter the number of a movie to learn about it: ");
    int selectedMovie = scanner.nextInt();
    scanner.nextLine();
    selectedMovie--;

    if (selectedMovie < 0 || selectedMovie >= moviesByCastMember.size()) {
      System.out.println("Invalid selection");
      return;
    }

    for (int i = 0; i < movies.size(); i++) {
      if (movies.get(i).getTitle().equals(moviesByCastMember.get(selectedMovie))) {
        System.out.println(movies.get(i));
        break;
      }
    }
  }




  private void searchKeywords()
  {
    System.out.print("Enter a keywoard search term: ");
    String keyTerm = scanner.nextLine();

    // prevent case sensitivity
    keyTerm = keyTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++)
    {
      String key = movies.get(i).getKeywords();
      key = key.toLowerCase();

      if (key.indexOf(keyTerm) != -1)
      {
        //add the Movie objest to the results list
        results.add(movies.get(i));
      }
    }


    // sort the results by title
    sortResults(results);

    // now, display them all to the user
    for (int i = 0; i < results.size(); i++)
    {
      String key = results.get(i).getKeywords();

      // this will print index 0 as choice 1 in the results list; better for user!
      int choiceNum = i + 1;

      System.out.println("" + choiceNum + ". " + key);
    }


    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    int choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = results.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }
  
  private void listGenres()
  {

  }
  
  private void listHighestRated()
  {
  
  }
  
  private void listHighestRevenue()
  {
  
  }
  
  private void importMovieList(String fileName)
  {
    try
    {
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = bufferedReader.readLine();
      
      movies = new ArrayList<Movie>();
      
      while ((line = bufferedReader.readLine()) != null) 
      {
        String[] movieFromCSV = line.split(",");
     
        String title = movieFromCSV[0];
        String cast = movieFromCSV[1];
        String director = movieFromCSV[2];
        String tagline = movieFromCSV[3];
        String keywords = movieFromCSV[4];
        String overview = movieFromCSV[5];
        int runtime = Integer.parseInt(movieFromCSV[6]);
        String genres = movieFromCSV[7];
        double userRating = Double.parseDouble(movieFromCSV[8]);
        int year = Integer.parseInt(movieFromCSV[9]);
        int revenue = Integer.parseInt(movieFromCSV[10]);
        
        Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
        movies.add(nextMovie);  
      }
      bufferedReader.close();
    }
    catch(IOException exception)
    {
      // Print out the exception that occurred
      System.out.println("Unable to access " + exception.getMessage());              
    }
  }
}