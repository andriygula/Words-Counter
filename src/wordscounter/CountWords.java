package wordscounter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author agula
 */
public class CountWords {
    public static void main( String... args) {
        
        if( args == null || args.length != 2) {
            System.err.println( "Usage: \r\n java wordscounter.CountWords <file path> <words count>");
            System.exit( 1);
        }
        
        String filePath = args[ 0];

        if( !Files.exists( Paths.get( filePath))) {
            System.err.println( "File specified was not found, pelas echeck input parameters");
            System.exit( 1);
        }

        int wordsCount = 0;
        
        try {
            wordsCount = Integer.parseInt( args[ 1]);
        } catch( Exception e) {
            System.err.println( "Invalid words count");
            System.exit( 1);
        }

        try {
            String content = readFile( filePath);
            
            String[] allWords = content.split( "\\W");
            
            Map<String, WordCounter> wordsMap = new HashMap<>( allWords.length / 3);
            
            for( String word : allWords) {
                if( word.length() == 0) {
                    continue;
                }
                
                word = word.toLowerCase();
                
                WordCounter wordDetails = wordsMap.get( word);
                
                if( wordDetails == null) {
                    wordDetails = new WordCounter( word);
                    wordsMap.put( word, wordDetails);
                    continue;
                }

                wordDetails.incrementCount();
            }

            TreeSet<WordCounter> ordered = new TreeSet<>();

            wordsMap.entrySet().stream().forEach( e -> ordered.add( e.getValue()));
            
            for( int i = 0; i < Math.min( wordsCount, ordered.size()); i++) {
                WordCounter wordDetails = ordered.pollLast(); 
                System.out.println( wordDetails.getWord() + " " + wordDetails.getCount());
            }
        } catch (IOException e) {
            System.err.println( "Error reading file: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Assume file is in ASCII encoding
     */
    public static String readFile( String path) throws IOException {
        try ( BufferedReader br = new BufferedReader( new FileReader( path))) {
            StringBuilder fileContent = new StringBuilder(); 
    
            char[] buff = new char[ 5000];
    
            int n;
            while( (n = br.read( buff)) != -1) {
                fileContent.append( new String( buff, 0, n));
            }
    
            return fileContent.toString();
        }
    }
    
    static class WordCounter implements Comparable<WordCounter> {
        String word;
        int count;
        
        WordCounter( String word) {
            this.word = word;
            count = 1;
        }

        int getCount() {
            return count;
        }

        String getWord() {
            return word;
        }

        int incrementCount() {
            return ++count;
        }

        @Override
        public int compareTo( WordCounter other) {
            int res = Integer.compare( count, other.getCount());
            return res != 0 ? res : other.getWord().compareTo( word);
        }
    }
}
