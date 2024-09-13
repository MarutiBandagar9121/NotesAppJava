package authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Statement;

/**
 * Servlet implementation class Login
 */
@WebServlet(
		urlPatterns = { "/login" }, 
		initParams = {
			@WebInitParam(name = "databaseUrl", value = "jdbc:mysql://localhost:3306/notesappservlet"),
			@WebInitParam(name = "databaseUser", value = "root"),
			@WebInitParam(name = "databasePassword", value = "Maruti@12345")
			})
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection=null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String databaseUrl = getServletConfig().getInitParameter("databaseUrl");
		String databaseUser = getServletConfig().getInitParameter("databaseUser");
		String databasePassword = getServletConfig().getInitParameter("databasePassword");
		System.out.println(databaseUrl);
		System.out.println(databaseUser);
		System.out.println(databasePassword);
    	try {
			connection=DriverManager.getConnection(databaseUrl,databaseUser,databasePassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Executing post method of login servlet.");
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		
		if(username.length()==0 || password.length()==0) {
			out.println("<html><head><title>Notes</title></head><body>");
			out.println("<h3>Fields cant be null.<h3>");
			out.println("<a href='/NotesApp/login.html'>Login page</a>");
			out.println("</body></html>");
			return;
		}
		else if(username.length()<8) {
			out.println("<html><head><title>Notes</title></head><body>");
			out.println("<h3>Username should be atleat of 8 chars<h3>");
			out.println("<a href='/NotesApp/login.html'>Login page</a>");
			out.println("</body></html>");
			return;
		}
		else if(password.length()<8) {
			out.println("<html><head><title>Notes</title></head><body>");
			out.println("<h3>Password should be atleat of 8 chars<h3>");
			out.println("<a href='/NotesApp/login.html'>Login page</a>");
			out.println("</body></html>");
			return;
		}
		else {
			if(connection!=null) {
				String selectQuery="select*from notesappservlet.user";
				 try {
					 Statement stmt = connection.createStatement();
			         ResultSet rs = stmt.executeQuery(selectQuery);
			         while(rs.next()) {
			        	 String tempUserName=rs.getString("username");
			        	 String tempPassword=rs.getString("password");
			        	 if(tempUserName.equals(username)) {
			        		 if(tempPassword.equals(password)) {
			        			out.println("<html><head><title>Notes</title></head><body>");
			     				out.println("<h3>Login Success!!<h3>");
			     				out.println("<a href='/NotesApp/viewnote.jsp'>View All My Notes.</a><br>");
			     				out.println("<a href='/NotesApp/createnote.html'>Create new Note.</a><br>");
			     				out.println("<a href='/NotesApp'>Home</a><br>");
			     				out.println("</body></html>");
			     				return;
			        		 }
			        		 else {
					        		out.println("<html><head><title>Notes</title></head><body>");
					 				out.println("<h3>Invalid Password<h3>");
					 				out.println("<a href='/NotesApp/login.html'>Home</a>");
					 				out.println("</body></html>");
					 				return;
					        	 }
			        	 }
			        	 else {
			        		 continue;
			        	 }
			         }
			        out.println("<html><head><title>Notes</title></head><body>");
		 			out.println("<h3>Invalid Credentials<h3>");
		 			out.println("<a href='/NotesApp/login.html'>Home</a>");
		 			out.println("</body></html>");
			         
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				out.println("<html><head><title>Notes</title></head><body>");
				out.println("<h3>Database Connection Failed<h3>");
				out.println("<a href='/NotesApp'>Home</a>");
				out.println("</body></html>");
			}
		}
		
	}

}
