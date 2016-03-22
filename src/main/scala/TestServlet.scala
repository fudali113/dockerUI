package scala.web

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/scala")
class TestServlet() extends Any {

	@RequestMapping("/test")
	def testScala(request: HttpServletRequest, response: HttpServletResponse ): Unit = {
		println(request.getMethod)
	}

}