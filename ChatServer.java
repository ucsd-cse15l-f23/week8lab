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
      }
      else {
        return "Invalid parameters: " + String.join("&", params);
      }
    }
    return "404 Not Found";
  }
}

class ChatMain {
  public static void main(String[] args) throws IOException {
    Server.start(4000, new ChatHandler());
  }
}
