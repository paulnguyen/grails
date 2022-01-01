package gumball.v2

class Gumball {

    String modelNumber
    String serialNumber
    Integer countGumballs

    static constraints = {
        serialNumber(unique: true)
    }
}

/*

Initialize DB with:

1. Model Number = M102988
2. Serial Number = 1234998871109
3. Count Gumballs = <Any Inventory Size>


*/