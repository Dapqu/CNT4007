import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static FileOutputStream fileStream;
    private static OutputStreamWriter fileWriter;

    // Creates a log file if it doesn't exist.
    public static void startLogger(String fileName) {
        try {
            File file = new File(fileName);

            if (file.createNewFile()) {
                System.out.println("Log file " + file.getName() + " created.");
            }
            else {
                System.out.println("Log file already exists.");
            }

            fileStream = new FileOutputStream(file);
            fileWriter = new OutputStreamWriter(fileStream);

        } catch (IOException e) {
            System.out.println("Logger starting error occurred.");
            throw new RuntimeException(e);
        }
    }

    // Writes given String to the log file.
    public static void writeLog(String log) {
        try {
            // [Time]: [message]
            fileWriter.write(time() + ": "+ log + '\n');

            // Debug
            System.out.println(time() + ": "+ log);
        } catch (IOException e) {
            System.out.println("Logger writing error occurred.");
            throw new RuntimeException(e);
        }
    }

    // Closes the log file and stop the logging process.
    public static void stopLogger() {
        try {
            fileWriter.flush();
            fileStream.close();
        } catch (IOException e) {
            System.out.println("Logger stopping error occurred.");
            throw new RuntimeException(e);
        }
    }

    // Gets the current date and times and returns it in a readable string.
    private static String time() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}