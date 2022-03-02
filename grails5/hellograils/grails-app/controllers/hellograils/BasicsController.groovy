package hellograils

/*
    Groovy Server Pages (GSP):
    https://gsp.grails.org/latest/guide/index.html

 */


class BasicsController {

    def index() {
        // dump out request object
        request.each { key, value ->
            println( "request: $key = $value")
        }

        // dump out params
        params?.each { key, value ->
            println( "params: $key = $value" )
        }

        if (params.view) {
            render (view: params.view)
        } else {
            render "GSP Basics"
        }

    }

    def hello() {
        render(view: "hello1")
    }
}
