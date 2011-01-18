package scalate

import scala.collection.mutable.Buffer
import java.io.File
import javax.servlet.ServletContext
import org.fusesource.scalate.{Binding,DefaultRenderContext,TemplateEngine}
import br.com.caelum.vraptor.validator.Validations
import org.fusesource.scalate.servlet.ServletRenderContext._
import scala.collection.JavaConversions._
class Boot(engine: TemplateEngine,context:ServletContext){
	engine.allowCaching = false
	engine.allowReload  = true
	def run: Unit = {
		engine.importStatements  ::= "import scalate.VraptorHelper._"
		engine.importStatements  ::= "import scalate.MyDvdsHelper._"
		engine.importStatements  ::= "import scalate.PimpedJSTLCore"
		engine.importStatements  ::= "import scalate.PimpedInt"
		engine.importStatements  ::= "import scalate.PimpedSet"
		engine.importStatements  ::= "import scalate.PimpedObject"
		engine.importStatements  ::= "import br.com.caelum.vraptor.mydvds.interceptor.UserInfo"
		engine.importStatements  ::= "import br.com.caelum.vraptor.mydvds.model._"			
		engine.importStatements  ::= "import br.com.caelum.vraptor.validator.ValidationMessage"
		engine.importStatements  ::= "import scala.collection.mutable.Buffer"
	}
}

object VraptorHelper{	  
	implicit def string2PimpedJSTLCore(str:String) = new PimpedJSTLCore(str)
	implicit def intToPimpedInt(n:Int) = new PimpedInt(n)
	implicit def bufferToPimpedSet[T](list:java.util.Set[T]) = new PimpedSet[T](list)
	implicit def objectToPimpedObject(obj:java.lang.Object) = new PimpedObject(obj)
}

class PimpedJSTLCore(str:String){	
	def url = request.getServletContext.getContextPath +"/"+ str
}

class PimpedInt(n:Int){
	def zebra = {
		if (n % 2 == 0) "even"
		else "odd"
	}
}

class PimpedSet[T](set:java.util.Set[T]){
	def withCounter = {
		set.zip(0 to set.size)
	}
}

class PimpedObject(obj:java.lang.Object){
	def i18n = MyDvdsHelper.i(obj.toString)
}

object MyDvdsHelper{
	class MyValidations extends Validations{
		override def i18n(key:String) = super.i18n(key)		
	}
	import br.com.caelum.vraptor.mydvds.interceptor.UserInfo
	def loggedUser = {
			request.getSession.getAttribute("userInfo").asInstanceOf[UserInfo] match {
				case null				=> new UserInfo
				case userInfo:UserInfo	=> userInfo
			}
	}
	
	def i(str:String) = new MyValidations().i18n(str)
}
