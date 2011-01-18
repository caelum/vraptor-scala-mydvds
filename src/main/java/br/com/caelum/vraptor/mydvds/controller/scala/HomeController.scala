package br.com.caelum.vraptor.mydvds.controller.scala

import br.com.caelum.vraptor.mydvds.model.User
import br.com.caelum.vraptor.{Get, Resource, Result, Validator, Post}
import br.com.caelum.vraptor.mydvds.dao.UserDao
import br.com.caelum.vraptor.mydvds.interceptor.{Public, UserInfo}

import br.com.caelum.vraptor.validator.Validations
import org.hamcrest.Matchers._

@Resource
class HomeController(dao:UserDao, userInfo:UserInfo, result:Result, validator:Validator) {
	
	@Public
	@Get
	def login{}
		
	@Post
	@Public
	def login(login:String,password:String) {
		val currentUser = dao find(login,password)
		validator checking(new Validations() {{
				that(currentUser, is(notNullValue[User]), "login", "invalid_login_or_password")
		}})
		userInfo login currentUser
		result redirectTo classOf[UsersController] home		
	}
	
	def logout {
		userInfo.logout
		result redirectTo this login
	}
	
	
}