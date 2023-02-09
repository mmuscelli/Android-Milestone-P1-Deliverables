/*
 * Purdue Android Milestone Course - Make Your Own Android App 1
 * Deliverable #1; Asterisk Stair
 * Date: 02/08/2023
 * Author: Miguelangel Muscelli
 */

fun main() {
    var asteriskList: MutableList<String>
    
    for (lineNumber in 1..5) {
        asteriskList = createLine(lineNumber)
        println(asteriskList)
    }

}

fun createLine(lineNumber: Int): MutableList<String> {
    val asteriskList: MutableList<String> = mutableListOf("  ", "  ", "  ", "  ", "  ")
    
    for (i in 1..lineNumber) {
        if (i == 1) {
            asteriskList.set(i - 1, "*")
        } else {
            asteriskList.set(i - 1, " *")
        }
    } 
    
    return asteriskList
}