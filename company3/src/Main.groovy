//@CompileStatic
class Main {
    static void main(String[] args) {
        int S = 1000, K = 50

        def algos = [Lib.&getKRandNaive, Lib.&getKRandReservoirSampling]

        algos.each { algo ->
            evaluate(getRandomlyFilledMap(S, K, algo), K)
        }

    }

    static def getRandomlyFilledMap(int size, int k, def clos) {
        def map = [:]

        (0..<size).each {
            map[it] = 0
        }
        size.times {
            clos(map.size(), k).each {
                map[it]++
            }
        }

        map
    }

    static def evaluate(Map map, int expect) {
        long sum = 0
        int max, min
        (max, min) = [Integer.MIN_VALUE, Integer.MAX_VALUE]
        map.each {
            sum += (it.value - expect).abs()
            if (it.value > max) max = it.value as int
            if (it.value < min) min = it.value as int
        }

        println "max=${max} min=${min} expect=${expect} среднее отклонение=${sum / map.size()}"
    }
}
