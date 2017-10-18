package ch02;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HelloServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		try {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head><title>Servlet 연습</title></head>");
			out.println("<body>");
			out.println("처음 작성하는 Servlet");
			out.println("</body>");
			out.println("</html>");
			out.close();
		} catch(Exception e) {
			getServletContext().log("Error in HelloServlet");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
