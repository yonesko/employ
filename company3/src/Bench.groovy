class Bench {
    static void main(String[] args) {
        def R = 10_000, K = 500, N = 100
        def sum = 0

        R.times {
            sum += benchmark { Lib.getKRandReservoirSampling(1000, 500) }
        }

        println "execution took ${sum / R} ms"

        sum = 0

        R.times {
            sum += benchmark { Lib.getKRandNaive(1000, 500) }
        }

        println "execution took ${sum / R} ms"
    }

    static def benchmark(def closure) {
        def start = System.currentTimeMillis()
        closure.call()
        def now = System.currentTimeMillis()
        now - start
    }
}
