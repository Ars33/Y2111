/**   
 * Copyright © 2017 公司名. All rights reserved.
 * 
 * @Title: LoginController.java 
 * @Prject: TencentLogin
 * @Package: com.cn.qq.login.controllor 
 * @Description: TODO
 * @author: lenovo   
 * @date: 2017年10月23日 上午10:02:50 
 * @version: V1.0   
 */
package com.cn.qq.login.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

/** 
 * @ClassName: LoginController 
 * @Description: TODO
 * @author: lenovo
 * @date: 2017年10月23日 上午10:02:50  
 */
@Controller
@RequestMapping("qq")
public class LoginController {
	@RequestMapping("do_login.chm")
	public void doLogin(HttpServletRequest request,HttpServletResponse response) throws IOException, QQConnectException 
		{
			//调取用户授权页面
			response.sendRedirect(new Oauth().getAuthorizeURL(request));
		
	}

	@RequestMapping("login")
	@ResponseBody
	public void login(HttpServletRequest request,HttpServletResponse response) throws QQConnectException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//根据Oauth获取Token
		AccessToken accessToken = new Oauth().getAccessTokenByQueryString
				(request.getQueryString(),request.getParameter("state"));
		//从request中获取到Code 来拿去Token
		String token = null;
		//有效期
		long expireIn;
		//获取到的Token
		token = accessToken.getAccessToken();
		//获取
		expireIn = accessToken.getExpireIn();
		//准备获取Open id
		OpenID openIDObj=new OpenID(token);
		//获取open id
		String openid=openIDObj.getUserOpenID();
		
		//获取QQ空间信息
		UserInfo qzone=new UserInfo(token, openID);
		//获取用户对象
		UserInfoBean qzoneUser=qzone.getUserInfo();
		return qzoneUser.toString();
		
	}

}

