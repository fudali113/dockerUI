package scala.web

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping( Array("/scala") )
class TestServlet{

	{
		println("i am scala")
	}

	@RequestMapping( Array("/test") )
	def testScala(request: HttpServletRequest, response: HttpServletResponse ): Unit = {
		println(request.getMethod)
	}

}

object TestServlet{
	def main(args: Array[String]) {
		println("i am scala")
	}
}
