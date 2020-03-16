package example.grails

import io.micronaut.context.ApplicationContext
import io.micronaut.core.io.socket.SocketUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class LoginSpec extends Specification {

    @Shared
    int serverPort = SocketUtils.findAvailableTcpPort()

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, ['micronaut.server.port': serverPort])

    @Shared
    String masterUsername = 'sherlock'

    @Shared
    String masterPassword = 'elementary'

    @Shared
    String loginPath = '/master/api/login'

    BlockingHttpClient masterClient

    void "call micronaut controller"() {
        given:
        String masterUrl = "http://localhost:$serverPort"
        masterClient = HttpClient.create(new URL(masterUrl)).toBlocking()

        expect:
        embeddedServer.applicationContext.containsBean(LoginController)

        when:
        String token = retrieveAccessToken()

        then:
        noExceptionThrown()

        token == 'xxx.yyy.zz'
    }


    String retrieveAccessToken() {
        Map json = masterClient.retrieve(HttpRequest.POST(loginPath, [username: masterUsername, password: masterPassword]), Map)
        return json.access_token
    }
}
