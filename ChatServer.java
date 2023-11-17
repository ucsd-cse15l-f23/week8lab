import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

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
      }
      else {
        return "Invalid parameters: " + String.join("&", params);
      }
    }
    else if(url.getPath().equals("/kawaii")) {
      String[] params = url.getQuery().split("&");
      String[] shouldBeUser = params[0].split("=");
      String matchingMessages = "";
      if (shouldBeUser[0].equals("user")) {
         for(String line : this.chatHistory.split("\n\n")) {
           if (line.contains(shouldBeUser[1])) {
            int numberOfExclamationMarks = 0;
            int numberOfEmojis = 0;
            for(int character : line.codePoints().toArray()) {
              if(character == (int)'!') {
                numberOfExclamationMarks += 1;
              }
              String[] emojis = { "😂", "🥹"};
              if(Arrays.asList(emojis).contains(new String(Character.toChars(character)))) {
                numberOfEmojis += 1;
              }
            }
            String signature = "";
            if (numberOfExclamationMarks >= 2){
              signature += "(υ◉ω◉υ)";
            }
            if (numberOfEmojis >= 1) {
              signature += "(⁀ᗢ⁀)";
            }
            if (numberOfEmojis >= 2) {
              signature += "٩(◕‿◕)۶";
            }

            matchingMessages += line + signature + "\n\n";
           }
         }

         return matchingMessages;

      }
    }
    return "404 Not Found";
  }
}

class ChatServer {
  public static void main(String[] args) throws IOException {
    Server.start(4000, new ChatHandler());
  }
}
