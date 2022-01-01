
## Grails Commands
	 
	grails create-domain-class Gumball
	
	grails create-controller GumballMachine
	grails create-controller GumballSecured
	grails create-controller GumballStateless

	grails run-app
	grails run-app --port=8090
	grails test run-app
	
	grails test-app
	grails test-app -unit
	grails test-app -integration


## Grails H2 DB Console

	http://localhost:8080/h2-console 
	jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
	

## Grails Console

	grails console
	grails test console

	---
	
	import gumball.v2.Gumball 
	
 	def gumball = Gumball.findBySerialNumber( "1234998871109" )
 	println gumball.modelNumber
 	println gumball.countGumballs
 	println gumball.serialNumber



