import java.io.IOException;
import java.net.URI;

class ChatHandler implements URLHandler {
  String chatHistory = "";

  public String handleRequest(URI url) {

    // expect /chat?user=<name>&message=<string>
    if (url.getPath().equals("/chat")) {
      String[] params = url.getQuery().split("&");
      String[] shouldBeUser = params[0].split("=");
      String[] shouldBeMessage = params[1].split("=");
      if (shouldBeUser[0].equals("user") && shouldBeMessage[0].equals("message")) {
        String user = shouldBeUser[1];
        String message = shouldBeMessage[1];
        this.chatHistory += user + ": " + message + "\n\n";
        return this.chatHistory;
      } else {
        return "Invalid parameters: " + String.join("&", params);
      }
    }
    // expect /semantic-analysis?user=<name>
    else if (url.getPath().equals("/semantic-analysis")) {
      String[] params = url.getQuery().split("&");
      String[] shouldBeUser = params[0].split("=");
      String matchingMessages = "";
      if (shouldBeUser[0].equals("user")) {
        String[] chatHistoryArr = this.chatHistory.split("\n\n");
        int index = 0;
        while (index < chatHistoryArr.length) {
          String line = chatHistoryArr[index];
          int numberOfExclamationMarks = 0;
          String analysis = "";
          index += 1;
          int[] codePoints = new int[0]; // initialize the codePoints array
          if (line.contains(shouldBeUser[1])) // { //bug1: will cause inclusion of all messages
            codePoints = line.codePoints().toArray();
          int characterIndex = 0;
          while (characterIndex < codePoints.length) {
            int character = codePoints[characterIndex];
            if (character == (int) '!') {
              numberOfExclamationMarks += 1;
            }
            if (new String(Character.toChars(character)).equals("😂")) {
              analysis = " This message has a LOL vibe."; // bug2: should be +=
            }
            if (new String(Character.toChars(character)).equals("🥹")) {
              analysis = " This message has a awwww vibe."; // bug2: should be +=
            } else {
              characterIndex += 1; // bug3: this should not be in an else statement
            }
          }
          if (numberOfExclamationMarks > 2) {
            analysis += " This message ends forcefully.";
          }
          matchingMessages += line + analysis + "\n\n";
          // }
        }
      }
      return matchingMessages;
    }
    else if(url.getPath().equals("/retreive-history")) {
      String[] params = url.getQuery().split("&");
      String[] shouldBeFile = params[0].split("=");
      if (shouldBeFile[0].equals("file")) {
        String fileName = shouldBeFile[1];
        // String fileName = shouldBeFileName[0];
        ChatHistoryReader reader = new ChatHistoryReader();
        try {
          String[] contents = reader.readFileAsArray("chathistory/" + fileName);
          for (String line : contents) {
            this.chatHistory += line + "\n\n";
          }
        } catch (IOException e) {
          System.err.println("Error reading file: " + e.getMessage());
        }
      }
      return this.chatHistory;
    }
    return "404 Not Found";
  }
}

class ChatServer {
  public static void main(String[] args) throws IOException {
    int port = Integer.parseInt(args[0]);
    System.out.println(port);
    Server.start(port, new ChatHandler());
  }
}
