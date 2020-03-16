package example.grails

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/master/api")
class LoginController {
    @Post("/login")
    Map index() {
        [access_token: 'xxx.yyy.zz']
    }
}
