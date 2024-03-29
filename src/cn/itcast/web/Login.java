package cn.itcast.web;

import cn.itcast.domain.User;
import cn.itcast.service.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String checkCode = (String) request .getSession().getAttribute("CHECKCODE_SERVER");
        request.getSession().removeAttribute("CHECKCODE_SERVER");
        String i_checkCode = request.getParameter("verifycode");
        Map<String, String[]> map = request.getParameterMap();
        User lastUser;
        if(i_checkCode==null||!i_checkCode.equalsIgnoreCase(checkCode)){
            request.setAttribute("error","验证码错误");
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }else {
            User user = new User();
            try {
                BeanUtils.populate(user,map);
                lastUser = new UserServiceImpl().login(user);
                if(lastUser==null){
                    request.setAttribute("error","账号或密码错误");
                    request.getRequestDispatcher("/login.jsp").forward(request,response);
                }else {
                    request.getSession().setAttribute("name",lastUser.getName());
                    response.sendRedirect(request.getContextPath()+"/index.jsp");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }
}
