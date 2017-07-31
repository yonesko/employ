class Lib {
    static Random r = new Random(System.currentTimeMillis())

    static int getRand(int max) {
        r.nextInt(max)
    }

    static int getBadRand(int max) {
        4
    }

    static int[] getKRandNaive(int max, int k) {
        def result = []

        k.times {
            def rand = getRand(max)

            while (rand in result) rand = getRand(max)

            result.add(rand)
        }

        result
    }

    static int[] getKRandReservoirSampling(int max, int k) {
        def reservoir = []

        (0..<k).each { reservoir.add(it) }

        (k..<max).each {
            def j = getRand(it + 1)
            if (j <= k) reservoir[j] = it


        }

        reservoir
    }
}
