package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acc = request.getParameter("user_acc");
		String pwd = request.getParameter("user_pwd");
		String name = request.getParameter("user_name");
		User user = new User(acc,pwd,name);
		request.setAttribute("user", user);
		request.getRequestDispatcher("chatRoom.jsp").forward(request, response);
//		request.getRequestDispatcher("/WEB-INF/jsp/websocketChatroom.jsp").forward(request, response);
	}

}
