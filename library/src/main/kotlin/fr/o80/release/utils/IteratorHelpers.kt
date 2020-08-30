package fr.o80.release.utils

fun <T> Iterator<T>.firstOrNull(): T? = if (hasNext()) next() else null
fun <T> Iterator<T>.takeWhile(predicate: (T) -> Boolean): List<T> {
    val list = mutableListOf<T>()
    while (hasNext()) {
        val next = next()
        if (!predicate(next)) {
            break
        }
        list.add(next)
    }
    return list
}
fun <T> Iterator<T>.reduce(reducer: (T, T) -> T): T? {
    var accumulator: T? = null
    while (hasNext()) {
        val curAcc = accumulator
        accumulator = if (curAcc == null) {
            next()
        } else {
            reducer(curAcc, next())
        }
    }
    return accumulator
}
