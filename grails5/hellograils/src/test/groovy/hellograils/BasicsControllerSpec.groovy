package hellograils

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class BasicsControllerSpec extends Specification implements ControllerUnitTest<BasicsController> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
