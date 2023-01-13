package by.dmitryrugol.testtask.rest;

import by.dmitryrugol.testtask.jwt.config.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin()
public class HelloWorldController {

	private final Logger log
			= LogManager.getLogger(this.getClass());

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping({ "/hello" })
	public String hello(HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader("Authorization");
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			String jwtToken = requestTokenHeader.substring(7);
			try {
				Long usrId = jwtTokenUtil.getUserIdFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				log.warn("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				log.warn("JWT Token has expired");
			}
		} else {
			log.warn("Incorrect token format");
		}

		return "Hello World";
	}

}
