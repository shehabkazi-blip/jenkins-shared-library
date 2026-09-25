def call() {
    timeout(time: 5, unit: "MINUTES") {
        def qg = waitForQualityGate()
        if (qg.status != 'OK') {
            error "Pipeline aborted due to Quality Gate failure: ${qg.status}"
        }
    }
}