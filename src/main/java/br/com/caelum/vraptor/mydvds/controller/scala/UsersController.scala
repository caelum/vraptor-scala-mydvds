package br.com.caelum.vraptor.mydvds.controller.scala

import br.com.caelum.vraptor.validator.Validations
import br.com.caelum.vraptor.Resource
import br.com.caelum.vraptor.mydvds.dao.UserDao
import br.com.caelum.vraptor.mydvds.interceptor.UserInfo
import br.com.caelum.vraptor.Result
import br.com.caelum.vraptor.Validator
import br.com.caelum.vraptor.mydvds.model.DvdType
import br.com.caelum.vraptor.Get
import br.com.caelum.vraptor.Post
import br.com.caelum.vraptor.Path
import br.com.caelum.vraptor.mydvds.model.User
import br.com.caelum.vraptor.mydvds.interceptor.Public
import scala.collection.JavaConversions._

@Resource
class UsersController(dao:UserDao, userInfo:UserInfo, result:Result, validator:Validator) {

	@Path(Array("/"))
	@Get
	def home = {
		dao.refresh(userInfo.getUser)
		result.include("dvdTypes",DvdType.values)
	}
	
	@Path(Array("/users"))
	@Post
	@Public	
	def add(user:User) {
		
//		validator checking(new Validations() {{
//				val loginDoesNotExist = !dao.containsUserWithLogin(user.getLogin())
//				that(loginDoesNotExist, "login", "login_already_exists");
//				that(user.getLogin().matches("[a-z0-9_]+"), "login", "invalid_login");				
//		}})		
		dao add user
		result.include("notice", "User " + user.getName + " successfully added")
		result redirectTo classOf[HomeController] login 
	}
	
	@Path(Array("/users"))
	@Get	
	@Public
	def list {
		val users = dao.listAll.map(databaseUser => {
			val user = new User
			user setLogin databaseUser.getLogin			
			user setName databaseUser.getName
			user
		})
		result include("users",users)
		
	}
	
	@Path(Array("/users/{user.login}"))
	@Get	
	@Public
	def view(user:User) = dao find user.getLogin
		
	
}