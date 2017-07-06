package com.sheng.example;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthcLevelFilter extends PassThruAuthenticationFilter {
	@SuppressWarnings("deprecation")
	@Override
	    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
	    HttpServletResponse httpResponse ;
	    HttpServletRequest httpRequest;
	    Map<String, Object> map = new HashMap<String, Object>();
	    httpResponse = WebUtils.toHttp(response); 
	    httpRequest = WebUtils.toHttp(request);

		if(isLoginRequest(httpRequest, httpResponse)){
	    return true;
	    }else {

			WebUtils.saveRequest(request);
			String url = httpRequest.getRequestURI();
			map.put("requestUri", url);
			PrintWriter out=null;
			try {
				httpResponse.setContentType("text/html; charset=utf-8");
				out =  httpResponse.getWriter();
//				if (url.equals("/logList.jsp")||url.equals("/parkManager.jsp")) {
//					httpResponse.setContentType("text/html;charset=utf-8");
//					out.append("<script language='javascript'>parent.location='../index.jsp';</script>");
//				}else if(url.equals("/getParkList")||url.equals("/getLogList_admin")||url.equals("/getLogList_park")){
//					PageData pageData=new PageData();
//					pageData.setData(new ArrayList<>());
//					ReturnData ret = new ReturnData();
//					ret.setMessage(URLEncoder.encode("用户未登录，试图访问！","utf-8"));
//					pageData.setOtherData(ret);
//					httpResponse.setCharacterEncoding("UTF-8");
//					httpResponse.setContentType("application/json; charset=utf-8");
//					out.append(JSONObject.toJSONString(pageData));
//				} else if(url.equals("/park/query_coast")) {
//					ReturnData ret = new ReturnData();
//					ret.setStatus(false);
//					ret.setMessage(URLEncoder.encode("用户未登录，试图访问！","utf-8"));
//					out.append(JSONObject.toJSONString(ret));
//				}else{
//					System.out.println("requestUri:" + httpRequest.getRequestURI());
//					httpResponse.setStatus(200, "redirect");
//					httpResponse.setCharacterEncoding("UTF-8");
//					httpResponse.setContentType("application/json; charset=utf-8");
//
//						ReturnData ret = new ReturnData();
//						ret.setMessage("用户未登录，试图访问！");
//						ret.setData(httpResponse.toString());
						out.append("用户未登录，试图访问！");
//
//
//				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}
				return false;

			}
	    }
	
}
