package br.com.caelum.vraptor.mydvds.interceptor.scala

import br.com.caelum.vraptor.mydvds.model.User
import br.com.caelum.vraptor.Intercepts
import br.com.caelum.vraptor.mydvds.controller.scala.HomeController
import scala.collection.mutable.Buffer
import br.com.caelum.vraptor.validator.ValidationMessage
import br.com.caelum.vraptor.Result
import br.com.caelum.vraptor.mydvds.dao.UserDao
import br.com.caelum.vraptor.core.InterceptorStack
import br.com.caelum.vraptor.resource.ResourceMethod
import br.com.caelum.vraptor.interceptor.Interceptor
import br.com.caelum.vraptor.mydvds.interceptor.{Public,UserInfo}

@Intercepts
class AuthorizationInterceptor(info:UserInfo,dao:UserDao,result:Result) extends Interceptor{

	def accepts(method:ResourceMethod ) = !method.containsAnnotation(classOf[Public]) 
	
	def intercept(stack:InterceptorStack ,method:ResourceMethod,resourceInstance:Object) {
		info.getUser match {
			case null => {
				result.include("errors", Buffer(new ValidationMessage("user is not logged in", "user")));
				result redirectTo classOf[HomeController] login				
			}
			case user:User => {
				dao refresh info.getUser
				stack next(method, resourceInstance)				
			}
		}
	}
}