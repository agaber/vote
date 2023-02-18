package dev.agaber.vote.service.server;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/** Returns a 200 OK status if the server is running. That's it. */
public class HealthzServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    try (var printWriter = response.getWriter()) {
      printWriter.println("<!DOCTYPE html>");
      printWriter.println("<html><body>ok</body></html>");
    }
    response.setStatus(HttpStatus.OK.value());
  }
}
